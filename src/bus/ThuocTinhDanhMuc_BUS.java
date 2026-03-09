package bus;

import dao.ThuocTinhDanhMuc_DAO;
import dto.ThuocTinhDanhMuc_DTO;
import javax.swing.JOptionPane;
import java.util.ArrayList;

public class ThuocTinhDanhMuc_BUS {
    private static ThuocTinhDanhMuc_BUS instance;
    private final ThuocTinhDanhMuc_DAO dao = ThuocTinhDanhMuc_DAO.getInstance();
    private ArrayList<ThuocTinhDanhMuc_DTO> listCache;

    private ThuocTinhDanhMuc_BUS() { refreshData(); }

    public static ThuocTinhDanhMuc_BUS getInstance() {
        if (instance == null) instance = new ThuocTinhDanhMuc_BUS();
        return instance;
    }

    public void refreshData() { listCache = dao.getAll(); }

    public ArrayList<ThuocTinhDanhMuc_DTO> getAll() {
        if (listCache == null) refreshData();
        return listCache;
    }

    public ThuocTinhDanhMuc_DTO getById(String maTT) {
        if (maTT == null || maTT.trim().isEmpty()) return null;
        for (ThuocTinhDanhMuc_DTO tt : getAll()) {
            if (tt.getMaThuocTinh().equalsIgnoreCase(maTT)) return tt;
        }
        return null;
    }

    public String getNextId() { return dao.getNextId(); }

    public boolean them(ThuocTinhDanhMuc_DTO tt) {
        if (dao.them(tt)) {
            refreshData();
            return true;
        }
        return false;
    }

    public boolean capNhat(ThuocTinhDanhMuc_DTO tt) {
        if (dao.capNhat(tt)) {
            refreshData();
            return true;
        }
        return false;
    }

    public ArrayList<ThuocTinhDanhMuc_DTO> timKiemNangCao(String keyword, String timTheo, String maDM, Integer trangThai) {
        ArrayList<ThuocTinhDanhMuc_DTO> result = new ArrayList<>();
        String key = (keyword == null) ? "" : keyword.toLowerCase().trim();

        for (ThuocTinhDanhMuc_DTO tt : getAll()) {
            boolean matchKey = timTheo.equals("Tất cả") ? (tt.getMaThuocTinh().toLowerCase().contains(key) || tt.getTenThuocTinh().toLowerCase().contains(key))
                    : timTheo.equals("Mã Thuộc Tính") ? tt.getMaThuocTinh().toLowerCase().contains(key)
                    : tt.getTenThuocTinh().toLowerCase().contains(key);

            boolean matchDM = (maDM == null || maDM.isEmpty() || tt.getMaDM().equals(maDM));
            boolean matchTT = (trangThai == null || tt.getTrangThai() == trangThai);

            if (matchKey && matchDM && matchTT) result.add(tt);
        }
        return result;
    }
}