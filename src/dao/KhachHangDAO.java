package dao;
import model.KhachHang;
import model.NhaCungCap;
import java.util.ArrayList;
import java.util.List;
public class KhachHangDAO {
    private final List<KhachHang> mockDataList;

    public KhachHangDAO() {
        mockDataList = new ArrayList<>();
        mockDataList.add(new KhachHang("KH001", "Mai Liên", "Đăk Nông", "0867946662", "mailien@gmail.com"));
        mockDataList.add(new KhachHang("KH002", "Nhi Xuân", "Bắc Ninh", "0924678749", "nhixuann@gmail.com"));
        mockDataList.add(new KhachHang("KH003", "Kim Hoan", "Quảng Nam", "0679412678", "kimhmhoann@gmail.com"));
    }

    public List<KhachHang> getAll() {
        return mockDataList;
    }

    public boolean insert(KhachHang kh) {
        System.out.println("DAO: [Mock] Đã thêm Khách Hàng" + kh.getTenKhachHang());
        mockDataList.add(kh);
        return true;
    }

    public boolean update(KhachHang kh) {
        System.out.println("DAO: [Mock] Đã cập nhật Khách Hàng" + kh.getMaKhachHang());
        return true;
    }

    public boolean delete(String maKhachHang) {
        System.out.println("DAO: [Mock] Đã xóa Khách Hàng" + maKhachHang);
        return mockDataList.removeIf(kh->kh.getMaKhachHang().equals(maKhachHang));// Xóa thực thể khỏi mockDataList để demo hoạt động
    }
    public String generateNewId(){
        int nextID = mockDataList.size()+1;
        return String.format("KH%03d", nextID);
    }
}
