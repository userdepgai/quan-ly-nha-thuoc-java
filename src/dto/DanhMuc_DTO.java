package dto;

public class DanhMuc_DTO {
    // --- HẰNG SỐ CHO TRẠNG THÁI ---
    public static final int TT_NGUNG_HOAT_DONG = 0;
    public static final int TT_HOAT_DONG = 1;
    public static final String NGUNG_HOAT_DONG = "Ngưng hoạt động";
    public static final String HOAT_DONG = "Đang hoạt động";

    private String maDM;
    private String tenDM;
    private int trangThai;

    public DanhMuc_DTO() {}

    public DanhMuc_DTO(String maDM, String tenDM, int trangThai) {
        this.maDM = maDM;
        this.tenDM = tenDM;
        this.trangThai = trangThai;
    }

    public String getMaDM() { return maDM; }
    public void setMaDM(String maDM) { this.maDM = maDM; }

    public String getTenDM() { return tenDM; }
    public void setTenDM(String tenDM) { this.tenDM = tenDM; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    // --- LOGIC CHUYỂN ĐỔI TRẠNG THÁI ---
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
            case HOAT_DONG -> this.trangThai = TT_HOAT_DONG;
            case NGUNG_HOAT_DONG -> this.trangThai = TT_NGUNG_HOAT_DONG;
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