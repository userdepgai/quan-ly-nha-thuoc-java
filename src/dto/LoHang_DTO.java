package dto;

import java.time.LocalDate;

public class LoHang_DTO {

    private String maLo;
    private double giaNhap;
    private LocalDate hsd;
    private int soLuong;
    private int soLuongConLai;
    private double thanhTien;
    private int trangThai;

    private String maNcc;
    private String maKvlt;
    private String maSp;

    public LoHang_DTO() {
    }

    public LoHang_DTO(String maLo, double giaNhap, LocalDate hsd,
                      int soLuong, int soLuongConLai,
                      double thanhTien, int trangThai,
                      String maNcc, String maKvlt, String maSp) {

        this.maLo = maLo;
        this.giaNhap = giaNhap;
        this.hsd = hsd;
        this.soLuong = soLuong;
        this.soLuongConLai = soLuongConLai;
        this.thanhTien = thanhTien;
        this.trangThai = trangThai;
        this.maNcc = maNcc;
        this.maKvlt = maKvlt;
        this.maSp = maSp;
    }

    public String getMaLo() {
        return maLo;
    }

    public void setMaLo(String maLo) {
        this.maLo = maLo;
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

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaNcc() {
        return maNcc;
    }

    public void setMaNcc(String maNcc) {
        this.maNcc = maNcc;
    }

    public String getMaKvlt() {
        return maKvlt;
    }

    public void setMaKvlt(String maKvlt) {
        this.maKvlt = maKvlt;
    }

    public String getMaSp() {
        return maSp;
    }

    public void setMaSp(String maSp) {
        this.maSp = maSp;
    }
}