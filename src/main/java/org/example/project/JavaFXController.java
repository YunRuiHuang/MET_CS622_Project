package org.example.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.*;

public class JavaFXController {
    private static final int PER_PAGE = 7;

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
    private TableView<Data> tableView;

    @FXML
    private TableColumn<Data, String> titleColumn;

    @FXML
    private TableColumn<Data, String> typeColumn;

    @FXML
    private TableColumn<Data, ?> amountColumn;

    @FXML
    private TableColumn<Data, String> timeColumn;

    @FXML
    private TableColumn<?, ?> moreColum;

    private void queryAndUpdateTableView(String type, Timestamp startDate, Timestamp endDate){
        Backend backend = new Backend();
        Data[] data = backend.query(type,startDate,endDate);
//        for (int i = 0; i < data.length; i++) {
//            System.out.println(data[i]);
//        }
        ObservableList<Data> dataList = FXCollections.observableArrayList(data);

        int totalPages = (int) Math.ceil(dataList.size()/(double) PER_PAGE);
        page.setPageCount(totalPages);
        page.setCurrentPageIndex(0);
        page.setMaxPageIndicatorCount(10);
        page.currentPageIndexProperty().addListener((obs,oldIndex, newIndex)->updateTableView(dataList, newIndex.intValue()));

        updateTableView(dataList, 0);
    }

    private void updateTableView(ObservableList<Data> dataList, int pageIndex){
        int fromIndex = pageIndex * PER_PAGE;
        int toIndex =  Math.min(fromIndex + PER_PAGE, dataList.size());
        if (fromIndex <= toIndex) { // 添加检查以确保fromIndex不大于toIndex
            tableView.setItems(FXCollections.observableArrayList(dataList.subList(fromIndex, toIndex)));
        } else {
            tableView.setItems(FXCollections.observableArrayList()); // 如果范围无效，则设置为空列表
        }
    }
    @FXML
    private void initialize(){
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        queryAndUpdateTableView(null, null, null);
    }

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
        type.setText("type");
        Stime.setValue(null);
        Etime.setValue(null);
        initialize();
    }

    @FXML
    void queryAction(ActionEvent event) {
        String selectedType = type.getText();
        String type = "type".equals(selectedType)? null : selectedType;
        LocalDate startDate = Stime.getValue();
        LocalDate endDate = Etime.getValue();
        if(startDate==null || endDate==null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
           alert.setContentText("Must select start date and end date");
           alert.showAndWait();
           return;
        }
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay();
        Timestamp startTime = Timestamp.valueOf(startDateTime);
        Timestamp endTime = Timestamp.valueOf(endDateTime);
        queryAndUpdateTableView(type,startTime,endTime);


    }

    @FXML
    void typeAction(ActionEvent event) {
        MenuItem sourceItem = (MenuItem) event.getSource();
        type.setText(sourceItem.getText());
    }
}