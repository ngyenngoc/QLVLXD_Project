package client.controller;

import client.service.SocketClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import shared.Request;
import shared.Response;
import shared.model.Customer;
import shared.model.Material;
import shared.model.SalesOrder;
import shared.model.SalesOrderDetail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class SalesOrderController {

    @FXML private Label lblDetailID;
    @FXML private Label lblDetailDate;
    @FXML private ComboBox<Customer> cbCustomer;
    @FXML private ComboBox<Material> cbMaterial;
    @FXML private TextArea txtNotes;
    @FXML private TextField txtTotalAmount;
    @FXML private TextField txtQuantity;
    @FXML private TextField txtPrice;
    @FXML private TextField txtSearch;

    @FXML private TableView<SalesOrder> tvOrders;
    @FXML private TableColumn<SalesOrder, String> colId;
    @FXML private TableColumn<SalesOrder, String> colDate;
    @FXML private TableColumn<SalesOrder, String> colCustomerName;
    @FXML private TableColumn<SalesOrder, BigDecimal> colAmount;
    @FXML private TableColumn<SalesOrder, String> colNote;
    @FXML private TableColumn<SalesOrder, String> colType;
    @FXML private TableColumn<SalesOrder, Integer> colPartner;
    @FXML private TableColumn<SalesOrder, BigDecimal> colStatus;

    private final ObservableList<SalesOrder> listOrders = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initTables();
        loadData();

        // 1. Sự kiện chọn dòng trên bảng
        tvOrders.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showOrderDetail(newVal);
            }
        });

        // 2. Tự động điền giá khi chọn vật liệu
        cbMaterial.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtPrice.setText(newVal.getSalePrice().toString());
                checkStockWarning(txtQuantity.getText());
                calculateTotal();
            }
        });

        // 3. Tự động tính tiền và check kho khi gõ số lượng
        txtQuantity.textProperty().addListener((obs, oldVal, newVal) -> {
            checkStockWarning(newVal);
            calculateTotal();
        });

        handleClear();
    }

    private void checkStockWarning(String quantityInput) {
        Material selectedMaterial = cbMaterial.getValue();
        if (selectedMaterial == null || quantityInput == null || quantityInput.isEmpty()) {
            txtQuantity.setStyle("");
            return;
        }

        try {
            int qtyRequested = Integer.parseInt(quantityInput);

            // Server xem tồn kho hiện tại của mặt hàng này là bao nhiêu
            Response resStock = SocketClient.sendRequest(new Request("GET_CURRENT_STOCK", selectedMaterial.getMaterialID()));
            if (resStock.isSuccess()) {
                int currentStock = (int) resStock.getData();
                if (qtyRequested > currentStock) {
                    txtQuantity.setStyle("-fx-border-color: red; -fx-control-inner-background: #ffcccc;");
                    Tooltip tooltip = new Tooltip("Vượt quá tồn kho mạng! Hiện còn: " + currentStock);
                    txtQuantity.setTooltip(tooltip);
                } else {
                    txtQuantity.setStyle("");
                    txtQuantity.setTooltip(null);
                }
            }
        } catch (NumberFormatException e) {
            // Nhập chữ không xử lý kho
        }
    }

    private void initTables() {
        colId.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("orderDateStr"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("notes"));
        colType.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        colPartner.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        tvOrders.setItems(listOrders);
    }

    private void showOrderDetail(SalesOrder order) {
        lblDetailID.setText(order.getOrderID());
        lblDetailDate.setText(order.getOrderDateStr());
        txtNotes.setText(order.getNotes());
        txtTotalAmount.setText(order.getTotalAmount().toString());
        txtQuantity.setText(String.valueOf(order.getQuantity()));
        txtPrice.setText(order.getSalePrice().toString());

        for (Customer c : cbCustomer.getItems()) {
            if (c.getCustomerID().equals(order.getCustomerID())) {
                cbCustomer.setValue(c);
                break;
            }
        }
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
        // 1. Lấy dữ liệu nạp cho ComboBox Khách hàng qua mạng
        Response resCust = SocketClient.sendRequest(new Request("GET_ALL_CUSTOMERS", null));
        if (resCust.isSuccess()) {
            cbCustomer.setItems(FXCollections.observableArrayList((List<Customer>) resCust.getData()));
        }

        // 2. Lấy dữ liệu nạp cho ComboBox Vật liệu qua mạng
        Response resMat = SocketClient.sendRequest(new Request("GET_ALL_MATERIALS", null));
        if (resMat.isSuccess()) {
            cbMaterial.setItems(FXCollections.observableArrayList((List<Material>) resMat.getData()));
        }

        // 3. Lấy danh sách hóa đơn từ Server mạng đổ vào bảng chính
        Response resOrders = SocketClient.sendRequest(new Request("GET_ALL_ORDERS", null));
        if (resOrders.isSuccess()) {
            listOrders.setAll((List<SalesOrder>) resOrders.getData());
        }
    }

    @FXML
    private void handleAdd() {
        if (cbCustomer.getValue() == null || cbMaterial.getValue() == null || txtQuantity.getText().isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập điền đủ thông tin đơn hàng!");
            return;
        }

        try {
            Material selectedMaterial = cbMaterial.getValue();
            int qtyInput = Integer.parseInt(txtQuantity.getText());

            // Hỏi Server xem lượng tồn kho thực tế có đủ đáp ứng không trước khi tạo hóa đơn
            Response resStock = SocketClient.sendRequest(new Request("GET_CURRENT_STOCK", selectedMaterial.getMaterialID()));
            if (resStock.isSuccess()) {
                int currentStock = (int) resStock.getData();
                if (qtyInput > currentStock) {
                    showAlert("Lỗi kho", "Số lượng bán (" + qtyInput + ") vượt quá tồn kho hiện tại (" + currentStock + ")!");
                    return;
                }
            }

            // Đóng gói đối tượng Đơn hàng
            SalesOrder order = new SalesOrder(
                    lblDetailID.getText(),
                    LocalDate.now(),
                    cbCustomer.getValue().getCustomerID(),
                    cbCustomer.getValue().getCustomerName(),
                    1,
                    txtNotes.getText(),
                    new BigDecimal(txtTotalAmount.getText())
            );

            // Đóng gói Chi tiết đơn hàng đi kèm
            SalesOrderDetail detail = new SalesOrderDetail(0, order.getOrderID(),
                    cbMaterial.getValue().getMaterialID(), cbMaterial.getValue().getMaterialName(),
                    qtyInput, new BigDecimal(txtPrice.getText()));

            // Gộp chung 2 đối tượng vào một danh sách cấu trúc để truyền qua dây mạng 1 lần duy nhất
            List<Object> orderPayload = List.of(order, List.of(detail));

            Response response = SocketClient.sendRequest(new Request("ADD_FULL_ORDER", orderPayload));
            if (response.isSuccess()) {
                showAlert("Thành công", "Đã tạo đơn hàng và cập nhật trừ kho vật liệu qua mạng!");
                loadData();
                handleClear();
            } else {
                showAlert("Lỗi", response.getMessage());
            }
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Số lượng hóa đơn phải là số nguyên hợp lệ!");
        }
    }

    @FXML
    private void handleUpdate() {
        SalesOrder selectedOrder = tvOrders.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) {
            showAlert("Thông báo", "Vui lòng chọn đơn hàng cần cập nhật từ danh sách!");
            return;
        }

        if (cbCustomer.getValue() == null || cbMaterial.getValue() == null || txtQuantity.getText().isEmpty()) {
            showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin trước khi cập nhật!");
            return;
        }

        try {
            // Thu thập dữ liệu mới từ Form
            selectedOrder.setCustomerID(cbCustomer.getValue().getCustomerID());
            selectedOrder.setCustomerName(cbCustomer.getValue().getCustomerName());
            selectedOrder.setNotes(txtNotes.getText());
            selectedOrder.setTotalAmount(new BigDecimal(txtTotalAmount.getText()));

            // Cập nhật thông tin chi tiết số lượng và giá
            selectedOrder.setQuantity(Integer.parseInt(txtQuantity.getText()));
            selectedOrder.setSalePrice(new BigDecimal(txtPrice.getText()));

            selectedOrder.setMaterialID(cbMaterial.getValue().getMaterialID());
            selectedOrder.setMaterialName(cbMaterial.getValue().getMaterialName());

            // Gửi lệnh cập nhật qua mạng Socket lên Server
            Response response = SocketClient.sendRequest(new Request("UPDATE_ORDER", selectedOrder));

            if (response.isSuccess()) {
                showAlert("Thành công", "Cập nhật đơn hàng và số lượng mua thành công!");

                // QUAN TRỌNG: Làm mới dữ liệu và bắt TableView vẽ lại giao diện
                loadData();
                tvOrders.refresh(); // Ép giao diện JavaFX cập nhật lại các cột (Số lượng, Đơn giá...)
                handleClear();      // Đưa form về trạng thái trống và sinh mã mới
            } else {
                showAlert("Lỗi", response.getMessage());
            }
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Số lượng hoặc giá bán phải là số hợp lệ!");
        } catch (Exception e) {
            showAlert("Lỗi", "Dữ liệu nhập vào form không hợp lệ!");
            e.printStackTrace();
        }
    }
    @FXML
    private void handleDelete() {
        SalesOrder selected = tvOrders.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Thông báo", "Vui lòng chọn hóa đơn cần xóa!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Xóa hóa đơn " + selected.getOrderID() + "?", ButtonType.YES, ButtonType.NO);
        if (confirm.showAndWait().get() == ButtonType.YES) {
            Response response = SocketClient.sendRequest(new Request("DELETE_ORDER", selected.getOrderID()));
            if (response.isSuccess()) {
                showAlert("Thành công", "Đã xóa hóa đơn ra khỏi mạng!");
                loadData();
                handleClear();
            } else {
                showAlert("Lỗi", response.getMessage());
            }
        }
    }

    @FXML
    private void handleClear() {
        Response resId = SocketClient.sendRequest(new Request("GENERATE_ORDER_ID", null));
        if (resId.isSuccess()) {
            lblDetailID.setText((String) resId.getData());
        } else {
            lblDetailID.setText("ERROR");
        }
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
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}