package dto;

public class ThuocTinhDanhMuc_DTO {

    private String maThuocTinh;
    private String tenThuocTinh;
    private int loaiThuocTinh;
    private int trangThai;

    private String maDM;

    public ThuocTinhDanhMuc_DTO() {
    }

    public ThuocTinhDanhMuc_DTO(String maThuocTinh, String tenThuocTinh,
                                int loaiThuocTinh, int trangThai, String maDM) {
        this.maThuocTinh = maThuocTinh;
        this.tenThuocTinh = tenThuocTinh;
        this.loaiThuocTinh = loaiThuocTinh;
        this.trangThai = trangThai;
        this.maDM = maDM;
    }

    public String getMaThuocTinh() {
        return maThuocTinh;
    }

    public void setMaThuocTinh(String maThuocTinh) {
        this.maThuocTinh = maThuocTinh;
    }

    public String getTenThuocTinh() {
        return tenThuocTinh;
    }

    public void setTenThuocTinh(String tenThuocTinh) {
        this.tenThuocTinh = tenThuocTinh;
    }

    public int getLoaiThuocTinh() {
        return loaiThuocTinh;
    }

    public void setLoaiThuocTinh(int loaiThuocTinh) {
        this.loaiThuocTinh = loaiThuocTinh;
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
