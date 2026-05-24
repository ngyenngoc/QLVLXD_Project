package server.network;

import server.dao.*;
import shared.Request;
import shared.Response;
import shared.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private MaterialDAO materialDAO;
    private CategoryDAO categoryDAO;
    private SupplierDAO supplierDAO;
    private CustomerDAO customerDAO;
    private SalesOrderDAO salesOrderDAO;
    private UsersDAO usersDAO;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.materialDAO = new MaterialDAO();
        this.categoryDAO = new CategoryDAO();
        this.supplierDAO = new SupplierDAO();
        this.customerDAO = new CustomerDAO();
        this.salesOrderDAO = new SalesOrderDAO();
        this.usersDAO = new UsersDAO();
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            Request request = (Request) in.readObject();
            System.out.println(" Server nhận yêu cầu: [" + request.getAction() + "]");
            Response response = null;

            switch (request.getAction()) {
                case "GET_ALL_MATERIALS":
                    response = new Response(true, "Lấy dữ liệu thành công", materialDAO.getAll());
                    break;

                case "GET_ALL_CATEGORIES":
                    response = new Response(true, "Lấy danh mục loại thành công", categoryDAO.getAll());
                    break;

                case "GET_ALL_SUPPLIERS":
                    response = new Response(true, "Lấy nhà cung cấp thành công", supplierDAO.getAll());
                    break;

                case "GENERATE_MATERIAL_ID":
                    response = new Response(true, "Sinh ID thành công", materialDAO.generateNewID());
                    break;

                case "ADD_MATERIAL":
                    Material newMat = (Material) request.getData();
                    if (materialDAO.insert(newMat)) {
                        response = new Response(true, "Thêm vật liệu thành công!", null);
                    } else {
                        response = new Response(false, "Không thể thêm vật liệu vào cơ sở dữ liệu!", null);
                    }
                    break;

                case "UPDATE_MATERIAL":
                    Material upMat = (Material) request.getData();
                    if (materialDAO.update(upMat)) {
                        response = new Response(true, "Cập nhật vật liệu thành công!", null);
                    } else {
                        response = new Response(false, "Cập nhật vật liệu thất bại!", null);
                    }
                    break;

                case "DELETE_MATERIAL":
                    String matID = (String) request.getData();
                    if (materialDAO.delete(matID)) {
                        response = new Response(true, "Xóa vật liệu thành công!", null);
                    } else {
                        response = new Response(false, "Không thể xóa vật liệu này!", null);
                    }
                    break;
                // ================= KHỐI XỬ LÝ KHÁCH HÀNG (CUSTOMER) =================
                case "GET_ALL_CUSTOMERS":
                    response = new Response(true, "Lấy danh sách khách hàng thành công", customerDAO.getAll());
                    break;

                case "GENERATE_CUSTOMER_ID":
                    response = new Response(true, "Sinh ID khách hàng thành công", customerDAO.generateNewID());
                    break;

                case "ADD_CUSTOMER":
                    Customer newCust = (Customer) request.getData();
                    if (customerDAO.insert(newCust)) {
                        response = new Response(true, "Thêm khách hàng thành công!", null);
                    } else {
                        response = new Response(false, "Không thể thêm khách hàng vào cơ sở dữ liệu!", null);
                    }
                    break;

                case "UPDATE_CUSTOMER":
                    Customer upCust = (Customer) request.getData();
                    if (customerDAO.update(upCust)) {
                        response = new Response(true, "Cập nhật khách hàng thành công!", null);
                    } else {
                        response = new Response(false, "Cập nhật khách hàng thất bại!", null);
                    }
                    break;

                case "DELETE_CUSTOMER":
                    String custID = (String) request.getData();
                    if (customerDAO.delete(custID)) {
                        response = new Response(true, "Xóa khách hàng thành công!", null);
                    } else {
                        response = new Response(false, "Không thể xóa khách hàng này!", null);
                    }
                    break;
                // ================= KHỐI XỬ LÝ LOẠI NHÀ CUNG CẤP (SUPPLIER) =================
                case "ADD_SUPPLIER":
                    Supplier newSup = (Supplier) request.getData();
                    if (supplierDAO.insert(newSup)) {
                        response = new Response(true, "Thêm loại nhà cung cấpthành công!", null);
                    } else {
                        response = new Response(false, "Không thể thêm nhà cung cấp vào cơ sở dữ liệu!", null);
                    }
                    break;
                case "GENERATE_SUPPLIER_ID":
                    response = new Response(true, "Sinh ID nhà cung cấp thành công", supplierDAO.generateNewID());
                    break;

                case "UPDATE_SUPPLIER":
                    Supplier upSup = (Supplier) request.getData();
                    if (supplierDAO.update(upSup)) {
                        response = new Response(true, "Cập nhật nhà cung cấp thành công!", null);
                    } else {
                        response = new Response(false, "Cập nhật nhà cung cấp thất bại!", null);
                    }
                    break;

                case "DELETE_SUPPLIER":
                    String supplierID = (String) request.getData();
                    if (supplierDAO.delete(supplierID)) {
                        response = new Response(true, "Xóa nhà cung cấp thành công!", null);
                    } else {
                        response = new Response(false, "Không thể xóa nhà cung cấp này (Có thể đang được dùng ở bảng Vật liệu)!", null);
                    }
                    break;
                // ================= KHỐI XỬ LÝ LOẠI VẬT LIỆU (CATEGORY) =================
                case "ADD_CATEGORY":
                    Category newCat = (Category) request.getData();
                    if (categoryDAO.insert(newCat)) {
                        response = new Response(true, "Thêm loại vật liệu thành công!", null);
                    } else {
                        response = new Response(false, "Không thể thêm loại vật liệu vào cơ sở dữ liệu!", null);
                    }
                    break;

                case "UPDATE_CATEGORY":
                    Category upCat = (Category) request.getData();
                    if (categoryDAO.update(upCat)) {
                        response = new Response(true, "Cập nhật loại vật liệu thành công!", null);
                    } else {
                        response = new Response(false, "Cập nhật loại vật liệu thất bại!", null);
                    }
                    break;

                case "DELETE_CATEGORY":
                    int categoryID = (int) request.getData();
                    if (categoryDAO.delete(categoryID)) {
                        response = new Response(true, "Xóa loại vật liệu thành công!", null);
                    } else {
                        response = new Response(false, "Không thể xóa loại vật liệu này (Có thể đang được dùng ở bảng Vật liệu)!", null);
                    }
                    break;
                // ================= KHỐI XỬ LÝ ĐƠN HÀNG (SALESORDER) =================
                case "GET_ALL_ORDERS":
                    response = new Response(true, "Lấy danh sách đơn hàng thành công", salesOrderDAO.getAll());
                    break;

                case "GENERATE_ORDER_ID":
                    response = new Response(true, "Sinh ID đơn hàng thành công", salesOrderDAO.generateNewID());
                    break;

                case "GET_CURRENT_STOCK":
                    String materialID = (String) request.getData();
                    int stock = materialDAO.getCurrentStock(materialID); // Dùng materialDAO có sẵn của Server
                    response = new Response(true, "Lấy tồn kho thành công", stock);
                    break;

                case "ADD_FULL_ORDER":
                    // Client gửi lên một mảng chứa [SalesOrder, List<SalesOrderDetail>]
                    java.util.List<Object> orderPayload = (java.util.List<Object>) request.getData();
                    shared.model.SalesOrder order = (shared.model.SalesOrder) orderPayload.get(0);
                    java.util.List<shared.model.SalesOrderDetail> details = (java.util.List<shared.model.SalesOrderDetail>) orderPayload.get(1);

                    if (salesOrderDAO.insertFullOrder(order, details)) {
                        response = new Response(true, "Tạo đơn hàng và trừ kho thành công!", null);
                    } else {
                        response = new Response(false, "Lỗi: Không thể hoàn thành đơn hàng hệ thống!", null);
                    }
                    break;

                case "UPDATE_ORDER":
                    shared.model.SalesOrder upOrder = (shared.model.SalesOrder) request.getData();
                    if (salesOrderDAO.update(upOrder)) {
                        response = new Response(true, "Cập nhật đơn hàng thành công!", null);
                    } else {
                        response = new Response(false, "Cập nhật đơn hàng thất bại!", null);
                    }
                    break;

                case "DELETE_ORDER":
                    String orderID = (String) request.getData();
                    if (salesOrderDAO.delete(orderID)) {
                        response = new Response(true, "Xóa đơn hàng thành công!", null);
                    } else {
                        response = new Response(false, "Không thể xóa đơn hàng do ràng buộc dữ liệu!", null);
                    }
                    break;
                // ================= KHỐI XỬ LÝ ĐĂNG NHẬP (USER LOGIN) =================
                case "LOGIN":
                    shared.model.Users loginInfo = (shared.model.Users) request.getData();
                    shared.model.Users validatedUser = usersDAO.login(loginInfo.getUserName(), loginInfo.getPassWord());

                    if (validatedUser != null) {
                        response = new Response(true, "Đăng nhập thành công!", validatedUser);
                    } else {
                        response = new Response(false, "Sai tên đăng nhập hoặc mật khẩu!", null);
                    }
                    break;
                // ================= KHỐI XỬ LÝ THỐNG KÊ (DASHBOARD) =================
                case "GET_DASHBOARD_STATS":
                    try {
                        Map<String, Object> statsMap = new HashMap<>();

                        statsMap.put("totalMaterial", materialDAO.countAll());
                        statsMap.put("lowStock", materialDAO.countLowStock());
                        statsMap.put("categoryStats", materialDAO.getCategoryStats());
                        statsMap.put("topSelling", materialDAO.getTopSellingMaterials());

                        response = new Response(true, "Lấy dữ liệu thống kê thành công!", statsMap);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        response = new Response(false, "Lỗi khi thống kê dữ liệu: " + ex.getMessage(), null);
                    }
                    break;
                // ================= KHỐI XỬ LÝ LỊCH SỬ CHAT (CHATMESSAGE) =================
                case "GET_CHAT_HISTORY":
                    //  Nhận mảng chứa tên 2 người (VD: NV1 và Khách hàng)
                    String[] participants = (String[]) request.getData();

                    ChatDAO chatDAO = new ChatDAO();
                    List<ChatMessage> history = chatDAO.getChatHistory(participants[0], participants[1]);

                    response = new Response(true, "Lấy lịch sử thành công", history);
                    break;

                default:
                    response = new Response(false, "Server không hiểu lệnh: " + request.getAction(), null);
            }

            out.writeObject(response);
            out.flush();

        } catch (Exception e) {
            System.err.println(" Lỗi khi xử lý Client: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}