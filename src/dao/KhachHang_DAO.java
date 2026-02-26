package dao;

import DBConnection.DBConnection;
import dto.KhachHang_DTO;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class KhachHang_DAO {

    public ArrayList<KhachHang_DTO> getAll() {
        ArrayList<KhachHang_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KHACHHANG";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Lấy dữ liệu và chuyển đổi java.sql.Date sang java.time.LocalDate
                LocalDate ngaySinh = rs.getDate("NgaySinh") != null ? rs.getDate("NgaySinh").toLocalDate() : null;
                LocalDate ngayDK = rs.getDate("NgayDKThanhVien") != null ? rs.getDate("NgayDKThanhVien").toLocalDate() : null;

                // Sử dụng Constructor đầy đủ của bà
                KhachHang_DTO kh = new KhachHang_DTO(
                        rs.getString("MaKH"),
                        rs.getString("TenKH"),
                        rs.getString("SDT"),
                        ngaySinh,
                        rs.getBoolean("GioiTinh"),
                        rs.getDouble("DiemThuong"),
                        rs.getDouble("DiemHang"),
                        rs.getString("Hang"),
                        ngayDK
                );

                // Vì DTO của bà kế thừa Nguoi_DTO, bà nhớ set thêm địa chỉ nếu Nguoi_DTO có hàm setDiaChi
                //kh.setDiaChi(rs.getString("DiaChi")); //

                list.add(kh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean them(KhachHang_DTO kh) {
        // Bỏ hoàn toàn phần dsDiaChi bị đỏ ở hình image_c26a7f.png
        String sql = "INSERT INTO KHACHHANG (MaKH, TenKH, NgaySinh, DiaChi, GioiTinh, SDT, DiemHang, DiemThuong, Hang, NgayDKThanhVien) VALUES(?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getMa());
            ps.setString(2, kh.getTen());
            ps.setDate(3, kh.getNgaySinh() != null ? Date.valueOf(kh.getNgaySinh()) : null);
            //ps.setString(4, kh.getDiaChi());
            ps.setBoolean(5, kh.isGioiTinh());
            ps.setString(6, kh.getSdt());
            ps.setDouble(7, kh.getDiemHang());
            ps.setDouble(8, kh.getDiemThuong());
            ps.setString(9, kh.getHang());
            ps.setDate(10, kh.getNgayDKThanhVien() != null ? Date.valueOf(kh.getNgayDKThanhVien()) : null);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhat(KhachHang_DTO kh) {
        String sql = "UPDATE KHACHHANG SET TenKH=?, NgaySinh=?, DiaChi=?, GioiTinh=?, SDT=?, " +
                "DiemHang=?, DiemThuong=?, Hang=?, NgayDKThanhVien=? WHERE MaKH=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getTen());
            ps.setDate(2, kh.getNgaySinh() != null ? Date.valueOf(kh.getNgaySinh()) : null);
          //  ps.setString(3, kh.getDiaChi());
            ps.setBoolean(4, kh.isGioiTinh());
            ps.setString(5, kh.getSdt());
            ps.setDouble(6, kh.getDiemHang());
            ps.setDouble(7, kh.getDiemThuong());
            ps.setString(8, kh.getHang());
            ps.setDate(9, kh.getNgayDKThanhVien() != null ? Date.valueOf(kh.getNgayDKThanhVien()) : null);
            ps.setString(10, kh.getMa());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getNextId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(MaKH,3,3) AS INT)) FROM KHACHHANG";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int number = rs.getInt(1);
                if (rs.wasNull()) return "KH001";
                return String.format("KH%03d", number + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "KH001";
    }
}