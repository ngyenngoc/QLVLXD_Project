package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import model.Product;


import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductView {
    public final TableView<Product> tblProduct = new TableView<>();
    public final Label lblMessage = new Label();
    public final TextField txtProductID = new TextField();
    public final TextField txtSupplierID = new TextField();
    public final TextField txtProductName = new TextField();
    public final TextField txtProductCategory = new TextField();
    public final TextField txtDescription = new TextField();
    public final TextField txtUnit = new TextField();
    public final TextField txtPurchasePrice = new TextField();
    public final TextField txtSellingPrice = new TextField();
    public final TextField txtstockQuantity = new TextField();
    public final ComboBox<String> cmbStatus = new ComboBox<>();
    public final DatePicker dpLastStockUpdate =  new DatePicker(LocalDate.now());
    public final DatePicker dpCreatedAt =  new DatePicker(LocalDate.now());
    public final DatePicker dpUpdatedAt =  new DatePicker(LocalDate.now());
    public final TextField txtAdminID = new TextField();

    public final Button btnAdd = new Button("Thêm mới");
    public final Button btnUpdate = new Button("Cập nhật");
    public final Button btnDelete = new Button("Xóa");
    public final Button btnRefresh = new Button("Thêm mới");

    public Pane createRootNode(){
        AnchorPane root = new AnchorPane();
        root.setPadding(new Insets(10));

        setupTableView();
        GridPane formLayout = createCrudForm();
        AnchorPane.setTopAnchor(tblProduct, 20.0);
        AnchorPane.setLeftAnchor(tblProduct, 10.0);
        AnchorPane.setRightAnchor(tblProduct, 10.0);
        AnchorPane.setTopAnchor(formLayout, 450.0);
        AnchorPane.setLeftAnchor(formLayout, 10.0);
        root.getChildren().addAll(tblProduct, formLayout); // Thêm các thành phần vào root
        return root;
    }
    private void setupTableView(){
        tblProduct.setPrefSize(1160,400);
        TableColumn<Product, String> productIDCol = new TableColumn<>("Mã sản phẩm");
        productIDCol.setCellValueFactory( new PropertyValueFactory<>("productID"));
        TableColumn<Product, String> supplierIDCol = new TableColumn<>("Mã nhà cung cấp");
        supplierIDCol.setCellValueFactory( new PropertyValueFactory<>("supplierID"));
        TableColumn<Product, String> productNameCol = new TableColumn<>("Tên sẩn phẩm");
        productNameCol.setCellValueFactory( new PropertyValueFactory<>("productName"));
        TableColumn<Product, String> productCategoryCol = new TableColumn<>("Loại sản phẩm");
        productCategoryCol.setCellValueFactory( new PropertyValueFactory<>("productCategory"));
        TableColumn<Product, String> descriptionCol = new TableColumn<>("Mô tả");
        descriptionCol.setCellValueFactory( new PropertyValueFactory<>("description"));
        TableColumn<Product, String> unitCol = new TableColumn<>("Đơn vị tính");
        unitCol.setCellValueFactory( new PropertyValueFactory<>("unit"));
        TableColumn<Product, BigDecimal> purchasePricCol = new TableColumn<>("Giá nhập");
        purchasePricCol.setCellValueFactory( new PropertyValueFactory<>("purchasePrice"));
        TableColumn<Product, BigDecimal> sellingPriceCol = new TableColumn<>("Giá bán");
        sellingPriceCol.setCellValueFactory( new PropertyValueFactory<>("sellingPrice"));
        TableColumn<Product, String> stockQuantityCol = new TableColumn<>("Số lượng tồn kho");
        stockQuantityCol.setCellValueFactory( new PropertyValueFactory<>("stockQuantity"));
        TableColumn<Product, Integer> statusCol = new TableColumn<>("Trạng thái");
        statusCol.setCellValueFactory( new PropertyValueFactory<>("status"));
        TableColumn<Product, String> lastStockUpdateCol = new TableColumn<>("Ngày cập nhật tồn kho");
        lastStockUpdateCol.setCellValueFactory( new PropertyValueFactory<>("lastStockUpdate"));
        TableColumn<Product, String> createdAtCol = new TableColumn<>("Ngày tạp");
        createdAtCol.setCellValueFactory( new PropertyValueFactory<>("createdAt"));
        TableColumn<Product, String> updatedAtCol = new TableColumn<>("Ngày cập nhật");
        updatedAtCol.setCellValueFactory( new PropertyValueFactory<>("updatedAt"));
        TableColumn<Product, String> adminIDCol = new TableColumn<>("Mã người dùng");
        adminIDCol.setCellValueFactory( new PropertyValueFactory<>("adminID"));
        tblProduct.getColumns().addAll(productIDCol, supplierIDCol, productNameCol, descriptionCol, unitCol, purchasePricCol, sellingPriceCol, stockQuantityCol, statusCol, lastStockUpdateCol, createdAtCol, updatedAtCol, adminIDCol);
        tblProduct.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    private GridPane createCrudForm(){
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        //Bố cục Form
        grid.add(new Label("Mã sản phẩm: "), 0, 0); grid.add(txtProductID, 1,0);
        grid.add(new Label("Mã nhà cung cấp: "), 0,1); grid.add(txtSupplierID, 1,1);
        grid.add(new Label("Tên sản phẩm: "), 0, 2); grid.add(txtProductName,1,2);
        grid.add(new Label("Loại sản phẩm: "), 0, 3); grid.add(txtProductCategory,1,3);
        grid.add(new Label("Mô tả: "), 0, 4); grid.add(txtDescription,1,4);
        grid.add(new Label("Đơn vị tính: "), 2, 0); grid.add(txtUnit,3,0);
        grid.add(new Label("Giá nhập: "), 2, 1); grid.add(txtPurchasePrice,3,1);
        grid.add(new Label("Giá bán: "), 2, 2); grid.add(txtSellingPrice,3,2);
        grid.add(new Label("Số lượng tồn kho : "), 2,3); grid.add(txtstockQuantity, 3,3);
        grid.add(new Label("Trạng thái: "), 2, 4); grid.add(cmbStatus,3,4);
        grid.add(new Label("Ngày cập nhật tồn kho: "), 4, 0); grid.add(dpLastStockUpdate,5,0);
        grid.add(new Label("Ngày tạo: "), 4, 1); grid.add(dpCreatedAt,5,1);
        grid.add(new Label("Ngày cập nhật: "), 4, 2); grid.add(dpUpdatedAt,5,2);
        grid.add(new Label("Mã người dùng"), 4, 3); grid.add(txtAdminID,5,3);

        HBox ButtonBox = new HBox(10);
        ButtonBox.getChildren().addAll(btnAdd, btnUpdate, btnDelete, btnRefresh);
        grid.add(ButtonBox, 0, 5, 4, 1);
        lblMessage.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        grid.add(lblMessage, 0, 6, 4, 1);
        txtProductID.setDisable(true);
        return grid;
    }
}
