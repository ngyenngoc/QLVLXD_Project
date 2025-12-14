package model;
import java.util.Objects;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
public class DonHangNhap {
    private String maDonHangNhap;
    private LocalDate ngayNhap;
    private BigDecimal tongTien;
    private String ghiChu;   // optional(Nhập gấp, Hàng dễ vỡ, Giảm giá đặc biệt)
    private String trangThai;  //optional(Đã nhập, Đã hủy, Chờ duyệt, Đã duyệt, Đang chờ)
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
    private String maNhaCungCap;
    private String nguoiTao;
    public DonHangNhap(String maDonHangNhap, LocalDate ngayNhap, BigDecimal tongTien, String ghiChu, String trangThai, LocalDateTime ngayTao, LocalDateTime ngayCapNhat,String maNhaCungCap, String nguoiTao){
        this.maDonHangNhap = maDonHangNhap;
        this.ngayNhap = ngayNhap;
        this.tongTien = tongTien;
        this.ghiChu = ghiChu;
        this.trangThai = trangThai;
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
        this.maNhaCungCap = maNhaCungCap;
        this.nguoiTao = nguoiTao;
    }
    public String getMaDonHangNhap() {
        return maDonHangNhap;
    }
    public LocalDate getNgayNhap() {
        return ngayNhap;
    }
    public BigDecimal getTongTien() {
        return tongTien;
    }
    public String getGhiChu() {
        return ghiChu;
    }
    public String getTrangThai() {
        return trangThai;
    }
    public LocalDateTime getNgayTao() {
        return ngayTao;
    }
    public LocalDateTime getNgayCapNhat() {
        return ngayCapNhat;
    }
    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }
    public String getNguoiTao() {
        return nguoiTao;
    }
    public void setMaDoHangNhap(String maDonHangNhap) {
        this.maDonHangNhap = maDonHangNhap;
    }
    public void setNgayNhap(LocalDate ngayNhap) {
        this.ngayNhap = ngayNhap;
    }
    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }
    public void setNgayCapNhat(LocalDateTime ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }
    public void setMaNhaCungCap(String maNhaCungCap) {
        this.maNhaCungCap = maNhaCungCap;
    }
    public void setNguoiTao(String nguoiTao) {
        this.nguoiTao = nguoiTao;
    }
}
