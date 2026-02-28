package bus;

import dao.*;
import dto.*;

import javax.swing.*;
import java.util.*;

public class PhanQuyen_BUS {

    private static PhanQuyen_BUS instance;
    private PhanQuyen_DAO pqDao = new PhanQuyen_DAO();
    private ArrayList<PhanQuyen_DTO> listCache;

    private PhanQuyen_BUS(){
        listCache = pqDao.getAll();
    }
    public static PhanQuyen_BUS getInstance() {
        if (instance == null) {
            instance = new PhanQuyen_BUS();
        }
        return instance;
    }

    public ArrayList<PhanQuyen_DTO> getAll(){
        return listCache;
    }

    public String getNextId() {
        return pqDao.getNextId();
    }

    public boolean them(PhanQuyen_DTO quyen) {
        boolean result = pqDao.them(quyen);
        if(result) refreshData();
        return result;

    }

    public boolean capNhat(PhanQuyen_DTO quyen) {
        boolean result = pqDao.capNhat(quyen);
        if(result) refreshData();
        return result;
    }

    public boolean kiemTraHopLe(PhanQuyen_DTO quyen) {
        if(quyen.getTenQuyen().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên quyền không được để trống");
            return false;
        }
        if(quyen.getMoTa().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mô tả không được để trống");
            return false;
        }
        if (quyen.getTrangThai() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn trạng thái");
            return false;
        }
        return true;
    }

    public ArrayList<PhanQuyen_DTO> timKiem(String keyword, Integer trangThai) {
        ArrayList<PhanQuyen_DTO> result = new ArrayList<>();
        keyword = keyword.toLowerCase();
        for (PhanQuyen_DTO pq : listCache) {
            boolean matchKeyword =
                    pq.getMaQuyen().toLowerCase().contains(keyword)
                            || pq.getTenQuyen().toLowerCase().contains(keyword) || keyword == null;
            boolean matchTrangThai = (trangThai == null || pq.getTrangThai() == trangThai);
            if (matchKeyword && matchTrangThai) {
                result.add(pq);
            }
        }
        return result;
    }

    public PhanQuyen_DTO getById(String maQuyen) {
        for(PhanQuyen_DTO pq : listCache) {
            if(pq.getMaQuyen().equals(maQuyen))
                return pq;
        }
        return null;
    }
    public PhanQuyen_DTO getByName(String name) {
        for(PhanQuyen_DTO pq : listCache) {
            if(pq.getTenQuyen().equals(name))
                return pq;
        }
        return null;
    }

    public ArrayList<PhanQuyen_DTO> getQuyenHoatDong() {
        ArrayList<PhanQuyen_DTO> result = new ArrayList<>();
        for (PhanQuyen_DTO pq : listCache) {
            if (pq.getTrangThai() == 1) {
                result.add(pq);
            }
        }
        return result;
    }

    public ArrayList<PhanQuyen_DTO> getQuyenNgungHoatDong() {
        ArrayList<PhanQuyen_DTO> result = new ArrayList<>();

        for (PhanQuyen_DTO pq : listCache) {
            if (pq.getTrangThai() == 0) {
                result.add(pq);
            }
        }

        return result;
    }

    public void refreshData() {
        listCache = pqDao.getAll();
    }

}
