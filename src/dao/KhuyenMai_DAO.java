package dao;

import DBConnection.DBConnection;
import dto.KhuyenMai_DTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class KhuyenMai_DAO {

    // 1. Lấy toàn bộ danh sách
    public ArrayList<KhuyenMai_DTO> getAll() {
        ArrayList<KhuyenMai_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KHUYENMAI";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhuyenMai_DTO km = new KhuyenMai_DTO(
                        rs.getString("Ma_KM"),
                        rs.getString("Ten_KM"),
                        rs.getInt("TrangThai"),
                        rs.getInt("DoiTuongApDung"),
                        rs.getInt("LoaiKM"),      // SỬA: Lấy kiểu INT từ SQL
                        rs.getDouble("GiaTriKM"),
                        rs.getString("Ma_CTKM"),
                        rs.getString("Ma_SP"),
                        rs.getString("Ma_DM")
                );
                list.add(km);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Tìm kiếm theo ID
    public KhuyenMai_DTO getById(String maKM) {
        KhuyenMai_DTO km = null;
        String sql = "SELECT * FROM KHUYENMAI WHERE Ma_KM = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKM);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                km = new KhuyenMai_DTO(
                        rs.getString("Ma_KM"),
                        rs.getString("Ten_KM"),
                        rs.getInt("TrangThai"),
                        rs.getInt("DoiTuongApDung"),
                        rs.getInt("LoaiKM"),      // SỬA: Lấy kiểu INT
                        rs.getDouble("GiaTriKM"),
                        rs.getString("Ma_CTKM"),
                        rs.getString("Ma_SP"),
                        rs.getString("Ma_DM")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return km;
    }

    // 3. Tự động sinh mã
    public String getNextId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(Ma_KM, 3, LEN(Ma_KM)-2) AS INT)) FROM KHUYENMAI";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int number = rs.getInt(1);
                if (rs.wasNull()) return "KM001";
                return String.format("KM%03d", number + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "KM001";
    }

    // 4. Thêm Khuyến mãi mới
    public boolean them(KhuyenMai_DTO km) {
        String sql = "INSERT INTO KHUYENMAI (Ma_KM, Ten_KM, LoaiKM, GiaTriKM, TrangThai, DoiTuongApDung, Ma_CTKM, Ma_SP, Ma_DM) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getMaKM());
            ps.setString(2, km.getTenKM());
            ps.setInt(3, km.getLoaiKhuyenMai());
            ps.setDouble(4, km.getGiaTriKhuyenMai());
            ps.setInt(5, km.getTrangThai());
            ps.setInt(6, km.getDoiTuongApDung());
            ps.setString(7, km.getMaChuongTrinh());

            // --- LOGIC RÀNG BUỘC NULL DỰA THEO ĐỐI TƯỢNG ---
            if (km.getDoiTuongApDung() == 1) {
                // Nếu áp dụng cho SẢN PHẨM (1)
                ps.setString(8, km.getMaSanPham());
                ps.setNull(9, java.sql.Types.VARCHAR);
            } else {
                // Nếu áp dụng cho DANH MỤC (0)
                ps.setNull(8, java.sql.Types.VARCHAR);
                ps.setString(9, km.getMaDanhMuc());
            }
            // -----------------------------------------------

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. Cập nhật Khuyến mãi
    public boolean capNhat(KhuyenMai_DTO km) {
        String sql = "UPDATE KHUYENMAI SET Ten_KM=?, LoaiKM=?, GiaTriKM=?, TrangThai=?, DoiTuongApDung=?, Ma_CTKM=?, Ma_SP=?, Ma_DM=? WHERE Ma_KM=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getTenKM());
            ps.setInt(2, km.getLoaiKhuyenMai());
            ps.setDouble(3, km.getGiaTriKhuyenMai());
            ps.setInt(4, km.getTrangThai());
            ps.setInt(5, km.getDoiTuongApDung());
            ps.setString(6, km.getMaChuongTrinh());

            // --- LOGIC RÀNG BUỘC NULL DỰA THEO ĐỐI TƯỢNG ---
            if (km.getDoiTuongApDung() == 1) {
                // Nếu áp dụng cho SẢN PHẨM (1)
                ps.setString(7, km.getMaSanPham());
                ps.setNull(8, java.sql.Types.VARCHAR);
            } else {
                // Nếu áp dụng cho DANH MỤC (0)
                ps.setNull(7, java.sql.Types.VARCHAR);
                ps.setString(8, km.getMaDanhMuc());
            }
            // -----------------------------------------------

            ps.setString(9, km.getMaKM());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 6. Lấy danh sách theo Mã Chương Trình (Bổ sung cho việc hiển thị bảng con nếu cần)
    public ArrayList<KhuyenMai_DTO> getByMaCTKM(String maCTKM) {
        ArrayList<KhuyenMai_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KHUYENMAI WHERE Ma_CTKM = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCTKM);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new KhuyenMai_DTO(
                        rs.getString("Ma_KM"),
                        rs.getString("Ten_KM"),
                        rs.getInt("TrangThai"),
                        rs.getInt("DoiTuongApDung"),
                        rs.getInt("LoaiKM"),
                        rs.getDouble("GiaTriKM"),
                        rs.getString("Ma_CTKM"),
                        rs.getString("Ma_SP"),
                        rs.getString("Ma_DM")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}