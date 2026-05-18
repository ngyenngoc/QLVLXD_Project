package client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class HomeController {

    @FXML
    private Label lblWelcome;

    @FXML
    public void initialize() {

        String myName = "Admin";

        if (myName != null && !myName.isEmpty()) {
            lblWelcome.setText("Xin chào " + myName + ", chào mừng bạn quay trở lại hệ thống!");
        }
    }
}