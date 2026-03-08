package bus;

import dao.ThongKe_DAO;
import dto.ThongKeKhachHang_DTO;
import dto.ThongKe_DTO;
import java.util.Date;
import java.util.List;

public class ThongKe_BUS {
    private ThongKe_DAO thongKeDAO;

    public ThongKe_BUS() {
        thongKeDAO = new ThongKe_DAO();
    }

    public List<ThongKe_DTO> thongKeDoanhThu(Date tuNgay, Date denNgay, String maDanhMuc) {

        if (tuNgay == null || denNgay == null) {
            return null;
        }

        if (tuNgay.after(denNgay)) {
            return null;
        }

        return thongKeDAO.thongKeDoanhThu(tuNgay, denNgay, maDanhMuc);
    }
    public List<ThongKeKhachHang_DTO> thongKeKhachHangVIP(Date tuNgay, Date denNgay, String hangThanhVien) {
        if (tuNgay == null || denNgay == null) {
            return null;
        }
        if (tuNgay.after(denNgay)) {
            return null;
        }
        return thongKeDAO.thongKeKhachHangVIP(tuNgay, denNgay, hangThanhVien);
    }
}