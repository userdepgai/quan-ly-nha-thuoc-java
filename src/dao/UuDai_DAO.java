package dao;

import DBConnection.DBConnection;
import dto.UuDai_DTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UuDai_DAO {

    /**
     * Lấy toàn bộ danh sách ưu đãi (Gộp cả CTKM và Voucher)
     * Sử dụng UNION ALL để nối 2 bảng lại với nhau
     */
    public ArrayList<UuDai_DTO> getAll() {
        ArrayList<UuDai_DTO> list = new ArrayList<>();

        // Câu lệnh SQL nối dữ liệu từ 2 bảng
        String sql = "SELECT Ma_CTKM AS Ma, Ten_CTKM AS Ten, NgayBatDau, NgayKetThuc, TrangThai FROM CHUONGTRINHKHUYENMAI " +
                "UNION ALL " +
                "SELECT MaVoucher AS Ma, TenVoucher AS Ten, NgayBatDau, NgayKetThuc, TrangThai FROM VOUCHER";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UuDai_DTO ud = new UuDai_DTO(
                        rs.getString("Ma"),
                        rs.getString("Ten"),
                        rs.getDate("NgayBatDau"),
                        rs.getDate("NgayKetThuc"),
                        rs.getInt("TrangThai")
                );
                list.add(ud);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm ưu đãi theo mã (Tìm trong cả 2 bảng)
    public UuDai_DTO getById(String ma) {
        UuDai_DTO ud = null;
        String sql = "SELECT Ma_CTKM AS Ma, Ten_CTKM AS Ten, NgayBatDau, NgayKetThuc, TrangThai FROM CHUONGTRINHKHUYENMAI WHERE Ma_CTKM = ? " +
                "UNION ALL " +
                "SELECT MaVoucher AS Ma, TenVoucher AS Ten, NgayBatDau, NgayKetThuc, TrangThai FROM VOUCHER WHERE MaVoucher = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ma);
            ps.setString(2, ma);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ud = new UuDai_DTO(
                            rs.getString("Ma"),
                            rs.getString("Ten"),
                            rs.getDate("NgayBatDau"),
                            rs.getDate("NgayKetThuc"),
                            rs.getInt("TrangThai")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ud;
    }
}