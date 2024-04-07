package com.example.network_builder;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.util.ArrayList;

//a separate controller for the diagram fxml file, since the other one kicked up a major fuss if used with everything
public class diagramController
{
    //static and private variables to be used
    public static int n = 0;

    //labels of the diagram, which will be set by the results of whatever methods are used
    @FXML
    private Label s11, s12, s13, s21, s22, s23, s31, s32, s33, input, output,
                  s1n3, s2n3, s3n3, in1, in2, in3, out1, out2, out3,
                  stages, cp_count, r_value, n_value, in_value, ex_time, stage_view;

    //variables for menu transitions, buttons to view inner stages
    @FXML
    private VBox menu;
    private TranslateTransition menuTransition;
    @FXML
    private VBox menu1;
    private TranslateTransition menuTransition1;

    @FXML
    private Button middle = new Button();

    @FXML
    private Button stageReturn = new Button();

    //private instances of the clos and slepian methods for local use
    //as well as an array to store all information regarding inner networks of a larger network
    private final clos_methods clos = new clos_methods();
    private final slepian_methods slep = new slepian_methods();

    public static ArrayList<double[]> allStages = new ArrayList<>();


    //even though this does nothing, if it isn't here the entire program breaks :/
    public diagramController()
    {

    }

    //runs when loading the diagram for the first time
    @FXML
    private void initialize()
    {
        //sets up side menu transitions
        menuTransition = new TranslateTransition(Duration.millis(100), menu);
        menuTransition1 = new TranslateTransition(Duration.millis(100), menu1);
        menuTransition.setToX(-200); // Slide the menu out initially
        menuTransition1.setToX(200); // Slide the menu out initially
        menuTransition.play();
        menuTransition1.play();
        // Update UI with initial values
        double[] values = networkController.inputs;

        //clos methods will always produce an array with 6 values, and slepian 5, so this is used to ensure
        //that the correct set up methods are called
        switch (values.length) {
            case 6 -> {
                setUpClos(networkController.inputs);
                allStages.add(networkController.inputs);
                createClosStages();
            }
            case 5 -> {
                setUpSlepian(networkController.inputs);
                allStages.add(networkController.inputs);
                createSlepStages();
            }
        }
    }

    //a method that takes the input of a double array full of values from the clos methods and sets them up on the diagram
    private void setUpClos(double[] diagramInputs) {
        if (diagramInputs == null) {
            return; //just in case
        }

        //strings for labels that will have repeating information so its easier to set
        String stage1 = (int)diagramInputs[4] + " x " + (int)diagramInputs[5];
        String stage2 = (int)diagramInputs[3] + " x " + (int)diagramInputs[3];
        String stage3 = (int)diagramInputs[5] + " x " + (int)diagramInputs[4];
        String stage1switch = String.valueOf((int)diagramInputs[3]);
        String nvalue = String.valueOf((int)diagramInputs[4]);
        String stage2switch = String.valueOf((int)diagramInputs[5]);
        String view = "Currently viewing: Stages " + (n + 1) + " - " + (((int)networkController.inputs[1] - n));

        //if the diagram is only 3 stages, removes the middle button and sets the middle stage values
        if((int)diagramInputs[1] == 3)
        {
            middle.setVisible(false);
            stageReturn.setVisible(false);
            s22.setText(stage2);
        }

        //if more than 3 stages, places the button to view inner networks
        else
        {
            middle.setVisible(true);
        }

        //sets stage return button if digram is greater than 3 stages
        if(n > 0 && n != (int)diagramInputs[1] - (n + 2))
        {
            stageReturn.setVisible(true);
        }



        //sets text for top stage view, input and output
        stage_view.setText(view);
        input.setText("Inputs\n" + (int)diagramInputs[0]);
        output.setText("Outputs\n" + (int)diagramInputs[0]);

        //sets values for each stage
        s11.setText(stage1);
        s12.setText(stage1);
        s13.setText(stage1);

        s21.setText(stage2);
        s23.setText(stage2);

        s31.setText(stage3);
        s32.setText(stage3);
        s33.setText(stage3);

        s1n3.setText(stage1switch);
        s2n3.setText(stage2switch);
        s3n3.setText(stage1switch);

        //sets inputs/outputs for stages 1/3
        in1.setText(nvalue);
        in2.setText(nvalue);
        in3.setText(nvalue);

        out1.setText(nvalue);
        out2.setText(nvalue);
        out3.setText(nvalue);

        //set information on side panel
        stages.setText("Total Number of Stages: " + (int)diagramInputs[1]);
        cp_count.setText("Total number of Crosspoints: " + (int)diagramInputs[2]);
        r_value.setText("Optimum value for R: " + (int)diagramInputs[3]);
        n_value.setText("Optimum value for N: " + (int)diagramInputs[4]);
        in_value.setText("Total number of inputs: " + (int)diagramInputs[0]);
        ex_time.setText("Execution Time: " + networkController.elapsedTime + "ms");
    }

