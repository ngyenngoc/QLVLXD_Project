package controller;


import dao.SupplierDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import model.Supplier;
import view.SupplierView;

import java.util.List;
import java.util.Optional;


public class SupplierController {
    private final SupplierDAO dao= new SupplierDAO();
    private final SupplierView view= new SupplierView();
    private final ObservableList<Supplier> supplierList = FXCollections.observableArrayList();
    // Phương thức chính để lấy View
    public Pane getSupplierManagementView(){
        initialize();
        return view.createRootNode();
    }
    public void initialize(){
        view.tblSupplier.setItems(supplierList);
        setupEventHandlers();
        loadSupplierData();
    }
    private void loadSupplierData(){
        List<Supplier> list = dao.getAll();
    supplierList.clear();
    supplierList.addAll(list);
    }
    private void setupEventHandlers() {
        view.btnAdd.setOnAction(e -> handleAddSupplier());
        view.btnUpdate.setOnAction(e -> handleUpdateSupplier());
        view.btnDelete.setOnAction(e -> handleDeleteSupplier());
        view.btnRefresh.setOnAction(e -> loadSupplierData());
        // Gán Listener cho TableView
        view.tblSupplier.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        displayDetails(newSelection);
                    }
                });
    }
    //Xử lý CRUD
    private void handleAddSupplier(){
        Supplier newSupplier = new Supplier(dao.generateNewID(), view.txtSupplierName.getText(), view.txtAddress.getText(), view.txtPhoneNumber.getText(), view.txtEmail.getText());
            if (dao.insert(newSupplier)){
                view.lblMessage.setText("Đã thêm nhà cung cấp " +newSupplier.getSupplierName() + " thành công!");
                supplierList.add(newSupplier);
                clearForm();
            }
            else{
                view.lblMessage.setText("Lỗi: Không thể thêm nhà cung cấp!");
            }
        }
    private void handleUpdateSupplier(){
    Supplier selectedSupplier = view.tblSupplier.getSelectionModel().getSelectedItem();
    if (selectedSupplier == null){
        view.lblMessage.setText("Vui lòng chọn một khách hàng trong danh sách để sửa");
        return;
    }
    Supplier updatedSupplier = new Supplier(
            view.txtSupplierID.getText(), view.txtSupplierName.getText(), view.txtAddress.getText(), view.txtPhoneNumber.getText(),view.txtEmail.getText()
        );
    if(dao.update(updatedSupplier)){
        view.lblMessage.setText("Cập nhật thông tin nhà cung cấp thành công!");
        int index = supplierList.indexOf(selectedSupplier);
        if(index != -1){
            supplierList.set(index, updatedSupplier);
        }
    }
    else{
        view.lblMessage.setText("Lỗi: Nhà cung cấp thất bại!");
    }
    }
    private void handleDeleteSupplier(){
    Supplier selectedSupplier = view.tblSupplier.getSelectionModel().getSelectedItem();
    if(selectedSupplier == null){
        view.lblMessage.setText("Vui lòng chọn nhà cung cấp cần xóa");
        return;
    }
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa nhà cung cấp " +selectedSupplier.getSupplierName() + "?");
        Optional<ButtonType> result = confirm.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            if (dao.delete(selectedSupplier.getSupplierID())){
                view.lblMessage.setText("Đã xóa nhà cung cấp " + selectedSupplier.getSupplierName() + " thành công");
                supplierList.remove(selectedSupplier);
                clearForm();
            }
            else{
                view.lblMessage.setText("Đã xóa khách hàng!");
            }
        }
    }
    private void displayDetails(Supplier supplier){
        view.txtSupplierID.setText(supplier.getSupplierID());
        view.txtSupplierName.setText(supplier.getSupplierName());
        view.txtAddress.setText(supplier.getAddress());
        view.txtPhoneNumber.setText(supplier.getPhoneNumber());
        view.txtEmail.setText(supplier.getEmail());
        view.lblMessage.setText("");
    }
    private void clearForm(){
        view.txtSupplierID.clear();
        view.txtSupplierName.clear();
        view.txtAddress.clear();
        view.txtPhoneNumber.clear();
        view.txtEmail.clear();
    }
}