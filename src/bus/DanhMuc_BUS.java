package bus;

import dao.DanhMuc_DAO;
import dto.DanhMuc_DTO;
import javax.swing.JOptionPane;
import java.util.ArrayList;

public class DanhMuc_BUS {

    private static DanhMuc_BUS instance;
    private final DanhMuc_DAO dmDao = new DanhMuc_DAO();
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

    public String getNextId() {
        return dmDao.getNextId();
    }

    public boolean them(DanhMuc_DTO dm) {
        if (!kiemTraHopLe(dm)) return false;
        // Kiểm tra trùng tên (Optional)
        for(DanhMuc_DTO d : listCache){
            if(d.getTenDM().equalsIgnoreCase(dm.getTenDM())){
                JOptionPane.showMessageDialog(null, "Tên danh mục đã tồn tại!");
                return false;
            }
        }

        boolean result = dmDao.them(dm);
        if (result) refreshData();
        return result;
    }

    public boolean capNhat(DanhMuc_DTO dm) {
        if (!kiemTraHopLe(dm)) return false;

        boolean result = dmDao.capNhat(dm);
        if (result) refreshData();
        return result;
    }

    public boolean kiemTraHopLe(DanhMuc_DTO dm) {
        if (dm.getTenDM() == null || dm.getTenDM().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên danh mục không được để trống");
            return false;
        }
        return true;
    }

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

    // Lấy theo ID
    public DanhMuc_DTO getById(String maDM) {
        for(DanhMuc_DTO dm : listCache) {
            if(dm.getMaDM().equals(maDM)) return dm;
        }
        return null;
    }

    public void refreshData() {
        listCache = dmDao.getAll();
    }
}