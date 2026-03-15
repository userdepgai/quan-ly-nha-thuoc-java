package dao;

import java.sql.*;
import java.util.ArrayList;
import dto.*;

import DBConnection.DBConnection;

public class PhanQuyenChucNang_DAO {

    public ArrayList<PhanQuyenChucNang_DTO> getAll(){

        ArrayList<PhanQuyenChucNang_DTO> list = new ArrayList<>();

        String sql = "SELECT * FROM PhanQuyenChucNang";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){

                PhanQuyenChucNang_DTO dto =
                        new PhanQuyenChucNang_DTO(
                                rs.getString("maQuyen"),
                                rs.getString("maCN")
                        );

                list.add(dto);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }
    public boolean hasPermission(String maQuyen, String maCN) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM PHANQUYENCHUCNANG WHERE MaQuyen = ? AND MaCN = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, maQuyen);
            pst.setString(2, maCN);

            ResultSet rs = pst.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public ArrayList<String> getChucNangByQuyen(String maQuyen) {

        ArrayList<String> list = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT MaCN FROM PHANQUYENCHUCNANG WHERE MaQuyen = ?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, maQuyen);

            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                list.add(rs.getString("MaCN"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public boolean add(PhanQuyenChucNang_DTO pqcn) {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO PHANQUYENCHUCNANG VALUES (?, ?)";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, pqcn.getMaQuyen());
            pst.setString(2, pqcn.getMaCN());

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean delete(String maQuyen, String maCN) {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "DELETE FROM PHANQUYENCHUCNANG WHERE MaQuyen = ? AND MaCN = ?";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, maQuyen);
            pst.setString(2, maCN);

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public void deleteByMaQuyen(String maQuyen){

        String sql = "DELETE FROM PHANQUYENCHUCNANG WHERE MaQuyen=?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, maQuyen);
            ps.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void insert(String maQuyen, String maChucNang){

        String sql = "INSERT INTO PHANQUYENCHUCNANG VALUES (?,?)";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, maQuyen);
            ps.setString(2, maChucNang);

            ps.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}