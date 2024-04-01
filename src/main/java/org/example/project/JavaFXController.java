package org.example.project;

import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;

import java.io.IOException;

public class JavaFXController {
    @FXML
    private DatePicker Etime;

    @FXML
    private DatePicker Stime;

    @FXML
    private Button clean;

    @FXML
    private MenuItem expend;

    @FXML
    private MenuItem income;

    @FXML
    private Pagination page;

    @FXML
    private Button query;

    @FXML
    private SplitMenuButton type;

    @FXML
    void addAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("addInformation.fxml"));
            Parent root = loader.load(); // Load the FXML file
            Stage stage = new Stage();
            stage.setHeight(350);
            stage.setWidth(500);
            stage.setTitle("Add Information");
            Scene scene = new Scene(root); // Create a scene with the loaded FXML
            stage.setResizable(false);
            stage.setScene(scene); // Set the scene for the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void cleanAction(ActionEvent event) {
        type.setText("");
        Stime.setValue(null);
        Etime.setValue(null);
    }

    @FXML
    void queryAction(ActionEvent event) {
        System.out.println("queryAction");
    }

    @FXML
    void typeAction(ActionEvent event) {
        MenuItem sourceItem = (MenuItem) event.getSource();
        type.setText(sourceItem.getText());
    }
}