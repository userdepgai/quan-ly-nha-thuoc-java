package bus;

import dto.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public String taoDonOnline(
            String maKH,
            String diaChiKH,
            String maDiaChi,
            ArrayList<ChiTietHoaDonBan_DTO> dsCT,
            int tinhTrangThanhToan
    ) {

        if(dsCT == null || dsCT.isEmpty())
            throw new RuntimeException("Giỏ hàng trống");

        if(diaChiKH == null || diaChiKH.isBlank())
            throw new RuntimeException("Thiếu địa chỉ giao");

        String maHD = getNextID();

        HoaDonOnline_DTO hd = new HoaDonOnline_DTO();

        hd.setMa(maHD);
        hd.setMaKhachHang(maKH);

        hd.setLoaiHDB(1);
        hd.setTrangThai(HoaDonBan_DTO.TT_CHO_DUYET);

        hd.setTinhTrangThanhToan(tinhTrangThanhToan);

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

        hd.setMaDiaChiGiaoHang(maDiaChi);

        this.hoaDon = hd;
        this.hoaDon.setDs_chiTietHDB(dsCT);

        tinhTongTien();

        hd.setThanhTien(
                hd.getTongTienGoc()
                        - hd.getTongGiaTriKhuyenMai()
                        + hd.getPhiVanChuyen()
        );

        boolean ok = hoaDonDAO.insert(hd);

        if(!ok)
            throw new RuntimeException("Insert hóa đơn thất bại");

        for(ChiTietHoaDonBan_DTO ct : dsCT){

            ct.setMaHDB(maHD);

            boolean kq = ctDAO.insert(ct);

            if(!kq)
                throw new RuntimeException("Insert chi tiết thất bại");
        }
        refreshData();
        return maHD;
    }
    public void duyetDon(String maHD) {

        HoaDonBan_DTO hd = getById(maHD);

        if (hd == null)
            throw new RuntimeException("Không tìm thấy hóa đơn");

        if (hd.getTrangThai() != HoaDonBan_DTO.TT_CHO_DUYET)
            throw new RuntimeException("Đơn không hợp lệ");

        ArrayList<ChiTietHoaDonBan_DTO> ds = ctDAO.getByMaHD(maHD);

        for (ChiTietHoaDonBan_DTO ct : ds) {


            Map<LoHang_DTO, Integer> dsLo = loBus.phanBoLoDeBan(ct.getMaSP(), ct.getSoLuong());

            if (dsLo.isEmpty()) {
                throw new RuntimeException("Không đủ hàng: " + ct.getMaSP());
            }
            String maLoDuocChon = dsLo.keySet().iterator().next().getMaLo();
            ctDAO.capNhatMaLo(maHD, ct.getMaSP(), maLoDuocChon);
        }
        hoaDonDAO.capNhatTrangThai(maHD, HoaDonBan_DTO.TT_DA_DUYET);
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