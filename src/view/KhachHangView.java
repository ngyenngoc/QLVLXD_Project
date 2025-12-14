package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import model.KhachHang;
import model.NhaCungCap;


public class KhachHangView {
    public final TableView<KhachHang> tblKhachHang = new TableView<>();
    public final Label lblMessage = new Label();

    public final TextField txtMaKhachHang = new TextField();
    public final TextField txtTenKhachHang = new TextField();
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
        AnchorPane.setTopAnchor(tblKhachHang, 20.0);
        AnchorPane.setLeftAnchor(tblKhachHang, 10.0);
        AnchorPane.setRightAnchor(tblKhachHang, 10.0);
        AnchorPane.setTopAnchor(formLayout, 450.0);
        AnchorPane.setLeftAnchor(formLayout, 10.0);
        root.getChildren().addAll(tblKhachHang, formLayout);
        return root;
    }

    private void setupTableView() {
        tblKhachHang.setPrefSize(1160, 400);
        TableColumn<KhachHang, String> maKhachHangCol = new TableColumn<>("Mã Khách Hàng");
        maKhachHangCol.setCellValueFactory(new PropertyValueFactory<>("MaKhachHang"));
        TableColumn<KhachHang, String> tenKhachHangCol = new TableColumn<>("Tên Khách Hàng");
        tenKhachHangCol.setCellValueFactory(new PropertyValueFactory<>("TenKhachHang"));
        TableColumn<KhachHang, String> diaChiCol = new TableColumn<>("Địa chỉ");
        diaChiCol.setCellValueFactory(new PropertyValueFactory<>("DiaChi"));
        TableColumn<KhachHang, String> soDienThoaiCol = new TableColumn<>("Số Điện Thoại");
        soDienThoaiCol.setCellValueFactory(new PropertyValueFactory<>("SoDienThoai"));
        TableColumn<KhachHang, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("Email"));
        tblKhachHang.getColumns().addAll(maKhachHangCol, tenKhachHangCol, diaChiCol, soDienThoaiCol, emailCol);
        tblKhachHang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private GridPane createCrudForm() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        //Bố cục Form
        grid.add(new Label("Mã Khách Hàng:"), 0, 0);
        grid.add(txtMaKhachHang, 1, 0);
        grid.add(new Label("Tên Khách Hàng:"), 0, 1);
        grid.add(txtTenKhachHang, 1, 1);
        grid.add(new Label("Địa Chỉ:"), 0, 2);
        grid.add(txtDiaChi, 1, 2);
        grid.add(new Label("Số Điện Thoại:"), 2, 0);
        grid.add(txtSoDienThoai, 3, 0);
        grid.add(new Label("Email:"), 2, 1);
        grid.add(txtEmail, 3, 1);

        HBox ButtonBox = new HBox(10);
        ButtonBox.getChildren().addAll(btnThem, btnSua, btnXoa, btnRefresh);
        grid.add(ButtonBox, 0, 3, 4, 1);
        lblMessage.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        grid.add(lblMessage, 0, 4, 4, 1);
        txtMaKhachHang.setDisable(true);
        return grid;
    }

}
