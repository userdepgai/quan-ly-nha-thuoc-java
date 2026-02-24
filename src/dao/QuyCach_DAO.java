package dao;

import dto.QuyCach_DTO;
import DBConnection.DBConnection;
import java.sql.*;
import java.util.ArrayList;

public class QuyCach_DAO {

    // Lấy toàn bộ danh sách quy cách
    public ArrayList<QuyCach_DTO> getAll() {
        ArrayList<QuyCach_DTO> ds = new ArrayList<>();
        String sql = "SELECT * FROM QUYCACH";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(new QuyCach_DTO(
                        rs.getString("MaQC"),
                        rs.getInt("SlTrongHop"),
                        rs.getInt("SlHopTrongThung"),
                        rs.getInt("SlspThung")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // Thêm mới một quy cách
    public boolean addQuyCach(QuyCach_DTO qc) {
        String sql = "INSERT INTO QUYCACH (MaQC, SlTrongHop, SlHopTrongThung, SlspThung) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, qc.getMaQC());
            ps.setInt(2, qc.getSlTrongHop());
            ps.setInt(3, qc.getSlHopTrongThung());
            ps.setInt(4, qc.getSlspThung());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tìm mã quy cách dựa trên các thông số (Dùng để kiểm tra trùng lặp trước khi thêm SP)
    public String getMaQCByDetails(int trongHop, int trongThung) {
        String sql = "SELECT MaQC FROM QUYCACH WHERE SlTrongHop = ? AND SlHopTrongThung = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, trongHop);
            ps.setInt(2, trongThung);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("MaQC");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}