package server.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    // Mở cổng mạng 9000
    private static final int PORT = 9000;
    // Tạo một ThreadPool (Đội ngũ nhân viên) có thể phục vụ tối đa 10 Client cùng lúc
    private static ExecutorService pool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("=========================================");
            System.out.println("🚀 SERVER ĐÃ KHỞI ĐỘNG TRÊN CỔNG " + PORT);
            System.out.println("Đang chờ Client kết nối...");
            System.out.println("=========================================");

            while (true) {
                // Đứng chặn ở đây chờ Client gọi tới
                Socket clientSocket = serverSocket.accept();
                System.out.println("🟢 Có Client mới kết nối từ IP: " + clientSocket.getInetAddress());

                // Giao Client này cho một nhân viên (ClientHandler) xử lý, người gác cổng quay lại đợi người tiếp theo
                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Lỗi khởi động Server: " + e.getMessage());
        }
    }
}