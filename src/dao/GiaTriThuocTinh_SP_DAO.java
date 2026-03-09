package dao;

import DBConnection.DBConnection;
import dto.GiaTriThuocTinh_SP_DTO;
import java.sql.*;
import java.util.ArrayList;

public class GiaTriThuocTinh_SP_DAO {
    private static GiaTriThuocTinh_SP_DAO instance;

    public static GiaTriThuocTinh_SP_DAO getInstance() {
        if (instance == null) instance = new GiaTriThuocTinh_SP_DAO();
        return instance;
    }

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
                        rs.getString("Ma_GTTT"),
                        rs.getInt("TrangThai")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean them(GiaTriThuocTinh_SP_DTO dto) {
        String sql = "INSERT INTO GIATRITHUOCTINHSP (Ma_GTTT, Ma_SP, MaThuocTinh, TrangThai) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dto.getMaGiaTri());
            ps.setString(2, dto.getMaSP());
            ps.setString(3, dto.getMaThuocTinh());
            ps.setInt(4, dto.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean capNhat(GiaTriThuocTinh_SP_DTO dto) {
        String sql = "UPDATE GIATRITHUOCTINHSP SET TrangThai=? WHERE Ma_GTTT=? AND Ma_SP=? AND MaThuocTinh=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dto.getTrangThai());
            ps.setString(2, dto.getMaGiaTri());
            ps.setString(3, dto.getMaSP());
            ps.setString(4, dto.getMaThuocTinh());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean xoa(String maGiaTri, String maSP, String maTT) {
        String sql = "DELETE FROM GIATRITHUOCTINHSP WHERE Ma_GTTT=? AND Ma_SP=? AND MaThuocTinh=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maGiaTri);
            ps.setString(2, maSP);
            ps.setString(3, maTT);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}