package dao;

import DBConnection.DBConnection;
import dto.ThanhToan_DTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ThanhToan_DAO {

    public ThanhToan_DTO layThongTinKhachHang(String idTaiKhoan) {
        ThanhToan_DTO thongTin = null;

        String sql = "SELECT kh.Ten_KH, kh.SDT, dc.SoNha, dc.Duong, dc.Phuong, dc.Tinh " +
                "FROM TAIKHOAN tk " +
                "JOIN KHACHHANG kh ON tk.SDT = kh.SDT " +
                "LEFT JOIN KHACHHANGDIACHI khdc ON kh.Ma_KH = khdc.Ma_KH AND khdc.TrangThai = 1 " +
                "LEFT JOIN DIACHI dc ON khdc.Ma_DC = dc.Ma_DC " +
                "WHERE tk.Ma_TK = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, idTaiKhoan);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String ten = rs.getString("Ten_KH");
                String sdt = rs.getString("SDT");

                String soNha = rs.getString("SoNha") != null ? rs.getString("SoNha") + " " : "";
                String duong = rs.getString("Duong") != null ? rs.getString("Duong") + ", " : "";
                String phuong = rs.getString("Phuong") != null ? rs.getString("Phuong") + ", " : "";
                String tinh = rs.getString("Tinh") != null ? rs.getString("Tinh") : "";

                String fullAddress = (soNha + duong + phuong + tinh).trim();
                if (fullAddress.endsWith(",")) {
                    fullAddress = fullAddress.substring(0, fullAddress.length() - 1);
                }
                thongTin = new ThanhToan_DTO(ten, sdt, fullAddress);
            }
        } catch (Exception e) {
            System.out.println("Lỗi SQL lấy thông tin giao hàng: " + e.getMessage());
            e.printStackTrace();
        }
        return thongTin;
    }
}