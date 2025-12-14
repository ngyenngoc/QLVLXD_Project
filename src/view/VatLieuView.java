package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import model.VatLieu; // Cần thiết để định nghĩa kiểu cho TableView
import java.math.BigDecimal; // Cần thiết cho các cột giá

public class VatLieuView {

    // --- Khai báo Controls (Được di chuyển từ Controller) ---
    public final TableView<VatLieu> tblVatLieu = new TableView<>();
    public final Label lblMessage = new Label();

    // TextFields
    public final TextField txtMaVL = new TextField();
    public final TextField txtTenVL = new TextField();
    public final TextField txtLoai = new TextField();
    public final TextField txtDonViTinh = new TextField();
    public final TextField txtGiaNhap = new TextField();
    public final TextField txtGiaBan = new TextField();
    public final TextField txtTonKho = new TextField();

    // Buttons
    public final Button btnThem = new Button("➕ Thêm Mới");
    public final Button btnSua = new Button("✍️ Cập nhật");
    public final Button btnXoa = new Button("🗑️ Xóa");
    public final Button btnRefresh = new Button("🔄 Làm mới");


    // --- Phương thức Tạo View Chính ---

    public Pane createRootNode() {
        AnchorPane root = new AnchorPane();
        root.setPadding(new Insets(10));

        setupTableView();
        GridPane formLayout = createCrudForm();

        // Định vị và Thêm các thành phần
        AnchorPane.setTopAnchor(tblVatLieu, 20.0);
        AnchorPane.setLeftAnchor(tblVatLieu, 10.0);
        AnchorPane.setRightAnchor(tblVatLieu, 10.0);

        AnchorPane.setTopAnchor(formLayout, 450.0);
        AnchorPane.setLeftAnchor(formLayout, 10.0);

        root.getChildren().addAll(tblVatLieu, formLayout);

        return root;
    }

    // --- Hàm Setup Cấu trúc TableView ---

    private void setupTableView() {
        tblVatLieu.setPrefSize(1160, 400);

        // Cấu hình các cột (Giống code cũ của bạn)
        TableColumn<VatLieu, String> maVLCol = new TableColumn<>("Mã VL");
        maVLCol.setCellValueFactory(new PropertyValueFactory<>("MaVL"));

        // ... Thêm các cột còn lại: tenVLCol, loaiCol, dvtCol, giaNhapCol, giaBanCol, tonKhoCol ...
        TableColumn<VatLieu, String> tenVLCol = new TableColumn<>("Tên Vật Liệu");
        tenVLCol.setCellValueFactory(new PropertyValueFactory<>("TenVL"));

        TableColumn<VatLieu, String> loaiCol = new TableColumn<>("Loại");
        loaiCol.setCellValueFactory(new PropertyValueFactory<>("Loai"));

        TableColumn<VatLieu, BigDecimal> dvtCol = new TableColumn<>("ĐVT");
        dvtCol.setCellValueFactory(new PropertyValueFactory<>("DonViTinh"));

        TableColumn<VatLieu, BigDecimal> giaNhapCol = new TableColumn<>("Giá Nhập");
        giaNhapCol.setCellValueFactory(new PropertyValueFactory<>("GiaNhap"));

        TableColumn<VatLieu, BigDecimal> giaBanCol = new TableColumn<>("Giá Bán");
        giaBanCol.setCellValueFactory(new PropertyValueFactory<>("GiaBan"));

        TableColumn<VatLieu, Integer> tonKhoCol = new TableColumn<>("Tồn Kho");
        tonKhoCol.setCellValueFactory(new PropertyValueFactory<>("SoLuongTonKho"));

        tblVatLieu.getColumns().addAll(maVLCol, tenVLCol, loaiCol, dvtCol, giaNhapCol, giaBanCol, tonKhoCol);
        tblVatLieu.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Lưu ý: Listener sẽ được gán trong Controller, không gán ở đây.
    }

    // --- Hàm Tạo Layout Form CRUD ---

    private GridPane createCrudForm() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);

        // Bố cục Form
        grid.add(new Label("Mã VL:"), 0, 0); grid.add(txtMaVL, 1, 0);
        grid.add(new Label("Giá Nhập:"), 2, 0); grid.add(txtGiaNhap, 3, 0);
        // ... Thêm các Label và TextField còn lại ...
        grid.add(new Label("Tên VL:"), 0, 1); grid.add(txtTenVL, 1, 1);
        grid.add(new Label("Giá Bán:"), 2, 1); grid.add(txtGiaBan, 3, 1);

        grid.add(new Label("Loại:"), 0, 2); grid.add(txtLoai, 1, 2);
        grid.add(new Label("Tồn Kho:"), 2, 2); grid.add(txtTonKho, 3, 2);

        grid.add(new Label("Đơn Vị Tính:"), 0, 3); grid.add(txtDonViTinh, 1, 3);

        // Hộp Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(btnThem, btnSua, btnXoa, btnRefresh);
        grid.add(buttonBox, 0, 4, 4, 1);

        lblMessage.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        grid.add(lblMessage, 0, 5, 4, 1);

        // Khóa trường Mã Vật liệu (vì nó là khóa chính)
        txtMaVL.setDisable(true);

        return grid;
    }

    // Bạn không cần Getters vì các Controls đã được khai báo là public final
}