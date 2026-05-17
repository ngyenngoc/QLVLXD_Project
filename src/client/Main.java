package client;// File: client.Main.java
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    // Tên file FXML chính
    private static final String FXML_FILE = "MainWindow.fxml";

    @Override
    public void start(Stage stage) throws IOException {
        // Tải FXML từ package 'client.view'
        // Lưu ý: Cấu trúc này giả định file nằm trong thư mục client.view/
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/client/view/UsersView.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1000, 700); // Kích thước lớn hơn cho đồ án

        stage.setTitle("Quản Lý Vật Liệu Xây Dựng");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}