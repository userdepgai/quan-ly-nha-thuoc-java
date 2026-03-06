package dto;

import java.time.LocalDate;

public class TaiKhoan_DTO {

    public static final int TT_KHOA = 0;
    public static final int TT_MO = 1;
    public static final String KHOA = "Khóa";
    public static final String MO = "Mở";

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
    public String getTrangThaiText() {
        return switch (trangThai) {
            case TT_MO -> MO;
            case TT_KHOA -> KHOA;
            default -> "Không xác định";
        };
    }
    public void setTrangThaiFromText(String text) {
        if (text == null) return;
        switch (text) {
            case MO -> this.trangThai = TT_MO;
            case KHOA -> this.trangThai = TT_KHOA;
        }
    }
    public static int parseTrangThaiFromText(String text) {
        if (text == null) return -1;
        return switch (text) {
            case MO -> TT_MO;
            case KHOA -> TT_KHOA;
            default -> -1;
        };
    }
}
