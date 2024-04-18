package org.example.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class addController {

    @FXML
    private ToggleGroup TypeGroup;

    @FXML
    private TextField amount;

    @FXML
    private Button cancel;

    @FXML
    private TextArea comment;

    @FXML
    private Button submit;

    @FXML
    private DatePicker date;

    @FXML
    private TextField titleField;

    public void initialize() {
        // 添加监听器，确保TextField只能输入数字
        amount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*([.]\\d{0,2})?")) {
                amount.setText(oldValue);
            }
        });
    }
    @FXML
    void cancelAction(ActionEvent event) {
        System.out.println("cancel");
    }

    @FXML
    void submitAction(ActionEvent event) {
        Toggle selectedToggle = TypeGroup.getSelectedToggle();
        String selectedType = selectedToggle.getUserData().toString();
        String titleText = titleField.getText();
        String amountText = amount.getText();
        String commentText = comment.getText();
        LocalDate selectedDate = date.getValue();

        if(selectedType==null ||titleText ==null||amountText==null||commentText==null||selectedDate==null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            alert.setContentText("The input information is incomplete!");
            alert.showAndWait();
            return;
        }

        LocalTime time = LocalTime.now();
        LocalTime timeToMinute = time.withNano(0);
        LocalDateTime dateTime = LocalDateTime.of(selectedDate, timeToMinute);
        Timestamp timestamp = Timestamp.valueOf(dateTime);
        double amount = Double.parseDouble(amountText);
        System.out.println(selectedType);
        System.out.println(titleText);
        System.out.println(timestamp);
        System.out.println(amount);
        System.out.println(commentText);
        Backend backend = new Backend();
        backend.add(selectedType, titleText, timestamp, amount, commentText);
    }
}
