package dto;

public class ChiTietPhieuNhapKho_DTO {
    private String maPNK;
    private String maLoHang;
    private int soLuongDat;

    public ChiTietPhieuNhapKho_DTO(){}

    public ChiTietPhieuNhapKho_DTO(String maPNK, String maLoHang, int soLuongDat) {
        this.maPNK = maPNK;
        this.maLoHang = maLoHang;
        this.soLuongDat = soLuongDat;
    }

    public String getMaPNK() {
        return maPNK;
    }

    public void setMaPNK(String maPNK) {
        this.maPNK = maPNK;
    }

    public String getMaLoHang() {
        return maLoHang;
    }

    public void setMaLoHang(String maLoHang) {
        this.maLoHang = maLoHang;
    }

    public int getSoLuongDat() {
        return soLuongDat;
    }

    public void setSoLuongDat(int soLuongDat) {
        this.soLuongDat = soLuongDat;
    }
}
