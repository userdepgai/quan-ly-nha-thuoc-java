package dto;

import java.time.LocalDate;

public class LoHang_DTO {

    public static final int TT_CHO = 0;
    public static final int TT_HOAN_THANH = 1;
    public static final int TT_HUY = 2;

    public static final String CHO = "Chờ";
    public static final String HOAN_THANH = "Hoàn thành";
    public static final String HUY = "Hủy";

    public static final int TK_BINH_THUONG = 0;
    public static final int TK_SAP_HET_HAN = 1;
    public static final int TK_HET_HAN = 2;
    public static final int TK_HET_HANG = 3;

    public static final String BINH_THUONG = "Bình thường";
    public static final String SAP_HET_HAN = "Sắp hết hạn";
    public static final String HET_HAN = "Hết hạn";
    public static final String HET_HANG = "Hết hàng";

    private String maLo;
    private double giaNhap;
    private LocalDate hsd;

    private int soLuongNhap;
    private int soLuongConLai;
    private int soLuongSPCL;

    private double thanhTien;

    private int trangThai;
    private int trangThaiTonKho;

    private String maPnk;
    private String maNcc;
    private String maKvlt;
    private String maSp;

    public LoHang_DTO() {}

    public LoHang_DTO(String maLo, double giaNhap, LocalDate hsd,
                      int soLuongNhap, int soLuongConLai, int soLuongSPCL,
                      double thanhTien,
                      int trangThai, int trangThaiTonKho,
                      String maPnk, String maNcc,
                      String maKvlt, String maSp) {

        this.maLo = maLo;
        this.giaNhap = giaNhap;
        this.hsd = hsd;
        this.soLuongNhap = soLuongNhap;
        this.soLuongConLai = soLuongConLai;
        this.soLuongSPCL = soLuongSPCL;
        this.thanhTien = thanhTien;
        this.trangThai = trangThai;
        this.trangThaiTonKho = trangThaiTonKho;
        this.maPnk = maPnk;
        this.maNcc = maNcc;
        this.maKvlt = maKvlt;
        this.maSp = maSp;
    }

    public int getSoLuongSPCL() {
        return soLuongSPCL;
    }

    public void setSoLuongSPCL(int soLuongSPCL) {
        this.soLuongSPCL = soLuongSPCL;
    }

    public String getMaLo() { return maLo; }
    public void setMaLo(String maLo) { this.maLo = maLo; }

    public double getGiaNhap() { return giaNhap; }
    public void setGiaNhap(double giaNhap) { this.giaNhap = giaNhap; }

    public LocalDate getHsd() { return hsd; }
    public void setHsd(LocalDate hsd) { this.hsd = hsd; }

    public int getSoLuongNhap() { return soLuongNhap; }
    public void setSoLuongNhap(int soLuongNhap) { this.soLuongNhap = soLuongNhap; }

    public int getSoLuongConLai() { return soLuongConLai; }
    public void setSoLuongConLai(int soLuongConLai) { this.soLuongConLai = soLuongConLai; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    public int getTrangThaiTonKho() { return trangThaiTonKho; }
    public void setTrangThaiTonKho(int trangThaiTonKho) { this.trangThaiTonKho = trangThaiTonKho; }

    public String getMaPnk() { return maPnk; }
    public void setMaPnk(String maPnk) { this.maPnk = maPnk; }

    public String getMaNcc() { return maNcc; }
    public void setMaNcc(String maNcc) { this.maNcc = maNcc; }

    public String getMaKvlt() { return maKvlt; }
    public void setMaKvlt(String maKvlt) { this.maKvlt = maKvlt; }

    public String getMaSp() { return maSp; }
    public void setMaSp(String maSp) { this.maSp = maSp; }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }


    public void congSoLuongSPCL(int num){
        this.soLuongSPCL += num;
    }
    
    public double tinhThanhTien() {
        return giaNhap * soLuongNhap;
    }

    public String getTrangThaiText() {
        return switch (trangThai) {
            case TT_CHO -> CHO;
            case TT_HOAN_THANH -> HOAN_THANH;
            case TT_HUY -> HUY;
            default -> "Không xác định";
        };
    }
    public String getTrangThaiTonKhoText() {
        return switch (trangThaiTonKho) {
            case TK_BINH_THUONG -> BINH_THUONG;
            case TK_SAP_HET_HAN -> SAP_HET_HAN;
            case TK_HET_HAN -> HET_HAN;
            case TK_HET_HANG -> HET_HANG;
            default -> "Không xác định";
        };
    }
    public void setTrangThaiFromText(String text) {
        if (text == null) return;
        switch (text) {
            case CHO -> this.trangThai = TT_CHO;
            case HOAN_THANH -> this.trangThai = TT_HOAN_THANH;
            case HUY -> this.trangThai = TT_HUY;
        }
    }
    public void setTrangThaiTonKhoFromText(String text) {
        if (text == null) return;
        switch (text) {
            case BINH_THUONG -> this.trangThaiTonKho = TK_BINH_THUONG;
            case SAP_HET_HAN -> this.trangThaiTonKho = TK_SAP_HET_HAN;
            case HET_HAN -> this.trangThaiTonKho = TK_HET_HAN;
            case HET_HANG -> this.trangThaiTonKho = TK_HET_HANG;
        }
    }

    public static int parseTrangThaiFromText(String text) {
        if (text == null) return -1;
        return switch (text) {
            case CHO -> TT_CHO;
            case HOAN_THANH -> TT_HOAN_THANH;
            case HUY -> TT_HUY;
            default -> -1;
        };
    }
    public static int parseTrangThaiTonKhoFromText(String text) {
        if (text == null) return -1;
        return switch (text) {
            case BINH_THUONG -> TK_BINH_THUONG;
            case SAP_HET_HAN -> TK_SAP_HET_HAN;
            case HET_HAN -> TK_HET_HAN;
            case HET_HANG -> TK_HET_HANG;
            default -> -1;
        };
    }

}