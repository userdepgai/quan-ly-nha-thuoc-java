package dto;

import java.time.LocalDate;

public class NhanVien_DTO extends NGUOI_DTO {

    private String maNV;
    private int chucVu;
    private LocalDate ngayVaoLam;
    private double luongCoBan;
    private int trangThai;
    private String maDC;

    public NhanVien_DTO() {}

    public NhanVien_DTO(String maNV, String ten, boolean gioiTinh, LocalDate ngaySinh, String sdt,
                        int chucVu, LocalDate ngayVaoLam,
                        double luongCoBan, int trangThai, String maDC) {

        super(ten, gioiTinh, ngaySinh, sdt);
        this.maNV = maNV;
        this.chucVu = chucVu;
        this.ngayVaoLam = ngayVaoLam;
        this.luongCoBan = luongCoBan;
        this.trangThai = trangThai;
        this.maDC = maDC;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public int getChucVu() {
        return chucVu;
    }

    public void setChucVu(int chucVu) {
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

    public String getMaDC() {
        return maDC;
    }

    public void setMaDC(String maDC) {
        this.maDC = maDC;
    }
}
