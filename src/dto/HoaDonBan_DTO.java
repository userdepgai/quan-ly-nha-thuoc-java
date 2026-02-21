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
    private String tenBacSi;
    private String maToa;
    private String ngayKeToa;

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
            String tenBacSi,
            String maToa,
            String ngayKeToa,
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
        this.tenBacSi = tenBacSi;
        this.maToa = maToa;
        this.ngayKeToa = ngayKeToa;
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

    public String getTenBacSi() {
        return tenBacSi;
    }

    public void setTenBacSi(String tenBacSi) {
        this.tenBacSi = tenBacSi;
    }

    public String getMaToa() {
        return maToa;
    }

    public void setMaToa(String maToa) {
        this.maToa = maToa;
    }

    public String getNgayKeToa() {
        return ngayKeToa;
    }

    public void setNgayKeToa(String ngayKeToa) {
        this.ngayKeToa = ngayKeToa;
    }

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
}
