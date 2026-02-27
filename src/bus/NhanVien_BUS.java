package bus;

import dao.NhanVien_DAO;
import dto.KhachHang_DTO;
import dto.NhanVien_DTO;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class NhanVien_BUS {
    private NhanVien_DAO nvDao = new NhanVien_DAO();
    private static NhanVien_BUS instance;
    private NhanVien_DAO dao = new NhanVien_DAO();
    private ArrayList<NhanVien_DTO> listCache;

    private NhanVien_BUS() {
        listCache = dao.getAll();
    }

    public static NhanVien_BUS getInstance() {
        if (instance == null)
            instance = new NhanVien_BUS();
        return instance;
    }

    public ArrayList<NhanVien_DTO> getAll() {
        return listCache;
    }

    public String getNextId() {
        return dao.getNextId();
    }

    public boolean them(NhanVien_DTO nv) {

        if (!kiemTraHopLe(nv, false)) return false;

        boolean result = nvDao.them(nv);
        if (result) refreshData();
        return result;
    }

    public boolean capNhat(NhanVien_DTO nv) {

        if (!kiemTraHopLe(nv, true)) return false;

        boolean result = nvDao.capNhat(nv);
        if (result) refreshData();
        return result;
    }

    public void refreshData() {
        listCache = dao.getAll();
    }
    public boolean isPhoneExists(String sdt) {
        for (NhanVien_DTO nv : listCache) {
            if (nv.getSdt().equals(sdt)) {
                return true;
            }
        }
        return false;
    }
    public boolean kiemTraHopLe(NhanVien_DTO nv, boolean isUpdate) {

        if (nv.getTen().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên không được để trống");
            return false;
        }

        if (!nv.getSdt().matches("\\d{10}")) {
            JOptionPane.showMessageDialog(null, "SĐT phải 10 số");
            return false;
        }

        // ===== KIỂM TRA TRÙNG SĐT =====
        for (NhanVien_DTO item : listCache) {

            if (item.getSdt().equals(nv.getSdt())) {

                // Nếu là cập nhật và là chính nó thì bỏ qua
                if (isUpdate && item.getMa().equals(nv.getMa())) {
                    continue;
                }

                JOptionPane.showMessageDialog(null, "SĐT đã tồn tại");
                return false;
            }
        }

        if (nv.getLuongCoBan() < 0) {
            JOptionPane.showMessageDialog(null, "Lương không hợp lệ");
            return false;
        }

        return true;
    }
    public ArrayList<NhanVien_DTO> timKiemNangCao(String keyword,
                                                  String chucVu,
                                                  String trangThai) {

        ArrayList<NhanVien_DTO> result = new ArrayList<>();

        for (NhanVien_DTO nv : listCache) {

            boolean matchKeyword = true;
            boolean matchChucVu = true;
            boolean matchTrangThai = true;

            // ===== LỌC KEYWORD (mã, tên, sdt) =====
            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = keyword.toLowerCase();
                matchKeyword =
                        nv.getMa().toLowerCase().contains(kw) ||
                                nv.getTen().toLowerCase().contains(kw) ||
                                nv.getSdt().contains(kw);
            }
            if (chucVu != null && !chucVu.equals("--chọn chức vụ--")) {
                matchChucVu = nv.getChucVu().trim().equalsIgnoreCase(chucVu.trim());
            }


            // ===== LỌC TRẠNG THÁI =====
            if (trangThai != null && !trangThai.equals("--chọn trạng thái--")) {

                int tt = trangThai.equals("Đang làm") ? 0 : 1;

                matchTrangThai = nv.getTrangThai() == tt;
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
    // ================= GET BY SDT =================
    public NhanVien_DTO getBysdt(String sdt) {

        for (NhanVien_DTO nv : listCache) {
            if (nv.getMa().equals(sdt))
                return nv;
        }
        return null;
    }
}