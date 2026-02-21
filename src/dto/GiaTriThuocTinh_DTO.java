package dto;

public class GiaTriThuocTinh_DTO {
    private String maGiaTri;
    private String ndGiaTri; // Nội dung giá trị
    private int trangThai;

    // ORM: Liên kết với Thuộc tính danh mục
    private ThuocTinhDanhMuc_DTO thuocTinh;

    public GiaTriThuocTinh_DTO() {}

    public String getMaGiaTri() { return maGiaTri; }
    public void setMaGiaTri(String maGiaTri) { this.maGiaTri = maGiaTri; }

    public String getNdGiaTri() { return ndGiaTri; }
    public void setNdGiaTri(String ndGiaTri) { this.ndGiaTri = ndGiaTri; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    public ThuocTinhDanhMuc_DTO getThuocTinh() { return thuocTinh; }
    public void setThuocTinh(ThuocTinhDanhMuc_DTO thuocTinh) { this.thuocTinh = thuocTinh; }
}