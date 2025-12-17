package controller;


import dao.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import model.Customer;
import view.CustomerView;

import java.util.List;
import java.util.Optional;


public class CustomerController {
    private final CustomerDAO dao= new CustomerDAO();
    private final CustomerView view= new CustomerView();
    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();
    public Pane getCustomerManagementView(){
        initialize();
        return view.createRootNode();
    }
    public void initialize(){
        view.tblCustomer.setItems(customerList);
        setupEventHandlers();
        loadCustomerData();
    }
    private void loadCustomerData(){
        List<Customer> list = dao.getAll();
        customerList.clear();
        customerList.addAll(list);
    }
    private void setupEventHandlers() {
        view.btnAdd.setOnAction(e -> handleAddCustomer());
        view.btnUpdate.setOnAction(e -> handleUpdateCustomer());
        view.btnDelete.setOnAction(e -> handleDeleteCustomer());
        view.btnRefresh.setOnAction(e -> loadCustomerData());
        // Gán Listener cho TableView
        view.tblCustomer.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        displayDetails(newSelection);
                    }
                });
    }
    //Xử lý CRUD
    private void handleAddCustomer(){
        Customer newCustomer = new Customer(dao.generateNewId(), view.txtCustomerName.getText(), view.txtAddress.getText(), view.txtPhoneNumber.getText(), view.txtEmail.getText());
        if (dao.insert(newCustomer)){
            view.lblMessage.setText("Customer added " +newCustomer.getCustomerName() + " successfully!");
            customerList.add(newCustomer);
            clearForm();
        }
        else{
            view.lblMessage.setText("Failed to add customer!");
        }
    }
    private void handleUpdateCustomer(){
        Customer selectedCustomer = view.tblCustomer.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null){
            view.lblMessage.setText("Please select a customer to edit");
            return;
        }
        Customer updatedCustomer = new Customer(
                view.txtCustomerID.getText(), view.txtCustomerName.getText(), view.txtAddress.getText(), view.txtPhoneNumber.getText(),view.txtEmail.getText()
        );
        if(dao.update(updatedCustomer)){
            view.lblMessage.setText("Customer updated successfully!");
            int index = customerList.indexOf(selectedCustomer);
            if(index != -1){
                customerList.set(index, updatedCustomer);
            }
        }
        else{
            view.lblMessage.setText("Updated failed!");
        }
    }
    private void handleDeleteCustomer(){
        Customer selectedCustomer = view.tblCustomer.getSelectionModel().getSelectedItem();
        if(selectedCustomer == null){
            view.lblMessage.setText("Please select a customer to delete");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " +selectedCustomer.getCustomerName() + "?");
        Optional<ButtonType> result = confirm.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            if (dao.delete(selectedCustomer.getCustomerID())){
                view.lblMessage.setText("Customer deleted " + selectedCustomer.getCustomerName() + " successfully");
                customerList.remove(selectedCustomer);
                clearForm();
            }
            else{
                view.lblMessage.setText("Delete failed!");
            }
        }
    }
    private void displayDetails(Customer Customer){
        view.txtCustomerID.setText(Customer.getCustomerID());
        view.txtCustomerName.setText(Customer.getCustomerName());
        view.txtAddress.setText(Customer.getAddress());
        view.txtPhoneNumber.setText(Customer.getPhoneNumber());
        view.txtEmail.setText(Customer.getEmail());
        view.lblMessage.setText("");
    }
    private void clearForm(){
        view.txtCustomerID.clear();
        view.txtCustomerName.clear();
        view.txtAddress.clear();
        view.txtPhoneNumber.clear();
        view.txtEmail.clear();
    }
}
