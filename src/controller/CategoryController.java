package controller;

import dao.CategoryDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Category;


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

    private final CategoryDAO dao = new CategoryDAO();
    private final ObservableList<Category> categoryList = FXCollections.observableArrayList();
    // Phương thức chính để lấy View
    @FXML
    public void initialize() {
        // Ánh xạ cột bảng với thuộc tính của model Supplier
        colID.setCellValueFactory(new PropertyValueFactory<>("categoryID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));


        tblCategory.setItems(categoryList);
        loadCategoryData();

        setupEventHandlers();
        // Gán sự kiện khi click vào dòng trong bảng
        tblCategory.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) displayDetails(newSelection);
                });
    }
    @FXML
    private void loadCategoryData(){
        List<Category> list = dao.getAll();
        categoryList.clear();
        categoryList.addAll(list);
    }
    private void setupEventHandlers() {
        btnAdd.setOnAction(e -> handleAddCategory());
        btnUpdate.setOnAction(e -> handleUpdateCategory());
        btnDelete.setOnAction(e -> handleDeleteCategory());
        btnRefresh.setOnAction(e -> loadCategoryData());
    }
    //Xử lý CRUD
    @FXML
    private void handleAddCategory() {
        Category newCategory = new Category(0, txtCategoryName.getText(), txtDescription.getText());
        if (dao.insert(newCategory)) {
            lblMessage.setText(" Thêm loại vật liệu " + newCategory.getCategoryName() + " thành công!");
            loadCategoryData();
            clearForm();
        } else {
            lblMessage.setText("Lỗi: Không thể thêm loại vật liệu!");
        }
    }
    @FXML
    private void handleUpdateCategory() {
        Category selectedCategory = tblCategory.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            lblMessage.setText("Vui lòng chọn một loại vật liệu trong danh sách để sửa");
            return;
        }
        Category updatedCategory = new Category(
                Integer.parseInt(txtCategoryID.getText()), txtCategoryName.getText(), txtDescription.getText());
        if (dao.update(updatedCategory)) {
            lblMessage.setText("Cập nhật thông tin loại vật liệu thành công!");
            int index = categoryList.indexOf(selectedCategory);
            if (index != -1) {
                categoryList.set(index, updatedCategory);
            }
        } else {
            lblMessage.setText("Lỗi: Cập nhật thất bại!");
        }
    }
        @FXML
        private void handleDeleteCategory(){
            Category selectedCategory = tblCategory.getSelectionModel().getSelectedItem();
            if(selectedCategory == null){
                lblMessage.setText("Vui lòng chọn loại vật liệu cần xóa");
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa loại vật liệu " +selectedCategory.getCategoryName() + "?");
            Optional<ButtonType> result = confirm.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                if (dao.delete(selectedCategory.getCategoryID())){
                    lblMessage.setText("Đã xóa loại vật liệu " + selectedCategory.getCategoryName() + " thành công");
                    categoryList.remove(selectedCategory);
                    clearForm();
                }
                else{
                    lblMessage.setText("Đã xóa loại vật liệu!");
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

    }
}
