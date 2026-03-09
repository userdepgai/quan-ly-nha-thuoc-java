package dao;

import DBConnection.DBConnection;
import dto.GioHang_DTO;
import java.sql.*;
import java.util.*;

public class GioHang_DAO {
    public ArrayList<GioHang_DTO> getAll() {
        ArrayList<GioHang_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM GIOHANG";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {
                GioHang_DTO gh = new GioHang_DTO(
                        rs.getString("Ma_GH"),
                        rs.getString("Ma_KH")
                );

                list.add(gh);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean them(GioHang_DTO gh) {

        String sql = "INSERT INTO GIOHANG VALUES(?,?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, gh.getMaGH());
            ps.setString(2, gh.getMaKH());

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public GioHang_DTO getByMaKH(String maKH) {

        String sql = "SELECT * FROM GIOHANG WHERE Ma_KH=?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKH);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return new GioHang_DTO(
                        rs.getString("Ma_GH"),
                        rs.getString("Ma_KH")
                );
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public String getNextId() {
        String prefix = "GH";
        String sql = "SELECT MAX(Ma_GH) FROM GIOHANG";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            if(rs.next()) {
                String lastId = rs.getString(1);
                if(lastId != null) {
                    int number = Integer.parseInt(lastId.substring(2));
                    number++;
                    return prefix + String.format("%06d", number);
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return "GH000001";
    }
}