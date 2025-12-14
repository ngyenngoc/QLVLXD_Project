package model;
import java.util.Objects;
public class KhachHang {
    private String maKhachHang;
    private String tenKhachHang;
    private String diaChi;
    private String soDienThoai;
    private String email;
    public KhachHang (String maKhachHang, String tenKhachHang, String diaChi, String soDienThoai, String email) {
        this.maKhachHang=maKhachHang;
        this.tenKhachHang=tenKhachHang;
        this.diaChi=diaChi;
        this.soDienThoai=soDienThoai;
        this.email=email;
    }
    public String getMaKhachHang(){
        return maKhachHang;
    }
    public String getTenKhachHang() {
        return tenKhachHang;
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
    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }
    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
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
