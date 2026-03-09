package dto;

public class GiaTriThuocTinh_SP_DTO {
    public static final int TT_NGUNG_SU_DUNG = 0;
    public static final int TT_DANG_SU_DUNG = 1;
    public static final String NGUNG_SU_DUNG = "Ngưng sử dụng";
    public static final String DANG_SU_DUNG = "Đang sử dụng";

    private String maSP;
    private String maThuocTinh;
    private String maGiaTri;
    private int trangThai;

    public GiaTriThuocTinh_SP_DTO() {
    }

    public GiaTriThuocTinh_SP_DTO(String maSP, String maThuocTinh, String maGiaTri, int trangThai) {
        this.maSP = maSP;
        this.maThuocTinh = maThuocTinh;
        this.maGiaTri = maGiaTri;
        this.trangThai = trangThai;
    }

    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    public String getMaThuocTinh() { return maThuocTinh; }
    public void setMaThuocTinh(String maThuocTinh) { this.maThuocTinh = maThuocTinh; }

    public String getMaGiaTri() { return maGiaTri; }
    public void setMaGiaTri(String maGiaTri) { this.maGiaTri = maGiaTri; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    public String getTrangThaiText() {
        return (this.trangThai == TT_DANG_SU_DUNG) ? DANG_SU_DUNG : NGUNG_SU_DUNG;
    }

    public void setTrangThaiFromText(String text) {
        if (DANG_SU_DUNG.equals(text)) {
            this.trangThai = TT_DANG_SU_DUNG;
        } else {
            this.trangThai = TT_NGUNG_SU_DUNG;
        }
    }

    public static int parseTrangThaiFromText(String text) {
        if (DANG_SU_DUNG.equals(text)) return TT_DANG_SU_DUNG;
        return TT_NGUNG_SU_DUNG;
    }
}