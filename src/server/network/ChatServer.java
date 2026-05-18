package server.network;

import shared.model.ChatMessage;
import server.dao.MessageDAO;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
    private static final int CHAT_PORT = 9001;

    // Thay đổi quan trọng: Lưu danh bạ trực tuyến (Tên tài khoản -> Đường ống gửi tin)
    private static final Map<String, ObjectOutputStream> onlineUsers = new HashMap<>();
    private final MessageDAO messageDAO = new MessageDAO();

    public void startChatServer() {
        try (ServerSocket serverSocket = new ServerSocket(CHAT_PORT)) {
            System.out.println("💬 [Chat Server 1-1] Đang mở cổng " + CHAT_PORT + " chờ kết nối...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ChatHandler(clientSocket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ChatHandler extends Thread {
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        private String currentUsername; // Lưu tên người đang kết nối ở luồng này

        public ChatHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                // Lệnh đầu tiên Client gửi lên bắt buộc phải là Tên của người đăng nhập
                currentUsername = (String) in.readObject();

                synchronized (onlineUsers) {
                    onlineUsers.put(currentUsername, out);
                    System.out.println("🟢 [" + currentUsername + "] đã tham gia hệ thống chat.");
                }

                // Vòng lặp lắng nghe tin nhắn chat 1-1
                while (true) {
                    ChatMessage incomingMsg = (ChatMessage) in.readObject();

                    // 1. Lưu tin nhắn vào SQL Server làm bằng chứng
                    messageDAO.insert(incomingMsg);

                    String receiver = incomingMsg.getReceiverName();
                    String sender = incomingMsg.getSenderName();

                    // 2. SHIPPER gửi tin nhắn tới Người Nhận (nếu họ đang online)
                    synchronized (onlineUsers) {
                        ObjectOutputStream receiverOut = onlineUsers.get(receiver);
                        if (receiverOut != null) {
                            receiverOut.writeObject(incomingMsg);
                            receiverOut.reset();
                        }

                        // 3. SHIPPER gửi lại phản hồi cho chính Người Gửi để màn hình của họ hiện lên
                        ObjectOutputStream senderOut = onlineUsers.get(sender);
                        if (senderOut != null) {
                            senderOut.writeObject(incomingMsg);
                            senderOut.reset();
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("🔴 " + currentUsername + " đã ngắt kết nối.");
            } finally {
                if (currentUsername != null) {
                    synchronized (onlineUsers) {
                        onlineUsers.remove(currentUsername);
                    }
                }
                try { socket.close(); } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}