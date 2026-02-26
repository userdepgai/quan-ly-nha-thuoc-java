package DAO;

import DBConnection.DBConnection;
import dto.NhaCungCap_DTO;
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
                //ncc.setDiaChi(rs.getString("DiaChi"));
                ncc.setTrangThai(rs.getInt("TrangThai"));

                list.add(ncc);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách NCC: " + e.getMessage());
        }

        return list;
    }

    public boolean insert(NhaCungCap_DTO ncc) {
        boolean check = false;
        // Đã xóa DiaChi và 1 dấu ?
        String sql = "INSERT INTO NHACUNGCAP (Ma_NCC, Ten_NCC, MaSoThue, SDT, NguoiLienHe, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, ncc.getMaNCC());
            ps.setString(2, ncc.getTenNCC());
            ps.setString(3, ncc.getMaSoThue());
            ps.setString(4, ncc.getSdt());
            ps.setString(5, ncc.getNguoiLienHe());

            // Đã xóa ps.setString(6, "");

            ps.setInt(6, ncc.getTrangThai()); // Đổi index từ 7 thành 6

            int result = ps.executeUpdate();
            if (result > 0) check = true;
            conn.close();
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Lỗi SQL Thêm:\n" + e.getMessage());
        }
        return check;
    }

    public boolean update(NhaCungCap_DTO ncc) {
        boolean check = false;
        String sql = "UPDATE NHACUNGCAP SET Ten_NCC=?, MaSoThue=?, SDT=?, NguoiLienHe=?, TrangThai=? WHERE Ma_NCC=?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, ncc.getTenNCC());
            ps.setString(2, ncc.getMaSoThue());
            ps.setString(3, ncc.getSdt());
            ps.setString(4, ncc.getNguoiLienHe());


            ps.setInt(5, ncc.getTrangThai());
            ps.setString(6, ncc.getMaNCC());

            int result = ps.executeUpdate();
            if (result > 0) check = true;
            conn.close();
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Lỗi SQL Cập nhật:\n" + e.getMessage());
        }
        return check;
    }

    public boolean updateTrangThai(String maNCC, int trangThaiMoi) {
        String sql = "UPDATE NHACUNGCAP SET TrangThai = ? WHERE Ma_NCC = ?";

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