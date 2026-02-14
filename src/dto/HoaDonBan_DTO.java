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

    private KhachHang_DTO khachHang;
    private Voucher_DTO voucher;
    private ArrayList<ChiTietHoaDonBan_DTO> ds_chiTietHDB;

    public HoaDonBan_DTO() {
        ds_chiTietHDB = new ArrayList<>();
    }

    public HoaDonBan_DTO(
            String ma, LocalDateTime ngayLap, LocalDateTime ngayHoanThanh,
            double thanhTien, int trangThai, NhanVien_DTO nhanVien,
            int tinhTrangThanhToan, double tongTienGoc,
            double tongGiaTriKhuyenMai, String ghiChu,
            int diemThuongQuyDoi, double tienNhan,
            double tienThoi, double thueVAT,
            boolean keToa, String tenBacSi,
            String maToa, String ngayKeToa,
            KhachHang_DTO khachHang,
            Voucher_DTO voucher,
            ArrayList<ChiTietHoaDonBan_DTO> ds
    ) {
        super(ma, ngayLap, ngayHoanThanh, thanhTien, trangThai, nhanVien);

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
        this.khachHang = khachHang;
        this.voucher = voucher;
        this.ds_chiTietHDB = ds != null ? ds : new ArrayList<>();
    }

    public int getTinhTrangThanhToan() { return tinhTrangThanhToan; }
    public void setTinhTrangThanhToan(int t) { tinhTrangThanhToan = t; }

    public double getTongTienGoc() { return tongTienGoc; }
    public void setTongTienGoc(double v) { tongTienGoc = v; }

    public double getTongGiaTriKhuyenMai() { return tongGiaTriKhuyenMai; }
    public void setTongGiaTriKhuyenMai(double v) { tongGiaTriKhuyenMai = v; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String v) { ghiChu = v; }

    public int getDiemThuongQuyDoi() { return diemThuongQuyDoi; }
    public void setDiemThuongQuyDoi(int v) { diemThuongQuyDoi = v; }

    public double getTienNhan() { return tienNhan; }
    public void setTienNhan(double v) { tienNhan = v; }

    public double getTienThoi() { return tienThoi; }
    public void setTienThoi(double v) { tienThoi = v; }

    public double getThueVAT() { return thueVAT; }
    public void setThueVAT(double v) { thueVAT = v; }

    public boolean isKeToa() { return keToa; }
    public void setKeToa(boolean v) { keToa = v; }

    public String getTenBacSi() { return tenBacSi; }
    public void setTenBacSi(String v) { tenBacSi = v; }

    public String getMaToa() { return maToa; }
    public void setMaToa(String v) { maToa = v; }

    public String getNgayKeToa() { return ngayKeToa; }
    public void setNgayKeToa(String v) { ngayKeToa = v; }

    public KhachHang_DTO getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang_DTO v) { khachHang = v; }

    public Voucher_DTO getVoucher() { return voucher; }
    public void setVoucher(Voucher_DTO v) { voucher = v; }

    public ArrayList<ChiTietHoaDonBan_DTO> getDs_chiTietHDB() {
        return ds_chiTietHDB;
    }
    public void setDs_chiTietHDB(ArrayList<ChiTietHoaDonBan_DTO> v) {
        ds_chiTietHDB = v;
    }
}
