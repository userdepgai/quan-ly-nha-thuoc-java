package bus;

import dao.SanPham_DAO;
import dto.QuyCach_DTO;
import dto.SanPham_DTO;

import java.util.ArrayList;
import java.util.Comparator;

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
            refreshData();
        }
        return listSanPham;
    }

    public SanPham_DTO getById(String maSP) {
        for (SanPham_DTO sp : getAll()) {
            if (sp.getMaSP().equals(maSP)) {
                return sp;
            }
        }
        return null;
    }

    public SanPham_DTO getByTenSP(String tenSP) {
        if (tenSP == null || tenSP.trim().isEmpty()) return null;
        for (SanPham_DTO sp : getAll()) {
            if (sp.getTenSP().equalsIgnoreCase(tenSP.trim())) {
                return sp;
            }
        }
        return null;
    }

    public String getNextId() {
        return spDao.getNextId();
    }

    /**
     * HÀM LỌC TỔNG HỢP NÂNG CAO (Dùng cho cả Tìm kiếm và Gợi ý)
     * @param keyword: Từ khóa nhập vào
     * @param timTheo: "Tất cả", "Mã sản phẩm", "Tên sản phẩm"
     * @param maDM: Mã danh mục hoặc "Tất cả"
     * @param trangThai: 1 (Đang bán), 0 (Ngừng bán), null (Tất cả)
     * @param sortLoiNhuan: "Không sắp xếp", "Tăng dần", "Giảm dần"
     */
    public ArrayList<SanPham_DTO> timKiemNangCao(String keyword, String timTheo, String maDM, Integer trangThai, String sortLoiNhuan) {
        ArrayList<SanPham_DTO> result = new ArrayList<>();
        String key = (keyword == null) ? "" : keyword.toLowerCase().trim();

        for (SanPham_DTO sp : getAll()) {
            // 1. Lọc theo Từ khóa & Tiêu chí tìm kiếm
            boolean matchKey = false;
            if (timTheo.equals("Tất cả")) {
                matchKey = sp.getMaSP().toLowerCase().contains(key) || sp.getTenSP().toLowerCase().contains(key);
            } else if (timTheo.equals("Mã sản phẩm")) {
                matchKey = sp.getMaSP().toLowerCase().contains(key);
            } else { // Tên sản phẩm
                matchKey = sp.getTenSP().toLowerCase().contains(key);
            }

            // 2. Lọc theo Danh mục
            boolean matchDM = (maDM == null || maDM.equals("Tất cả") || (sp.getMaDM() != null && sp.getMaDM().equals(maDM)));

            // 3. Lọc theo Trạng thái
            boolean matchTT = (trangThai == null || sp.getTrangThai() == trangThai);

            // Kết hợp các điều kiện
            if (matchKey && matchDM && matchTT) {
                result.add(sp);
            }
        }

        // 4. Xử lý Sắp xếp theo Lợi nhuận (nếu có yêu cầu)
        if (sortLoiNhuan != null && !sortLoiNhuan.equals("Không sắp xếp")) {
            result.sort((sp1, sp2) -> {
                if (sortLoiNhuan.equals("Tăng dần")) {
                    return Double.compare(sp1.getLoiNhuan(), sp2.getLoiNhuan());
                } else {
                    return Double.compare(sp2.getLoiNhuan(), sp1.getLoiNhuan());
                }
            });
        }

        return result;
    }

    private String xuLyQuyCach(int slTrongHop, int slHopTrongThung) {
        for (QuyCach_DTO qc : qcBus.getAll()) {
            if (qc.getSlTrongHop() == slTrongHop && qc.getSlHopTrongThung() == slHopTrongThung) {
                return qc.getMaQC();
            }
        }
        String maQCMoi = qcBus.getNextId();
        int tongSL = slTrongHop * slHopTrongThung;
        QuyCach_DTO qcNew = new QuyCach_DTO(maQCMoi, slTrongHop, slHopTrongThung, tongSL);
        if (qcBus.them(qcNew)) return maQCMoi;
        return null;
    }

    public boolean them(SanPham_DTO sp, int slTrongHop, int slHopTrongThung) {
        if (!kiemTraHopLe(sp)) return false;
        if (getById(sp.getMaSP()) != null) return false;

        String maQC = xuLyQuyCach(slTrongHop, slHopTrongThung);
        if (maQC == null) return false;
        sp.setMaQC(maQC);

        boolean result = spDao.them(sp);
        if (result) {
            listSanPham.add(sp);
        }
        return result;
    }

    public boolean capNhat(SanPham_DTO sp, int slTrongHop, int slHopTrongThung) {
        if (!kiemTraHopLe(sp)) return false;

        String maQC = xuLyQuyCach(slTrongHop, slHopTrongThung);
        if (maQC == null) return false;
        sp.setMaQC(maQC);

        boolean result = spDao.capNhat(sp);
        if (result) {
            refreshData();
        }
        return result;
    }

    public void refreshData() {
        listSanPham = spDao.getAll();
    }

    private boolean kiemTraHopLe(SanPham_DTO sp) {
        if (sp.getTenSP() == null || sp.getTenSP().trim().isEmpty()) return false;
        if (sp.getLoiNhuan() < 0) return false;
        return true;
    }
    public ArrayList<String[]> getThuocTinhSP(String maSP){
        return spDao.getThuocTinhSP(maSP);
    }
    public double tinhGiaBan(String maSP, double giaNhap){
        SanPham_DTO sp = getById(maSP);
        QuyCach_DTO qc = qcBus.getById(sp.getMaQC());
        double giaMotSP = giaNhap / qc.getSlspThung();
        return giaMotSP *  sp.getLoiNhuan();
    }

}