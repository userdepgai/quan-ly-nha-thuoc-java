package dto;

public class SanPham_DTO {
    private String maSP;
    private String tenSP;
    private String donViTinh;
    private double phanTramLoiNhuan;
    private String hinhAnh;
    private int trangThai;
    private String maDM;
    private String maQC;

    public SanPham_DTO() {
    }

    public SanPham_DTO(String maSP, String tenSP, String donViTinh, double phanTramLoiNhuan, String hinhAnh, int trangThai, String maDM, String maQC) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.donViTinh = donViTinh;
        this.phanTramLoiNhuan = phanTramLoiNhuan;
        this.hinhAnh = hinhAnh;
        this.trangThai = trangThai;
        this.maDM = maDM;
        this.maQC = maQC;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public double getPhanTramLoiNhuan() {
        return phanTramLoiNhuan;
    }

    public void setPhanTramLoiNhuan(double phanTramLoiNhuan) {
        this.phanTramLoiNhuan = phanTramLoiNhuan;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaDM() {
        return maDM;
    }

    public void setMaDM(String maDM) {
        this.maDM = maDM;
    }

    public String getMaQC() {
        return maQC;
    }

    public void setMaQC(String maQC) {
        this.maQC = maQC;
    }
}