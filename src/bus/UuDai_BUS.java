package bus;

import dao.UuDai_DAO;
import dto.UuDai_DTO;

import java.util.ArrayList;
import java.util.Date;

public class UuDai_BUS {
    private static UuDai_BUS instance;
    private final UuDai_DAO udDao = new UuDai_DAO();
    private ArrayList<UuDai_DTO> listCache;

    // Singleton Pattern
    private UuDai_BUS() {
        // Load dữ liệu ngay khi khởi tạo
        refreshData();
    }

    public static UuDai_BUS getInstance() {
        if (instance == null) {
            instance = new UuDai_BUS();
        }
        return instance;
    }

    // 1. Lấy toàn bộ danh sách (cả cũ và mới)
    public ArrayList<UuDai_DTO> getAll() {
        if (listCache == null) {
            refreshData();
        }
        return listCache;
    }

    // 2. Lấy danh sách ưu đãi ĐANG DIỄN RA (Còn hiệu lực ngày giờ và Trạng thái = 1)
    public ArrayList<UuDai_DTO> getUuDaiDangDienRa() {
        ArrayList<UuDai_DTO> result = new ArrayList<>();
        Date today = new Date(); // Ngày hiện tại

        if (listCache == null) refreshData();

        for (UuDai_DTO ud : listCache) {
            // Điều kiện: Trạng thái hoạt động (1)
            // VÀ Ngày bắt đầu <= Hôm nay <= Ngày kết thúc
            if (ud.getTrangThai() == 1 &&
                    ud.getNgayBatDau().compareTo(today) <= 0 &&
                    ud.getNgayKetThuc().compareTo(today) >= 0) {

                result.add(ud);
            }
        }
        return result;
    }

    // 3. Phân loại riêng: Chỉ lấy Voucher
    public ArrayList<UuDai_DTO> getOnlyVoucher() {
        ArrayList<UuDai_DTO> result = new ArrayList<>();
        for (UuDai_DTO ud : listCache) {
            // Giả sử mã Voucher bắt đầu bằng "VC"
            if (ud.getMa().startsWith("VC")) {
                result.add(ud);
            }
        }
        return result;
    }

    // 4. Phân loại riêng: Chỉ lấy Chương trình khuyến mãi
    public ArrayList<UuDai_DTO> getOnlyCTKM() {
        ArrayList<UuDai_DTO> result = new ArrayList<>();
        for (UuDai_DTO ud : listCache) {
            // Giả sử mã CTKM bắt đầu bằng "CTKM"
            if (ud.getMa().startsWith("CTKM")) {
                result.add(ud);
            }
        }
        return result;
    }

    // 5. Làm mới dữ liệu từ Database
    public void refreshData() {
        listCache = udDao.getAll();
    }
}