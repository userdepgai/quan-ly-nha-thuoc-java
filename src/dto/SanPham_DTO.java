package dto;

public class SanPham_DTO {

    private String maSP;
    private String tenSP;
    private int donViTinh;
    private String hinhAnh;
    private double loiNhuan;
    private int trangThai;

    private String maDM;
    private String maQC;

    public SanPham_DTO() {
    }

    public SanPham_DTO(String maSP, String tenSP, int donViTinh,
                       String hinhAnh, double loiNhuan,
                       int trangThai, String maDM, String maQuyCach) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.donViTinh = donViTinh;
        this.hinhAnh = hinhAnh;
        this.loiNhuan = loiNhuan;
        this.trangThai = trangThai;
        this.maDM = maDM;
        this.maQC = maQuyCach;
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

    public int getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(int donViTinh) {
        this.donViTinh = donViTinh;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public double getLoiNhuan() {
        return loiNhuan;
    }

    public void setLoiNhuan(double loiNhuan) {
        this.loiNhuan = loiNhuan;
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

    public String getMaQuyCach() {
        return maQC;
    }

    public void setMaQuyCach(String maQuyCach) {
        this.maQC = maQuyCach;
    }
}
