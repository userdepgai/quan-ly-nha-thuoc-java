package dto;

import java.time.LocalDate;

public class TaiKhoan_DTO {
    private String SDT;
    private String matKhau;
    private PhanQuyen_DTO quyen;
    private LocalDate ngayKichHoat;
    private int trangThai;


    public TaiKhoan_DTO(String SDT, String matKhau, PhanQuyen_DTO quyen, LocalDate ngayKichHoat, int trangThai) {
        this.SDT = SDT;
        this.matKhau = matKhau;
        this.quyen = quyen;
        this.ngayKichHoat = ngayKichHoat;
        this.trangThai = trangThai;
    }

    public LocalDate getNgayKichHoat() {
        return ngayKichHoat;
    }

    public void setNgayKichHoat(LocalDate ngayKichHoat) {
        this.ngayKichHoat = ngayKichHoat;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public PhanQuyen_DTO getQuyen() {
        return quyen;
    }

    public void setQuyen(PhanQuyen_DTO quyen) {
        this.quyen = quyen;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}
