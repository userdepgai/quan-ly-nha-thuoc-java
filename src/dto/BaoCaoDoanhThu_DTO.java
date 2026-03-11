package dto;

import java.util.Date;
public class BaoCaoDoanhThu_DTO {
    private Date ngayBan;
    private int soLuongHoaDon;
    private int soLuongSPDaBan;
    private double tongTienHang;
    private double giamGia;
    private double doanhThuThuan;
    private double loiNhuanGop;

    public BaoCaoDoanhThu_DTO() {}

    public Date getNgayBan() { return ngayBan; }
    public void setNgayBan(Date ngayBan) { this.ngayBan = ngayBan; }

    public int getSoLuongHoaDon() { return soLuongHoaDon; }
    public void setSoLuongHoaDon(int soLuongHoaDon) { this.soLuongHoaDon = soLuongHoaDon; }

    public int getSoLuongSPDaBan() { return soLuongSPDaBan; }
    public void setSoLuongSPDaBan(int soLuongSPDaBan) { this.soLuongSPDaBan = soLuongSPDaBan; }

    public double getTongTienHang() { return tongTienHang; }
    public void setTongTienHang(double tongTienHang) { this.tongTienHang = tongTienHang; }

    public double getGiamGia() { return giamGia; }
    public void setGiamGia(double giamGia) { this.giamGia = giamGia; }

    public double getDoanhThuThuan() { return doanhThuThuan; }
    public void setDoanhThuThuan(double doanhThuThuan) { this.doanhThuThuan = doanhThuThuan; }

    public double getLoiNhuanGop() { return loiNhuanGop; }
    public void setLoiNhuanGop(double loiNhuanGop) { this.loiNhuanGop = loiNhuanGop; }
}