package dto;

import java.time.LocalDate;
import java.util.List;

public class KhachHang_DTO {
    private String maKhachHang;
    private String tenKhachHang;
    private boolean gioiTinh;
    private LocalDate ngaySinh;
    private String sdt;
    private double diemThuong;
    private double diemHang;
    private String hang;
    private LocalDate ngayDangKy;
    private int trangThai;

    private List<DIACHI_DTO> danhSachDiaChi;

    public KhachHang_DTO() {
    }

    public KhachHang_DTO(String maKhachHang, String tenKhachHang, boolean gioiTinh,
                         LocalDate ngaySinh, String sdt,
                         double diemThuong, double diemHang,
                         String hang, LocalDate ngayDangKy,
                         int trangThai, List<DIACHI_DTO> danhSachDiaChi) {
        this.maKhachHang = maKhachHang;
        this.tenKhachHang = tenKhachHang;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.sdt = sdt;
        this.diemThuong = diemThuong;
        this.diemHang = diemHang;
        this.hang = hang;
        this.ngayDangKy = ngayDangKy;
        this.trangThai = trangThai;
        this.danhSachDiaChi = danhSachDiaChi;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
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

    public LocalDate getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(LocalDate ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public List<DIACHI_DTO> getDanhSachDiaChi() {
        return danhSachDiaChi;
    }

    public void setDanhSachDiaChi(List<DIACHI_DTO> danhSachDiaChi) {
        this.danhSachDiaChi = danhSachDiaChi;
    }
}
