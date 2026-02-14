package dto;

public class KhachHang_Voucher_DTO extends GioiHanUuDai_DTO {
    // Kỹ thuật ORM: Tham chiếu đến đối tượng Voucher
    private Voucher_DTO voucher;

    public KhachHang_Voucher_DTO() {
        super();
    }

    public KhachHang_Voucher_DTO(int soLuotToiDa, int soLuotConLai, KhachHang_DTO khachHang, Voucher_DTO voucher) {
        super(soLuotToiDa, soLuotConLai, khachHang);
        this.voucher = voucher;
    }

    public Voucher_DTO getVoucher() { return voucher; }
    public void setVoucher(Voucher_DTO voucher) { this.voucher = voucher; }
}