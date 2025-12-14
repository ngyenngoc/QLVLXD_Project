package model;
import java.util.Objects;
public class NhaCungCap {
    private String maNhaCungCap;
    private String tenNhaCungCap;
    private String diaChi;
    private String soDienThoai;
    private String email;
public NhaCungCap (String maNhaCungCap, String tenNhaCungCap, String diaChi, String soDienThoai, String email) {
    this.maNhaCungCap=maNhaCungCap;
    this.tenNhaCungCap=tenNhaCungCap;
    this.diaChi=diaChi;
    this.soDienThoai=soDienThoai;
    this.email=email;
    }
public String getMaNhaCungCap(){
    return maNhaCungCap;
    }
    public String getTenNhaCungCap() {
        return tenNhaCungCap;
    }
    public String getDiaChi() {
        return diaChi;
    }
    public String getSoDienThoai() {
        return soDienThoai;
    }
    public String getEmail() {
        return email;
    }
    public void setMaNhaCungCap(String maNhaCungCap) {
        this.maNhaCungCap = maNhaCungCap;
    }
    public void setTenNhaCungCap(String tenNhaCungCap) {
        this.tenNhaCungCap = tenNhaCungCap;
    }
    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}

