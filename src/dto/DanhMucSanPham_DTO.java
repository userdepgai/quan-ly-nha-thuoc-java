package dto;

public class DanhMucSanPham_DTO {
    private String maDM;
    private String tenDM;
    private int trangThai;

    public DanhMucSanPham_DTO() {}

    public DanhMucSanPham_DTO(String maDM, String tenDM, int trangThai) {
        this.maDM = maDM;
        this.tenDM = tenDM;
        this.trangThai = trangThai;
    }

    public String getMaDM() { return maDM; }
    public void setMaDM(String maDM) { this.maDM = maDM; }

    public String getTenDM() { return tenDM; }
    public void setTenDM(String tenDM) { this.tenDM = tenDM; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }
}
