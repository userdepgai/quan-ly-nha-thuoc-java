package dao;

import DBConnection.DBConnection;
import dto.KhachHang_Voucher_DTO;
import java.sql.*;
import java.util.ArrayList;

public class KhachHang_Voucher_DAO {
    private static KhachHang_Voucher_DAO instance;

    public static KhachHang_Voucher_DAO getInstance() {
        if (instance == null) {
            instance = new KhachHang_Voucher_DAO();
        }
        return instance;
    }

    // 1. Phân phối Voucher cho TẤT CẢ khách hàng đang hoạt động
    public boolean phanPhoiVoucherToanHeThong(String maVoucher, int soLuot) {
        String sql = "INSERT INTO KHACHHANGVOUCHER (MaVoucher, Ma_KH, SoLuongConLai, SoLuongToiDa) " +
                "SELECT ?, Ma_KH, ?, ? FROM KHACHHANG WHERE TrangThai = 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maVoucher);
            ps.setInt(2, soLuot);
            ps.setInt(3, soLuot);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2. Thêm thủ công cho 1 khách hàng cụ thể
    public boolean them(KhachHang_Voucher_DTO dto) {
        String sql = "INSERT INTO KHACHHANGVOUCHER (MaVoucher, Ma_KH, SoLuongConLai, SoLuongToiDa) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dto.getMaVoucher());
            ps.setString(2, dto.getMaKH());
            ps.setInt(3, dto.getSoLuotConLai());
            ps.setInt(4, dto.getSoLuotToiDa());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Cập nhật số lượt còn lại (Dùng khi khách hàng áp dụng voucher vào hóa đơn)
    public boolean capNhatLuotDung(String maVoucher, String maKH, int soLuotMoi) {
        String sql = "UPDATE KHACHHANGVOUCHER SET SoLuongConLai = ? WHERE MaVoucher = ? AND Ma_KH = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, soLuotMoi);
            ps.setString(2, maVoucher);
            ps.setString(3, maKH);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Lấy danh sách Voucher của 1 khách hàng (Để kiểm tra điều kiện dùng)
    public ArrayList<KhachHang_Voucher_DTO> getByMaKH(String maKH) {
        ArrayList<KhachHang_Voucher_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KHACHHANGVOUCHER WHERE Ma_KH = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKH);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                KhachHang_Voucher_DTO dto = new KhachHang_Voucher_DTO(
                        rs.getInt("SoLuongToiDa"),
                        rs.getInt("SoLuongConLai"),
                        rs.getString("Ma_KH"),
                        rs.getString("MaVoucher")
                );
                list.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}