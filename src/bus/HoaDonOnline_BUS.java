package bus;

import dto.*;

import java.time.LocalDateTime;
import java.util.ArrayList;


// HOA DON ONLINE BUS
// kế thừa HoaDonBan_BUS
public class HoaDonOnline_BUS extends HoaDonBan_BUS {

    private static HoaDonOnline_BUS instance;

    private HoaDonOnline_BUS(){
        super();
        loadKhachHang();
    }

    public static HoaDonOnline_BUS getInstance(){
        if(instance == null)
            instance = new HoaDonOnline_BUS();
        return instance;
    }


    public ArrayList<HoaDonOnline_DTO> getDanhSachDuyetOnline() {
        return hoaDonDAO.getDanhSachDuyetOnline();
    }
    // =====================================================
    // KHÁCH ĐẶT ONLINE → AUTO TẠO HÓA ĐƠN
    // =====================================================
    public String taoDonOnline(
            String maKH,
            String diaChiGiao,
            ArrayList<ChiTietHoaDonBan_DTO> dsCT
    ){

        HoaDonOnline_DTO hd = new HoaDonOnline_DTO();

        String maHD = getNextID(); // dùng từ BUS CHA

        hd.setMa(maHD);
        hd.setMaKhachHang(maKH);
        hd.setLoaiHDB(1); // ONLINE
        hd.setTrangThai(HoaDonBan_DTO.TT_CHO_DUYET);
        hd.setPhiVanChuyen(50000);
        hd.setTinhTrangThanhToan(0);
        hd.setNgayLap(LocalDateTime.now());

        // ===== THÊM THÔNG TIN ONLINE =====
        hd.setMaDiaChiGiaoHang(diaChiGiao);
        double tong = 0;

        for (ChiTietHoaDonBan_DTO ct : dsCT) {
            tong += ct.getThanhTien();
        }

        hd.setTongTienGoc(tong);
        hd.setThanhTien(tong + hd.getPhiVanChuyen());

        hoaDonDAO.insert(hd);

        for (ChiTietHoaDonBan_DTO ct : dsCT) {
            ct.setMaHDB(maHD);
            ctDAO.insert(ct);
        }

        return maHD;
    }


    // =====================================================
    // NHÂN VIÊN DUYỆT ĐƠN
    // =====================================================
    public void duyetDon(String maHD) {

        HoaDonBan_DTO hd = hoaDonDAO.getById(maHD);

        if (hd == null)
            throw new RuntimeException("Không tìm thấy hóa đơn");

        if (hd.getTrangThai() != HoaDonBan_DTO.TT_CHO_DUYET)
            throw new RuntimeException("Đơn không hợp lệ");

        ArrayList<ChiTietHoaDonBan_DTO> ds =
                ctDAO.getByMaHD(maHD);
    /*
        // kiểm tra tồn kho
        for (ChiTietHoaDonBan_DTO ct : ds) {

            boolean duHang =
                    loDAO.kiemTraTonKho(ct.getMaSP(), ct.getSoLuong());

            if (!duHang)
                throw new RuntimeException(
                        "Không đủ hàng: " + ct.getMaSP());
        }
*/
        // trừ kho FIFO
        for (ChiTietHoaDonBan_DTO ct : ds) {
           // loDAO.truKhoFIFO(ct.getMaSP(), ct.getSoLuong());
        }

        hoaDonDAO.capNhatTrangThai(maHD,
                HoaDonBan_DTO.TT_DA_DUYET);
    }


