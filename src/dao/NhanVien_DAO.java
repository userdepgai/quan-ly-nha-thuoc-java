package dao;

import DBConnection.DBConnection;
import dto.NhanVien_DTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class NhanVien_DAO {

    public ArrayList<NhanVien_DTO> getAll() {

        ArrayList<NhanVien_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM NHANVIEN";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                NhanVien_DTO nv = new NhanVien_DTO(
                        rs.getString("Ma_NV"),
                        rs.getString("Ten_NV"),
                        rs.getString("SDT"),
                        rs.getDate("NamSinh").toLocalDate(),
                        rs.getBoolean("GioiTinh"),
                        rs.getString("ChucVu"),
                        rs.getDate("NgayVaoLam").toLocalDate(),
                        rs.getDouble("LuongCoBan"),
                        rs.getInt("TrangThai"),
                        rs.getString("Ma_DC")
                );

                list.add(nv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ===== TỰ SINH MÃ =====
    public String getNextId() {

        String sql = "SELECT MAX(CAST(SUBSTRING(Ma_NV,3,6) AS INT)) FROM NHANVIEN";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                int number = rs.getInt(1);

                if (rs.wasNull()) return "NV000001";

                number++;
                return String.format("NV%06d", number);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "NV000001";
    }

    // ===== THÊM =====
    public boolean them(NhanVien_DTO nv) {

        String sql = """
    INSERT INTO NHANVIEN
    (Ma_NV, Ten_NV, SDT, NamSinh, GioiTinh,
     ChucVu, NgayVaoLam, LuongCoBan, TrangThai, Ma_DC)
    VALUES (?,?,?,?,?,?,?,?,?,?)
""";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getMa());
            ps.setString(2, nv.getTen());
            ps.setString(3, nv.getSdt());
            ps.setDate(4, Date.valueOf(nv.getNgaySinh()));
            ps.setBoolean(5, nv.isGioiTinh());
            ps.setString(6, nv.getChucVu());
            ps.setDate(7, Date.valueOf(nv.getNgayVaoLam()));
            ps.setDouble(8, nv.getLuongCoBan());
            ps.setInt(9, nv.getTrangThai());
            ps.setString(10, nv.getMaDiaChi());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ===== CẬP NHẬT =====
    public boolean capNhat(NhanVien_DTO nv) {

        String sql = """
        UPDATE NHANVIEN
        SET Ten_NV=?, 
            SDT=?, 
            NamSinh=?, 
            GioiTinh=?,
            ChucVu=?, 
            NgayVaoLam=?, 
            LuongCoBan=?, 
            TrangThai=?,
            Ma_DC=?
        WHERE Ma_NV=?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getTen());
            ps.setString(2, nv.getSdt());
            ps.setDate(3, Date.valueOf(nv.getNgaySinh()));
            ps.setBoolean(4, nv.isGioiTinh());
            ps.setString(5, nv.getChucVu());
            ps.setDate(6, Date.valueOf(nv.getNgayVaoLam()));
            ps.setDouble(7, nv.getLuongCoBan());
            ps.setInt(8, nv.getTrangThai());
            ps.setString(9, nv.getMaDiaChi());   // ✅ đúng vị trí Ma_DC
            ps.setString(10, nv.getMa());        // ✅ đúng vị trí WHERE

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}