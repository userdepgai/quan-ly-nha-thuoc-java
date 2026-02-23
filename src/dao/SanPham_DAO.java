package dao;
import dto.SanPham_DTO;
import java.sql.*;
import java.util.ArrayList;
// Giả sử DBConnection nằm trong package database hoặc connection
import DBConnection.DBConnection;

public class SanPham_DAO {

    // 1. Lấy toàn bộ danh sách sản phẩm hiển thị lên JTable
    public ArrayList<SanPham_DTO> getAll() {
        ArrayList<SanPham_DTO> ds = new ArrayList<>();
        String sql = "SELECT * FROM SANPHAM";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SanPham_DTO sp = new SanPham_DTO(
                        rs.getString("MaSP"),
                        rs.getString("TenSP"),
                        rs.getInt("DonViTinh"),
                        rs.getString("HinhAnh"),
                        rs.getDouble("LoiNhuan"),
                        rs.getInt("TrangThai"),
                        rs.getString("MaDM"),
                        rs.getString("MaQC")
                );
                ds.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // 2. Thêm sản phẩm mới (Nút "Thêm sản phẩm")
    public boolean addSanPham(SanPham_DTO sp) {
        String sql = "INSERT INTO SANPHAM (MaSP, TenSP, DonViTinh, HinhAnh, LoiNhuan, TrangThai, MaDM, MaQC) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, sp.getMaSP());
            ps.setString(2, sp.getTenSP());
            ps.setInt(3, sp.getDonViTinh());
            ps.setString(4, sp.getHinhAnh());
            ps.setDouble(5, sp.getLoiNhuan());
            ps.setInt(6, sp.getTrangThai());
            ps.setString(7, sp.getMaDM());
            ps.setString(8, sp.getMaQuyCach()); // Theo tên getter trong DTO của bạn

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Cập nhật thông tin sản phẩm (Nút "Cập nhật")
    public boolean updateSanPham(SanPham_DTO sp) {
        String sql = "UPDATE SANPHAM SET TenSP=?, DonViTinh=?, HinhAnh=?, LoiNhuan=?, TrangThai=?, MaDM=?, MaQC=? WHERE MaSP=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, sp.getTenSP());
            ps.setInt(2, sp.getDonViTinh());
            ps.setString(3, sp.getHinhAnh());
            ps.setDouble(4, sp.getLoiNhuan());
            ps.setInt(5, sp.getTrangThai());
            ps.setString(6, sp.getMaDM());
            ps.setString(7, sp.getMaQuyCach());
            ps.setString(8, sp.getMaSP());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Tìm kiếm nâng cao (Tương ứng với "Bộ lọc" trên giao diện)
    public ArrayList<SanPham_DTO> searchSanPham(String searchStr, String maDM, Double loiNhuan, Integer trangThai) {
        ArrayList<SanPham_DTO> ds = new ArrayList<>();

        // Xây dựng câu SQL động dựa trên các bộ lọc
        StringBuilder sql = new StringBuilder("SELECT * FROM SANPHAM WHERE 1=1");

        if (searchStr != null && !searchStr.trim().isEmpty()) {
            sql.append(" AND (MaSP LIKE ? OR TenSP LIKE ?)");
        }
        if (maDM != null && !maDM.equals("Tất cả")) { // Giả sử "Tất cả" là không lọc theo DM
            sql.append(" AND MaDM = ?");
        }
        if (loiNhuan != null) {
            sql.append(" AND LoiNhuan = ?");
        }
        if (trangThai != null && trangThai != -1) { // Giả sử -1 là không lọc trạng thái
            sql.append(" AND TrangThai = ?");
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int index = 1;
            if (searchStr != null && !searchStr.trim().isEmpty()) {
                String searchPattern = "%" + searchStr + "%";
                ps.setString(index++, searchPattern);
                ps.setString(index++, searchPattern);
            }
            if (maDM != null && !maDM.equals("Tất cả")) {
                ps.setString(index++, maDM);
            }
            if (loiNhuan != null) {
                ps.setDouble(index++, loiNhuan);
            }
            if (trangThai != null && trangThai != -1) {
                ps.setInt(index++, trangThai);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ds.add(new SanPham_DTO(
                        rs.getString("MaSP"),
                        rs.getString("TenSP"),
                        rs.getInt("DonViTinh"),
                        rs.getString("HinhAnh"),
                        rs.getDouble("LoiNhuan"),
                        rs.getInt("TrangThai"),
                        rs.getString("MaDM"),
                        rs.getString("MaQC")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
}
