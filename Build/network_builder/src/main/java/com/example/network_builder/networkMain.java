package com.example.network_builder;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class networkMain extends Application {
    @Override
    public void start(Stage stage)
    {
        //set scene
        var scene = new Scene(new Pane());

        //sets up viewhandler to switch between fxml files at runtime
        ViewHandler.setScene(scene);
        ViewHandler.switchScene(View.MAINMENU);

        //configures stage, sets application image (just a goofy lil guy)
        stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("chrom.png")));
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}