package dto;

import java.time.LocalDate;

public class KhachHang_DTO extends Nguoi_DTO {

    private double diemThuong;
    private double diemHang;
    private String hang;
    private LocalDate ngayDKThanhVien;

    // Constructor rỗng
    public KhachHang_DTO() {
        super();
    }

    // Constructor đầy đủ
    public KhachHang_DTO(
            String ma,
            String ten,
            String sdt,
            LocalDate ngaySinh,
            boolean gioiTinh,
            double diemThuong,
            double diemHang,
            String hang,
            LocalDate ngayDKThanhVien
    ) {
        super(ma, ten, sdt, ngaySinh, gioiTinh);
        this.diemThuong = diemThuong;
        this.diemHang = diemHang;
        this.hang = hang;
        this.ngayDKThanhVien = ngayDKThanhVien;
    }

    // ===== Getter & Setter =====
    public double getDiemThuong() {
        return diemThuong;
    }

    public void setDiemThuong(double diemThuong) {
        this.diemThuong = diemThuong;
    }

    public double getDiemHang() {
        return diemHang;
    }

    public void setDiemHang(double diemHang) {
        this.diemHang = diemHang;
    }

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    public LocalDate getNgayDKThanhVien() {
        return ngayDKThanhVien;
    }

    public void setNgayDKThanhVien(LocalDate ngayDKThanhVien) {
        this.ngayDKThanhVien = ngayDKThanhVien;
    }
}
