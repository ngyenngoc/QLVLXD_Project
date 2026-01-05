package controller;

import dao.MaterialDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Material;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class MaterialController {
    // Ánh xạ các linh kiện từ FXML
    @FXML
    private TextField txtMaterialID, txtMaterialName, txtPurchasePrice, txtSalePrice, txtStockQuantity, txtDescription, txtCategoryID;
    @FXML
    private ComboBox<String> cbUnit; // ComboBox đơn vị tính
    @FXML
    private TableView<Material> tblMaterial;
    @FXML
    private TableColumn<Material, String> colID, colName, colUnit, colDesc;
    @FXML
    private TableColumn<Material, BigDecimal> colPurchase, colSale;
    @FXML
    private TableColumn<Material, Integer> colStock, colCatID;
    @FXML
    private Label lblMessage;
    @FXML
    private Button btnAdd, btnUpdate, btnDelete, btnRefresh;

    private final MaterialDAO dao = new MaterialDAO();
    private final ObservableList<Material> materialList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Cấu hình ComboBox Unit
        ObservableList<String> units = FXCollections.observableArrayList(
                "Cái", "Bộ", "Kg", "Mét", "Thùng", "Bao", "Viên"
        );
        cbUnit.setItems(units);
        cbUnit.setPromptText("Chọn đơn vị");

        // 2. Ánh xạ cột bảng với model Material
        colID.setCellValueFactory(new PropertyValueFactory<>("materialID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        colPurchase.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        colSale.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCatID.setCellValueFactory(new PropertyValueFactory<>("categoryID"));

        tblMaterial.setItems(materialList);
        loadMaterialData();


        // Sự kiện click dòng trong bảng
        tblMaterial.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) displayDetails(newSelection);
                });
    }

    @FXML
    private void loadMaterialData() {
        List<Material> list = dao.getAll();
        materialList.clear();
        materialList.addAll(list);
    }

    @FXML
    private void handleAddMaterial() {
        txtMaterialID.setText(dao.generateNewID());
        try {
            Material newMaterial = new Material(
                    dao.generateNewID(),
                    txtMaterialName.getText(),
                    cbUnit.getValue(), // Lấy giá trị từ ComboBox
                    new BigDecimal(txtPurchasePrice.getText()),
                    new BigDecimal(txtSalePrice.getText()),
                    Integer.parseInt(txtStockQuantity.getText()),
                    txtDescription.getText(),
                    Integer.parseInt(txtCategoryID.getText())
            );

            if (dao.insert(newMaterial)) {
                lblMessage.setText("Thêm vật liệu " + newMaterial.getMaterialName() + " thành công!");
                materialList.add(newMaterial);
                clearForm();
            } else {
                lblMessage.setText("Lỗi: Không thể thêm vật liệu!");
            }
        } catch (Exception e) {
            lblMessage.setText("Lỗi: Kiểm tra lại định dạng số!");
        }
        if (txtMaterialName.getText().isEmpty() || cbUnit.getValue() == null) {
            lblMessage.setText("Lỗi: Vui lòng nhập tên và chọn đơn vị!");
            return;
        }
    }

    @FXML
    private void handleUpdateMaterial() {
        Material selectedMaterial = tblMaterial.getSelectionModel().getSelectedItem();
        if (selectedMaterial == null) {
            lblMessage.setText("Vui lòng chọn một vật liệu trong danh sách để sửa");
            return;
        }

        try {
            Material updatedMaterial = new Material(
                    txtMaterialID.getText(),
                    txtMaterialName.getText(),
                    cbUnit.getValue(), // Lấy giá trị từ ComboBox
                    new BigDecimal(txtPurchasePrice.getText()),
                    new BigDecimal(txtSalePrice.getText()),
                    Integer.parseInt(txtStockQuantity.getText()),
                    txtDescription.getText(),
                    Integer.parseInt(txtCategoryID.getText())
            );

            if (dao.update(updatedMaterial)) {
                lblMessage.setText("Cập nhật thông tin vật liệu thành công!");
                int index = materialList.indexOf(selectedMaterial);
                if (index != -1) {
                    materialList.set(index, updatedMaterial);
                }
            } else {
                lblMessage.setText("Lỗi: Cập nhật thất bại!");
            }
        } catch (Exception e) {
            lblMessage.setText("Lỗi: Dữ liệu không hợp lệ!");
        }
    }

    @FXML
    private void handleDeleteMaterial() {
        Material selectedMaterial = tblMaterial.getSelectionModel().getSelectedItem();
        if (selectedMaterial == null) {
            lblMessage.setText("Vui lòng chọn vật liệu cần xóa");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa vật liệu " + selectedMaterial.getMaterialName() + "?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (dao.delete(selectedMaterial.getMaterialID())) {
                lblMessage.setText("Đã xóa vật liệu " + selectedMaterial.getMaterialName() + " thành công");
                materialList.remove(selectedMaterial);
                clearForm();
            } else {
                lblMessage.setText("Lỗi: Không thể xóa vật liệu!");
            }
        }
    }

    private void displayDetails(Material material) {
        txtMaterialID.setText(material.getMaterialID());
        txtMaterialName.setText(material.getMaterialName());
        cbUnit.setValue(material.getUnit()); // Gán giá trị vào ComboBox
        txtPurchasePrice.setText(material.getPurchasePrice().toString());
        txtSalePrice.setText(material.getSalePrice().toString());
        txtStockQuantity.setText(String.valueOf(material.getStockQuantity()));
        txtDescription.setText(material.getDescription());
        txtCategoryID.setText(String.valueOf(material.getCategoryID()));
        lblMessage.setText("");
    }

    private void clearForm() {
        txtMaterialID.clear();
        txtMaterialName.clear();
        cbUnit.setValue(null); // Xóa chọn trong ComboBox
        txtPurchasePrice.clear();
        txtSalePrice.clear();
        txtStockQuantity.clear();
        txtDescription.clear();
        txtCategoryID.clear();
    }
}