package dao;

import DBConnection.DBConnection;
import dto.ThuocTinhDanhMuc_DTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ThuocTinhDanhMuc_DAO {

    // 1. Lấy toàn bộ thuộc tính danh mục
    public ArrayList<ThuocTinhDanhMuc_DTO> getAll() {
        ArrayList<ThuocTinhDanhMuc_DTO> list = new ArrayList<>();
        String sql = "SELECT * FROM THUOCTINHDANHMUC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ThuocTinhDanhMuc_DTO tt = new ThuocTinhDanhMuc_DTO(
                        rs.getString("MaThuocTinh"),
                        rs.getString("Ten_TT"),
                        rs.getInt("Kieu_TT"),
                        rs.getInt("TrangThai"),
                        rs.getString("Ma_DM")
                );
                list.add(tt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Tự động sinh mã tiếp theo (TT001 -> TT002)
    public String getNextId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(MaThuocTinh, 3, LEN(MaThuocTinh)-2) AS INT)) FROM THUOCTINHDANHMUC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int number = rs.getInt(1);
                if (rs.wasNull()) return "TT001";
                return String.format("TT%03d", number + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "TT001";
    }

    // 3. Thêm mới
    public boolean them(ThuocTinhDanhMuc_DTO tt) {
        String sql = "INSERT INTO THUOCTINHDANHMUC (MaThuocTinh, Ten_TT, Kieu_TT, TrangThai, Ma_DM) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tt.getMaThuocTinh());
            ps.setString(2, tt.getTenThuocTinh());
            ps.setInt(3, tt.getKieuThuocTinh());
            ps.setInt(4, tt.getTrangThai());
            ps.setString(5, tt.getMaDM());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Cập nhật
    public boolean capNhat(ThuocTinhDanhMuc_DTO tt) {
        String sql = "UPDATE THUOCTINHDANHMUC SET Ten_TT=?, Kieu_TT=?, TrangThai=?, Ma_DM=? WHERE MaThuocTinh=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tt.getTenThuocTinh());
            ps.setInt(2, tt.getKieuThuocTinh());
            ps.setInt(3, tt.getTrangThai());
            ps.setString(4, tt.getMaDM());
            ps.setString(5, tt.getMaThuocTinh());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}