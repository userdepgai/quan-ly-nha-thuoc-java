package bus;

import dao.SanPhamNCC_DAO;
import dto.SanPhamNCC_DTO;

import java.util.ArrayList;

public class SanPhamNCC_BUS {

    private static SanPhamNCC_BUS instance;
    private SanPhamNCC_DAO dao = new SanPhamNCC_DAO();
    private ArrayList<SanPhamNCC_DTO> listCache;

    private SanPhamNCC_BUS() {
        refreshData();
    }

    public static SanPhamNCC_BUS getInstance() {
        if (instance == null) {
            instance = new SanPhamNCC_BUS();
        }
        return instance;
    }
    public ArrayList<SanPhamNCC_DTO> getAll() {
        return listCache;
    }

    public void refreshData() {
        listCache = dao.getAll();
    }
    public ArrayList<SanPhamNCC_DTO> getByMaNCC(String maNCC) {
        ArrayList<SanPhamNCC_DTO> result = new ArrayList<>();

        if (listCache == null) {
            refreshData();
        }
        for (SanPhamNCC_DTO spNcc : listCache) {
            if (spNcc.getMaNCC().equalsIgnoreCase(maNCC)) {
                result.add(spNcc);
            }
        }

        return result;
    }

}