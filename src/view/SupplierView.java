package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import model.Supplier;

public class SupplierView {
    public final TableView<Supplier> tblSupplier = new TableView<>();
    public final Label lblMessage = new Label();

    public final TextField txtSupplierID = new TextField();
    public final TextField txtSupplierName = new TextField();
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
        AnchorPane.setTopAnchor(tblSupplier, 20.0);
        AnchorPane.setLeftAnchor(tblSupplier, 10.0);
        AnchorPane.setRightAnchor(tblSupplier, 10.0);
        AnchorPane.setTopAnchor(formLayout, 450.0);
        AnchorPane.setLeftAnchor(formLayout, 10.0);
        root.getChildren().addAll(tblSupplier, formLayout);
        return root;
    }

    private void setupTableView() {
        tblSupplier.setPrefSize(1160, 400);
        TableColumn<Supplier, String> supplierIDCol = new TableColumn<>("Supplier id");
        supplierIDCol.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
        TableColumn<Supplier, String> supplierNameCol = new TableColumn<>("Supplier name");
        supplierNameCol.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        TableColumn<Supplier, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        TableColumn<Supplier, String> phoneNumberCol = new TableColumn<>("Phone number");
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        TableColumn<Supplier, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        tblSupplier.getColumns().addAll(supplierIDCol, supplierNameCol, addressCol, phoneNumberCol, emailCol);
        tblSupplier.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    private GridPane createCrudForm() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        //Bố cục Form
        grid.add(new Label("Supplier id:"), 0, 0); grid.add(txtSupplierID, 1, 0);
        grid.add(new Label("Supplier name:  "), 0, 1); grid.add(txtSupplierName, 1 ,1);
        grid.add(new Label("Address: "), 0, 2); grid.add(txtAddress, 1 ,2);
        grid.add(new Label("Phone number: "), 2, 0); grid.add(txtPhoneNumber, 3 ,0);
        grid.add(new Label("Email: "), 2, 1); grid.add(txtEmail, 3 ,1);

        HBox ButtonBox = new HBox(10);
        ButtonBox.getChildren().addAll(btnAdd, btnUpdate, btnDelete, btnRefresh);
        grid.add(ButtonBox, 0, 3, 4, 1);
        lblMessage.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        grid.add(lblMessage, 0, 4, 4, 1);
        txtSupplierID.setDisable(true);
        return grid;
    }
}