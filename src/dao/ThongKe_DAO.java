package dao;

import dto.ThongKe_DTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThongKe_DAO {

    public List<ThongKe_DTO> thongKeDoanhThu(java.util.Date tuNgay, java.util.Date denNgay) {
        List<ThongKe_DTO> dsThongKe = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {

            String sql = "SELECT ct.MaSP, sp.GiaNhap, ct.GiaBan, " +
                    "SUM(ct.SoLuong) AS SLBan, " +
                    "SUM((ct.GiaBan - sp.GiaNhap) * ct.SoLuong) AS LoiNhuan " +
                    "FROM CHITIETHOADON ct " +
                    "JOIN HOADON hd ON ct.MaHD = hd.MaHD " +
                    "JOIN SANPHAMNCC sp ON ct.MaSP = sp.MaSP " +
                    "WHERE hd.NgayLap BETWEEN ? AND ? " +
                    "GROUP BY ct.MaSP, sp.GiaNhap, ct.GiaBan";

            pst = conn.prepareStatement(sql);

            // Chuyển java.util.Date sang java.sql.Date để truyền vào SQL
            pst.setDate(1, new java.sql.Date(tuNgay.getTime()));
            pst.setDate(2, new java.sql.Date(denNgay.getTime()));

            rs = pst.executeQuery();

            while (rs.next()) {
                ThongKe_DTO tk = new ThongKe_DTO();
                tk.setMaSanPham(rs.getString("MaSP"));
                tk.setGiaNhap(rs.getDouble("GiaNhap"));
                tk.setGiaBan(rs.getDouble("GiaBan"));
                tk.setSoLuongBan(rs.getInt("SLBan"));
                tk.setLoiNhuan(rs.getDouble("LoiNhuan"));

                dsThongKe.add(tk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Nhớ đóng connection
            try { if (rs != null) rs.close(); if (pst != null) pst.close(); if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return dsThongKe;
    }
}