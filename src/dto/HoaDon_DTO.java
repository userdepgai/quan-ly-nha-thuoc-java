package dto;

import java.time.LocalDateTime;

public class HoaDon_DTO {

    private String ma;
    private LocalDateTime ngayLap;
    private LocalDateTime ngayHoanThanh;
    private double thanhTien;
    private int trangThai;
    private NhanVien_DTO nhanVien;

    public HoaDon_DTO() {}

    public HoaDon_DTO(String ma, LocalDateTime ngayLap, LocalDateTime ngayHoanThanh,
                      double thanhTien, int trangThai, NhanVien_DTO nhanVien) {
        this.ma = ma;
        this.ngayLap = ngayLap;
        this.ngayHoanThanh = ngayHoanThanh;
        this.thanhTien = thanhTien;
        this.trangThai = trangThai;
        this.nhanVien = nhanVien;
    }

    public String getMa() { return ma; }
    public void setMa(String ma) { this.ma = ma; }

    public LocalDateTime getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDateTime ngayLap) { this.ngayLap = ngayLap; }

    public LocalDateTime getNgayHoanThanh() { return ngayHoanThanh; }
    public void setNgayHoanThanh(LocalDateTime ngayHoanThanh) {
        this.ngayHoanThanh = ngayHoanThanh;
    }

    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    public NhanVien_DTO getNhanVien() { return nhanVien; }
    public void setNhanVien(NhanVien_DTO nhanVien) {
        this.nhanVien = nhanVien;
    }
}
