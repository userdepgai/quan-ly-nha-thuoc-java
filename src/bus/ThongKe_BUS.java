package bus;

import dao.ThongKe_DAO;
import dto.ThongKe_DTO;
import java.util.List;

public class ThongKe_BUS {
    private ThongKe_DAO thongKeDAO;

    public ThongKe_BUS() {
        thongKeDAO = new ThongKe_DAO();
    }

    public List<ThongKe_DTO> thongKeDoanhThu(java.util.Date tuNgay, java.util.Date denNgay) {
        // Có thể thêm code kiểm tra logic ở đây (ví dụ: tuNgay có lớn hơn denNgay không)
        if (tuNgay.after(denNgay)) {
            return null; // Ngày bắt đầu không thể sau ngày kết thúc
        }
        return thongKeDAO.thongKeDoanhThu(tuNgay, denNgay);
    }
}