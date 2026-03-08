package utils;
import bus.PhanQuyenChucNang_BUS;
import dto.TaiKhoan_DTO;
public class Session {
    private static TaiKhoan_DTO currentUser;
    public static TaiKhoan_DTO getCurrentUser() {
        return currentUser;
    }
    public static void setCurrentUser(TaiKhoan_DTO user) {
        currentUser = user;
    }
    public static void clear() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
    public static boolean isCustomer() {
        if(currentUser == null) return false;
        return PhanQuyenChucNang_BUS
                .getInstance()
                .hasPermission(currentUser.getMaQuyen(),"CUAHANG") && currentUser.getMaQuyen().equalsIgnoreCase("Q001");
    }
    public static boolean hasFunction(String maCN) {
        if(currentUser == null) return false;
        return PhanQuyenChucNang_BUS
                .getInstance()
                .hasPermission(currentUser.getMaQuyen(), maCN);
    }
}
