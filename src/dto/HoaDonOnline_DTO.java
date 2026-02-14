package dto;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class HoaDonOnline_DTO extends HoaDonBan_DTO {

    private DiaChi_DTO diaChiGiaoHang;
    private double phiVanChuyen;

    public HoaDonOnline_DTO() {}

    public HoaDonOnline_DTO(
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
            ArrayList<ChiTietHoaDonBan_DTO> ds,
            DiaChi_DTO diaChi, double phiVC
    ) {
        super(ma, ngayLap, ngayHoanThanh, thanhTien, trangThai, nhanVien,
                tinhTrangThanhToan, tongTienGoc, tongGiaTriKhuyenMai,
                ghiChu, diemThuongQuyDoi, tienNhan, tienThoi,
                thueVAT, keToa, tenBacSi, maToa, ngayKeToa,
                khachHang, voucher, ds);

        this.diaChiGiaoHang = diaChi;
        this.phiVanChuyen = phiVC;
    }

    public DiaChi_DTO getDiaChiGiaoHang() { return diaChiGiaoHang; }
    public void setDiaChiGiaoHang(DiaChi_DTO v) { diaChiGiaoHang = v; }

    public double getPhiVanChuyen() { return phiVanChuyen; }
    public void setPhiVanChuyen(double v) { phiVanChuyen = v; }
}
