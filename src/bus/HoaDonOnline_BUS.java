package bus;

import dto.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
    public ArrayList<HoaDonOnline_DTO> getDanhSachOnlineTheoKhachHang(String maKH){
        return hoaDonDAO.getDanhSachOnlineTheoKhachHang(maKH);
    }

    // =====================================================
    // KHÁCH ĐẶT ONLINE → AUTO TẠO HÓA ĐƠN
    // =====================================================
    public String taoDonOnline(
            String maKH,
            String diaChiGiao,
            ArrayList<ChiTietHoaDonBan_DTO> dsCT
    ){

        if(dsCT == null || dsCT.isEmpty())
            throw new RuntimeException("Giỏ hàng trống");

        String maHD = getNextID();

        HoaDonOnline_DTO hd = new HoaDonOnline_DTO();

        hd.setMa(maHD);
        hd.setMaKhachHang(maKH);

        hd.setLoaiHDB(1);
        hd.setTrangThai(HoaDonBan_DTO.TT_CHO_DUYET);
        hd.setTinhTrangThanhToan(HoaDonBan_DTO.TT_CHUA_THANH_TOAN);

        hd.setNgayLap(LocalDateTime.now());

        hd.setKeToa(false);
        hd.setTongTienGoc(0);
        hd.setTongGiaTriKhuyenMai(0);
        hd.setDiemThuongQuyDoi(0);
        hd.setTienNhan(0);
        hd.setTienThoi(0);
        hd.setThueVAT(0);
        hd.setMaNhanVien(null);
        hd.setMaVoucher(null);

        hd.setPhiVanChuyen(15000);
        hd.setMaDiaChiGiaoHang(diaChiGiao);

        this.hoaDon = hd;
        this.hoaDon.setDs_chiTietHDB(dsCT);

        tinhTongTien();


        boolean ok = hoaDonDAO.insert(hd);

        if(!ok)
            throw new RuntimeException("Insert hóa đơn thất bại");

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

        hoaDonDAO.capNhatTrangThai(maHD,
                HoaDonBan_DTO.TT_DA_DUYET);
    }
    public void khongDuyetDon(String maHD){

        HoaDonBan_DTO hd = getById(maHD);

        if (hd == null)
            throw new RuntimeException("Không tìm thấy hóa đơn");

        if (hd.getTrangThai() != HoaDonBan_DTO.TT_CHO_DUYET)
            throw new RuntimeException("Đơn không ở trạng thái chờ duyệt");

        hoaDonDAO.capNhatTrangThai(maHD,
                HoaDonBan_DTO.TT_DA_HUY);
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
    // TỪ CHỐI ĐƠN
    // =====================================================
    public void huyDonHang(String maHD) {

        HoaDonBan_DTO hd = getById(maHD);

        if (hd.getTrangThai() == HoaDonBan_DTO.TT_DANG_GIAO) {

            ArrayList<ChiTietHoaDonBan_DTO> ds =
                    ctDAO.getByMaHD(maHD);

            for (ChiTietHoaDonBan_DTO ct : ds) {

                Map<LoHang_DTO,Integer> dsLo = new HashMap<>();

                ArrayList<LoHang_DTO> loList =
                        loBus.getByMaSP(ct.getMaSP());

                int soLuongCanCong = ct.getSoLuong();

                for (LoHang_DTO lo : loList) {

                    if (soLuongCanCong <= 0)
                        break;

                    int slCong = soLuongCanCong;

                    dsLo.put(lo, slCong);

                    soLuongCanCong -= slCong;
                }

                loBus.congTonKhiHuy(dsLo);
            }
        }

        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_DA_HUY
        );
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

        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_YEU_CAU_HOAN
        );
    }

    public void duyetHoanHang(String maHD){

        HoaDonBan_DTO hd = getById(maHD);

        if(hd.getTrangThai() != HoaDonBan_DTO.TT_YEU_CAU_HOAN)
            throw new RuntimeException("Chưa yêu cầu hoàn");

        ArrayList<ChiTietHoaDonBan_DTO> ds =
                ctDAO.getByMaHD(maHD);

        for(ChiTietHoaDonBan_DTO ct : ds){

            Map<LoHang_DTO,Integer> dsLo =
                    loBus.phanBoLoDeBan(
                            ct.getMaSP(),
                            ct.getSoLuong()
                    );

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
    public void khongDuyetHoanHang(String maHD){

        HoaDonBan_DTO hd = getById(maHD);

        if(hd.getTrangThai() != HoaDonBan_DTO.TT_YEU_CAU_HOAN)
            throw new RuntimeException("Đơn chưa yêu cầu hoàn");

        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_HOAN_THANH
        );
    }
    // =====================================================
    // CỘNG ĐIỂM
    // =====================================================
    private void congDiem(HoaDonBan_DTO hd) {

        int diem = (int)(hd.getThanhTien() / 10000);

       khBus.congDiemMuaHang(hd.getMaKhachHang(), diem);
    }

    public HoaDonOnline_DTO getHoaDonOnline(String maHD){

        HoaDonBan_DTO hd = getById(maHD);

        if(hd instanceof HoaDonOnline_DTO online){
            return online;
        }

        return null;
    }
    public ArrayList<HoaDonOnline_DTO> timKiemHoaDonOnline(
            String kieuTim,
            String keyword,
            Integer trangThai,
            Integer thanhToan,
            Integer mucGia,
            LocalDateTime tuNgay,
            LocalDateTime denNgay
    ){

        ArrayList<HoaDonBan_DTO> ds =
                super.timKiem(
                        kieuTim,
                        keyword,
                        trangThai,
                        thanhToan,
                        1,
                        mucGia,
                        tuNgay,
                        denNgay
                );

        ArrayList<HoaDonOnline_DTO> ketQua = new ArrayList<>();

        for(HoaDonBan_DTO hd : ds){
            if(hd instanceof HoaDonOnline_DTO online){
                ketQua.add(online);
            }
        }

        return ketQua;
    }
}