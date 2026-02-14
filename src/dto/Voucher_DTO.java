package dto;

import java.sql.Date;

public class Voucher_DTO extends UuDai_DTO {
    private int loaiVoucher;
    private double giaTriVoucher;
    private double donToiThieu;

    public Voucher_DTO() {
        super();
    }

    public Voucher_DTO(String ma, String ten, Date ngayBatDau, Date ngayKetThuc, int trangThai, int loaiVoucher, double giaTriVoucher, double donToiThieu) {
        super(ma, ten, ngayBatDau, ngayKetThuc, trangThai);
        this.loaiVoucher = loaiVoucher;
        this.giaTriVoucher = giaTriVoucher;
        this.donToiThieu = donToiThieu;
    }

    public int getLoaiVoucher() { return loaiVoucher; }
    public void setLoaiVoucher(int loaiVoucher) { this.loaiVoucher = loaiVoucher; }

    public double getGiaTriVoucher() { return giaTriVoucher; }
    public void setGiaTriVoucher(double giaTriVoucher) { this.giaTriVoucher = giaTriVoucher; }

    public double getDonToiThieu() { return donToiThieu;}
    public void setDonToiThieu(double donToiThieu) { this.donToiThieu = donToiThieu;}
}