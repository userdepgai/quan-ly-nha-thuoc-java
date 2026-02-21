package dto;

import java.time.LocalDateTime;

public class HoaDon_DTO {

    private String ma;
    private LocalDateTime ngayLap;
    private LocalDateTime ngayHoanThanh;
    private double thanhTien;
    private int trangThai;

    private String maNhanVien;

    public HoaDon_DTO() {
    }

    public HoaDon_DTO(String ma, LocalDateTime ngayLap, LocalDateTime ngayHoanThanh,
                      double thanhTien, int trangThai, String maNhanVien) {
        this.ma = ma;
        this.ngayLap = ngayLap;
        this.ngayHoanThanh = ngayHoanThanh;
        this.thanhTien = thanhTien;
        this.trangThai = trangThai;
        this.maNhanVien = maNhanVien;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }

    public LocalDateTime getNgayHoanThanh() {
        return ngayHoanThanh;
    }

    public void setNgayHoanThanh(LocalDateTime ngayHoanThanh) {
        this.ngayHoanThanh = ngayHoanThanh;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }
}
