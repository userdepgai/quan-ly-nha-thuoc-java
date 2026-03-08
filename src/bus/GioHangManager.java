package bus;

import dto.ChiTietGioHang_DTO;
import java.util.ArrayList;
import java.util.List;

public class GioHangManager {
    public static List<ChiTietGioHang_DTO> danhSachGioHang = new ArrayList<>();
    public static void themGioHang(ChiTietGioHang_DTO spMoi) {
        boolean daTonTai = false;
        for (ChiTietGioHang_DTO item : danhSachGioHang) {
            if (item.getMaSP().equals(spMoi.getMaSP())) {
                item.setSoLuong(item.getSoLuong() + spMoi.getSoLuong());
                daTonTai = true;
                break;
            }
        }
        if (!daTonTai) {
            danhSachGioHang.add(spMoi);
        }
    }
}