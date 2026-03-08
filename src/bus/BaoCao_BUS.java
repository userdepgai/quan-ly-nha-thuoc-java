package bus;

import dao.BaoCao_DAO;
import dto.BaoCaoDoanhThu_DTO;
import dto.BaoCaoTonKho_DTO;

import java.util.Date;
import java.util.List;

public class BaoCao_BUS {

    private BaoCao_DAO baoCaoDAO = new BaoCao_DAO();

    public List<BaoCaoTonKho_DTO> baoCaoTonKho(Date tuNgay, Date denNgay, String maDanhMuc) {

        if (tuNgay == null || denNgay == null) {
            return null;
        }

        if (tuNgay.after(denNgay)) {
            return null;
        }

        return baoCaoDAO.baoCaoTonKho(tuNgay, denNgay, maDanhMuc);
    }
    public List<BaoCaoDoanhThu_DTO> baoCaoDoanhThuTheoNgay(Date tuNgay, Date denNgay) {
        if (tuNgay == null || denNgay == null || tuNgay.after(denNgay)) {
            return null;
        }
        return baoCaoDAO.baoCaoDoanhThuTheoNgay(tuNgay, denNgay);
    }
}