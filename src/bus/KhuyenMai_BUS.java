package bus;

import dao.KhuyenMai_DAO;
import dto.ChuongTrinhKM_DTO;
import dto.KhuyenMai_DTO;
import dto.Voucher_DTO;

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
        if (maKM == null || maKM.trim().isEmpty()) return null;
        if (listCache == null) refreshData();
        for (KhuyenMai_DTO km : listCache) {
            if (km.getMaKM().equals(maKM)) return km;
        }
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

    public boolean them(KhuyenMai_DTO km, int soLuotSuDung) {
        if (!kiemTraHopLe(km)) return false;

        if (soLuotSuDung <= 0) {
            JOptionPane.showMessageDialog(null, "Số lượt sử dụng cho khách hàng phải lớn hơn 0!");
            return false;
        }

        boolean resultKM = kmDao.them(km);

        if (resultKM) {
            boolean resultDistribute = KhachHang_KM_BUS.getInstance()
                    .phanPhoiToanHeThong(km.getMaKM(), soLuotSuDung);

            if (resultDistribute) {
                refreshData();
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Lỗi khi phân phối lượt dùng cho khách hàng!");

                refreshData();
                return false;
            }
        }
        return false;
    }

    public boolean capNhat(KhuyenMai_DTO km) {
        if (!kiemTraHopLe(km)) return false;
        boolean result = kmDao.capNhat(km);
        if (result) refreshData();
        return result;
    }
    public boolean capNhatTrangThaiTheoCTKM(String maCT, int trangThaiMoi) {
        boolean result = kmDao.capNhatTrangThaiTheoCTKM(maCT, trangThaiMoi);
        if (result) refreshData();
        return result;
    }

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

    public ArrayList<KhuyenMai_DTO> getDSKMTheoSP(String maSP, String maDanhMuc) {
        ArrayList<KhuyenMai_DTO> result = new ArrayList<>();
        ChuongTrinhKM_BUS ctkmBus = ChuongTrinhKM_BUS.getInstance();

        for (KhuyenMai_DTO km : getAll()) {
            ChuongTrinhKM_DTO ct = ctkmBus.getById(km.getMaChuongTrinh());

            if (!km.getTrangThaiThucTe(ct).equals(KhuyenMai_DTO.DANG_AP_DUNG)) {
                continue;
            }

            if (km.getDoiTuongApDung() == KhuyenMai_DTO.DT_SAN_PHAM) {
                if (km.getMaSanPham().equals(maSP)) result.add(km);
            } else if (km.getDoiTuongApDung() == KhuyenMai_DTO.DT_DANH_MUC) {
                if (km.getMaDanhMuc().equals(maDanhMuc)) result.add(km);
            }
        }
        return result;
    }

    public ArrayList<KhuyenMai_DTO> getDSKMSapXepTotNhat(String maSP, String maDanhMuc, double giaBan) {
        ArrayList<KhuyenMai_DTO> list = getDSKMTheoSP(maSP, maDanhMuc);

        list.sort((km1, km2) -> {
            double giam1 = tinhTienGiam(km1, giaBan);
            double giam2 = tinhTienGiam(km2, giaBan);
            return Double.compare(giam2, giam1);
        });

        return list;
    }

    public KhuyenMai_DTO getByTen(String tenKM) {
        for (KhuyenMai_DTO km : getAll()) {
            if (km.getTenKM().equalsIgnoreCase(tenKM)) return km;
        }
        return null;
    }

    public double tinhTienGiam(KhuyenMai_DTO km, double giaBan) {
        if (km.getLoaiKhuyenMai() == KhuyenMai_DTO.LOAI_PHAN_TRAM) {
            return giaBan * km.getGiaTriKhuyenMai();
        } else {
            return km.getGiaTriKhuyenMai();
        }
    }

}