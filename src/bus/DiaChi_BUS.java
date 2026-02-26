package BUS;

import DAO.DiaChi_DAO;
import dto.DIACHI_DTO;

import java.util.ArrayList;

public class DiaChi_BUS {

    private static DiaChi_BUS instance;
    private DiaChi_DAO dao = new DiaChi_DAO();
    private ArrayList<DIACHI_DTO> list;
    public static DiaChi_BUS getInstance() {
        if (instance == null) {
            instance = new DiaChi_BUS();
        }
        return instance;
    }

    private DiaChi_BUS() {
        list = dao.getAll();
    }

    public ArrayList<DIACHI_DTO> getAll() {
        list = dao.getAll();
        return list;
    }

    public String getNextId() {
        return dao.getNextId();
    }

    public boolean them(DIACHI_DTO dc) {
        if (dc.getTinh().trim().isEmpty() ||
                dc.getPhuong().trim().isEmpty() ||
                dc.getDuong().trim().isEmpty() ||
                dc.getSoNha().trim().isEmpty()) {
            return false;
        }
        boolean result = dao.them(dc);
        if (result)
            list.add(dc);
        return result;
    }

    public boolean capNhat(DIACHI_DTO dc) {
        boolean result = dao.capNhat(dc);
        if (result) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getMaDiaChi().equals(dc.getMaDiaChi())) {
                    list.set(i, dc);
                    break;
                }
            }
        }
        return result;
    }
    public DIACHI_DTO getById(String maDC) {
        for (DIACHI_DTO dc : list) {
            if (dc.getMaDiaChi().equals(maDC)) {
                return dc;
            }
        }
        return null;
    }
}