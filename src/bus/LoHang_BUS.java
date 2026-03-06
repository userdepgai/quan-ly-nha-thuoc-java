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

    public ArrayList<LoHang_DTO> getLoConBanByMaSP(String maSp) {
        ArrayList<LoHang_DTO> result = new ArrayList<>();
        if (maSp == null || maSp.isEmpty()) return result;
        LocalDate today = LocalDate.now();
        for (LoHang_DTO lo : listCache) {
            if (lo.getMaSp().equals(maSp)
                    && lo.getSoLuongConLai() > 0
                    && lo.getTrangThai() == 1
                    && (lo.getHsd().isAfter(today) || lo.getHsd().isEqual(today))) {

                result.add(lo);
            }
        }
        sapXepTheoHSDTangDan(result);
        return result;
    }
    private void sapXepTheoHSDTangDan(ArrayList<LoHang_DTO> list) {
        list.sort((o1, o2) -> o1.getHsd().compareTo(o2.getHsd()));
    }

    public boolean kiemTraDuTon(String maSp, int soLuongCanBan) {
        if (soLuongCanBan <= 0) return false;
        int tongTon = getTongTonByMaSP(maSp);
        return tongTon >= soLuongCanBan;
    }

    public Map<LoHang_DTO, Integer> phanBoLoDeBan(String maSp, int soLuongCanBan) {
        Map<LoHang_DTO, Integer> result = new LinkedHashMap<>();
        if (!kiemTraDuTon(maSp, soLuongCanBan)) {
            return result;
        }
        ArrayList<LoHang_DTO> dsLo = getLoConBanByMaSP(maSp);
        int soLuongConLaiCanBan = soLuongCanBan;
        for (LoHang_DTO lo : dsLo) {
            if (soLuongConLaiCanBan <= 0) break;
            int ton = lo.getSoLuongConLai();
            if (ton <= 0) continue;
            int soLuongTru = Math.min(ton, soLuongConLaiCanBan);
            result.put(lo, soLuongTru);
            soLuongConLaiCanBan -= soLuongTru;
        }
        return result;
    }

    public void congTonKhiHuy(Map<LoHang_DTO, Integer> dsLo) {

        if (dsLo == null || dsLo.isEmpty()) return;

        for (Map.Entry<LoHang_DTO, Integer> entry : dsLo.entrySet()) {

            LoHang_DTO lo = entry.getKey();
            int soLuongCong = entry.getValue();

            lo.congSoLuongConLai(soLuongCong);
        }

        refreshData();
    }

    public int getTongTonByMaSP(String maSp) {

        int tong = 0;

        LocalDate today = LocalDate.now();

        for (LoHang_DTO lo : listCache) {
            if (lo.getMaSp().equals(maSp)
                    && lo.getSoLuongConLai() > 0
                    && lo.getTrangThai() == 1
                    && (lo.getHsd().isAfter(today) || lo.getHsd().isEqual(today))) {

                tong += lo.getSoLuongConLai();
            }
        }

        return tong;
    }

    public ArrayList<LoHang_DTO> getLoHetHan() {

        ArrayList<LoHang_DTO> result = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (LoHang_DTO lo : listCache) {
            if (lo.getHsd().isBefore(today)
                    && lo.getSoLuongConLai() > 0) {

                result.add(lo);
            }
        }

        return result;
    }
    public ArrayList<LoHang_DTO> getByMaSP(String maSp) {

        ArrayList<LoHang_DTO> result = new ArrayList<>();

        if (maSp == null || maSp.isEmpty()) return result;

        for (LoHang_DTO lo : listCache) {
            if (lo.getMaSp().equals(maSp)) {
                result.add(lo);
            }
        }

        return result;
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
    public ArrayList<LoHang_DTO> getLoConBanByMaSP(String maSp) {
        ArrayList<LoHang_DTO> result = new ArrayList<>();
        if (maSp == null || maSp.isEmpty()) return result;
        LocalDate today = LocalDate.now();
        for (LoHang_DTO lo : listCache) {
            if (lo.getMaSp().equals(maSp)
                    && lo.getSoLuongConLai() > 0
                    && lo.getTrangThai() == 1
                    && (lo.getHsd().isAfter(today) || lo.getHsd().isEqual(today))) {

                result.add(lo);
            }
        }
        sapXepTheoHSDTangDan(result);
        return result;
    }
    private void sapXepTheoHSDTangDan(ArrayList<LoHang_DTO> list) {
        list.sort((o1, o2) -> o1.getHsd().compareTo(o2.getHsd()));
    }

    public boolean kiemTraDuTon(String maSp, int soLuongCanBan) {
        if (soLuongCanBan <= 0) return false;
        int tongTon = getTongTonByMaSP(maSp);
        return tongTon >= soLuongCanBan;
    }

    public Map<LoHang_DTO, Integer> phanBoLoDeBan(String maSp, int soLuongCanBan) {
        Map<LoHang_DTO, Integer> result = new LinkedHashMap<>();
        if (!kiemTraDuTon(maSp, soLuongCanBan)) {
            return result;
        }
        ArrayList<LoHang_DTO> dsLo = getLoConBanByMaSP(maSp);
        int soLuongConLaiCanBan = soLuongCanBan;
        for (LoHang_DTO lo : dsLo) {
            if (soLuongConLaiCanBan <= 0) break;
            int ton = lo.getSoLuongConLai();
            if (ton <= 0) continue;
            int soLuongTru = Math.min(ton, soLuongConLaiCanBan);
            result.put(lo, soLuongTru);
            soLuongConLaiCanBan -= soLuongTru;
        }
        return result;
    }

    public void congTonKhiHuy(Map<LoHang_DTO, Integer> dsLo) {

        if (dsLo == null || dsLo.isEmpty()) return;

        for (Map.Entry<LoHang_DTO, Integer> entry : dsLo.entrySet()) {

            LoHang_DTO lo = entry.getKey();
            int soLuongCong = entry.getValue();

            lo.congSoLuongConLai(soLuongCong);
        }

        refreshData();
    }

    public int getTongTonByMaSP(String maSp) {

        int tong = 0;

        LocalDate today = LocalDate.now();

        for (LoHang_DTO lo : listCache) {
            if (lo.getMaSp().equals(maSp)
                    && lo.getSoLuongConLai() > 0
                    && lo.getTrangThai() == 1
                    && (lo.getHsd().isAfter(today) || lo.getHsd().isEqual(today))) {

                tong += lo.getSoLuongConLai();
            }
        }

        return tong;
    }
    public boolean kiemTraHopLe(LoHang_DTO lo) {

        if (lo.getGiaNhap() <= 0) {
            JOptionPane.showMessageDialog(null, "Giá nhập phải lớn hơn 0");
            return false;
        }

        if (lo.getSoLuongNhap() <= 0) {
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
    public double getGiaNhapCaoNhatTrongLoChon(Map<LoHang_DTO, Integer> dsLoDaChon) {
        if (dsLoDaChon == null || dsLoDaChon.isEmpty()) {
            return -1;
        }
        double maxGiaNhap = Double.MIN_VALUE;
        for (LoHang_DTO lo : dsLoDaChon.keySet()) {
            maxGiaNhap = Math.max(maxGiaNhap, lo.getGiaNhap());
        }
        return maxGiaNhap;
    }
    public void refreshData() {
        listCache = dao.getAll();
    }
}