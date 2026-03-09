package bus;

import dao.GiaTriThuocTinh_DAO;
import dto.GiaTriThuocTinh_DTO;
import java.util.ArrayList;

public class GiaTriThuocTinh_BUS {
    private static GiaTriThuocTinh_BUS instance;
    private final GiaTriThuocTinh_DAO dao = GiaTriThuocTinh_DAO.getInstance();
    private ArrayList<GiaTriThuocTinh_DTO> listCache;

    private GiaTriThuocTinh_BUS() { refreshData(); }

    public static GiaTriThuocTinh_BUS getInstance() {
        if (instance == null) instance = new GiaTriThuocTinh_BUS();
        return instance;
    }

    public void refreshData() { listCache = dao.getAll(); }

    public ArrayList<GiaTriThuocTinh_DTO> getAll() {
        if (listCache == null) refreshData();
        return listCache;
    }

    public GiaTriThuocTinh_DTO getById(String maGT) {
        if (maGT == null || maGT.trim().isEmpty()) return null;
        for (GiaTriThuocTinh_DTO gt : getAll()) {
            if (gt.getMaGiaTri().equalsIgnoreCase(maGT)) return gt;
        }
        return null;
    }

    public ArrayList<GiaTriThuocTinh_DTO> getByMaThuocTinh(String maTT) {
        ArrayList<GiaTriThuocTinh_DTO> res = new ArrayList<>();
        for (GiaTriThuocTinh_DTO gt : getAll()) {
            if (gt.getMaThuocTinh().equalsIgnoreCase(maTT)) res.add(gt);
        }
        return res;
    }

    public String getNextId() { return dao.getNextId(); }

    public boolean them(GiaTriThuocTinh_DTO gt) {
        if (dao.them(gt)) {
            refreshData();
            return true;
        }
        return false;
    }

    public boolean capNhat(GiaTriThuocTinh_DTO gt) {
        if (dao.capNhat(gt)) {
            refreshData();
            return true;
        }
        return false;
    }
    public String getMaByNoiDung(String maTT, String noiDung) {
        for (GiaTriThuocTinh_DTO gt : getAll()) {
            if (gt.getMaThuocTinh().equals(maTT) && gt.getNdGiaTri().equalsIgnoreCase(noiDung.trim())) {
                return gt.getMaGiaTri();
            }
        }
        return null;
    }
}