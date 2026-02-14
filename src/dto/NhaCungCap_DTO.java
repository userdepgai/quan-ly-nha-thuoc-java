package dto;

public class NhaCungCap_DTO {
    private String maNCC;
    private String tenNCC;
    private String nguoiLienHe;
    private String sdt;
    private String maSoThue;
    private int trangThai;
    private DIACHI_DTO diaChi;


    public NhaCungCap_DTO() {
    }


    public NhaCungCap_DTO(String maNCC, String tenNCC, String nguoiLienHe, String sdt, String maSoThue, int trangThai, DIACHI_DTO diaChi) {
        this.maNCC = maNCC;
        this.tenNCC = tenNCC;
        this.nguoiLienHe = nguoiLienHe;
        this.sdt = sdt;
        this.maSoThue = maSoThue;
        this.trangThai = trangThai;
        this.diaChi = diaChi;
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


    public DIACHI_DTO getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(DIACHI_DTO diaChi) {
        this.diaChi = diaChi;
    }
}