package client.controller;

import client.service.SocketClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import shared.Request;
import shared.Response;
import shared.model.Customer;

import java.util.List;
import java.util.Optional;

public class CustomerController {
    @FXML private TextField txtCustomerID, txtCustomerName, txtAddress, txtPhoneNumber, txtEmail;
    @FXML private TableView<Customer> tblCustomer;
    @FXML private TableColumn<Customer, String> colID, colName, colAddress, colPhone, colEmail;
    @FXML private Label lblMessage;
    @FXML private Button btnAdd, btnUpdate, btnDelete, btnRefresh;

    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Ánh xạ cột bảng với thuộc tính của shared.model Customer
        colID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        tblCustomer.setItems(customerList);
        loadCustomerData();
        txtCustomerID.setDisable(true);

        setupEventHandlers();

        // Gán sự kiện khi click vào dòng trong bảng
        tblCustomer.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) displayDetails(newSelection);
                });
    }

    @FXML
    private void loadCustomerData(){
        // 1. Lấy danh sách khách hàng từ Server qua Socket
        Response response = SocketClient.sendRequest(new Request("GET_ALL_CUSTOMERS", null));
        if (response.isSuccess()) {
            List<Customer> list = (List<Customer>) response.getData();
            customerList.clear();
            customerList.addAll(list);
        } else {
            lblMessage.setText("Lỗi nạp dữ liệu: " + response.getMessage());
        }

        // 2. Xin Server sinh mã ID tự động mới
        Response resId = SocketClient.sendRequest(new Request("GENERATE_CUSTOMER_ID", null));
        if (resId.isSuccess()) {
            txtCustomerID.setText((String) resId.getData());
        } else {
            txtCustomerID.setText("ERROR");
        }
    }

    private void setupEventHandlers() {
        btnAdd.setOnAction(e -> handleAddCustomer());
        btnUpdate.setOnAction(e -> handleUpdateCustomer());
        btnDelete.setOnAction(e -> handleDeleteCustomer());
        btnRefresh.setOnAction(e -> loadCustomerData());
    }

    @FXML
    private void handleAddCustomer(){
        if (txtCustomerName.getText().isEmpty() || txtPhoneNumber.getText().isEmpty()) {
            lblMessage.setText("Lỗi: Vui lòng nhập tên và số điện thoại!");
            return;
        }

        // Tạo thực thể khách hàng với ID đã có sẵn trong textField
        Customer newCustomer = new Customer(
                txtCustomerID.getText(),
                txtCustomerName.getText(),
                txtAddress.getText(),
                txtPhoneNumber.getText(),
                txtEmail.getText()
        );

        // Gửi lệnh thêm qua mạng
        Response response = SocketClient.sendRequest(new Request("ADD_CUSTOMER", newCustomer));
        if (response.isSuccess()){
            lblMessage.setText("Thêm khách hàng " + newCustomer.getCustomerName() + " thành công!");
            loadCustomerData(); // Tải lại bảng và sinh ID mới luôn cho tiện
            clearForm();
        } else {
            lblMessage.setText("Lỗi: " + response.getMessage());
        }
    }

    @FXML
    private void handleUpdateCustomer(){
        Customer selectedCustomer = tblCustomer.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null){
            lblMessage.setText("Vui lòng chọn một khách hàng trong danh sách để sửa");
            return;
        }

        Customer updatedCustomer = new Customer(
                txtCustomerID.getText(),
                txtCustomerName.getText(),
                txtAddress.getText(),
                txtPhoneNumber.getText(),
                txtEmail.getText()
        );

        // Gửi lệnh cập nhật lên Server
        Response response = SocketClient.sendRequest(new Request("UPDATE_CUSTOMER", updatedCustomer));
        if (response.isSuccess()){
            lblMessage.setText("Cập nhật thông tin khách hàng thành công!");
            loadCustomerData();
            clearForm();
        } else {
            lblMessage.setText("Lỗi: " + response.getMessage());
        }
    }

    @FXML
    private void handleDeleteCustomer(){
        Customer selectedCustomer = tblCustomer.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null){
            lblMessage.setText("Vui lòng chọn khách hàng cần xóa");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa khách hàng " + selectedCustomer.getCustomerName() + "?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            // Gửi ID khách hàng lên mạng yêu cầu xóa
            Response response = SocketClient.sendRequest(new Request("DELETE_CUSTOMER", selectedCustomer.getCustomerID()));
            if (response.isSuccess()){
                lblMessage.setText("Đã xóa khách hàng thành công.");
                loadCustomerData();
                clearForm();
            } else {
                lblMessage.setText("Lỗi: " + response.getMessage());
            }
        }
    }

    private void displayDetails(Customer customer){
        txtCustomerID.setText(customer.getCustomerID());
        txtCustomerName.setText(customer.getCustomerName());
        txtAddress.setText(customer.getAddress());
        txtPhoneNumber.setText(customer.getPhoneNumber());
        txtEmail.setText(customer.getEmail());
        lblMessage.setText("");
    }

    private void clearForm(){
        txtCustomerName.clear();
        txtAddress.clear();
        txtPhoneNumber.clear();
        txtEmail.clear();
    }
}