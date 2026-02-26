package bus;

import dao.KhachHang_DAO;
import dto.KhachHang_DTO;

import javax.swing.*;
import java.util.ArrayList;

public class KhachHang_BUS {

    private static KhachHang_BUS instance;
    private KhachHang_DAO khDao = new KhachHang_DAO();
    private ArrayList<KhachHang_DTO> listCache;

    private KhachHang_BUS() {
        listCache = khDao.getAll();
    }

    public static KhachHang_BUS getInstance() {
        if (instance == null)
            instance = new KhachHang_BUS();
        return instance;
    }

    public ArrayList<KhachHang_DTO> getAll() {
        return listCache;
    }

    public void refreshData() {
        listCache = khDao.getAll();
    }

    public String getNextId() {
        return khDao.getNextId();
    }

    public boolean them(KhachHang_DTO kh) {

        if (!kiemTraHopLe(kh)) return false;

        boolean result = khDao.them(kh);

        if (result) refreshData();

        return result;
    }

    public boolean capNhat(KhachHang_DTO kh) {

        if (!kiemTraHopLe(kh)) return false;

        boolean result = khDao.capNhat(kh);

        if (result) refreshData();

        return result;
    }

    // ================= VALIDATE =================
    public boolean kiemTraHopLe(KhachHang_DTO kh) {

        if (kh.getTen() == null || kh.getTen().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên khách hàng không được để trống");
            return false;
        }

        if (kh.getSdt() == null || kh.getSdt().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "SĐT không được để trống");
            return false;
        }

        if (kh.getNgaySinh() == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày sinh");
            return false;
        }

        if (kh.getHang() == null || kh.getHang().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Hạng khách hàng không được để trống");
            return false;
        }

        if (kh.getNgayDKThanhVien() == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày đăng ký");
            return false;
        }

        if (kh.getDiemThuong() < 0 || kh.getDiemHang() < 0) {
            JOptionPane.showMessageDialog(null, "Điểm không hợp lệ");
            return false;
        }

        return true;
    }

    // ================= TÌM KIẾM =================
    public ArrayList<KhachHang_DTO> timKiem(String keyword) {

        ArrayList<KhachHang_DTO> result = new ArrayList<>();
        keyword = keyword.toLowerCase();

        for (KhachHang_DTO kh : listCache) {

            boolean match =
                    kh.getMa().toLowerCase().contains(keyword)
                            || kh.getTen().toLowerCase().contains(keyword)
                            || kh.getSdt().toLowerCase().contains(keyword)
                            || kh.getHang().toLowerCase().contains(keyword);

            if (match)
                result.add(kh);
        }

        return result;
    }
    public KhachHang_DTO getById(String maKH) {

        for (KhachHang_DTO nv : listCache) {
            if (nv.getMa().equals(maKH))
                return nv;
        }

        return null;
    }
    public KhachHang_DTO getBySdt(String maKH) {
        for(KhachHang_DTO pq : listCache) {
            if(pq.getMa().equals(maKH))
                return pq;
        }
        return null;
    }
}