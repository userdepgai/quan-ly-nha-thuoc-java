package dao;

import DBConnection.DBConnection;
import dto.KhachHang_DiaChi_DTO;

import java.sql.*;
import java.util.ArrayList;

public class KhachHang_DiaChi_DAO {

    // ================== LẤY THEO MÃ KH ==================
    public ArrayList<KhachHang_DiaChi_DTO> getByMaKH(String maKH) {

        ArrayList<KhachHang_DiaChi_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KHACHHANGDIACHI WHERE Ma_KH = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKH);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    KhachHang_DiaChi_DTO dto =
                            new KhachHang_DiaChi_DTO(
                                    rs.getString("Ma_KH"),
                                    rs.getString("Ma_DC"),
                                    rs.getInt("TrangThai")
                            );

                    list.add(dto);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================== THÊM ==================
    public boolean them(KhachHang_DiaChi_DTO dto) {

        String sqlInsert =
                "INSERT INTO KHACHHANGDIACHI (Ma_KH, Ma_DC, TrangThai) VALUES (?,?,?)";

        try (Connection conn = DBConnection.getConnection()) {

            // Nếu là mặc định -> reset tất cả về 0
            if (dto.getTrangThai() == 1) {

                String sqlReset =
                        "UPDATE KHACHHANGDIACHI SET TrangThai = 0 WHERE Ma_KH = ?";

                try (PreparedStatement psReset = conn.prepareStatement(sqlReset)) {
                    psReset.setString(1, dto.getMaKhachHang());
                    psReset.executeUpdate();
                }
            }

            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {

                psInsert.setString(1, dto.getMaKhachHang());
                psInsert.setString(2, dto.getMaDiaChi());
                psInsert.setInt(3, dto.getTrangThai());

                return psInsert.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================== XOÁ ==================
    public boolean xoa(String maKH, String maDC) {

        String sql =
                "DELETE FROM KHACHHANGDIACHI WHERE Ma_KH = ? AND Ma_DC = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKH);
            ps.setString(2, maDC);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================== ĐẶT MẶC ĐỊNH ==================
    public boolean datMacDinh(String maKH, String maDC) {

        try (Connection conn = DBConnection.getConnection()) {

            // reset về 0
            String sqlReset =
                    "UPDATE KHACHHANGDIACHI SET TrangThai = 0 WHERE Ma_KH = ?";

            try (PreparedStatement ps1 = conn.prepareStatement(sqlReset)) {
                ps1.setString(1, maKH);
                ps1.executeUpdate();
            }

            // set = 1
            String sqlSet =
                    "UPDATE KHACHHANGDIACHI SET TrangThai = 1 WHERE Ma_KH = ? AND Ma_DC = ?";

            try (PreparedStatement ps2 = conn.prepareStatement(sqlSet)) {
                ps2.setString(1, maKH);
                ps2.setString(2, maDC);
                return ps2.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}