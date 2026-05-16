package client.controller;

import client.service.SocketClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import shared.Request;
import shared.Response;
import shared.model.Category;
import shared.model.Material;
import shared.model.Supplier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class MaterialController {
    @FXML private TableView<Material> tblMaterial;
    @FXML private TableColumn<Material, String> colID, colName, colUnit, colDesc, colCategoryName;
    @FXML private TableColumn<Material, BigDecimal> colPurchase, colSale;
    @FXML private TableColumn<Material, Integer> colStock;
    @FXML private TextField txtMaterialID, txtMaterialName, txtPurchasePrice, txtSalePrice, txtStockQuantity, txtDescription;

    @FXML private ComboBox<Category> cbCategory;
    @FXML private ComboBox<String> cbUnit;

    @FXML private TableColumn<Material, String> colSPLName;
    @FXML private ComboBox<Supplier> cbSupplier;

    @FXML private Label lblMessage;
    @FXML private Button btnAdd, btnUpdate, btnDelete, btnRefresh;

    private final ObservableList<Material> materialList = FXCollections.observableArrayList();
    private final ObservableList<Category> categoryList = FXCollections.observableArrayList();
    private final ObservableList<Supplier> supplierList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Cấu hình ComboBox Đơn vị
        ObservableList<String> units = FXCollections.observableArrayList(
                "Cái", "Bộ", "Kg", "Mét", "Thùng", "Bao", "Viên"
        );
        cbUnit.setItems(units);
        cbUnit.setPromptText("Chọn đơn vị");

        // 2. Lấy danh mục Loại vật liệu qua Mạng
        Response resCats = SocketClient.sendRequest(new Request("GET_ALL_CATEGORIES", null));
        if (resCats.isSuccess()) {
            List<Category> listCats = (List<Category>) resCats.getData();
            categoryList.setAll(listCats);
            cbCategory.setItems(categoryList);
        }
        cbCategory.setPromptText("Chọn loại vật liệu");

        // 3. Ánh xạ cột bảng
        colID.setCellValueFactory(new PropertyValueFactory<>("materialID"));
        colCategoryName.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        colName.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        colPurchase.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        colSale.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colSPLName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));

        tblMaterial.setItems(materialList);
        handleloadMaterialData();

        // 4. Sự kiện click dòng hiển thị chi tiết
        tblMaterial.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) displayDetails(newSelection);
                });

        // 5. Lấy danh sách Nhà cung cấp qua Mạng
        Response resSpls = SocketClient.sendRequest(new Request("GET_ALL_SUPPLIERS", null));
        if (resSpls.isSuccess()) {
            List<Supplier> listSpls = (List<Supplier>) resSpls.getData();
            supplierList.setAll(listSpls);
            cbSupplier.setItems(supplierList);
        }
    }

    @FXML
    private void loadMaterialData() {
        // Lấy danh sách vật liệu từ Server
        Response response = SocketClient.sendRequest(new Request("GET_ALL_MATERIALS", null));
        if (response.isSuccess()) {
            List<Material> list = (List<Material>) response.getData();
            materialList.clear();
            materialList.addAll(list);
        } else {
            lblMessage.setText("Lỗi mạng: " + response.getMessage());
        }
    }

    @FXML
    private void handleAddMaterial() {
        if (txtMaterialName.getText().isEmpty() || cbUnit.getValue() == null || cbCategory.getValue() == null || cbSupplier.getValue() == null) {
            lblMessage.setText("Lỗi: Vui lòng nhập đầy đủ thông tin mẫu!");
            return;
        }

        try {
            int catID = cbCategory.getValue().getCategoryID();
            String SPLID = cbSupplier.getValue().getSupplierID();

            Material newMaterial = new Material(
                    txtMaterialID.getText(), // ID đã được đồng bộ từ Server lúc clearForm
                    txtMaterialName.getText(),
                    cbUnit.getValue(),
                    new BigDecimal(txtPurchasePrice.getText()),
                    new BigDecimal(txtSalePrice.getText()),
                    Integer.parseInt(txtStockQuantity.getText()),
                    txtDescription.getText(),
                    catID,
                    "",
                    SPLID,
                    ""
            );

            // Gửi lệnh thêm mới lên Server
            Response response = SocketClient.sendRequest(new Request("ADD_MATERIAL", newMaterial));
            if (response.isSuccess()) {
                lblMessage.setText("Thêm thành công: " + newMaterial.getMaterialName());
                loadMaterialData();
                clearForm();
            } else {
                lblMessage.setText("Lỗi: " + response.getMessage());
            }
        } catch (NumberFormatException e) {
            lblMessage.setText("Lỗi: Giá tiền hoặc số lượng phải là số hợp lệ!");
        } catch (Exception e) {
            lblMessage.setText("Lỗi hệ thống: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateMaterial() {
        Material selectedMaterial = tblMaterial.getSelectionModel().getSelectedItem();
        if (selectedMaterial == null) {
            lblMessage.setText("Vui lòng chọn một vật liệu để sửa");
            return;
        }
        if (cbCategory.getValue() == null || cbSupplier.getValue() == null) {
            lblMessage.setText("Vui lòng nhập chọn đầy đủ Loại và Nhà cung cấp!");
            return;
        }

        try {
            int catID = cbCategory.getValue().getCategoryID();
            String SPLID = cbSupplier.getValue().getSupplierID();
            Material updatedMaterial = new Material(
                    selectedMaterial.getMaterialID(),
                    txtMaterialName.getText(),
                    cbUnit.getValue(),
                    new BigDecimal(txtPurchasePrice.getText()),
                    new BigDecimal(txtSalePrice.getText()),
                    Integer.parseInt(txtStockQuantity.getText()),
                    txtDescription.getText(),
                    catID,
                    "",
                    SPLID,
                    ""
            );

            // Gửi lệnh cập nhật lên Server
            Response response = SocketClient.sendRequest(new Request("UPDATE_MATERIAL", updatedMaterial));
            if (response.isSuccess()) {
                lblMessage.setText("Cập nhật thành công!");
                loadMaterialData();
                clearForm();
            } else {
                lblMessage.setText("Lỗi: " + response.getMessage());
            }
        } catch (Exception e) {
            lblMessage.setText("Lỗi dữ liệu: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteMaterial() {
        Material selectedMaterial = tblMaterial.getSelectionModel().getSelectedItem();
        if (selectedMaterial == null) {
            lblMessage.setText("Vui lòng chọn vật liệu cần xóa");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Xóa vật liệu " + selectedMaterial.getMaterialName() + "?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Gửi ID lên server yêu cầu xóa
            Response response = SocketClient.sendRequest(new Request("DELETE_MATERIAL", selectedMaterial.getMaterialID()));
            if (response.isSuccess()) {
                lblMessage.setText("Đã xóa thành công vật liệu.");
                loadMaterialData();
                clearForm();
            } else {
                lblMessage.setText("Lỗi: " + response.getMessage());
            }
        }
    }

    @FXML
    private void handleloadMaterialData() {
        loadMaterialData();
        clearForm();
    }

    private void displayDetails(Material material) {
        txtMaterialID.setText(material.getMaterialID());
        txtMaterialName.setText(material.getMaterialName());
        cbUnit.setValue(material.getUnit());
        txtPurchasePrice.setText(material.getPurchasePrice().toString());
        txtSalePrice.setText(material.getSalePrice().toString());
        txtStockQuantity.setText(String.valueOf(material.getStockQuantity()));
        txtDescription.setText(material.getDescription());

        for (Category c : cbCategory.getItems()) {
            if (c.getCategoryID() == material.getCategoryID()) {
                cbCategory.setValue(c);
                break;
            }
        }
        for (Supplier s : cbSupplier.getItems()) {
            if (s.getSupplierID().equals(material.getSupplierID())) {
                cbSupplier.setValue(s);
                break;
            }
        }
        lblMessage.setText("");
    }

    private void clearForm() {
        // Xin mã ID mới tự động sinh ra từ Server
        Response response = SocketClient.sendRequest(new Request("GENERATE_MATERIAL_ID", null));
        if (response.isSuccess()) {
            txtMaterialID.setText((String) response.getData());
        } else {
            txtMaterialID.setText("ERROR");
        }

        txtMaterialName.clear();
        cbUnit.setValue(null);
        txtPurchasePrice.clear();
        txtSalePrice.clear();
        txtStockQuantity.clear();
        txtDescription.clear();
        cbCategory.setValue(null);
        cbSupplier.setValue(null);
        tblMaterial.getSelectionModel().clearSelection();
    }
}