    // =====================================================
    // TỪ CHỐI ĐƠN
    // =====================================================
    public void huyDonHang(String maHD) {
        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_DA_HUY
        );
    }


    // =====================================================
    // GIAO HÀNG
    // =====================================================
    public void giaoHang(String maHD) {

        HoaDonBan_DTO hd = hoaDonDAO.getById(maHD);

        if (hd.getTrangThai() != HoaDonBan_DTO.TT_DA_DUYET)
            throw new RuntimeException("Chưa duyệt");

        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_DANG_GIAO
        );
    }


    // =====================================================
    // HOÀN THÀNH
    // =====================================================
    public void hoanThanh(String maHD) {

        HoaDonBan_DTO hd = hoaDonDAO.getById(maHD);

        if (hd.getTrangThai() != HoaDonBan_DTO.TT_DANG_GIAO)
            throw new RuntimeException("Chưa giao");

        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_HOAN_THANH
        );

        congDiem(hd);
    }


    // =====================================================
    // HOÀN HÀNG (<=7 NGÀY)
    // =====================================================
    public void hoanHang(String maHD) {

        HoaDonBan_DTO hd = hoaDonDAO.getById(maHD);

        if (hd.getTrangThai() != HoaDonBan_DTO.TT_HOAN_THANH)
            throw new RuntimeException("Chưa hoàn thành");

        if (hd.getNgayHoanThanh()
                .plusDays(7)
                .isBefore(LocalDateTime.now()))
            throw new RuntimeException("Quá hạn hoàn");

        ArrayList<ChiTietHoaDonBan_DTO> ds =
                ctDAO.getByMaHD(maHD);

        for (ChiTietHoaDonBan_DTO ct : ds) {
            //loDAO.congKho(ct.getMaSP(), ct.getSoLuong());
        }

        int diem = (int)(hd.getThanhTien() / 10000);
        khDAO.truDiem(hd.getMaKhachHang(), diem);

        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_YEU_CAU_HOAN
        );
    }


    // =====================================================
    // AUTO HỦY SAU 3 NGÀY
    // =====================================================
    public void autoHuyDonTre() {

        ArrayList<HoaDonBan_DTO> ds = hoaDonDAO.getAll();

        for (HoaDonBan_DTO hd : ds) {

            if (hd.getLoaiHDB() == 1 &&
                    hd.getTrangThai() == HoaDonBan_DTO.TT_CHO_DUYET &&
                    hd.getNgayLap().plusDays(3).isBefore(LocalDateTime.now())) {

                hoaDonDAO.capNhatTrangThai(
                        hd.getMa(),
                        HoaDonBan_DTO.TT_DA_HUY);
            }
        }
    }

    public void capNhatTrangThai(String maHD, int trangThaiMoi){

        switch (trangThaiMoi){

            case HoaDonBan_DTO.TT_DA_DUYET -> duyetDon(maHD);
            case HoaDonBan_DTO.TT_DANG_GIAO -> giaoHang(maHD);
            case HoaDonBan_DTO.TT_HOAN_THANH -> hoanThanh(maHD);
            case HoaDonBan_DTO.TT_DA_HUY -> huyDonHang(maHD);
            case HoaDonBan_DTO.TT_YEU_CAU_HOAN -> hoanHang(maHD);

            default -> throw new RuntimeException("Không thể cập nhật");
        }
    }
    // =====================================================
    // CỘNG ĐIỂM
    // =====================================================
    private void congDiem(HoaDonBan_DTO hd) {

        int diem = (int)(hd.getThanhTien() / 10000);

        khDAO.congDiem(hd.getMaKhachHang(), diem);
    }

    public HoaDonOnline_DTO getHoaDonOnline(String maHD) {

        HoaDonBan_DTO hdBan = hoaDonDAO.getById(maHD);

        if (hdBan == null) return null;

        HoaDonOnline_DTO hd = new HoaDonOnline_DTO();

        hd.setMa(hdBan.getMa());
        hd.setNgayLap(hdBan.getNgayLap());
        hd.setMaNhanVien(hdBan.getMaNhanVien());
        hd.setMaKhachHang(hdBan.getMaKhachHang());
        hd.setTrangThai(hdBan.getTrangThai());
        hd.setTinhTrangThanhToan(hdBan.getTinhTrangThanhToan());
        hd.setThanhTien(hdBan.getThanhTien());
        hd.setTongTienGoc(hdBan.getTongTienGoc());
        hd.setThueVAT(hdBan.getThueVAT());

        return hd;
    }
}