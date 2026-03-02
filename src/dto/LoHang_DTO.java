package dto;

import java.time.LocalDate;

public class LoHang_DTO {

    private String maLo;
    private double giaNhap;
    private LocalDate hsd;

    private int soLuongNhap;
    private int soLuongConLai;

    private double thanhTien;

    private int trangThai;
    private int trangThaiTonKho;

    private String maPnk;
    private String maNcc;
    private String maKvlt;
    private String maSp;

    public LoHang_DTO() {}

    public LoHang_DTO(String maLo, double giaNhap, LocalDate hsd,
                      int soLuongNhap, int soLuongConLai,
                      double thanhTien,
                      int trangThai, int trangThaiTonKho,
                      String maPnk, String maNcc,
                      String maKvlt, String maSp) {

        this.maLo = maLo;
        this.giaNhap = giaNhap;
        this.hsd = hsd;
        this.soLuongNhap = soLuongNhap;
        this.soLuongConLai = soLuongConLai;
        this.thanhTien = thanhTien;
        this.trangThai = trangThai;
        this.trangThaiTonKho = trangThaiTonKho;
        this.maPnk = maPnk;
        this.maNcc = maNcc;
        this.maKvlt = maKvlt;
        this.maSp = maSp;
    }

    public String getMaLo() { return maLo; }
    public void setMaLo(String maLo) { this.maLo = maLo; }

    public double getGiaNhap() { return giaNhap; }
    public void setGiaNhap(double giaNhap) { this.giaNhap = giaNhap; }

    public LocalDate getHsd() { return hsd; }
    public void setHsd(LocalDate hsd) { this.hsd = hsd; }

    public int getSoLuongNhap() { return soLuongNhap; }
    public void setSoLuongNhap(int soLuongNhap) { this.soLuongNhap = soLuongNhap; }

    public int getSoLuongConLai() { return soLuongConLai; }
    public void setSoLuongConLai(int soLuongConLai) { this.soLuongConLai = soLuongConLai; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    public int getTrangThaiTonKho() { return trangThaiTonKho; }
    public void setTrangThaiTonKho(int trangThaiTonKho) { this.trangThaiTonKho = trangThaiTonKho; }

    public String getMaPnk() { return maPnk; }
    public void setMaPnk(String maPnk) { this.maPnk = maPnk; }

    public String getMaNcc() { return maNcc; }
    public void setMaNcc(String maNcc) { this.maNcc = maNcc; }

    public String getMaKvlt() { return maKvlt; }
    public void setMaKvlt(String maKvlt) { this.maKvlt = maKvlt; }

    public String getMaSp() { return maSp; }
    public void setMaSp(String maSp) { this.maSp = maSp; }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public void congSoLuongConLai(int num) {
        this.soLuongConLai += num;
    }

    public boolean truSoLuongConLai(int num) {
        if (num <= 0 || num > soLuongConLai) return false;
        this.soLuongConLai -= num;
        return true;
    }

    public double tinhThanhTien() {
        return giaNhap * soLuongNhap;
    }
}