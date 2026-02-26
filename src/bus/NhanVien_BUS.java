package bus;

import dao.NhanVien_DAO;
import dto.NhanVien_DTO;
import dto.PhanQuyen_DTO;

import javax.swing.*;
import java.util.ArrayList;

public class NhanVien_BUS {

    private static NhanVien_BUS instance;
    private NhanVien_DAO nvDao = new NhanVien_DAO();
    private ArrayList<NhanVien_DTO> listCache;

    private NhanVien_BUS() {
        listCache = nvDao.getAll();
    }

    public static NhanVien_BUS getInstance() {
        if (instance == null) {
            instance = new NhanVien_BUS();
        }
        return instance;
    }

    public ArrayList<NhanVien_DTO> getAll() {
        return listCache;
    }

    public void refreshData() {
        listCache = nvDao.getAll();
    }

    public String getNextId() {
        return nvDao.getNextId();
    }

    public boolean them(NhanVien_DTO nv) {
        if (!kiemTraHopLe(nv)) return false;

        boolean result = nvDao.them(nv);
        if (result) refreshData();

        return result;
    }

    public boolean capNhat(NhanVien_DTO nv) {
        if (!kiemTraHopLe(nv)) return false;

        boolean result = nvDao.capNhat(nv);
        if (result) refreshData();

        return result;
    }

    // ================== VALIDATE ==================

    public boolean kiemTraHopLe(NhanVien_DTO nv) {

        if (nv.getTen().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên nhân viên không được để trống");
            return false;
        }

        if (nv.getNgaySinh() == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày sinh");
            return false;
        }

        if (nv.getMaDiaChi() == null || nv.getMaDiaChi().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn địa chỉ");
            return false;
        }

        if (nv.getNgaySinh() == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày sinh");
            return false;
        }

        if (nv.getSdt().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không được để trống");
            return false;
        }

        if (nv.getChucVu().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Chức vụ không được để trống");
            return false;
        }

        if (nv.getLuongCoBan() <= 0) {
            JOptionPane.showMessageDialog(null, "Lương cơ bản phải lớn hơn 0");
            return false;
        }

        if (nv.getNgayVaoLam() == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày vào làm");
            return false;
        }

        if (nv.getTrangThai() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn trạng thái");
            return false;
        }

        return true;
    }

    // ================== TÌM KIẾM ==================

    public ArrayList<NhanVien_DTO> timKiem(String keyword, Integer trangThai) {

        ArrayList<NhanVien_DTO> result = new ArrayList<>();
        keyword = keyword.toLowerCase();

        for (NhanVien_DTO nv : listCache) {

            boolean matchKeyword =
                    nv.getMa().toLowerCase().contains(keyword)
                            || nv.getTen().toLowerCase().contains(keyword)
                            || nv.getSdt().toLowerCase().contains(keyword)
                            || nv.getChucVu().toLowerCase().contains(keyword);

            boolean matchTrangThai =
                    (trangThai == null || nv.getTrangThai() == trangThai);

            if (matchKeyword && matchTrangThai) {
                result.add(nv);
            }
        }

        return result;
    }

    public NhanVien_DTO getById(String maNV) {

        for (NhanVien_DTO nv : listCache) {
            if (nv.getMa().equals(maNV))
                return nv;
        }

        return null;
    }
    public NhanVien_DTO getBySdt(String maNV) {
        for(NhanVien_DTO pq : listCache) {
            if(pq.getMa().equals(maNV))
                return pq;
        }
        return null;
    }
}