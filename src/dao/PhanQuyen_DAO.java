package dao;


import DBConnection.DBConnection;
import dto.PhanQuyen_DTO;
import java.sql.*;
import java.util.*;

public class PhanQuyen_DAO {

    public ArrayList<PhanQuyen_DTO> getAll() {
        ArrayList<PhanQuyen_DTO>  list = new ArrayList<>();
        String sql = "SELECT * FROM PHANQUYEN";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PhanQuyen_DTO phanQuyen_dto = new PhanQuyen_DTO(
                     rs.getString("MaQuyen"),
                     rs.getString("TenQuyen"),
                     rs.getString("MoTa"),
                     rs.getInt("TrangThai")
                );

                list.add(phanQuyen_dto);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getNextId() {

        String sql = "SELECT MAX(CAST(SUBSTRING(MaQuyen,2,3) AS INT)) FROM PHANQUYEN";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int number = rs.getInt(1);
                if (rs.wasNull()) {
                    return "Q001";
                }
                number++;
                return String.format("Q%03d", number);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Q001";
    }
    public boolean them(PhanQuyen_DTO quyen) {
        String sql = "INSERT INTO PHANQUYEN VALUES(?,?,?,?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, quyen.getMaQuyen());
            ps.setString(2, quyen.getTenQuyen());
            ps.setString(3, quyen.getMoTa());
            ps.setInt(4, quyen.getTrangThai());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhat(PhanQuyen_DTO quyen) {
        String sql = "UPDATE PHANQUYEN SET TenQuyen=?, MoTa=?, TrangThai=? WHERE MaQuyen=?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, quyen.getTenQuyen());
            ps.setString(2, quyen.getMoTa());
            ps.setInt(3, quyen.getTrangThai());
            ps.setString(4, quyen.getMaQuyen());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
