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

    // Nghiệp vụ: Phân phối Voucher cho toàn bộ khách hàng khi tạo mã mới trên GUI
    public boolean phanPhoiToanHeThong(String maVoucher, int soLuot) {
        if (maVoucher == null || maVoucher.isEmpty() || soLuot <= 0) return false;
        return khvDAO.phanPhoiVoucherToanHeThong(maVoucher, soLuot);
    }

    // Nghiệp vụ: Kiểm tra khách hàng có còn lượt dùng mã này không
    public boolean conLuotSuDung(String maVoucher, String maKH) {
        ArrayList<KhachHang_Voucher_DTO> list = khvDAO.getByMaKH(maKH);
        for (KhachHang_Voucher_DTO item : list) {
            if (item.getMaVoucher().equals(maVoucher)) {
                return item.getSoLuotConLai() > 0;
            }
        }
        return false;
    }

    // Nghiệp vụ: Trừ 1 lượt sử dụng sau khi khách hàng thanh toán hóa đơn thành công
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

    // Lấy số lượt còn lại để hiển thị lên form (nếu cần)
    public int getSoLuotConLai(String maVoucher, String maKH) {
        ArrayList<KhachHang_Voucher_DTO> list = khvDAO.getByMaKH(maKH);
        for (KhachHang_Voucher_DTO item : list) {
            if (item.getMaVoucher().equals(maVoucher)) {
                return item.getSoLuotConLai();
            }
        }
        return 0;
    }
    // Trong KhachHang_Voucher_BUS.java
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