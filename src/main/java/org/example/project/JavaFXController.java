package org.example.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.*;
import java.util.Optional;

public class JavaFXController {
    private static final int PER_PAGE = 7;

    @FXML
    private DatePicker Etime;

    @FXML
    private DatePicker Stime;

    @FXML
    private Button clean;

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
    private TableColumn<Data, Void> moreColumn;

    @FXML
    private TableColumn<Data, Integer> idColumn;

    @FXML
    private Label incomeText;

    @FXML
    private Label expendText;

    @FXML
    private Label totalText;

    private void setupMoreColumn() {
        moreColumn.setCellFactory(new Callback<TableColumn<Data, Void>, TableCell<Data, Void>>() {
            @Override
            public TableCell<Data, Void> call(final TableColumn<Data, Void> param) {
                final TableCell<Data, Void> cell = new TableCell<Data, Void>() {

                    private final Button editBtn = new Button();
                    private final Button infoBtn = new Button();
                    private final Button deleteBtn = new Button();
                    private final HBox hbox = new HBox(8, editBtn, infoBtn, deleteBtn);

                    {
                        hbox.setAlignment(Pos.CENTER);
                        editBtn.getStyleClass().add("editbutton-cell");
                        infoBtn.getStyleClass().add("infobutton-cell");
                        deleteBtn.getStyleClass().add("deletebutton-cell");

                        editBtn.setTooltip(new Tooltip("Edit"));
                        infoBtn.setTooltip(new Tooltip("Info"));
                        deleteBtn.setTooltip(new Tooltip("Delete"));

                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Data data = getTableRow().getItem();
                            int id = data.getId();
                            editBtn.setOnAction(event -> editAction(data));
                            infoBtn.setOnAction(event -> infoAction(data));
                            deleteBtn.setOnAction(event -> deleteAction(id));
                            setGraphic(hbox);
                        }
                    }
                };
                return cell;
            }
        });
    }
    private void queryAndUpdateTableView(String type, Timestamp startDate, Timestamp endDate){
        tableView.setFixedCellSize(25);
        setupMoreColumn();
        Backend backend = new Backend();
        Data[] data = backend.query(type,startDate,endDate);
        Summary summary = backend.summary();
        incomeText.setAlignment(Pos.CENTER);
        expendText.setAlignment(Pos.CENTER);
        totalText.setAlignment(Pos.CENTER);
        String incomeFormatted = String.format("%.2f",summary.getIn());
        String expendFormatted = String.format("%.2f",summary.getOut());
        String totalFormatted = String.format("%.2f",summary.getTotal());
        incomeText.setText(incomeFormatted);
        expendText.setText(expendFormatted);
        totalText.setText(totalFormatted);
//        for (int i = 0; i < data.length; i++) {
//            System.out.println(data[i]);
//        }
//        System.out.println(summary);
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
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setVisible(false);
        queryAndUpdateTableView(null, null, null);
    }

    private void refreshData() {
        String selectedType = type.getText();
        String queryType = "type".equals(selectedType) ? null : selectedType;
        LocalDate startDate = Stime.getValue();
        LocalDate endDate = Etime.getValue();
        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atStartOfDay();
            Timestamp startTime = Timestamp.valueOf(startDateTime);
            Timestamp endTime = Timestamp.valueOf(endDateTime);
            queryAndUpdateTableView(queryType, startTime, endTime);
        } else {
            queryAndUpdateTableView(null, null, null); // No dates selected, query all
        }
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

    private void editAction(Data data) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("editInformation.fxml"));
            Parent root = loader.load(); // Load the FXML file

            editController editController = loader.getController();
            editController.initData(data);
            editController.setOnSubmitDoneCallback(()->{
                refreshData();
            });
            Stage stage = new Stage();
            stage.setHeight(350);
            stage.setWidth(500);
            stage.setTitle("Edit Information");
            Scene scene = new Scene(root); // Create a scene with the loaded FXML
            stage.setResizable(false);
            stage.setScene(scene); // Set the scene for the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 实现编辑逻辑
    }

    private void deleteAction(int id) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to delete this line of bills");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> response = alert.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.YES) {
            Backend backend = new Backend();
            backend.delete(id);
            refreshData();
        }
        // 实现删除逻辑
    }

    private void infoAction(Data data) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("commentInformation.fxml"));
            Parent root = loader.load(); // Load the FXML file

            commentController commentController = loader.getController();
            commentController.initData(data);
            Stage stage = new Stage();
            stage.setHeight(350);
            stage.setWidth(500);
            stage.setTitle("Comment Information");
            Scene scene = new Scene(root); // Create a scene with the loaded FXML
            stage.setResizable(false);
            stage.setScene(scene); // Set the scene for the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 实现信息查看逻辑
    }

    @FXML
    void summaryAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("summaryInformation.fxml"));
            Parent root = loader.load(); // Load the FXML file
            Stage stage = new Stage();
            stage.setHeight(350);
            stage.setWidth(500);
            stage.setTitle("Summary Information");
            Scene scene = new Scene(root); // Create a scene with the loaded FXML
            stage.setResizable(false);
            stage.setScene(scene); // Set the scene for the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}