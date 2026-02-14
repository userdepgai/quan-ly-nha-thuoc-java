package dto;

public class KhachHang_Voucher_DTO extends GioiHanUuDai_DTO {

    private String maVoucher;

    public KhachHang_Voucher_DTO() {
        super();
    }

    public KhachHang_Voucher_DTO(int soLuotToiDa, int soLuotConLai,
                                 String maKH, String maVoucher) {
        super(soLuotToiDa, soLuotConLai, maKH);
        this.maVoucher = maVoucher;
    }

    public String getMaVoucher() {
        return maVoucher;
    }

    public void setMaVoucher(String maVoucher) {
        this.maVoucher = maVoucher;
    }
}
