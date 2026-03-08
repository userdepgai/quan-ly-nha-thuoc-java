package dto;

public class ChiTietPhieuNhapKho_DTO {

    private String maPNK;
    private String maSP;
    private int soLuong;
    private String maLo;

    public ChiTietPhieuNhapKho_DTO() {}

    public ChiTietPhieuNhapKho_DTO(String maPNK, String maSP, int soLuong, String maLo) {
        this.maPNK = maPNK;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.maLo = maLo;
    }

    public String getMaPNK() {
        return maPNK;
    }

    public void setMaPNK(String maPNK) {
        this.maPNK = maPNK;
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