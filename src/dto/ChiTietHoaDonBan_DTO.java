package dto;

public class ChiTietHoaDonBan_DTO {

    private int soLuong;
    private double donGia;
    private double tienGiam;
    private double thanhTien;

    private LoHang_DTO loSanPham;
    private KhuyenMai_DTO khuyenMai;

    public ChiTietHoaDonBan_DTO() {}

    public ChiTietHoaDonBan_DTO(int soLuong, double donGia,
                                double tienGiam, double thanhTien,
                                LoHang_DTO lo, KhuyenMai_DTO km) {
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.tienGiam = tienGiam;
        this.thanhTien = thanhTien;
        this.loSanPham = lo;
        this.khuyenMai = km;
    }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int v) { soLuong = v; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double v) { donGia = v; }

    public double getTienGiam() { return tienGiam; }
    public void setTienGiam(double v) { tienGiam = v; }

    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double v) { thanhTien = v; }

    public LoHang_DTO getLoSanPham() { return loSanPham; }
    public void setLoSanPham(LoHang_DTO v) { loSanPham = v; }

    public KhuyenMai_DTO getKhuyenMai() { return khuyenMai; }
    public void setKhuyenMai(KhuyenMai_DTO v) { khuyenMai = v; }
}
