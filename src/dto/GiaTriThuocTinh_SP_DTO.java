package dto;

public class GiaTriThuocTinh_SP_DTO {
    // Kỹ thuật ORM: Kết hợp 3 đối tượng để tạo thành một định nghĩa đầy đủ
    private SanPham_DTO sanPham;
    private ThuocTinhDanhMuc_DTO thuocTinh;
    private GiaTriThuocTinh_DTO giaTri;

    public GiaTriThuocTinh_SP_DTO() {}

    public GiaTriThuocTinh_SP_DTO(SanPham_DTO sanPham, ThuocTinhDanhMuc_DTO thuocTinh, GiaTriThuocTinh_DTO giaTri) {
        this.sanPham = sanPham;
        this.thuocTinh = thuocTinh;
        this.giaTri = giaTri;
    }

    public SanPham_DTO getSanPham() { return sanPham; }
    public void setSanPham(SanPham_DTO sanPham) { this.sanPham = sanPham; }

    public ThuocTinhDanhMuc_DTO getThuocTinh() { return thuocTinh; }
    public void setThuocTinh(ThuocTinhDanhMuc_DTO thuocTinh) { this.thuocTinh = thuocTinh; }

    public GiaTriThuocTinh_DTO getGiaTri() { return giaTri; }
    public void setGiaTri(GiaTriThuocTinh_DTO giaTri) { this.giaTri = giaTri; }
}