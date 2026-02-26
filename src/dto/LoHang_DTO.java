package dto;

import java.time.LocalDate;

public class LoHang_DTO {

    private String maLo;
    private String maSP;

    private int soLuong;
    private int soLuongConLai;
    private double giaNhap;
    private LocalDate hsd;
    private double thanhTien;

    public LoHang_DTO() {
    }

    public LoHang_DTO(String maLo, String maSanPham,
                      int soLuong, int soLuongConLai,
                      double giaNhap, LocalDate hsd,
                      double thanhTien) {
        this.maLo = maLo;
        this.maSP = maSanPham;
        this.soLuong = soLuong;
        this.soLuongConLai = soLuongConLai;
        this.giaNhap = giaNhap;
        this.hsd = hsd;
        this.thanhTien = thanhTien;
    }

    public String getMaLo() {
        return maLo;
    }

    public void setMaLo(String maLo) {
        this.maLo = maLo;
    }

    public String getMaSanPham() {
        return maSP;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSP = maSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getSoLuongConLai() {
        return soLuongConLai;
    }

    public void setSoLuongConLai(int soLuongConLai) {
        this.soLuongConLai = soLuongConLai;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public LocalDate getHsd() {
        return hsd;
    }

    public void setHsd(LocalDate hsd) {
        this.hsd = hsd;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }
}
