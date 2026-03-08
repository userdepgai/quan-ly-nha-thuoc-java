package dto;

public class SanPham_DTO {
    // --- HẰNG SỐ CHO TRẠNG THÁI ---
    public static final int TT_NGUNG_BAN = 0;
    public static final int TT_DANG_BAN = 1;
    public static final String NGUNG_BAN = "Ngừng bán";
    public static final String DANG_BAN = "Đang bán";

    // --- HẰNG SỐ CHO KÊ ĐƠN ---
    public static final int KD_KHONG = 0;
    public static final int KD_CO = 1;
    public static final String KHONG_KE_DON = "Không";
    public static final String CO_KE_DON = "Có";

    private String maSP;
    private String tenSP;
    private String donViTinh;
    private double loiNhuan;
    private String hinhAnh;
    private int keDon;
    private int trangThai;

    private String maDM;    // Khóa ngoại tham chiếu đến DanhMuc
    private String maQC;    // Khóa ngoại tham chiếu đến QuyCach

    public SanPham_DTO() {
    }

    public SanPham_DTO(String maSP, String tenSP, String donViTinh,
                       double loiNhuan, String hinhAnh, int keDon,
                       int trangThai, String maDM, String maQC) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.donViTinh = donViTinh;
        this.loiNhuan = loiNhuan;
        this.hinhAnh = hinhAnh;
        this.keDon = keDon;
        this.trangThai = trangThai;
        this.maDM = maDM;
        this.maQC = maQC;
    }

    // --- GETTER & SETTER CƠ BẢN ---
    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public String getDonViTinh() { return donViTinh; }
    public void setDonViTinh(String donViTinh) { this.donViTinh = donViTinh; }

    public double getLoiNhuan() { return loiNhuan; }
    public void setLoiNhuan(double loiNhuan) { this.loiNhuan = loiNhuan; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public int getKeDon() { return keDon; }
    public void setKeDon(int keDon) { this.keDon = keDon; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    public String getMaDM() { return maDM; }
    public void setMaDM(String maDM) { this.maDM = maDM; }

    public String getMaQC() { return maQC; }
    public void setMaQC(String maQC) { this.maQC = maQC; }


    public String getTrangThaiText() {
        return switch (trangThai) {
            case TT_DANG_BAN -> DANG_BAN;
            case TT_NGUNG_BAN -> NGUNG_BAN;
            default -> "Không xác định";
        };
    }

    public void setTrangThaiFromText(String text) {
        if (text == null) return;
        switch (text) {
            case DANG_BAN -> this.trangThai = TT_DANG_BAN;
            case NGUNG_BAN -> this.trangThai = TT_NGUNG_BAN;
        }
    }

    public static int parseTrangThaiFromText(String text) {
        if (text == null) return -1;
        return switch (text) {
            case DANG_BAN -> TT_DANG_BAN;
            case NGUNG_BAN -> TT_NGUNG_BAN;
            default -> -1;
        };
    }

    // =========================================================

    public String getKeDonText() {
        return switch (keDon) {
            case KD_CO -> CO_KE_DON;
            case KD_KHONG -> KHONG_KE_DON;
            default -> "Không xác định";
        };
    }

    public void setKeDonFromText(String text) {
        if (text == null) return;
        switch (text) {
            case CO_KE_DON -> this.keDon = KD_CO;
            case KHONG_KE_DON -> this.keDon = KD_KHONG;
        }
    }

    public static int parseKeDonFromText(String text) {
        if (text == null) return -1;
        return switch (text) {
            case CO_KE_DON -> KD_CO;
            case KHONG_KE_DON -> KD_KHONG;
            default -> -1;
        };
    }

    @Override
    public String toString() {
        return this.tenSP;
    }
}