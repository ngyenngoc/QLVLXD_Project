package dao;

import model.DonHangNhap;
import model.KhachHang;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DonHangNhapDAO {
    private final List<DonHangNhap> mockDataList;

    public DonHangNhapDAO() {
        mockDataList = new ArrayList<>();
        mockDataList.add(new DonHangNhap("DHN001", LocalDate.of(2025,12,10), new BigDecimal("120000.0"), "Nhập gấp","Chờ duyệt", LocalDateTime.now().minusDays(5), LocalDateTime.now(), "NCC001", "ADMIN"));
        mockDataList.add(new DonHangNhap("DHN002", LocalDate.of(2025,12,12), new BigDecimal("10000.0"), "Hàng dễ vỡ","Đã nhập", LocalDateTime.now().minusDays(10), LocalDateTime.now() , "NCC002", "ADMIN"));
        mockDataList.add(new DonHangNhap("DHN003", LocalDate.of(2025,12,17), new BigDecimal("500000.0"), "Hàng sale","Đã duyệt", LocalDateTime.now().minusDays(11), LocalDateTime.now(), "3", "ADMIN"));
    }
    public List<DonHangNhap> getAll() {
        return mockDataList;
    }
    public boolean insert(DonHangNhap dhn){
    dhn.setNgayTao(LocalDateTime.now());
    dhn.setNgayCapNhat(LocalDateTime.now());
    dhn.setTrangThai("Đang chờ") ; // thiết lập trạng thái mặc định kho thêm mockDataList
        mockDataList.add(dhn);
    System.out.println("DAO: [Mock] Đã thêm Đơn Hàng Nhập " + dhn.getMaDonHangNhap());
    return true;
    }
    public boolean update(DonHangNhap updatedDhn) { //  Duyệt qua danh sách để tìm đối tượng cũ
        for (int i = 0; i < mockDataList.size(); i++) {

            //  So sánh dựa trên Khóa Chính (MaDonHangNhap)
            if (mockDataList.get(i).getMaDonHangNhap().equals(updatedDhn.getMaDonHangNhap())) {
                // Thay thế đối tượng cũ bằng đối tượng mới
                mockDataList.set(i, updatedDhn);
                System.out.println("DAO: [Mock] Đã cập nhật Đơn Hàng Nhập: " + updatedDhn.getMaDonHangNhap());
                return true; // Cập nhật thành công
            }
        }
        return false;
    }

    public boolean delete(String maDonHangNhap) {
        System.out.println("DAO: [Mock] Đã xóa Đơn Hàng Nhập" + maDonHangNhap);
        return mockDataList.removeIf(kh->kh.getMaDonHangNhap().equals(maDonHangNhap));// Xóa thực thể khỏi mockDataList để demo hoạt động
    }

    public String generateNewId(){
        int nextID = mockDataList.size()+1;
        return String.format("DHN%03d", nextID);
    }
}


