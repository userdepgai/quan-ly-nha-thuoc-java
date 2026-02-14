package dto;

import java.sql.Date;

public class Voucher_DTO {
    private String maVoucher;
    private String tenVoucher;
    private String loaiVoucher;
    private double giaTriVoucher;
    private double donToiThieu;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private int trangThai;

    public Voucher_DTO() {
    }

    public Voucher_DTO(String maVoucher, String tenVoucher, String loaiVoucher, double giaTriVoucher, double donToiThieu, Date ngayBatDau, Date ngayKetThuc, int trangThai) {
        this.maVoucher = maVoucher;
        this.tenVoucher = tenVoucher;
        this.loaiVoucher = loaiVoucher;
        this.giaTriVoucher = giaTriVoucher;
        this.donToiThieu = donToiThieu;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
    }

    public String getMaVoucher() {
        return maVoucher;
    }

    public void setMaVoucher(String maVoucher) {
        this.maVoucher = maVoucher;
    }

    public String getTenVoucher() {
        return tenVoucher;
    }

    public void setTenVoucher(String tenVoucher) {
        this.tenVoucher = tenVoucher;
    }

    public String getLoaiVoucher() {
        return loaiVoucher;
    }

    public void setLoaiVoucher(String loaiVoucher) {
        this.loaiVoucher = loaiVoucher;
    }

    public double getGiaTriVoucher() {
        return giaTriVoucher;
    }

    public void setGiaTriVoucher(double giaTriVoucher) {
        this.giaTriVoucher = giaTriVoucher;
    }

    public double getDonToiThieu() {
        return donToiThieu;
    }

    public void setDonToiThieu(double donToiThieu) {
        this.donToiThieu = donToiThieu;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}