package client.service;

import shared.Request;
import shared.Response;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient {
    private static final String SERVER_HOST = "localhost"; // Chạy trên cùng máy tính
    private static final int SERVER_PORT = 9000;           // Cổng cổng mạng của Server

    /**
     * Hàm trung tâm gửi dữ liệu lên Server và chờ kết quả trả về
     */
    public static Response sendRequest(Request request) {
        // Mở kết nối Socket thẳng tới Server
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // 1. Đẩy gói tin Request đi
            out.writeObject(request);
            out.flush();

            // 2. Đứng đợi nhận gói tin Response trả về từ Server
            return (Response) in.readObject();

        } catch (Exception e) {
            System.err.println(" Không thể kết nối tới Server! Hãy chắc chắn Server đã được bật.");
            return new Response(false, "Lỗi kết nối mạng: " + e.getMessage(), null);
        }
    }
}