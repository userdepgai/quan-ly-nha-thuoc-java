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

        String maNV = getMaNhanVienDangNhap();

        hd.setMaNhanVien(maNV);

        if (hd.getTrangThai() != HoaDonBan_DTO.TT_CHO_DUYET)
            throw new RuntimeException("Đơn không hợp lệ");

        ArrayList<ChiTietHoaDonBan_DTO> ds =
                ctDAO.getByMaHD(maHD);

        for (ChiTietHoaDonBan_DTO ct : ds) {

            boolean duHang =
                    loBus.kiemTraDuTon(
                            ct.getMaSP(),
                            ct.getSoLuong()
                    );

            if (!duHang)
                throw new RuntimeException(
                        "Không đủ hàng: " + ct.getMaSP());
        }

        ctDAO.deleteByMaHD(maHD);

        for (ChiTietHoaDonBan_DTO ct : ds) {

            Map<LoHang_DTO,Integer> dsLo =
                    loBus.phanBoLoDeBan(
                            ct.getMaSP(),
                            ct.getSoLuong()
                    );

            boolean ok =
                    loBus.banSanPham(
                            dsLo,
                            ct.getMaSP()
                    );

            if(!ok)
                throw new RuntimeException("Xuất kho thất bại");

            for(Map.Entry<LoHang_DTO,Integer> e : dsLo.entrySet()){

                ChiTietHoaDonBan_DTO newCT =
                        new ChiTietHoaDonBan_DTO();

                newCT.setMaHDB(ct.getMaHDB());
                newCT.setMaSP(ct.getMaSP());
                newCT.setMaLo(e.getKey().getMaLo());
                newCT.setSoLuong(e.getValue());
                newCT.setGiaBan(ct.getGiaBan());
                newCT.setGiaBanSauApKM(
                        ct.getGiaBanSauApKM()
                );
                newCT.setMaKhuyenMai(
                        ct.getMaKhuyenMai()==null ?
                                null :
                                ct.getMaKhuyenMai()
                );
                newCT.setThanhTien(
                        e.getValue()*ct.getGiaBanSauApKM()
                );
                ctDAO.insert(newCT);
            }
        }

        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_DA_DUYET,
                maNV
        );
        refreshData();
    }
    public void khongDuyetDon(String maHD){

        HoaDonBan_DTO hd = getById(maHD);

        if (hd == null)
            throw new RuntimeException("Không tìm thấy hóa đơn");

        String maNV = getMaNhanVienDangNhap();

        hd.setMaNhanVien(maNV);

        if (hd.getTrangThai() != HoaDonBan_DTO.TT_CHO_DUYET)
            throw new RuntimeException("Đơn không ở trạng thái chờ duyệt");

        if(hd.getTinhTrangThanhToan() == HoaDonBan_DTO.TT_DA_THANH_TOAN){

            hoaDonDAO.capNhatThanhToan(
                    maHD,
                    HoaDonBan_DTO.TT_DA_HOAN_TIEN
            );
        }

        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_DA_HUY,
                maNV
        );
        refreshData();
    }
    public void khachHuyDon(String maHD){

        HoaDonBan_DTO hd = getById(maHD);

        if(hd == null)
            throw new RuntimeException("Không tìm thấy hóa đơn");

        if(hd.getTrangThai() != HoaDonBan_DTO.TT_CHO_DUYET)
            throw new RuntimeException("Chỉ được hủy khi đang chờ duyệt");

        if(hd.getTinhTrangThanhToan()
                == HoaDonBan_DTO.TT_DA_THANH_TOAN){

            hoaDonDAO.capNhatThanhToan(
                    maHD,
                    HoaDonBan_DTO.TT_DA_HOAN_TIEN
            );
        }

        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_DA_HUY,
                null
        );
        refreshData();
    }
    public void giaoHang(String maHD ) {

        HoaDonBan_DTO hd = getById(maHD);

        if (hd == null)
            throw new RuntimeException("Không tìm thấy hóa đơn");
        String maNV = getMaNhanVienDangNhap();

        hd.setMaNhanVien(maNV);

        if (hd.getTrangThai() != HoaDonBan_DTO.TT_DA_DUYET)
            throw new RuntimeException("Đơn chưa được duyệt");

        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_DANG_GIAO,
                maNV
        );
        refreshData();
    }

    public void hoanThanh(String maHD ) {

        HoaDonBan_DTO hd = getById(maHD);

        if (hd == null)
            throw new RuntimeException("Không tìm thấy hóa đơn");
        String maNV = getMaNhanVienDangNhap();

        hd.setMaNhanVien(maNV);

        if (hd.getTrangThai() != HoaDonBan_DTO.TT_DANG_GIAO)
            throw new RuntimeException("Đơn chưa giao");

        if (hd.getTinhTrangThanhToan()
                == HoaDonBan_DTO.TT_CHUA_THANH_TOAN) {

            hoaDonDAO.capNhatThanhToan(
                    maHD,
                    HoaDonBan_DTO.TT_DA_THANH_TOAN
            );
        }

        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_HOAN_THANH,
                maNV
        );

        congDiem(hd);
        refreshData();
    }

    public void huyDonHang(String maHD ) {

        HoaDonBan_DTO hd = getById(maHD);

        if (hd == null)
            throw new RuntimeException("Không tìm thấy hóa đơn");

        String maNV = getMaNhanVienDangNhap();

        hd.setMaNhanVien(maNV);


        ArrayList<ChiTietHoaDonBan_DTO> ds =
                ctDAO.getByMaHD(maHD);

        for (ChiTietHoaDonBan_DTO ct : ds) {

            LoHang_DTO lo =
                    loBus.getById(ct.getMaLo());

            if(lo == null)
                continue;

            Map<LoHang_DTO,Integer> dsLo =
                    new HashMap<>();

            dsLo.put(lo, ct.getSoLuong());

            loBus.hoanTraSanPham(
                    dsLo,
                    ct.getMaSP()
            );
        }

        if(hd.getTinhTrangThanhToan() == HoaDonBan_DTO.TT_DA_THANH_TOAN){

            hoaDonDAO.capNhatThanhToan(
                    maHD,
                    HoaDonBan_DTO.TT_DA_HOAN_TIEN
            );
        }

        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_DA_HUY,
                maNV
        );
        refreshData();
    }
    public void hoanHang(String maHD) {

        HoaDonBan_DTO hd = getById(maHD);

        if(hd == null)
            throw new RuntimeException("Không tìm thấy hóa đơn");

        if (hd.getTrangThai() != HoaDonBan_DTO.TT_HOAN_THANH)
            throw new RuntimeException("Chưa hoàn thành");

        if (hd.getNgayHoanThanh()
                .plusDays(7)
                .isBefore(LocalDateTime.now()))
            throw new RuntimeException("Quá hạn hoàn");

        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_YEU_CAU_HOAN,
                null
        );
        refreshData();
    }

    public void duyetHoanHang(String maHD ){

        HoaDonBan_DTO hd = getById(maHD);

        if(hd == null)
            throw new RuntimeException("Không tìm thấy hóa đơn");
        String maNV = getMaNhanVienDangNhap();

        hd.setMaNhanVien(maNV);

        if(hd.getTrangThai() != HoaDonBan_DTO.TT_YEU_CAU_HOAN)
            throw new RuntimeException("Chưa yêu cầu hoàn");

        ArrayList<ChiTietHoaDonBan_DTO> ds =
                ctDAO.getByMaHD(maHD);

        for(ChiTietHoaDonBan_DTO ct : ds){

            LoHang_DTO lo =
                    loBus.getById(ct.getMaLo());

            if(lo == null)
                continue;

            Map<LoHang_DTO,Integer> dsLo =
                    new HashMap<>();

            dsLo.put(lo, ct.getSoLuong());

            loBus.hoanTraSanPham(
                    dsLo,
                    ct.getMaSP()
            );
        }
        if(hd.getTinhTrangThanhToan() == HoaDonBan_DTO.TT_DA_THANH_TOAN){

            hoaDonDAO.capNhatThanhToan(
                    maHD,
                    HoaDonBan_DTO.TT_DA_HOAN_TIEN
            );
        }

        int diem = (int)(hd.getThanhTien() / 10000);

        try{
            khBus.truDiemThuong(
                    hd.getMaKhachHang(),
                    diem
            );
        }catch(Exception e){

        }

        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_DA_HUY,
                maNV
        );
        refreshData();
    }
    public void khongDuyetHoanHang(String maHD ){

        HoaDonBan_DTO hd = getById(maHD);

        if(hd == null)
            throw new RuntimeException("Không tìm thấy hóa đơn");

        String maNV = getMaNhanVienDangNhap();

        hd.setMaNhanVien(maNV);

        if(hd.getTrangThai() != HoaDonBan_DTO.TT_YEU_CAU_HOAN)
            throw new RuntimeException("Đơn chưa yêu cầu hoàn");

        hoaDonDAO.capNhatTrangThai(
                maHD,
                HoaDonBan_DTO.TT_HOAN_THANH,
                maNV
        );
        refreshData();
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

        ArrayList<HoaDonOnline_DTO> ketQua = new ArrayList<>();

        if(keyword != null)
            keyword = keyword.toLowerCase();

        for(HoaDonOnline_DTO hd : getDanhSachDuyetOnline()){

            if(matchKeyword(hd,kieuTim,keyword) &&
                    matchTrangThai(hd,trangThai) &&
                    matchThanhToan(hd,thanhToan) &&
                    matchNgay(hd,tuNgay,denNgay) &&
                    matchGia(hd,mucGia))
            {
                ketQua.add(hd);
            }
        }

        return ketQua;
    }

}