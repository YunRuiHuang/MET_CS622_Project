package org.example.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
public class addController {
    @FXML
    private Button cancel;

    @FXML
    private Button submit;

    @FXML
    void cancelAction(ActionEvent event) {
        System.out.println("cancel");
    }

    @FXML
    void submitAction(ActionEvent event) {

    }
}
