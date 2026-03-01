package dao;

import DBConnection.DBConnection;
import dto.SanPham_DTO;
import java.sql.*;
import java.util.ArrayList;

public class SanPham_DAO {

    public ArrayList<SanPham_DTO> getAll() {
        ArrayList<SanPham_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM SANPHAM";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SanPham_DTO sp = new SanPham_DTO();
                sp.setMaSP(rs.getString("Ma_SP"));
                sp.setTenSP(rs.getString("Ten_SP"));
                sp.setDonViTinh(rs.getString("DonViTinh"));
                sp.setLoiNhuan(rs.getDouble("LoiNhuan"));
                sp.setHinhAnh(rs.getString("HinhAnh"));
                sp.setKeDon(rs.getInt("KeDon"));
                sp.setTrangThai(rs.getInt("TrangThai"));
                sp.setMaDM(rs.getString("Ma_DM"));
                sp.setMaQC(rs.getString("Ma_QC"));

                list.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getNextId() {
        // Lấy số lớn nhất từ mã SP (SP000050 -> lấy 50)
        String sql = "SELECT MAX(CAST(SUBSTRING(Ma_SP, 3, 6) AS INT)) FROM SANPHAM";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int nextId = rs.getInt(1) + 1;
                return String.format("SP%06d", nextId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "SP000001";
    }

    public boolean them(SanPham_DTO sp) {
        String sql = "INSERT INTO SANPHAM (Ma_SP, Ten_SP, DonViTinh, LoiNhuan, HinhAnh, KeDon, TrangThai, Ma_DM, Ma_QC) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sp.getMaSP());
            ps.setString(2, sp.getTenSP());
            ps.setString(3, sp.getDonViTinh());
            ps.setDouble(4, sp.getLoiNhuan());
            ps.setString(5, sp.getHinhAnh());
            ps.setInt(6, sp.getKeDon());
            ps.setInt(7, sp.getTrangThai());
            ps.setString(8, sp.getMaDM());
            ps.setString(9, sp.getMaQC());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhat(SanPham_DTO sp) {
        String sql = "UPDATE SANPHAM SET Ten_SP=?, DonViTinh=?, LoiNhuan=?, HinhAnh=?, KeDon=?, TrangThai=?, Ma_DM=?, Ma_QC=? WHERE Ma_SP=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSP());
            ps.setString(2, sp.getDonViTinh());
            ps.setDouble(3, sp.getLoiNhuan());
            ps.setString(4, sp.getHinhAnh());
            ps.setInt(5, sp.getKeDon());
            ps.setInt(6, sp.getTrangThai());
            ps.setString(7, sp.getMaDM());
            ps.setString(8, sp.getMaQC());
            ps.setString(9, sp.getMaSP());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}