package com.example.network_builder;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.IntegerStringConverter;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class networkController {

    //private variables to be used locally
    private final clos_methods clos = new clos_methods();
    private final slepian_methods slepian = new slepian_methods();

    //static variables as they are used in diagramControler
    public static double[] inputs = null;
    public static boolean checkBoxValue;

    //variables to log execution time
    long startTime = 0, endTime = 0;
    static long elapsedTime = 0;


    //private fields for input to configure networks
    @FXML
    private Slider clos_slider = new Slider();

    @FXML
    private TextField clos_input = new TextField();

    @FXML
    private CheckBox clos_opt = new CheckBox();

    @FXML
    private Slider slepian_slider = new Slider();

    @FXML
    private TextField slepian_input = new TextField();

    @FXML
    private TextField slepian_switch = new TextField();

    @FXML
    private CheckBox slepian_opt = new CheckBox();

    //used if error is thrown
    @FXML
    private Label input_error;

    //handles submission of inputs on the "clos_setup.fxml" page
    @FXML
    private void handleClosSubmit()
    {
        //gets values from input fields
        int sliderValue = (int) clos_slider.getValue();
        String textFieldValue = clos_input.getText();
        checkBoxValue = clos_opt.isSelected();

        //throws visual errors in case of invalid input
        if (!textFieldValue.isEmpty()) {
            try {
                int number = Integer.parseInt(textFieldValue);
                if (number < 3) {
                    // Handle invalid input (number less than or equal to 3)
                    input_error.setText("Value is too low!");
                    return;
                }
            } catch (NumberFormatException e) {
                // Handle invalid input (not a valid number)
                input_error.setText("This input is not valid.");
                return;
            }
        } else {
            // Handle empty input
            input_error.setText("This input is not valid.");
            return;
        }

        //records execution time of method during execution
        startTime = System.currentTimeMillis();
        inputs = clos.X_Enu(sliderValue, Integer.parseInt(textFieldValue), checkBoxValue);
        endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime;



        //make it so it stores all middle stage info
        //clears allstages arraylist in case it was used previously
        //thus preventing overlap if configuring multiple switches
        diagramController.allStages.clear();
        diagramController.n = 0;

        //swaps to graph
        onGraphClick();
    }

    @FXML
    private void handleSlepianSubmit()
    {
        //gets input
        int sliderValue = (int) slepian_slider.getValue();
        String textFieldValue = slepian_input.getText();
        String switchValue = slepian_switch.getText();
        checkBoxValue = slepian_opt.isSelected();

        // Validate input, see above method
        if (!textFieldValue.isEmpty()) {
            try {
                int number = Integer.parseInt(textFieldValue);
                if (number < 3) {
                    // Handle invalid input (number less than or equal to 3)
                    input_error.setText("Value is too low!");
                    return;
                }
            } catch (NumberFormatException e) {
                // Handle invalid input (not a valid number)
                input_error.setText("This input is not valid.");
                return;
            }
        } else {
            // Handle empty input
            input_error.setText("This input is not valid.");
            return;
        }

        //variable to record input of switch (if any)
        int switchval;

        //set switchval if there is an input, otherwise set to 0
        if(switchValue.isEmpty())
        {
            switchval = 0;
        }

        else
        {
            switchval = Integer.parseInt(switchValue);
        }

        //record time during execution
        startTime = System.currentTimeMillis();
        inputs = slepian.slepian_Enu(sliderValue, Integer.parseInt(textFieldValue), switchval, checkBoxValue);
        endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime;


        //clears allstages if used previously
        diagramController.allStages.clear();
        diagramController.n = 0;
        onGraphClick();
    }

    //methods to switch between scenes
    @FXML
    protected void onClosClick() {ViewHandler.switchScene(View.CLOS);}

    @FXML
    protected void onSlepianClick() {
        ViewHandler.switchScene(View.SLEPIAN);
    }

    @FXML
    protected void onAcknowClick() {
        ViewHandler.switchScene(View.ACKNOW);
    }

    @FXML
    protected void onInfoClick() {
        ViewHandler.switchScene(View.INFO);
    }

    @FXML
    protected void onReturnClick() {
        ViewHandler.switchScene(View.MAINMENU);
    }

    @FXML
    protected void onGraphClick() {
        ViewHandler.switchScene(View.GRAPH);
    }

    //runs when application starts
    @FXML
    private void initialize()
    {
        //prevents invalid inputs (ie anything that is not a numeric input)
        Pattern pattern = Pattern.compile("\\d*"); // Matches zero or more digits
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (pattern.matcher(change.getControlNewText()).matches()) {
                return change;
            } else {
                return null; //rejects
            }
        };

        //sets above to the textfields used in setup pages (1 in clos, 2 in slepian)
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), null, filter);
        TextFormatter<Integer> textFormatter2 = new TextFormatter<>(new IntegerStringConverter(), null, filter);
        TextFormatter<Integer> textFormatter3 = new TextFormatter<>(new IntegerStringConverter(), null, filter);
        clos_input.setTextFormatter(textFormatter);
        slepian_input.setTextFormatter(textFormatter2);
        slepian_switch.setTextFormatter(textFormatter3);
    }

    //switches to diagram view
    @FXML
    protected void viewDiagram() {
        ViewHandler.switchScene(View.GRAPH);
    }


}