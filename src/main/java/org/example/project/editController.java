package org.example.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class editController {

    @FXML
    private ToggleGroup TypeGroup;

    @FXML
    private TextField amount;

    @FXML
    private Button cancel;

    @FXML
    private TextArea comment;

    @FXML
    private DatePicker date;

    @FXML
    private RadioButton expend;

    @FXML
    private RadioButton income;

    @FXML
    private Button submit;

    @FXML
    private TextField titleField;

    private Data currentData;

    private int currentId;

    public void initData(Data data) {
        this.currentData = data;
        this.titleField.setText(data.getTitle());
        this.amount.setText(String.valueOf(data.getAmount()));
        this.comment.setText(data.getComment());
        if ("Income".equals(data.getType())) {
            income.setSelected(true);
        } else if ("Expend".equals(data.getType())) {
            expend.setSelected(true);
        }
        this.date.setValue(data.getTime().toLocalDateTime().toLocalDate());
        this.currentId = data.getId();
    }
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
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public interface OnSubmitDone{
        void onEditSubmit();
    }
    private OnSubmitDone onSubmitDoneCallback;

    public void setOnSubmitDoneCallback(OnSubmitDone callback) {
        this.onSubmitDoneCallback = callback;
    }
    @FXML
    void editAction(ActionEvent event) {
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
        Backend backend = new Backend();
        backend.edit(currentId, selectedType, titleText, timestamp, amount, commentText);
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        if (onSubmitDoneCallback != null) {
            onSubmitDoneCallback.onEditSubmit();
        }
    }
}
