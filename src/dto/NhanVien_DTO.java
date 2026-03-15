package dto;

import java.time.LocalDate;

public class NhanVien_DTO extends Nguoi_DTO {

    public static final int TT_NGHI = 0;
    public static final int TT_DANG_LAM = 1;

    public static final String NGHI = "Nghỉ";
    public static final String DANG_LAM = "Đang làm";

    private String chucVu;
    private LocalDate ngayVaoLam;
    private double luongCoBan;
    private int trangThai;
    private String maDiaChi;
    private String maQuyen;

    public NhanVien_DTO() {
        super();
    }

    public NhanVien_DTO(String ma, String ten, String sdt,
                        LocalDate ngaySinh, boolean gioiTinh,
                        String chucVu, LocalDate ngayVaoLam,
                        double luongCoBan, int trangThai,
                        String maDiaChi, String maQuyen) {

        super(ma, ten, sdt, ngaySinh, gioiTinh);
        this.chucVu = chucVu;
        this.ngayVaoLam = ngayVaoLam;
        this.luongCoBan = luongCoBan;
        this.trangThai = trangThai;
        this.maDiaChi = maDiaChi;
        this.maQuyen = maQuyen;
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
    public String getMaQuyen() {
        return maQuyen;
    }

    public void setMaQuyen(String maQuyen) {
        this.maQuyen = maQuyen;
    }

    public String getTrangThaiText() {
        return switch (trangThai) {
            case TT_DANG_LAM -> DANG_LAM;
            case TT_NGHI -> NGHI;
            default -> "Không xác định";
        };
    }

    public void setTrangThaiFromText(String text) {
        if (text == null) return;
        switch (text) {
            case DANG_LAM -> this.trangThai = TT_DANG_LAM;
            case NGHI -> this.trangThai = TT_NGHI;
        }
    }

    public static int parseTrangThaiFromText(String text) {
        if (text == null) return -1;
        return switch (text) {
            case DANG_LAM -> TT_DANG_LAM;
            case NGHI -> TT_NGHI;
            default -> -1;
        };
    }
}