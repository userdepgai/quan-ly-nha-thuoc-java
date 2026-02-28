package dao;

import DBConnection.DBConnection;
import dto.GiaTriThuocTinh_DTO;
import java.sql.*;
import java.util.ArrayList;

public class GiaTriThuocTinh_DAO {

    // 1. Lấy tất cả giá trị thuộc tính (Dùng khi cần load toàn bộ)
    public ArrayList<GiaTriThuocTinh_DTO> getAll() {
        ArrayList<GiaTriThuocTinh_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM GIATRITHUOCTINH";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                GiaTriThuocTinh_DTO gt = new GiaTriThuocTinh_DTO(
                        rs.getString("Ma_GTTT"),
                        rs.getString("NoiDungGiaTri"),
                        rs.getInt("TrangThai"),
                        rs.getString("MaThuocTinh")
                );
                list.add(gt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. LẤY DANH SÁCH GIÁ TRỊ THEO MÃ THUỘC TÍNH (Rất quan trọng cho form của bạn)
    public ArrayList<GiaTriThuocTinh_DTO> getByMaThuocTinh(String maThuocTinh) {
        ArrayList<GiaTriThuocTinh_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM GIATRITHUOCTINH WHERE MaThuocTinh = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maThuocTinh);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                GiaTriThuocTinh_DTO gt = new GiaTriThuocTinh_DTO(
                        rs.getString("Ma_GTTT"),
                        rs.getString("NoiDungGiaTri"),
                        rs.getInt("TrangThai"),
                        rs.getString("MaThuocTinh")
                );
                list.add(gt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. Tự động sinh mã tiếp theo (GT001, GT002...)
    public String getNextId() {
        // Lấy GT + 3 số. Hàm này bóc tách số ra để lấy Max rồi + 1
        String sql = "SELECT MAX(CAST(SUBSTRING(Ma_GTTT, 3, LEN(Ma_GTTT)-2) AS INT)) FROM GIATRITHUOCTINH";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int number = rs.getInt(1);
                if (rs.wasNull()) return "GT001"; // Nếu chưa có dữ liệu

                number++;
                return String.format("GT%03d", number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "GT001";
    }

    // 4. Thêm giá trị thuộc tính mới
    public boolean them(GiaTriThuocTinh_DTO gt) {
        String sql = "INSERT INTO GIATRITHUOCTINH (Ma_GTTT, NoiDungGiaTri, TrangThai, MaThuocTinh) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, gt.getMaGiaTri());
            ps.setString(2, gt.getNdGiaTri());
            ps.setInt(3, gt.getTrangThai());
            ps.setString(4, gt.getMaThuocTinh());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. Cập nhật giá trị thuộc tính
    public boolean capNhat(GiaTriThuocTinh_DTO gt) {
        String sql = "UPDATE GIATRITHUOCTINH SET NoiDungGiaTri=?, TrangThai=?, MaThuocTinh=? WHERE Ma_GTTT=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, gt.getNdGiaTri());
            ps.setInt(2, gt.getTrangThai());
            ps.setString(3, gt.getMaThuocTinh());
            ps.setString(4, gt.getMaGiaTri());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}