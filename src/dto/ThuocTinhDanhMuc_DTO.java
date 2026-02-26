package dto;

public class ThuocTinhDanhMuc_DTO {
    private String maThuocTinh;
    private String tenThuocTinh;
    private int kieuThuocTinh;
    private int trangThai;
    private String maDM;

    public ThuocTinhDanhMuc_DTO() {
    }

    public ThuocTinhDanhMuc_DTO(String maThuocTinh, String tenThuocTinh,
                                int kieuThuocTinh, int trangThai, String maDM) {
        this.maThuocTinh = maThuocTinh;
        this.tenThuocTinh = tenThuocTinh;
        this.kieuThuocTinh = kieuThuocTinh;
        this.trangThai = trangThai;
        this.maDM = maDM;
    }

    // Nhớ Generate lại các hàm Getter/Setter cho kieuThuocTinh nhé!
    public String getMaThuocTinh() { return maThuocTinh; }
    public void setMaThuocTinh(String maThuocTinh) { this.maThuocTinh = maThuocTinh; }

    public String getTenThuocTinh() { return tenThuocTinh; }
    public void setTenThuocTinh(String tenThuocTinh) { this.tenThuocTinh = tenThuocTinh; }

    public int getKieuThuocTinh() { return kieuThuocTinh; }
    public void setKieuThuocTinh(int kieuThuocTinh) { this.kieuThuocTinh = kieuThuocTinh; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    public String getMaDM() { return maDM; }
    public void setMaDM(String maDM) { this.maDM = maDM; }
}