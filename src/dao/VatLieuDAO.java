package dao;

import model.VatLieu;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Lớp DAO này sẽ tạm thời sử dụng Mock Data (dữ liệu giả)
// Sau này, các hàm này sẽ được thay thế bằng JDBC để tương tác với SQL
public class VatLieuDAO {

    private final List<VatLieu> mockDataList;

    public VatLieuDAO() {
        // Khởi tạo danh sách vật liệu mẫu (Mock Data)
        mockDataList = new ArrayList<>();

        // Thêm các đối tượng VatLieu. Đảm bảo 7 tham số được truyền đúng thứ tự và kiểu dữ liệu:
        // (MaVL, TenVL, Loai, DonViTinh, GiaNhap, GiaBan, SoLuongTonKho)

        // 1. Xi Măng
        mockDataList.add(new VatLieu("VL001", "Xi Măng HT", "Vật liệu thô",
                new BigDecimal("1.0"), // Giả sử 1 bao = 1 đơn vị
                new BigDecimal("80000.00"),
                new BigDecimal("95000.00"), 500));

        // 2. Gạch Ống
        mockDataList.add(new VatLieu("VL002", "Gạch 4 Lỗ", "Gạch",
                new BigDecimal("1.0"), // Giả sử 1 viên = 1 đơn vị
                new BigDecimal("1200.00"),
                new BigDecimal("1500.00"), 12000));

        // 3. Cát Vàng
        mockDataList.add(new VatLieu("VL003", "Cát Vàng", "Cát",
                new BigDecimal("1.0"), // Giả sử 1 m3 = 1 đơn vị
                new BigDecimal("450000.00"),
                new BigDecimal("550000.00"), 150));
    }

    /**
     * Tạm thời lấy danh sách vật liệu bằng Mock Data
     */
    public List<VatLieu> getAll() {
        return mockDataList;
    }

    /**
     * Hàm CREATE (Thêm vật liệu mới) - Tạm thời chỉ thêm vào list giả
     */
    public boolean insert(VatLieu vl) {
        System.out.println("DAO: [MOCK] Đã thêm vật liệu " + vl.getTenVL());
        // Thêm vào list giả để thấy sự thay đổi khi thử nghiệm CRUD
        mockDataList.add(vl);
        return true;
    }

    /**
     * Hàm UPDATE (Cập nhật vật liệu) - Tạm thời chỉ log và giả định thành công
     */
    public boolean update(VatLieu vl) {
        // Trong thực tế cần tìm và thay thế đối tượng cũ trong CSDL
        System.out.println("DAO: [MOCK] Đã cập nhật vật liệu " + vl.getMaVL());
        return true;
    }

    /**
     * Hàm DELETE (Xóa vật liệu) - Tạm thời chỉ log và giả định thành công
     */
    public boolean delete(String maVL) {
        // Trong thực tế cần gọi lệnh DELETE SQL
        System.out.println("DAO: [MOCK] Đã xóa vật liệu có Mã " + maVL);
        return true;
    }

    public String generateNewId() {
        int nextId = mockDataList.size() + 1;
        return String.format("VL%03d", nextId);
    }
}