package dto;

import java.sql.Date;

public class ChuongTrinhKhuyenMai_DTO {
    private String maCTKM;
    private String tenCTKM;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private int trangThai;
    private String moTa;

    public ChuongTrinhKhuyenMai_DTO() {
    }

    public ChuongTrinhKhuyenMai_DTO(String maCTKM, String tenCTKM, Date ngayBatDau, Date ngayKetThuc, int trangThai, String moTa) {
        this.maCTKM = maCTKM;
        this.tenCTKM = tenCTKM;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
        this.moTa = moTa;
    }

    public String getMaCTKM() {
        return maCTKM;
    }

    public void setMaCTKM(String maCTKM) {
        this.maCTKM = maCTKM;
    }

    public String getTenCTKM() {
        return tenCTKM;
    }

    public void setTenCTKM(String tenCTKM) {
        this.tenCTKM = tenCTKM;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
}