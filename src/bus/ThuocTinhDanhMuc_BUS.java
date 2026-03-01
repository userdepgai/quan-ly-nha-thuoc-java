package bus;

import dao.ThuocTinhDanhMuc_DAO;
import dto.ThuocTinhDanhMuc_DTO;

import javax.swing.JOptionPane;
import java.util.ArrayList;

public class ThuocTinhDanhMuc_BUS {
    private static ThuocTinhDanhMuc_BUS instance;
    private final ThuocTinhDanhMuc_DAO ttDao = new ThuocTinhDanhMuc_DAO();
    private ArrayList<ThuocTinhDanhMuc_DTO> listCache;

    // Constructor Singleton - Load dữ liệu lần đầu
    private ThuocTinhDanhMuc_BUS() {
        listCache = ttDao.getAll();
    }

    public static ThuocTinhDanhMuc_BUS getInstance() {
        if (instance == null) {
            instance = new ThuocTinhDanhMuc_BUS();
        }
        return instance;
    }

    // 1. Lấy toàn bộ danh sách từ Cache
    public ArrayList<ThuocTinhDanhMuc_DTO> getAll() {
        return listCache;
    }

    // 2. Lấy mã tiếp theo từ DB
    public String getNextId() {
        return ttDao.getNextId();
    }

    // 3. Thêm thuộc tính danh mục
    public boolean them(ThuocTinhDanhMuc_DTO tt) {
        if (!kiemTraHopLe(tt)) return false;

        boolean result = ttDao.them(tt);
        if (result) {
            refreshData(); // Cập nhật Cache Thuộc tính
        }
        return result;
    }

    // 4. Cập nhật thuộc tính danh mục
    public boolean capNhat(ThuocTinhDanhMuc_DTO tt) {
        if (!kiemTraHopLe(tt)) return false;

        boolean result = ttDao.capNhat(tt);
        if (result) {
            refreshData(); // Cập nhật Cache Thuộc tính
        }
        return result;
    }

    // 5. Lấy thuộc tính theo ID từ Cache
    public ThuocTinhDanhMuc_DTO getById(String maTT) {
        if (maTT == null) return null;
        for (ThuocTinhDanhMuc_DTO tt : listCache) {
            if (tt.getMaThuocTinh().equals(maTT)) {
                return tt;
            }
        }
        return null;
    }

    // 6. Tìm kiếm và lọc dữ liệu trên Cache
    public ArrayList<ThuocTinhDanhMuc_DTO> timKiem(String keyword, String maDM, Integer trangThai) {
        ArrayList<ThuocTinhDanhMuc_DTO> result = new ArrayList<>();
        String key = (keyword == null) ? "" : keyword.toLowerCase();

        for (ThuocTinhDanhMuc_DTO tt : listCache) {
            boolean matchKey = tt.getTenThuocTinh().toLowerCase().contains(key) ||
                    tt.getMaThuocTinh().toLowerCase().contains(key);

            // Lọc theo Danh mục (DM001, DM002...)
            boolean matchDM = (maDM == null || maDM.equals("Tất cả") || tt.getMaDM().equals(maDM));

            // Lọc theo Trạng thái (1: Hoạt động, 0: Ngưng)
            boolean matchTT = (trangThai == null || tt.getTrangThai() == trangThai);

            if (matchKey && matchDM && matchTT) {
                result.add(tt);
            }
        }
        return result;
    }

    // 7. Refresh dữ liệu từ DB
    public void refreshData() {
        listCache = ttDao.getAll();
    }

    // 8. Kiểm tra hợp lệ dữ liệu
    private boolean kiemTraHopLe(ThuocTinhDanhMuc_DTO tt) {
        if (tt.getTenThuocTinh() == null || tt.getTenThuocTinh().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên thuộc tính không được để trống!");
            return false;
        }
        if (tt.getMaDM() == null || tt.getMaDM().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Phải chọn danh mục áp dụng!");
            return false;
        }
        return true;
    }
}