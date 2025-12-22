// File: controller/MainWindowController.java
package controller;

import javafx.fxml.FXML;
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
    private final ProductController productController = new ProductController();

    @FXML
    public void initialize() {
        // Tải View Quản Lý Vật Liệu ngay khi khởi động
        handleShowProduct(null);
    }

    @FXML
    private void handleShowProduct(ActionEvent event) {
        System.out.println("Loading Product Management Interface ...");

        // 1. Lấy Root Node (Pane) từ VatLieuController
        Pane productView = productController.getProductManagementView();

        // 2. Xóa nội dung cũ và thêm View mới vào StackPane
        centerContent.getChildren().clear();
        centerContent.getChildren().add(productView);

        // Đảm bảo View được căn chỉnh và tự co dãn
        StackPane.setAlignment(productView, javafx.geometry.Pos.CENTER);
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }
}