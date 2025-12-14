package controller;

import dao.VatLieuDAO;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.VatLieu;
import view.VatLieuView; // Cần import lớp View mới
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class VatLieuController {

    private final VatLieuDAO vatLieuDAO = new VatLieuDAO();
    private final VatLieuView view = new VatLieuView(); // Khởi tạo View
    private final ObservableList<VatLieu> vatLieuList = FXCollections.observableArrayList();

    public Pane getVatLieuManagementView() {
        // Chỉ cần trả về Root Node do View tạo ra
        initialize();
        return view.createRootNode();
    }

    private void initialize() {
        // 1. Liên kết TableView với ObservableList
        view.tblVatLieu.setItems(vatLieuList);

        // 2. Gán các Sự kiện (đã di chuyển từ setupEventHandlers cũ)
        setupEventHandlers();

        // 3. Tải dữ liệu ban đầu
        loadVatLieuData();
    }

    // --- Logic Tải dữ liệu ---

    private void loadVatLieuData() {
        List<VatLieu> list = vatLieuDAO.getAll();
        vatLieuList.clear();
        vatLieuList.addAll(list);
    }

    // --- Xử lý Sự kiện (Event Handlers) ---

    private void setupEventHandlers() {
        // Gán sự kiện cho Buttons
        view.btnThem.setOnAction(e -> handleThemVatLieu());
        view.btnSua.setOnAction(e -> handleSuaVatLieu());
        view.btnXoa.setOnAction(e -> handleXoaVatLieu());
        view.btnRefresh.setOnAction(e -> loadVatLieuData());

        // Gán Listener cho TableView
        view.tblVatLieu.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        hienThiChiTiet(newSelection);
                    }
                });
    }

// --- Hàm Xử lý XÓA (DELETE) ---

