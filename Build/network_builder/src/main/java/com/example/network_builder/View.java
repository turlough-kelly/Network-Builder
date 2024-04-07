package com.example.network_builder;

//this class sets the enums for each page
//to make it easier for viewHandler
public enum View
{
    MAINMENU("main_menu.fxml"),
    CLOS("clos_setup.fxml"),
    SLEPIAN("slepian_setup.fxml"),
    INFO("information.fxml"),
    ACKNOW("acknowledgements.fxml"),
    GRAPH("diagram.fxml"),
    GRAPH_INFO("graph_info.fxml"),
    EQUATIONS("equations.fxml");


    //methods to set and get file names
    private String fileName;

    View(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }
}
