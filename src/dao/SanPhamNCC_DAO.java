package dao;

import DBConnection.DBConnection;
import dto.SanPhamNCC_DTO;

import java.sql.*;
import java.util.ArrayList;

public class SanPhamNCC_DAO {

    public ArrayList<SanPhamNCC_DTO> getAll() {
        ArrayList<SanPhamNCC_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM SANPHAMNCC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SanPhamNCC_DTO sp = new SanPhamNCC_DTO();
                sp.setMaSanPham(rs.getString("Ma_SP"));
                sp.setMaNCC(rs.getString("Ma_NCC"));
                sp.setGiaNhap(rs.getDouble("GiaNhap"));
                sp.setTrangThai(rs.getInt("TrangThai"));
                list.add(sp);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi getAll SANPHAMNCC: " + e.getMessage());
        }
        return list;
    }
    public boolean insert(SanPhamNCC_DTO spNcc) {
        String sql = "INSERT INTO SANPHAMNCC (Ma_SP, Ma_NCC, GiaNhap, TrangThai) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, spNcc.getMaSanPham());
            ps.setString(2, spNcc.getMaNCC());
            ps.setDouble(3, spNcc.getGiaNhap());
            ps.setInt(4, spNcc.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean update(SanPhamNCC_DTO spNcc) {
        String sql = "UPDATE SANPHAMNCC SET GiaNhap = ?, TrangThai = ? WHERE Ma_SP = ? AND Ma_NCC = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, spNcc.getGiaNhap());
            ps.setInt(2, spNcc.getTrangThai());
            ps.setString(3, spNcc.getMaSanPham());
            ps.setString(4, spNcc.getMaNCC());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String maNCC, String maSP) {
        String sql = "DELETE FROM SANPHAMNCC WHERE Ma_NCC = ? AND Ma_SP = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNCC);
            ps.setString(2, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}