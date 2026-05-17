package client.controller;

import client.service.SocketClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import shared.Request;
import shared.Response;
import shared.model.Supplier;

import java.util.List;
import java.util.Optional;

public class SupplierController {
    @FXML private TextField txtSupplierID, txtSupplierName, txtAddress, txtPhoneNumber, txtEmail;
    @FXML private TableView<Supplier> tblSupplier;
    @FXML private TableColumn<Supplier, String> colID, colSPLName, colAddress, colPhone, colEmail;
    @FXML private Label lblMessage;
    @FXML private Button btnAdd, btnUpdate, btnDelete, btnRefresh;

    private final ObservableList<Supplier> supplierList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Ánh xạ cột bảng với thuộc tính của shared.model Supplier
        colID.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
        colSPLName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        tblSupplier.setItems(supplierList);
        loadSupplierData();
        txtSupplierID.setDisable(true); // Khóa ô nhập ID để hệ thống tự điền mã sinh từ Server

        setupEventHandlers();

        // Gán sự kiện khi click vào dòng trong bảng
        tblSupplier.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) displayDetails(newSelection);
                });
    }

    @FXML
    private void loadSupplierData(){
        // 1. Gọi mạng lấy danh sách Nhà cung cấp từ Server
        Response response = SocketClient.sendRequest(new Request("GET_ALL_SUPPLIERS", null));
        if (response.isSuccess()) {
            List<Supplier> list = (List<Supplier>) response.getData();
            supplierList.clear();
            supplierList.addAll(list);
        } else {
            lblMessage.setText("Lỗi mạng: " + response.getMessage());
        }

        // 2. Xin Server cấp mã ID tự động mới cho Nhà cung cấp
        Response resId = SocketClient.sendRequest(new Request("GENERATE_SUPPLIER_ID", null));
        if (resId.isSuccess()) {
            txtSupplierID.setText((String) resId.getData());
        } else {
            txtSupplierID.setText("ERROR");
        }
    }

    private void setupEventHandlers() {
        btnAdd.setOnAction(e -> handleAddSupplier());
        btnUpdate.setOnAction(e -> handleUpdateSupplier());
        btnDelete.setOnAction(e -> handleDeleteSupplier());
        btnRefresh.setOnAction(e -> loadSupplierData());
    }

    @FXML
    private void handleAddSupplier(){
        if (txtSupplierName.getText().isEmpty() || txtPhoneNumber.getText().isEmpty()) {
            lblMessage.setText("Lỗi: Vui lòng nhập Tên nhà cung cấp và Số điện thoại!");
            return;
        }

        // Tạo đối tượng lấy ID từ chuỗi sinh tự động trên ô nhập dữ liệu
        Supplier newSupplier = new Supplier(
                txtSupplierID.getText(),
                txtSupplierName.getText(),
                txtAddress.getText(),
                txtPhoneNumber.getText(),
                txtEmail.getText()
        );

        // Gửi gói tin yêu cầu thêm qua mạng
        Response response = SocketClient.sendRequest(new Request("ADD_SUPPLIER", newSupplier));
        if (response.isSuccess()){
            lblMessage.setText("Thêm nhà cung cấp " + newSupplier.getSupplierName() + " thành công!");
            loadSupplierData(); // Tải lại dữ liệu và tự cập nhật mã ID mới
            clearForm();
        } else {
            lblMessage.setText("Lỗi: " + response.getMessage());
        }
    }

    @FXML
    private void handleUpdateSupplier(){
        Supplier selectedSupplier = tblSupplier.getSelectionModel().getSelectedItem();
        if (selectedSupplier == null){
            lblMessage.setText("Vui lòng chọn một nhà cung cấp trong danh sách để sửa");
            return;
        }

        Supplier updatedSupplier = new Supplier(
                txtSupplierID.getText(),
                txtSupplierName.getText(),
                txtAddress.getText(),
                txtPhoneNumber.getText(),
                txtEmail.getText()
        );

        // Gửi gói tin cập nhật qua mạng
        Response response = SocketClient.sendRequest(new Request("UPDATE_SUPPLIER", updatedSupplier));
        if (response.isSuccess()){
            lblMessage.setText("Cập nhật thông tin nhà cung cấp thành công!");
            loadSupplierData();
            clearForm();
        } else {
            lblMessage.setText("Lỗi: " + response.getMessage());
        }
    }

    @FXML
    private void handleDeleteSupplier(){
        Supplier selectedSupplier = tblSupplier.getSelectionModel().getSelectedItem();
        if(selectedSupplier == null){
            lblMessage.setText("Vui lòng chọn nhà cung cấp cần xóa");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa nhà cung cấp " + selectedSupplier.getSupplierName() + "?");
        Optional<ButtonType> result = confirm.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            // Gửi ID nhà cung cấp lên mạng yêu cầu xóa
            Response response = SocketClient.sendRequest(new Request("DELETE_SUPPLIER", selectedSupplier.getSupplierID()));
            if (response.isSuccess()){
                lblMessage.setText("Đã xóa nhà cung cấp " + selectedSupplier.getSupplierName() + " thành công.");
                loadSupplierData();
                clearForm();
            } else {
                lblMessage.setText("Lỗi: " + response.getMessage());
            }
        }
    }

    private void displayDetails(Supplier supplier){
        txtSupplierID.setText(supplier.getSupplierID());
        txtSupplierName.setText(supplier.getSupplierName());
        txtAddress.setText(supplier.getAddress());
        txtPhoneNumber.setText(supplier.getPhoneNumber());
        txtEmail.setText(supplier.getEmail());
        lblMessage.setText("");
    }

    private void clearForm(){
        txtSupplierName.clear();
        txtAddress.clear();
        txtPhoneNumber.clear();
        txtEmail.clear();
    }
}