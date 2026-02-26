package dto;

public class GiaTriThuocTinh_DTO {

    private String maGiaTri;
    private String ndGiaTri;
    private int trangThai;

    private String maThuocTinh;

    public GiaTriThuocTinh_DTO() {
    }

    public GiaTriThuocTinh_DTO(String maGiaTri, String ndGiaTri,
                               int trangThai, String maThuocTinh) {
        this.maGiaTri = maGiaTri;
        this.ndGiaTri = ndGiaTri;
        this.trangThai = trangThai;
        this.maThuocTinh = maThuocTinh;
    }

    public String getMaGiaTri() {
        return maGiaTri;
    }

    public void setMaGiaTri(String maGiaTri) {
        this.maGiaTri = maGiaTri;
    }

    public String getNdGiaTri() {
        return ndGiaTri;
    }

    public void setNdGiaTri(String ndGiaTri) {
        this.ndGiaTri = ndGiaTri;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaThuocTinh() {
        return maThuocTinh;
    }

    public void setMaThuocTinh(String maThuocTinh) {
        this.maThuocTinh = maThuocTinh;
    }
}
