package client.controller;

import client.service.SocketClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import shared.Request;
import shared.Response;
import shared.model.ChatMessage;

import shared.model.Customer;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ChatController {

    @FXML private TextArea txtChatHistory;
    @FXML private TextField txtMessage;

    @FXML private ComboBox<String> cbReceiver;

    private Socket chatSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private String myName = "NV1";

    @FXML
    public void initialize() {
        connectToChatServer();
        loadCustomerList(); // 2. BỔ SUNG: Gọi hàm nạp dữ liệu vào ComboBox khi vừa mở cửa sổ
    }

    // danh sách khách hàng từ Database qua Socket cổng 9000
    private void loadCustomerList() {
        try {
            // Yêu cầu Server chính lấy dữ liệu
            Request req = new Request("GET_ALL_CUSTOMERS", null);
            Response res = SocketClient.sendRequest(req);

            if (res != null && res.isSuccess()) {
                List<Customer> customers = (List<Customer>) res.getData();

                // Xóa dữ liệu cũ và đổ dữ liệu mới vào ComboBox
                cbReceiver.getItems().clear();
                for (Customer c : customers) {
                    // Lấy Tên Khách Hàng (hoặc Số điện thoại/Email tùy bạn) nhét vào ComboBox
                    // Lưu ý: Sửa 'getCustomerName()' thành hàm GET tương ứng trong file Customer.java của bạn nếu nó tên khác
                    cbReceiver.getItems().add(c.getCustomerName());
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tải danh sách Khách hàng: " + e.getMessage());
        }
    }

    private void connectToChatServer() {
        try {
            chatSocket = new Socket("localhost", 9001);
            out = new ObjectOutputStream(chatSocket.getOutputStream());
            in = new ObjectInputStream(chatSocket.getInputStream());

            // Gửi tên của mình lên đầu tiên để Server "ghi vào danh bạ"
            out.writeObject(myName);
            out.flush();

            Thread listenerThread = new Thread(() -> {
                try {
                    while (true) {
                        ChatMessage msg = (ChatMessage) in.readObject();
                        Platform.runLater(() -> {
                            // Hiển thị dạng định nghĩa rõ ràng: [Người gửi -> Người nhận]: Nội dung
                            txtChatHistory.appendText("[" + msg.getSenderName() + "]: " + msg.getContent() + "\n");
                        });
                    }
                } catch (Exception e) {
                    Platform.runLater(() -> txtChatHistory.appendText(" Mất kết nối chat.\n"));
                }
            });
            listenerThread.setDaemon(true);
            listenerThread.start();

        } catch (Exception e) {
            txtChatHistory.appendText(" Lỗi kết nối Server cổng 9001.\n");
        }
    }

    @FXML
    private void handleSend() {
        // 4. ĐÃ THAY ĐỔI: Lấy giá trị từ ComboBox thay vì TextField
        String receiver = cbReceiver.getValue();
        String content = txtMessage.getText().trim();

        // Kiểm tra xem đã chọn khách hàng trong danh sách chưa
        if (receiver == null || receiver.isEmpty() || content.isEmpty()) return;

        try {
            // Đóng gói tin nhắn 1-1 chuẩn xác: Người gửi, Người nhận thật, Nội dung
            ChatMessage newMsg = new ChatMessage(myName, receiver, content);

            out.writeObject(newMsg);
            out.reset();

            txtMessage.clear();
        } catch (Exception e) {
            txtChatHistory.appendText(" Lỗi gửi tin.\n");
        }
    }
}