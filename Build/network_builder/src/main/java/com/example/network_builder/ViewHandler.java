package com.example.network_builder;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Duration;
import java.io.IOException;

//a class to handle swapping between views set in the View class
public class ViewHandler
{
    private static Scene scene;

    //sets scene created above
    public static void setScene(Scene scene)
    {
        ViewHandler.scene = scene;
    }

    //switches scene, using the view to determine what scene is to be switched to
    //uses a fade in/fade out transition to make it look nicer and not as jarring
    public static void switchScene(View view)
    {
        try {
            Parent root = FXMLLoader.load(ViewHandler.class.getResource(view.getFileName()));

            // Set the opacity of the root node of the next scene to 0
            root.setOpacity(0.0);

            //sets the transtion to half a second
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), scene.getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event -> {
                scene.setRoot(root);
                // Apply fade in transition on new scene
                FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
