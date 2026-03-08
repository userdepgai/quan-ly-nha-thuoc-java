package bus;

import dao.*;
import dto.*;
import utils.Session;


import javax.swing.*;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.*;

/**
 * BUS Hóa Đơn Bán
 */
public class HoaDonBan_BUS {
    private boolean dungDiemThuong = false;
    private static HoaDonBan_BUS instance;

    protected HoaDonBan_DAO hoaDonDAO = new HoaDonBan_DAO();
    protected ChiTietHoaDonBan_DAO ctDAO = new ChiTietHoaDonBan_DAO();
    protected KhachHang_BUS khBus = KhachHang_BUS.getInstance();
    private SanPham_BUS spBus = SanPham_BUS.getInstance();
    protected LoHang_BUS loBus = LoHang_BUS.getInstance();
    private QuyCach_BUS qcBus = QuyCach_BUS.getInstance();
    private KhuyenMai_BUS kmBus = KhuyenMai_BUS.getInstance();
    private Voucher_BUS vchBus = Voucher_BUS.getInstance();
    private NhanVien_BUS nvBus = NhanVien_BUS.getInstance();
    private DiaChi_BUS diaChiBUS = DiaChi_BUS.getInstance();

    protected HoaDonBan_DTO hoaDon;
    private ArrayList<HoaDonBan_DTO> listCache;

    protected ArrayList<ChiTietHoaDonBan_DTO> dsChiTietHDB = new ArrayList<>();

    public ArrayList<HoaDonBan_DTO> getAllHoaDon(){
        return hoaDonDAO.getAll();
    }


    public ArrayList<ChiTietHoaDonBan_DTO> getDsTam(){
        return dsChiTietHDB;
    }
    public HoaDonBan_DTO getHoaDon(){
        return hoaDon;
    }
    public ArrayList<ChiTietHoaDonBan_DTO> getChiTietHoaDon(String maHD){

        if(maHD == null || maHD.isEmpty())
            return new ArrayList<>();

        return ctDAO.getByMaHD(maHD);
    }

    protected HoaDonBan_BUS(){listCache = hoaDonDAO.getAll();}

    public static HoaDonBan_BUS getInstance(){
        if(instance == null)
            instance = new HoaDonBan_BUS();
        return instance;
    }
    public void setDungDiemThuong(boolean dung){

        this.dungDiemThuong = dung;

        tinhTongTien(); // tính lại tiền ngay
    }
    // =====================================================
    // LOAD KHÁCH HÀNG
    // =====================================================

    public String getTenKH(String maKH){

        KhachHang_DTO kh = khBus.getById(maKH);

        return kh != null ? kh.getTen() : "";
    }

    public String getSDT(String maKH){

        KhachHang_DTO kh = khBus.getById(maKH);

        return kh != null ? kh.getSdt() : "";
    }
    public String getTenSP(String maSP){

        SanPham_DTO sp = spBus.getById(maSP);

        return sp != null ? sp.getTenSP() : maSP;
    }
    public String getDonViTinh(String maSP){

        SanPham_DTO sp = spBus.getById(maSP);

        return sp != null ? sp.getDonViTinh() : maSP;
    }
    public String getTenKhuyenMai(String maKM){

        KhuyenMai_DTO km = kmBus.getById(maKM);

        return km != null ? km.getTenKM() : maKM;
    }
    public String getTenVoucher(String maVoucher){

        Voucher_DTO vch = vchBus.getById(maVoucher);

        return vch != null ? vch.getTen() : maVoucher;
    }
    public String getDiaChiDayDu(String maDiaChi){

        DIACHI_DTO dc = diaChiBUS.getById(maDiaChi);

        if(dc == null)
            return "";

        return dc.toString();
    }



    // =====================================================
    // SINH MÃ
    // =====================================================
    public String getNextID(){
        return hoaDonDAO.getNextID();
    }

    // =====================================================
    // TẠO HÓA ĐƠN
    // =====================================================
   public void taoHoaDonMoi(){

        hoaDon = new HoaDonBan_DTO();

        // ===== lấy user đang login =====
        TaiKhoan_DTO tk = Session.getCurrentUser();

        if(tk == null)
            throw new RuntimeException("Chưa đăng nhập");

       String sdt = tk.getSdt();

       NhanVien_DTO nv = nvBus.getBysdt(sdt);

       if(nv == null)
           throw new RuntimeException("Không tìm thấy nhân viên");

       String maNV = nv.getMa();
       hoaDon.setLoaiHDB(0);
       hoaDon.setMa(getNextID());
       hoaDon.setMaNhanVien(maNV);
       hoaDon.setNgayLap(LocalDateTime.now());
       hoaDon.setTongGiaTriKhuyenMai(0);
       hoaDon.setKeToa(false);

        dsChiTietHDB.clear();
    }


