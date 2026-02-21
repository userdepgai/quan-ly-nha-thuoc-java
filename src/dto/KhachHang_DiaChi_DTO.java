package dto;

public class KhachHang_DiaChi_DTO {

    private String maKH;
    private String maDC;
    private int trangThai; // 1: mặc định, 0: không

    public KhachHang_DiaChi_DTO() {}

    public KhachHang_DiaChi_DTO(String maKH, String maDC, int trangThai) {
        this.maKH = maKH;
        this.maDC = maDC;
        this.trangThai = trangThai;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getMaDC() {
        return maDC;
    }

    public void setMaDC(String maDC) {
        this.maDC = maDC;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}
