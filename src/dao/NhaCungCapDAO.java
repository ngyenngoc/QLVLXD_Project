package dao;
import model.NhaCungCap;
import java.util.ArrayList;
import java.util.List;
public class NhaCungCapDAO {
    private final List<NhaCungCap> mockDataList;
    public NhaCungCapDAO(){
        mockDataList = new ArrayList<>();
        mockDataList.add(new NhaCungCap("NCC01", "Mai Hoa", "Đăk Lăk", "0923346789", "maihoa@gmail.com"));
        mockDataList.add(new NhaCungCap("NCC002", "Hoa Như", "Gia Lai", "0678325567", "hoanhu@gmail.com"));
        mockDataList.add(new NhaCungCap("NCC003", "Kim Hương", "Đà Nẵng", "0235684901", "kimhuong@gmail.com"));
    }
    public List<NhaCungCap> getAll(){
        return mockDataList;
    }
    public boolean insert(NhaCungCap ncc){
        System.out.println("DAO: [Mock] Đã thêm nhà cung cấp" + ncc.getTenNhaCungCap());
        mockDataList.add(ncc);
        return true;
    }
    public boolean update(NhaCungCap ncc){
        System.out.println("DAO: [Mock] Đã cập nhật nhà cung cấp" + ncc.getMaNhaCungCap());
        return true;
    }
    public boolean delete(String maNhaCungCap){
        System.out.println("DAO: [Mock] Đã xóa Nhà cung cấp" +maNhaCungCap);
    return true;
    }
    public String generateNewID(){
    int nextID = mockDataList.size()+1;
    return String.format("NCC%03d", nextID);
    }
}
