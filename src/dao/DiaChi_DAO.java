package DAO;

import DBConnection.DBConnection;
import dto.DIACHI_DTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DiaChi_DAO {
    public ArrayList<DIACHI_DTO> getAll() {
        ArrayList<DIACHI_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM DIACHI";
        try(Connection db = DBConnection.getConnection();
            PreparedStatement ps = db.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DIACHI_DTO dc = new DIACHI_DTO(
                        rs.getString("Ma_DC"),
                        rs.getString("Tinh"),
                        rs.getString("Phuong"),
                        rs.getString("Duong"),
                        rs.getString("SoNha")
                );
                list.add(dc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public String getNextId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(Ma_DC,3,6) AS INT)) FROM DIACHI";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int number = rs.getInt(1);
                if (rs.wasNull()) {
                    return "DC000001";
                }
                number++;
                return String.format("DC%06d", number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "DC000001";
    }
    public boolean them(DIACHI_DTO dc) {
        String sql = "INSERT INTO DIACHI VALUES(?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dc.getMaDiaChi());
            ps.setString(2, dc.getTinh());
            ps.setString(3, dc.getPhuong());
            ps.setString(4, dc.getDuong());
            ps.setString(5, dc.getSoNha());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhat(DIACHI_DTO dc) {
        String sql = "UPDATE DIACHI SET Tinh=?, Phuong=?, Duong=?, SoNha=? WHERE Ma_DC=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dc.getTinh());
            ps.setString(2, dc.getPhuong());
            ps.setString(3, dc.getDuong());
            ps.setString(4, dc.getSoNha());
            ps.setString(5, dc.getMaDiaChi());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
