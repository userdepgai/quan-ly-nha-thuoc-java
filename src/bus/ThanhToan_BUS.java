package bus;

import dao.ThanhToan_DAO;
import dto.ThanhToan_DTO;

public class ThanhToan_BUS {

    private ThanhToan_DAO thanhToanDAO;

    public ThanhToan_BUS() {
        this.thanhToanDAO = new ThanhToan_DAO();
    }

    public ThanhToan_DTO layThongTinKhachHang(String idTaiKhoan) {
        if (idTaiKhoan == null || idTaiKhoan.trim().isEmpty()) {
            return null;
        }

        return thanhToanDAO.layThongTinKhachHang(idTaiKhoan);
    }
}