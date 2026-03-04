package bus;

import dao.KhachHang_DAO;
import dto.KhachHang_DTO;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class KhachHang_BUS {

    private static KhachHang_BUS instance;
    private KhachHang_DAO khDao = new KhachHang_DAO();
    private ArrayList<KhachHang_DTO> listCache;

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
            default: return 1; // Đồng
        }
    }

    // ================= GET ALL =================
    public ArrayList<KhachHang_DTO> getAll() {
        return listCache;
    }

    // ================= NEXT ID =================
    public String getNextId() {
        return khDao.getNextId();
    }

    // ================= THÊM =================
    public boolean them(KhachHang_DTO kh) {

        kh.setDiemThuong(0);      // mặc định
        kh.setDiemHang(0);        // mặc định

        boolean result = khDao.them(kh);
        if(result) refreshData();
        return result;
    }

    // ================= CẬP NHẬT =================
    public boolean capNhat(KhachHang_DTO kh) {

        boolean result = khDao.capNhat(kh);
        if(result) refreshData();
        return result;
    }

    // ================= VALIDATE =================
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

    // ================= TÌM KIẾM =================
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

                default: // Tất cả
                    if (kh.getMa().toLowerCase().contains(keyword)
                            || kh.getTen().toLowerCase().contains(keyword)
                            || kh.getSdt().contains(keyword))
                        result.add(kh);
            }
        }

        return result;
    }

    // ================= GET BY ID =================
    public KhachHang_DTO getById(String maKH) {

        for (KhachHang_DTO kh : listCache) {
            if (kh.getMa().equals(maKH))
                return kh;
        }
        return null;
    }
    // ================= GET BY SDT =================
    public KhachHang_DTO getBysdt(String sdt) {

        for (KhachHang_DTO kh : listCache) {
            if (kh.getSdt().equals(sdt))
                return kh;
        }
        return null;
    }
    // ================= REFRESH =================
    public void refreshData() {
        listCache = khDao.getAll();
    }
    private String tinhHang(double diemHang) {
        if (diemHang >= 3000) return "Kim cương";
        if (diemHang >= 1000) return "Vàng";
        if (diemHang >= 300) return "Bạc";
        return "Đồng";
    }
    public void congDiemMuaHang(String maKH, double tongTien) {

        KhachHang_DTO kh = getById(maKH);
        if (kh == null) return;

        // Làm tròn xuống bội số 10.000
        double tienLamTron = Math.floor(tongTien / 10000) * 10000;

        int heSo = getHeSoDiem(kh.getHang());

        double diemCong = (tienLamTron / 10000) * heSo;

        // Cộng điểm thưởng
        kh.setDiemThuong(kh.getDiemThuong() + diemCong);

        // Cộng điểm hạng (không bao giờ trừ)
        kh.setDiemHang(kh.getDiemHang() + diemCong);

        // Xét lại hạng
        kh.setHang(tinhHang(kh.getDiemHang()));

        khDao.capNhat(kh);
        refreshData();
    }
    public boolean truDiemThuong(String maKH, double diemCanTru) {

        KhachHang_DTO kh = getById(maKH);
        if (kh == null) return false;

        if (diemCanTru <= 0) return false;

        if (kh.getDiemThuong() < diemCanTru) {
            JOptionPane.showMessageDialog(null, "Không đủ điểm thưởng để sử dụng");
            return false;
        }

        // Trừ điểm thưởng
        kh.setDiemThuong(kh.getDiemThuong() - diemCanTru);

        // ❌ KHÔNG trừ điểm hạng
        // Điểm hạng là tích lũy vĩnh viễn

        khDao.capNhat(kh);
        refreshData();

        return true;
    }
    public double quyDoiTien(double diemThuong) {
        return (diemThuong / 500) * 10000;
    }
}