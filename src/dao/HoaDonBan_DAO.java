package dao;

import DBConnection.DBConnection;
import dto.HoaDonBan_DTO;

import java.sql.*;
import java.util.ArrayList;

public class HoaDonBan_DAO {

    // =============================
    // Lấy toàn bộ hóa đơn
    // =============================
    public ArrayList<HoaDonBan_DTO> getAll() {

        ArrayList<HoaDonBan_DTO> list = new ArrayList<>();

        String sql = "SELECT * FROM HOADONBAN ORDER BY Ma_HDB DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                HoaDonBan_DTO hd = new HoaDonBan_DTO();

                hd.setMa(rs.getString("Ma_HDB"));
                hd.setTrangThai(rs.getInt("TrangThai"));
                hd.setTinhTrangThanhToan(rs.getInt("TinhTrangThanhToan"));
                hd.setThanhTien(rs.getDouble("ThanhTien"));
                hd.setMaNhanVien(rs.getString("Ma_NV"));

                list.add(hd);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =============================
    // Thêm hóa đơn (tạo khung hóa đơn)
    // =============================
    public boolean insert(HoaDonBan_DTO hd) {

        String sql =
                "INSERT INTO HOADONBAN " +
                        "(Ma_HDB, TrangThai, TinhTrangThanhToan, ThanhTien, Ma_NV) " +
                        "VALUES (?,?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hd.getMa());
            ps.setInt(2, hd.getTrangThai());
            ps.setInt(3, hd.getTinhTrangThanhToan());
            ps.setDouble(4, hd.getThanhTien());
            ps.setString(5, hd.getMaNhanVien());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =============================
    // Update trạng thái hóa đơn
    // =============================
    public boolean updateTrangThai(String maHD, int trangThai) {

        String sql =
                "UPDATE HOADONBAN SET TrangThai=? WHERE Ma_HDB=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, trangThai);
            ps.setString(2, maHD);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =============================
    // Update tình trạng thanh toán
    // =============================
    public boolean updateThanhToan(String maHD, int tinhTrang) {

        String sql =
                "UPDATE HOADONBAN SET TinhTrangThanhToan=? WHERE Ma_HDB=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tinhTrang);
            ps.setString(2, maHD);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}