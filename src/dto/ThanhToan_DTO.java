package dto;

import java.util.List;

public class ThanhToan_DTO {

    private String tenKH;
    private String sdt;
    private String diaChiChiTiet;
    private List<ChiTietHoaDonBan_DTO> danhSachSanPham;
    private String maVoucher;
    private double tongThanhToan;
    private String ghiChu;
    public ThanhToan_DTO() {
    }


    public ThanhToan_DTO(String tenKH, String sdt, String diaChiChiTiet) {
        this.tenKH = tenKH;
        this.sdt = sdt;
        this.diaChiChiTiet = diaChiChiTiet;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChiChiTiet() {
        return diaChiChiTiet;
    }

    public void setDiaChiChiTiet(String diaChiChiTiet) {
        this.diaChiChiTiet = diaChiChiTiet;
    }

    public List<ChiTietHoaDonBan_DTO> getDanhSachSanPham() {
        return danhSachSanPham;
    }

    public void setDanhSachSanPham(List<ChiTietHoaDonBan_DTO> danhSachSanPham) {
        this.danhSachSanPham = danhSachSanPham;
    }

    public String getMaVoucher() {
        return maVoucher;
    }

    public void setMaVoucher(String maVoucher) {
        this.maVoucher = maVoucher;
    }

    public double getTongThanhToan() {
        return tongThanhToan;
    }

    public void setTongThanhToan(double tongThanhToan) {
        this.tongThanhToan = tongThanhToan;
    }
    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}