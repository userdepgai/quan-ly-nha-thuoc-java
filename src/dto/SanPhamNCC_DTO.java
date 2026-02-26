package dto;

import dto.SANPHAM;

public class SanPhamNCC_DTO {

    private SANPHAM sanPham;
    private NHACUNGCAP nhaCungCap;

    private double giaNhap;
    private int trangThai;


    public SanPhamNCC_DTO() {
    }


    public SanPhamNCC_DTO(SANPHAM sanPham, NHACUNGCAP nhaCungCap, double giaNhap, int trangThai) {
        this.sanPham = sanPham;
        this.nhaCungCap = nhaCungCap;
        this.giaNhap = giaNhap;
        this.trangThai = trangThai;
    }

    public SANPHAM getSanPham() {
        return sanPham;
    }

    public void setSanPham(SANPHAM sanPham) {
        this.sanPham = sanPham;
    }

    public NHACUNGCAP getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(NHACUNGCAP nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
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