package dao;

import DBConnection.DBConnection;
import dto.DanhMuc_DTO;
import java.sql.*;
import java.util.ArrayList;

public class DanhMuc_DAO {

    // 1. Lấy tất cả danh mục
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

    // 2. Tự động sinh mã tiếp theo (DM001 -> DM002...)
    public String getNextId() {
        // Cắt chuỗi từ ký tự thứ 3, lấy 3 ký tự số
        String sql = "SELECT MAX(CAST(SUBSTRING(Ma_DM, 3, 3) AS INT)) FROM DANHMUC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int number = rs.getInt(1);
                if (rs.wasNull()) return "DM001"; // Nếu chưa có dữ liệu

                number++;
                return String.format("DM%03d", number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "DM001";
    }

    // 3. Thêm danh mục
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

    // 4. Cập nhật danh mục
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