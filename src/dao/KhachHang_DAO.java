package dao;

import DBConnection.DBConnection;
import dto.KhachHang_DTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class KhachHang_DAO {

    // ================== GET ALL ==================
    public ArrayList<KhachHang_DTO> getAll() {

        ArrayList<KhachHang_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KHACHHANG";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                KhachHang_DTO kh = new KhachHang_DTO(
                        rs.getString("Ma_KH"),
                        rs.getString("Ten_KH"),
                        rs.getString("SDT"),
                        rs.getDate("NgaySinh").toLocalDate(),
                        rs.getBoolean("GioiTinh"),
                        rs.getDouble("DiemThuong"),
                        rs.getDouble("DiemHang"),
                        rs.getString("Hang"),
                        rs.getDate("NgayDKThanhVien").toLocalDate()
                );

                list.add(kh);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================== TỰ SINH MÃ ==================
    public String getNextId() {

        String sql = "SELECT MAX(CAST(SUBSTRING(Ma_KH,3,6) AS INT)) FROM KHACHHANG";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                int number = rs.getInt(1);

                if (rs.wasNull()) {
                    return "KH000001";
                }

                number++;
                return String.format("KH%06d", number);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "KH000001";
    }

    // ================== THÊM ==================
    public boolean them(KhachHang_DTO kh) {

        String sql = "INSERT INTO KHACHHANG VALUES (?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getMa());
            ps.setString(2, kh.getTen());
            ps.setString(3, kh.getSdt());
            ps.setDate(4, Date.valueOf(kh.getNgaySinh()));
            ps.setBoolean(5, kh.isGioiTinh());
            ps.setDouble(6, kh.getDiemThuong());
            ps.setDouble(7, kh.getDiemHang());
            ps.setString(8, kh.getHang());
            ps.setDate(9, Date.valueOf(kh.getNgayDKThanhVien()));

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================== CẬP NHẬT ==================
    public boolean capNhat(KhachHang_DTO kh) {

        String sql = """
                UPDATE KHACHHANG 
                SET Ten_KH=?, 
                    SDT=?, 
                    NgaySinh=?, 
                    GioiTinh=?, 
                    DiemThuong=?, 
                    DiemHang=?, 
                    Hang=?, 
                    NgayDKThanhVien=? 
                WHERE Ma_KH=?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getTen());
            ps.setString(2, kh.getSdt());
            ps.setDate(3, Date.valueOf(kh.getNgaySinh()));
            ps.setBoolean(4, kh.isGioiTinh());
            ps.setDouble(5, kh.getDiemThuong());
            ps.setDouble(6, kh.getDiemHang());
            ps.setString(7, kh.getHang());
            ps.setDate(8, Date.valueOf(kh.getNgayDKThanhVien()));
            ps.setString(9, kh.getMa());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}