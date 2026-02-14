package dto;

public class ChiTietHoaDonBan_DTO {

    private int soLuong;
    private double giaBan;
    private double giaBanSauAp_KM;
    private double thanhTien;

    private LoHang_DTO loSanPham;
    private KhuyenMai_DTO khuyenMai;

    public ChiTietHoaDonBan_DTO() {}

    public ChiTietHoaDonBan_DTO(int soLuong, double donGia,
                                double tienGiam, double thanhTien,
                                LoHang_DTO lo, KhuyenMai_DTO km) {
        this.soLuong = soLuong;
        this.giaBan = donGia;
        this.giaBanSauAp_KM = tienGiam;
        this.thanhTien = thanhTien;
        this.loSanPham = lo;
        this.khuyenMai = km;
    }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int v) { soLuong = v; }

    public double getGiaBan() { return giaBan; }
    public void setGiaBan(double v) { giaBan = v; }

    public double getGiaBanSauAp_KM() { return giaBanSauAp_KM; }
    public void setGiaBanSauAp_KM(double v) { giaBanSauAp_KM = v; }

    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double v) { thanhTien = v; }

    public LoHang_DTO getLoSanPham() { return loSanPham; }
    public void setLoSanPham(LoHang_DTO v) { loSanPham = v; }

    public KhuyenMai_DTO getKhuyenMai() { return khuyenMai; }
    public void setKhuyenMai(KhuyenMai_DTO v) { khuyenMai = v; }
}
