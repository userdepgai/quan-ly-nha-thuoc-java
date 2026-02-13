package dto;

import java.time.LocalDate;
import java.util.ArrayList;

public class KhachHang_DTO extends NGUOI_DTO {

    private String maKH;
    private double diemThuong;
    private double diemHang;
    private String hang;
    private LocalDate ngayDKThanhVien;

    private ArrayList<KhachHang_DiaChi_DTO> dsDiaChi;

    public KhachHang_DTO() {
        super();
        dsDiaChi = new ArrayList<>();
    }

    public KhachHang_DTO(String maKH, String ten, boolean gioiTinh, LocalDate ngaySinh, String sdt,
                         double diemThuong, double diemHang,
                         String hang, LocalDate ngayDKThanhVien,
                         ArrayList<KhachHang_DiaChi_DTO> dsDiaChi) {

        super(ten, gioiTinh, ngaySinh, sdt);
        this.maKH = maKH;
        this.diemThuong = diemThuong;
        this.diemHang = diemHang;
        this.hang = hang;
        this.ngayDKThanhVien = ngayDKThanhVien;
        this.dsDiaChi = dsDiaChi;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

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

    public ArrayList<KhachHang_DiaChi_DTO> getDsDiaChi() {
        return dsDiaChi;
    }

    public void setDsDiaChi(ArrayList<KhachHang_DiaChi_DTO> dsDiaChi) {
        this.dsDiaChi = dsDiaChi;
    }
}
