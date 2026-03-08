package bus;

import dao.KhachHang_DiaChi_DAO;
import dto.KhachHang_DiaChi_DTO;

import javax.swing.*;
import java.util.ArrayList;

public class KhachHang_DiaChi_BUS {

    private static KhachHang_DiaChi_BUS instance;
    private KhachHang_DiaChi_DAO khdcDao = new KhachHang_DiaChi_DAO();
    private ArrayList<KhachHang_DiaChi_DTO> listCache;

    private KhachHang_DiaChi_BUS() {
        listCache = new ArrayList<>();
    }

    public static KhachHang_DiaChi_BUS getInstance() {
        if (instance == null) {
            instance = new KhachHang_DiaChi_BUS();
        }
        return instance;
    }

    // ================= GET THEO KH =================
    public ArrayList<KhachHang_DiaChi_DTO> getByMaKH(String maKH) {
        listCache = khdcDao.getByMaKH(maKH);
        return listCache;
    }

    // ================= THÊM =================
    public boolean them(KhachHang_DiaChi_DTO dto) {

        if (dto.getMaKhachHang() == null || dto.getMaKhachHang().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã khách hàng không hợp lệ");
            return false;
        }

        if (dto.getMaDiaChi() == null || dto.getMaDiaChi().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã địa chỉ không hợp lệ");
            return false;
        }

        // Nếu là địa chỉ đầu tiên → tự động mặc định
        ArrayList<KhachHang_DiaChi_DTO> ds = khdcDao.getByMaKH(dto.getMaKhachHang());
        if (ds.isEmpty()) {
            dto.setTrangThai(1);
        }

        boolean result = khdcDao.them(dto);
        if (result) {
            refreshData(dto.getMaKhachHang());
        }

        return result;
    }

    // ================= XOÁ =================
    public boolean xoa(String maKH, String maDC) {

        // Không cho xoá nếu là mặc định
        for (KhachHang_DiaChi_DTO dto : listCache) {
            if (dto.getMaDiaChi().equals(maDC) && dto.getTrangThai() == 1) {
                JOptionPane.showMessageDialog(null,
                        "Không thể xoá địa chỉ mặc định");
                return false;
            }
        }

        boolean result = khdcDao.xoa(maKH, maDC);
        if (result) {
            refreshData(maKH);
        }

        return result;
    }

    // ================= ĐẶT MẶC ĐỊNH =================
    public boolean datMacDinh(String maKH, String maDC) {

        boolean result = khdcDao.datMacDinh(maKH, maDC);
        if (result) {
            refreshData(maKH);
        }

        return result;
    }

    // ================= GET MẶC ĐỊNH =================
    public KhachHang_DiaChi_DTO getMacDinh(String maKH) {

        ArrayList<KhachHang_DiaChi_DTO> ds = khdcDao.getByMaKH(maKH);

        for (KhachHang_DiaChi_DTO dto : ds) {
            if (dto.getTrangThai() == 1) {
                return dto;
            }
        }

        return null;
    }

    // ================= REFRESH =================
    public void refreshData(String maKH) {
        listCache = khdcDao.getByMaKH(maKH);
    }
}