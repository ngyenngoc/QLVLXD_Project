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

    @FXML private Label lblDetailID;
    @FXML private Label lblDetailDate;
    @FXML private ComboBox<Customer> cbCustomer; // Đổi từ lblDetailPartner sang cb nếu muốn chọn
    @FXML private ComboBox<Material> cbMaterial;
    @FXML private TextArea txtNotes;
    @FXML private TextField txtTotalAmount;
    @FXML private TextField txtQuantity;
    @FXML private TextField txtPrice;

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

        txtQuantity.textProperty().addListener((obs, oldVal, newVal) -> {
            checkStockWarning(newVal);
            calculateTotal();
        });

        // Kiểm tra lại nếu người dùng đổi vật liệu khác khi đã nhập số lượng
        cbMaterial.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtPrice.setText(newVal.getSalePrice().toString());
                checkStockWarning(txtQuantity.getText()); // Kiểm tra lại kho của vật liệu mới
                calculateTotal();
            }
        });

        handleClear();

        handleClear();
    }

    private void checkStockWarning(String quantityInput) {
        Material selectedMaterial = cbMaterial.getValue();

        // Nếu chưa chọn vật liệu hoặc ô số lượng trống thì không làm gì
        if (selectedMaterial == null || quantityInput == null || quantityInput.isEmpty()) {
            txtQuantity.setStyle(""); // Reset về mặc định
            return;
        }

        try {
            int qtyRequested = Integer.parseInt(quantityInput);
            int currentStock = materialDAO.getCurrentStock(selectedMaterial.getMaterialID());

            if (qtyRequested > currentStock) {
                // Đổi màu nền hoặc viền đỏ để cảnh báo trực quan
                txtQuantity.setStyle("-fx-border-color: red; -fx-control-inner-background: #ffcccc;");

                // Bạn có thể dùng Tooltip để hiện số dư mà không cần bật Alert liên tục (gây phiền)
                Tooltip tooltip = new Tooltip("Vượt quá tồn kho! Hiện còn: " + currentStock);
                txtQuantity.setTooltip(tooltip);
            } else {
                // Trở về bình thường nếu hợp lệ
                txtQuantity.setStyle("");
                txtQuantity.setTooltip(null);
            }
        } catch (NumberFormatException e) {
            // Nếu nhập chữ thì để calculateTotal xử lý hoặc báo lỗi định dạng
        }
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
        Material selected = cbMaterial.getValue();
        if (selected == null) {
            showAlert("Lỗi", "Vui lòng chọn vật liệu!");
            return;
        }

        if (cbCustomer.getValue() == null || cbMaterial.getValue() == null || txtQuantity.getText().isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập đủ thông tin");
            return;
        }
        try {
            Material selectedMaterial = cbMaterial.getValue();
            int qtyInput = Integer.parseInt(txtQuantity.getText());
            int currentStock = materialDAO.getCurrentStock(selectedMaterial.getMaterialID());

            if (qtyInput > currentStock) {
                showAlert("Lỗi kho", "Số lượng bán (" + qtyInput + ") vượt quá tồn kho hiện tại (" + currentStock + ")!");
                return;
            }
            SalesOrder order = new SalesOrder(
                    lblDetailID.getText(),
                    LocalDate.now(),
                    cbCustomer.getValue().getCustomerID(),
                    cbCustomer.getValue().getCustomerName(),
                    1,
//                cbPaymentMethod.getValue(),
                    txtNotes.getText(),
                    new BigDecimal(txtTotalAmount.getText())
            );

            // Tạo 1 list chi tiết ảo chỉ chứa 1 món hàng
            SalesOrderDetail detail = new SalesOrderDetail(0, order.getOrderID(),
                    cbMaterial.getValue().getMaterialID(), cbMaterial.getValue().getMaterialName(),
                    Integer.parseInt(txtQuantity.getText()), new BigDecimal(txtPrice.getText()));

            if (orderDAO.insertFullOrder(order, java.util.List.of(detail))) {
                showAlert("Thành công", "Đã tạo đơn hàng và cập nhật kho vật liệu!");
                loadData();
                handleClear();
            } else {
                showAlert("Lỗi", "Không thể hoàn thành đơn hàng!");
            }
        }
        catch(NumberFormatException e){
            showAlert("Lỗi", "Số lượng phải là số nguyên hợp lệ!");
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


    @FXML
    private void handleDelete() {
        SalesOrder selected = tvOrders.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Thông báo", "Vui lòng chọn hóa đơn cần xóa!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Xóa hóa đơn " + selected.getOrderID() + "?", ButtonType.YES, ButtonType.NO);
        if (confirm.showAndWait().get() == ButtonType.YES) {
            if (orderDAO.delete(selected.getOrderID())) {
                showAlert("Thành công", "Đã xóa hóa đơn!");
                loadData();
                handleClear();
            } else {
                showAlert("Lỗi", "Không thể xóa (có thể do ràng buộc dữ liệu chi tiết)!");
            }
        }
    }

    @FXML
    private void handleUpdate() {
        SalesOrder selectedOrder = tvOrders.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) {
            showAlert("Thông báo", "Vui lòng chọn đơn hàng cần cập nhật từ danh sách!");
            return;
        }

        // Thu thập dữ liệu từ form
        try {
            selectedOrder.setCustomerID(cbCustomer.getValue().getCustomerID());
            selectedOrder.setCustomerName(cbCustomer.getValue().getCustomerName());
            selectedOrder.setNotes(txtNotes.getText());
            selectedOrder.setTotalAmount(new BigDecimal(txtTotalAmount.getText()));

            // Cập nhật các thông tin chi tiết nếu cần (Tùy vào cấu trúc DB của bạn)
            selectedOrder.setQuantity(Integer.parseInt(txtQuantity.getText()));
            selectedOrder.setSalePrice(new BigDecimal(txtPrice.getText()));

            // Gọi DAO cập nhật (Giả sử bạn đã có hàm update trong SalesOrderDAO)
            if (orderDAO.update(selectedOrder)) {
                showAlert("Thành công", "Cập nhật đơn hàng thành công!");
                listOrders.setAll(orderDAO.getAll()); // Refresh bảng
                loadData();
            } else {
                showAlert("Lỗi", "Cập nhật thất bại!");
            }
        } catch (Exception e) {
            showAlert("Lỗi", "Dữ liệu nhập vào không hợp lệ!");
        }
    }
    }
