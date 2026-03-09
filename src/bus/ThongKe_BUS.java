package bus;

import dao.ThongKe_DAO;
import dto.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ThongKe_BUS {
    private ThongKe_DAO thongKeDAO;
    private SanPham_BUS spBus = SanPham_BUS.getInstance();
    protected LoHang_BUS loBus = LoHang_BUS.getInstance();
    private QuyCach_BUS qcBus = QuyCach_BUS.getInstance();

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

    public double tinhGiaBanGoiY(String maSP, double giaNhapLo, int soLuongThungLo) {
        SanPham_DTO sp = spBus.getById(maSP);
        QuyCach_DTO qc = qcBus.getById(sp.getMaQC());

        double giaNhap1Thung = giaNhapLo / soLuongThungLo;
        double giaNhapDonVi = giaNhap1Thung / qc.getSlspThung();

        return giaNhapDonVi * sp.getLoiNhuan();
    }
}