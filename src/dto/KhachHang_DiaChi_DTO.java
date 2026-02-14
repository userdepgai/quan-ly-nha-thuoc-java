package dto;

public class KhachHang_DiaChi_DTO {

    private KhachHang_DTO khachHang;
    private DIACHI_DTO diaChi;
    private int trangThai;

    public KhachHang_DiaChi_DTO() {
    }

    public KhachHang_DiaChi_DTO(KhachHang_DTO khachHang,
                                DIACHI_DTO diaChi,
                                int trangThai) {
        this.khachHang = khachHang;
        this.diaChi = diaChi;
        this.trangThai = trangThai;
    }

    public KhachHang_DTO getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang_DTO khachHang) {
        this.khachHang = khachHang;
    }

    public DIACHI_DTO getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(DIACHI_DTO diaChi) {
        this.diaChi = diaChi;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}
