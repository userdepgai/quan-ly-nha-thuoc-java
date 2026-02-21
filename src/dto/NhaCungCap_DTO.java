package dto;

public class NhaCungCap_DTO {
    private String maNCC;
    private String tenNCC;
    private String nguoiLienHe;
    private String sdt;
    private String maSoThue;
    private int trangThai;
    private String maDC;


    public NhaCungCap_DTO(String maNCC, String tenNCC, String nguoiLienHe, String sdt, String maSoThue, int trangThai, String maDC) {
        this.maNCC = maNCC;
        this.tenNCC = tenNCC;
        this.nguoiLienHe = nguoiLienHe;
        this.sdt = sdt;
        this.maSoThue = maSoThue;
        this.trangThai = trangThai;
        this.maDC = maDC;
    }

    public String getMaNCC() {
        return maNCC;
    }
    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public String getTenNCC() {
        return tenNCC;
    }
    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }

    public String getNguoiLienHe() {
        return nguoiLienHe;
    }
    public void setNguoiLienHe(String nguoiLienHe) {
        this.nguoiLienHe = nguoiLienHe;
    }

    public String getSdt() {
        return sdt;
    }
    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getMaSoThue() {
        return maSoThue;
    }
    public void setMaSoThue(String maSoThue) {
        this.maSoThue = maSoThue;
    }

    public int getTrangThai() {
        return trangThai;
    }
    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
    public String getMaDC() {
        return maDC;
    }
    public void setMaDC(String maDC) {
        this.maDC = maDC;
    }
}