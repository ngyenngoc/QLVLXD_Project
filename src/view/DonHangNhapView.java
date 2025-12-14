package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import model.DonHangNhap;

import java.time.LocalDate;

public class DonHangNhapView {
    public final TableView<DonHangNhap> tblDonHangNhap =new TableView<>();
    public final Label lblMessage = new Label();

    public final TextField txtMaDonHangNhap = new TextField();
    public final DatePicker dpNgayNhap =  new DatePicker(LocalDate.now());
    public final TextField txtTongTien = new TextField();
    public final TextArea txtGhiChu = new TextArea();
    public final Label lblNgayTao = new Label();
    public final Label lblNgayCapNhat = new Label();
    public final ComboBox<String> cmbTrangThai = new ComboBox<>();
    public final ComboBox<String> cmbMaNhaCungCap = new ComboBox<>();
    public final ComboBox<String> cmbNguoiTao = new ComboBox<>();

    public final Button btnThem = new Button("Thêm mới");
    public final Button btnSua = new Button("Cập nhật");
    public final Button btnXoa = new Button("Xóa");
    public final Button btnRefresh = new Button("Làm mới");

    public Pane createRootNode(){
        AnchorPane root = new AnchorPane();
        root.setPadding(new Insets(10));

        setupTableView();
        GridPane formLayout = createCrudForm();
        AnchorPane.setTopAnchor(tblDonHangNhap,20.0);
        AnchorPane.setLeftAnchor(tblDonHangNhap,10.0);
        AnchorPane.setRightAnchor(tblDonHangNhap,10.0);
        AnchorPane.setTopAnchor(formLayout, 450.0);
        AnchorPane.setLeftAnchor(formLayout, 10.0);
        root.getChildren().addAll(tblDonHangNhap, formLayout);
        return root;
    }
    private void setupTableView(){
        tblDonHangNhap.setPrefSize(1160,400);
        TableColumn<DonHangNhap, String> maDonHangNhapCol = new TableColumn<>("Mã Đơn Hàng Nhập");
        maDonHangNhapCol.setCellValueFactory( new PropertyValueFactory<>("maDonHangNhap"));
        TableColumn<DonHangNhap, String> NgayNhapCol = new TableColumn<>("Ngày Nhập");
        NgayNhapCol.setCellValueFactory( new PropertyValueFactory<>("ngayNhap"));
        TableColumn<DonHangNhap, String> TongTienCol = new TableColumn<>("Tổng Tiền");
        TongTienCol.setCellValueFactory( new PropertyValueFactory<>("tongTien"));
        TableColumn<DonHangNhap, String> GhiChuCol = new TableColumn<>("Ghi Chú");
        GhiChuCol.setCellValueFactory( new PropertyValueFactory<>("ghiChu"));
        TableColumn<DonHangNhap, String> TrangThaiCol = new TableColumn<>("Trạng Thái");
        TrangThaiCol.setCellValueFactory( new PropertyValueFactory<>("trangThai"));
        TableColumn<DonHangNhap, String> NgayTaoCol = new TableColumn<>("Ngày Tạo");
        NgayTaoCol.setCellValueFactory( new PropertyValueFactory<>("ngayTao"));
        TableColumn<DonHangNhap, String> NgayCapNhatCol = new TableColumn<>("Ngày Cập Nhật");
        NgayCapNhatCol.setCellValueFactory( new PropertyValueFactory<>("ngayCapNhat"));
        TableColumn<DonHangNhap, String> maNhaCungCapCol = new TableColumn<>("Mã Nhà Cung Cấp");
        maNhaCungCapCol.setCellValueFactory( new PropertyValueFactory<>("maNhaCungCap"));
        TableColumn<DonHangNhap, String> NguoiTaoCol = new TableColumn<>("Người tạo");
        NguoiTaoCol.setCellValueFactory( new PropertyValueFactory<>("nguoiTao"));
        tblDonHangNhap.getColumns().addAll(maDonHangNhapCol, NgayNhapCol, TongTienCol, GhiChuCol, TrangThaiCol, NgayTaoCol, NgayCapNhatCol, maNhaCungCapCol, NguoiTaoCol);
        tblDonHangNhap.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    private GridPane createCrudForm(){
       GridPane grid  = new  GridPane();
       grid.setHgap(15);
       grid.setVgap(10);
       //Bố cục Form
        grid.add(new Label("Mã Đơn Hàng Nhâp:"),0,0); grid.add(txtMaDonHangNhap,1,0);
        grid.add(new Label("Ngày Nhâp:"),0,1); grid.add(dpNgayNhap,1,1);
        grid.add(new Label("Tổng Tiền:"),0,2); grid.add(txtTongTien,1,2);
        grid.add(new Label("Ghi Chú:"),0,3); grid.add(txtGhiChu,1,3,3,1);  // kéo dài qua 3 cột
        grid.add(new Label("Ngày Tạo:"),0,4); grid.add(lblNgayTao,1,4);
        grid.add(new Label("Ngày Cập Nhật:"),2,4); grid.add(lblNgayCapNhat,3,4);
        grid.add(new Label("Trạng Thái:"),2,2); grid.add(cmbTrangThai,3,2);
        grid.add(new Label("Mã Nhà Cung Cấp :"),2,0); grid.add(cmbMaNhaCungCap,3,0);
        grid.add(new Label("Người Tạo:"),2,1); grid.add(cmbNguoiTao,3,1);

        HBox ButtonBox = new HBox(10);
        ButtonBox.getChildren().addAll(btnThem, btnSua, btnXoa, btnRefresh);
        grid.add(ButtonBox, 0, 5, 4, 1);
        lblMessage.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        grid.add(lblMessage, 0, 6, 4, 1);
        txtMaDonHangNhap.setDisable(true);
        return grid;
    }
}
