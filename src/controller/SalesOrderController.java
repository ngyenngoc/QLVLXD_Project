package controller;

import dao.CustomerDAO;
import dao.MaterialDAO;
import dao.SalesOrderDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import model.Material;
import model.SalesOrder;
import model.SalesOrderDetail;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SalesOrderController {

    // Khớp fx:id từ FXML
    @FXML private Label lblDetailID;
    @FXML private Label lblDetailDate;
    @FXML private ComboBox<Customer> cbCustomer; // Đổi từ lblDetailPartner sang cb nếu muốn chọn
    @FXML private ComboBox<Material> cbMaterial;
    @FXML private TextArea txtNotes;
    @FXML private TextField txtTotalAmount;
    @FXML private TextField txtQuantity;
    @FXML private TextField txtPrice;

    // Bảng danh sách hóa đơn (tvOrders)
    @FXML private TableView<SalesOrder> tvOrders;
    @FXML private TableColumn<SalesOrder, String> colId;
    @FXML private TableColumn<SalesOrder, String> colDate;
    @FXML private TableColumn<SalesOrder, String> colCustomerName;
    @FXML private TableColumn<SalesOrder, BigDecimal> colAmount;
    @FXML private TableColumn<SalesOrder, String> colNote;

    @FXML private TableColumn<SalesOrder, String> colType; // Cột Tên vật liệu
    @FXML private TableColumn<SalesOrder, Integer> colPartner; // Cột Số lượng
    @FXML private TableColumn<SalesOrder, BigDecimal> colStatus; // Cột Giá bán

    private SalesOrderDAO orderDAO = new SalesOrderDAO();
    private MaterialDAO materialDAO = new MaterialDAO();
    private CustomerDAO customerDAO = new CustomerDAO();

    private ObservableList<SalesOrder> listOrders = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initTables();
        loadData();

        // 1. Sự kiện chọn dòng trên bảng: Đổ dữ liệu xuống Form
        tvOrders.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showOrderDetail(newVal);
            }
        });

        // 2. Tự động điền giá và tính tiền khi chọn vật liệu
        cbMaterial.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtPrice.setText(newVal.getSalePrice().toString());
                calculateTotal();
            }
        });

        // 3. Tự động tính tiền khi gõ số lượng
        txtQuantity.textProperty().addListener((o, ov, nv) -> calculateTotal());

        handleClear();
    }

    private void initTables() {
        colId.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("orderDateStr"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("notes"));

        // Ánh xạ cho các cột chi tiết dựa trên fx:id trong FXML của bạn
        colType.setCellValueFactory(new PropertyValueFactory<>("materialName"));  // Cột Tên vật liệu
        colPartner.setCellValueFactory(new PropertyValueFactory<>("quantity"));  // Cột Số lượng
        colStatus.setCellValueFactory(new PropertyValueFactory<>("salePrice"));  // Cột Giá bán

        tvOrders.setItems(listOrders);
    }

    private void showOrderDetail(SalesOrder order) {
        lblDetailID.setText(order.getOrderID());
        lblDetailDate.setText(order.getOrderDateStr());
        txtNotes.setText(order.getNotes());
        txtTotalAmount.setText(order.getTotalAmount().toString());

        txtQuantity.setText(String.valueOf(order.getQuantity()));
        txtPrice.setText(order.getSalePrice().toString());

        // Chọn đúng Khách hàng trong ComboBox
        for (Customer c : cbCustomer.getItems()) {
            if (c.getCustomerID().equals(order.getCustomerID())) {
                cbCustomer.setValue(c);
                break;
            }
        }

        // Chọn đúng Vật liệu trong ComboBox
        for (Material m : cbMaterial.getItems()) {
            if (m.getMaterialID().equals(order.getMaterialID())) {
                cbMaterial.setValue(m);
                break;
            }
        }
    }

    private void calculateTotal() {
        try {
            BigDecimal qty = new BigDecimal(txtQuantity.getText().isEmpty() ? "0" : txtQuantity.getText());
            BigDecimal price = new BigDecimal(txtPrice.getText().isEmpty() ? "0" : txtPrice.getText());
            txtTotalAmount.setText(qty.multiply(price).toString());
        } catch (Exception e) {
            txtTotalAmount.setText("0");
        }
    }

    @FXML
    private void handleSearch() {
        String keyword = txtSearch.getText().toLowerCase().trim();
        if (keyword.isEmpty()) {
            tvOrders.setItems(listOrders);
            return;
        }

        ObservableList<SalesOrder> filteredList = FXCollections.observableArrayList();
        for (SalesOrder order : listOrders) {
            if (order.getCustomerName().toLowerCase().contains(keyword) ||
                    order.getOrderID().toLowerCase().contains(keyword)) {
                filteredList.add(order);
            }
        }
        tvOrders.setItems(filteredList);
    }

    private void loadData() {
        cbCustomer.setItems(FXCollections.observableArrayList(customerDAO.getAll()));
        cbMaterial.setItems(FXCollections.observableArrayList(materialDAO.getAll()));
        listOrders.setAll(orderDAO.getAll());
    }

    @FXML
    private void handleAdd() {
        if (cbCustomer.getValue() == null || cbMaterial.getValue() == null) {
            showAlert("Lỗi", "Vui lòng nhập đủ thông tin");
            return;
        }

        // Logic tạo đơn hàng (Vì FXML của bạn hiện tại thiết kế 1 đơn 1 dòng vật liệu)
        // Nếu muốn 1 đơn nhiều vật liệu, bạn cần thêm 1 TableView chi tiết nữa vào FXML
        // Ở đây tôi viết theo hướng đơn giản nhất để khớp với FXML của bạn:

        SalesOrder order = new SalesOrder(
                lblDetailID.getText(),
                LocalDate.now(),
                cbCustomer.getValue().getCustomerID(),
                cbCustomer.getValue().getCustomerName(),
                1,
                txtNotes.getText(),
                new BigDecimal(txtTotalAmount.getText())
        );

        // Tạo 1 list chi tiết ảo chỉ chứa 1 món hàng
        SalesOrderDetail detail = new SalesOrderDetail(0, order.getOrderID(),
                cbMaterial.getValue().getMaterialID(), cbMaterial.getValue().getMaterialName(),
                Integer.parseInt(txtQuantity.getText()), new BigDecimal(txtPrice.getText()));

        if (orderDAO.insertFullOrder(order, java.util.List.of(detail))) {
            showAlert("Thành công", "Đã tạo đơn hàng!");
            listOrders.setAll(orderDAO.getAll());
            handleClear();
        }
    }

    @FXML
    private void handleClear() {
        lblDetailID.setText(orderDAO.generateNewID());
        lblDetailDate.setText(LocalDate.now().toString());
        cbCustomer.setValue(null);
        cbMaterial.setValue(null);
        txtQuantity.clear();
        txtPrice.clear();
        txtNotes.clear();
        txtTotalAmount.setText("0");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML private TextField txtSearch; // Ô nhập từ khóa tìm kiếm


    @FXML private void handleDelete() {} // Khai báo để tránh lỗi FXML action
    @FXML private void handleUpdate() {}
}