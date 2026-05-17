package client.controller;

import client.service.SocketClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import shared.Request;
import shared.Response;
import shared.model.Users;

public class UsersController {
    @FXML private TextField txtUserName;
    @FXML private PasswordField txtPassWord;
    @FXML private Button btnLogin;

    @FXML
    private void handleLogin(ActionEvent event) {
        String userName = txtUserName.getText().trim();
        String passWord = txtPassWord.getText().trim();

        if (userName.isEmpty() || passWord.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin tài khoản!");
            return;
        }

        // Đóng gói thông tin đăng nhập tạm thời vào đối tượng đại diện Users (ID tạm để là 0)
        Users loginPayload = new Users(0, userName, passWord);

        // ĐÃ SỬA: Gửi gói tin yêu cầu xác thực tài khoản qua mạng Socket lên Server
        Response response = SocketClient.sendRequest(new Request("LOGIN", loginPayload));

        if (response.isSuccess()) {
            // Đăng nhập thành công, có thể lấy đối tượng User thật về nếu cần quản lý phiên làm việc
            Users loggedInUser = (Users) response.getData();

            openMainWindow();
            closeLoginWindow();
        } else {
            // Thất bại do sai tài khoản hoặc mật khẩu thông báo từ Server
            showAlert("Thất bại", response.getMessage());
        }
    }

    private void openMainWindow() {
        try {
            // ĐÃ SỬA: Trỏ chính xác vào đường dẫn tuyệt đối mới trong Resource package
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/client/view/MainWindow.fxml")
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Hệ thống quản lý vật liệu xây dựng - Đại học Công nghệ");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở giao diện chính! Chi tiết: " + e.getMessage());
        }
    }

    private void closeLoginWindow() {
        Stage stage = (Stage) txtUserName.getScene().getWindow();
        stage.close();
    }

    // Hàm hiện thông báo
    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}