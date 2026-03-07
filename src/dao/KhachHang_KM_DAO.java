package dao;

import DBConnection.DBConnection;
import dto.KhachHang_KM_DTO;
import java.sql.*;
import java.util.ArrayList;

public class KhachHang_KM_DAO {
    private static KhachHang_KM_DAO instance;

    public static KhachHang_KM_DAO getInstance() {
        if (instance == null) {
            instance = new KhachHang_KM_DAO();
        }
        return instance;
    }

    // 1. Phân phối Khuyến mãi cho TẤT CẢ khách hàng (Dùng khi tạo mã KM mới)
    public boolean phanPhoiKMToanHeThong(String maKM, int soLuot) {
        String sql = "INSERT INTO KHUYENMAIKHACHHANG (Ma_KM, Ma_KH, SoLuongToiDa, SoLuongConLai) " +
                "SELECT ?, Ma_KH, ?, ? FROM KHACHHANG WHERE TrangThai = 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKM);
            ps.setInt(2, soLuot);
            ps.setInt(3, soLuot);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2. Thêm thủ công cho 1 khách hàng cụ thể
    public boolean them(KhachHang_KM_DTO dto) {
        String sql = "INSERT INTO KHUYENMAIKHACHHANG (Ma_KM, Ma_KH, SoLuongToiDa, SoLuongConLai) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dto.getMaKM());
            ps.setString(2, dto.getMaKH());
            ps.setInt(3, dto.getSoLuotToiDa());
            ps.setInt(4, dto.getSoLuotConLai());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Cập nhật số lượt còn lại (Dùng khi khách hàng thanh toán hóa đơn)
    public boolean capNhatLuotDung(String maKM, String maKH, int soLuotMoi) {
        String sql = "UPDATE KHUYENMAIKHACHHANG SET SoLuongConLai = ? WHERE Ma_KM = ? AND Ma_KH = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, soLuotMoi);
            ps.setString(2, maKM);
            ps.setString(3, maKH);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Lấy danh sách khuyến mãi của 1 khách hàng (Để kiểm tra lượt dùng)
    public ArrayList<KhachHang_KM_DTO> getByMaKH(String maKH) {
        ArrayList<KhachHang_KM_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KHUYENMAIKHACHHANG WHERE Ma_KH = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKH);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                KhachHang_KM_DTO dto = new KhachHang_KM_DTO(
                        rs.getInt("SoLuongToiDa"),
                        rs.getInt("SoLuongConLai"),
                        rs.getString("Ma_KH"),
                        rs.getString("Ma_KM")
                );
                list.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}