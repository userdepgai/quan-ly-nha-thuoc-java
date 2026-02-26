package dto;

import java.time.LocalDate;

public class Nguoi_DTO {

    protected String ma;
    protected String ten;
    protected String sdt;
    protected LocalDate ngaySinh;
    protected boolean gioiTinh;

    public Nguoi_DTO() {
    }

    public Nguoi_DTO(String ma, String ten, String sdt,
                     LocalDate ngaySinh, boolean gioiTinh) {
        this.ma = ma;
        this.ten = ten;
        this.sdt = sdt;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }
}
