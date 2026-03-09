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

    public ArrayList<DanhMuc_DTO> timKiemNangCao(String keyword, String timTheo, Integer trangThai) {
        ArrayList<DanhMuc_DTO> result = new ArrayList<>();
        String key = (keyword == null) ? "" : keyword.toLowerCase().trim();

        for (DanhMuc_DTO dm : listCache) {
            boolean matchKey = false;
            if (timTheo.equals("Tất cả")) {
                matchKey = dm.getMaDM().toLowerCase().contains(key) || dm.getTenDM().toLowerCase().contains(key);
            } else if (timTheo.equals("Mã danh mục")) {
                matchKey = dm.getMaDM().toLowerCase().contains(key);
            } else { // Tên danh mục
                matchKey = dm.getTenDM().toLowerCase().contains(key);
            }

            boolean matchTT = (trangThai == null || dm.getTrangThai() == trangThai);

            if (matchKey && matchTT) {
                result.add(dm);
            }
        }
        return result;
    }

    public ArrayList<ThuocTinhDanhMuc_DTO> getThuocTinhByMaDM(String maDM) {
        ArrayList<ThuocTinhDanhMuc_DTO> result = new ArrayList<>();
        if (maDM == null || maDM.isEmpty()) return result;

        for (ThuocTinhDanhMuc_DTO tt : ttBus.getAll()) {
            if (tt.getMaDM().equals(maDM)) {
                result.add(tt);
            }
        }
        return result;
    }

    public String getNextId() {
        return dmDao.getNextId();
    }

    public boolean them(DanhMuc_DTO dm) {
        boolean result = dmDao.them(dm);
        if (result) refreshData();
        return result;
    }

    public boolean capNhat(DanhMuc_DTO dm) {
        boolean result = dmDao.capNhat(dm);
        if (result) refreshData();
        return result;
    }

    public void refreshData() {
        listCache = dmDao.getAll();
    }

}