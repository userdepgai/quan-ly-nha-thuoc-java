package dao;

import DBConnection.DBConnection;
import dto.ChiTietGioHang_DTO;
import java.sql.*;
import java.util.*;

public class ChiTietGioHang_DAO {
    public ArrayList<ChiTietGioHang_DTO> getByMaGH(String maGH){
        ArrayList<ChiTietGioHang_DTO> list = new ArrayList<>();

        String sql = "SELECT * FROM CHITIETGIOHANG WHERE Ma_GH=?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maGH);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                ChiTietGioHang_DTO ct = new ChiTietGioHang_DTO(
                        rs.getString("Ma_GH"),
                        rs.getString("Ma_SP"),
                        rs.getInt("SoLuong")
                );

                list.add(ct);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean them(ChiTietGioHang_DTO ct) {
        String sql = "INSERT INTO CHITIETGIOHANG VALUES(?,?,?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ct.getMaGH());
            ps.setString(2, ct.getMaSP());
            ps.setInt(3, ct.getSoLuong());

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean capNhatSoLuong(ChiTietGioHang_DTO ct) {
        String sql = "UPDATE CHITIETGIOHANG SET SoLuong=? WHERE Ma_GH=? AND Ma_SP=?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ct.getSoLuong());
            ps.setString(2, ct.getMaGH());
            ps.setString(3, ct.getMaSP());

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean xoa(String maGH, String maSP) {
        String sql = "DELETE FROM CHITIETGIOHANG WHERE Ma_GH=? AND Ma_SP=?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maGH);
            ps.setString(2, maSP);

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public ChiTietGioHang_DTO getById(String maGH, String maSP) {

        String sql = "SELECT * FROM CHITIETGIOHANG WHERE Ma_GH=? AND Ma_SP=?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maGH);
            ps.setString(2, maSP);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return new ChiTietGioHang_DTO(
                        rs.getString("Ma_GH"),
                        rs.getString("Ma_SP"),
                        rs.getInt("SoLuong")
                );
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}