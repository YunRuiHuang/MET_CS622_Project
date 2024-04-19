package org.example.project;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class commentController {

    @FXML
    private Label commentText;

    @FXML
    private Label titleText;
    private Data currentData;

    public void initData(Data data) {
        titleText.setAlignment(Pos.CENTER);
        commentText.setAlignment(Pos.TOP_LEFT);
        this.currentData = data;
        this.titleText.setText(data.getTitle());
        this.commentText.setText(data.getComment());
    }
}
