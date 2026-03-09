package dto;

import java.sql.Date;
import java.time.LocalDate;

public class Voucher_DTO extends UuDai_DTO {
    public static final int TT_NGUNG_AP_DUNG = 0;
    public static final int TT_DANG_AP_DUNG = 1;
    public static final String NGUNG_AP_DUNG = "Ngưng áp dụng";
    public static final String DANG_AP_DUNG = "Đang áp dụng";

    public static final int LOAI_PHAN_TRAM = 0;
    public static final int LOAI_TIEN_MAT = 1;
    public static final String PHAN_TRAM = "Phần trăm";
    public static final String TIEN_MAT = "Tiền mặt";

    private int loaiVoucher;
    private double giaTriVoucher;
    private double donToiThieu;

    public Voucher_DTO() {
        super();
    }

    public Voucher_DTO(String ma, String ten, Date ngayBatDau, Date ngayKetThuc, int trangThai,
                       int loaiVoucher, double giaTriVoucher, double donToiThieu) {
        super(ma, ten, ngayBatDau, ngayKetThuc, trangThai);
        this.loaiVoucher = loaiVoucher;
        this.giaTriVoucher = giaTriVoucher;
        this.donToiThieu = donToiThieu;
    }

    public int getLoaiVoucher() { return loaiVoucher; }
    public void setLoaiVoucher(int loaiVoucher) { this.loaiVoucher = loaiVoucher; }

    public double getGiaTriVoucher() { return giaTriVoucher; }
    public void setGiaTriVoucher(double giaTriVoucher) { this.giaTriVoucher = giaTriVoucher; }

    public double getDonToiThieu() { return donToiThieu; }
    public void setDonToiThieu(double donToiThieu) { this.donToiThieu = donToiThieu; }

    public String getTrangThaiText() {
        if (this.getTrangThai() == TT_NGUNG_AP_DUNG) return NGUNG_AP_DUNG;

        LocalDate today = LocalDate.now();
        if (this.getNgayBatDau() == null || this.getNgayKetThuc() == null) return NGUNG_AP_DUNG;

        LocalDate start = this.getNgayBatDau().toLocalDate();
        LocalDate end = this.getNgayKetThuc().toLocalDate();

        if (today.isBefore(start)) return "Chưa diễn ra";
        if (today.isAfter(end)) return NGUNG_AP_DUNG;

        return DANG_AP_DUNG;
    }

    public boolean setTrangThaiFromText(String text) {
        if (text == null) return false;
        if (text.equals(DANG_AP_DUNG)) {
            if (this.getNgayKetThuc() != null) {
                if (LocalDate.now().isAfter(this.getNgayKetThuc().toLocalDate())) return false;
            }
            this.setTrangThai(TT_DANG_AP_DUNG);
            return true;
        } else {
            this.setTrangThai(TT_NGUNG_AP_DUNG);
            return true;
        }
    }

    public String getLoaiVoucherText() {
        return (loaiVoucher == LOAI_PHAN_TRAM) ? PHAN_TRAM : TIEN_MAT;
    }
}