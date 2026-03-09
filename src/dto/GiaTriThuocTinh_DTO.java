package dto;

public class GiaTriThuocTinh_DTO {
    private String maGiaTri;
    private String ndGiaTri;
    private String maThuocTinh;

    public GiaTriThuocTinh_DTO() {
    }

    public GiaTriThuocTinh_DTO(String maGiaTri, String ndGiaTri, String maThuocTinh) {
        this.maGiaTri = maGiaTri;
        this.ndGiaTri = ndGiaTri;
        this.maThuocTinh = maThuocTinh;
    }

    public String getMaGiaTri() { return maGiaTri; }
    public void setMaGiaTri(String maGiaTri) { this.maGiaTri = maGiaTri; }

    public String getNdGiaTri() { return ndGiaTri; }
    public void setNdGiaTri(String ndGiaTri) { this.ndGiaTri = ndGiaTri; }

    public String getMaThuocTinh() { return maThuocTinh; }
    public void setMaThuocTinh(String maThuocTinh) { this.maThuocTinh = maThuocTinh; }

    @Override
    public String toString() {
        return ndGiaTri;
    }
}