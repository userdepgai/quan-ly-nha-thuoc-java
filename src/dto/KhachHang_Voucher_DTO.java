package dto;

public class KhachHang_Voucher_DTO {
    private String maKH;
    private Voucher_DTO voucher;
    private int soLuongToiDa;   // Tổng số lần khách hàng được dùng voucher này
    private int soLuotConLai;   // Số lần còn lại có thể sử dụng

    public KhachHang_Voucher_DTO() {}

    public KhachHang_Voucher_DTO(String maKH, Voucher_DTO voucher, int soLuongToiDa, int soLuotConLai) {
        this.maKH = maKH;
        this.voucher = voucher;
        this.soLuongToiDa = soLuongToiDa;
        this.soLuotConLai = soLuotConLai;
    }

    // Getters and Setters
    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public Voucher_DTO getVoucher() { return voucher; }
    public void setVoucher(Voucher_DTO voucher) { this.voucher = voucher; }

    public int getSoLuongToiDa() { return soLuongToiDa; }
    public void setSoLuongToiDa(int soLuongToiDa) { this.soLuongToiDa = soLuongToiDa; }

    public int getSoLuotConLai() { return soLuotConLai; }
    public void setSoLuotConLai(int soLuotConLai) { this.soLuotConLai = soLuotConLai; }
}