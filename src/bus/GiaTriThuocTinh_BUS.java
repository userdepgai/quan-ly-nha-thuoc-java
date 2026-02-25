package bus;

import dao.GiaTriThuocTinh_DAO;
import dto.GiaTriThuocTinh_DTO;
import java.util.ArrayList;

public class GiaTriThuocTinh_BUS {
    private GiaTriThuocTinh_DAO gtDAO;

    public GiaTriThuocTinh_BUS() {
        gtDAO = new GiaTriThuocTinh_DAO();
    }

    // Lấy tất cả
    public ArrayList<GiaTriThuocTinh_DTO> getAll() {
        return gtDAO.getAll();
    }

    // Lấy theo Mã Thuộc Tính (Dùng để load vào bảng con khi click bảng cha)
    public ArrayList<GiaTriThuocTinh_DTO> getByMaThuocTinh(String maThuocTinh) {
        if (maThuocTinh == null || maThuocTinh.trim().isEmpty()) {
            return new ArrayList<>(); // Trả về mảng rỗng nếu mã trống
        }
        return gtDAO.getByMaThuocTinh(maThuocTinh);
    }

    // Sinh mã tự động
    public String getNextId() {
        return gtDAO.getNextId();
    }

    // Thêm (Có kiểm tra rỗng)
    public boolean them(GiaTriThuocTinh_DTO gt) {
        if (gt.getNdGiaTri().trim().isEmpty() || gt.getMaThuocTinh().trim().isEmpty()) {
            return false; // Không cho thêm nếu nội dung bị rỗng
        }
        return gtDAO.them(gt);
    }

    // Cập nhật
    public boolean capNhat(GiaTriThuocTinh_DTO gt) {
        if (gt.getNdGiaTri().trim().isEmpty()) {
            return false;
        }
        return gtDAO.capNhat(gt);
    }
}