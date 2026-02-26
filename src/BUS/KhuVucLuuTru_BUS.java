package BUS;

import DAO.KhuVucLuuTru_DAO;
import dto.KhuVucLuuTru_DTO;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class KhuVucLuuTru_BUS {

    private static KhuVucLuuTru_BUS instance;
    private KhuVucLuuTru_DAO dao = new KhuVucLuuTru_DAO();
    private ArrayList<KhuVucLuuTru_DTO> listCache;

    private KhuVucLuuTru_BUS() {
        listCache = dao.getAll();
    }


    public static KhuVucLuuTru_BUS getInstance() {
        if (instance == null) {
            instance = new KhuVucLuuTru_BUS();
        }
        return instance;
    }
    public ArrayList<KhuVucLuuTru_DTO> getAll() {
        return listCache;
    }
    public void refreshData() {
        listCache = dao.getAll();
    }

    public boolean kiemTraHopLe(KhuVucLuuTru_DTO kv) {
        if (kv.getMaKVLT() == null || kv.getMaKVLT().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã khu vực không được để trống");
            return false;
        }
        if (kv.getSucChua() <= 0) {
            JOptionPane.showMessageDialog(null, "Sức chứa phải lớn hơn 0");
            return false;
        }
        if (kv.getSucChua() < kv.getHienCo()) {
            JOptionPane.showMessageDialog(null, "Sức chứa không được nhỏ hơn số lượng hiện có");
            return false;
        }
        return true;
    }

    public boolean insert(KhuVucLuuTru_DTO kv) {
        kv.setHienCo(0);
        boolean result = dao.insert(kv);
        if (result) {
            refreshData();
        }
        return result;
    }

    public boolean update(KhuVucLuuTru_DTO kv) {
        boolean result = dao.update(kv);
        if (result) {
            refreshData();
        }
        return result;
    }

    public boolean updateTrangThai(String maKVLT, int trangThaiMoi) {
        if (maKVLT == null || maKVLT.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã khu vực không hợp lệ");
            return false;
        }
        if (trangThaiMoi < 1 || trangThaiMoi > 3) {
            JOptionPane.showMessageDialog(null, "Trạng thái không hợp lệ");
            return false;
        }

        boolean result = dao.updateTrangThai(maKVLT, trangThaiMoi);
        if (result) {
            refreshData();
        }
        return result;
    }

    public KhuVucLuuTru_DTO getById(String maKVLT) {
        for(KhuVucLuuTru_DTO kv : listCache) {
            if(kv.getMaKVLT().equals(maKVLT))
                return kv;
        }
        return null;
    }
}