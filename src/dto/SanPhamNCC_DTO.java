package dto;

import dto.SANPHAM;

public class SanPhamNCC_DTO {

    private String maSanPham;
    private String maNCC;

    private double giaNhap;
    private int trangThai;

    public SanPhamNCC_DTO() {
    }

    public SanPhamNCC_DTO(String maSanPham, String maNCC,
                          double giaNhap, int trangThai) {
        this.maSanPham = maSanPham;
        this.maNCC = maNCC;
        this.giaNhap = giaNhap;
        this.trangThai = trangThai;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}
