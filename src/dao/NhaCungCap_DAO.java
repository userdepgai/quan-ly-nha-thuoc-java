package dao;

import DBConnection.DBConnection;
import dto.NhaCungCap_DTO;
import dto.DIACHI_DTO;
import java.sql.*;
import java.util.ArrayList;

public class NhaCungCap_DAO {

    public ArrayList<NhaCungCap_DTO> getAll() {
        ArrayList<NhaCungCap_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM NHACUNGCAP";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NhaCungCap_DTO ncc = new NhaCungCap_DTO();
                ncc.setMaNCC(rs.getString("Ma_NCC"));
                ncc.setTenNCC(rs.getString("Ten_NCC"));
                ncc.setMaSoThue(rs.getString("MaSoThue"));
                ncc.setSdt(rs.getString("SDT"));
                ncc.setNguoiLienHe(rs.getString("NguoiLienHe"));
                ncc.setTrangThai(rs.getInt("TrangThai"));

                String maDC = rs.getString("Ma_DC");
                System.out.println("DEBUG: Ma_NCC=" + rs.getString("Ma_NCC") + " Ma_DC=" + maDC);
                DIACHI_DTO dc = new DIACHI_DTO();
                dc.setMaDiaChi(maDC);
                ncc.setDiaChi(dc);
                list.add(ncc);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách NCC: " + e.getMessage());
        }
        return list;
    }

    public boolean insert(NhaCungCap_DTO ncc) {
        String sql = "INSERT INTO NHACUNGCAP (Ma_NCC, Ten_NCC, MaSoThue, SDT, NguoiLienHe, TrangThai, Ma_DC) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ncc.getMaNCC());
            ps.setString(2, ncc.getTenNCC());
            ps.setString(3, ncc.getMaSoThue());
            ps.setString(4, ncc.getSdt());
            ps.setString(5, ncc.getNguoiLienHe());
            ps.setInt(6, ncc.getTrangThai());
            ps.setString(7, ncc.getDiaChi() != null ? ncc.getDiaChi().getMaDiaChi() : null);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(NhaCungCap_DTO ncc) {
        String sql = "UPDATE NHACUNGCAP SET Ten_NCC=?, MaSoThue=?, SDT=?, NguoiLienHe=?, TrangThai=?, Ma_DC=? WHERE Ma_NCC=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ncc.getTenNCC());
            ps.setString(2, ncc.getMaSoThue());
            ps.setString(3, ncc.getSdt());
            ps.setString(4, ncc.getNguoiLienHe());
            ps.setInt(5, ncc.getTrangThai());
            ps.setString(6, ncc.getDiaChi() != null ? ncc.getDiaChi().getMaDiaChi() : null);
            ps.setString(7, ncc.getMaNCC());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTrangThai(String maNCC, int trangThaiMoi) {
        String sql = "UPDATE NHACUNGCAP SET trangThai = ? WHERE Ma_NCC = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trangThaiMoi);
            ps.setString(2, maNCC);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật trạng thái NCC: " + e.getMessage());
        }
        return false;
    }

}