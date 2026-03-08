package bus;

import dao.NhaCungCap_DAO;
import dto.NhaCungCap_DTO;
import dto.DIACHI_DTO;

import java.util.ArrayList;
import javax.swing.JOptionPane;

public class NhaCungCap_BUS {

    private static NhaCungCap_BUS instance;
    private NhaCungCap_DAO dao = new NhaCungCap_DAO();
    private ArrayList<NhaCungCap_DTO> listCache;

    private NhaCungCap_BUS() {
        refreshData();
    }

    public static NhaCungCap_BUS getInstance() {
        if (instance == null) {
            instance = new NhaCungCap_BUS();
        }
        return instance;
    }

    public ArrayList<NhaCungCap_DTO> getAll() {
        return listCache;
    }

    public void refreshData() {
        listCache = dao.getAll();
        DiaChi_BUS diaChiBUS = DiaChi_BUS.getInstance();
        for (NhaCungCap_DTO ncc : listCache) {
            if (ncc.getDiaChi() != null && ncc.getDiaChi().getMaDiaChi() != null) {
                DIACHI_DTO fullDC = diaChiBUS.getById(ncc.getDiaChi().getMaDiaChi());
                if (fullDC != null) {
                    ncc.setDiaChi(fullDC);
                }
            }
        }
    }
    public ArrayList<NhaCungCap_DTO> timKiem(String keyword, Integer trangThai) {
        ArrayList<NhaCungCap_DTO> result = new ArrayList<>();
        String k = (keyword == null) ? "" : keyword.toLowerCase().trim();

        for (NhaCungCap_DTO ncc : listCache) {
            boolean matchKeyword = k.isEmpty() ||
                    ncc.getMaNCC().toLowerCase().contains(k) ||
                    ncc.getTenNCC().toLowerCase().contains(k) ||
                    ncc.getSdt().contains(k) ||
                    ncc.getMaSoThue().toLowerCase().contains(k);
            boolean matchTrangThai = (trangThai == null || ncc.getTrangThai() == trangThai);

            if (matchKeyword && matchTrangThai) {
                result.add(ncc);
            }
        }
        return result;
    }

    public boolean kiemTraHopLe(NhaCungCap_DTO ncc) {
        if (ncc.getTenNCC() == null || ncc.getTenNCC().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên nhà cung cấp không được để trống!");
            return false;
        }
        if (ncc.getSdt() == null || !ncc.getSdt().matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ (phải là 10-11 chữ số)!");
            return false;
        }
        if (ncc.getTrangThai() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn trạng thái hợp lệ!");
            return false;
        }

        if (ncc.getDiaChi() == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập địa chỉ!");
            return false;
        }

        return true;
    }

    public boolean insert(NhaCungCap_DTO ncc) {
        if (!kiemTraHopLe(ncc)) return false;
        DiaChi_BUS diaChiBUS = DiaChi_BUS.getInstance();
        DIACHI_DTO dc = ncc.getDiaChi();
        if (dc.getMaDiaChi() == null || dc.getMaDiaChi().trim().isEmpty()) {
            dc.setMaDiaChi(diaChiBUS.getNextId());
            if (!diaChiBUS.them(dc)) {
                return false;
            }
        }

        boolean result = dao.insert(ncc);
        if (result) {
            refreshData();
        }
        return result;
    }

    public boolean update(NhaCungCap_DTO ncc) {
        if (!kiemTraHopLe(ncc)) return false;
        if (ncc.getDiaChi() != null) {
            DiaChi_BUS.getInstance().capNhat(ncc.getDiaChi());
        }

        boolean result = dao.update(ncc);
        if (result) {
            refreshData();
        }
        return result;
    }

    public NhaCungCap_DTO getById(String maNCC) {
        for (NhaCungCap_DTO ncc : listCache) {
            if (ncc.getMaNCC().equalsIgnoreCase(maNCC)) return ncc;
        }
        return null;
    }
    public NhaCungCap_DTO getByName(String tenNCC) {
        if (tenNCC == null || tenNCC.trim().isEmpty()) return null;

        for (NhaCungCap_DTO ncc : listCache) {
            if (ncc.getTenNCC().equalsIgnoreCase(tenNCC.trim())) {
                return ncc;
            }
        }
        return null;
    }
    public String getNextId() {
        return dao.getNextId();
    }
}