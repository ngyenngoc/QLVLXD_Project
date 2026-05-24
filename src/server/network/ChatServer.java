package server.network;

import shared.model.ChatMessage;
import server.dao.ChatDAO;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
    private static final int CHAT_PORT = 9001;

    // Lưu danh bạ trực tuyến (Tên tài khoản -> Đường ống gửi tin)
    private static final Map<String, ObjectOutputStream> onlineUsers = new HashMap<>();


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
        private String currentUsername;

        public ChatHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());


                currentUsername = (String) in.readObject();

                synchronized (onlineUsers) {
                    onlineUsers.put(currentUsername, out);
                    System.out.println("🟢 [" + currentUsername + "] đã tham gia hệ thống chat.");
                }

                // Vòng lặp lắng nghe tin nhắn chat 1-1
                while (true) {
                    ChatMessage incomingMsg = (ChatMessage) in.readObject();

                    new ChatDAO().saveMessage(incomingMsg);


                    String receiver = incomingMsg.getReceiverName();
                    String sender = incomingMsg.getSenderName();

                    synchronized (onlineUsers) {
                        // Gửi cho người nhận
                        ObjectOutputStream receiverOut = onlineUsers.get(receiver);
                        if (receiverOut != null) {
                            receiverOut.writeObject(incomingMsg);
                            receiverOut.reset();
                        }

                        // Phản hồi lại cho người gửi để cập nhật giao diện
                        ObjectOutputStream senderOut = onlineUsers.get(sender);
                        if (senderOut != null) {
                            senderOut.writeObject(incomingMsg);
                            senderOut.reset();
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(" " + currentUsername + " đã ngắt kết nối.");
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