package dto;

public class KhachHang_Voucher_DTO {
    // Kỹ thuật ORM: Liên kết giữa Khách hàng và Voucher
    private String maKH;
    private Voucher_DTO voucher;

    public KhachHang_Voucher_DTO() {}

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public Voucher_DTO getVoucher() { return voucher; }
    public void setVoucher(Voucher_DTO voucher) { this.voucher = voucher; }
}