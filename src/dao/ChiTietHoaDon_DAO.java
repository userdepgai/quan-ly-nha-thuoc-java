package dao;

import DBConnection.DBConnection;
import dto.ChiTietHoaDonBan_DTO;

import java.sql.*;
import java.util.ArrayList;

public class ChiTietHoaDon_DAO {

    // =====================================
    // Lấy chi tiết theo mã hóa đơn
    // =====================================
    public ArrayList<ChiTietHoaDonBan_DTO> getByMaHD(String maHD) {

        ArrayList<ChiTietHoaDonBan_DTO> list = new ArrayList<>();

        String sql =
                "SELECT * FROM CHI_TIET_HOA_DON WHERE Ma_HDB=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ChiTietHoaDonBan_DTO ct =
                        new ChiTietHoaDonBan_DTO();

                ct.setMaHDB(rs.getString("Ma_HDB"));
                ct.setMaLo(rs.getString("Ma_LO"));
                ct.setSoLuong(rs.getInt("SoLuong"));
                ct.setGiaBan(rs.getDouble("GiaBan"));
                ct.setThanhTien(rs.getDouble("ThanhTien"));

                list.add(ct);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =====================================
    // Thêm chi tiết hóa đơn
    // =====================================
    public boolean insert(ChiTietHoaDonBan_DTO ct) {

        String sql =
                "INSERT INTO CHI_TIET_HOA_DON " +
                        "(Ma_HDB, Ma_LO, SoLuong, GiaBan, ThanhTien) " +
                        "VALUES (?,?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ct.getMaHDB());
            ps.setString(2, ct.getMaLo());
            ps.setInt(3, ct.getSoLuong());
            ps.setDouble(4, ct.getGiaBan());
            ps.setDouble(5, ct.getThanhTien());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================
    // Xóa chi tiết theo hóa đơn
    // =====================================
    public boolean deleteByMaHD(String maHD) {

        String sql =
                "DELETE FROM CHI_TIET_HOA_DON WHERE Ma_HDB=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHD);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}