    // =====================================================
    // THÊM SẢN PHẨM FIFO
    // =====================================================
    public void themSanPham(String maSP, int soLuong,String tenKM,boolean coToa){
        // ===== BUS tự tìm mã KM =====
        SanPham_DTO sp = spBus.getById(maSP);

        if(sp == null)
            throw new RuntimeException("Không tìm thấy sản phẩm");

        if(sp.getKeDon() == SanPham_DTO.KD_CO && !coToa){
            throw new RuntimeException("Sản phẩm này cần toa bác sĩ");
        }
        String maKM = null;

        if(tenKM != null){

            KhuyenMai_DTO km = kmBus.getByTen(tenKM);

            if(km != null){

                maKM = km.getMaKM();

                // ===== bắt buộc phải có khách hàng =====
                if(hoaDon.getMaKhachHang() == null){
                    throw new RuntimeException(
                            "Vui lòng nhập số điện thoại khách hàng để kiểm tra lượt sử dụng"
                    );
                }

                // ===== kiểm tra lượt KM =====
                boolean conLuot =
                        KhachHang_KM_BUS.getInstance()
                                .conLuotSuDung(maKM, hoaDon.getMaKhachHang());

                if(!conLuot){
                    throw new RuntimeException(
                            "Số lượt sử dụng khuyến mãi này đã hết"
                    );
                }
            }
        }

        if(soLuong <= 0)
            throw new RuntimeException("Số lượng phải > 0");

        if(daTonTai(maSP))
            throw new RuntimeException("Mỗi sản phẩm chỉ 1 dòng");
        // ===== CHỌN LÔ FIFO =====
        if(!loBus.kiemTraDuTon(maSP, soLuong))
            throw new RuntimeException("Không đủ tồn");

        Map<LoHang_DTO,Integer> dsLoChon =
                loBus.phanBoLoDeBan(maSP, soLuong);

// ===== LẤY GIÁ NHẬP MAX =====
        double giaNhapMax =
                loBus.getGiaNhapCaoNhatTrongLoChon(dsLoChon);

// ===== TÍNH GIÁ BÁN =====
        double giaBan = tinhGiaBan(maSP, giaNhapMax);

        for(Map.Entry<LoHang_DTO,Integer> entry : dsLoChon.entrySet()){

            LoHang_DTO lo = entry.getKey();
            int sl = entry.getValue();

            ChiTietHoaDonBan_DTO ct = new ChiTietHoaDonBan_DTO();

            ct.setMaSP(maSP);
            ct.setMaLo(lo.getMaLo());
            ct.setMaKhuyenMai(maKM);

            ct.setSoLuong(sl);
            ct.setGiaBan(giaBan);

            double giaSauKM = tinhGiaSauKhuyenMai(giaBan, maKM);

            ct.setGiaBanSauApKM(giaSauKM);

            double thanhTien = giaSauKM * sl;

            ct.setThanhTien(thanhTien);

            dsChiTietHDB.add(ct);
        }

        tinhTongTien();
    }

    public ArrayList<KhuyenMai_DTO> goiYKhuyenMai(
        String maSP,
        String maDanhMuc,
        double giaBan)
{
    return kmBus.getDSKMSapXepTotNhat(
            maSP,
            maDanhMuc,
            giaBan
    );
}


    public ArrayList<Voucher_DTO> goiYVoucher(){

    if(hoaDon == null) return new ArrayList<>();
        if(hoaDon.getMaKhachHang() == null)
            return new ArrayList<>();
    double thanhTienTam =
            hoaDon.getTongTienGoc()
            - hoaDon.getTongGiaTriKhuyenMai();

    return vchBus.getDSVoucherSapXepTotNhat(thanhTienTam);
}


