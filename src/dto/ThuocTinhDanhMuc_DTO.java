package dto;

public class ThuocTinhDanhMuc_DTO {
    private String maThuocTinh;
    private String tenThuocTinh;
    private int loaiThuocTinh;
    private int trangThai;

    private DanhMucSanPham_DTO danhMuc;

    public ThuocTinhDanhMuc_DTO() {}

    public String getMaThuocTinh() { return maThuocTinh; }
    public void setMaThuocTinh(String maThuocTinh) { this.maThuocTinh = maThuocTinh; }

    public String getTenThuocTinh() { return tenThuocTinh; }
    public void setTenThuocTinh(String tenThuocTinh) { this.tenThuocTinh = tenThuocTinh; }

    public int getLoaiThuocTinh() { return loaiThuocTinh; }
    public void setLoaiThuocTinh(int loaiThuocTinh) { this.loaiThuocTinh = loaiThuocTinh; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    public DanhMucSanPham_DTO getDanhMuc() { return danhMuc; }
    public void setDanhMuc(DanhMucSanPham_DTO danhMuc) { this.danhMuc = danhMuc; }
}