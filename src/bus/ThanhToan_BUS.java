package bus;

import dao.*;
import dto.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ThanhToan_BUS {
    private LoHang_DAO loHangDAO = new LoHang_DAO();
    private KhachHang_DAO khachHangDAO;
    private ThanhToan_DAO thanhToanDAO;
    private ChiTietHoaDonBan_DAO chiTietHoaDonDAO;

    public ThanhToan_BUS() {
        this.khachHangDAO = new KhachHang_DAO();
        this.thanhToanDAO = new ThanhToan_DAO();
        this.chiTietHoaDonDAO = new ChiTietHoaDonBan_DAO();
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

    public ArrayList<ChiTietHoaDonBan_DTO> taoDanhSachChiTiet(List<ChiTietGioHang_DTO> dsMua) {

        ArrayList<ChiTietHoaDonBan_DTO> list = new ArrayList<>();

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
                               String tenKH,
                               String diaChiKH,
                               String maDiaChi,
                               ArrayList<ChiTietHoaDonBan_DTO> dsCT,
                               int tinhTrangThanhToan) {

        if (dsCT == null || dsCT.isEmpty()) {
            return null;
        }

        String maKH = null;

        ArrayList<KhachHang_DTO> dsKhachHang = khachHangDAO.getAll();

        if (dsKhachHang != null) {
            for (KhachHang_DTO kh : dsKhachHang) {
                if (kh.getSdt() != null && kh.getSdt().equals(sdtTK)) {
                    maKH = kh.getMa();
                    break;
                }
            }
        }
        if (maKH == null) {

            maKH = khachHangDAO.getNextId();

            KhachHang_DTO khMoi = new KhachHang_DTO(
                    maKH,
                    tenKH != null && !tenKH.isEmpty() ? tenKH : "Khách hàng " + sdtTK,
                    sdtTK,
                    LocalDate.of(2000, 1, 1),
                    true,
                    0,
                    0,
                    "Đồng",
                    LocalDate.now()
            );

            khachHangDAO.them(khMoi);
        }
        for (ChiTietHoaDonBan_DTO ct : dsCT) {
            ct.setMaLo(null);
        }


        try {

            String maHDB = HoaDonOnline_BUS
                    .getInstance()
                    .taoDonOnline(
                            maKH,
                            diaChiKH,
                            maDiaChi,
                            dsCT,
                            tinhTrangThanhToan
                    );
            return maHDB;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<ChiTietHoaDonBan_DTO> getChiTietHoaDon(String maHDB) {
        return chiTietHoaDonDAO.getByMaHD(maHDB);
    }
    private String timLoConHang(String maSP){

        ArrayList<LoHang_DTO> list = loHangDAO.getAll();

        if(list == null) return null;

        for(LoHang_DTO lo : list){

            if(lo.getMaSp().equals(maSP) && lo.getSoLuongConLai() > 0){
                return lo.getMaLo();
            }
        }

        return null;
    }
}