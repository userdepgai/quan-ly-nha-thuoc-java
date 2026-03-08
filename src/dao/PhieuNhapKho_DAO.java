package dao;

import dto.*;
import DBConnection.DBConnection;

import java.sql.*;
import java.util.*;

public class PhieuNhapKho_DAO {
    public ArrayList<PhieuNhapKho_DTO> getAll() {
        ArrayList<PhieuNhapKho_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM PHIEUNHAPKHO";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                PhieuNhapKho_DTO pnk = new PhieuNhapKho_DTO(
                        rs.getString("Ma_PNK"),
                        rs.getTimestamp("NgayLap").toLocalDateTime(),
                        rs.getTimestamp("NgayHoanThanh") != null
                                ? rs.getTimestamp("NgayHoanThanh").toLocalDateTime()
                                : null,
                        rs.getDouble("TongTien"),
                        rs.getInt("TrangThai"),
                        rs.getString("Ma_NV"),
                        rs.getString("Ma_KVLT"),
                        rs.getString("Ma_NCC"),
                        new ArrayList<>()
                );

                list.add(pnk);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public String getNextID() {
        String sql = "SELECT MAX(CAST(SUBSTRING(Ma_PNK,4,6) AS INT)) FROM PHIEUNHAPKHO";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int number = rs.getInt(1);
                if (rs.wasNull()) {
                    return "PNK000001";
                }
                number++;
                return String.format("PNK%06d", number);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "PNK000001";
    }
    public boolean them(PhieuNhapKho_DTO pnk) {

        String sql = "INSERT INTO PHIEUNHAPKHO "
                + "(Ma_PNK, NgayLap, NgayHoanThanh, TongTien, "
                + "TrangThai, Ma_NV, Ma_KVLT, Ma_NCC) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pnk.getMa());
            ps.setTimestamp(2, Timestamp.valueOf(pnk.getNgayLap()));

            if (pnk.getNgayHoanThanh() != null) {
                ps.setTimestamp(3, Timestamp.valueOf(pnk.getNgayHoanThanh()));
            } else {
                ps.setNull(3, Types.TIMESTAMP);
            }

            ps.setDouble(4, pnk.getThanhTien());
            ps.setInt(5, pnk.getTrangThai());
            ps.setString(6, pnk.getMaNhanVien());
            ps.setString(7, pnk.getMaKVLT());
            ps.setString(8, pnk.getMaNCC());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean capNhat(PhieuNhapKho_DTO pnk) {

        String sql = "UPDATE PHIEUNHAPKHO SET "
                + "NgayLap=?, NgayHoanThanh=?, TongTien=?, "
                + "TrangThai=?, Ma_NV=?, Ma_KVLT=?, Ma_NCC=? "
                + "WHERE Ma_PNK=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(pnk.getNgayLap()));

            if (pnk.getNgayHoanThanh() != null) {
                ps.setTimestamp(2, Timestamp.valueOf(pnk.getNgayHoanThanh()));
            } else {
                ps.setNull(2, Types.TIMESTAMP);
            }

            ps.setDouble(3, pnk.getThanhTien());
            ps.setInt(4, pnk.getTrangThai());
            ps.setString(5, pnk.getMaNhanVien());
            ps.setString(6, pnk.getMaKVLT());
            ps.setString(7, pnk.getMaNCC());
            ps.setString(8, pnk.getMa());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}