package bus;

import dto.ProductItem;
import java.util.ArrayList;
import java.util.List;

public class GioHangManager {
    public static List<ProductItem> danhSachGioHang = new ArrayList<>();

    public static void themVaoGioHang(ProductItem spMoi) {
        for (ProductItem item : danhSachGioHang) {
            if (item.ma.equals(spMoi.ma)) {
                item.soLuong += spMoi.soLuong;
                return;
            }
        }
        danhSachGioHang.add(spMoi);
    }
}