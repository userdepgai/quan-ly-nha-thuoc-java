package bus;

import dto.ProductItem;
import java.util.ArrayList;
import java.util.List;

public class GioHangManager {
    // Đây là "Cái rổ chung" chứa các sản phẩm khách đã chọn
    public static List<ProductItem> danhSachGioHang = new ArrayList<>();

    // Hàm để xử lý logic: Ném hàng vào rổ
    public static void themVaoGioHang(ProductItem spMoi) {
        for (ProductItem item : danhSachGioHang) {
            if (item.ma.equals(spMoi.ma)) {
                // Nếu sản phẩm đã có trong giỏ thì chỉ cộng dồn số lượng
                item.soLuong += spMoi.soLuong;
                return;
            }
        }
        // Nếu chưa có thì thêm một dòng mới vào giỏ
        danhSachGioHang.add(spMoi);
    }
}