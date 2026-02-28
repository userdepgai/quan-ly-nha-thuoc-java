package dao;

import DBConnection.DBConnection;
import dto.KhuVucLuuTru_DTO;
import dto.DIACHI_DTO;

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
                kv.setMaKVLT(rs.getString("Ma_KVLT"));
                kv.setTenKVLT(rs.getString("Ten_KVLT"));
                kv.setNgayLapKho(rs.getDate("NgayLapKho"));
                kv.setSucChua(rs.getInt("SucChua"));
                kv.setHienCo(rs.getInt("HienCo"));
                kv.setTrangThai(rs.getInt("TrangThai"));
                DIACHI_DTO dc = new DIACHI_DTO();
                dc.setMaDiaChi(rs.getString("Ma_DC"));
                kv.setDiaChi(dc);

                list.add(kv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(KhuVucLuuTru_DTO kv) {
        String sql = "INSERT INTO KHUVUCLUUTRU (Ma_KVLT, Ten_KVLT, SucChua, HienCo, NgayLapKho, TrangThai, Ma_DC) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kv.getMaKVLT());
            ps.setString(2, kv.getTenKVLT());
            ps.setInt(3, kv.getSucChua());
            ps.setInt(4, kv.getHienCo());
            ps.setDate(5, kv.getNgayLapKho());
            ps.setInt(6, kv.getTrangThai());
            ps.setString(7, kv.getDiaChi() != null ? kv.getDiaChi().getMaDiaChi() : null);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean update(KhuVucLuuTru_DTO kv) {
        String sql = "UPDATE KHUVUCLUUTRU SET Ten_KVLT=?, SucChua=?, HienCo=?, NgayLapKho=?, TrangThai=?, Ma_DC=? WHERE Ma_KVLT=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kv.getTenKVLT());
            ps.setInt(2, kv.getSucChua());
            ps.setInt(3, kv.getHienCo());
            ps.setDate(4, kv.getNgayLapKho());
            ps.setInt(5, kv.getTrangThai());
            ps.setString(6, kv.getDiaChi() != null ? kv.getDiaChi().getMaDiaChi() : null);
            ps.setString(7, kv.getMaKVLT());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTrangThai(String maKVLT, int trangThaiMoi) {
        String sql = "UPDATE KHUVUCLUUTRU SET TrangThai=? WHERE Ma_KVLT=?";
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

    public String getNextId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(Ma_KVLT, 3, 4) AS INT)) FROM KHUVUCLUUTRU";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int number = rs.getInt(1);
                if (rs.wasNull()) return "KV0001";
                return String.format("KV%04d", ++number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "KV0001";
    }
}