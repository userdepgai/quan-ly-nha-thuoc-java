package DAO;

import DBConnection.DBConnection;
import dto.KhuVucLuuTru_DTO;
import java.sql.*;
import java.util.ArrayList;

public class KhuVucLuuTru_DAO {

    public ArrayList<KhuVucLuuTru_DTO> getAll() {
        ArrayList<KhuVucLuuTru_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KHUVUCLUUTRU";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhuVucLuuTru_DTO kv = new KhuVucLuuTru_DTO();

                kv.setMaKVLT(rs.getString("MaKVLT"));
                kv.setTenKVLT(rs.getString("TenKVLT"));
                kv.setNgayLapKho(rs.getDate("NgayLapKho"));
                kv.setSucChua(rs.getInt("SucChua"));
                kv.setHienCo(rs.getInt("HienCo"));
                //kv.setDiaChi(rs.getString("DiaChi"));
                kv.setTrangThai(rs.getInt("TrangThai"));

                list.add(kv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Hàm thêm
    public boolean insert(KhuVucLuuTru_DTO kv) {
        boolean check = false;
        String sql = "INSERT INTO KHUVUCLUUTRU (MaKVLT, TenKVLT, SucChua, HienCo, NgayLapKho, DiaChi, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, kv.getMaKVLT());
            ps.setString(2, kv.getTenKVLT());
            ps.setInt(3, kv.getSucChua());
            ps.setInt(4, kv.getHienCo());
            ps.setDate(5, kv.getNgayLapKho());
            //ps.setString(6, kv.getDiaChi());
            ps.setInt(7, kv.getTrangThai());

            int result = ps.executeUpdate();

            if (result > 0) {
                check = true;
            }

            conn.close();

        } catch (Exception e) {
            System.out.println("Lỗi thêm khu vực: " + e.getMessage());
        }

        return check;
    }

    // Hàm cập nhật
    public boolean update(KhuVucLuuTru_DTO kv) {
        boolean check = false;
        String sql = "UPDATE KHUVUCLUUTRU SET TenKVLT=?, SucChua=?, HienCo=?, NgayLapKho=?, DiaChi=?, TrangThai=? WHERE MaKVLT=?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, kv.getTenKVLT());
            ps.setInt(2, kv.getSucChua());
            ps.setInt(3, kv.getHienCo());
            ps.setDate(4, kv.getNgayLapKho());
            //ps.setString(5, kv.getDiaChi());
            ps.setInt(6, kv.getTrangThai());
            ps.setString(7, kv.getMaKVLT());

            int result = ps.executeUpdate();

            if (result > 0) {
                check = true;
            }
            conn.close();

        } catch (Exception e) {
            System.out.println("Lỗi cập nhật: " + e.getMessage());
        }
        return check;
    }
    // Update trạng thái
    public boolean updateTrangThai(String maKVLT, int trangThaiMoi) {
        if (trangThaiMoi < 1 || trangThaiMoi > 3) {
            System.out.println("Trạng thái không hợp lệ!");
            return false;
        }

        String sql = "UPDATE KHUVUCLUUTRU SET TrangThai = ? WHERE MaKVLT = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trangThaiMoi);
            ps.setString(2, maKVLT);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}