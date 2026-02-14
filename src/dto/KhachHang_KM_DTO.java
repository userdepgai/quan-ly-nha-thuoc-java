package dto;

public class KhachHang_KM_DTO {
    private String maKH;
    private KhuyenMai_DTO khuyenMai;
    private int soLuongToiDa;      // Số lần tối đa được hưởng khuyến mãi này
    private int soLuotConLai;      // Số lần còn lại được hưởng

    public KhachHang_KM_DTO() {}

    public KhachHang_KM_DTO(String maKH, KhuyenMai_DTO khuyenMai, int soLuongToiDa, int soLuotConLai) {
        this.maKH = maKH;
        this.khuyenMai = khuyenMai;
        this.soLuongToiDa = soLuongToiDa;
        this.soLuotConLai = soLuotConLai;
    }

    // Getters and Setters
    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public KhuyenMai_DTO getKhuyenMai() { return khuyenMai; }
    public void setKhuyenMai(KhuyenMai_DTO khuyenMai) { this.khuyenMai = khuyenMai; }

    public int getSoLuongToiDa() { return soLuongToiDa; }
    public void setSoLuongToiDa(int soLuongToiDa) { this.soLuongToiDa = soLuongToiDa; }

    public int getSoLuotConLai() { return soLuotConLai; }
    public void setSoLuotConLai(int soLuotConLai) { this.soLuotConLai = soLuotConLai; }
}