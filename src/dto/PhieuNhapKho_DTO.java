package dto;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PhieuNhapKho_DTO extends HoaDon_DTO{

    public static final int TT_CHUAN_BI = 0;
    public static final int TT_CHO = 1;
    public static final int TT_HOAN_THANH = 2;

    public static final String CHUAN_BI = "Chuẩn bị";
    public static final String CHO = "Chờ";
    public static final String HOAN_THANH = "Hoàn thành";

    private String maKVLT;
    private String maNCC;
    private ArrayList<ChiTietPhieuNhapKho_DTO> ds_chiTietPNK;

    public PhieuNhapKho_DTO() {
        this.ds_chiTietPNK = new ArrayList<>();
    }

    public PhieuNhapKho_DTO(String ma, LocalDateTime ngayLap, LocalDateTime ngayHoanThanh, double thanhTien, int trangThai, String maNhanVien, String maKVLT, String maNCC, ArrayList<ChiTietPhieuNhapKho_DTO> ds_chiTietPNK) {
        super(ma, ngayLap, ngayHoanThanh, thanhTien, trangThai, maNhanVien);
        this.maKVLT = maKVLT;
        this.maNCC = maNCC;
        this.ds_chiTietPNK = ds_chiTietPNK;
    }

    public PhieuNhapKho_DTO(String maKVLT, String maNCC, ArrayList<ChiTietPhieuNhapKho_DTO> ds_chiTietPNK) {
        this.maKVLT = maKVLT;
        this.maNCC = maNCC;
        this.ds_chiTietPNK = ds_chiTietPNK;
    }

    public String getMaKVLT() {
        return maKVLT;
    }

    public void setMaKVLT(String maKVLT) {
        this.maKVLT = maKVLT;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public ArrayList<ChiTietPhieuNhapKho_DTO> getDs_chiTietPNK() {
        return ds_chiTietPNK;
    }

    public void setDs_chiTietPNK(ArrayList<ChiTietPhieuNhapKho_DTO> ds_chiTietPNK) {
        this.ds_chiTietPNK = ds_chiTietPNK;
    }

    public String getTrangThaiText() {
        return switch (this.getTrangThai()) {
            case TT_CHUAN_BI -> CHUAN_BI;
            case TT_CHO -> CHO;
            case TT_HOAN_THANH -> HOAN_THANH;
            default -> "Không xác định";
        };
    }

    public void setTrangThaiFromText(String text) {
        if (text == null) return;

        switch (text) {
            case CHUAN_BI -> this.setTrangThai(TT_CHUAN_BI);
            case CHO -> this.setTrangThai(TT_CHO);
            case HOAN_THANH -> this.setTrangThai(TT_HOAN_THANH);
        }
    }
    public static int parseTrangThaiFromText(String text) {
        if (text == null) return -1;

        return switch (text) {
            case CHUAN_BI -> TT_CHUAN_BI;
            case CHO -> TT_CHO;
            case HOAN_THANH -> TT_HOAN_THANH;
            default -> -1;
        };
    }
}