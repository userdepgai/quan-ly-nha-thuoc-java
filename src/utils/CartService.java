package utils;

import bus.*;
import dto.*;

import java.util.HashMap;

public class CartService {

    public static void mergeGuestCartToUserCart() {

        if(!Session.isLoggedIn()) return;
        if(GuestCart.isEmpty()) return;

        TaiKhoan_DTO user = Session.getCurrentUser();

        String sdt = user.getSdt();

        KhachHang_DTO kh = KhachHang_BUS.getInstance().getBysdt(sdt);

        if(kh == null) return;

        String maKH = kh.getMa();

        GioHang_DTO gh = GioHang_BUS.getInstance().getByMaKH(maKH);
        ChiTietGioHang_BUS ctBUS = new ChiTietGioHang_BUS();

        HashMap<String,Integer> cart = GuestCart.getCart();

        for(String maSP : cart.keySet()) {

            int soLuong = cart.get(maSP);

            ChiTietGioHang_DTO existing =
                    ctBUS.getById(gh.getMaGH(), maSP);

            if(existing != null) {

                existing.setSoLuong(existing.getSoLuong() + soLuong);

                ctBUS.capNhatSoLuong(existing);

            } else {

                ctBUS.them(new ChiTietGioHang_DTO(
                        gh.getMaGH(),
                        maSP,
                        soLuong
                ));
            }
        }
        GuestCart.clear();
    }
}