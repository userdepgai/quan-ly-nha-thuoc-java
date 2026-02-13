
public class SanPhamNCC_DTO {
    private String maSP;
    private String maNCC;
    private double giaNhap;
    private int trangThai;

    public SanPhamNCC_DTO(String maSP, String maNCC, double giaNhap, int trangThai) {
        this.maSP = maSP;
        this.maNCC = maNCC;
        this.giaNhap = giaNhap;
        this.trangThai = trangThai;
    }

    public String getMaSP() {
        return maSP;
    }
    public void setMaSP(String maSP) {
        this.maSP = maSP;
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