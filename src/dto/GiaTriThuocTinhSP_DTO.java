package dto;

public class GiaTriThuocTinhSP_DTO {
    private String maGTTT;
    private String maSP;
    private String maThuocTinh;

    public GiaTriThuocTinhSP_DTO() {
    }

    public GiaTriThuocTinhSP_DTO(String maGTTT, String maSP, String maThuocTinh) {
        this.maGTTT = maGTTT;
        this.maSP = maSP;
        this.maThuocTinh = maThuocTinh;
    }

    public String getMaGTTT() {
        return maGTTT;
    }

    public void setMaGTTT(String maGTTT) {
        this.maGTTT = maGTTT;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getMaThuocTinh() {
        return maThuocTinh;
    }

    public void setMaThuocTinh(String maThuocTinh) {
        this.maThuocTinh = maThuocTinh;
    }
}