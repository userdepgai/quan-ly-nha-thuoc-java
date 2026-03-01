package utils;
import dto.TaiKhoan_DTO;
public class Session {
    private static TaiKhoan_DTO currentUser;
    public static void setCurrentUser(TaiKhoan_DTO user) {
        currentUser = user;
    }
    public static TaiKhoan_DTO getCurrentUser() {
        return currentUser;
    }
    public static void clear() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
