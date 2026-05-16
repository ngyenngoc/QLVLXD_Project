package client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;

public class MainWindowController {

    @FXML
    private StackPane centerContent;

    @FXML
    public void initialize() {
        // Mặc định hiện trang chủ hoặc trang khi vừa mở máy
    }

    @FXML
    private void handleShowCustomer(ActionEvent event) {
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
    private javafx.scene.layout.VBox subMenuMaterial;

    @FXML
    public void toggleMaterialMenu(javafx.event.ActionEvent event) {
        if (subMenuMaterial != null) {
            boolean isVisible = subMenuMaterial.isVisible();
            subMenuMaterial.setVisible(!isVisible);
            subMenuMaterial.setManaged(!isVisible);
        }
    }

    @FXML
    private void handleShowDashboard(ActionEvent event) {
        try {
            // Cách viết này an toàn hơn để tìm file trong Resource Folders
            URL fxmlLocation = getClass().getResource("/client/view/DashboardView.fxml");


            if (fxmlLocation == null) {
                throw new IOException("Không tìm thấy file DashboardlView.fxml tại thư mục client.view!");
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
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }
}