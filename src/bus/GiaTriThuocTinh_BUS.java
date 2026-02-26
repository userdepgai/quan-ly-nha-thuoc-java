package bus;

import dao.GiaTriThuocTinh_DAO;
import dto.GiaTriThuocTinh_DTO;

import javax.swing.JOptionPane;
import java.util.ArrayList;

public class GiaTriThuocTinh_BUS {

    // Áp dụng Singleton Pattern
    private static GiaTriThuocTinh_BUS instance;
    private final GiaTriThuocTinh_DAO gtDao = new GiaTriThuocTinh_DAO();

    // Danh sách lưu trữ tạm (Cache) để tăng tốc độ truy xuất
    private ArrayList<GiaTriThuocTinh_DTO> listCache;

    // Constructor Private - Load dữ liệu lần đầu khi khởi tạo
    private GiaTriThuocTinh_BUS() {
        listCache = gtDao.getAll();
    }

    // Lấy Instance duy nhất của lớp
    public static GiaTriThuocTinh_BUS getInstance() {
        if (instance == null) {
            instance = new GiaTriThuocTinh_BUS();
        }
        return instance;
    }

    // 1. Lấy toàn bộ danh sách từ Cache
    public ArrayList<GiaTriThuocTinh_DTO> getAll() {
        return listCache;
    }

    // 2. Lấy mã tự động tiếp theo từ DB
    public String getNextId() {
        return gtDao.getNextId();
    }

    // 3. Lấy Giá trị thuộc tính theo Mã Giá Trị (Lọc từ Cache)
    public GiaTriThuocTinh_DTO getById(String maGiaTri) {
        if (maGiaTri == null) return null;
        for (GiaTriThuocTinh_DTO gt : listCache) {
            if (gt.getMaGiaTri().equals(maGiaTri)) {
                return gt;
            }
        }
        return null;
    }

    // 4. LẤY DANH SÁCH GIÁ TRỊ THEO MÃ THUỘC TÍNH (Dùng cho bảng con)
    // Cực nhanh vì lọc trực tiếp từ Cache, không gọi DAO
    public ArrayList<GiaTriThuocTinh_DTO> getByMaThuocTinh(String maThuocTinh) {
        ArrayList<GiaTriThuocTinh_DTO> result = new ArrayList<>();
        if (maThuocTinh == null || maThuocTinh.trim().isEmpty()) {
            return result;
        }

        for (GiaTriThuocTinh_DTO gt : listCache) {
            if (gt.getMaThuocTinh().equals(maThuocTinh)) {
                result.add(gt);
            }
        }
        return result;
    }

    // 5. Thêm Giá trị thuộc tính mới
    public boolean them(GiaTriThuocTinh_DTO gt) {
        if (!kiemTraHopLe(gt)) return false;

        boolean result = gtDao.them(gt);
        if (result) {
            refreshData(); // Load lại Cache nếu thêm thành công vào CSDL
        }
        return result;
    }

    // 6. Cập nhật Giá trị thuộc tính
    public boolean capNhat(GiaTriThuocTinh_DTO gt) {
        if (!kiemTraHopLe(gt)) return false;

        boolean result = gtDao.capNhat(gt);
        if (result) {
            refreshData(); // Load lại Cache nếu sửa thành công trong CSDL
        }
        return result;
    }

    // 7. Ràng buộc dữ liệu hợp lệ trước khi thao tác DB
    public boolean kiemTraHopLe(GiaTriThuocTinh_DTO gt) {
        if (gt.getNdGiaTri() == null || gt.getNdGiaTri().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nội dung giá trị không được để trống!", "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (gt.getMaThuocTinh() == null || gt.getMaThuocTinh().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã thuộc tính tham chiếu không được để trống!", "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // 8. Refresh data từ DB lên Cache
    public void refreshData() {
        listCache = gtDao.getAll();
    }
}