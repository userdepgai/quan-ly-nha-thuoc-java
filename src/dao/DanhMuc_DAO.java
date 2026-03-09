package dao;

import DBConnection.DBConnection;
import dto.DanhMuc_DTO;
import java.sql.*;
import java.util.ArrayList;

public class DanhMuc_DAO {

    public ArrayList<DanhMuc_DTO> getAll() {
        ArrayList<DanhMuc_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM DANHMUC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DanhMuc_DTO dm = new DanhMuc_DTO(
                        rs.getString("Ma_DM"),
                        rs.getString("Ten_DM"),
                        rs.getInt("TrangThai")
                );
                list.add(dm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getNextId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(Ma_DM, 3, 3) AS INT)) FROM DANHMUC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int number = rs.getInt(1);
                if (rs.wasNull()) return "DM001";

                number++;
                return String.format("DM%03d", number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "DM001";
    }

    public boolean them(DanhMuc_DTO dm) {
        String sql = "INSERT INTO DANHMUC (Ma_DM, Ten_DM, TrangThai) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dm.getMaDM());
            ps.setString(2, dm.getTenDM());
            ps.setInt(3, dm.getTrangThai());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhat(DanhMuc_DTO dm) {
        String sql = "UPDATE DANHMUC SET Ten_DM=?, TrangThai=? WHERE Ma_DM=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dm.getTenDM());
            ps.setInt(2, dm.getTrangThai());
            ps.setString(3, dm.getMaDM());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}