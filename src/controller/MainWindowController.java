package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
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
        handleShowCustomer(null);
    }

    @FXML
    private void handleShowCustomer(ActionEvent event) {
        try {
            // Cách viết này an toàn hơn để tìm file trong Resource Folders
            URL fxmlLocation = getClass().getResource("/view/CustomerView.fxml");

            if (fxmlLocation == null) {
                // Nếu vẫn không tìm thấy, thử tìm tương đối từ controller
                fxmlLocation = getClass().getResource("../view/CustomerView.fxml");
            }

            if (fxmlLocation == null) {
                throw new IOException("Không tìm thấy file MaterialView.fxml tại thư mục view!");
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
    private void handleShowMaterial(ActionEvent event) {
        try {
            // Cách viết này an toàn hơn để tìm file trong Resource Folders
            URL fxmlLocation = getClass().getResource("/view/MaterialView.fxml");

            if (fxmlLocation == null) {
                // Nếu vẫn không tìm thấy, thử tìm tương đối từ controller
                fxmlLocation = getClass().getResource("../view/MaterialView.fxml");
            }

            if (fxmlLocation == null) {
                throw new IOException("Không tìm thấy file MaterialView.fxml tại thư mục view!");
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
            URL fxmlLocation = getClass().getResource("/view/CategoryView.fxml");

            if (fxmlLocation == null) {
                // Nếu vẫn không tìm thấy, thử tìm tương đối từ controller
                fxmlLocation = getClass().getResource("../view/CategoryView.fxml");
            }

            if (fxmlLocation == null) {
                throw new IOException("Không tìm thấy file MaterialView.fxml tại thư mục view!");
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
            URL fxmlLocation = getClass().getResource("/view/SupplierView.fxml");

            if (fxmlLocation == null) {
                // Nếu vẫn không tìm thấy, thử tìm tương đối từ controller
                fxmlLocation = getClass().getResource("../view/SupplierView.fxml");
            }

            if (fxmlLocation == null) {
                throw new IOException("Không tìm thấy file MaterialView.fxml tại thư mục view!");
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
            URL fxmlLocation = getClass().getResource("/view/UsersView.fxml");

            if (fxmlLocation == null) {
                // Nếu vẫn không tìm thấy, thử tìm tương đối từ controller
                fxmlLocation = getClass().getResource("../view/UsersView.fxml");
            }

            if (fxmlLocation == null) {
                throw new IOException("Không tìm thấy file UsersView.fxml tại thư mục view!");
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
            URL fxmlLocation = getClass().getResource("/view/OrdersView.fxml");

            if (fxmlLocation == null) {
                // Nếu vẫn không tìm thấy, thử tìm tương đối từ controller
                fxmlLocation = getClass().getResource("../view/OrdersView.fxml");
            }

            if (fxmlLocation == null) {
                throw new IOException("Không tìm thấy file MaterialView.fxml tại thư mục view!");
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
            URL fxmlLocation = getClass().getResource("/view/SalesOrderView.fxml");

            if (fxmlLocation == null) {
                // Nếu vẫn không tìm thấy, thử tìm tương đối từ controller
                fxmlLocation = getClass().getResource("../view/SalesOrderView.fxml");
            }

            if (fxmlLocation == null) {
                throw new IOException("Không tìm thấy file MaterialView.fxml tại thư mục view!");
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
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }
}