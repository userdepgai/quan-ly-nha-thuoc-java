package dto;

import java.time.LocalDate;

public class NhanVien_DTO {
    private String maNhanVien;
    private String tenNhanVien;
    private boolean gioiTinh;
    private LocalDate namSinh;
    private String sdt;
    private String chucVu;
    private LocalDate ngayVaoLam;
    private double luongCoBan;
    private int trangThai;

    private DIACHI_DTO diaChi;

    public NhanVien_DTO() {
    }

    public NhanVien_DTO(String maNhanVien, String tenNhanVien, boolean gioiTinh,
                        LocalDate namSinh, String sdt, String chucVu,
                        LocalDate ngayVaoLam, double luongCoBan,
                        int trangThai, DIACHI_DTO diaChi) {
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.gioiTinh = gioiTinh;
        this.namSinh = namSinh;
        this.sdt = sdt;
        this.chucVu = chucVu;
        this.ngayVaoLam = ngayVaoLam;
        this.luongCoBan = luongCoBan;
        this.trangThai = trangThai;
        this.diaChi = diaChi;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public LocalDate getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(LocalDate namSinh) {
        this.namSinh = namSinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public LocalDate getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(LocalDate ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public double getLuongCoBan() {
        return luongCoBan;
    }

    public void setLuongCoBan(double luongCoBan) {
        this.luongCoBan = luongCoBan;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public DIACHI_DTO getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(DIACHI_DTO diaChi) {
        this.diaChi = diaChi;
    }
}

