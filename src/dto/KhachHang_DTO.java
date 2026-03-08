package dto;

import java.time.LocalDate;

public class KhachHang_DTO extends Nguoi_DTO {

    private int diemThuong;
    private int diemHang;
    private String hang;
    private LocalDate ngayDKThanhVien;

    public KhachHang_DTO(){
        super();
    }


    // Constructor đầy đủ
    public KhachHang_DTO(
            String ma,
            String ten,
            String sdt,
            LocalDate ngaySinh,
            boolean gioiTinh,
            int diemThuong,
            int diemHang,
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

    public int getDiemThuong() {
        return diemThuong;
    }

    public void setDiemThuong(int diemThuong) {
        this.diemThuong = diemThuong;
    }

    public int getDiemHang() {
        return diemHang;
    }

    public void setDiemHang(int diemHang) {
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