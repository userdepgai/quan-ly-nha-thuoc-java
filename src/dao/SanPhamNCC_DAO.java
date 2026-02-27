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
}