package dto;

public class ThuocTinhDanhMuc_DTO {
    private String maThuocTinh;
    private String tenTT;
    private String kieuTT;
    private int trangThai;
    private String maDM;

    public ThuocTinhDanhMuc_DTO() {
    }

    public ThuocTinhDanhMuc_DTO(String maThuocTinh, String tenTT, String kieuTT, int trangThai, String maDM) {
        this.maThuocTinh = maThuocTinh;
        this.tenTT = tenTT;
        this.kieuTT = kieuTT;
        this.trangThai = trangThai;
        this.maDM = maDM;
    }

    public String getMaThuocTinh() {
        return maThuocTinh;
    }

    public void setMaThuocTinh(String maThuocTinh) {
        this.maThuocTinh = maThuocTinh;
    }

    public String getTenTT() {
        return tenTT;
    }

    public void setTenTT(String tenTT) {
        this.tenTT = tenTT;
    }

    public String getKieuTT() {
        return kieuTT;
    }

    public void setKieuTT(String kieuTT) {
        this.kieuTT = kieuTT;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaDM() {
        return maDM;
    }

    public void setMaDM(String maDM) {
        this.maDM = maDM;
    }
}