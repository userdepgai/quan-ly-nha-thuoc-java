package dto;

public class KhuyenMaiKhachHang_DTO {
    private String maKM;
    private String maKH;
    private int soLuongConLai;
    private int soLuongToiDa;

    public KhuyenMaiKhachHang_DTO() {
    }

    public KhuyenMaiKhachHang_DTO(String maKM, String maKH, int soLuongConLai, int soLuongToiDa) {
        this.maKM = maKM;
        this.maKH = maKH;
        this.soLuongConLai = soLuongConLai;
        this.soLuongToiDa = soLuongToiDa;
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public int getSoLuongConLai() {
        return soLuongConLai;
    }

    public void setSoLuongConLai(int soLuongConLai) {
        this.soLuongConLai = soLuongConLai;
    }

    public int getSoLuongToiDa() {
        return soLuongToiDa;
    }

    public void setSoLuongToiDa(int soLuongToiDa) {
        this.soLuongToiDa = soLuongToiDa;
    }
}