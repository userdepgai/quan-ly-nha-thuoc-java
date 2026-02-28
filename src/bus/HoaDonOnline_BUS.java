package bus;

import dao.*;
import dto.*;

import java.time.LocalDateTime;
import java.util.ArrayList;



  //HOA DON ONLINE BUS


public class HoaDonOnline_BUS {

    private HoaDonBan_DAO hoaDonDAO = new HoaDonBan_DAO();
    private ChiTietHoaDonBan_DAO ctDAO = new ChiTietHoaDonBan_DAO();
    private LoHang_DAO loDAO = new LoHang_DAO();
    private KhachHang_DAO khDAO = new KhachHang_DAO();


       //KHÁCH ĐẶT ONLINE → AUTO TẠO HÓA ĐƠN
    public String taoDonOnline(
            String maKH,
            ArrayList<ChiTietHoaDonBan_DTO> dsCT
    ) {

        HoaDonBan_DTO hd = new HoaDonBan_DTO();

        String maHD = hoaDonDAO.getNextID();

        hd.setMa(maHD);
        hd.setMaKhachHang(maKH);
        hd.setLoaiHDB(1); // ONLINE
        hd.setTrangThai(0); // CHỜ DUYỆT
        hd.setTinhTrangThanhToan(0);
        hd.setNgayLap(LocalDateTime.now());

        double tong = 0;

        for (ChiTietHoaDonBan_DTO ct : dsCT) {
            tong += ct.getThanhTien();
        }

        hd.setTongTienGoc(tong);
        hd.setThanhTien(tong);

        hoaDonDAO.insert(hd);

        for (ChiTietHoaDonBan_DTO ct : dsCT) {
            ct.setMaHDB(maHD);
            ctDAO.insert(ct);
        }

        return maHD;
    }


       //NHÂN VIÊN DUYỆT ĐƠN

    public void duyetDon(String maHD) {

        HoaDonBan_DTO hd = hoaDonDAO.getById(maHD);

        if (hd == null)
            throw new RuntimeException("Không tìm thấy hóa đơn");

        if (hd.getTrangThai() != 0)
            throw new RuntimeException("Đơn không hợp lệ");

        ArrayList<ChiTietHoaDonBan_DTO> ds =
                ctDAO.getByMaHD(maHD);

        // kiểm tra tồn kho
        for (ChiTietHoaDonBan_DTO ct : ds) {

            boolean duHang =
                    loDAO.kiemTraTonKho(ct.getMaSP(), ct.getSoLuong());

            if (!duHang)
                throw new RuntimeException(
                        "Không đủ hàng: " + ct.getMaSP());
        }

        // trừ kho FIFO
        for (ChiTietHoaDonBan_DTO ct : ds) {
            loDAO.truKhoFIFO(ct.getMaSP(), ct.getSoLuong());
        }

        hoaDonDAO.capNhatTrangThai(maHD, 1); // ĐÃ DUYỆT
    }


      // TỪ CHỐI ĐƠN

    public void tuChoiDon(String maHD) {

        hoaDonDAO.capNhatTrangThai(maHD, -1);
    }


      // GIAO HÀNG

    public void giaoHang(String maHD) {

        HoaDonBan_DTO hd = hoaDonDAO.getById(maHD);

        if (hd.getTrangThai() != 1)
            throw new RuntimeException("Chưa duyệt");

        hoaDonDAO.capNhatTrangThai(maHD, 2); // ĐANG GIAO
    }


       //HOÀN THÀNH (AUTO CHUYỂN)

    public void hoanThanh(String maHD) {

        HoaDonBan_DTO hd = hoaDonDAO.getById(maHD);

        if (hd.getTrangThai() != 2)
            throw new RuntimeException("Chưa giao");

        hoaDonDAO.capNhatTrangThai(maHD, 3);

        congDiem(hd);
    }


      // HOÀN HÀNG (<=7 NGÀY)

    public void hoanHang(String maHD) {

        HoaDonBan_DTO hd = hoaDonDAO.getById(maHD);

        if (hd.getTrangThai() != 3)
            throw new RuntimeException("Chưa hoàn thành");

        if (hd.getNgayHoanThanh()
                .plusDays(7)
                .isBefore(LocalDateTime.now()))
            throw new RuntimeException("Quá hạn hoàn");

        ArrayList<ChiTietHoaDonBan_DTO> ds =
                ctDAO.getByMaHD(maHD);

        // trả kho
        for (ChiTietHoaDonBan_DTO ct : ds) {
            loDAO.congKho(ct.getMaSP(), ct.getSoLuong());
        }

        // trừ điểm KH
        int diem = (int)(hd.getThanhTien() / 10000);
        khDAO.truDiem(hd.getMaKhachHang(), diem);

        hoaDonDAO.capNhatTrangThai(maHD, -2); // HOÀN
    }


       //AUTO HỦY SAU 3 NGÀY
    public void autoHuyDonTre() {

        ArrayList<HoaDonBan_DTO> ds = hoaDonDAO.getAll();

        for (HoaDonBan_DTO hd : ds) {

            if (hd.getLoaiHDB() == 1 &&
                    hd.getTrangThai() == 0 &&
                    hd.getNgayLap()
                            .plusDays(3)
                            .isBefore(LocalDateTime.now())) {

                hoaDonDAO.capNhatTrangThai(hd.getMa(), -3);
            }
        }
    }


      // CỘNG ĐIỂM SAU KHI HOÀN THÀNH

    private void congDiem(HoaDonBan_DTO hd) {

        int diem = (int)(hd.getThanhTien() / 10000);

        khDAO.congDiem(hd.getMaKhachHang(), diem);
    }
}