// File: controller/VatLieuController.java (Cập nhật hàm handleXoaVatLieu)

    private void handleXoaVatLieu() {
        VatLieu selectedVl = view.tblVatLieu.getSelectionModel().getSelectedItem();

        if (selectedVl == null) {
            view.lblMessage.setText("Lỗi: Vui lòng chọn một Vật liệu để xóa.");
            return;
        }

        // 1. Hiển thị hộp thoại xác nhận (Phần này giữ nguyên)
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Xác Nhận Xóa");
        confirmDialog.setHeaderText("Bạn có chắc chắn muốn xóa vật liệu này?");
        confirmDialog.setContentText("Mã VL: " + selectedVl.getMaVL() + " - Tên: " + selectedVl.getTenVL());

        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // 1. Thực hiện xóa trong DAO (giả lập xóa trên DB)
            if (vatLieuDAO.delete(selectedVl.getMaVL())) {

                // 2. Xóa đối tượng đã chọn TRỰC TIẾP khỏi ObservableList
                // Đây là bước quan trọng nhất để JavaFX tự động cập nhật
                vatLieuList.remove(selectedVl);

                view.lblMessage.setText("Xóa vật liệu " + selectedVl.getTenVL() + " thành công!");
                xoaTrangForm();

                // KHÔNG CẦN gọi loadVatLieuData() hoặc refresh() nữa

            } else {
                view.lblMessage.setText("Lỗi: Xóa vật liệu thất bại. (Không tìm thấy đối tượng trong DAO)");
            }
        } else {
            view.lblMessage.setText("Hủy thao tác xóa.");
        }
    }
    // --- Hàm Xử lý SỬA (UPDATE) ---

    private void handleSuaVatLieu() {
        VatLieu selectedVl = view.tblVatLieu.getSelectionModel().getSelectedItem();

        if (selectedVl == null) {
            view.lblMessage.setText("Lỗi: Vui lòng chọn một Vật liệu để cập nhật.");
            return;
        }

        try {
            // Lấy MaVL từ form (đã disable)
            String maVL = view.txtMaVL.getText();

            // 1. Kiểm tra và chuyển đổi dữ liệu mới từ Form
            // Kiểm tra các trường không được trống và là số hợp lệ
            if (view.txtTenVL.getText().trim().isEmpty() || view.txtLoai.getText().trim().isEmpty()) {
                view.lblMessage.setText("Lỗi: Tên VL và Loại không được để trống.");
                return;
            }

            BigDecimal donViTinh = new BigDecimal(view.txtDonViTinh.getText());
            BigDecimal giaNhap = new BigDecimal(view.txtGiaNhap.getText());
            BigDecimal giaBan = new BigDecimal(view.txtGiaBan.getText());
            int soLuongTonKho = Integer.parseInt(view.txtTonKho.getText());

            // 2. Tạo đối tượng VatLieu mới với dữ liệu cập nhật
            VatLieu updatedVl = new VatLieu(
                    maVL, view.txtTenVL.getText(), view.txtLoai.getText(),
                    donViTinh, giaNhap, giaBan, soLuongTonKho
            );

            // 3. Thực hiện cập nhật qua DAO
            if (vatLieuDAO.update(updatedVl)) {
                view.lblMessage.setText("Cập nhật vật liệu " + updatedVl.getTenVL() + " thành công!");

                // 4. Tìm vị trí của đối tượng cũ trong ObservableList
                // vatLieuList là ObservableList liên kết với TableView
                int index = -1;
                for (int i = 0; i < vatLieuList.size(); i++) {
                    if (vatLieuList.get(i).getMaVL().equals(updatedVl.getMaVL())) {
                        index = i;
                        break;
                    }
                }

                // 5. Thay thế đối tượng cũ bằng đối tượng mới (Kích hoạt cập nhật giao diện)
                if (index != -1) {
                    vatLieuList.set(index, updatedVl); // <-- Dòng này giải quyết vấn đề
                }

                view.tblVatLieu.getSelectionModel().select(updatedVl);
            } else {
                view.lblMessage.setText("Cập nhật thất bại! (Không tìm thấy mã VL: " + maVL + ")");
            }

        } catch (NumberFormatException e) {
            view.lblMessage.setText("Lỗi định dạng: Đơn vị tính, Giá nhập, Giá bán, Tồn kho phải là số hợp lệ.");
        } catch (Exception e) {
            view.lblMessage.setText("Lỗi hệ thống khi cập nhật: " + e.getMessage());
        }
    }
    // --- Logic CRUD ---

    private void handleThemVatLieu() {
        try {
            // Logic kiểm tra và chuyển đổi dữ liệu (Giống code cũ)
            // ... (Kiểm tra trống, chuyển đổi kiểu dữ liệu) ...

            // Sử dụng các TextField từ lớp View đã được khởi tạo: view.txtTenVL
            VatLieu newVL = new VatLieu(
                    vatLieuDAO.generateNewId(), // Thêm hàm tạo ID giả lập trong DAO
                    view.txtTenVL.getText(), view.txtLoai.getText(),
                    new BigDecimal(view.txtDonViTinh.getText()), new BigDecimal(view.txtGiaNhap.getText()),
                    new BigDecimal(view.txtGiaBan.getText()), Integer.parseInt(view.txtTonKho.getText())
            );

            if (vatLieuDAO.insert(newVL)) {
                view.lblMessage.setText("Thêm vật liệu " + newVL.getTenVL() + " thành công!");
                loadVatLieuData();
                xoaTrangForm();
            } else {
                view.lblMessage.setText("Thêm thất bại!");
            }

        } catch (NumberFormatException e) {
            view.lblMessage.setText("Lỗi: Giá tiền/Tồn kho phải là số hợp lệ.");
        } catch (Exception e) {
            view.lblMessage.setText("Lỗi hệ thống khi thêm mới.");
        }
    }

    // File: controller/VatLieuController.java

// ... (Hàm handleThemVatLieu) ...

    // --- Các Hàm Hỗ trợ Giao diện (Di chuyển từ Controller) ---
    private void hienThiChiTiet(VatLieu vl) {
        view.txtMaVL.setText(vl.getMaVL());
        view.txtTenVL.setText(vl.getTenVL());
        view.txtLoai.setText(vl.getLoai());
        view.txtDonViTinh.setText(vl.getDonViTinh().toPlainString()); // Chuyển BigDecimal sang String
        view.txtGiaNhap.setText(vl.getGiaNhap().toPlainString());
        view.txtGiaBan.setText(vl.getGiaBan().toPlainString());
        view.txtTonKho.setText(String.valueOf(vl.getSoLuongTonKho()));
        view.lblMessage.setText("Đang chọn: " + vl.getTenVL());
    }

    private void xoaTrangForm() {
        view.txtMaVL.clear();
        view.txtTenVL.clear();
        view.txtLoai.clear();
        view.txtDonViTinh.clear();
        view.txtGiaNhap.clear();
        view.txtGiaBan.clear();
        view.txtTonKho.clear();
        view.lblMessage.setText("");
    }
}