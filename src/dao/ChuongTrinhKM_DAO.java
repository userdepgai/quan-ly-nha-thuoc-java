package dao;

import DBConnection.DBConnection;
import dto.ChuongTrinhKM_DTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Date;

public class ChuongTrinhKM_DAO {

    // 1. Lấy tất cả chương trình khuyến mãi
    public ArrayList<ChuongTrinhKM_DTO> getAll() {
        ArrayList<ChuongTrinhKM_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM CHUONGTRINHKHUYENMAI";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Mapping dữ liệu từ cột SQL vào Constructor của DTO con
                ChuongTrinhKM_DTO ctkm = new ChuongTrinhKM_DTO(
                        rs.getString("Ma_CTKM"),      // map vào cha (ma)
                        rs.getString("Ten_CTKM"),     // map vào cha (ten)
                        rs.getDate("NgayBatDau"),     // map vào cha (ngayBatDau)
                        rs.getDate("NgayKetThuc"),    // map vào cha (ngayKetThuc)
                        rs.getInt("TrangThai"),       // map vào cha (trangThai)
                        rs.getString("MoTa")          // map vào con (moTa)
                );
                list.add(ctkm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Tự động sinh mã tiếp theo (CTKM001 -> CTKM002)
    public String getNextId() {
        // Lấy số lớn nhất từ chuỗi mã (Bỏ 4 ký tự đầu "CTKM")
        String sql = "SELECT MAX(CAST(SUBSTRING(Ma_CTKM, 5, LEN(Ma_CTKM)-4) AS INT)) FROM CHUONGTRINHKHUYENMAI";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int number = rs.getInt(1);
                if (rs.wasNull()) return "CTKM001"; // Nếu chưa có dữ liệu
                return String.format("CTKM%03d", number + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "CTKM001";
    }

    // 3. Thêm mới
    public boolean them(ChuongTrinhKM_DTO ctkm) {
        String sql = "INSERT INTO CHUONGTRINHKHUYENMAI (Ma_CTKM, Ten_CTKM, NgayBatDau, NgayKetThuc, TrangThai, MoTa) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ctkm.getMa());
            ps.setString(2, ctkm.getTen());
            ps.setDate(3, ctkm.getNgayBatDau());
            ps.setDate(4, ctkm.getNgayKetThuc());
            ps.setInt(5, ctkm.getTrangThai());
            ps.setString(6, ctkm.getMoTa());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Cập nhật
    public boolean capNhat(ChuongTrinhKM_DTO ctkm) {
        String sql = "UPDATE CHUONGTRINHKHUYENMAI SET Ten_CTKM=?, NgayBatDau=?, NgayKetThuc=?, TrangThai=?, MoTa=? WHERE Ma_CTKM=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ctkm.getTen());
            ps.setDate(2, ctkm.getNgayBatDau());
            ps.setDate(3, ctkm.getNgayKetThuc());
            ps.setInt(4, ctkm.getTrangThai());
            ps.setString(5, ctkm.getMoTa());
            ps.setString(6, ctkm.getMa());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. Tìm theo ID
    public ChuongTrinhKM_DTO getById(String ma) {
        String sql = "SELECT * FROM CHUONGTRINHKHUYENMAI WHERE Ma_CTKM = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ma);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new ChuongTrinhKM_DTO(
                        rs.getString("Ma_CTKM"),
                        rs.getString("Ten_CTKM"),
                        rs.getDate("NgayBatDau"),
                        rs.getDate("NgayKetThuc"),
                        rs.getInt("TrangThai"),
                        rs.getString("MoTa")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}