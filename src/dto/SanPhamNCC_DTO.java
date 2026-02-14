package dto;

public class SanPhamNCC_DTO {

    private SanPham_DTO sanPham;
    private NhaCungCap_DTO nhaCungCap;

    private double giaNhap;
    private int trangThai;


    public SanPhamNCC_DTO() {
    }


    public SanPhamNCC_DTO(SanPham_DTO sanPham, NhaCungCap_DTO nhaCungCap, double giaNhap, int trangThai) {
        this.sanPham = sanPham;
        this.nhaCungCap = nhaCungCap;
        this.giaNhap = giaNhap;
        this.trangThai = trangThai;
    }

    public SanPham_DTO getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham_DTO sanPham) {
        this.sanPham = sanPham;
    }

    public NhaCungCap_DTO getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(NhaCungCap_DTO nhaCungCap) {
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