package dao;

import DBConnection.DBConnection;
import dto.ThongKeKhachHang_DTO;
import dto.ThongKe_DTO;
//import bus.HoaDon_BUS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThongKe_DAO {

    private DBConnection dbConnection = new DBConnection();

    public List<ThongKe_DTO> thongKeDoanhThu(Date tuNgay, Date denNgay, String maDanhMuc) {
        List<ThongKe_DTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT sp.Ma_SP, sp.LoiNhuan, ");
            sql.append("MAX(lh.GiaNhap) AS GiaNhapGoc, ");
            sql.append("MAX(qc.SLSP_Thung) AS QuyCach, ");
            sql.append("SUM(ct.SoLuong) AS TongSLBan, ");
            sql.append("MAX(ct.GiaBan) AS GiaBanThucTe ");

            sql.append("FROM HOADONBAN hd ");
            sql.append("JOIN CHITIETHOADON ct ON hd.Ma_HDB = ct.Ma_HDB ");
            sql.append("JOIN LOHANG lh ON ct.Ma_Lo = lh.Ma_Lo ");
            sql.append("JOIN SANPHAM sp ON ct.Ma_SP = sp.Ma_SP ");
            sql.append("JOIN QUYCACH qc ON sp.Ma_QC = qc.Ma_QC ");
            sql.append("WHERE CAST(hd.NgayLap AS DATE) BETWEEN ? AND ? ");

            if (maDanhMuc != null && !maDanhMuc.equalsIgnoreCase("ALL")) {
                sql.append("AND sp.Ma_DM = ? ");
            }
            sql.append("GROUP BY sp.Ma_SP, sp.LoiNhuan");

            ps = conn.prepareStatement(sql.toString());
            ps.setDate(1, new java.sql.Date(tuNgay.getTime()));
            ps.setDate(2, new java.sql.Date(denNgay.getTime()));

            if (maDanhMuc != null && !maDanhMuc.equalsIgnoreCase("ALL")) {
                ps.setString(3, maDanhMuc);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                String maSP = rs.getString("Ma_SP");
                double giaNhapDonVi = rs.getDouble("GiaNhapGoc") / rs.getInt("QuyCach");
                int slBanLe = rs.getInt("TongSLBan");
                //double giaBanNiemYet = HoaDon_BUS.getInstance().getGiaSanPham(maSP);
                double tienLoiNhuan = (giaBanNiemYet - giaNhapDonVi) * slBanLe;

                ThongKe_DTO dto = new ThongKe_DTO();
                dto.setMaSanPham(maSP);
                dto.setGiaNhap(giaNhapDonVi);
                //dto.setGiaBan(giaBanNiemYet);
                dto.setSoLuongBan(slBanLe);
                dto.setLoiNhuan(tienLoiNhuan);

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