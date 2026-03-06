package bus;

import dao.SanPham_DAO;
import dto.QuyCach_DTO;
import dto.SanPham_DTO;

import java.util.ArrayList;

public class SanPham_BUS {
    private static SanPham_BUS instance;
    private final SanPham_DAO spDao = new SanPham_DAO();

    private final QuyCach_BUS qcBus = QuyCach_BUS.getInstance();

    private ArrayList<SanPham_DTO> listSanPham;

    private SanPham_BUS() {
        this.listSanPham = spDao.getAll();
    }

    public static SanPham_BUS getInstance() {
        if (instance == null) {
            instance = new SanPham_BUS();
        }
        return instance;
    }

    /**
     * Lấy danh sách sản phẩm (từ Cache)
     */
    public ArrayList<SanPham_DTO> getAll() {
        if (listSanPham == null) {
            listSanPham = spDao.getAll();
        }
        return listSanPham;
    }

    /**
     * Lấy sản phẩm theo mã (tìm trong Cache cho nhanh)
     */
    public SanPham_DTO getById(String maSP) {
        for (SanPham_DTO sp : getAll()) {
            if (sp.getMaSP().equals(maSP)) {
                return sp;
            }
        }
        return null;
    }
    public SanPham_DTO getByTenSP(String tenSP) {
        if (tenSP == null || tenSP.trim().isEmpty()) {
            return null;
        }

        for (SanPham_DTO sp : getAll()) {
            if (sp.getTenSP().equalsIgnoreCase(tenSP.trim())) {
                return sp;
            }
        }
        return null;
    }

    /**
     * Lấy mã sản phẩm tiếp theo (Gọi DAO)
     */
    public String getNextId() {
        return spDao.getNextId();
    }

    /**
     * LOGIC NGHIỆP VỤ QUAN TRỌNG: Xử lý Quy Cách
     * 1. Tìm trong DB xem đã có quy cách (slTrongHop, slHopTrongThung) chưa.
     * 2. Nếu có -> Trả về MaQC.
     * 3. Nếu chưa -> Tạo mới QuyCach -> Trả về MaQC mới.
     */
    private String xuLyQuyCach(int slTrongHop, int slHopTrongThung) {
        // 1. Duyệt danh sách quy cách hiện có để tìm trùng
        for (QuyCach_DTO qc : qcBus.getAll()) {
            if (qc.getSlTrongHop() == slTrongHop && qc.getSlHopTrongThung() == slHopTrongThung) {
                return qc.getMaQC();
            }
        }

        // 2. Nếu chưa có, tạo mới
        String maQCMoi = qcBus.getNextId();
        // Tính tổng số lượng SP trong thùng = slTrongHop * slHopTrongThung
        int tongSL = slTrongHop * slHopTrongThung;

        QuyCach_DTO qcNew = new QuyCach_DTO(maQCMoi, slTrongHop, slHopTrongThung, tongSL);

        // Gọi BUS Quy Cách để thêm mới
        if (qcBus.them(qcNew)) {
            return maQCMoi;
        }

        return null; // Trả về null nếu lỗi tạo quy cách
    }

    /**
     * Thêm sản phẩm
     * @param sp Đối tượng sản phẩm (đã có thông tin cơ bản)
     * @param slTrongHop Số lượng trong hộp (lấy từ GUI)
     * @param slHopTrongThung Số hộp trong thùng (lấy từ GUI)
     */
    public boolean them(SanPham_DTO sp, int slTrongHop, int slHopTrongThung) {
        // 1. Kiểm tra dữ liệu hợp lệ
        if (!kiemTraHopLe(sp)) return false;

        // 2. Kiểm tra trùng mã
        if (getById(sp.getMaSP()) != null) return false;

        // 3. Xử lý logic Quy Cách để lấy được Mã QC
        String maQC = xuLyQuyCach(slTrongHop, slHopTrongThung);
        if (maQC == null) return false; // Không xử lý được quy cách thì không thêm SP

        sp.setMaQC(maQC); // Gán mã QC vào SP

        // 4. Gọi DAO thêm xuống DB
        boolean result = spDao.them(sp);

        // 5. Cập nhật Cache nếu thành công
        if (result) {
            listSanPham.add(sp);
        }
        return result;
    }

    /**
     * Cập nhật sản phẩm
     */
    public boolean capNhat(SanPham_DTO sp, int slTrongHop, int slHopTrongThung) {
        if (!kiemTraHopLe(sp)) return false;

        // 1. Xử lý quy cách (người dùng có thể thay đổi số lượng đóng gói)
        String maQC = xuLyQuyCach(slTrongHop, slHopTrongThung);
        if (maQC == null) return false;

        sp.setMaQC(maQC);

        // 2. Gọi DAO cập nhật
        boolean result = spDao.capNhat(sp);

        // 3. Cập nhật Cache: Reload lại toàn bộ hoặc sửa đối tượng trong list
        if (result) {
            refreshData(); // Cách an toàn nhất là load lại list
        }
        return result;
    }

    /**
     * Làm mới danh sách cache từ Database
     */
    public void refreshData() {
        listSanPham = spDao.getAll();
    }

    /**
     * Tìm kiếm và Lọc (Xử lý trên Ram/Cache)
     */
    public ArrayList<SanPham_DTO> timKiem(String keyword, String maDM, Integer trangThai) {
        ArrayList<SanPham_DTO> result = new ArrayList<>();
        String key = (keyword == null) ? "" : keyword.toLowerCase().trim();

        for (SanPham_DTO sp : getAll()) {
            // Điều kiện 1: Từ khóa (Mã hoặc Tên)
            boolean matchKey = sp.getTenSP().toLowerCase().contains(key)
                    || sp.getMaSP().toLowerCase().contains(key);

            // Điều kiện 2: Danh mục (Nếu null hoặc "Tất cả" thì bỏ qua)
            boolean matchDM = (maDM == null || maDM.equals("Tất cả") || (sp.getMaDM() != null && sp.getMaDM().equals(maDM)));

            // Điều kiện 3: Trạng thái (Nếu null thì bỏ qua)
            boolean matchTT = (trangThai == null || sp.getTrangThai() == trangThai);

            // Kết hợp AND
            if (matchKey && matchDM && matchTT) {
                result.add(sp);
            }
        }
        return result;
    }

    /**
     * Validate dữ liệu nghiệp vụ
     */
    private boolean kiemTraHopLe(SanPham_DTO sp) {
        if (sp.getTenSP() == null || sp.getTenSP().trim().isEmpty()) return false;
        if (sp.getLoiNhuan() < 0) return false;
        // Có thể thêm các validate khác ở đây
        return true;
    }
}