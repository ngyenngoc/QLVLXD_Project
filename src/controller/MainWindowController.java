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
    private final SupplierController supplierController = new SupplierController();

    @FXML
    public void initialize() {
        // Tải View Quản Lý Vật Liệu ngay khi khởi động
        handleShowSupplier(null);
    }

    @FXML
    private void handleShowSupplier(ActionEvent event) {
        System.out.println("Loading Supplier Management Interface ...");

        // 1. Lấy Root Node (Pane) từ VatLieuController
        Pane supplierView = supplierController.getSupplierManagementView();

        // 2. Xóa nội dung cũ và thêm View mới vào StackPane
        centerContent.getChildren().clear();
        centerContent.getChildren().add(supplierView);

        // Đảm bảo View được căn chỉnh và tự co dãn
        StackPane.setAlignment(supplierView, javafx.geometry.Pos.CENTER);
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }
}