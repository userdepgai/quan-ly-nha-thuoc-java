package bus;

import dao.DanhMuc_DAO;
import dto.DanhMuc_DTO;
import dto.ThuocTinhDanhMuc_DTO;

import java.util.ArrayList;

public class DanhMuc_BUS {
    private static DanhMuc_BUS instance;
    private final DanhMuc_DAO dmDao = new DanhMuc_DAO();

    private final ThuocTinhDanhMuc_BUS ttBus = ThuocTinhDanhMuc_BUS.getInstance();

    private ArrayList<DanhMuc_DTO> listCache;

    private DanhMuc_BUS() {
        listCache = dmDao.getAll();
    }

    public static DanhMuc_BUS getInstance() {
        if (instance == null) {
            instance = new DanhMuc_BUS();
        }
        return instance;
    }

    // 1. Lấy tất cả danh mục từ Cache
    public ArrayList<DanhMuc_DTO> getAll() {
        return listCache;
    }

    public DanhMuc_DTO getById(String maDM) {
        if (maDM == null) return null;
        for (DanhMuc_DTO dm : listCache) {
            if (dm.getMaDM().equals(maDM)) {
                return dm;
            }
        }
        return null;
    }

    // --- BỔ SUNG THÊM HÀM TÌM KIẾM (Nếu GUI cần dùng) ---
    public ArrayList<DanhMuc_DTO> timKiem(String keyword, Integer trangThai) {
        ArrayList<DanhMuc_DTO> result = new ArrayList<>();
        String key = (keyword == null) ? "" : keyword.toLowerCase().trim();

        for (DanhMuc_DTO dm : listCache) {
            boolean matchKey = dm.getTenDM().toLowerCase().contains(key) ||
                    dm.getMaDM().toLowerCase().contains(key);
            boolean matchTT = (trangThai == null || dm.getTrangThai() == trangThai);

            if (matchKey && matchTT) {
                result.add(dm);
            }
        }
        return result;
    }

    // 2. Lấy danh sách Thuộc tính thuộc về một Danh mục cụ thể
    // Hàm này sẽ lọc từ Cache của ThuocTinhDanhMuc_BUS
    public ArrayList<ThuocTinhDanhMuc_DTO> getThuocTinhByMaDM(String maDM) {
        ArrayList<ThuocTinhDanhMuc_DTO> result = new ArrayList<>();
        if (maDM == null || maDM.isEmpty()) return result;

        // Lọc trực tiếp từ listCache của ThuocTinhDanhMuc_BUS thông qua phương thức getAll()
        for (ThuocTinhDanhMuc_DTO tt : ttBus.getAll()) {
            if (tt.getMaDM().equals(maDM)) {
                result.add(tt);
            }
        }
        return result;
    }

    // 3. Lấy mã danh mục tiếp theo
    public String getNextId() {
        return dmDao.getNextId();
    }

    // 4. Thêm danh mục
    public boolean them(DanhMuc_DTO dm) {
        boolean result = dmDao.them(dm);
        if (result) refreshData();
        return result;
    }

    // 5. Cập nhật danh mục
    public boolean capNhat(DanhMuc_DTO dm) {
        boolean result = dmDao.capNhat(dm);
        if (result) refreshData();
        return result;
    }

    public void refreshData() {
        listCache = dmDao.getAll();
    }
}