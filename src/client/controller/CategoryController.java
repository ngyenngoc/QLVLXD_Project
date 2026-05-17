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

import java.util.List;
import java.util.Optional;

public class CategoryController {
    @FXML private TextField txtCategoryID, txtCategoryName;
    @FXML private TextArea txtDescription;
    @FXML private TableView<Category> tblCategory;
    @FXML private TableColumn<Category, Integer> colID;
    @FXML private TableColumn<Category, String> colName, colDesc;
    @FXML private Label lblMessage;
    @FXML private Button btnAdd, btnUpdate, btnDelete, btnRefresh;

    private final ObservableList<Category> categoryList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Ánh xạ cột bảng với thuộc tính của shared.model Category
        colID.setCellValueFactory(new PropertyValueFactory<>("categoryID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));

        tblCategory.setItems(categoryList);
        loadCategoryData();

        // ĐÃ SỬA: Kích hoạt gọi hàm gán sự kiện cho các nút bấm hoạt động
        setupEventHandlers();

        // Gán sự kiện khi click vào dòng trong bảng
        tblCategory.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) displayDetails(newSelection);
                });
    }

    @FXML
    private void loadCategoryData(){
        // Gọi mạng lấy toàn bộ danh mục loại vật liệu
        Response response = SocketClient.sendRequest(new Request("GET_ALL_CATEGORIES", null));
        if (response.isSuccess()) {
            List<Category> list = (List<Category>) response.getData();
            categoryList.clear();
            categoryList.addAll(list);
        } else {
            lblMessage.setText("Lỗi mạng: " + response.getMessage());
        }
    }

    private void setupEventHandlers() {
        btnAdd.setOnAction(e -> handleAddCategory());
        btnUpdate.setOnAction(e -> handleUpdateCategory());
        btnDelete.setOnAction(e -> handleDeleteCategory());
        btnRefresh.setOnAction(e -> loadCategoryData());
    }

    @FXML
    private void handleAddCategory() {
        if (txtCategoryID.getText().isEmpty() || txtCategoryName.getText().isEmpty()) {
            lblMessage.setText("Lỗi: Vui lòng nhập Mã loại và Tên loại!");
            return;
        }
        try {
            int id = Integer.parseInt(txtCategoryID.getText());
            Category newCategory = new Category(id, txtCategoryName.getText(), txtDescription.getText());

            // Gửi lệnh thêm mới qua Socket
            Response response = SocketClient.sendRequest(new Request("ADD_CATEGORY", newCategory));
            if (response.isSuccess()) {
                lblMessage.setText("Thêm loại vật liệu " + newCategory.getCategoryName() + " thành công!");
                loadCategoryData();
                clearForm();
            } else {
                lblMessage.setText("Lỗi: " + response.getMessage());
            }
        } catch (NumberFormatException e) {
            lblMessage.setText("Lỗi: Mã loại phải là số nguyên!");
        }
    }

    @FXML
    private void handleUpdateCategory() {
        Category selectedCategory = tblCategory.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            lblMessage.setText("Vui lòng chọn một loại vật liệu trong danh sách để sửa");
            return;
        }
        if (txtCategoryName.getText().isEmpty()) {
            lblMessage.setText("Lỗi: Tên không được để trống!");
            return;
        }

        Category updatedCategory = new Category(
                selectedCategory.getCategoryID(), txtCategoryName.getText(), txtDescription.getText());

        // Gửi lệnh cập nhật lên Server
        Response response = SocketClient.sendRequest(new Request("UPDATE_CATEGORY", updatedCategory));
        if (response.isSuccess()) {
            lblMessage.setText("Cập nhật thông tin loại vật liệu thành công!");
            loadCategoryData();
            clearForm();
        } else {
            lblMessage.setText("Lỗi: " + response.getMessage());
        }
    }

    @FXML
    private void handleDeleteCategory(){
        Category selectedCategory = tblCategory.getSelectionModel().getSelectedItem();
        if(selectedCategory == null){
            lblMessage.setText("Vui lòng chọn loại vật liệu cần xóa");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa: " + selectedCategory.getCategoryName() + "?");
        confirm.setHeaderText("Xác nhận xóa");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Gửi mã int ID lên mạng để xóa
            Response response = SocketClient.sendRequest(new Request("DELETE_CATEGORY", selectedCategory.getCategoryID()));
            if (response.isSuccess()) {
                lblMessage.setText("Đã xóa thành công!");
                loadCategoryData();
                clearForm();
            } else {
                lblMessage.setText("Lỗi: " + response.getMessage());
            }
        }
    }

    private void displayDetails(Category category){
        txtCategoryID.setText(String.valueOf(category.getCategoryID()));
        txtCategoryName.setText(category.getCategoryName());
        txtDescription.setText(category.getDescription());
        lblMessage.setText("");
    }

    private void clearForm(){
        txtCategoryID.clear();
        txtCategoryName.clear();
        txtDescription.clear();
        tblCategory.getSelectionModel().clearSelection();
    }
}