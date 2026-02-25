package BUS;

import DAO.KhuVucLuuTru_DAO;
import dto.KhuVucLuuTru_DTO;
import java.util.ArrayList;

public class KhuVucLuuTru_BUS {

    private KhuVucLuuTru_DAO dao;

    public KhuVucLuuTru_BUS() {
        dao = new KhuVucLuuTru_DAO();
    }

    public ArrayList<KhuVucLuuTru_DTO> getAll() {
        return dao.getAll();
    }

    // them
    public boolean insert(KhuVucLuuTru_DTO kv) {
        if (kv.getMaKVLT() == null || kv.getMaKVLT().trim().isEmpty()) {
            System.out.println("Mã khu vực không được để trống");
            return false;
        }
        if (kv.getSucChua() <= 0) {
            System.out.println("Sức chứa phải lớn hơn 0");
            return false;
        }
        kv.setHienCo(0);

        return dao.insert(kv);
    }

    // cap nhat kho
    public boolean update(KhuVucLuuTru_DTO kv) {

        if (kv.getMaKVLT() == null || kv.getMaKVLT().trim().isEmpty()) {
            System.out.println("Mã khu vực không hợp lệ");
            return false;
        }

        if (kv.getSucChua() < kv.getHienCo()) {
            System.out.println("Sức chứa không được nhỏ hơn số lượng hiện có");
            return false;
        }

        return dao.update(kv);
    }

    // Cap nhat trang thai
    public boolean updateTrangThai(String maKVLT, int trangThaiMoi) {
        if (maKVLT == null || maKVLT.trim().isEmpty()) {
            System.out.println("Mã khu vực không hợp lệ");
            return false;
        }
        if (trangThaiMoi < 1 || trangThaiMoi > 3) {
            System.out.println("Trạng thái không hợp lệ");
            return false;
        }

        return dao.updateTrangThai(maKVLT, trangThaiMoi);
    }
}