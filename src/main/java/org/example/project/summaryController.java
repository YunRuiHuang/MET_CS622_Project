package org.example.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class summaryController {

    @FXML
    private TableColumn<?, ?> expendColumn1;

    @FXML
    private TableColumn<?, ?> expendColumn2;

    @FXML
    private Label expendText;

    @FXML
    private TableColumn<?, ?> incomeColumn1;

    @FXML
    private TableColumn<?, ?> incomeColumn2;

    @FXML
    private Label incomeText;

    @FXML
    private TableColumn<MonthData, String> monthColumn1;

    @FXML
    private TableColumn<MonthData, String> monthColumn2;

    @FXML
    private SplitMenuButton selectYear;

    @FXML
    private TableView<MonthData> tableView1;

    @FXML
    private TableView<MonthData> tableView2;

    @FXML
    private TableColumn<?, ?> totalColumn1;

    @FXML
    private TableColumn<?, ?> totalColumn2;

    @FXML
    private Label totalText;

    public class MonthData{
        private String monthName;
        private double income;
        private double expend;
        private double total;

        public MonthData(String monthName, double income, double expend, double total){

            this.monthName = monthName;
            this.income = income;
            this.expend = expend;
            this.total = total;
        }
        public String getMonthName() {
            return monthName;
        }

        public void setMonthName(String monthName) {
            this.monthName = monthName;
        }
        public double getIncome() {
            return income;
        }

        public void setIncome(double income) {
            this.income = income;
        }
        public double getExpend() {
            return expend;
        }

        public void setExpend(double expend) {
            this.expend = expend;
        }
        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

    }
    public void initialize(){
        Backend backend = new Backend();
        Data[] data = backend.query(null,null,null);
        Set<Integer> years = new HashSet<>();
        for (Data datum : data) {
            int year = datum.getTime().toLocalDateTime().getYear();  // 假设getTime返回的是Timestamp
            years.add(year);
        }
        selectYear.getItems().clear();
        for (Integer year : years) {
            MenuItem item = new MenuItem(year.toString());
            item.setOnAction(event -> {
                handleYearSelection(year);
                selectYear.setText((year.toString()));
            });  // 给每个菜单项设置一个事件处理器
            selectYear.getItems().add(item);
        }
        tableView1.setFixedCellSize(35);
        tableView2.setFixedCellSize(35);
        monthColumn1.setCellValueFactory(new PropertyValueFactory<>("monthName"));
        monthColumn2.setCellValueFactory(new PropertyValueFactory<>("monthName"));
        incomeColumn1.setCellValueFactory(new PropertyValueFactory<>("income"));
        incomeColumn2.setCellValueFactory(new PropertyValueFactory<>("income"));
        expendColumn1.setCellValueFactory(new PropertyValueFactory<>("expend"));
        expendColumn2.setCellValueFactory(new PropertyValueFactory<>("expend"));
        totalColumn1.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalColumn2.setCellValueFactory(new PropertyValueFactory<>("total"));
        fillMonthData();
    }

    private void fillMonthData(){
        ObservableList<MonthData> month1 = FXCollections.observableArrayList();
        ObservableList<MonthData> month2 = FXCollections.observableArrayList();
        for (int i = 1; i <=12; i++) {
            String monthNames = Month.of(i).getDisplayName(TextStyle.SHORT,Locale.US);
            MonthData monthData = new MonthData(monthNames, 0,0,0);
            if (i <= 6) { // First half (January to June)
                month1.add(monthData);
            } else { // Second half (July to December)
                month2.add(monthData);
            }
        }

        tableView1.setItems(month1);
        tableView2.setItems(month2);
    }

    private void handleYearSelection(int year) {
        // 处理选择特定年份时的逻辑
        Backend backend = new Backend();
        Summary yearSummary = backend.summaryByYear(year);
        String incomeFormatted = String.format("%.2f",yearSummary.getIn());
        String expendFormatted = String.format("%.2f",yearSummary.getOut());
        String totalFormatted = String.format("%.2f",yearSummary.getTotal());
        incomeText.setText(incomeFormatted);
        expendText.setText(expendFormatted);
        totalText.setText(totalFormatted);
        updateData(year);
    }

    private void updateData(int year){
        Backend backend = new Backend();
        ObservableList<MonthData> month1 = tableView1.getItems();
        ObservableList<MonthData> month2 = tableView2.getItems();
        for (int i = 1; i <=12; i++) {
            Summary monthSummary = backend.summaryByMonth(year,i);
            MonthData monthData = new MonthData(
                    Month.of(i).getDisplayName(TextStyle.SHORT, Locale.US),
                    monthSummary.getIn(),
                    monthSummary.getOut(),
                    monthSummary.getTotal()
            );
            if (i <= 6) {
                month1.set(i - 1, monthData);
            } else {
                month2.set(i - 7, monthData);
            }
        }
        tableView1.refresh();
        tableView2.refresh();
    }
}
