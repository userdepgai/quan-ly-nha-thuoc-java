package dao;

import dto.DanhMucSanPham_DTO;
import DBConnection.DBConnection;
import java.sql.*;
import java.util.ArrayList;

public class DanhMucSanPham_DAO {

    // 1. Lấy toàn bộ danh sách danh mục để đổ lên bảng "Danh sách Danh mục"
    public ArrayList<DanhMucSanPham_DTO> getAll() {
        ArrayList<DanhMucSanPham_DTO> ds = new ArrayList<>();
        String sql = "SELECT * FROM DANHMUC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ds.add(new DanhMucSanPham_DTO(
                        rs.getString("MaDM"),
                        rs.getString("TenDM"),
                        rs.getInt("TrangThai")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // 2. Thêm danh mục mới (Nút "Thêm Danh mục")
    public boolean addDanhMuc(DanhMucSanPham_DTO dm) {
        String sql = "INSERT INTO DANHMUCSANPHAM (MaDM, TenDM, TrangThai) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dm.getMaDM());
            ps.setString(2, dm.getTenDM());
            ps.setInt(3, dm.getTrangThai());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Cập nhật danh mục (Nút "Cập nhật")
    public boolean updateDanhMuc(DanhMucSanPham_DTO dm) {
        String sql = "UPDATE DANHMUCSANPHAM SET TenDM = ?, TrangThai = ? WHERE MaDM = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dm.getTenDM());
            ps.setInt(2, dm.getTrangThai());
            ps.setString(3, dm.getMaDM());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Tìm kiếm và lọc (Tương ứng phần "Bộ lọc" trên giao diện)
    // searchOption: "Mã danh mục" hoặc "Tên danh mục" từ ComboBox "Tìm theo"
    // searchValue: Giá trị nhập vào ô "Nhập thông tin tìm kiếm"
    // status: Giá trị từ ComboBox "Trạng thái" (ví dụ: 1-Hoạt động, 0-Ngưng, -1-Tất cả)
    public ArrayList<DanhMucSanPham_DTO> search(String searchOption, String searchValue, int status) {
        ArrayList<DanhMucSanPham_DTO> ds = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM DANHMUCSANPHAM WHERE 1=1");

        // Lọc theo thông tin nhập
        if (searchValue != null && !searchValue.trim().isEmpty()) {
            if ("Mã danh mục".equals(searchOption)) {
                sql.append(" AND MaDM LIKE ?");
            } else if ("Tên danh mục".equals(searchOption)) {
                sql.append(" AND TenDM LIKE ?");
            }
        }

        // Lọc theo trạng thái
        if (status != -1) {
            sql.append(" AND TrangThai = ?");
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + searchValue + "%");
            }
            if (status != -1) {
                ps.setInt(paramIndex++, status);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ds.add(new DanhMucSanPham_DTO(
                        rs.getString("MaDM"),
                        rs.getString("TenDM"),
                        rs.getInt("TrangThai")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // 5. Kiểm tra mã danh mục đã tồn tại chưa (Dùng khi thêm mới)
    public boolean isMaDMExist(String maDM) {
        String sql = "SELECT COUNT(*) FROM DANHMUCSANPHAM WHERE MaDM = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maDM);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}