// File: controller/MainWindowController.java
package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;

public class MainWindowController {

    @FXML
    private MenuItem menuMaterial;

    @FXML
    private StackPane centerContent;

    // Giữ một instance của Controller con
    private final DonHangNhapController dhnController = new DonHangNhapController();

    @FXML
    public void initialize() {
        // Tải View Quản Lý Vật Liệu ngay khi khởi động
        handleShowDHN(null);
    }

    @FXML
    private void handleShowDHN(ActionEvent event) {
        System.out.println("Tải giao diện Quản Lý Đơn Hàng Nhập...");

        // 1. Lấy Root Node (Pane) từ VatLieuController
        Pane dhnView = dhnController.getDHNManagementView();

        // 2. Xóa nội dung cũ và thêm View mới vào StackPane
        centerContent.getChildren().clear();
        centerContent.getChildren().add(dhnView);

        // Đảm bảo View được căn chỉnh và tự co dãn
        StackPane.setAlignment(dhnView, javafx.geometry.Pos.CENTER);
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }
}