package dao;

import dto.*;
import DBConnection.DBConnection;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class LoHang_DAO {
    public ArrayList<LoHang_DTO> getAll() {
        ArrayList<LoHang_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM LOHANG";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                LoHang_DTO lo = new LoHang_DTO(
                        rs.getString("Ma_Lo"),
                        rs.getDouble("GiaNhap"),
                        rs.getDate("HSD").toLocalDate(),
                        rs.getInt("SoLuong"),
                        rs.getInt("SoLuongConLai"),
                        rs.getDouble("ThanhTien"),
                        rs.getInt("TrangThai"),
                        rs.getInt("TrangThaiTonKho"),
                        rs.getString("Ma_PNK"),
                        rs.getString("Ma_NCC"),
                        rs.getString("Ma_KVLT"),
                        rs.getString("Ma_SP")
                );

                list.add(lo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public String getNextID() {
        String sql = "SELECT MAX(CAST(SUBSTRING(Ma_Lo,3,6) AS INT)) FROM LOHANG";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int number = rs.getInt(1);
                if (rs.wasNull()) {
                    return "LO000001";
                }
                number++;
                return String.format("LO%06d", number);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "LO000001";
    }

    public boolean them(LoHang_DTO lo) {

        String sql = "INSERT INTO LOHANG "
                + "(Ma_Lo, GiaNhap, HSD, NgaySanXuat, "
                + "SoLuong, SoLuongConLai, ThanhTien, "
                + "TrangThai, TrangThaiTonKho, "
                + "Ma_PNK, Ma_NCC, Ma_KVLT, Ma_SP) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, lo.getMaLo());
            ps.setDouble(2, lo.getGiaNhap());
            ps.setDate(3, Date.valueOf(lo.getHsd()));

            ps.setInt(5, lo.getSoLuongNhap());
            ps.setInt(6, lo.getSoLuongConLai());

            ps.setDouble(7, lo.getGiaNhap() * lo.getSoLuongNhap());

            ps.setInt(8, lo.getTrangThai());
            ps.setInt(9, lo.getTrangThaiTonKho());
            ps.setString(10, lo.getMaPnk());
            ps.setString(11, lo.getMaNcc());
            ps.setString(12, lo.getMaKvlt());
            ps.setString(13, lo.getMaSp());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean capNhat(LoHang_DTO lo) {

        String sql = "UPDATE LOHANG SET "
                + "GiaNhap=?, HSD=?, NgaySanXuat=?, "
                + "SoLuong=?, SoLuongConLai=?, ThanhTien=?, "
                + "TrangThai=?, TrangThaiTonKho=?, "
                + "Ma_PNK=?, Ma_NCC=?, Ma_KVLT=?, Ma_SP=? "
                + "WHERE Ma_Lo=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, lo.getGiaNhap());
            ps.setDate(2, Date.valueOf(lo.getHsd()));

            ps.setInt(4, lo.getSoLuongNhap());
            ps.setInt(5, lo.getSoLuongConLai());

            ps.setDouble(6, lo.getGiaNhap() * lo.getSoLuongNhap());

            ps.setInt(7, lo.getTrangThai());
            ps.setInt(8, lo.getTrangThaiTonKho());
            ps.setString(9, lo.getMaPnk());
            ps.setString(10, lo.getMaNcc());
            ps.setString(11, lo.getMaKvlt());
            ps.setString(12, lo.getMaSp());
            ps.setString(13, lo.getMaLo());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}