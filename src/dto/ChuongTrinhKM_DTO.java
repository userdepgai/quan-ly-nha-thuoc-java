package dto;

import java.sql.Date;
import java.time.LocalDate;

public class ChuongTrinhKM_DTO extends UuDai_DTO {
    public static final int TT_NGUNG_AP_DUNG = 0;
    public static final int TT_DANG_AP_DUNG = 1;

    public static final String NGUNG_AP_DUNG = "Ngưng áp dụng";
    public static final String DANG_AP_DUNG = "Đang áp dụng";

    private String moTa;

    public ChuongTrinhKM_DTO() {
        super();
    }

    public ChuongTrinhKM_DTO(String ma, String ten, Date ngayBatDau, Date ngayKetThuc, int trangThai, String moTa) {
        super(ma, ten, ngayBatDau, ngayKetThuc, trangThai);
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getTrangThaiText() {
        if (this.getTrangThai() == TT_NGUNG_AP_DUNG) {
            return NGUNG_AP_DUNG;
        }

        LocalDate today = LocalDate.now();
        LocalDate start = this.getNgayBatDau().toLocalDate();
        LocalDate end = this.getNgayKetThuc().toLocalDate();

        if (today.isBefore(start)) {
            return "Chưa diễn ra";
        }

        if (today.isAfter(end)) {
            return NGUNG_AP_DUNG;
        }

        return DANG_AP_DUNG;
    }

    public boolean setTrangThaiFromText(String text) {
        if (text == null) return false;

        if (text.equals(DANG_AP_DUNG)) {
            LocalDate today = LocalDate.now();
            if (this.getNgayKetThuc() != null) {
                LocalDate end = this.getNgayKetThuc().toLocalDate();
                if (today.isAfter(end)) {
                    return false;
                }
            }
            this.setTrangThai(TT_DANG_AP_DUNG);
            return true;
        }
        else if (text.equals(NGUNG_AP_DUNG)) {
            this.setTrangThai(TT_NGUNG_AP_DUNG);
            return true;
        }

        return false;
    }

    public static int parseTrangThaiFromText(String text) {
        if (text == null) return -1;

        return switch (text) {
            case DANG_AP_DUNG, "Chưa diễn ra" -> TT_DANG_AP_DUNG;
            case NGUNG_AP_DUNG -> TT_NGUNG_AP_DUNG;
            default -> -1;
        };
    }
}