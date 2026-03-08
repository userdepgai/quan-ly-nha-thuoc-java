package dao;

import DBConnection.DBConnection;
import dto.GiaTriThuocTinh_DTO;
import java.sql.*;
import java.util.ArrayList;

public class GiaTriThuocTinh_DAO {
    private static GiaTriThuocTinh_DAO instance;

    public static GiaTriThuocTinh_DAO getInstance() {
        if (instance == null) instance = new GiaTriThuocTinh_DAO();
        return instance;
    }

    public ArrayList<GiaTriThuocTinh_DTO> getAll() {
        ArrayList<GiaTriThuocTinh_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM GIATRITHUOCTINH";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new GiaTriThuocTinh_DTO(
                        rs.getString("Ma_GTTT"),       // Khớp SQL
                        rs.getString("NoiDungGiaTri"), // Khớp SQL
                        rs.getString("MaThuocTinh")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public String getNextId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(Ma_GTTT, 3, LEN(Ma_GTTT)-2) AS INT)) FROM GIATRITHUOCTINH";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int number = rs.getInt(1);
                if (rs.wasNull()) return "GT001";
                return String.format("GT%03d", number + 1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return "GT001";
    }

    public boolean them(GiaTriThuocTinh_DTO gt) {
        String sql = "INSERT INTO GIATRITHUOCTINH (Ma_GTTT, NoiDungGiaTri, MaThuocTinh) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gt.getMaGiaTri());
            ps.setString(2, gt.getNdGiaTri());
            ps.setString(3, gt.getMaThuocTinh());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean capNhat(GiaTriThuocTinh_DTO gt) {
        String sql = "UPDATE GIATRITHUOCTINH SET NoiDungGiaTri=?, MaThuocTinh=? WHERE Ma_GTTT=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gt.getNdGiaTri());
            ps.setString(2, gt.getMaThuocTinh());
            ps.setString(3, gt.getMaGiaTri());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}