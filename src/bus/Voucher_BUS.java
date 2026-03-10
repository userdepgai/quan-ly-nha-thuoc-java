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

    public boolean them(Voucher_DTO v, int soLuot) {
        if (!validate(v)) return false;

        if (soLuot <= 0) {
            JOptionPane.showMessageDialog(null, "Số lượt sử dụng tối đa phải lớn hơn 0!");
            return false;
        }

        if (dao.them(v)) {
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
        if (dao.capNhat(v)) {
            refreshData();
            return true;
        }
        return false;
    }

    public ArrayList<Voucher_DTO> timKiemNangCao(String keyword, String timTheo, String locLoai, String locTrangThai, Date filterBD, Date filterKT) {
        ArrayList<Voucher_DTO> result = new ArrayList<>();
        String key = keyword.toLowerCase().trim();

        for (Voucher_DTO v : getAll()) {
            boolean matchKey = false;
            if (timTheo.equals("Tất cả")) {
                matchKey = v.getMa().toLowerCase().contains(key) || v.getTen().toLowerCase().contains(key);
            } else if (timTheo.equals("Mã Voucher")) {
                matchKey = v.getMa().toLowerCase().contains(key);
            } else {
                matchKey = v.getTen().toLowerCase().contains(key);
            }

            boolean matchLoai = locLoai.equals("Tất cả") || v.getLoaiVoucherText().equals(locLoai);

            boolean matchStatus = locTrangThai.equals("Tất cả") || v.getTrangThaiText().equals(locTrangThai);

            boolean matchDate = true;
            if (filterBD != null && filterKT != null) {
                if (v.getNgayBatDau().after(filterKT) || v.getNgayKetThuc().before(filterBD)) {
                    matchDate = false;
                }
            } else if (filterBD != null) {
                if (v.getNgayKetThuc().before(filterBD)) matchDate = false;
            } else if (filterKT != null) {
                if (v.getNgayBatDau().after(filterKT)) matchDate = false;
            }

            if (matchKey && matchLoai && matchStatus && matchDate) {
                result.add(v);
            }
        }
        return result;
    }

    public boolean kiemTraLogicLocNgay(java.util.Date bd, java.util.Date kt, boolean isStartDateChanged) {
        if (bd != null && kt != null) {
            if (bd.after(kt)) {
                if (isStartDateChanged) {
                    JOptionPane.showMessageDialog(null, "Ngày bắt đầu không được sau ngày kết thúc!", "Lỗi chọn ngày", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Ngày kết thúc không được trước ngày bắt đầu!", "Lỗi chọn ngày", JOptionPane.WARNING_MESSAGE);
                }
                return false;
            }
        }
        return true;
    }

    private boolean validate(Voucher_DTO v) {
        if (v.getTen() == null || v.getTen().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên voucher không được để trống!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (v.getGiaTriVoucher() <= 0) {
            JOptionPane.showMessageDialog(null, "Giá trị voucher phải lớn hơn 0!", "Lỗi logic", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (v.getLoaiVoucher() == Voucher_DTO.LOAI_PHAN_TRAM && v.getGiaTriVoucher() > 1.0) {
            JOptionPane.showMessageDialog(null, "Voucher phần trăm không được vượt quá 100% (Giá trị phải <= 1.0)!", "Lỗi logic", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (v.getNgayBatDau() != null && v.getNgayKetThuc() != null) {
            if (v.getNgayBatDau().after(v.getNgayKetThuc())) {
                JOptionPane.showMessageDialog(null, "Lỗi: Ngày kết thúc không được trước ngày bắt đầu!", "Lỗi thời gian", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn đầy đủ ngày bắt đầu và ngày kết thúc!", "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    public ArrayList<Voucher_DTO> getDSVoucherHopLe(double thanhTien) {
        ArrayList<Voucher_DTO> result = new ArrayList<>();

        for (Voucher_DTO v : getAll()) {
            if (!v.getTrangThaiText().equals(Voucher_DTO.DANG_AP_DUNG)) {
                continue;
            }

            if (thanhTien >= v.getDonToiThieu()) {
                result.add(v);
            }
        }
        return result;
    }

    public ArrayList<Voucher_DTO> getDSVoucherSapXepTotNhat(double thanhTien) {
        ArrayList<Voucher_DTO> list = getDSVoucherHopLe(thanhTien);

        list.sort((v1, v2) -> {
            double giam1 = tinhTienGiamVoucher(v1, thanhTien);
            double giam2 = tinhTienGiamVoucher(v2, thanhTien);
            return Double.compare(giam2, giam1);
        });

        return list;
    }

    public Voucher_DTO getByTenVoucher(String ten) {
        if (ten == null || ten.trim().isEmpty()) return null;
        for (Voucher_DTO v : getAll()) {
            if (v.getTen().equalsIgnoreCase(ten.trim())) {
                return v;
            }
        }
        return null;
    }

    public double tinhTienGiamVoucher(Voucher_DTO v, double thanhTien) {
        if (v.getLoaiVoucher() == Voucher_DTO.LOAI_PHAN_TRAM) {
            return thanhTien * v.getGiaTriVoucher();
        } else {
            return v.getGiaTriVoucher();
        }
    }
}