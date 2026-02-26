package dto;

public class GiaTriThuocTinh_SP_DTO {

    private String maSP;
    private String maThuocTinh;
    private String maGiaTri;

    public GiaTriThuocTinh_SP_DTO() {
    }

    public GiaTriThuocTinh_SP_DTO(String maSP, String maThuocTinh, String maGiaTri) {
        this.maSP = maSP;
        this.maThuocTinh = maThuocTinh;
        this.maGiaTri = maGiaTri;
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

    public String getMaGiaTri() {
        return maGiaTri;
    }

    public void setMaGiaTri(String maGiaTri) {
        this.maGiaTri = maGiaTri;
    }
}
