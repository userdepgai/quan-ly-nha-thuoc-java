package dao;

import DBConnection.DBConnection;
import dto.GiaTriThuocTinh_SP_DTO;
import java.sql.*;
import java.util.ArrayList;

public class GiaTriThuocTinh_SP_DAO {
    public ArrayList<GiaTriThuocTinh_SP_DTO> getAll() {
        ArrayList<GiaTriThuocTinh_SP_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM GIATRITHUOCTINHSP";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new GiaTriThuocTinh_SP_DTO(
                        rs.getString("Ma_SP"),
                        rs.getString("MaThuocTinh"),
                        rs.getString("Ma_GTTT")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean them(GiaTriThuocTinh_SP_DTO dto) {
        String sql = "INSERT INTO GIATRITHUOCTINHSP (Ma_SP, MaThuocTinh, Ma_GTTT) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dto.getMaSP());
            ps.setString(2, dto.getMaThuocTinh());
            ps.setString(3, dto.getMaGiaTri());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean xoaByMaSP(String maSP) {
        String sql = "DELETE FROM GIATRITHUOCTINHSP WHERE Ma_SP = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSP);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}