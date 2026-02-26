package dao;

import DBConnection.DBConnection;
import dto.NhanVien_DTO;
import java.sql.*;
import java.util.*;

public class NhanVien_DAO {

    public ArrayList<NhanVien_DTO> getAll() {

        ArrayList<NhanVien_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM NHANVIEN";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                NhanVien_DTO nv = new NhanVien_DTO(
                        rs.getString("MaNV"),
                        rs.getString("TenNV"),
                        rs.getDate("NgaySinh"),
                        rs.getString("DiaChi"),
                        rs.getString("GioiTinh"),
                        rs.getString("SoDienThoai"),
                        rs.getString("ChucVu"),
                        rs.getInt("TrangThai"),
                        rs.getDouble("LuongCoBan"),
                        rs.getDate("NgayVaoLam")
                );

                list.add(nv);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ===== TỰ SINH NV001 =====
    public String getNextId() {

        String sql = "SELECT MAX(CAST(SUBSTRING(MaNV,3,3) AS INT)) FROM NHANVIEN";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            if(rs.next()) {
                int number = rs.getInt(1);

                if(rs.wasNull())
                    return "NV001";

                number++;
                return String.format("NV%03d", number);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return "NV001";
    }

    public boolean them(NhanVien_DTO nv) {

        String sql = "INSERT INTO NHANVIEN VALUES(?,?,?,?,?,?,?,?,?,?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getMa());
            ps.setString(2, nv.getTen());
            ps.setDate(3, java.sql.Date.valueOf(nv.getNgaySinh()));
            ps.setString(4, nv.getMaDiaChi());
            ps.setBoolean(5, nv.isGioiTinh());
            ps.setString(6, nv.getSdt());
            ps.setString(7, nv.getChucVu());
            ps.setInt(8, nv.getTrangThai());
            ps.setDouble(9, nv.getLuongCoBan());
            ps.setDate(10, java.sql.Date.valueOf(nv.getNgayVaoLam()));

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean capNhat(NhanVien_DTO nv) {

        String sql = """
            UPDATE NHANVIEN
            SET TenNV=?,
                NgaySinh=?,
                DiaChi=?,
                GioiTinh=?,
                SoDienThoai=?,
                ChucVu=?,
                TrangThai=?,
                LuongCoBan=?,
                NgayVaoLam=?
            WHERE MaNV=?
        """;

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getMa());
            ps.setString(2, nv.getTen());
            ps.setDate(3, java.sql.Date.valueOf(nv.getNgaySinh()));
            ps.setString(4, nv.getMaDiaChi());
            ps.setBoolean(5, nv.isGioiTinh());
            ps.setString(6, nv.getSdt());
            ps.setString(7, nv.getChucVu());
            ps.setInt(8, nv.getTrangThai());
            ps.setDouble(9, nv.getLuongCoBan());
            ps.setDate(10, java.sql.Date.valueOf(nv.getNgayVaoLam()));

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}