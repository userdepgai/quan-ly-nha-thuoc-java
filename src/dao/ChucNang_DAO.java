package dao;

import DBConnection.DBConnection;
import dto.ChucNang_DTO;
import java.sql.*;
import java.util.ArrayList;
public class ChucNang_DAO {

    public ArrayList<ChucNang_DTO> getAll() {
        ArrayList<ChucNang_DTO> list = new ArrayList<>();

        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM CHUCNANG";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ChucNang_DTO cn = new ChucNang_DTO(
                        rs.getString("MaCN"),
                        rs.getString("TenCN"),
                        rs.getString("MoTa")
                );

                list.add(cn);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}