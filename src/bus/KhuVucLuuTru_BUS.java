package bus;

import dao.KhuVucLuuTru_DAO;
import dto.KhuVucLuuTru_DTO;
import dto.DIACHI_DTO;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class KhuVucLuuTru_BUS {

    private static KhuVucLuuTru_BUS instance;
    private KhuVucLuuTru_DAO dao = new KhuVucLuuTru_DAO();
    private ArrayList<KhuVucLuuTru_DTO> listCache;

    private KhuVucLuuTru_BUS() {
        refreshData();
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
        DiaChi_BUS diaChiBUS = DiaChi_BUS.getInstance();


        for (KhuVucLuuTru_DTO kv : listCache) {
            if (kv.getDiaChi() != null && kv.getDiaChi().getMaDiaChi() != null) {
                DIACHI_DTO fullDC = diaChiBUS.getById(kv.getDiaChi().getMaDiaChi());
                kv.setDiaChi(fullDC);
            }
        }
    }

    public boolean kiemTraHopLe(KhuVucLuuTru_DTO kv) {
        if (kv.getTenKVLT() == null || kv.getTenKVLT().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên khu vực không được để trống");
            return false;
        }
        if (kv.getSucChua() <= 0) {
            JOptionPane.showMessageDialog(null, "Sức chứa phải lớn hơn 0");
            return false;
        }
        if (kv.getDiaChi() == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn/nhập địa chỉ");
            return false;
        }
        return true;
    }

    public boolean insert(KhuVucLuuTru_DTO kv) {
        if (!kiemTraHopLe(kv)) return false;

        DiaChi_BUS diaChiBUS = DiaChi_BUS.getInstance();
        DIACHI_DTO dc = kv.getDiaChi();

        if (dc.getMaDiaChi() == null || dc.getMaDiaChi().isEmpty()) {
            dc.setMaDiaChi(diaChiBUS.getNextId());
            if (!diaChiBUS.them(dc)) {
                JOptionPane.showMessageDialog(null, "Lỗi khi lưu địa chỉ mới");
                return false;
            }
        }

        if (kv.getMaKVLT() == null || kv.getMaKVLT().isEmpty()) {
            kv.setMaKVLT(dao.getNextId());
        }


        boolean result = dao.insert(kv);
        if (result) {
            refreshData();
            JOptionPane.showMessageDialog(null, "Thêm khu vực lưu trữ thành công!");
        }
        return result;
    }

    public boolean update(KhuVucLuuTru_DTO kv) {
        if (!kiemTraHopLe(kv)) return false;

        DiaChi_BUS.getInstance().capNhat(kv.getDiaChi());

        boolean result = dao.update(kv);
        if (result) {
            refreshData();
            JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
        }
        return result;
    }

    public boolean updateTrangThai(String maKVLT, int trangThaiMoi) {
        if (maKVLT == null || maKVLT.trim().isEmpty()) return false;

        boolean result = dao.updateTrangThai(maKVLT, trangThaiMoi);
        if (result) {
            refreshData();
        }
        return result;
    }

    public KhuVucLuuTru_DTO getById(String maKVLT) {
        for (KhuVucLuuTru_DTO kv : listCache) {
            if (kv.getMaKVLT().equals(maKVLT))
                return kv;
        }
        return null;
    }

    public String getNextId() {
        return dao.getNextId();
    }
}