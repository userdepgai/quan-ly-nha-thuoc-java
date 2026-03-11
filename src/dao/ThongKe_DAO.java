package dao;

import DBConnection.DBConnection;
import bus.HoaDonBan_BUS;
import bus.LoHang_BUS;
import dto.LoHang_DTO;
import dto.ThongKeKhachHang_DTO;
import dto.ThongKe_DTO;
import bus.ThongKe_BUS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class ThongKe_DAO {
    private DBConnection dbConnection = new DBConnection();
    public List<ThongKe_DTO> thongKeDoanhThu(Date tuNgay, Date denNgay, String maDanhMuc) {
        List<ThongKe_DTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT sp.Ma_SP, ");
        sql.append("SUM(ct.SoLuong) AS TongSLBan, ");
        sql.append("SUM(ct.SoLuong * ct.GiaBan) AS TongDoanhThu, ");
        sql.append("SUM(ct.SoLuong * (CAST(lh.GiaNhap AS FLOAT) / qc.SLSP_Thung)) AS TongTienNhap ");
        sql.append("FROM HOADONBAN hd ");
        sql.append("JOIN CHITIETHOADON ct ON hd.Ma_HDB = ct.Ma_HDB ");
        sql.append("JOIN SANPHAM sp ON ct.Ma_SP = sp.Ma_SP ");
        sql.append("JOIN QUYCACH qc ON sp.Ma_QC = qc.Ma_QC ");
        sql.append("JOIN LOHANG lh ON ct.Ma_Lo = lh.Ma_Lo ");

        sql.append("WHERE CAST(hd.NgayLap AS DATE) BETWEEN ? AND ? AND hd.TrangThai = 3 ");

        if (maDanhMuc != null && !maDanhMuc.equalsIgnoreCase("ALL")) {
            sql.append("AND sp.Ma_DM = ? ");
        }

        sql.append("GROUP BY sp.Ma_SP");

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setDate(1, new java.sql.Date(tuNgay.getTime()));
            ps.setDate(2, new java.sql.Date(denNgay.getTime()));

            if (maDanhMuc != null && !maDanhMuc.equalsIgnoreCase("ALL")) {
                ps.setString(3, maDanhMuc);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKe_DTO dto = new ThongKe_DTO();
                    String maSP = rs.getString("Ma_SP");
                    int slBan = rs.getInt("TongSLBan");
                    double tongDoanhThu = rs.getDouble("TongDoanhThu");
                    double tongTienNhap = rs.getDouble("TongTienNhap");
                    double giaBanTB = (slBan > 0) ? (tongDoanhThu / slBan) : 0;
                    double giaNhapLeTB = (slBan > 0) ? (tongTienNhap / slBan) : 0;

                    dto.setMaSanPham(maSP);
                    dto.setSoLuongBan(slBan);
                    dto.setGiaBan(giaBanTB);
                    dto.setGiaNhap(giaNhapLeTB);
                    dto.setLoiNhuan(tongDoanhThu - tongTienNhap);

                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<ThongKeKhachHang_DTO> thongKeKhachHangVIP(Date tuNgay, Date denNgay, String hangThanhVien) {
        List<ThongKeKhachHang_DTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT kh.Ma_KH, kh.Ten_KH, kh.SDT, kh.Hang, ");
            sql.append("COUNT(hd.Ma_HDB) AS SoLanMuaHang, ");
            sql.append("SUM(hd.ThanhTien) AS TongTienChiTieu ");
            sql.append("FROM KHACHHANG kh ");
            sql.append("JOIN HOADONBAN hd ON kh.Ma_KH = hd.Ma_KH ");
            sql.append("WHERE CAST(hd.NgayLap AS DATE) BETWEEN ? AND ? ");

            boolean locTheoHang = hangThanhVien != null && !hangThanhVien.equalsIgnoreCase("Tất cả");
            if (locTheoHang) {
                sql.append("AND kh.Hang = ? ");
            }
            sql.append("GROUP BY kh.Ma_KH, kh.Ten_KH, kh.SDT, kh.Hang ");
            sql.append("ORDER BY TongTienChiTieu DESC");

            ps = conn.prepareStatement(sql.toString());
            ps.setDate(1, new java.sql.Date(tuNgay.getTime()));
            ps.setDate(2, new java.sql.Date(denNgay.getTime()));

            if (locTheoHang) {
                ps.setString(3, hangThanhVien);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                ThongKeKhachHang_DTO dto = new ThongKeKhachHang_DTO();
                dto.setMaKH(rs.getString("Ma_KH"));
                dto.setTenKH(rs.getString("Ten_KH"));
                dto.setSdt(rs.getString("SDT"));
                dto.setXepHang(rs.getString("Hang"));
                dto.setSoLanMuaHang(rs.getInt("SoLanMuaHang"));
                dto.setTongTienChiTieu(rs.getDouble("TongTienChiTieu"));

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