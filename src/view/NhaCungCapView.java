package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import model.NhaCungCap;


import java.util.concurrent.Callable;

public class NhaCungCapView {
    public final TableView<NhaCungCap> tblNhaCungCap = new TableView<>();
    public final Label lblMessage = new Label();

    public final TextField txtMaNhaCungCap = new TextField();
    public final TextField txtTenNhaCungCap = new TextField();
    public final TextField txtDiaChi = new TextField();
    public final TextField txtSoDienThoai = new TextField();
    public final TextField txtEmail = new TextField();

    public final Button btnThem = new Button("Thêm mới");
    public final Button btnSua = new Button("Cập nhật");
    public final Button btnXoa = new Button("Xóa");
    public final Button btnRefresh = new Button("Làm mới");

    public Pane createRootNode() {
        AnchorPane root = new AnchorPane();
        root.setPadding(new Insets(10));

        setupTableView();
        GridPane formLayout = createCrudForm();
        AnchorPane.setTopAnchor(tblNhaCungCap, 20.0);
        AnchorPane.setLeftAnchor(tblNhaCungCap, 10.0);
        AnchorPane.setRightAnchor(tblNhaCungCap, 10.0);
        AnchorPane.setTopAnchor(formLayout, 450.0);
        AnchorPane.setLeftAnchor(formLayout, 10.0);
        root.getChildren().addAll(tblNhaCungCap, formLayout);
        return root;
    }

    private void setupTableView() {
        tblNhaCungCap.setPrefSize(1160, 400);
        TableColumn<NhaCungCap, String> maNhaCungCapCol = new TableColumn<>("Mã Nhà Cung Cấp");
        maNhaCungCapCol.setCellValueFactory(new PropertyValueFactory<>("MaNhaCungCap"));
        TableColumn<NhaCungCap, String> tenNhaCungCapCol = new TableColumn<>("Tên Nhà Cung Cấp");
        tenNhaCungCapCol.setCellValueFactory(new PropertyValueFactory<>("TenNhaCungCap"));
        TableColumn<NhaCungCap, String> diaChiCol = new TableColumn<>("Địa chỉ");
        diaChiCol.setCellValueFactory(new PropertyValueFactory<>("DiaChi"));
        TableColumn<NhaCungCap, String> soDienThoaiCol = new TableColumn<>("Số Điện Thoại");
        soDienThoaiCol.setCellValueFactory(new PropertyValueFactory<>("SoDienThoai"));
        TableColumn<NhaCungCap, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("Email"));
        tblNhaCungCap.getColumns().addAll(maNhaCungCapCol, tenNhaCungCapCol,diaChiCol,soDienThoaiCol, emailCol);
        tblNhaCungCap.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    private GridPane createCrudForm() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        //Bố cục Form
        grid.add(new Label("Mã Nhà Cung Cấp:"), 0, 0); grid.add(txtMaNhaCungCap, 1, 0);
        grid.add(new Label("Tên Nhà Cung Cấp:"), 0, 1); grid.add(txtTenNhaCungCap, 1 ,1);
        grid.add(new Label("Địa Chỉ:"), 0, 2); grid.add(txtDiaChi, 1 ,2);
        grid.add(new Label("Số Điện Thoại:"), 2, 0); grid.add(txtSoDienThoai, 3 ,0);
        grid.add(new Label("Email:"), 2, 1); grid.add(txtEmail, 3 ,1);

        HBox ButtonBox = new HBox(10);
        ButtonBox.getChildren().addAll(btnThem, btnSua, btnXoa, btnRefresh);
        grid.add(ButtonBox, 0, 3, 4, 1);
        lblMessage.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        grid.add(lblMessage, 0, 4, 4, 1);
        txtMaNhaCungCap.setDisable(true);
        return grid;
    }
}