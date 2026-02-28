package bus;

import dao.SanPham_DAO;
import dto.QuyCach_DTO;
import dto.SanPham_DTO;

import javax.swing.JOptionPane;
import java.util.ArrayList;

public class SanPham_BUS {
    private static SanPham_BUS instance;
    private SanPham_DAO spDao = new SanPham_DAO();
    private ArrayList<SanPham_DTO> listCache;

    // Gọi QuyCach_BUS để xử lý quy cách
    private QuyCach_BUS qcBus = QuyCach_BUS.getInstance();

    private SanPham_BUS() {
        listCache = spDao.getAll();
    }

    public static SanPham_BUS getInstance() {
        if (instance == null) instance = new SanPham_BUS();
        return instance;
    }

    public ArrayList<SanPham_DTO> getAll() {
        return listCache;
    }

    public String getNextId() {
        return spDao.getNextId();
    }

    // Hàm xử lý logic tìm hoặc tạo quy cách
    private String xuLyQuyCach(int slTrongHop, int slHopTrongThung) {
        // 1. Tìm xem quy cách này có chưa
        for (QuyCach_DTO qc : qcBus.getAll()) {
            if (qc.getSlTrongHop() == slTrongHop && qc.getSlHopTrongThung() == slHopTrongThung) {
                return qc.getMaQC(); // Trả về mã QC đã tồn tại
            }
        }
        // 2. Nếu chưa có -> Tạo mới
        String maQCMoi = qcBus.getNextId();
        QuyCach_DTO qcNew = new QuyCach_DTO(maQCMoi, slTrongHop, slHopTrongThung, slTrongHop * slHopTrongThung);
        if (qcBus.them(qcNew)) {
            return maQCMoi;
        }
        return null;
    }

    // Thêm sản phẩm (Nhận thêm tham số quy cách từ GUI)
    public boolean themSanPham(SanPham_DTO sp, int slTrongHop, int slHopTrongThung) {
        if (!kiemTraHopLe(sp)) return false;

        // Xử lý quy cách trước
        String maQC = xuLyQuyCach(slTrongHop, slHopTrongThung);
        if (maQC == null) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tạo quy cách đóng gói!");
            return false;
        }
        sp.setMaQC(maQC); // Gán mã QC vào SP

        boolean result = spDao.them(sp);
        if (result) refreshData();
        return result;
    }

    // Cập nhật sản phẩm
    public boolean capNhatSanPham(SanPham_DTO sp, int slTrongHop, int slHopTrongThung) {
        if (!kiemTraHopLe(sp)) return false;

        // Xử lý quy cách (có thể người dùng đổi quy cách)
        String maQC = xuLyQuyCach(slTrongHop, slHopTrongThung);
        if (maQC == null) return false;
        sp.setMaQC(maQC);

        boolean result = spDao.capNhat(sp);
        if (result) refreshData();
        return result;
    }

    public void refreshData() {
        listCache = spDao.getAll();
    }

    // Lấy SP theo ID từ Cache
    public SanPham_DTO getById(String maSP) {
        for(SanPham_DTO sp : listCache) {
            if (sp.getMaSP().equals(maSP)) return sp;
        }
        return null;
    }

    // Tìm kiếm
    public ArrayList<SanPham_DTO> timKiem(String keyword, String maDM, Integer trangThai) {
        ArrayList<SanPham_DTO> result = new ArrayList<>();
        String key = (keyword == null) ? "" : keyword.toLowerCase();

        for (SanPham_DTO sp : listCache) {
            boolean matchKey = sp.getTenSP().toLowerCase().contains(key) || sp.getMaSP().toLowerCase().contains(key);
            boolean matchDM = (maDM == null || maDM.equals("Tất cả") || sp.getMaDM().equals(maDM));
            boolean matchTT = (trangThai == null || sp.getTrangThai() == trangThai);

            if (matchKey && matchDM && matchTT) result.add(sp);
        }
        return result;
    }

    private boolean kiemTraHopLe(SanPham_DTO sp) {
        if (sp.getTenSP().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên sản phẩm không được để trống");
            return false;
        }
        if (sp.getLoiNhuan() < 0) {
            JOptionPane.showMessageDialog(null, "Lợi nhuận không hợp lệ");
            return false;
        }
        return true;
    }
}