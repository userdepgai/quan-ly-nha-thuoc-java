package bus;

import dao.QuyCach_DAO;
import dto.QuyCach_DTO;

import javax.swing.JOptionPane;
import java.util.ArrayList;

public class QuyCach_BUS {

    private static QuyCach_BUS instance;
    private final QuyCach_DAO qcDao = new QuyCach_DAO();
    private ArrayList<QuyCach_DTO> listCache;

    // Constructor Private - Load dữ liệu lần đầu
    private QuyCach_BUS() {
        listCache = qcDao.getAll();
    }

    // Singleton Instance
    public static QuyCach_BUS getInstance() {
        if (instance == null) {
            instance = new QuyCach_BUS();
        }
        return instance;
    }

    // 1. Lấy danh sách từ Cache
    public ArrayList<QuyCach_DTO> getAll() {
        return listCache;
    }

    // 2. Lấy mã tiếp theo từ DB
    public String getNextId() {
        return qcDao.getNextId();
    }

    // 3. Lấy Quy cách theo ID từ Cache
    public QuyCach_DTO getById(String maQC) {
        if (maQC == null) return null;
        for (QuyCach_DTO qc : listCache) {
            if (qc.getMaQC().equals(maQC)) {
                return qc;
            }
        }
        return null;
    }

    // 4. Thêm quy cách (Có tính toán tự động)
    public boolean them(QuyCach_DTO qc) {
        if (!kiemTraHopLe(qc)) return false;

        // Logic tự động tính tổng số lượng trong thùng để đảm bảo nhất quán
        // SLSP_Thung = SLTrongHop * SLHopTrongThung
        int tong = qc.getSlTrongHop() * qc.getSlHopTrongThung();
        qc.setSlspThung(tong);

        boolean result = qcDao.them(qc);
        if (result) {
            refreshData(); // Reload Cache
        }
        return result;
    }

    // 5. Cập nhật quy cách
    public boolean capNhat(QuyCach_DTO qc) {
        if (!kiemTraHopLe(qc)) return false;

        // Tính toán lại tổng trước khi update
        int tong = qc.getSlTrongHop() * qc.getSlHopTrongThung();
        qc.setSlspThung(tong);

        boolean result = qcDao.capNhat(qc);
        if (result) {
            refreshData(); // Reload Cache
        }
        return result;
    }

    // 6. Kiểm tra dữ liệu hợp lệ
    public boolean kiemTraHopLe(QuyCach_DTO qc) {
        if (qc.getSlTrongHop() <= 0) {
            JOptionPane.showMessageDialog(null, "Số lượng trong hộp phải lớn hơn 0");
            return false;
        }
        if (qc.getSlHopTrongThung() <= 0) {
            JOptionPane.showMessageDialog(null, "Số lượng hộp trong thùng phải lớn hơn 0");
            return false;
        }
        return true;
    }

    // 7. Refresh data từ DB lên Cache
    public void refreshData() {
        listCache = qcDao.getAll();
    }
}