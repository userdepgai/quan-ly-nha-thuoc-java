package bus;

import dao.KhachHang_KM_DAO;
import dto.KhachHang_KM_DTO;
import java.util.ArrayList;

public class KhachHang_KM_BUS {
    private static KhachHang_KM_BUS instance;
    private final KhachHang_KM_DAO khkmDAO = KhachHang_KM_DAO.getInstance();

    public static KhachHang_KM_BUS getInstance() {
        if (instance == null) instance = new KhachHang_KM_BUS();
        return instance;
    }

    public boolean phanPhoiToanHeThong(String maKM, int soLuot) {
        if (maKM == null || maKM.isEmpty() || soLuot <= 0) return false;
        return khkmDAO.phanPhoiKMToanHeThong(maKM, soLuot);
    }

    public boolean truLuotSuDung(String maKM, String maKH) {
        ArrayList<KhachHang_KM_DTO> list = khkmDAO.getByMaKH(maKH);
        for (KhachHang_KM_DTO item : list) {
            if (item.getMaKM().equals(maKM)) {
                if (item.getSoLuotConLai() > 0) {
                    return khkmDAO.capNhatLuotDung(maKM, maKH, item.getSoLuotConLai() - 1);
                }
            }
        }
        return false;
    }

    public boolean conLuotSuDung(String maKM, String maKH) {
        ArrayList<KhachHang_KM_DTO> list = khkmDAO.getByMaKH(maKH);
        for (KhachHang_KM_DTO item : list) {
            if (item.getMaKM().equals(maKM)) {
                return item.getSoLuotConLai() > 0;
            }
        }
        return false;
    }

    public int getSoLuotToiDa(String maKM, String maKH) {
        ArrayList<KhachHang_KM_DTO> list = khkmDAO.getByMaKH(maKH);
        for (KhachHang_KM_DTO item : list) {
            if (item.getMaKM().equals(maKM)) {
                return item.getSoLuotToiDa();
            }
        }
        return 0;
    }
}