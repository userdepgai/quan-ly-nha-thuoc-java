package dto;

public class ThongKe_DTO {
    private String maSanPham;
    private double giaNhap;
    private double giaBan;
    private int soLuongBan;
    private double loiNhuan;

    private double tongDoanhThu;

    public ThongKe_DTO() {
    }

    public String getMaSanPham() { return maSanPham; }
    public void setMaSanPham(String maSanPham) { this.maSanPham = maSanPham; }

    public double getGiaNhap() { return giaNhap; }
    public void setGiaNhap(double giaNhap) { this.giaNhap = giaNhap; }

    public double getGiaBan() { return giaBan; }
    public void setGiaBan(double giaBan) { this.giaBan = giaBan; }

    public int getSoLuongBan() { return soLuongBan; }
    public void setSoLuongBan(int soLuongBan) { this.soLuongBan = soLuongBan; }

    public double getLoiNhuan() { return loiNhuan; }
    public void setLoiNhuan(double loiNhuan) { this.loiNhuan = loiNhuan; }

    public double getTongDoanhThu() { return tongDoanhThu; }
    public void setTongDoanhThu(double tongDoanhThu) { this.tongDoanhThu = tongDoanhThu; }
}