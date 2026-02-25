package bus;

import dao.*;
import dto.*;

import javax.swing.*;
import java.util.*;

public class TaiKhoan_BUS {
    private static TaiKhoan_BUS instance;
    private TaiKhoan_DAO dao = new TaiKhoan_DAO();
    private ArrayList<TaiKhoan_DTO> listCache;

    private PhanQuyen_BUS phanQuyenBus = PhanQuyen_BUS.getInstance();

    private TaiKhoan_BUS() {
        listCache = dao.getAll();
    }
    public static TaiKhoan_BUS getInstance() {
        if (instance == null) {
            instance = new TaiKhoan_BUS();
        }
        return instance;
    }

    public ArrayList<TaiKhoan_DTO> getAll() {
        return dao.getAll();
    }

    public String getNextID() {
        return dao.getNextID();
    }

    public boolean them(TaiKhoan_DTO tk) {
        boolean result = dao.them(tk);
        if(result) refreshData();
        return result;
    }

    public boolean capNhat(TaiKhoan_DTO tk) {
        boolean result = dao.capNhat(tk);
        if(result) refreshData();
        return result;
    }

    public String getTenQuyen(String maQuyen) {
        PhanQuyen_DTO quyen = phanQuyenBus.getById(maQuyen);
        return quyen != null ? quyen.getTenQuyen() : "Không xác định";
    }
    public TaiKhoan_DTO getBySDT(String sdt) {
        for(TaiKhoan_DTO tk : listCache) {
            if(tk.getSdt().equals(sdt))
                return tk;
        }
        return null;
    }
    public ArrayList<TaiKhoan_DTO> timKiem(String keyWord,String quyen, Integer trangThai) {
        ArrayList<TaiKhoan_DTO> result = new ArrayList<>();
        keyWord = keyWord.toLowerCase();
        for (TaiKhoan_DTO tk : listCache) {
            boolean matchTrangThai = (trangThai == null || tk.getTrangThai() == trangThai);
            boolean matchQuyen = (quyen == null || phanQuyenBus.getByName(quyen).getMaQuyen().equals(tk.getMaQuyen()));
            boolean matchKeyWord = tk.getSdt().toLowerCase().contains(keyWord) || keyWord == null;
            if ( matchKeyWord && matchQuyen && matchTrangThai)
                result.add(tk);
        }
        return null;
    }

    public ArrayList<TaiKhoan_DTO> dangNhap(String sdt, String matKhau) {
        ArrayList<TaiKhoan_DTO> result = new ArrayList<>();
        for (TaiKhoan_DTO tk : listCache) {
            if (tk.getSdt().equals(sdt)
                    && tk.getMatKhau().equals(matKhau)
                    && tk.getTrangThai() == 1) {
                result.add(tk);
            }
        }
        return result;
    }

    public boolean kiemTraHopLe(TaiKhoan_DTO tk) {

        if (tk.getSdt() == null || tk.getSdt().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không được để trống");
            return false;
        }
        if (!tk.getSdt().matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(null, "Số điện thoại phải gồm 10 số và bắt đầu bằng 0");
            return false;
        }
        if (tk.getMatKhau() == null || tk.getMatKhau().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mật khẩu không được để trống");
            return false;
        }
        if (tk.getMatKhau().length() < 6) {
            JOptionPane.showMessageDialog(null, "Mật khẩu phải có ít nhất 6 ký tự");
            return false;
        }
        if (tk.getTrangThai() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn trạng thái");
            return false;
        }
        if (tk.getMaQuyen() == null || tk.getMaQuyen().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn quyền");
            return false;
        }
        if (tk.getNgayKichHoat() == null) {
            JOptionPane.showMessageDialog(null, "Ngày kích hoạt không được để trống");
            return false;
        }

        return true;
    }
    public void refreshData() {
        listCache = dao.getAll();
    }
}
