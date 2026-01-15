package controller;


import dao.SupplierDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import model.Supplier;

import java.util.List;
import java.util.Optional;


public class SupplierController {
    @FXML private TextField txtSupplierID, txtSupplierName, txtAddress, txtPhoneNumber, txtEmail;
    @FXML
    private TableView<Supplier> tblSupplier;
    @FXML private TableColumn<Supplier, String> colID, colName, colAddress, colPhone, colEmail;
    @FXML private Label lblMessage;
    @FXML private Button btnAdd, btnUpdate, btnDelete, btnRefresh;

    private final SupplierDAO dao = new SupplierDAO();
    private final ObservableList<Supplier> supplierList = FXCollections.observableArrayList();
    // Phương thức chính để lấy View
    @FXML
    public void initialize() {
        // Ánh xạ cột bảng với thuộc tính của model Supplier
        colID.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        tblSupplier.setItems(supplierList);
        loadSupplierData();

        setupEventHandlers();
        // Gán sự kiện khi click vào dòng trong bảng
        tblSupplier.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) displayDetails(newSelection);
                });
    }
    @FXML
    private void loadSupplierData(){
        List<Supplier> list = dao.getAll();
    supplierList.clear();
    supplierList.addAll(list);
    }
    private void setupEventHandlers() {
        btnAdd.setOnAction(e -> handleAddSupplier());
        btnUpdate.setOnAction(e -> handleUpdateSupplier());
        btnDelete.setOnAction(e -> handleDeleteSupplier());
        btnRefresh.setOnAction(e -> loadSupplierData());
        // Gán Listener cho TableView
        tblSupplier.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        displayDetails(newSelection);
                    }
                });
    }
    @FXML
    //Xử lý CRUD
    private void handleAddSupplier(){
        Supplier newSupplier = new Supplier(dao.generateNewID(), txtSupplierName.getText(), txtAddress.getText(), txtPhoneNumber.getText(), txtEmail.getText());
            if (dao.insert(newSupplier)){
                lblMessage.setText(" Thêm nhà cung cấp " +newSupplier.getSupplierName() + " thành công!");
                supplierList.add(newSupplier);
                clearForm();
            }
            else{
                lblMessage.setText("Lỗi: Không thể thêm nhà cung cấp!");
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
            txtSupplierID.getText(), txtSupplierName.getText(), txtAddress.getText(), txtPhoneNumber.getText(),txtEmail.getText()
        );
    if(dao.update(updatedSupplier)){
        lblMessage.setText("Cập nhật thông tin nhà cung cấp thành công!");
        int index = supplierList.indexOf(selectedSupplier);
        if(index != -1){
            supplierList.set(index, updatedSupplier);
        }
    }
    else{
        lblMessage.setText("Lỗi: Nhà cung cấp thất bại!");
    }
    }
    @FXML
    private void handleDeleteSupplier(){
    Supplier selectedSupplier = tblSupplier.getSelectionModel().getSelectedItem();
    if(selectedSupplier == null){
        lblMessage.setText("Vui lòng chọn nhà cung cấp cần xóa");
        return;
    }
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa nhà cung cấp " +selectedSupplier.getSupplierName() + "?");
        Optional<ButtonType> result = confirm.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            if (dao.delete(selectedSupplier.getSupplierID())){
                lblMessage.setText("Đã xóa nhà cung cấp " + selectedSupplier.getSupplierName() + " thành công");
                supplierList.remove(selectedSupplier);
                clearForm();
            }
            else{
                lblMessage.setText("Đã xóa nhà cung cấp!");
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
        txtSupplierID.clear();
        txtSupplierName.clear();
        txtAddress.clear();
        txtPhoneNumber.clear();
        txtEmail.clear();
    }
}