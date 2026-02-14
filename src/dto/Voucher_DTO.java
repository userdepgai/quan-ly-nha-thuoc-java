package dto;

import java.sql.Date;

public class Voucher_DTO extends UuDai_DTO {
    private int loaiVoucher;
    private double giaTriVoucher;

    public Voucher_DTO() {
        super();
    }

    public Voucher_DTO(String ma, String ten, Date ngayBatDau, Date ngayKetThuc, int trangThai, int loaiVoucher, double giaTriVoucher) {
        super(ma, ten, ngayBatDau, ngayKetThuc, trangThai);
        this.loaiVoucher = loaiVoucher;
        this.giaTriVoucher = giaTriVoucher;
    }

    public int getLoaiVoucher() { return loaiVoucher; }
    public void setLoaiVoucher(int loaiVoucher) { this.loaiVoucher = loaiVoucher; }

    public double getGiaTriVoucher() { return giaTriVoucher; }
    public void setGiaTriVoucher(double giaTriVoucher) { this.giaTriVoucher = giaTriVoucher; }
}