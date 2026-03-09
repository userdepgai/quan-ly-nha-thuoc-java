package bus;

import dao.UuDai_DAO;
import dto.UuDai_DTO;

import java.util.ArrayList;
import java.util.Date;

public class UuDai_BUS {
    private static UuDai_BUS instance;
    private final UuDai_DAO udDao = new UuDai_DAO();
    private ArrayList<UuDai_DTO> listCache;

    private UuDai_BUS() {
        refreshData();
    }

    public static UuDai_BUS getInstance() {
        if (instance == null) {
            instance = new UuDai_BUS();
        }
        return instance;
    }

    public ArrayList<UuDai_DTO> getAll() {
        if (listCache == null) {
            refreshData();
        }
        return listCache;
    }

    public ArrayList<UuDai_DTO> getUuDaiDangDienRa() {
        ArrayList<UuDai_DTO> result = new ArrayList<>();
        Date today = new Date();
        if (listCache == null) refreshData();

        for (UuDai_DTO ud : listCache) {
            if (ud.getTrangThai() == 1 &&
                    ud.getNgayBatDau().compareTo(today) <= 0 &&
                    ud.getNgayKetThuc().compareTo(today) >= 0) {

                result.add(ud);
            }
        }
        return result;
    }

    public ArrayList<UuDai_DTO> getOnlyVoucher() {
        ArrayList<UuDai_DTO> result = new ArrayList<>();
        for (UuDai_DTO ud : listCache) {
            if (ud.getMa().startsWith("VC")) {
                result.add(ud);
            }
        }
        return result;
    }

    public ArrayList<UuDai_DTO> getOnlyCTKM() {
        ArrayList<UuDai_DTO> result = new ArrayList<>();
        for (UuDai_DTO ud : listCache) {
            if (ud.getMa().startsWith("CTKM")) {
                result.add(ud);
            }
        }
        return result;
    }

    public void refreshData() {
        listCache = udDao.getAll();
    }
}