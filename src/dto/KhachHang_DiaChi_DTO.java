package dto;

public class KhachHang_DiaChi_DTO {

    private String maKhachHang;
    private String maDiaChi;
    private int trangThai;

    public KhachHang_DiaChi_DTO() {
    }

    public KhachHang_DiaChi_DTO(String maKhachHang,
                                String maDiaChi,
                                int trangThai) {
        this.maKhachHang = maKhachHang;
        this.maDiaChi = maDiaChi;
        this.trangThai = trangThai;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getMaDiaChi() {
        return maDiaChi;
    }

    public void setMaDiaChi(String maDiaChi) {
        this.maDiaChi = maDiaChi;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}
