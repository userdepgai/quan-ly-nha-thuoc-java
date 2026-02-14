package dto;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class HoaDonOnline_DTO extends HoaDonBan_DTO {

    private String maDiaChiGiaoHang;
    private double phiVanChuyen;

    public HoaDonOnline_DTO() {
    }

    public HoaDonOnline_DTO(
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
            ArrayList<ChiTietHoaDonBan_DTO> ds,

            String maDiaChiGiaoHang,
            double phiVanChuyen
    ) {
        super(ma, ngayLap, ngayHoanThanh, thanhTien, trangThai, maNhanVien,
                tinhTrangThanhToan, tongTienGoc, tongGiaTriKhuyenMai,
                ghiChu, diemThuongQuyDoi, tienNhan, tienThoi,
                thueVAT, keToa, tenBacSi, maToa, ngayKeToa,
                maKhachHang, maVoucher, ds);

        this.maDiaChiGiaoHang = maDiaChiGiaoHang;
        this.phiVanChuyen = phiVanChuyen;
    }

    public String getMaDiaChiGiaoHang() {
        return maDiaChiGiaoHang;
    }

    public void setMaDiaChiGiaoHang(String maDiaChiGiaoHang) {
        this.maDiaChiGiaoHang = maDiaChiGiaoHang;
    }

    public double getPhiVanChuyen() {
        return phiVanChuyen;
    }

    public void setPhiVanChuyen(double phiVanChuyen) {
        this.phiVanChuyen = phiVanChuyen;
    }
}
