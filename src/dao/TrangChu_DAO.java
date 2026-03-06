package dao;

import dto.SanPham_DTO;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.*;
import java.sql.*;
import DBConnection.DBConnection;
import dto.QuyCach_DTO;
public class TrangChu_DAO {
    private SanPham_DAO spDAO = new SanPham_DAO();

    public ArrayList<SanPham_DTO> getAll() {
        return spDAO.getAll();
    }

    public ArrayList<SanPham_DTO> timNangCao(
            Double giaTu,
            Double giaDen,
            String loai,
            String congDung,
            String doiTuong
    ) {

        ArrayList<SanPham_DTO> result = new ArrayList<>();

        for (SanPham_DTO sp : spDAO.getAll()) {

            boolean ok = true;

            // if (giaTu != null && sp.getGia() < giaTu) ok = false;
            //if (giaDen != null && sp.getGia() > giaDen) ok = false;

            // if (!loai.equals("Tất cả") && !sp.getLoai().equals(loai)) ok = false;

            //if (!congDung.isEmpty() && !sp.getCongDung().equals(congDung)) ok = false;

            // if (!doiTuong.isEmpty() && !sp.getDoiTuong().equals(doiTuong)) ok = false;

            if (ok) result.add(sp);
        }

        return result;
    }
    public ArrayList<SanPham_DTO> timTheoMa(String keyword) {

        ArrayList<SanPham_DTO> result = new ArrayList<>();

        for (SanPham_DTO sp : spDAO.getAll()) {
            if (sp.getMaSP().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(sp);
            }
        }

        return result;
    }
    public ArrayList<SanPham_DTO> timTheoTen(String keyword) {

        ArrayList<SanPham_DTO> result = new ArrayList<>();

        for (SanPham_DTO sp : spDAO.getAll()) {
            if (sp.getTenSP().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(sp);
            }
        }

        return result;
    }
    public double getGiaNhapMoiNhat(String maSP){

        double giaNhap = 0;

        String sql = """
        SELECT TOP 1 GiaNhap
        FROM LoHang
        WHERE Ma_SP = ?
        ORDER BY HSD DESC
    """;

        try(
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ){

            ps.setString(1, maSP);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                giaNhap = rs.getDouble("GiaNhap");
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return giaNhap;
    }
    public QuyCach_DTO getQuyCach(String maQC){

        QuyCach_DAO qcDAO = new QuyCach_DAO();

        for(QuyCach_DTO qc : qcDAO.getAll()){
            if(qc.getMaQC().equals(maQC)){
                return qc;
            }
        }

        return null;
    }
}