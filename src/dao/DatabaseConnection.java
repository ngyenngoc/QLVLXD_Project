package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
    public static Connection getConnection() {
        // 1. THÔNG TIN ĐĂNG NHẬP
        String user = "sa";
        String password = "040227"; // <== HÃY ĐIỀN MẬT KHẨU CỦA BẠN VÀO ĐÂY

        // 2. CHUỖI KẾT NỐI (Đúng tên DB QLVLXD_OOP bạn đã tạo)
        String url = "jdbc:sqlserver://localhost:1433;" +
                "databaseName=QLVLXD_OOP;" +
                "encrypt=true;" +
                "trustServerCertificate=true;";

        try {
            // Nạp Driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Lỗi kết nối chi tiết: " + e.getMessage());
            return null;
        }
    }

    // ĐÂY LÀ HÀM ĐỂ BẠN NHẤN NÚT CHẠY (Nút Play sẽ hiện ở đây)
    public static void main(String[] args) {
        System.out.println("--- ĐANG KIỂM TRA KẾT NỐI SQL SERVER ---");

        Connection conn = getConnection();

        if (conn != null) {
            System.out.println("=> KẾT QUẢ: Chúc mừng! Kết nối THÀNH CÔNG.");
            try {
                conn.close(); // Đóng kết nối sau khi test xong
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("=> KẾT QUẢ: Kết nối THẤT BẠI.");
            System.out.println("Lưu ý: Kiểm tra xem Port 1433 đã bật chưa và mật khẩu 'sa' đúng chưa.");
        }
    }
}