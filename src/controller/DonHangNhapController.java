package controller;

import dao.DonHangNhapDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import model.DonHangNhap;
import view.DonHangNhapView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class DonHangNhapController {
    private final DonHangNhapDAO dao = new DonHangNhapDAO();
    private final DonHangNhapView view = new DonHangNhapView();
    private final ObservableList<DonHangNhap> donhangnhapList = FXCollections.observableArrayList();
    // Format thời gian cho việc hiển thị (sử dụng cho Label)
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");

    // Phương thức chính để lấy View
    public Pane getDHNManagementView() {
        initialize();
        return view.createRootNode();
    }

    public void initialize() {
        view.tblDonHangNhap.setItems(donhangnhapList);
        loadComboBoxData();
        setupEventHandlers();
        loadDonHangNhapData();
    }

    private void loadDonHangNhapData() {
        List<DonHangNhap> list = dao.getAll();
        donhangnhapList.clear();
        donhangnhapList.addAll(list);
    }

    //Tải dữ liệu khóa ngoại vào ComboBox (Giả lập)
    private void loadComboBoxData() {
        view.cmbMaNhaCungCap.setItems(FXCollections.observableArrayList("NCC001", "NCC002", "NCC003"));
        view.cmbNguoiTao.setItems(FXCollections.observableArrayList("ADMIN", "USER1", "GUEST"));
        view.cmbTrangThai.setItems(FXCollections.observableArrayList("Đã nhập", "Đã hủy", "Chờ duyệt", "Đã duyệt", "Đang chờ"));
        //Thiết lập giá trị mặc định
        if (!view.cmbMaNhaCungCap.getItems().isEmpty()) view.cmbMaNhaCungCap.getSelectionModel().selectFirst();
        if (!view.cmbNguoiTao.getItems().isEmpty()) view.cmbNguoiTao.getSelectionModel().selectFirst();
        view.cmbTrangThai.getSelectionModel().select("Đang chờ");
    }

    private void setupEventHandlers() {
        view.btnThem.setOnAction(e -> handleThem());
        view.btnSua.setOnAction(e -> handleSua());
        view.btnXoa.setOnAction(e -> handleXoa());
        view.btnRefresh.setOnAction(e -> loadDonHangNhapData());
        // Gán Listener cho TableView
        view.tblDonHangNhap.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        hienThiChiTiet(newSelection);
                    }
                });
    }

    private DonHangNhap getDonHangNhapFromView() throws NumberFormatException, IllegalStateException {
        if (view.dpNgayNhap.getValue() == null) {
            throw new IllegalStateException("Ngày nhập không được để trống.");
        }
        if (view.txtTongTien.getText().trim().isEmpty() || view.cmbMaNhaCungCap.getValue() == null || view.cmbNguoiTao.getValue() == null) {
            throw new IllegalStateException("Vui lòng điền đầy đủ Tổng Tiền và Khóa Ngoại.");
        }
        String maDonHangNhap = view.txtMaDonHangNhap.getText().trim().isEmpty() ? dao.generateNewId() : view.txtMaDonHangNhap.getText();
        return new DonHangNhap(maDonHangNhap, view.dpNgayNhap.getValue(), new BigDecimal(view.txtTongTien.getText()), view.txtGhiChu.getText(), view.cmbTrangThai.getValue(), null, null, view.cmbMaNhaCungCap.getValue(), view.cmbNguoiTao.getValue());
    }

    private void handleThem() {
        try {
            DonHangNhap DHN = getDonHangNhapFromView();
            if (dao.insert(DHN)) {
                view.lblMessage.setText("Tạo đơn nhập thành công.");
                donhangnhapList.add(DHN);
                xoaTrangForm();
            } else {
                view.lblMessage.setText("Tạo đơn thất bại.");
            }
        } catch (NumberFormatException e) {
            view.lblMessage.setText("Lỗi: " + e.getMessage());
        } catch (IllegalStateException e) {
            view.lblMessage.setText("Lỗi: Không thể thêm đơn hàng." + e.getMessage());
        }
    }

    private void handleSua() {
        DonHangNhap selectedDHN = view.tblDonHangNhap.getSelectionModel().getSelectedItem();
        if (selectedDHN == null) {
            view.lblMessage.setText("Vui lòng chọn đơn hàng để cập nhật.");
            return;
        }
        try {
            DonHangNhap updatedDHN = getDonHangNhapFromView();
            // Bảo toàn NgayTao gốc và NgayCapNhat mới
            updatedDHN.setNgayTao(selectedDHN.getNgayTao());
            if (dao.update(updatedDHN)) {
                view.lblMessage.setText("Cập nhật đơn hàng thành công.");
                int index = donhangnhapList.indexOf(selectedDHN);
                if (index != -1) {
                    donhangnhapList.set(index, updatedDHN);
                }
            } else {
                view.lblMessage.setText("Cập nhật thất bại.");
            }
        } catch (NumberFormatException e) {
            view.lblMessage.setText("Lỗi: Tổng tiền phải là số hợp lệ.");
        } catch (IllegalStateException e) {
            view.lblMessage.setText("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            view.lblMessage.setText("Lỗi: Không thể cập nhật đơn hàng. " + e.getMessage());
        }
    }

                private void handleXoa() {
                    DonHangNhap selectedDHN = getDonHangNhapFromView();
                    if (selectedDHN == null) {
                        view.lblMessage.setText("Vui lòng chọn đơn hàng để xóa.");
                        return;
                    }
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,"Xác nhận xóa đơn hàng nhập");
                    Optional<ButtonType> result =confirm.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        if (dao.delete(selectedDHN.getMaDonHangNhap())) {
                            view.lblMessage.setText("Xóa Đơn Hàng " + selectedDHN.getMaDonHangNhap() + " thành công.");
                            donhangnhapList.remove(selectedDHN);
                            xoaTrangForm();
                        } else {
                            view.lblMessage.setText("Xóa thất bại.");
                        }
                    }
                }
                private void hienThiChiTiet(DonHangNhap dhn){
                view.txtMaDonHangNhap.setText(dhn.getMaDonHangNhap());
                view.dpNgayNhap.setValue(dhn.getNgayNhap());
                view.txtTongTien.setText(dhn.getTongTien().toString());
                view.txtGhiChu.setText(dhn.getGhiChu());
                view.cmbMaNhaCungCap.setValue(dhn.getMaNhaCungCap());
                view.cmbNguoiTao.setValue(dhn.getNguoiTao());
                view.cmbTrangThai.setValue(dhn.getTrangThai());

                //Hiện thị ngày giờ tạo/cập nhật (dùng Label)
            view.lblNgayTao.setText(dhn.getNgayTao().format(DonHangNhapController.DATE_TIME_FORMATTER));
            view.lblNgayCapNhat.setText(dhn.getNgayCapNhat().format(DonHangNhapController.DATE_TIME_FORMATTER));
            view.lblMessage.setText("");
            }
            private void xoaTrangForm(){
                view.txtMaDonHangNhap.clear();
                view.dpNgayNhap.setValue(LocalDate.now());
                view.txtTongTien.clear();
                view.txtGhiChu.clear();
                view.lblNgayTao.setText("");
                view.lblNgayCapNhat.setText("");
                view.lblMessage.setText("");

                // Reset ComboBoxes về mặc định
                view.cmbMaNhaCungCap.getSelectionModel().selectFirst();
                view.cmbNguoiTao.getSelectionModel().selectFirst();
                view.cmbTrangThai.getSelectionModel().select("Đang chờ");
            }
            }

