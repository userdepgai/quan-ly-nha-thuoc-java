package dao;

import dto.HoaDonBan_DTO;
import DBConnection.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class HoaDonBan_DAO {

    // ================= GET ALL =================
    public ArrayList<HoaDonBan_DTO> getAll() {

        ArrayList<HoaDonBan_DTO> list = new ArrayList<>();

        String sql = "SELECT * FROM HOADONBAN ORDER BY NgayLap DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapHoaDon(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= GET BY ID =================
    public HoaDonBan_DTO getById(String maHD) {

        String sql = "SELECT * FROM HOADONBAN WHERE Ma_HDB=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return mapHoaDon(rs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================= NEXT ID =================
    public String getNextID() {

        String sql = "SELECT MAX(Ma_HDB) FROM HOADONBAN";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                String lastID = rs.getString(1);

                if (lastID == null)
                    return "HDB00000001";

                int num = Integer.parseInt(lastID.substring(3));
                return String.format("HDB%08d", num + 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "HDB00000001";
    }

    // ================= INSERT =================
    public boolean insert(HoaDonBan_DTO hd) {

        try (Connection conn = DBConnection.getConnection()) {
            return insert(conn, hd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ================= INSERT TRANSACTION =================
    public boolean insert(Connection conn, HoaDonBan_DTO hd)
            throws SQLException {

        String sql = """
            INSERT INTO HOADONBAN(
                Ma_HDB,
                NgayLap,
                TinhTrangThanhToan,
                KeToa,
                TongTienGoc,
                TongGiaTri_KM,
                DiemThuongQuyDoi,
                ThanhTien,
                TienNhan,
                TienThoi,
                GhiChu,
                ThueVAT,
                TrangThai,
                Ma_NV,
                Ma_KH,
                MaVoucher,
                LoaiHDB
            )
            VALUES ( ?,GETDATE(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )
            """;

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, hd.getMa());
        ps.setInt(2, hd.getTinhTrangThanhToan());
        ps.setBoolean(3, hd.isKeToa());
        ps.setDouble(4, hd.getTongTienGoc());
        ps.setDouble(5, hd.getTongGiaTriKhuyenMai());
        ps.setInt(6, hd.getDiemThuongQuyDoi());
        ps.setDouble(7, hd.getThanhTien());
        ps.setDouble(8, hd.getTienNhan());
        ps.setDouble(9, hd.getTienThoi());
        ps.setString(10, hd.getGhiChu());
        ps.setDouble(11, hd.getThueVAT());
        ps.setInt(12, hd.getTrangThai());
        ps.setString(13, hd.getMaNhanVien());
        ps.setString(14, hd.getMaKhachHang());
        ps.setString(15, hd.getMaVoucher());
        ps.setInt(16, hd.getLoaiHDB());

        return ps.executeUpdate() > 0;
    }

    // ================= UPDATE TRANG THAI =================
    public boolean capNhatTrangThai(String maHD, int trangThai) {

        String sql = """
            UPDATE HOADONBAN
            SET TrangThai=?,
                NgayHoanThanh =
                    CASE WHEN ?=1 THEN GETDATE()
                         ELSE NgayHoanThanh END
            WHERE Ma_HDB=?
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, trangThai);
            ps.setInt(2, trangThai);
            ps.setString(3, maHD);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= MAP RESULTSET =================
    private HoaDonBan_DTO mapHoaDon(ResultSet rs) throws SQLException {

        HoaDonBan_DTO hd = new HoaDonBan_DTO();

        hd.setMa(rs.getString("Ma_HDB"));

        Timestamp lap = rs.getTimestamp("NgayLap");
        if (lap != null)
            hd.setNgayLap(lap.toLocalDateTime());

        Timestamp ht = rs.getTimestamp("NgayHoanThanh");
        if (ht != null)
            hd.setNgayHoanThanh(ht.toLocalDateTime());

        hd.setTrangThai(rs.getInt("TrangThai"));
        hd.setTinhTrangThanhToan(rs.getInt("TinhTrangThanhToan"));
        hd.setTongTienGoc(rs.getDouble("TongTienGoc"));
        hd.setTongGiaTriKhuyenMai(rs.getDouble("TongGiaTri_KM"));
        hd.setThanhTien(rs.getDouble("ThanhTien"));
        hd.setTienNhan(rs.getDouble("TienNhan"));
        hd.setTienThoi(rs.getDouble("TienThoi"));
        hd.setThueVAT(rs.getDouble("ThueVAT"));
        hd.setGhiChu(rs.getString("GhiChu"));
        hd.setLoaiHDB(rs.getInt("LoaiHDB"));
        hd.setMaNhanVien(rs.getString("Ma_NV"));
        hd.setMaKhachHang(rs.getString("Ma_KH"));
        hd.setMaVoucher(rs.getString("MaVoucher"));
        hd.setKeToa(rs.getBoolean("KeToa"));

        return hd;
    }
}