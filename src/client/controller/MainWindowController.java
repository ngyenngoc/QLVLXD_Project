package client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;

public class MainWindowController {

    @FXML
    private StackPane centerContent;
    @FXML private Button btnDashboard;
    @FXML private Button btnMaterialGroup;
    @FXML private Button btnCustomer;
    @FXML private Button btnOrder;
    @FXML private Button btnSupplier;
    @FXML private Button btnChat; // Nút Chat nội bộ
    @FXML private Button btnHome;

    @FXML private javafx.scene.layout.VBox subMenuMaterial;
    @FXML private Button btnMaterial; // Menu con 1
    @FXML private Button btnCategory; // Menu con 2

    @FXML
    public void initialize() {
        // Mặc định hiện trang chủ hoặc trang khi vừa mở máy
        handleShowHome(null);
    }
    private void setActiveButton(Button clickedButton) {
        Button[] allButtons = {btnDashboard, btnMaterialGroup, btnCustomer, btnOrder, btnSupplier,btnHome, btnDashboard, btnChat, btnMaterial, btnCategory};

        for (Button btn : allButtons) {
            if (btn != null) {
                btn.getStyleClass().remove("menu-button-active");
            }
        }

        if (clickedButton != null) {
            clickedButton.getStyleClass().add("menu-button-active");
        }
    }


    @FXML
    private void handleShowCustomer(ActionEvent event) {
        setActiveButton(btnCustomer);
        try {
            // Cách viết này an toàn hơn để tìm file trong Resource Folders
            URL fxmlLocation = getClass().getResource("/client/view/CustomerView.fxml");

            if (fxmlLocation == null) {
                throw new IOException("Không tìm thấy file CustomerView.fxml tại thư mục client.view!");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent customerView = loader.load();

            if (centerContent != null) {
                centerContent.getChildren().setAll( customerView);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Lỗi nạp giao diện : " + e.getMessage());
        }
    }

    @FXML
    private void handleShowMaterial(ActionEvent event) {
        setActiveButton(btnMaterial);
        try {
            // Cách viết này an toàn hơn để tìm file trong Resource Folders
            URL fxmlLocation = getClass().getResource("/client/view/MaterialView.fxml");

            if (fxmlLocation == null) {
                throw new IOException("Không tìm thấy file MaterialView.fxml tại thư mục client.view!");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent materialView = loader.load();

            if (centerContent != null) {
                centerContent.getChildren().setAll(materialView);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Lỗi nạp giao diện : " + e.getMessage());
        }
    }

     @FXML
    private void handleShowCategory(ActionEvent event) {
         setActiveButton(btnCategory);
        try {
            // Cách viết này an toàn hơn để tìm file trong Resource Folders
            URL fxmlLocation = getClass().getResource("/client/view/CategoryView.fxml");


            if (fxmlLocation == null) {
                throw new IOException("Không tìm thấy file MaterialView.fxml tại thư mục client.view!");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent materialView = loader.load();

            if (centerContent != null) {
                centerContent.getChildren().setAll(materialView);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Lỗi nạp giao diện : " + e.getMessage());
        }
    }
    @FXML
    private void handleShowSupplier(ActionEvent event) {
        setActiveButton(btnSupplier);
        try {
            // Cách viết này an toàn hơn để tìm file trong Resource Folders
            URL fxmlLocation = getClass().getResource("/client/view/SupplierView.fxml");

            if (fxmlLocation == null) {
                throw new IOException("Không tìm thấy file SupplierView.fxml tại thư mục client.view!");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent materialView = loader.load();

            if (centerContent != null) {
                centerContent.getChildren().setAll(materialView);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Lỗi nạp giao diện : " + e.getMessage());
        }
    }
    @FXML
    private void handleShowUsers(ActionEvent event) {
        try {
            // Cách viết này an toàn hơn để tìm file trong Resource Folders
            URL fxmlLocation = getClass().getResource("/client/view/UsersView.fxml");

            if (fxmlLocation == null) {
                throw new IOException("Không tìm thấy file UsersView.fxml tại thư mục client.view!");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent materialView = loader.load();

            if (centerContent != null) {
                centerContent.getChildren().setAll(materialView);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Lỗi nạp giao diện : " + e.getMessage());
        }
    }

    @FXML
    private void handleShowOrders(ActionEvent event) {
        setActiveButton(btnOrder);
        try {
            // Cách viết này an toàn hơn để tìm file trong Resource Folders
            URL fxmlLocation = getClass().getResource("/client/view/OrdersView.fxml");


            if (fxmlLocation == null) {
                throw new IOException("Không tìm thấy file OrdersView.fxml tại thư mục client.view!");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent materialView = loader.load();

            if (centerContent != null) {
                centerContent.getChildren().setAll(materialView);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Lỗi nạp giao diện : " + e.getMessage());
        }
    }
    @FXML
    private void handleShowSalesOrder(ActionEvent event) {
        setActiveButton(btnOrder);
        try {
            // Cách viết này an toàn hơn để tìm file trong Resource Folders
            URL fxmlLocation = getClass().getResource("/client/view/SalesOrderView.fxml");


            if (fxmlLocation == null) {
                throw new IOException("Không tìm thấy file SalesOrderView.fxml tại thư mục client.view!");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent materialView = loader.load();

            if (centerContent != null) {
                centerContent.getChildren().setAll(materialView);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Lỗi nạp giao diện : " + e.getMessage());
        }
    }


    @FXML
    public void toggleMaterialMenu(javafx.event.ActionEvent event) {
        setActiveButton(btnMaterialGroup);
        if (subMenuMaterial != null) {
            boolean isVisible = subMenuMaterial.isVisible();
            subMenuMaterial.setVisible(!isVisible);
            subMenuMaterial.setManaged(!isVisible);
        }
    }

    @FXML
    private void handleShowDashboard(ActionEvent event) {
        setActiveButton(btnDashboard);
        try {
            // Cách viết này an toàn hơn để tìm file trong Resource Folders
            URL fxmlLocation = getClass().getResource("/client/view/DashboardView.fxml");


            if (fxmlLocation == null) {
                throw new IOException("Không tìm thấy file DashboardView.fxml tại thư mục client.view!");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent dashboardView = loader.load();

            if (centerContent != null) {
                centerContent.getChildren().setAll(dashboardView);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Lỗi nạp giao diện : " + e.getMessage());
        }
    }
    @FXML
    private void handleOpenChat() {
        setActiveButton(btnChat);
        try {
            // Tải file giao diện Chat từ thư mục view
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/client/view/ChatView.fxml"));
            javafx.scene.Parent root = loader.load();

            // Tạo một cửa sổ (Stage) mới
            javafx.stage.Stage chatStage = new javafx.stage.Stage();
            chatStage.setTitle("💬 Kênh trao đổi nội bộ");

            // Thiết lập kích thước vừa vặn cho khung chat
            chatStage.setScene(new javafx.scene.Scene(root, 400, 500));

            // Không cho phép phóng to toàn màn hình để giữ form đẹp
            chatStage.setResizable(false);

            // Hiển thị cửa sổ song song với màn hình chính
            chatStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            // Nếu bạn có sẵn hàm showAlert trong Controller này thì dùng, không thì dùng Alert mặc định
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Không thể mở cửa sổ Chat nội bộ! Vui lòng kiểm tra lại đường dẫn file FXML.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleShowHome(ActionEvent event) {
        setActiveButton(btnHome);
        try {
            URL fxmlLocation = getClass().getResource("/client/view/HomeView.fxml");

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent homeView = loader.load();

            centerContent.getChildren().setAll(homeView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }
}