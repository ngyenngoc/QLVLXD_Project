package server.network;

import server.dao.CategoryDAO;
import server.dao.MaterialDAO;
import server.dao.SupplierDAO;
import server.dao.CustomerDAO;
import shared.Request;
import shared.Response;
import shared.model.Material;
import shared.model.Customer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private MaterialDAO materialDAO;
    private CategoryDAO categoryDAO;
    private SupplierDAO supplierDAO;
    private CustomerDAO customerDAO;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.materialDAO = new MaterialDAO();
        this.categoryDAO = new CategoryDAO();
        this.supplierDAO = new SupplierDAO();
        this.customerDAO = new CustomerDAO();
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            Request request = (Request) in.readObject();
            System.out.println("📬 Server nhận yêu cầu: [" + request.getAction() + "]");
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