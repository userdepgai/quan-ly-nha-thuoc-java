package dto;

public class KhachHang_KM_DTO {
    private String maKH;
    private KhuyenMai_DTO khuyenMai;

    public KhachHang_KM_DTO() {}

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public KhuyenMai_DTO getKhuyenMai() { return khuyenMai; }
    public void setKhuyenMai(KhuyenMai_DTO khuyenMai) { this.khuyenMai = khuyenMai; }
}