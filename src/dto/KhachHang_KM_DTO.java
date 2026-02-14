package dto;

public class KhachHang_KM_DTO extends GioiHanUuDai_DTO {
    // Kỹ thuật ORM: Tham chiếu đến đối tượng KhuyenMai
    private KhuyenMai_DTO khuyenMai;

    public KhachHang_KM_DTO() {
        super();
    }

    public KhachHang_KM_DTO(int soLuotToiDa, int soLuotConLai, KhachHang_DTO khachHang, KhuyenMai_DTO khuyenMai) {
        super(soLuotToiDa, soLuotConLai, khachHang);
        this.khuyenMai = khuyenMai;
    }

    public KhuyenMai_DTO getKhuyenMai() { return khuyenMai; }
    public void setKhuyenMai(KhuyenMai_DTO khuyenMai) { this.khuyenMai = khuyenMai; }
}