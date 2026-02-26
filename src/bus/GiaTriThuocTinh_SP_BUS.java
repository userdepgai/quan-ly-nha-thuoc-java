package bus;

import dao.GiaTriThuocTinh_SP_DAO;
import dto.GiaTriThuocTinh_SP_DTO;
import java.util.ArrayList;

public class GiaTriThuocTinh_SP_BUS {
    private static GiaTriThuocTinh_SP_BUS instance;
    private final GiaTriThuocTinh_SP_DAO dao = new GiaTriThuocTinh_SP_DAO();
    private ArrayList<GiaTriThuocTinh_SP_DTO> listCache;

    private GiaTriThuocTinh_SP_BUS() { listCache = dao.getAll(); }

    public static GiaTriThuocTinh_SP_BUS getInstance() {
        if (instance == null) instance = new GiaTriThuocTinh_SP_BUS();
        return instance;
    }

    public ArrayList<GiaTriThuocTinh_SP_DTO> getByMaSP(String maSP) {
        ArrayList<GiaTriThuocTinh_SP_DTO> result = new ArrayList<>();
        for (GiaTriThuocTinh_SP_DTO item : listCache) {
            if (item.getMaSP().equals(maSP)) result.add(item);
        }
        return result;
    }

    public void refreshData() { listCache = dao.getAll(); }
}