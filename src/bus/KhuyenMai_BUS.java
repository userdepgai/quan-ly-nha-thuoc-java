package bus;

import dao.KhuyenMai_DAO;
import dto.KhuyenMai_DTO;

import javax.swing.*;
import java.util.ArrayList;

public class KhuyenMai_BUS {
    private static KhuyenMai_BUS instance;
    private final KhuyenMai_DAO kmDao = new KhuyenMai_DAO();
    private ArrayList<KhuyenMai_DTO> listCache;

    private KhuyenMai_BUS() {
        refreshData();
    }

    public static KhuyenMai_BUS getInstance() {
        if (instance == null) {
            instance = new KhuyenMai_BUS();
        }
        return instance;
    }

    public void refreshData() {
        listCache = kmDao.getAll();
    }

    public ArrayList<KhuyenMai_DTO> getAll() {
        if (listCache == null) refreshData();
        return listCache;
    }

    public String getNextId() {
        return kmDao.getNextId();
    }

    public KhuyenMai_DTO getById(String maKM) {
        // 1. Kiểm tra đầu vào
        if (maKM == null || maKM.trim().isEmpty()) {
            return null;
        }

        // 2. Tìm trong Cache trước (cho nhanh)
        if (listCache == null) refreshData();

        for (KhuyenMai_DTO km : listCache) {
            if (km.getMaKM().equals(maKM)) {
                return km;
            }
        }

        // 3. Nếu không tìm thấy trong Cache (trường hợp hiếm, do dữ liệu mới cập nhật), gọi xuống DAO
        return kmDao.getById(maKM);
    }

    public ArrayList<KhuyenMai_DTO> getByMaCTKM(String maCTKM) {
        ArrayList<KhuyenMai_DTO> result = new ArrayList<>();
        if(listCache == null) refreshData();
        for (KhuyenMai_DTO km : listCache) {
            if (km.getMaChuongTrinh().equals(maCTKM)) {
                result.add(km);
            }
        }
        return result;
    }

    public boolean them(KhuyenMai_DTO km) {
        if (!kiemTraHopLe(km)) return false;
        boolean result = kmDao.them(km);
        if (result) refreshData();
        return result;
    }

    public boolean capNhat(KhuyenMai_DTO km) {
        if (!kiemTraHopLe(km)) return false;
        boolean result = kmDao.capNhat(km);
        if (result) refreshData();
        return result;
    }

    // Hàm validate (QUAN TRỌNG: Sửa điều kiện check loại khuyến mãi)
    private boolean kiemTraHopLe(KhuyenMai_DTO km) {
        if (km.getTenKM() == null || km.getTenKM().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên khuyến mãi không được để trống!");
            return false;
        }
        if (km.getGiaTriKhuyenMai() <= 0) {
            JOptionPane.showMessageDialog(null, "Giá trị khuyến mãi phải lớn hơn 0!");
            return false;
        }

        if (km.getLoaiKhuyenMai() == 0 && km.getGiaTriKhuyenMai() > 1.0) {
            JOptionPane.showMessageDialog(null, "Khuyến mãi phần trăm không được quá 100%!");
            return false;
        }

        // Kiểm tra đối tượng áp dụng (Giữ nguyên vì doiTuongApDung vẫn là int)
        if (km.getDoiTuongApDung() == 1 && (km.getMaSanPham() == null || km.getMaSanPham().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn Sản phẩm áp dụng!");
            return false;
        }
        if (km.getDoiTuongApDung() == 0 && (km.getMaDanhMuc() == null || km.getMaDanhMuc().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn Danh mục áp dụng!");
            return false;
        }
        return true;
    }
}