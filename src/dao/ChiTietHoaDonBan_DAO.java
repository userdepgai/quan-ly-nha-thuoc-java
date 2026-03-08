package dao;

import dto.ChiTietHoaDonBan_DTO;
import DBConnection.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class ChiTietHoaDonBan_DAO {

    // ================= GET BY HÓA ĐƠN =================
    public ArrayList<ChiTietHoaDonBan_DTO> getByMaHD(String maHD) {

        ArrayList<ChiTietHoaDonBan_DTO> list = new ArrayList<>();

        String sql = """
        SELECT ct.*
        FROM CHITIETHOADON ct
        WHERE ct.Ma_HDB = ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ChiTietHoaDonBan_DTO ct = new ChiTietHoaDonBan_DTO();

                ct.setMaSP(rs.getString("Ma_SP"));
                ct.setMaHDB(rs.getString("Ma_HDB"));
                ct.setMaLo(rs.getString("Ma_Lo"));
                ct.setMaKhuyenMai(rs.getString("Ma_KM"));
                ct.setSoLuong(rs.getInt("SoLuong"));
                ct.setGiaBan(rs.getDouble("GiaBan"));
                ct.setGiaBanSauApKM(rs.getDouble("GiaBanSauAp_KM"));
                ct.setThanhTien(rs.getDouble("ThanhTien"));

                list.add(ct);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public boolean insert(ChiTietHoaDonBan_DTO ct) {

        String sql = """
        INSERT INTO CHITIETHOADON
        (Ma_SP, Ma_HDB, Ma_Lo, Ma_KM,
         SoLuong, GiaBan, GiaBanSauAp_KM, ThanhTien)
        VALUES (?,?,?,?,?,?,?,?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ct.getMaSP());
            ps.setString(2, ct.getMaHDB());
            ps.setString(3, ct.getMaLo());
            ps.setString(4, ct.getMaKhuyenMai());
            ps.setInt(5, ct.getSoLuong());
            ps.setDouble(6, ct.getGiaBan());
            ps.setDouble(7, ct.getGiaBanSauApKM());
            ps.setDouble(8, ct.getThanhTien());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}