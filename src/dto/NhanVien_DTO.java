package dto;

import java.time.LocalDate;

public class NhanVien_DTO extends Nguoi_DTO {

    private String chucVu;
    private LocalDate ngayVaoLam;
    private double luongCoBan;
    private int trangThai;
    private String maDiaChi;

    public NhanVien_DTO() {
        super();
    }

    public NhanVien_DTO(String ma, String ten, String sdt,
                        LocalDate ngaySinh, boolean gioiTinh,
                        String chucVu, LocalDate ngayVaoLam,
                        double luongCoBan, int trangThai,
                        String maDiaChi) {

        super(ma, ten, sdt, ngaySinh, gioiTinh);
        this.chucVu = chucVu;
        this.ngayVaoLam = ngayVaoLam;
        this.luongCoBan = luongCoBan;
        this.trangThai = trangThai;
        this.maDiaChi = maDiaChi;
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

    public String getMaDiaChi() {
        return maDiaChi;
    }

    public void setMaDiaChi(String maDiaChi) {
        this.maDiaChi = maDiaChi;
    }
}
