package dao;

import DBConnection.DBConnection;
import dto.QuyCach_DTO;
import java.sql.*;
import java.util.ArrayList;

public class QuyCach_DAO {

    // 1. Lấy toàn bộ danh sách quy cách
    public ArrayList<QuyCach_DTO> getAll() {
        ArrayList<QuyCach_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM QUYCACH";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                QuyCach_DTO qc = new QuyCach_DTO(
                        rs.getString("Ma_QC"),
                        rs.getInt("SLTrongHop"),
                        rs.getInt("SLHopTrongThung"),
                        rs.getInt("SLSP_Thung")
                );
                list.add(qc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Tự động sinh mã tiếp theo (Dạng QC001, QC002...)
    public String getNextId() {
        // Cắt chuỗi từ ký tự thứ 3, lấy số int. Ví dụ QC001 -> 1
        String sql = "SELECT MAX(CAST(SUBSTRING(Ma_QC, 3, 3) AS INT)) FROM QUYCACH";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int number = rs.getInt(1);
                if (rs.wasNull()) {
                    return "QC001";
                }
                number++;
                return String.format("QC%03d", number);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "QC001";
    }

    // 3. Thêm quy cách mới
    public boolean them(QuyCach_DTO qc) {
        String sql = "INSERT INTO QUYCACH (Ma_QC, SLTrongHop, SLHopTrongThung, SLSP_Thung) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, qc.getMaQC());
            ps.setInt(2, qc.getSlTrongHop());
            ps.setInt(3, qc.getSlHopTrongThung());
            ps.setInt(4, qc.getSlspThung());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Cập nhật quy cách
    public boolean capNhat(QuyCach_DTO qc) {
        String sql = "UPDATE QUYCACH SET SLTrongHop=?, SLHopTrongThung=?, SLSP_Thung=? WHERE Ma_QC=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, qc.getSlTrongHop());
            ps.setInt(2, qc.getSlHopTrongThung());
            ps.setInt(3, qc.getSlspThung());
            ps.setString(4, qc.getMaQC());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}