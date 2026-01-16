package controller;

import dao.CategoryDAO;
import dao.MaterialDAO;
import dao.SupplierDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Category;
import model.Material;
import model.Supplier;

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

    private final MaterialDAO dao = new MaterialDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO(); // [THÊM MỚI] Để lấy danh sách loại
    private final SupplierDAO supplierDAO = new SupplierDAO();
    private final ObservableList<Material> materialList = FXCollections.observableArrayList();
    private final ObservableList<Category> categoryList = FXCollections.observableArrayList(); // [THÊM MỚI] List cho ComboBox
    private final ObservableList<Supplier> supplierList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Cấu hình ComboBox Đơn vị
        ObservableList<String> units = FXCollections.observableArrayList(
                "Cái", "Bộ", "Kg", "Mét", "Thùng", "Bao", "Viên"
        );
        cbUnit.setItems(units);
        cbUnit.setPromptText("Chọn đơn vị");

        List<Category> listCats = categoryDAO.getAll();
        categoryList.addAll(listCats);
        cbCategory.setItems(categoryList);
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

        // 4. Sự kiện click dòng
        tblMaterial.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) displayDetails(newSelection);
                });
        supplierList.setAll(supplierDAO.getAll());
        cbSupplier.setItems(supplierList);
    }

    @FXML
    private void loadMaterialData() {
        List<Material> list = dao.getAll();
        materialList.clear();
        materialList.addAll(list);
    }

    @FXML
    private void handleAddMaterial() {
        // Kiểm tra dữ liệu đầu vào
        if (txtMaterialName.getText().isEmpty() || cbUnit.getValue() == null || cbCategory.getValue() == null) {
            lblMessage.setText("Lỗi: Vui lòng nhập tên, đơn vị và chọn loại vật liệu!");
            return;
        }

        try {
            int catID = cbCategory.getValue().getCategoryID();
            String SPLID = cbSupplier.getValue().getSupplierID();
            Material newMaterial = new Material(
                    dao.generateNewID(),
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

            if (dao.insert(newMaterial)) {
                lblMessage.setText("Thêm thành công: " + newMaterial.getMaterialName());
                loadMaterialData();
                clearForm();
            } else {
                lblMessage.setText("Lỗi: Không thể thêm vật liệu!");
            }
        } catch (NumberFormatException e) {
            lblMessage.setText("Lỗi: Giá tiền hoặc số lượng phải là số!");
        } catch (Exception e) {
            lblMessage.setText("Lỗi: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateMaterial() {
        Material selectedMaterial = tblMaterial.getSelectionModel().getSelectedItem();
        if (selectedMaterial == null) {
            lblMessage.setText("Vui lòng chọn một vật liệu để sửa");
            return;
        }
        if (cbCategory.getValue() == null) {
            lblMessage.setText("Vui lòng chọn loại vật liệu!");
            return;
        }

        try {
            int catID = cbCategory.getValue().getCategoryID();
            String SPLID = cbSupplier.getValue().getSupplierID();
            Material updatedMaterial = new Material(
                    selectedMaterial.getMaterialID(), // Giữ ID cũ
                    txtMaterialName.getText(),
                    cbUnit.getValue(),
                    new BigDecimal(txtPurchasePrice.getText()),
                    new BigDecimal(txtSalePrice.getText()),
                    Integer.parseInt(txtStockQuantity.getText()),
                    txtDescription.getText(),
                    catID, // ID loại mới
                    "",
                    SPLID,
                    ""
            );

            if (dao.update(updatedMaterial)) {
                lblMessage.setText("Cập nhật thành công!");
                loadMaterialData();
                clearForm();
            } else {
                lblMessage.setText("Lỗi: Cập nhật thất bại!");
            }
        } catch (Exception e) {
            lblMessage.setText("Lỗi: Dữ liệu không hợp lệ! " + e.getMessage());
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
            if (dao.delete(selectedMaterial.getMaterialID())) {
                lblMessage.setText("Đã xóa thành công");
                materialList.remove(selectedMaterial);
                clearForm();
            } else {
                lblMessage.setText("Lỗi: Không thể xóa vật liệu!");
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

        // [LOGIC HIỂN THỊ] Chọn đúng loại trong ComboBox dựa vào ID
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
            lblMessage.setText("");
        }
    }

    private void clearForm() {
        txtMaterialID.setText(dao.generateNewID());
        txtMaterialName.clear();
        cbUnit.setValue(null);
        txtPurchasePrice.clear();
        txtSalePrice.clear();
        txtStockQuantity.clear();
        txtDescription.clear();
        cbCategory.setValue(null); // Xóa chọn ComboBox
        tblMaterial.getSelectionModel().clearSelection();
    }
}