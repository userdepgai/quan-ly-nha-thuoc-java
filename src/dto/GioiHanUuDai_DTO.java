package dto;

public class GioiHanUuDai_DTO {
    protected int soLuotToiDa;
    protected int soLuotConLai;
    protected KhachHang_DTO khachHang; // Kỹ thuật ORM tham chiếu đối tượng

    public GioiHanUuDai_DTO() {}

    public GioiHanUuDai_DTO(int soLuotToiDa, int soLuotConLai, KhachHang_DTO khachHang) {
        this.soLuotToiDa = soLuotToiDa;
        this.soLuotConLai = soLuotConLai;
        this.khachHang = khachHang;
    }

    // Getters and Setters
    public int getSoLuotToiDa() { return soLuotToiDa; }
    public void setSoLuotToiDa(int soLuotToiDa) { this.soLuotToiDa = soLuotToiDa; }

    public int getSoLuotConLai() { return soLuotConLai; }
    public void setSoLuotConLai(int soLuotConLai) { this.soLuotConLai = soLuotConLai; }

    public KhachHang_DTO getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang_DTO khachHang) { this.khachHang = khachHang; }
}
