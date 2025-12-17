package dao;
import model.Supplier;
import java.util.ArrayList;
import java.util.List;
public class SupplierDAO {
    private final List<Supplier> mockDataList;
    public SupplierDAO(){
        mockDataList = new ArrayList<>();
        mockDataList.add(new Supplier("NCC001", "Mai Hoa", "Đăk Lăk", "0923346789", "maihoa@gmail.com"));
        mockDataList.add(new Supplier("NCC002", "Hoa Như", "Gia Lai", "0678325567", "hoanhu@gmail.com"));
        mockDataList.add(new Supplier("NCC003", "Kim Hương", "Đà Nẵng", "0235684901", "kimhuong@gmail.com"));
    }
    public List<Supplier> getAll(){
        return mockDataList;
    }

    public boolean insert(Supplier supplier){
        System.out.println("DAO: [Mock] Supplier added" + supplier.getSupplierName());
        mockDataList.add(supplier);
        return true;
    }
    public boolean update(Supplier supplier){
        System.out.println("DAO: [Mock] Supplier updated" + supplier.getSupplierID());
        return true;
    }
    public boolean delete(String supplierID){
        System.out.println("DAO: [Mock] Supplier deleted" +supplierID);
        return mockDataList.removeIf(s -> s.getSupplierID().equals(supplierID));
    }
    public String generateNewID(){
    int nextID = mockDataList.size()+1;
    return String.format("%03d", nextID);
    }
}
