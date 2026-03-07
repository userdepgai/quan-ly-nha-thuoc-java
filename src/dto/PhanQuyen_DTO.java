package dto;

public class PhanQuyen_DTO {
    public static final int TT_NGUNG_HOAT_DONG = 0;
    public static final int TT_HOAT_DONG = 1;
    public static final String NGUNG_HOAT_DONG = "Ngưng hoạt động";
    public static final String HOAT_DONG = "Hoạt động";

    private String maQuyen;
    private String tenQuyen;
    private String moTa;
    private int trangThai;

    public PhanQuyen_DTO(String maQuyen, String tenQuyen, String moTa, int trangThai) {
        this.maQuyen = maQuyen;
        this.tenQuyen = tenQuyen;
        this.moTa = moTa;
        this.trangThai = trangThai;
    }

    public String getMaQuyen() {
        return maQuyen;
    }

    public void setMaQuyen(String maQuyen) {
        this.maQuyen = maQuyen;
    }

    public String getTenQuyen() {
        return tenQuyen;
    }

    public void setTenQuyen(String tenQuyen) {
        this.tenQuyen = tenQuyen;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getTrangThaiText() {
        return switch (trangThai) {
            case TT_HOAT_DONG -> HOAT_DONG;
            case TT_NGUNG_HOAT_DONG -> NGUNG_HOAT_DONG;
            default -> "Không xác định";
        };
    }
    public void setTrangThaiFromText(String text) {
        if (text == null) return;
        switch (text) {
            case HOAT_DONG ->
                    this.trangThai = TT_HOAT_DONG;
            case NGUNG_HOAT_DONG ->
                    this.trangThai = TT_NGUNG_HOAT_DONG;
        }
    }

    public static int parseTrangThaiFromText(String text) {
        if (text == null) return -1;
        return switch (text) {
            case HOAT_DONG -> TT_HOAT_DONG;
            case NGUNG_HOAT_DONG -> TT_NGUNG_HOAT_DONG;
            default -> -1;
        };
    }
}
