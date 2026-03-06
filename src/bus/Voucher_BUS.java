package bus;

import dao.Voucher_DAO;
import dao.KhachHang_Voucher_DAO; // Bạn cần tạo lớp DAO này tương tự KhachHang_KM_DAO
import dto.Voucher_DTO;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Date;

public class Voucher_BUS {
    private static Voucher_BUS instance;
    private final Voucher_DAO dao = new Voucher_DAO();
    private ArrayList<Voucher_DTO> listCache;

    private Voucher_BUS() { refreshData(); }

    public static Voucher_BUS getInstance() {
        if (instance == null) instance = new Voucher_BUS();
        return instance;
    }

    public void refreshData() { listCache = dao.getAll(); }

    public ArrayList<Voucher_DTO> getAll() {
        if (listCache == null) refreshData();
        return listCache;
    }

    public String getNextId() { return dao.getNextId(); }

    public Voucher_DTO getById(String ma) {
        if (ma == null || ma.trim().isEmpty()) return null;
        for (Voucher_DTO v : getAll()) {
            if (v.getMa().equalsIgnoreCase(ma)) return v;
        }
        return null;
    }

    /**
     * LOGIC THÊM VÀ PHÂN PHỐI VOUCHER
     */
    public boolean them(Voucher_DTO v, int soLuot) {
        if (!validate(v)) return false;
        if (soLuot <= 0) {
            JOptionPane.showMessageDialog(null, "Số lượt sử dụng phải lớn hơn 0!");
            return false;
        }

        // 1. Thêm vào bảng VOUCHER
        if (dao.them(v)) {
            // 2. Phân phối cho tất cả khách hàng (Sử dụng SQL INSERT SELECT để tối ưu)
            boolean distributed = KhachHang_Voucher_DAO.getInstance().phanPhoiVoucherToanHeThong(v.getMa(), soLuot);
            if (distributed) {
                refreshData();
                return true;
            }
        }
        return false;
    }

    public boolean capNhat(Voucher_DTO v) {
        if (!validate(v)) return false;
        if (dao.capNhat(v)) { refreshData(); return true; }
        return false;
    }

    /**
     * HÀM LỌC TỔNG HỢP (Chuyển từ GUI xuống)
     */
    public ArrayList<Voucher_DTO> timKiemNangCao(String keyword, String timTheo, String locTrangThai) {
        ArrayList<Voucher_DTO> result = new ArrayList<>();
        String key = keyword.toLowerCase().trim();

        for (Voucher_DTO v : getAll()) {
            // 1. Khớp từ khóa
            boolean matchKey = false;
            if (timTheo.equals("Tất cả")) {
                matchKey = v.getMa().toLowerCase().contains(key) || v.getTen().toLowerCase().contains(key);
            } else if (timTheo.equals("Mã Voucher")) {
                matchKey = v.getMa().toLowerCase().contains(key);
            } else {
                matchKey = v.getTen().toLowerCase().contains(key);
            }

            // 2. Khớp trạng thái (Lấy text logic từ DTO)
            boolean matchStatus = locTrangThai.equals("Tất cả") || v.getTrangThaiText().equals(locTrangThai);

            if (matchKey && matchStatus) result.add(key.isEmpty() && locTrangThai.equals("Tất cả") ? v : v);

            if (matchKey && matchStatus) {
                result.add(v);
            }
        }
        return result;
    }

    private boolean validate(Voucher_DTO v) {
        if (v.getTen().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên voucher không được để trống!");
            return false;
        }
        if (v.getLoaiVoucher() == Voucher_DTO.LOAI_PHAN_TRAM && v.getGiaTriVoucher() > 1.0) {
            JOptionPane.showMessageDialog(null, "Voucher phần trăm không được quá 100% (1.0)!");
            return false;
        }
        if (v.getGiaTriVoucher() <= 0) {
            JOptionPane.showMessageDialog(null, "Giá trị voucher phải lớn hơn 0!");
            return false;
        }
        if (v.getNgayBatDau().after(v.getNgayKetThuc())) {
            JOptionPane.showMessageDialog(null, "Ngày bắt đầu phải trước ngày kết thúc!");
            return false;
        }
        return true;
    }
    // ========================================================================
    // CÁC HÀM PHỤC VỤ NGHIỆP VỤ HÓA ĐƠN (GỌI TỪ GUI BÁN HÀNG)
    // ========================================================================

    /**
     * 1. Lấy danh sách Voucher hợp lệ dựa trên tổng tiền hóa đơn
     * Sử dụng logic trạng thái thông minh từ DTO (bao gồm cả kiểm tra ngày)
     */
    public ArrayList<Voucher_DTO> getDSVoucherHopLe(double thanhTien) {
        ArrayList<Voucher_DTO> result = new ArrayList<>();

        for (Voucher_DTO v : getAll()) {
            // Bước 1: Kiểm tra trạng thái thông minh
            // getTrangThaiText() trả về "Đang áp dụng" chỉ khi (TrangThai=1 AND BD <= Hôm nay <= KT)
            if (!v.getTrangThaiText().equals(Voucher_DTO.DANG_AP_DUNG)) {
                continue;
            }

            // Bước 2: Kiểm tra điều kiện đơn hàng tối thiểu
            if (thanhTien >= v.getDonToiThieu()) {
                result.add(v);
            }
        }
        return result;
    }

    /**
     * 2. Lấy danh sách Voucher sắp xếp cái nào giảm nhiều nhất lên đầu
     * Giúp hệ thống tự động gợi ý Voucher hời nhất cho khách hàng
     */
    public ArrayList<Voucher_DTO> getDSVoucherSapXepTotNhat(double thanhTien) {
        ArrayList<Voucher_DTO> list = getDSVoucherHopLe(thanhTien);

        // Sắp xếp giảm dần theo số tiền thực tế được giảm
        list.sort((v1, v2) -> {
            double giam1 = tinhTienGiamVoucher(v1, thanhTien);
            double giam2 = tinhTienGiamVoucher(v2, thanhTien);
            return Double.compare(giam2, giam1);
        });

        return list;
    }

    /**
     * 3. Tìm Voucher theo tên (Dùng khi nhân viên nhập tay hoặc chọn từ ComboBox)
     */
    public Voucher_DTO getByTenVoucher(String ten) {
        if (ten == null || ten.trim().isEmpty()) return null;
        for (Voucher_DTO v : getAll()) {
            // Lưu ý: v.getTen() là thuộc tính lấy từ lớp cha UuDai_DTO
            if (v.getTen().equalsIgnoreCase(ten.trim())) {
                return v;
            }
        }
        return null;
    }

    /**
     * 4. Hàm tính toán số tiền được giảm của 1 Voucher (Helper)
     * Sử dụng hằng số INT từ DTO thay vì so sánh chuỗi
     */
    public double tinhTienGiamVoucher(Voucher_DTO v, double thanhTien) {
        if (v.getLoaiVoucher() == Voucher_DTO.LOAI_PHAN_TRAM) {
            // Ví dụ: 1,000,000 * 0.05 = 50,000
            return thanhTien * v.getGiaTriVoucher();
        } else {
            // Giảm thẳng tiền mặt (Ví dụ: 20,000)
            return v.getGiaTriVoucher();
        }
    }
}