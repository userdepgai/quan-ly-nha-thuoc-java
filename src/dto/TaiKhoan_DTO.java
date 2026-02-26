package dto;

import java.time.LocalDate;

public class TaiKhoan_DTO {

    private String maTK;
    private String sdt;
    private String matKhau;
    private String maQuyen;
    private LocalDate ngayKichHoat;
    private int trangThai;

    public TaiKhoan_DTO() {
    }

    public TaiKhoan_DTO(String maTK, String sdt, String matKhau, String maQuyen, LocalDate ngayKichHoat, int trangThai) {
        this.maTK = maTK;
        this.sdt = sdt;
        this.matKhau = matKhau;
        this.maQuyen = maQuyen;
        this.ngayKichHoat = ngayKichHoat;
        this.trangThai = trangThai;
    }

    public String getMaTK() {
        return maTK;
    }

    public void setMaTK(String maTK) {
        this.maTK = maTK;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getMaQuyen() {
        return maQuyen;
    }

    public void setMaQuyen(String maQuyen) {
        this.maQuyen = maQuyen;
    }

    public LocalDate getNgayKichHoat() {
        return ngayKichHoat;
    }

    public void setNgayKichHoat(LocalDate ngayKichHoat) {
        this.ngayKichHoat = ngayKichHoat;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}
