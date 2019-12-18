/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Duc
 */
public class StageDraggable {

    public static double x = 0, y = 0;
    public static Stage stage;
    
    public static void makeDraggable(AnchorPane form) {
        form.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        form.setOnMouseDragged(event -> {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX()- x);
            stage.setY(event.getScreenY() - y);
        });
    }
    
    
}
