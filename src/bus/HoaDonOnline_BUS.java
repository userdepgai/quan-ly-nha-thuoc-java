package bus;

import dto.HoaDonOnline_DTO;

import java.time.LocalDateTime;

public class HoaDonOnline_BUS extends HoaDonBan_BUS {

    private static HoaDonOnline_BUS instance;

    public static HoaDonOnline_BUS getInstance() {
        if (instance == null)
            instance = new HoaDonOnline_BUS();
        return instance;
    }

    /* =========================================================
                    TẠO ĐƠN ONLINE
    ========================================================= */
    public HoaDonOnline_DTO taoDonOnline(String maKH) {

        HoaDonOnline_DTO hd = new HoaDonOnline_DTO();


        hd.setMa(hoaDonDAO.getNextID());

        hd.setNgayLap(LocalDateTime.now());

        hd.setMaKhachHang(maKH);

        hd.setTrangThai(0); // CHỜ DUYỆT
        hd.setTinhTrangThanhToan(0);
        hd.setLoaiHDB(1); // ONLINE

        dsChiTietTam.clear();

        return hd;
    }

    /* =========================================================
                    DUYỆT HÓA ĐƠN
                    ⭐ TRỪ LÔ TẠI ĐÂY
    ========================================================= */
    public boolean duyetHoaDon(HoaDonOnline_DTO hd, String maNVDuyet) {

        hd.setTrangThai(1);
        hd.setMaNhanVien(maNVDuyet);

        for (var ct : dsChiTietTam) {

            // TODO khi có Lo_BUS
            // Lo_BUS.getInstance()
            //      .truSoLuong(ct.getMaLo(), ct.getSoLuong());
        }

        // update DB
        hoaDonDAO.capNhatTrangThai(hd.getMa(), 1);

        return true;
    }

    /* =========================================================
                    GIAO HÀNG
    ========================================================= */
    public void giaoHang(HoaDonOnline_DTO hd) {

        hd.setTrangThai(2);
        hoaDonDAO.capNhatTrangThai(hd.getMa(), 2);
    }

    /* =========================================================
                    HOÀN THÀNH
    ========================================================= */
    public void hoanThanh(HoaDonOnline_DTO hd) {

        hd.setTrangThai(3);
        hd.setNgayHoanThanh(LocalDateTime.now());

        hoaDonDAO.capNhatTrangThai(hd.getMa(), 3);
    }

    /* =========================================================
                    YÊU CẦU HOÀN HÀNG
    ========================================================= */
    public void yeuCauHoanHang(HoaDonOnline_DTO hd) {

        if (hd.getNgayHoanThanh() == null) return;

        long soNgay =
                java.time.Duration.between(
                        hd.getNgayHoanThanh(),
                        LocalDateTime.now()
                ).toDays();

        if (soNgay <= 7) {
            hd.setTrangThai(5);
            hoaDonDAO.capNhatTrangThai(hd.getMa(), 5);
        }
    }

    /* =========================================================
                    DUYỆT HOÀN HÀNG
    ========================================================= */
    public void duyetHoanHang(HoaDonOnline_DTO hd) {

        // TODO:
        // Lo_BUS.getInstance().congSoLuong()

        hd.setTinhTrangThanhToan(2); // đã hoàn tiền

        hoaDonDAO.capNhatTrangThai(hd.getMa(), 6);
    }
}