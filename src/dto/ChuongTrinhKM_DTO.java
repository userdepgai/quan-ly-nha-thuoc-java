package dto;

import java.sql.Date;
import java.time.LocalDate;

public class ChuongTrinhKM_DTO extends UuDai_DTO {

    // --- HẰNG SỐ TRẠNG THÁI ---
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

    /**
     * LOGIC HIỂN THỊ TRẠNG THÁI (QUAN TRỌNG)
     * Ưu tiên 1: Nếu DB lưu là 0 (Admin chặn) -> Luôn là Ngưng áp dụng.
     * Ưu tiên 2: Nếu DB lưu là 1 -> Kiểm tra ngày hiện tại xem còn hạn không.
     */
    public String getTrangThaiText() {
        // 1. Kiểm tra trạng thái cứng từ DB (Admin tắt thủ công)
        if (this.getTrangThai() == TT_NGUNG_AP_DUNG) {
            return NGUNG_AP_DUNG;
        }

        // 2. Nếu Admin bật (1), kiểm tra tiếp hạn sử dụng thực tế
        LocalDate today = LocalDate.now();
        LocalDate start = this.getNgayBatDau().toLocalDate();
        LocalDate end = this.getNgayKetThuc().toLocalDate();

        if (today.isBefore(start)) {
            return "Chưa diễn ra";
        }

        // Nếu đã qua ngày kết thúc -> Ngưng áp dụng
        if (today.isAfter(end)) {
            return NGUNG_AP_DUNG;
        }

        // Còn lại -> Đang áp dụng
        return DANG_AP_DUNG;
    }

    /**
     * HÀM SET TRẠNG THÁI TỪ GUI (Bổ sung theo yêu cầu của bạn)
     * Dùng khi Admin chọn ComboBox và nhấn Lưu
     */
    /**
     * HÀM SET TRẠNG THÁI TỪ GUI (Có kiểm tra điều kiện ngày tháng)
     * @return true nếu set thành công, false nếu không được phép (do hết hạn)
     */
    public boolean setTrangThaiFromText(String text) {
        if (text == null) return false;

        if (text.equals(DANG_AP_DUNG)) {
            // KIỂM TRA ĐIỀU KIỆN: Chỉ cho phép bật (set = 1) nếu chưa hết hạn
            LocalDate today = LocalDate.now();
            if (this.getNgayKetThuc() != null) {
                LocalDate end = this.getNgayKetThuc().toLocalDate();
                if (today.isAfter(end)) {
                    // Đã hết hạn thì không cho phép set thành "Đang áp dụng"
                    return false;
                }
            }
            this.setTrangThai(TT_DANG_AP_DUNG);
            return true;
        }
        else if (text.equals(NGUNG_AP_DUNG)) {
            // Luôn cho phép dừng chương trình (set = 0)
            this.setTrangThai(TT_NGUNG_AP_DUNG);
            return true;
        }

        return false;
    }

    /**
     * HÀM TĨNH PARSE TRẠNG THÁI (Dùng cho BUS/DAO)
     */
    public static int parseTrangThaiFromText(String text) {
        if (text == null) return -1;
        // Lưu ý: GUI chỉ gửi về yêu cầu "Kích hoạt" (1) hoặc "Hủy/Ngưng" (0)
        // Việc hết hạn hay chưa là do ngày tháng quyết định logic hiển thị
        return switch (text) {
            case DANG_AP_DUNG, "Chưa diễn ra" -> TT_DANG_AP_DUNG;
            case NGUNG_AP_DUNG -> TT_NGUNG_AP_DUNG;
            default -> -1;
        };
    }
}