package bus;

import dto.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;


// HOA DON ONLINE BUS
// kế thừa HoaDonBan_BUS
public class HoaDonOnline_BUS extends HoaDonBan_BUS {

    private static HoaDonOnline_BUS instance;

    private HoaDonOnline_BUS(){
        super();
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

        String maHD = getNextID();

        hd.setMa(maHD);
        hd.setMaKhachHang(maKH);
        hd.setLoaiHDB(1);
        hd.setTrangThai(HoaDonBan_DTO.TT_CHO_DUYET);
        hd.setTinhTrangThanhToan(HoaDonBan_DTO.TT_CHUA_THANH_TOAN);
        hd.setNgayLap(LocalDateTime.now());

        hd.setPhiVanChuyen(50000);
        hd.setMaDiaChiGiaoHang(diaChiGiao);

        // ⭐ dùng logic BUS cha
        this.hoaDon = hd;
        this.dsChiTietHDB = dsCT;

        tinhTongTien();

        hoaDonDAO.insert(hd);

        for(ChiTietHoaDonBan_DTO ct : dsCT){
            ct.setMaHDB(maHD);
            ctDAO.insert(ct);
        }

        return maHD;
    }

    // =====================================================
    // NHÂN VIÊN DUYỆT ĐƠN
    // =====================================================
    public void duyetDon(String maHD) {

        HoaDonBan_DTO hd = getById(maHD);

        if (hd == null)
            throw new RuntimeException("Không tìm thấy hóa đơn");

        if (hd.getTrangThai() != HoaDonBan_DTO.TT_CHO_DUYET)
            throw new RuntimeException("Đơn không hợp lệ");

        ArrayList<ChiTietHoaDonBan_DTO> ds =
                ctDAO.getByMaHD(maHD);

        for (ChiTietHoaDonBan_DTO ct : ds) {

            boolean duHang =
                    loBus.kiemTraDuTon(ct.getMaSP(), ct.getSoLuong());

            if (!duHang)
                throw new RuntimeException(
                        "Không đủ hàng: " + ct.getMaSP());
        }

        loBus.refreshData();

        hoaDonDAO.capNhatTrangThai(maHD,
                HoaDonBan_DTO.TT_DA_DUYET);
    }


    // =====================================================
    // TỪ CHỐI ĐƠN
    // =====================================================
    public void huyDonHang(String maHD) {

        HoaDonBan_DTO hd = getById(maHD);

        if(hd.getTrangThai() == HoaDonBan_DTO.TT_DANG_GIAO){

            ArrayList<ChiTietHoaDonBan_DTO> ds =
                    ctDAO.getByMaHD(maHD);

            for(ChiTietHoaDonBan_DTO ct : ds){

                var dsLo =
                        loBus.phanBoLoDeBan(ct.getMaSP(), ct.getSoLuong());

                loBus.congTonKhiHuy(dsLo);
            }
        }

        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_DA_HUY
        );
    }


    // =====================================================
    // GIAO HÀNG
    // =====================================================
    public void giaoHang(String maHD) {

        HoaDonBan_DTO hd = getById(maHD);

        if (hd.getTrangThai() != HoaDonBan_DTO.TT_DA_DUYET)
            throw new RuntimeException("Chưa duyệt");

        ArrayList<ChiTietHoaDonBan_DTO> ds =
                ctDAO.getByMaHD(maHD);

        for (ChiTietHoaDonBan_DTO ct : ds) {

            Map<LoHang_DTO, Integer> dsLo =
                    loBus.phanBoLoDeBan(ct.getMaSP(), ct.getSoLuong());

            if(dsLo.isEmpty())
                throw new RuntimeException("Không đủ hàng");

            for(Map.Entry<LoHang_DTO,Integer> entry : dsLo.entrySet()){

                LoHang_DTO lo = entry.getKey();
                int soLuongTru = entry.getValue();

                lo.truSoLuongConLai(soLuongTru);

                loBus.capNhat(lo);   // update DB
            }
        }

        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_DANG_GIAO
        );
    }


    // =====================================================
    // HOÀN THÀNH
    // =====================================================
    public void hoanThanh(String maHD) {

        HoaDonBan_DTO hd = getById(maHD);

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

        HoaDonBan_DTO hd = getById(maHD);

        if (hd.getTrangThai() != HoaDonBan_DTO.TT_HOAN_THANH)
            throw new RuntimeException("Chưa hoàn thành");

        if (hd.getNgayHoanThanh()
                .plusDays(7)
                .isBefore(LocalDateTime.now()))
            throw new RuntimeException("Quá hạn hoàn");

        ArrayList<ChiTietHoaDonBan_DTO> ds =
                ctDAO.getByMaHD(maHD);

        for (ChiTietHoaDonBan_DTO ct : ds) {

            var dsLo = loBus.phanBoLoDeBan(ct.getMaSP(), ct.getSoLuong());

            loBus.congTonKhiHuy(dsLo);
        }

        int diem = (int)(hd.getThanhTien() / 10000);
        khBus.truDiemThuong(hd.getMaKhachHang(), diem);

        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_YEU_CAU_HOAN
        );
    }

    public void duyetHoanHang(String maHD){

        HoaDonBan_DTO hd = getById(maHD);

        if(hd.getTrangThai() != HoaDonBan_DTO.TT_YEU_CAU_HOAN)
            throw new RuntimeException("Không phải yêu cầu hoàn");

        ArrayList<ChiTietHoaDonBan_DTO> ds =
                ctDAO.getByMaHD(maHD);

        for(ChiTietHoaDonBan_DTO ct : ds){

            var dsLo =
                    loBus.phanBoLoDeBan(ct.getMaSP(), ct.getSoLuong());

            loBus.congTonKhiHuy(dsLo);
        }

        int diem = (int)(hd.getThanhTien() / 10000);

        khBus.truDiemThuong(
                hd.getMaKhachHang(),
                diem
        );

        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_DA_HUY
        );
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

       khBus.congDiemMuaHang(hd.getMaKhachHang(), diem);
    }
    /*
    public HoaDonOnline_DTO getHoaDonOnline(String maHD) {

        HoaDonBan_DTO hdBan = getById(maHD);

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
    */

    public HoaDonOnline_DTO getHoaDonOnline(String maHD){

        HoaDonBan_DTO hd = getById(maHD);

        if(hd instanceof HoaDonOnline_DTO online){
            return online;
        }

        return null;
    }
}