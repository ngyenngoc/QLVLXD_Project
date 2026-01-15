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
    @FXML private ComboBox<String> cbPaymentMethod;
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

    private SalesOrderDAO orderDAO = new SalesOrderDAO();
    private MaterialDAO materialDAO = new MaterialDAO();
    private CustomerDAO customerDAO = new CustomerDAO();

    private ObservableList<SalesOrder> listOrders = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initTables();
        loadData();

        // Tự động điền giá khi chọn vật liệu
        cbMaterial.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtPrice.setText(newVal.getSalePrice().toString());
            }
        });

        handleClear();
    }

    private void initTables() {
        // Ánh xạ cột cho tvOrders theo đúng tên thuộc tính trong model SalesOrder
        colId.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("orderDateStr"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("notes"));

        tvOrders.setItems(listOrders);
    }

    private void loadData() {
        cbCustomer.setItems(FXCollections.observableArrayList(customerDAO.getAll()));
        cbMaterial.setItems(FXCollections.observableArrayList(materialDAO.getAll()));
        cbPaymentMethod.setItems(FXCollections.observableArrayList("Tiền mặt", "Chuyển khoản"));
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
                cbPaymentMethod.getValue(),
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

    @FXML private void handleDelete() {} // Khai báo để tránh lỗi FXML action
    @FXML private void handleUpdate() {}
}