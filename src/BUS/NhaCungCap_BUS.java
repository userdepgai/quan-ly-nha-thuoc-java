package BUS;

import DAO.NhaCungCap_DAO;
import dto.NhaCungCap_DTO;
import java.util.ArrayList;

public class NhaCungCap_BUS {

    private NhaCungCap_DAO dao;

    public NhaCungCap_BUS() {
        dao = new NhaCungCap_DAO();
    }

    public ArrayList<NhaCungCap_DTO> getAll() {
        return dao.getAll();
    }

    // Thêm nhà cung cấp
    public boolean insert(NhaCungCap_DTO ncc) {
        if (ncc.getMaNCC() == null || ncc.getMaNCC().trim().isEmpty()) {
            System.out.println("Mã nhà cung cấp không được để trống");
            return false;
        }
        if (ncc.getTenNCC() == null || ncc.getTenNCC().trim().isEmpty()) {
            System.out.println("Tên nhà cung cấp không được để trống");
            return false;
        }
        if (ncc.getSdt() == null || ncc.getSdt().trim().isEmpty()) {
            System.out.println("Số điện thoại không được để trống");
            return false;
        }

        return dao.insert(ncc);
    }

    // Cập nhật nhà cung cấp
    public boolean update(NhaCungCap_DTO ncc) {
        if (ncc.getMaNCC() == null || ncc.getMaNCC().trim().isEmpty()) {
            System.out.println("Mã nhà cung cấp không hợp lệ");
            return false;
        }
        if (ncc.getTenNCC() == null || ncc.getTenNCC().trim().isEmpty()) {
            System.out.println("Tên nhà cung cấp không được để trống");
            return false;
        }
        if (ncc.getSdt() == null || ncc.getSdt().trim().isEmpty()) {
            System.out.println("Số điện thoại không được để trống");
            return false;
        }

        return dao.update(ncc);
    }

    // Cập nhật trạng thái nhà cung cấp
    public boolean updateTrangThai(String maNCC, int trangThaiMoi) {
        if (maNCC == null || maNCC.trim().isEmpty()) {
            System.out.println("Mã nhà cung cấp không hợp lệ");
            return false;
        }


        if (trangThaiMoi != 0 && trangThaiMoi != 1) {
            System.out.println("Trạng thái không hợp lệ");
            return false;
        }

        return dao.updateTrangThai(maNCC, trangThaiMoi);
    }
}