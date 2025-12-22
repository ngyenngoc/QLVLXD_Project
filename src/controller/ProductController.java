package controller;


import dao.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import model.Product;
import view.ProductView;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ProductController {
    private final ProductDAO dao= new ProductDAO();
    private final ProductView view= new ProductView();
    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    // Phương thức chính để lấy View
    public Pane getProductManagementView(){
        initialize();
        return view.createRootNode();
    }
    public void initialize(){
        view.cmbStatus.getItems().addAll("Sẵn có", "Chờ duyệt", "Đã duyệt", "Đang chờ", "Ngừng sản xuất");
        view.cmbStatus.setPromptText("Chọn trạng thái");
        view.tblProduct.setItems(productList);
        setupEventHandlers();
        loadProductData();
    }
    private void loadProductData(){
        List<Product> list = dao.getAll();
        productList.clear();
        productList.addAll(list);
    }
    private void setupEventHandlers() {
        view.btnAdd.setOnAction(e -> handleAddProduct());
        view.btnUpdate.setOnAction(e -> handleUpdateProduct());
        view.btnDelete.setOnAction(e -> handleDeleteProduct());
        view.btnRefresh.setOnAction(e -> {
                    loadProductData();
                    clearForm();
                    view.lblMessage.setText("Dữ liệu đã được làm mới!");
                });

    // Gán Listener cho TableView
        view.tblProduct.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        displayDetails(newSelection);
                    }
                });
    }
    //Xử lý CRUD
    private void handleAddProduct(){
        try{
            String id = dao.generateNewId();
            BigDecimal purchase = new BigDecimal(view.txtPurchasePrice.getText().trim());
            BigDecimal selling = new BigDecimal(view.txtSellingPrice.getText().trim());
            int stock = Integer.parseInt(view.txtstockQuantity.getText().trim());
            LocalDateTime now = LocalDateTime.now();
            Product newProduct = new Product(
                    id,
                    view.txtSupplierID.getText().trim(),
                    view.txtProductName.getText().trim(),
                    view.txtProductCategory.getText().trim(),
                    view.txtDescription.getText().trim(),
                    view.txtUnit.getText().trim(),
                    purchase,
                    selling,
                    stock,
                    view.cmbStatus.getValue(),
                    now, // lastStockUpdate
                    now, // createdAt
                    now, // updatedAt
                    view.txtAdminID.getText().trim()
            );
            if (dao.insert(newProduct)){
                view.lblMessage.setText("Thêm sản phẩm thành công!");
                productList.add(newProduct);
                clearForm();
            }
        } catch (NumberFormatException ex) {
            view.lblMessage.setText("Lỗi: Giá tiền hoặc Số lượng không hợp lệ!");
        } catch (Exception ex) {
            view.lblMessage.setText("Lỗi: " + ex.getMessage());
        }
    }
        private void handleUpdateProduct(){
        Product selectedProduct = view.tblProduct.getSelectionModel().getSelectedItem();
        if ( selectedProduct == null){
            view.lblMessage.setText("Vui lòng chọn một sản phẩm để chỉnh sửa");
            return;
        }
        try {
            Product updatedProduct = new Product(
                    view.txtProductID.getText(), view.txtSupplierID.getText(), view.txtProductName.getText(), view.txtProductCategory.getText(), view.txtDescription.getText(), view.txtUnit.getText(), new BigDecimal(view.txtPurchasePrice.getText()), new BigDecimal(view.txtSellingPrice.getText()), Integer.parseInt(view.txtstockQuantity.getText()),
                    view.cmbStatus.getValue(), selectedProduct.getLastStockUpdate(), selectedProduct.getCreatedAt(), LocalDateTime.now(), view.txtAdminID.getText()
            );
            if (dao.update(updatedProduct)) {
                view.lblMessage.setText("Cập nhật sản phẩm thành công!");
                loadProductData();
            }
        } catch(Exception ex){
            view.lblMessage.setText("Cập nhật thất bại: Vui lòng kiểm tra lại dữ liệu nhập!");
        }
    }
    private void handleDeleteProduct(){
            Product selectedProduct = view.tblProduct.getSelectionModel().getSelectedItem();
            if (selectedProduct == null) {
                view.lblMessage.setText("Vui lòng chọn một sản phẩm để xóa");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Bạn có chắc chắn muốn xóa sản phẩm: " + selectedProduct.getProductName() + "?");
            Optional<ButtonType> result = confirm.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (dao.delete(selectedProduct.getProductID())) {
                    view.lblMessage.setText("Đã xóa sản phẩm thành công");
                    productList.remove(selectedProduct);
                    clearForm();
                } else {
                    view.lblMessage.setText("Xóa thất bại!");
                }
            }
        }
        private void displayDetails(Product product){
        view.txtProductID.setText(product.getProductID());
        view.txtSupplierID.setText(product.getSupplierID());
        view.txtProductName.setText(product.getProductName());
        view.txtProductCategory.setText(product.getProductCategory());
        view.txtDescription.setText(product.getDescription());
        view.txtUnit.setText(product.getUnit());
// Chuyển BigDecimal sang String để hiển thị
            view.txtPurchasePrice.setText(product.getPurchasePrice().toPlainString());
            view.txtSellingPrice.setText(product.getSellingPrice().toPlainString());

            view.txtstockQuantity.setText(String.valueOf(product.getStockQuantity()));
            view.cmbStatus.setValue(product.getStatus());

            // Hiển thị ngày tháng lên DatePicker
            view.dpLastStockUpdate.setValue(product.getLastStockUpdate().toLocalDate());
            view.dpCreatedAt.setValue(product.getCreatedAt().toLocalDate());
            view.dpUpdatedAt.setValue(product.getUpdatedAt().toLocalDate());

            view.txtAdminID.setText(product.getAdminID());
            view.lblMessage.setText("");
            // Gán giá trị trạng thái từ đối tượng Product vào ComboBox
            view.cmbStatus.setValue(product.getStatus());
        }

    // Làm sạch Form nhập liệu
    private void clearForm() {
        view.txtProductID.clear();
        view.txtSupplierID.clear();
        view.txtProductName.clear();
        view.txtProductCategory.clear();
        view.txtDescription.clear();
        view.txtUnit.clear();
        view.txtPurchasePrice.clear();
        view.txtSellingPrice.clear();
        view.txtstockQuantity.clear();
        view.cmbStatus.setValue(null);
        view.txtAdminID.clear();

    }
}
