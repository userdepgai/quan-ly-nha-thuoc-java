package dto;

public class ChiTietGioHang_DTO {

    private String maGH;
    private String maSP;
    private int soLuong;
    private String maLo;

    public ChiTietGioHang_DTO() {
    }

    public ChiTietGioHang_DTO(String maGH, String maSP, int soLuong) {
        this.maGH = maGH;
        this.maSP = maSP;
        this.soLuong = soLuong;

    }
    public String getMaGH() {
        return maGH;
    }

    public void setMaGH(String maGH) {
        this.maGH = maGH;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    public String getMaLo() {
        return maLo;
    }

    public void setMaLo(String maLo) {
        this.maLo = maLo;
    }
}