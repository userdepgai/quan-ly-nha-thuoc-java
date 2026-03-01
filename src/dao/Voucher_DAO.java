package dao;

import DBConnection.DBConnection;
import dto.Voucher_DTO;
import java.sql.*;
import java.util.ArrayList;

public class Voucher_DAO {

    public ArrayList<Voucher_DTO> getAll() {
        ArrayList<Voucher_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM VOUCHER";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Voucher_DTO(
                        rs.getString("MaVoucher"),
                        rs.getString("TenVoucher"),
                        rs.getDate("NgayBatDau"),
                        rs.getDate("NgayKetThuc"),
                        rs.getInt("TrangThai"),
                        rs.getInt("LoaiVoucher"),
                        rs.getDouble("GiaTriVoucher"),
                        rs.getDouble("DonToiThieu")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public String getNextId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(MaVoucher, 3, LEN(MaVoucher)-2) AS INT)) FROM VOUCHER";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int number = rs.getInt(1);
                if (rs.wasNull()) return "VC001";
                return String.format("VC%03d", number + 1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return "VC001";
    }

    public boolean them(Voucher_DTO v) {
        String sql = "INSERT INTO VOUCHER (MaVoucher, TenVoucher, LoaiVoucher, GiaTriVoucher, DonToiThieu, NgayBatDau, NgayKetThuc, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getMa());
            ps.setString(2, v.getTen());
            ps.setInt(3, v.getLoaiVoucher());
            ps.setDouble(4, v.getGiaTriVoucher());
            ps.setDouble(5, v.getDonToiThieu());
            ps.setDate(6, v.getNgayBatDau());
            ps.setDate(7, v.getNgayKetThuc());
            ps.setInt(8, v.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean capNhat(Voucher_DTO v) {
        String sql = "UPDATE VOUCHER SET TenVoucher=?, LoaiVoucher=?, GiaTriVoucher=?, DonToiThieu=?, NgayBatDau=?, NgayKetThuc=?, TrangThai=? WHERE MaVoucher=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getTen());
            ps.setInt(2, v.getLoaiVoucher());
            ps.setDouble(3, v.getGiaTriVoucher());
            ps.setDouble(4, v.getDonToiThieu());
            ps.setDate(5, v.getNgayBatDau());
            ps.setDate(6, v.getNgayKetThuc());
            ps.setInt(7, v.getTrangThai());
            ps.setString(8, v.getMa());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}