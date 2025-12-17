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
                view.lblMessage.setText("Supplier added " +newSupplier.getSupplierName() + " successfully!");
                supplierList.add(newSupplier);
                clearForm();
            }
            else{
                view.lblMessage.setText("Failed to add supplier!");
            }
        }
    private void handleUpdateSupplier(){
    Supplier selectedSupplier = view.tblSupplier.getSelectionModel().getSelectedItem();
    if (selectedSupplier == null){
        view.lblMessage.setText("Please select a supplier to edit");
        return;
    }
    Supplier updatedSupplier = new Supplier(
            view.txtSupplierID.getText(), view.txtSupplierName.getText(), view.txtAddress.getText(), view.txtPhoneNumber.getText(),view.txtEmail.getText()
        );
    if(dao.update(updatedSupplier)){
        view.lblMessage.setText("Supplier updated successfully!");
        int index = supplierList.indexOf(selectedSupplier);
        if(index != -1){
            supplierList.set(index, updatedSupplier);
        }
    }
    else{
        view.lblMessage.setText("Updated failed!");
    }
    }
    private void handleDeleteSupplier(){
    Supplier selectedSupplier = view.tblSupplier.getSelectionModel().getSelectedItem();
    if(selectedSupplier == null){
        view.lblMessage.setText("Please select a customer to delete");
        return;
    }
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " +selectedSupplier.getSupplierName() + "?");
        Optional<ButtonType> result = confirm.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            if (dao.delete(selectedSupplier.getSupplierID())){
                view.lblMessage.setText("Supplier deleted " + selectedSupplier.getSupplierName() + " successfully");
                supplierList.remove(selectedSupplier);
                clearForm();
            }
            else{
                view.lblMessage.setText("Delete failed!");
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