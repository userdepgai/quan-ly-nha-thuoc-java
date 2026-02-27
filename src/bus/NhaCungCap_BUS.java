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
                ncc.setDiaChi(fullDC);
            }
        }
    }

    public boolean kiemTraHopLe(NhaCungCap_DTO ncc) {

        if (ncc.getMaNCC() == null || ncc.getMaNCC().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã nhà cung cấp không được để trống");
            return false;
        }

        if (ncc.getTenNCC() == null || ncc.getTenNCC().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên nhà cung cấp không được để trống");
            return false;
        }

        if (ncc.getSdt() == null || ncc.getSdt().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không được để trống");
            return false;
        }

        if (ncc.getDiaChi() == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn/nhập địa chỉ");
            return false;
        }

        return true;
    }


    public boolean insert(NhaCungCap_DTO ncc) {

        if (!kiemTraHopLe(ncc)) return false;
        if (getById(ncc.getMaNCC()) != null) {
            JOptionPane.showMessageDialog(null, "Mã nhà cung cấp đã tồn tại");
            return false;
        }

        DiaChi_BUS diaChiBUS = DiaChi_BUS.getInstance();
        DIACHI_DTO dc = ncc.getDiaChi();

        if (dc.getMaDiaChi() == null || dc.getMaDiaChi().trim().isEmpty()) {
            dc.setMaDiaChi(diaChiBUS.getNextId());
            if (!diaChiBUS.them(dc)) {
                JOptionPane.showMessageDialog(null, "Lỗi khi lưu địa chỉ mới");
                return false;
            }
        }

        boolean result = dao.insert(ncc);

        if (result) {
            refreshData();
            JOptionPane.showMessageDialog(null, "Thêm nhà cung cấp thành công!");
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
            JOptionPane.showMessageDialog(null, "Cập nhật nhà cung cấp thành công!");
        }

        return result;
    }

    public boolean updateTrangThai(String maNCC, int trangThaiMoi) {

        if (maNCC == null || maNCC.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã nhà cung cấp không hợp lệ");
            return false;
        }

        if (trangThaiMoi != 0 && trangThaiMoi != 1) {
            JOptionPane.showMessageDialog(null, "Trạng thái không hợp lệ");
            return false;
        }

        boolean result = dao.updateTrangThai(maNCC, trangThaiMoi);

        if (result) {
            refreshData();
        }

        return result;
    }


    public NhaCungCap_DTO getById(String maNCC) {
        for (NhaCungCap_DTO ncc : listCache) {
            if (ncc.getMaNCC().equals(maNCC)) {
                return ncc;
            }
        }
        return null;
    }
}