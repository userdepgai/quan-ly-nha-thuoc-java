package dto;

public class SanPhamNCC_DTO {

    private String sanPham;
    private String nhaCungCap;
    private double giaNhap;
    private int trangThai;


    public SanPhamNCC_DTO(String sanPham, String nhaCungCap, double giaNhap, int trangThai) {
        this.sanPham = sanPham;
        this.nhaCungCap = nhaCungCap;
        this.giaNhap = giaNhap;
        this.trangThai = trangThai;
    }


    public String getSanPham() {
        return sanPham;
    }

    public void setSanPham(String sanPham) {
        this.sanPham = sanPham;
    }


    public String getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(String nhaCungCap) {
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