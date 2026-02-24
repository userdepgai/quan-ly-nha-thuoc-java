package dao;
import dto.HoaDonBan_DTO;
import dao.HoaDonBan_DAO;
public class TestDAO {

    public static void main(String[] args) {

        HoaDonBan_DAO dao = new HoaDonBan_DAO();

        HoaDonBan_DTO hd = new HoaDonBan_DTO();

        hd.setMa("HDTEST001");
        hd.setTrangThai(0);
        hd.setTinhTrangThanhToan(0);
        hd.setThanhTien(100000);
        hd.setMaNhanVien("NV000002");

        boolean kq = dao.insert(hd);

        System.out.println("Insert: " + kq);
    }
}