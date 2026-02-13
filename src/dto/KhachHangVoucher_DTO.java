package dto;

public class KhachHangVoucher_DTO {
    private String maKH;
    private String maVoucher;
    private int soLuongConLai;
    private int soLuongToiDa;

    public KhachHangVoucher_DTO() {
    }

    public KhachHangVoucher_DTO(String maKH, String maVoucher, int soLuongConLai, int soLuongToiDa) {
        this.maKH = maKH;
        this.maVoucher = maVoucher;
        this.soLuongConLai = soLuongConLai;
        this.soLuongToiDa = soLuongToiDa;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getMaVoucher() {
        return maVoucher;
    }

    public void setMaVoucher(String maVoucher) {
        this.maVoucher = maVoucher;
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