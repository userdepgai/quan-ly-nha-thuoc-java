package dao;

import dto.HoaDonBan_DTO;
import DBConnection.DBConnection;
import dto.HoaDonOnline_DTO;

import java.sql.*;
import java.util.ArrayList;

public class HoaDonBan_DAO {

    public ArrayList<HoaDonBan_DTO> getAll() {

        ArrayList<HoaDonBan_DTO> list = new ArrayList<>();

        String sql = """
                SELECT hdb.*,
                       kh.Ten_KH,
                       kh.SDT
                FROM HOADONBAN hdb
                LEFT JOIN KHACHHANG kh
                    ON hdb.Ma_KH = kh.Ma_KH
                ORDER BY hdb.NgayLap  ASC
            """;

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
    public ArrayList<HoaDonOnline_DTO> getDanhSachDuyetOnline() {

        ArrayList<HoaDonOnline_DTO> ds = new ArrayList<>();

        String sql = """
                SELECT hdb.*, kh.Ten_KH, kh.SDT
                        FROM HOADONBAN hdb
                        LEFT JOIN KHACHHANG kh
                             ON hdb.Ma_KH = kh.Ma_KH
                        WHERE hdb.LoaiHDB = 1
                        AND hdb.TrangThai IN (0,1,2,5)
                        ORDER BY hdb.TrangThai ASC, hdb.NgayLap ASC
    """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                HoaDonOnline_DTO hd = new HoaDonOnline_DTO();

                hd.setMa(rs.getString("Ma_HDB"));
                hd.setNgayLap(rs.getTimestamp("NgayLap").toLocalDateTime());
                hd.setMaNhanVien(rs.getString("Ma_NV"));
                hd.setMaKhachHang(rs.getString("Ma_KH"));
                hd.setTrangThai(rs.getInt("TrangThai"));
                hd.setLoaiHDB(rs.getInt("LoaiHDB"));
                hd.setTinhTrangThanhToan(rs.getInt("TinhTrangThanhToan"));
                hd.setThanhTien(rs.getDouble("ThanhTien"));

                ds.add(hd);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }
    public ArrayList<HoaDonOnline_DTO> getDanhSachOnlineTheoKhachHang(String maKH){

        ArrayList<HoaDonOnline_DTO> ds = new ArrayList<>();

        String sql = """
            SELECT *
            FROM HOADONBAN
            WHERE LoaiHDB = 1
            AND Ma_KH = ?
            ORDER BY NgayLap DESC
            """;

        try(
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ){

            ps.setString(1, maKH);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                HoaDonOnline_DTO hd = new HoaDonOnline_DTO();

                hd.setMa(rs.getString("Ma_HDB"));
                hd.setNgayLap(rs.getTimestamp("NgayLap").toLocalDateTime());
                hd.setMaNhanVien(rs.getString("Ma_NV"));
                hd.setMaKhachHang(rs.getString("Ma_KH"));
                hd.setTrangThai(rs.getInt("TrangThai"));
                hd.setLoaiHDB(rs.getInt("LoaiHDB"));
                hd.setTinhTrangThanhToan(rs.getInt("TinhTrangThanhToan"));
                hd.setThanhTien(rs.getDouble("ThanhTien"));
                hd.setPhiVanChuyen(rs.getDouble("PhiVanChuyen"));
                hd.setMaDiaChiGiaoHang(rs.getString("Ma_DC"));

                ds.add(hd);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return ds;
    }
    public String getNextID() {

        String sql = "SELECT MAX(Ma_HDB) FROM HOADONBAN";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                String lastID = rs.getString(1);

                if (lastID == null)
                    return "HDB000001";

                int num = Integer.parseInt(lastID.substring(3));
                return String.format("HDB%06d", num + 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "HDB000001";
    }

    public boolean insert(HoaDonBan_DTO hd) {

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
                   PhiVanChuyen,
                   ThueVAT,
                   TrangThai,
                   LoaiHDB,
                   Ma_NV,
                   Ma_KH,
                   MaVoucher,
                   Ma_DC
            )
            VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hd.getMa());
            ps.setTimestamp(2, Timestamp.valueOf(hd.getNgayLap()));
            ps.setInt(3, hd.getTinhTrangThanhToan());
            ps.setBoolean(4, hd.isKeToa());
            ps.setDouble(5, hd.getTongTienGoc());
            ps.setDouble(6, hd.getTongGiaTriKhuyenMai());
            ps.setInt(7, hd.getDiemThuongQuyDoi());
            ps.setDouble(8, hd.getThanhTien());
            ps.setDouble(9, hd.getTienNhan());
            ps.setDouble(10, hd.getTienThoi());
            ps.setString(11, hd.getGhiChu());

            if (hd instanceof HoaDonOnline_DTO online) {
                ps.setDouble(12, online.getPhiVanChuyen());
                ps.setString(19, online.getMaDiaChiGiaoHang());
            } else {
                ps.setDouble(12, 0);
                ps.setString(19, null);
            }

            ps.setDouble(13, hd.getThueVAT());
            ps.setInt(14, hd.getTrangThai());
            ps.setInt(15, hd.getLoaiHDB());
            ps.setString(16, hd.getMaNhanVien());
            ps.setString(17, hd.getMaKhachHang());
            ps.setString(18, hd.getMaVoucher());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean capNhatTrangThai(String maHD, int trangThai, String maNV) {

        String sql = """
        UPDATE HOADONBAN
        SET TrangThai=?,
            Ma_NV=?,
            NgayHoanThanh =
                CASE WHEN ?=3 THEN GETDATE()
                     ELSE NgayHoanThanh END
        WHERE Ma_HDB=?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, trangThai);

            ps.setString(2, maNV);

            ps.setInt(3, trangThai);

            ps.setString(4, maHD);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean capNhatThanhToan(String maHD, int tinhTrangThanhToan) {

        String sql = """
        UPDATE HOADONBAN
        SET TinhTrangThanhToan = ?
        WHERE Ma_HDB = ?
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, tinhTrangThanhToan);
            ps.setString(2, maHD);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    private HoaDonBan_DTO mapHoaDon(ResultSet rs) throws SQLException {

        int loai = rs.getInt("LoaiHDB");

        HoaDonBan_DTO hd;

        if(loai == 1){
            hd = new HoaDonOnline_DTO();
        }else{
            hd = new HoaDonBan_DTO();
        }

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
        hd.setLoaiHDB(loai);
        hd.setMaNhanVien(rs.getString("Ma_NV"));
        hd.setMaKhachHang(rs.getString("Ma_KH"));
        hd.setMaVoucher(rs.getString("MaVoucher"));
        hd.setKeToa(rs.getBoolean("KeToa"));


        if(hd instanceof HoaDonOnline_DTO online){
            online.setPhiVanChuyen(rs.getDouble("PhiVanChuyen"));
            online.setMaDiaChiGiaoHang(rs.getString("Ma_DC"));
        }

        return hd;
    }

}