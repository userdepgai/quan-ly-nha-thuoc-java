package bus;

import dao.KhachHang_Voucher_DAO;
import dto.KhachHang_Voucher_DTO;
import java.util.ArrayList;

public class KhachHang_Voucher_BUS {
    private static KhachHang_Voucher_BUS instance;
    private final KhachHang_Voucher_DAO khvDAO = KhachHang_Voucher_DAO.getInstance();

    public static KhachHang_Voucher_BUS getInstance() {
        if (instance == null) {
            instance = new KhachHang_Voucher_BUS();
        }
        return instance;
    }

    public boolean phanPhoiToanHeThong(String maVoucher, int soLuot) {
        if (maVoucher == null || maVoucher.isEmpty() || soLuot <= 0) return false;
        return khvDAO.phanPhoiVoucherToanHeThong(maVoucher, soLuot);
    }

    public boolean conLuotSuDung(String maVoucher, String maKH) {
        ArrayList<KhachHang_Voucher_DTO> list = khvDAO.getByMaKH(maKH);
        for (KhachHang_Voucher_DTO item : list) {
            if (item.getMaVoucher().equals(maVoucher)) {
                return item.getSoLuotConLai() > 0;
            }
        }
        return false;
    }

    public boolean truLuotSuDung(String maVoucher, String maKH) {
        ArrayList<KhachHang_Voucher_DTO> list = khvDAO.getByMaKH(maKH);
        for (KhachHang_Voucher_DTO item : list) {
            if (item.getMaVoucher().equals(maVoucher)) {
                if (item.getSoLuotConLai() > 0) {
                    return khvDAO.capNhatLuotDung(maVoucher, maKH, item.getSoLuotConLai() - 1);
                }
            }
        }
        return false;
    }

    public int getSoLuotConLai(String maVoucher, String maKH) {
        ArrayList<KhachHang_Voucher_DTO> list = khvDAO.getByMaKH(maKH);
        for (KhachHang_Voucher_DTO item : list) {
            if (item.getMaVoucher().equals(maVoucher)) {
                return item.getSoLuotConLai();
            }
        }
        return 0;
    }

    public int getSoLuotToiDa(String maVoucher, String maKH) {
        ArrayList<KhachHang_Voucher_DTO> list = khvDAO.getByMaKH(maKH);
        for (KhachHang_Voucher_DTO item : list) {
            if (item.getMaVoucher().equals(maVoucher)) {
                return item.getSoLuotToiDa();
            }
        }
        return 0;
    }
}