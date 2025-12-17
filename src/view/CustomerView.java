package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import model.Customer;


public class CustomerView {
    public final TableView<Customer> tblCustomer = new TableView<>();
    public final Label lblMessage = new Label();

    public final TextField txtCustomerID = new TextField();
    public final TextField txtCustomerName = new TextField();
    public final TextField txtAddress = new TextField();
    public final TextField txtPhoneNumber = new TextField();
    public final TextField txtEmail = new TextField();

    public final Button btnAdd = new Button("Add new");
    public final Button btnUpdate = new Button("Update");
    public final Button btnDelete = new Button("Delete");
    public final Button btnRefresh = new Button("Refresh");

    public Pane createRootNode() {
        AnchorPane root = new AnchorPane();
        root.setPadding(new Insets(10));
        setupTableView();
        GridPane formLayout = createCrudForm();
        AnchorPane.setTopAnchor(tblCustomer, 20.0);
        AnchorPane.setLeftAnchor(tblCustomer, 10.0);
        AnchorPane.setRightAnchor(tblCustomer, 10.0);
        AnchorPane.setTopAnchor(formLayout, 450.0);
        AnchorPane.setLeftAnchor(formLayout, 10.0);
        root.getChildren().addAll(tblCustomer, formLayout);
        return root;
    }

    private void setupTableView() {
        tblCustomer.setPrefSize(1160, 400);
        TableColumn<Customer, String> customerIDCol = new TableColumn<>("Customer id");
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        TableColumn<Customer, String> customerNameCol = new TableColumn<>("Customer name");
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        TableColumn<Customer, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        TableColumn<Customer, String> phoneNumberCol = new TableColumn<>("Phone number");
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        TableColumn<Customer, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        tblCustomer.getColumns().addAll(customerIDCol, customerNameCol, addressCol, phoneNumberCol, emailCol);
        tblCustomer.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private GridPane createCrudForm() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        //Bố cục Form
        grid.add(new Label("Customer id:"), 0, 0);
        grid.add(txtCustomerID, 1, 0);
        grid.add(new Label("Customer name:"), 0, 1);
        grid.add(txtCustomerName, 1, 1);
        grid.add(new Label("Address:"), 0, 2);
        grid.add(txtAddress, 1, 2);
        grid.add(new Label("Phone number:"), 2, 0);
        grid.add(txtPhoneNumber, 3, 0);
        grid.add(new Label("Email:"), 2, 1);
        grid.add(txtEmail, 3, 1);

        HBox ButtonBox = new HBox(10);
        ButtonBox.getChildren().addAll(btnAdd, btnUpdate, btnDelete, btnRefresh);
        grid.add(ButtonBox, 0, 3, 4, 1);
        lblMessage.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        grid.add(lblMessage, 0, 4, 4, 1);
        txtCustomerID.setDisable(true);
        return grid;
    }

}
