package bus;

import dao.Voucher_DAO;
import dto.Voucher_DTO;
import javax.swing.JOptionPane;
import java.util.ArrayList;

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

    public ArrayList<Voucher_DTO> getAll() { return listCache; }

    public String getNextId() { return dao.getNextId(); }

    public Voucher_DTO getById(String ma) {
        if (ma == null || ma.trim().isEmpty()) return null;

        // 1. Tìm trong danh sách Cache
        if (listCache == null) refreshData();
        for (Voucher_DTO v : listCache) {
            if (v.getMa().equalsIgnoreCase(ma)) {
                return v;
            }
        }

        return null;
    }

    public boolean them(Voucher_DTO v) {
        if (!validate(v)) return false;
        if (dao.them(v)) { refreshData(); return true; }
        return false;
    }

    public boolean capNhat(Voucher_DTO v) {
        if (!validate(v)) return false;
        if (dao.capNhat(v)) { refreshData(); return true; }
        return false;
    }

    private boolean validate(Voucher_DTO v) {
        if (v.getTen().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên voucher không được để trống!");
            return false;
        }
        if (v.getLoaiVoucher() == 0 && v.getGiaTriVoucher() > 1.0) {
            JOptionPane.showMessageDialog(null, "Voucher phần trăm không được quá 100% (1.0)!");
            return false;
        }
        if (v.getNgayBatDau().after(v.getNgayKetThuc())) {
            JOptionPane.showMessageDialog(null, "Ngày bắt đầu phải trước ngày kết thúc!");
            return false;
        }
        return true;
    }
}