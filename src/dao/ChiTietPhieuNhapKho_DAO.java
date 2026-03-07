package dao;

import dto.*;
import DBConnection.DBConnection;

import java.sql.*;
import java.util.*;

public class ChiTietPhieuNhapKho_DAO {
    public ArrayList<ChiTietPhieuNhapKho_DTO> getByMaPNK(String maPNK) {

        ArrayList<ChiTietPhieuNhapKho_DTO> list = new ArrayList<>();

        String sql = "SELECT * FROM CHITIETPHIEUNHAP WHERE Ma_PNK = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPNK);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ChiTietPhieuNhapKho_DTO ct = new ChiTietPhieuNhapKho_DTO(
                        rs.getString("Ma_PNK"),
                        rs.getString("Ma_SP"),
                        rs.getInt("SoLuong"),
                        rs.getString("Ma_Lo")
                );

                list.add(ct);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public boolean them(ChiTietPhieuNhapKho_DTO ct) {

        String sql = "INSERT INTO CHITIETPHIEUNHAP (Ma_PNK, Ma_SP, SoLuong, Ma_Lo) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ct.getMaPNK());
            ps.setString(2, ct.getMaSP());
            ps.setInt(3, ct.getSoLuong());
            ps.setString(4, ct.getMaLo());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean capNhat(ChiTietPhieuNhapKho_DTO ct) {

        String sql = "UPDATE CHITIETPHIEUNHAP " +
                "SET SoLuong = ?, Ma_Lo = ? " +
                "WHERE Ma_PNK = ? AND Ma_SP = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ct.getSoLuong());
            ps.setString(2, ct.getMaLo());
            ps.setString(3, ct.getMaPNK());
            ps.setString(4, ct.getMaSP());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean xoa(String maPNK, String maSP) {

        String sql = "DELETE FROM CHITIETPHIEUNHAP " +
                "WHERE Ma_PNK = ? AND Ma_SP = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPNK);
            ps.setString(2, maSP);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}