package dao;

import DBConnection.DBConnection;
import dto.BaoCaoDoanhThu_DTO;
import dto.BaoCaoTonKho_DTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class BaoCao_DAO {

    private DBConnection dbConnection = new DBConnection();
    public List<BaoCaoTonKho_DTO> baoCaoTonKho(Date tuNgay, Date denNgay, String maDanhMuc) {
        List<BaoCaoTonKho_DTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT sp.Ma_SP, sp.Ten_SP, dm.Ten_DM AS PhanLoai, ");
            sql.append("lh.SoLuongConLai AS TonKho, lh.HSD AS HanSuDung, ");
            sql.append("pnk.NgayLap AS NgayLap, kv.Ten_KVLT AS Kho ");
            sql.append("FROM LOHANG lh ");
            sql.append("JOIN SANPHAM sp ON lh.Ma_SP = sp.Ma_SP ");
            sql.append("JOIN DANHMUC dm ON sp.Ma_DM = dm.Ma_DM ");
            sql.append("JOIN PHIEUNHAPKHO pnk ON lh.Ma_PNK = pnk.Ma_PNK ");
            sql.append("JOIN KHUVUCLUUTRU kv ON lh.Ma_KVLT = kv.Ma_KVLT ");

            sql.append("WHERE lh.SoLuongConLai > 0 ");
            sql.append("AND CAST(pnk.NgayLap AS DATE) BETWEEN ? AND ? ");

            boolean locTheoDM = maDanhMuc != null && !maDanhMuc.equalsIgnoreCase("ALL");
            if (locTheoDM) {
                sql.append("AND sp.Ma_DM = ? ");
            }
            sql.append("ORDER BY pnk.NgayLap DESC");

            ps = conn.prepareStatement(sql.toString());

            ps.setDate(1, new java.sql.Date(tuNgay.getTime()));
            ps.setDate(2, new java.sql.Date(denNgay.getTime()));

            if (locTheoDM) {
                ps.setString(3, maDanhMuc);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                BaoCaoTonKho_DTO dto = new BaoCaoTonKho_DTO();
                dto.setMaSP(rs.getString("Ma_SP"));
                dto.setTenSP(rs.getString("Ten_SP"));
                dto.setPhanLoai(rs.getString("PhanLoai"));
                dto.setTonKho(rs.getInt("TonKho"));
                dto.setHanSuDung(rs.getDate("HanSuDung"));
                dto.setNgayLap(rs.getDate("NgayLap"));
                dto.setKho(rs.getString("Kho"));

                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }
    public List<BaoCaoDoanhThu_DTO> baoCaoDoanhThuTheoNgay(Date tuNgay, Date denNgay) {
        List<BaoCaoDoanhThu_DTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT CAST(hd.NgayLap AS DATE) AS NgayBan, ");
            sql.append("COUNT(DISTINCT hd.Ma_HDB) AS SoLuongHoaDon, ");
            sql.append("SUM(ct.SoLuong) AS SoLuongSPDaBan, ");
            sql.append("SUM(ct.GiaBan * ct.SoLuong) AS TongTienHang, ");
            sql.append("SUM((ct.GiaBan * ct.SoLuong) - ct.ThanhTien) AS GiamGia, ");
            sql.append("SUM(ct.ThanhTien) AS DoanhThuThuan, ");
            sql.append("SUM(((lh.GiaNhap / qc.SLSP_Thung) * sp.LoiNhuan - (lh.GiaNhap / qc.SLSP_Thung)) * ct.SoLuong) AS LoiNhuanGop ");

            sql.append("FROM HOADONBAN hd ");
            sql.append("JOIN CHITIETHOADON ct ON hd.Ma_HDB = ct.Ma_HDB ");
            sql.append("JOIN LOHANG lh ON ct.Ma_Lo = lh.Ma_Lo ");
            sql.append("JOIN SANPHAM sp ON ct.Ma_SP = sp.Ma_SP ");
            sql.append("JOIN QUYCACH qc ON sp.Ma_QC = qc.Ma_QC ");

            sql.append("WHERE CAST(hd.NgayLap AS DATE) BETWEEN ? AND ? ");
            sql.append("GROUP BY CAST(hd.NgayLap AS DATE) ");
            sql.append("ORDER BY CAST(hd.NgayLap AS DATE) DESC");

            ps = conn.prepareStatement(sql.toString());
            ps.setDate(1, new java.sql.Date(tuNgay.getTime()));
            ps.setDate(2, new java.sql.Date(denNgay.getTime()));

            rs = ps.executeQuery();
            while (rs.next()) {
                BaoCaoDoanhThu_DTO dto = new BaoCaoDoanhThu_DTO();
                dto.setNgayBan(rs.getDate("NgayBan"));
                dto.setSoLuongHoaDon(rs.getInt("SoLuongHoaDon"));
                dto.setSoLuongSPDaBan(rs.getInt("SoLuongSPDaBan"));
                dto.setTongTienHang(rs.getDouble("TongTienHang"));
                dto.setGiamGia(rs.getDouble("GiamGia"));
                dto.setDoanhThuThuan(rs.getDouble("DoanhThuThuan"));
                dto.setLoiNhuanGop(rs.getDouble("LoiNhuanGop"));

                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }
}