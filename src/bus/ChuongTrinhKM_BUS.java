package bus;

import dao.ChuongTrinhKM_DAO;
import dto.ChuongTrinhKM_DTO;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.sql.Date;

public class ChuongTrinhKM_BUS {
    private static ChuongTrinhKM_BUS instance;
    private final ChuongTrinhKM_DAO dao = new ChuongTrinhKM_DAO();
    private ArrayList<ChuongTrinhKM_DTO> listCache;

    private ChuongTrinhKM_BUS() {
        refreshData();
    }

    public static ChuongTrinhKM_BUS getInstance() {
        if (instance == null) {
            instance = new ChuongTrinhKM_BUS();
        }
        return instance;
    }

    public ArrayList<ChuongTrinhKM_DTO> getAll() {
        if (listCache == null) refreshData();
        return listCache;
    }

    public void refreshData() {
        listCache = dao.getAll();
    }

    public String getNextId() {
        return dao.getNextId();
    }

    // Lấy các CTKM đang có hiệu lực (Trạng thái = 1 và Trong khoảng thời gian)
    public ArrayList<ChuongTrinhKM_DTO> getDangDienRa() {
        ArrayList<ChuongTrinhKM_DTO> result = new ArrayList<>();
        long millis = System.currentTimeMillis();
        Date today = new Date(millis);

        for (ChuongTrinhKM_DTO ct : listCache) {
            if (ct.getTrangThai() == 1 &&
                    !ct.getNgayBatDau().after(today) && // Bắt đầu <= Hôm nay
                    !ct.getNgayKetThuc().before(today)) { // Kết thúc >= Hôm nay
                result.add(ct);
            }
        }
        return result;
    }

    // Thêm có kiểm tra hợp lệ
    public boolean them(ChuongTrinhKM_DTO ctkm) {
        if (!kiemTraHopLe(ctkm)) return false;

        boolean result = dao.them(ctkm);
        if (result) refreshData();
        return result;
    }

    // Cập nhật có kiểm tra hợp lệ
    public boolean capNhat(ChuongTrinhKM_DTO ctkm) {
        if (!kiemTraHopLe(ctkm)) return false;

        boolean result = dao.capNhat(ctkm);
        if (result) refreshData();
        return result;
    }

    // Tìm kiếm (Mã hoặc Tên)
    public ArrayList<ChuongTrinhKM_DTO> timKiem(String keyword) {
        ArrayList<ChuongTrinhKM_DTO> result = new ArrayList<>();
        String key = keyword.toLowerCase();

        for (ChuongTrinhKM_DTO ct : listCache) {
            if (ct.getMa().toLowerCase().contains(key) ||
                    ct.getTen().toLowerCase().contains(key)) {
                result.add(ct);
            }
        }
        return result;
    }

    // Hàm validate dữ liệu chung
    private boolean kiemTraHopLe(ChuongTrinhKM_DTO ctkm) {
        if (ctkm.getTen().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên chương trình không được để trống!");
            return false;
        }
        // Kiểm tra logic ngày: Ngày bắt đầu không được lớn hơn ngày kết thúc
        if (ctkm.getNgayBatDau().after(ctkm.getNgayKetThuc())) {
            JOptionPane.showMessageDialog(null, "Ngày bắt đầu không được lớn hơn ngày kết thúc!");
            return false;
        }
        return true;
    }
    public ChuongTrinhKM_DTO getById(String ma) {
        if (ma == null || ma.trim().isEmpty()) return null;
        for (ChuongTrinhKM_DTO ct : listCache) {
            if (ct.getMa().equals(ma)) {
                return ct;
            }
        }
        return null;
    }
}