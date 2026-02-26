package dao;
import dto.*;
import DBConnection.DBConnection;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class TaiKhoan_DAO {

    public ArrayList<TaiKhoan_DTO> getAll() {
        ArrayList<TaiKhoan_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM TAIKHOAN";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
               TaiKhoan_DTO tk = new TaiKhoan_DTO(
                       rs.getString("Ma_TK"),
                       rs.getString("SDT"),
                       rs.getString("MatKhau"),
                       rs.getString("MaQuyen"),
                       rs.getDate("NgayKichHoat").toLocalDate(),
                       rs.getInt("TrangThai")
               );
                list.add(tk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getNextID() {
        String sql = "SELECT MAX(CAST(SUBSTRING(Ma_TK,3,6) AS INT)) FROM TAIKHOAN";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            if(rs.next()) {
                int number =  rs.getInt(1);
                if(rs.wasNull()){
                    return "TK000001";
                }
                number++;
                return String.format("TK%06d",number);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "TK000001";
    }

    public boolean them(TaiKhoan_DTO tk) {
        String sql = "INSERT INTO TAIKHOAN VALUES(?,?,?,?,?,?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1,tk.getMaTK());
            ps.setString(2,tk.getSdt());
            ps.setString(3,tk.getMatKhau());
            ps.setInt(4,tk.getTrangThai());
            ps.setDate(5, Date.valueOf(tk.getNgayKichHoat()));
            ps.setString(6,tk.getMaQuyen());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhat(TaiKhoan_DTO tk) {
        String sql = "UPDATE TAIKHOAN SET SDT = ?, MatKhau = ?, TrangThai = ?, NgayKichHoat = ?, MaQuyen = ?, WHERE Ma_TK = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tk.getSdt());
            ps.setString(2, tk.getMatKhau());
            ps.setInt(3, tk.getTrangThai());
            ps.setDate(4, Date.valueOf(tk.getNgayKichHoat()));
            ps.setString(5, tk.getMaQuyen());
            ps.setString(6, tk.getMaTK());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
