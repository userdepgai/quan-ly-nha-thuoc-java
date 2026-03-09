package bus;

import dao.QuyCach_DAO;
import dto.QuyCach_DTO;

import javax.swing.JOptionPane;
import java.util.ArrayList;

public class QuyCach_BUS {

    private static QuyCach_BUS instance;
    private final QuyCach_DAO qcDao = new QuyCach_DAO();
    private ArrayList<QuyCach_DTO> listCache;

    private QuyCach_BUS() {
        listCache = qcDao.getAll();
    }

    public static QuyCach_BUS getInstance() {
        if (instance == null) {
            instance = new QuyCach_BUS();
        }
        return instance;
    }

    public ArrayList<QuyCach_DTO> getAll() {
        return listCache;
    }

    public String getNextId() {
        return qcDao.getNextId();
    }

    public QuyCach_DTO getById(String maQC) {
        if (maQC == null) return null;
        for (QuyCach_DTO qc : listCache) {
            if (qc.getMaQC().equals(maQC)) {
                return qc;
            }
        }
        return null;
    }

    public boolean them(QuyCach_DTO qc) {
        if (!kiemTraHopLe(qc)) return false;

        int tong = qc.getSlTrongHop() * qc.getSlHopTrongThung();
        qc.setSlspThung(tong);

        boolean result = qcDao.them(qc);
        if (result) {
            refreshData();
        }
        return result;
    }

    public boolean capNhat(QuyCach_DTO qc) {
        if (!kiemTraHopLe(qc)) return false;

        int tong = qc.getSlTrongHop() * qc.getSlHopTrongThung();
        qc.setSlspThung(tong);

        boolean result = qcDao.capNhat(qc);
        if (result) {
            refreshData();
        }
        return result;
    }

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

    public void refreshData() {
        listCache = qcDao.getAll();
    }
}