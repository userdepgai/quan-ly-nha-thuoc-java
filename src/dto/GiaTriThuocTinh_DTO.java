package dto;

public class GiaTriThuocTinh_DTO {
    private String maGTTT;
    private String noiDungGiaTri;
    private int trangThai;
    private String maThuocTinh;

    public GiaTriThuocTinh_DTO() {
    }

    public GiaTriThuocTinh_DTO(String maGTTT, String noiDungGiaTri, int trangThai, String maThuocTinh) {
        this.maGTTT = maGTTT;
        this.noiDungGiaTri = noiDungGiaTri;
        this.trangThai = trangThai;
        this.maThuocTinh = maThuocTinh;
    }

    public String getMaGTTT() {
        return maGTTT;
    }

    public void setMaGTTT(String maGTTT) {
        this.maGTTT = maGTTT;
    }

    public String getNoiDungGiaTri() {
        return noiDungGiaTri;
    }

    public void setNoiDungGiaTri(String noiDungGiaTri) {
        this.noiDungGiaTri = noiDungGiaTri;
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