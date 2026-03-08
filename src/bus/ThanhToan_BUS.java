package bus;

import dao.ThanhToan_DAO;
import dto.*;

import java.util.ArrayList;
import java.util.List;

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

    public String getTenSanPham(String maSP) {

        SanPham_DTO sp = SanPham_BUS.getInstance().getById(maSP);

        if (sp != null) {
            return sp.getTenSP();
        }

        return "Sản phẩm không tồn tại";
    }

    public double getGiaSanPham(String maSP) {
        return HoaDonBan_BUS.getInstance().getGiaBanSP(maSP, 1);
    }

    public List<ChiTietHoaDonBan_DTO> taoDanhSachChiTiet(List<ChiTietGioHang_DTO> dsMua) {

        List<ChiTietHoaDonBan_DTO> list = new ArrayList<>();

        for (ChiTietGioHang_DTO item : dsMua) {

            ChiTietHoaDonBan_DTO ct = new ChiTietHoaDonBan_DTO();

            ct.setMaSP(item.getMaSP());
            ct.setMaLo(item.getMaLo());
            ct.setSoLuong(item.getSoLuong());

            double gia = getGiaSanPham(item.getMaSP());

            ct.setGiaBan(gia);
            ct.setGiaBanSauApKM(gia);
            ct.setThanhTien(gia * item.getSoLuong());

            list.add(ct);
        }

        return list;
    }
    public String taoDonOnline(String sdtTK,
                               String diaChi,
                               ArrayList<ChiTietHoaDonBan_DTO> dsCT) {

        KhachHang_DTO kh =
                KhachHang_BUS.getInstance().getBysdt(sdtTK);

        if (kh == null) {
            return null;
        }

        String maKH = kh.getMa();

        return HoaDonOnline_BUS.getInstance()
                .taoDonOnline(maKH, diaChi, dsCT);
    }
}