    public void apDungVoucher(String tenVoucher){

        if(tenVoucher == null){
            hoaDon.setMaVoucher(null);
            tinhThanhTienSauCung();
            return;
        }

        Voucher_DTO v = vchBus.getByTenVoucher(tenVoucher);

        if(v != null){

            boolean conLuot =
                    KhachHang_Voucher_BUS.getInstance()
                            .conLuotSuDung(
                                    v.getMa(),
                                    hoaDon.getMaKhachHang()
                            );

            if(!conLuot)
                throw new RuntimeException("Voucher đã hết lượt sử dụng");

            hoaDon.setMaVoucher(v.getMa());
        }

        tinhThanhTienSauCung();
    }


    public HoaDonBan_DTO getById(String maHD){

        for(HoaDonBan_DTO hd : listCache){
            if(hd.getMa().equals(maHD))
                return hd;
        }
        return null;
    }

     private boolean daTonTai(String maSP){
            return dsChiTietHDB.stream()
                    .anyMatch(x -> x.getMaSP().equals(maSP));
        }

    public double tinhGiaBan(String maSP, double giaNhapMax){

        SanPham_DTO sp = spBus.getById(maSP);
        QuyCach_DTO qc = qcBus.getById(sp.getMaQC());

        double giaMotSP =
                giaNhapMax /
                        qc.getSlspThung();

        return giaMotSP *  sp.getLoiNhuan();
    }
    public double getGiaBanSP(String maSP, int soLuong){

        Map<LoHang_DTO,Integer> dsLo =
                loBus.phanBoLoDeBan(maSP, soLuong);

        if(dsLo.isEmpty())
            return 0;

        double giaNhapMax =
                loBus.getGiaNhapCaoNhatTrongLoChon(dsLo);

        return tinhGiaBan(maSP, giaNhapMax);
    }
    public double tinhGiaSauKhuyenMai(double giaBan, String maKM){

        if(maKM == null) return giaBan;

        KhuyenMai_DTO km = kmBus.getById(maKM);
        if(km == null) return giaBan;

        if(km.getLoaiKhuyenMai() == 0) // %
            return giaBan * (1 - km.getGiaTriKhuyenMai());

        if(km.getLoaiKhuyenMai() == 1) // tiền
            return Math.max(0,
                    giaBan - km.getGiaTriKhuyenMai());

        return giaBan;
    }
    public double tinhThanhTienMotSP(
            double giaBan,
            int soLuong,
            String maKM)
    {
        double giaTriKM = tinhGiaTriKhuyenMai(giaBan, maKM);

        return (giaBan - giaTriKM) * soLuong;
    }
    private double tinhTongTienGoc(){

        return dsChiTietHDB.stream()
                .mapToDouble(ct ->
                        ct.getGiaBan() * ct.getSoLuong())
                .sum();
    }
    private double tinhTongGiaTriKhuyenMai(){

        double tongGoc = 0;
        double tongSauKM = 0;

        for(ChiTietHoaDonBan_DTO ct : dsChiTietHDB){

            tongGoc += ct.getGiaBan() * ct.getSoLuong();

            tongSauKM += tinhThanhTienMotSP(
                    ct.getGiaBan(),
                    ct.getSoLuong(),
                    ct.getMaKhuyenMai());
        }

        return tongGoc - tongSauKM;
    }
    private double tinhGiaTriKhuyenMai(
            double giaBan,
            String maKM)
    {
        if(maKM == null || maKM.isBlank())
            return 0;

        KhuyenMai_DTO km = kmBus.getById(maKM);
        if(km == null) return 0;

        // ===== KM %
        if(km.getLoaiKhuyenMai() == 0){
            return giaBan * km.getGiaTriKhuyenMai() ;
        }

        // ===== KM tiền
        if(km.getLoaiKhuyenMai() == 1){
            return Math.min(km.getGiaTriKhuyenMai(), giaBan);
        }

        return 0;
    }
    private double tinhGiaTriVoucher(double sauKM){

        String maVoucher = hoaDon.getMaVoucher();
        if(maVoucher == null) return 0;

        Voucher_DTO v = vchBus.getById(maVoucher);
        if(v == null) return 0;

        if(v.getLoaiVoucher() == 0) // %
            return sauKM * v.getGiaTriVoucher();

        if(v.getLoaiVoucher() == 1) // tiền
            return v.getGiaTriVoucher();

        return 0;
    }

