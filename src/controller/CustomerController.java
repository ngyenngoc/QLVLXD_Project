package controller;


import dao. CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import model.Customer;

import java.util.List;
import java.util.Optional;


public class CustomerController {
    @FXML private TextField txtCustomerID, txtCustomerName, txtAddress, txtPhoneNumber, txtEmail;
    @FXML
    private TableView<Customer> tblCustomer;
    @FXML private TableColumn<Customer, String> colID, colName, colAddress, colPhone, colEmail;
    @FXML private Label lblMessage;
    @FXML private Button btnAdd, btnUpdate, btnDelete, btnRefresh;

    private final CustomerDAO dao = new CustomerDAO();
    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();
    // Phương thức chính để lấy View
    @FXML
    public void initialize() {
        // Ánh xạ cột bảng với thuộc tính của model Customer
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
        List<Customer> list = dao.getAll();
        customerList.clear();
        customerList.addAll(list);
        txtCustomerID.setText(dao.generateNewID());
    }
    private void setupEventHandlers() {
        btnAdd.setOnAction(e -> handleAddCustomer());
        btnUpdate.setOnAction(e -> handleUpdateCustomer());
        btnDelete.setOnAction(e -> handleDeleteCustomer());
        btnRefresh.setOnAction(e -> loadCustomerData());
        // Gán Listener cho TableView
        tblCustomer.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        displayDetails(newSelection);
                    }
                });
    }
    @FXML
    //Xử lý CRUD
    private void handleAddCustomer(){
        String newID = dao.generateNewID();
        txtCustomerID.setText(newID);
        Customer newCustomer = new Customer(dao.generateNewID(), txtCustomerName.getText(), txtAddress.getText(), txtPhoneNumber.getText(), txtEmail.getText());
        if (dao.insert(newCustomer)){
            lblMessage.setText(" Thêm khách hàng " +newCustomer.getCustomerName() + " thành công!");
            customerList.add(newCustomer);
            clearForm();
        }
        else{
            lblMessage.setText("Lỗi: Không thể thêm khách hàng!");
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
                txtCustomerID.getText(), txtCustomerName.getText(), txtAddress.getText(), txtPhoneNumber.getText(),txtEmail.getText()
        );
        if(dao.update(updatedCustomer)){
            lblMessage.setText("Cập nhật thông tin khách hàng thành công!");
            int index = customerList.indexOf(selectedCustomer);
            if(index != -1){
                customerList.set(index, updatedCustomer);
            }
        }
        else{
            lblMessage.setText("Lỗi: Khách hàng thất bại!");
        }
    }
    @FXML
    private void handleDeleteCustomer(){
        Customer selectedCustomer = tblCustomer.getSelectionModel().getSelectedItem();
        if(selectedCustomer == null){
            lblMessage.setText("Vui lòng chọn khách hàng cần xóa");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa khách hàng " +selectedCustomer.getCustomerName() + "?");
        Optional<ButtonType> result = confirm.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            if (dao.delete(selectedCustomer.getCustomerID())){
                lblMessage.setText("Đã xóa khách hàng " + selectedCustomer.getCustomerName() + " thành công");
                customerList.remove(selectedCustomer);
                clearForm();
            }
            else{
                lblMessage.setText("Đã xóa khách hàng!");
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
        txtCustomerID.clear();
        txtCustomerName.clear();
        txtAddress.clear();
        txtPhoneNumber.clear();
        txtEmail.clear();
    }
}