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

    /**
     * HÀM THÊM MỚI KHUYẾN MÃI (Cập nhật logic phân phối cho Khách hàng)
     * @param km Đối tượng khuyến mãi
     * @param soLuotSuDung Số lượt tối đa cho MỖI khách hàng (Lấy từ txtSoLuotSuDung trên GUI)
     */
    public boolean them(KhuyenMai_DTO km, int soLuotSuDung) {
        // 1. Kiểm tra tính hợp lệ của thông tin khuyến mãi
        if (!kiemTraHopLe(km)) return false;

        // 2. Kiểm tra tính hợp lệ của số lượt sử dụng
        if (soLuotSuDung <= 0) {
            JOptionPane.showMessageDialog(null, "Số lượt sử dụng cho khách hàng phải lớn hơn 0!");
            return false;
        }

        // 3. Thực hiện thêm vào bảng KHUYENMAI
        boolean resultKM = kmDao.them(km);

        if (resultKM) {
            // 4. Nếu thêm KM thành công, thực hiện phân phối cho TẤT CẢ khách hàng
            // Gọi lớp BUS KhachHang_KM đã viết ở bước trước
            boolean resultDistribute = KhachHang_KM_BUS.getInstance()
                    .phanPhoiToanHeThong(km.getMaKM(), soLuotSuDung);

            if (resultDistribute) {
                refreshData(); // Cập nhật lại danh sách cache
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Lỗi khi phân phối lượt dùng cho khách hàng!");
                // Vẫn return true hoặc false tùy vào việc bạn có muốn hoàn tác (rollback) KM hay không.
                // Ở đây ta refreshData để đảm bảo UI đồng bộ.
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
            // Bước 1: Kiểm tra chương trình cha có đang chạy hay không
            ChuongTrinhKM_DTO ct = ctkmBus.getById(km.getMaChuongTrinh());
            if (ct == null || !ct.getTrangThaiText().equals(ChuongTrinhKM_DTO.DANG_AP_DUNG)) {
                continue; // Chương trình cha ngưng hoặc hết hạn -> bỏ qua
            }

            // Bước 2: Kiểm tra trạng thái của riêng mã KM đó
            if (km.getTrangThai() != KhuyenMai_DTO.TT_DANG_AP_DUNG) continue;

            // Bước 3: Kiểm tra đối tượng áp dụng
            if (km.getDoiTuongApDung() == KhuyenMai_DTO.DT_SAN_PHAM) {
                if (km.getMaSanPham().equals(maSP)) result.add(km);
            } else if (km.getDoiTuongApDung() == KhuyenMai_DTO.DT_DANH_MUC) {
                if (km.getMaDanhMuc().equals(maDanhMuc)) result.add(km);
            }
        }
        return result;
    }


    /**
     * 3. Lấy danh sách KM áp dụng cho SP, sắp xếp cái nào giảm nhiều nhất lên đầu
     */
    public ArrayList<KhuyenMai_DTO> getDSKMSapXepTotNhat(String maSP, String maDanhMuc, double giaBan) {
        ArrayList<KhuyenMai_DTO> list = getDSKMTheoSP(maSP, maDanhMuc);

        // Sắp xếp giảm dần theo số tiền được giảm
        list.sort((km1, km2) -> {
            double giam1 = tinhTienGiam(km1, giaBan);
            double giam2 = tinhTienGiam(km2, giaBan);
            return Double.compare(giam2, giam1);
        });

        return list;
    }



    /**
     * 5. Tìm KM theo tên
     */
    public KhuyenMai_DTO getByTen(String tenKM) {
        for (KhuyenMai_DTO km : getAll()) {
            if (km.getTenKM().equalsIgnoreCase(tenKM)) return km;
        }
        return null;
    }


    /**
     * 7. Hàm tính toán số tiền được giảm của 1 mã KM (Helper)
     */
    public double tinhTienGiam(KhuyenMai_DTO km, double giaBan) {
        if (km.getLoaiKhuyenMai() == KhuyenMai_DTO.LOAI_PHAN_TRAM) {
            return giaBan * km.getGiaTriKhuyenMai(); // Ví dụ: 100k * 0.05 = 5k
        } else {
            return km.getGiaTriKhuyenMai(); // Giảm thẳng tiền mặt
        }
    }

}