    private int tinhGiaTriDiemThuong(){

        if(hoaDon.getMaKhachHang() == null)
            return 0;

        KhachHang_DTO kh =
                khBus.getById(hoaDon.getMaKhachHang());

        if(kh == null)
            return 0;

        int diem = kh.getDiemThuong();

        int soLanDoi = diem / 500;

        return soLanDoi * 10000;
    }
    public void tinhThanhTienSauCung(){

        double tongGoc = tinhTongTienGoc();

        double tongKM = tinhTongGiaTriKhuyenMai();

        double sauKM = tongGoc - tongKM;

        double voucher = tinhGiaTriVoucher(sauKM);

        int diem = 0;

        if(dungDiemThuong){
            diem = tinhGiaTriDiemThuong();
        }

        double truocVAT =
                sauKM - voucher - diem;

        truocVAT = Math.max(0, truocVAT);


        final double VAT_RATE = 0.05;

        double vat = truocVAT * VAT_RATE;
        vat = Math.round(vat * 100.0) / 100.0;

        double phiShip = 0;

        if(hoaDon instanceof HoaDonOnline_DTO online){
            phiShip = online.getPhiVanChuyen();
        }

        double thanhTien = truocVAT + vat + phiShip;

        thanhTien = Math.max(0, thanhTien);

        hoaDon.setTongTienGoc(tongGoc);
        hoaDon.setTongGiaTriKhuyenMai(tongKM);
        hoaDon.setDiemThuongQuyDoi(diem);
        hoaDon.setThueVAT(VAT_RATE);
        hoaDon.setThanhTien(thanhTien);
    }
    private int tinhDiemTichLuy(){

        if(hoaDon.getMaKhachHang() == null)
            return 0;

        KhachHang_DTO kh =
                khBus.getById(hoaDon.getMaKhachHang());

        if(kh == null) return 0;

        double thanhTien = hoaDon.getThanhTien();

        // số mốc 10k
        int moc10k = (int)(thanhTien / 10000);

        int heSo = 1; // Đồng mặc định

        switch (kh.getHang()){
            case "BAC": heSo = 2; break;
            case "VANG": heSo = 3; break;
            case "KIMCUONG": heSo = 5; break;
        }

        return moc10k * heSo;
    }
    // =====================================================
    // TÍNH TIỀN
    // =====================================================
    protected void tinhTongTien(){

        tinhThanhTienSauCung();
    }
    // =====================================================
    // LƯU HÓA ĐƠN
    // =====================================================
    public boolean luuHoaDon(){

        try{
            hoaDon.setNgayHoanThanh(LocalDateTime.now());
            hoaDon.setTrangThai(HoaDonBan_DTO.TT_HOAN_THANH);
            hoaDon.setTinhTrangThanhToan(1);

            // ===== insert hóa đơn =====
            boolean ok = hoaDonDAO.insert(hoaDon);

            if(!ok)
                throw new RuntimeException("Insert hóa đơn thất bại");

            System.out.println("Insert hóa đơn: " + hoaDon.getMa());

            // ===== insert chi tiết =====
            for(ChiTietHoaDonBan_DTO ct : dsChiTietHDB){

                ct.setMaHDB(hoaDon.getMa());

                ctDAO.insert(ct);

                // ===== trừ tồn lô =====
                LoHang_DTO lo = loBus.getById(ct.getMaLo());

                lo.setSoLuongConLai(
                        lo.getSoLuongConLai() - ct.getSoLuong()
                );

                loBus.capNhat(lo);

                // ===== trừ lượt khuyến mãi =====
                if(ct.getMaKhuyenMai() != null
                        && hoaDon.getMaKhachHang() != null){

                    KhachHang_KM_BUS.getInstance()
                            .truLuotSuDung(
                                    ct.getMaKhuyenMai(),
                                    hoaDon.getMaKhachHang()
                            );
                }
            }

            // ===== trừ lượt voucher =====
            if(hoaDon.getMaVoucher() != null
                    && hoaDon.getMaKhachHang() != null){

                KhachHang_Voucher_BUS.getInstance()
                        .truLuotSuDung(
                                hoaDon.getMaVoucher(),
                                hoaDon.getMaKhachHang()
                        );
            }

            // ===== trừ điểm nếu dùng =====
            if(dungDiemThuong && hoaDon.getMaKhachHang() != null){

                int diemSuDung =
                        (int)(hoaDon.getDiemThuongQuyDoi() / 10000) * 500;

                KhachHang_BUS.getInstance().truDiemThuong(
                        hoaDon.getMaKhachHang(),
                        diemSuDung
                );
            }

            // ===== cộng điểm sau mua =====
            congDiemKhach();

            refreshData();

            return true;

        }catch(Exception e){

            System.out.println("=== LỖI LƯU HÓA ĐƠN ===");
            e.printStackTrace();
        }

        return false;
    }

