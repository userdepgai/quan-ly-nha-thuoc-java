package bus;

import dao.KhachHang_DAO;
import dto.KhachHang_DTO;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import dto.KhachHang_DiaChi_DTO;
import dto.DIACHI_DTO;
import dao.KhachHang_DiaChi_DAO;
import dao.DiaChi_DAO;
public class KhachHang_BUS {

    private static KhachHang_BUS instance;
    private KhachHang_DAO khDao = new KhachHang_DAO();
    private ArrayList<KhachHang_DTO> listCache;
    private KhachHang_DiaChi_DAO khDiaChiDAO = new KhachHang_DiaChi_DAO();
    private DiaChi_DAO diaChiDAO = new DiaChi_DAO();
    private KhachHang_BUS() {
        listCache = khDao.getAll();
    }

    public static KhachHang_BUS getInstance() {
        if (instance == null) {
            instance = new KhachHang_BUS();
        }
        return instance;
    }

    private int getHeSoDiem(String hang) {
        switch (hang) {
            case "Bạc": return 2;
            case "Vàng": return 3;
            case "Kim cương": return 5;
            default: return 1;
        }
    }

    public ArrayList<KhachHang_DTO> getAll() {
        return listCache;
    }

    public String getNextId() {
        return khDao.getNextId();
    }

    public boolean them(KhachHang_DTO kh) {

        kh.setDiemThuong(0);
        kh.setDiemHang(0);

        boolean result = khDao.them(kh);
        if(result) refreshData();
        return result;
    }

    public boolean capNhat(KhachHang_DTO kh) {

        boolean result = khDao.capNhat(kh);
        if(result) refreshData();
        return result;
    }

    public boolean kiemTraHopLe(KhachHang_DTO kh) {

        if (kh.getTen().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên khách hàng không được để trống");
            return false;
        }

        if (kh.getSdt().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không được để trống");
            return false;
        }

        if (!kh.getSdt().matches("\\d{10}")) {
            JOptionPane.showMessageDialog(null, "Số điện thoại phải 10 số");
            return false;
        }
        for (KhachHang_DTO k : listCache) {
            if (k.getSdt().equals(kh.getSdt()) && !k.getMa().equals(kh.getMa())) {
                JOptionPane.showMessageDialog(null, "Số điện thoại đã tồn tại");
                return false;
            }
        }

        if (kh.getNgaySinh() == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày sinh");
            return false;
        }

        if (kh.getNgaySinh().isAfter(LocalDate.now())) {
            JOptionPane.showMessageDialog(null, "Ngày sinh không hợp lệ");
            return false;
        }

        if (kh.getNgayDKThanhVien() == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày đăng ký");
            return false;
        }

        if (kh.getDiemThuong() < 0 || kh.getDiemHang() < 0) {
            JOptionPane.showMessageDialog(null, "Điểm không được âm");
            return false;
        }

        return true;
    }

    public ArrayList<KhachHang_DTO> timKiem(String keyword, String loai) {

        ArrayList<KhachHang_DTO> result = new ArrayList<>();
        if (keyword == null) keyword = "";
        keyword = keyword.toLowerCase();

        for (KhachHang_DTO kh : listCache) {

            switch (loai) {

                case "Mã KH":
                    if (kh.getMa().toLowerCase().contains(keyword))
                        result.add(kh);
                    break;

                case "Tên KH":
                    if (kh.getTen().toLowerCase().contains(keyword))
                        result.add(kh);
                    break;

                case "SĐT":
                    if (kh.getSdt().contains(keyword))
                        result.add(kh);
                    break;

                default:
                    if (kh.getMa().toLowerCase().contains(keyword)
                            || kh.getTen().toLowerCase().contains(keyword)
                            || kh.getSdt().contains(keyword))
                        result.add(kh);
            }
        }

        return result;
    }

    public KhachHang_DTO getById(String maKH) {

        for (KhachHang_DTO kh : listCache) {
            if (kh.getMa().equals(maKH))
                return kh;
        }
        return null;
    }

    public KhachHang_DTO getBysdt(String sdt) {

        for (KhachHang_DTO kh : listCache) {
            if (kh.getSdt().equals(sdt))
                return kh;
        }
        return null;
    }

    public void refreshData() {
        listCache = khDao.getAll();
    }

    private String tinhHang(int diemHang) {
        if (diemHang >= 3000) return "Kim cương";
        if (diemHang >= 1000) return "Vàng";
        if (diemHang >= 300) return "Bạc";
        return "Đồng";
    }

    public void congDiemMuaHang(String maKH, int diemCong) {

        KhachHang_DTO kh = getById(maKH);
        if (kh == null) return;

        kh.setDiemThuong(kh.getDiemThuong() + diemCong);

        kh.setDiemHang(kh.getDiemHang() + diemCong);

        kh.setHang(tinhHang(kh.getDiemHang()));

        khDao.capNhat(kh);
        refreshData();
    }

    public boolean truDiemThuong(String maKH, int diemCanTru) {

        KhachHang_DTO kh = getById(maKH);
        if (kh == null) return false;

        if (diemCanTru <= 0) return false;

        if (kh.getDiemThuong() < diemCanTru) {
            JOptionPane.showMessageDialog(null, "Không đủ điểm thưởng để sử dụng");
            return false;
        }

        kh.setDiemThuong(kh.getDiemThuong() - diemCanTru);

        khDao.capNhat(kh);
        refreshData();

        return true;
    }

    public double quyDoiTien(double diemThuong) {
        return (diemThuong / 500) * 10000;
    }
    public String getDiaChi(String maKH) {

        ArrayList<KhachHang_DiaChi_DTO> listKHDC = khDiaChiDAO.getByMaKH(maKH);

        if (listKHDC == null || listKHDC.isEmpty()) return "";

        ArrayList<DIACHI_DTO> listDiaChi = diaChiDAO.getAll();

        for (KhachHang_DiaChi_DTO khdc : listKHDC) {

            for (DIACHI_DTO dc : listDiaChi) {

                if (dc.getMaDiaChi().equals(khdc.getMaDiaChi())) {

                    return dc.getSoNha() + " " +
                            dc.getDuong() + ", " +
                            dc.getPhuong() + ", " +
                            dc.getTinh();
                }
            }
        }

        return "";
    }
}