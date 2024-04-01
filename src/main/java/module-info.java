module org.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.project to javafx.fxml;
    exports org.example.project;
}