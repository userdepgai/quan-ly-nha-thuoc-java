package bus;

import dao.*;
import dto.*;

import javax.swing.*;
import java.time.LocalDate;
import java.util.*;

public class LoHang_BUS {

    private static LoHang_BUS instance;
    private LoHang_DAO dao = new LoHang_DAO();
    private ArrayList<LoHang_DTO> listCache;

    private LoHang_BUS() {
        listCache = dao.getAll();
    }

    public static LoHang_BUS getInstance() {
        if (instance == null) {
            instance = new LoHang_BUS();
        }
        return instance;
    }

    public ArrayList<LoHang_DTO> getAll() {
        return dao.getAll();
    }

    public String getNextID() {
        return dao.getNextID();
    }

    public boolean them(LoHang_DTO lo) {
        boolean result = dao.them(lo);
        if (result) refreshData();
        return result;
    }

    public boolean capNhat(LoHang_DTO lo) {
        boolean result = dao.capNhat(lo);
        if (result) refreshData();
        return result;
    }

    public LoHang_DTO getById(String maLo) {
        for (LoHang_DTO lo : listCache) {
            if (lo.getMaLo().equals(maLo))
                return lo;
        }
        return null;
    }

    public ArrayList<LoHang_DTO> timKiem(String keyword, Integer trangThai) {
        ArrayList<LoHang_DTO> result = new ArrayList<>();

        for (LoHang_DTO lo : listCache) {
            boolean matchTrangThai = (trangThai == null || lo.getTrangThai() == trangThai);
            boolean matchKey = (keyword == null || keyword.isEmpty()
                    || lo.getMaLo().toLowerCase().contains(keyword.toLowerCase()));

            if (matchTrangThai && matchKey) {
                result.add(lo);
            }
        }

        return result;
    }

    public boolean kiemTraHopLe(LoHang_DTO lo) {

        if (lo.getGiaNhap() <= 0) {
            JOptionPane.showMessageDialog(null, "Giá nhập phải lớn hơn 0");
            return false;
        }

        if (lo.getSoLuong() <= 0) {
            JOptionPane.showMessageDialog(null, "Số lượng phải lớn hơn 0");
            return false;
        }

        if (lo.getSoLuongConLai() < 0) {
            JOptionPane.showMessageDialog(null, "Số lượng còn lại không hợp lệ");
            return false;
        }

        if (lo.getHsd() == null || lo.getHsd().isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(null, "Hạn sử dụng không hợp lệ");
            return false;
        }

        if (lo.getMaNcc() == null || lo.getMaNcc().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn nhà cung cấp");
            return false;
        }

        if (lo.getMaKvlt() == null || lo.getMaKvlt().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn khu vực lưu trữ");
            return false;
        }

        if (lo.getMaSp() == null || lo.getMaSp().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm");
            return false;
        }

        return true;
    }

    public void refreshData() {
        listCache = dao.getAll();
    }
}