    //almost identical as above, its just easier to have as a separate method for clarity when reading the code
    private void setUpSlepian(double[] diagramInputs)
    {
        if (diagramInputs == null) {
            return; // Values not set yet
        }
        String stage1and3 = (int)diagramInputs[4] + " x " + (int)diagramInputs[4];
        String stage2 = (int)diagramInputs[3] + " x " + (int)diagramInputs[3];
        String stage1switch = String.valueOf((int)diagramInputs[3]);
        String nvalue = String.valueOf((int)diagramInputs[4]);
        String stage2switch = String.valueOf((int)diagramInputs[4]);
        String view = "Currently viewing: Stages " + (n + 1) + " - " + (((int)networkController.inputs[1] - n));

        if((int)diagramInputs[1] == 3)
        {
            middle.setVisible(false);
            stageReturn.setVisible(false);
            s22.setText(stage2);
        }
        else
        {
            middle.setVisible(true);
        }

        if(n > 0 && n != (int)diagramInputs[1] - (n + 2))
        {
            stageReturn.setVisible(true);
        }

        input.setText("Inputs\n" + (int)diagramInputs[0]);
        output.setText("Outputs\n" + (int)diagramInputs[0]);

        stage_view.setText(view);

        s11.setText(stage1and3);
        s12.setText(stage1and3);
        s13.setText(stage1and3);

        s21.setText(stage2);
        s23.setText(stage2);

        s31.setText(stage1and3);
        s32.setText(stage1and3);
        s33.setText(stage1and3);

        s1n3.setText(stage1switch);
        s2n3.setText(stage2switch);
        s3n3.setText(stage1switch);

        in1.setText(nvalue);
        in2.setText(nvalue);
        in3.setText(nvalue);

        out1.setText(nvalue);
        out2.setText(nvalue);
        out3.setText(nvalue);

        stages.setText("Total Number of Stages: " + (int)diagramInputs[1]);
        cp_count.setText("Total number of Crosspoints: " + (int)diagramInputs[2]);
        r_value.setText("Optimum value for R: " + (int)diagramInputs[4]);
        n_value.setText("Optimum value for N: " + (int)diagramInputs[3]);
        in_value.setText("Total number of inputs: " + (int)diagramInputs[0]);
        ex_time.setText("Execution Time: " + networkController.elapsedTime + "ms");
    }

    //method to handle opening/closing the side menus
    @FXML
    private void toggleMenu() {
        if (menuTransition.getStatus() == TranslateTransition.Status.RUNNING) {
            return; // Animation is already in progress, ignore
        }

        if (menu.getTranslateX() != 0) {
            // Menu is hidden, slide it in
            menuTransition.setToX(0);
            menuTransition1.setToX(0);
        } else {
            // Menu is visible, slide it out
            menuTransition.setToX(-200);
            menuTransition1.setToX(200);
        }
        //"plays" the transition, ie it slides them out
        menuTransition.play();
        menuTransition1.play();
    }

    //methods to make sure the buttons do the thing they're supposed to
    @FXML
    protected void onReturnClick() {
        ViewHandler.switchScene(View.MAINMENU);
    }

    @FXML
    protected void onDiagramInfoClick() {
        ViewHandler.switchScene(View.GRAPH_INFO);
    }

    @FXML
    protected void viewEquations() {
        ViewHandler.switchScene(View.EQUATIONS);
    }

    //if the diagram is larger than 3 stages, this method swaps the labels to the inner stage(s) so you can view each
    //network in the full network
    @FXML
    protected void showMiddleStage()
    {
        switch(networkController.inputs.length)
        {
            //like above, checks size of array to decide if it is clos or slepian
            case 6:
                if(n != allStages.size())
                {
                    //increments n to keep track of position
                    //then sets labels and hides/shows middle stage values if necessary
                    n++;
                    setUpClos(allStages.get(n));
                    if(allStages.get(n)[1] == 3)
                    {
                        s22.setVisible(true);
                    }
                }
                break;

                //same as above, this is for slepian
            case 5:
                if(n != allStages.size())
                {
                    n++;
                    setUpSlepian(allStages.get(n));
                    if(allStages.get(n)[1] == 3)
                    {
                        s22.setVisible(true);
                    }

                }
                break;
        }
    }

    //very similar to above method, except it works in reverse, so it just goes backwards
    //decrementing n as it does so
    @FXML
    protected void returnToPreviousStage()
    {
        switch(networkController.inputs.length)
        {
            case 6:
                    if(n > 0)
                    {
                        n--;
                        setUpClos(allStages.get(n));
                        s22.setVisible(false);
                        break;
                    }

            case 5:
                    if(n > 0)
                    {
                        n--;
                        setUpSlepian(allStages.get(n));
                        s22.setVisible(false);
                        break;
                    }
        }
    }

    //a method to create all the inner stages of the clos network using the originally calculated network as a base
    protected void createClosStages()
    {
        //sets the limit of the floor of half the stage count since thats how many networks are there
        //IE for a 7 stage network there are 3 total networks: the large 7 stage, the smaller 5 stage, and the even smaller 3 stage
        //with half of 7 being 3.5 and the floor of that being 3, it works every time
        int lim = (int) Math.floor(networkController.inputs[1] / 2);
        for(int i = 1; i < lim; i++)
        {
            //adds the calculation to the overall arraylist above
            allStages.add(clos.X_Enu((int)allStages.get(i - 1)[1] - 2, (int)allStages.get(i - 1)[3], networkController.checkBoxValue));
        }
    }

    //same as above, just for slepian
    //has its own method for clarity
    protected void createSlepStages()
    {
        int lim = (int) Math.floor(networkController.inputs[1] / 2);
        for(int i = 0; i < lim; i++)
        {
            allStages.add(slep.slepian_Enu((int)allStages.get(i)[1] - 2, (int)allStages.get(i)[3], (int)allStages.get(i)[4], networkController.checkBoxValue));
        }
    }

}
