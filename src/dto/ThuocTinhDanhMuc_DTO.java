package dto;

public class ThuocTinhDanhMuc_DTO {
    // --- HẰNG SỐ TRẠNG THÁI (Đồng bộ với GUI và các DTO khác) ---
    public static final int TT_NGUNG_HOAT_DONG = 0;
    public static final int TT_HOAT_DONG = 1;
    public static final String NGUNG_HOAT_DONG = "Ngưng hoạt động";
    public static final String HOAT_DONG = "Đang hoạt động";

    private String maThuocTinh;
    private String tenThuocTinh;
    private int trangThai;
    private String maDM;

    public ThuocTinhDanhMuc_DTO() {
    }

    public ThuocTinhDanhMuc_DTO(String maThuocTinh, String tenThuocTinh,
                                int trangThai, String maDM) {
        this.maThuocTinh = maThuocTinh;
        this.tenThuocTinh = tenThuocTinh;
        this.trangThai = trangThai;
        this.maDM = maDM;
    }

    // --- GETTER & SETTER CƠ BẢN ---
    public String getMaThuocTinh() {
        return maThuocTinh;
    }

    public void setMaThuocTinh(String maThuocTinh) {
        this.maThuocTinh = maThuocTinh;
    }

    public String getTenThuocTinh() {
        return tenThuocTinh;
    }

    public void setTenThuocTinh(String tenThuocTinh) {
        this.tenThuocTinh = tenThuocTinh;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaDM() {
        return maDM;
    }

    public void setMaDM(String maDM) {
        this.maDM = maDM;
    }

    // =========================================================
    // LOGIC XỬ LÝ TRẠNG THÁI CHO GUI
    // =========================================================


    public String getTrangThaiText() {
        return (this.trangThai == TT_HOAT_DONG) ? HOAT_DONG : NGUNG_HOAT_DONG;
    }

    public void setTrangThaiFromText(String text) {
        if (HOAT_DONG.equals(text)) {
            this.trangThai = TT_HOAT_DONG;
        } else {
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