package dto;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class HoaDonBan_DTO extends HoaDon_DTO {
    private int tinhTrangThanhToan;
    private double tongTienGoc;
    private double tongGiaTriKhuyenMai;
    private String ghiChu;
    private int diemThuongQuyDoi;
    private double tienNhan;
    private double tienThoi;
    private double thueVAT;
    private boolean keToa;
    private int loaiHDB;

    private String maKhachHang;
    private String maVoucher;

    private ArrayList<ChiTietHoaDonBan_DTO> ds_chiTietHDB;

    public HoaDonBan_DTO() {
        ds_chiTietHDB = new ArrayList<>();
    }

    public HoaDonBan_DTO(
            String ma,
            LocalDateTime ngayLap,
            LocalDateTime ngayHoanThanh,
            double thanhTien,
            int trangThai,
            String maNhanVien,

            int tinhTrangThanhToan,
            double tongTienGoc,
            double tongGiaTriKhuyenMai,
            String ghiChu,
            int diemThuongQuyDoi,
            double tienNhan,
            double tienThoi,
            double thueVAT,
            boolean keToa,
            int loaiHDB,
            String maKhachHang,
            String maVoucher,
            ArrayList<ChiTietHoaDonBan_DTO> ds
    ) {
        super(ma, ngayLap, ngayHoanThanh, thanhTien, trangThai, maNhanVien);

        this.tinhTrangThanhToan = tinhTrangThanhToan;
        this.tongTienGoc = tongTienGoc;
        this.tongGiaTriKhuyenMai = tongGiaTriKhuyenMai;
        this.ghiChu = ghiChu;
        this.diemThuongQuyDoi = diemThuongQuyDoi;
        this.tienNhan = tienNhan;
        this.tienThoi = tienThoi;
        this.thueVAT = thueVAT;
        this.keToa = keToa;
        this.loaiHDB = loaiHDB;
        this.maKhachHang = maKhachHang;
        this.maVoucher = maVoucher;
        this.ds_chiTietHDB = ds != null ? ds : new ArrayList<>();
    }

    public int getTinhTrangThanhToan() {
        return tinhTrangThanhToan;
    }

    public void setTinhTrangThanhToan(int tinhTrangThanhToan) {
        this.tinhTrangThanhToan = tinhTrangThanhToan;
    }

    public double getTongTienGoc() {
        return tongTienGoc;
    }

    public void setTongTienGoc(double tongTienGoc) {
        this.tongTienGoc = tongTienGoc;
    }

    public double getTongGiaTriKhuyenMai() {
        return tongGiaTriKhuyenMai;
    }

    public void setTongGiaTriKhuyenMai(double tongGiaTriKhuyenMai) {
        this.tongGiaTriKhuyenMai = tongGiaTriKhuyenMai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public int getDiemThuongQuyDoi() {
        return diemThuongQuyDoi;
    }

    public void setDiemThuongQuyDoi(int diemThuongQuyDoi) {
        this.diemThuongQuyDoi = diemThuongQuyDoi;
    }

    public double getTienNhan() {
        return tienNhan;
    }

    public void setTienNhan(double tienNhan) {
        this.tienNhan = tienNhan;
    }

    public double getTienThoi() {
        return tienThoi;
    }

    public void setTienThoi(double tienThoi) {
        this.tienThoi = tienThoi;
    }

    public double getThueVAT() {
        return thueVAT;
    }

    public void setThueVAT(double thueVAT) {
        this.thueVAT = thueVAT;
    }

    public boolean isKeToa() {
        return keToa;
    }

    public void setKeToa(boolean keToa) {
        this.keToa = keToa;
    }
    public int getLoaiHDB() {return loaiHDB;}

    public void setLoaiHDB(int loaiHDB) {this.loaiHDB = loaiHDB;}

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getMaVoucher() {
        return maVoucher;
    }

    public void setMaVoucher(String maVoucher) {
        this.maVoucher = maVoucher;
    }

    public ArrayList<ChiTietHoaDonBan_DTO> getDs_chiTietHDB() {
        return ds_chiTietHDB;
    }

    public void setDs_chiTietHDB(ArrayList<ChiTietHoaDonBan_DTO> ds_chiTietHDB) {
        this.ds_chiTietHDB = ds_chiTietHDB;
    }

    // =============================
// TRẠNG THÁI HÓA ĐƠN
// =============================

    // ===== VALUE (dùng cho DB + BUS) =====
    public static final int TT_CHO_DUYET = 0;
    public static final int TT_DA_DUYET = 1;
    public static final int TT_DANG_GIAO = 2;
    public static final int TT_HOAN_THANH = 3;
    public static final int TT_DA_HUY = 4;
    public static final int TT_YEU_CAU_HOAN = 5;


    // ===== TEXT (dùng cho GUI) =====
    public static final String CHO_DUYET = "Chờ duyệt";
    public static final String DA_DUYET = "Đã duyệt";
    public static final String DANG_GIAO = "Đang giao";
    public static final String HOAN_THANH = "Đã hoàn thành";
    public static final String DA_HUY = "Hủy";
    public static final String YEU_CAU_HOAN = "Yêu cầu hoàn hàng";


    // =============================
// INT -> TEXT (hiển thị GUI)
// =============================
    public String getTrangThaiText() {

        switch (getTrangThai()) {
            case TT_CHO_DUYET: return CHO_DUYET;
            case TT_DA_DUYET: return DA_DUYET;
            case TT_DANG_GIAO: return DANG_GIAO;
            case TT_HOAN_THANH: return HOAN_THANH;
            case TT_DA_HUY: return DA_HUY;
            case TT_YEU_CAU_HOAN: return YEU_CAU_HOAN;
            default: return "Không xác định";
        }
    }


    // =============================
// KÊ TOA
// =============================
    public static final String KHONG_KE_TOA = "Không";
    public static final String CO_KE_TOA = "Có";


    // boolean -> text
    public String getKeToaText() {
        return keToa ? CO_KE_TOA : KHONG_KE_TOA;
    }



    // =============================
// TÌNH TRẠNG THANH TOÁN
// =============================

    // ===== VALUE (dùng cho DB + BUS) =====
    public static final int TT_CHUA_THANH_TOAN = 0;
    public static final int TT_DA_THANH_TOAN = 1;
    public static final int TT_DA_HOAN_TIEN = 2;


    // ===== TEXT (dùng cho GUI) =====
    public static final String CHUA_THANH_TOAN = "Chưa thanh toán";
    public static final String DA_THANH_TOAN = "Đã thanh toán";
    public static final String DA_HOAN_TIEN = "Đã hoàn tiền";


    // =============================
// INT -> TEXT (DTO -> GUI)
// =============================
    public String getTinhTrangThanhToanText() {

        switch (tinhTrangThanhToan) {
            case TT_CHUA_THANH_TOAN:
                return CHUA_THANH_TOAN;

            case TT_DA_THANH_TOAN:
                return DA_THANH_TOAN;

            case TT_DA_HOAN_TIEN:
                return DA_HOAN_TIEN;

            default:
                return "Không xác định";
        }
    }


    // =============================
// LOẠI HÓA ĐƠN BÁN
// =============================

    // ===== VALUE (dùng cho DB + BUS) =====
    public static final int LOAI_TAI_QUAY = 0;
    public static final int LOAI_TRUC_TUYEN = 1;


    // ===== TEXT (dùng cho GUI) =====
    public static final String TAI_QUAY = "Tại quầy";
    public static final String TRUC_TUYEN = "Trực tuyến";


    // =============================
// INT -> TEXT (DTO -> GUI)
// =============================
    public String getLoaiHDBText() {

        switch (loaiHDB) {
            case LOAI_TAI_QUAY:
                return TAI_QUAY;

            case LOAI_TRUC_TUYEN:
                return TRUC_TUYEN;

            default:
                return "Không xác định";
        }
    }


}
