package model;
import java.math.BigDecimal;
public class VatLieu {
    private String maVL;
    private String tenVL;
    private String loai;
    private BigDecimal donViTinh;
    private  BigDecimal giaNhap;
    private  BigDecimal giaBan;
    private int soLuongTonKho;
    public VatLieu(String maVL, String tenVL, String loai, BigDecimal donViTinh, BigDecimal giaNhap, BigDecimal giaBan, int soLuongTonKho){
        this.maVL = maVL;
        this.tenVL = tenVL;
        this.loai = loai;
        this.donViTinh = donViTinh;
        this.giaNhap = giaNhap;
        this.giaBan = giaBan;
        this.soLuongTonKho = soLuongTonKho;
    }
    public String getMaVL(){
        return maVL;
    }
    public String getTenVL(){
        return tenVL;
    }
    public String getLoai(){
        return loai;
    }
    public BigDecimal getDonViTinh(){
        return donViTinh;
    }
    public BigDecimal getGiaNhap(){
        return giaNhap;
    }
    public BigDecimal getGiaBan(){
        return giaBan;
    }
    public int getSoLuongTonKho(){
        return soLuongTonKho;
    }
    public void setMaVL(String maVL) {
        this.maVL = maVL;
    }
    public void setTenVL(String tenVL) {
        this.tenVL = tenVL;
    }
    public void setLoai(String loai) {
        this.loai = loai;
    }
    public void setDonViTinh(BigDecimal donViTinh) {
        this.donViTinh = donViTinh;
    }
    public void setGiaNhap(BigDecimal giaNhap) {
        this.giaNhap = giaNhap;
    }
    public void setGiaBan(BigDecimal giaBan) {
        this.giaBan = giaBan;
    }
    public void setSoLuongTonKho(int soLuongTonKho) {
        this.soLuongTonKho = soLuongTonKho;
    }

}