    public void xoaSanPhamKeToa(){

        for(int i = dsChiTietHDB.size()-1; i >= 0; i--){

            ChiTietHoaDonBan_DTO ct = dsChiTietHDB.get(i);

            SanPham_DTO sp =
                    spBus.getById(ct.getMaSP());

            if(sp.getKeDon() == SanPham_DTO.KD_CO){
                dsChiTietHDB.remove(i);
            }

        }

        tinhTongTien();
    }
    private void congDiemKhach(){

        if(hoaDon.getMaKhachHang()==null) return;

        int diem = tinhDiemTichLuy();

        khBus.congDiemMuaHang(
               hoaDon.getMaKhachHang(),
                diem
        );
    }


    // =====================================================

    public void xoaSanPham(String maSP){
        dsChiTietHDB.removeIf(x -> x.getMaSP().equals(maSP));
        tinhTongTien();
    }

    public ArrayList<HoaDonBan_DTO> timKiem(
            String kieuTim,
            String keyword,
            Integer trangThai,
            Integer tinhTrangThanhToan,
            Integer loaiHD,
            Integer mucGia,
            LocalDateTime tuNgay,
            LocalDateTime denNgay
    ){

        ArrayList<HoaDonBan_DTO> result = new ArrayList<>();

        if(keyword != null)
            keyword = keyword.toLowerCase();

        for(HoaDonBan_DTO hd : listCache){

            // =====================
            // keyword
            // =====================
            boolean matchKeyword = true;

            if(keyword != null && !keyword.isBlank()){

                switch (kieuTim){

                    case "Mã hóa đơn":

                        matchKeyword =
                                hd.getMa().toLowerCase().contains(keyword);
                        break;

                    case "SĐT":

                        String sdt = getSDT(hd.getMaKhachHang());

                        matchKeyword =
                                sdt != null && sdt.contains(keyword);
                        break;

                    case "Tên khách hàng":

                        String ten = getTenKH(hd.getMaKhachHang());

                        matchKeyword =
                                ten != null &&
                                        ten.toLowerCase().contains(keyword);
                        break;
                }
            }

            // =====================
            // trạng thái
            // =====================
            boolean matchTrangThai =
                    (trangThai == null ||
                            hd.getTrangThai() == trangThai);

            // =====================
            // thanh toán
            // =====================
            boolean matchThanhToan =
                    (tinhTrangThanhToan == null ||
                            hd.getTinhTrangThanhToan() == tinhTrangThanhToan);

            // =====================
            // loại hóa đơn
            // =====================
            boolean matchLoai =
                    (loaiHD == null ||
                            hd.getLoaiHDB() == loaiHD);

            // =====================
            // ngày
            // =====================
            boolean matchNgay = true;

            if(tuNgay != null && denNgay != null){

                LocalDateTime ngayLap = hd.getNgayLap();

                matchNgay =
                        !ngayLap.isBefore(tuNgay) &&
                                !ngayLap.isAfter(denNgay);
            }

            // =====================
            // giá
            // =====================
            boolean matchGia = true;

            if(mucGia != null){

                double gia = hd.getThanhTien();

                switch (mucGia){

                    case 0: // <500k
                        matchGia = gia < 500000;
                        break;

                    case 1: // 500k - 1tr
                        matchGia = gia >= 500000 && gia <= 1000000;
                        break;

                    case 2: // 1tr - 3tr
                        matchGia = gia > 1000000 && gia <= 3000000;
                        break;

                    case 3: // >3tr
                        matchGia = gia > 3000000;
                        break;
                }
            }

            if(matchKeyword &&
                    matchTrangThai &&
                    matchThanhToan &&
                    matchLoai &&
                    matchNgay &&
                    matchGia)
            {
                result.add(hd);
            }
        }

        return result;
    }
    public void capNhatSoLuong(String maSP, int soLuong, String tenKM, boolean coToa){

        dsChiTietHDB.removeIf(ct -> ct.getMaSP().equals(maSP));

        themSanPham(maSP, soLuong, tenKM, coToa);

        tinhTongTien();
    }

    public void refreshData(){
        listCache = hoaDonDAO.getAll();
    }
}

