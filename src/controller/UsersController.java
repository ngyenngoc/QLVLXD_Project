package controller;

import dao.UsersDAO;
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
import model.Users;

public class UsersController {
    @FXML
    private TextField txtUserName;
    @FXML private PasswordField txtPassWord;
    @FXML private Button btnLogin;
    private final UsersDAO userDAO = new UsersDAO();
    @FXML private void handleLogin(ActionEvent event){
        String userName = txtUserName.getText();
        String passWord = txtPassWord.getText();

        if (userName.isEmpty() || passWord.isEmpty()){
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        Users user = userDAO.login(userName, passWord);
        if (user != null){
            openMainWindow();
            closeLoginWindow();
        } else {
            showAlert("Thất bại", "Sai tên đăng nhập hoặc mật khẩu");
        }

    }
    private void openMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/MainWindow.fxml")
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Hệ thống quản lý vật liệu xây dựng");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở giao diện chính!");
        }
    }

    private void closeLoginWindow() {
        Stage stage = (Stage) txtUserName.getScene().getWindow();
        stage.close();
    }

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
}
}