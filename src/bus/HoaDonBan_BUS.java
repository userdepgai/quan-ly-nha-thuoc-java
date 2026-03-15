package bus;

import dao.*;
import dto.*;
import utils.Session;


import javax.swing.*;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.*;

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


    public ArrayList<HoaDonBan_DTO> getAllHoaDon(){
        return hoaDonDAO.getAll();
    }
    public ArrayList<ChiTietHoaDonBan_DTO> getDsTam(){

        if(hoaDon == null)
            return new ArrayList<>();

        return hoaDon.getDs_chiTietHDB();
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

        tinhTongTien();
    }

    public String getTenNV(String maNV){

        NhanVien_DTO nv = nvBus.getById(maNV);

        return nv != null ? nv.getTen() : "";
    }
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
    protected String getMaNhanVienDangNhap(){

        TaiKhoan_DTO tk = Session.getCurrentUser();

        if(tk == null)
            throw new RuntimeException("Chưa đăng nhập");

        String sdt = tk.getSdt();

        NhanVien_DTO nv = nvBus.getBysdt(sdt);

        if(nv == null)
            throw new RuntimeException("Không tìm thấy nhân viên");

        return nv.getMa();
    }
    public String getNextID(){
        return hoaDonDAO.getNextID();
    }

   public void taoHoaDonMoi(){

        hoaDon = new HoaDonBan_DTO();
       String maNV = getMaNhanVienDangNhap();
       hoaDon.setLoaiHDB(0);
       hoaDon.setMa(getNextID());
       hoaDon.setMaNhanVien(maNV);
       hoaDon.setNgayLap(LocalDateTime.now());
       hoaDon.setTongGiaTriKhuyenMai(0);
       hoaDon.setKeToa(false);

       hoaDon.getDs_chiTietHDB().clear();
    }

    public void themSanPham(String maSP, int soLuong,String tenKM,boolean coToa){


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

                if(hoaDon.getMaKhachHang() == null){
                    throw new RuntimeException(
                            "Vui lòng nhập số điện thoại khách hàng để kiểm tra lượt sử dụng"
                    );
                }
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

        if(!loBus.kiemTraDuTon(maSP, soLuong))
            throw new RuntimeException("Không đủ tồn");

        Map<LoHang_DTO,Integer> dsLoChon =
                loBus.phanBoLoDeBan(maSP, soLuong);
        double giaNhapMax =
                loBus.getGiaNhapCaoNhatTrongLoChon(dsLoChon);
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

            hoaDon.getDs_chiTietHDB().add(ct);
        }

        tinhTongTien();
    }

    public ArrayList<KhuyenMai_DTO> goiYKhuyenMai(String maSP, String maDanhMuc, double giaBan) {
        return kmBus.getDSKMSapXepTotNhat(maSP, maDanhMuc, giaBan
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

        for(ChiTietHoaDonBan_DTO ct : hoaDon.getDs_chiTietHDB()){

            if(ct.getMaSP().equals(maSP)){
                return true;
            }
        }

        return false;
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

        if(km.getLoaiKhuyenMai() == KhuyenMai_DTO.LOAI_PHAN_TRAM)
            return giaBan * (1 - km.getGiaTriKhuyenMai());

        if(km.getLoaiKhuyenMai() == KhuyenMai_DTO.LOAI_TIEN_MAT)
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

        double tongTien = 0;

        for(ChiTietHoaDonBan_DTO ct : hoaDon.getDs_chiTietHDB()){

            double gia = ct.getGiaBan();

            int soLuong = ct.getSoLuong();

            double thanhTien = gia * soLuong;

            tongTien += thanhTien;
        }

        return tongTien;
    }
    private double tinhTongGiaTriKhuyenMai(){

        double tongGoc = tinhTongTienGoc();

        double tongSauKM = 0;

        for(ChiTietHoaDonBan_DTO ct : hoaDon.getDs_chiTietHDB()){

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

        if(km.getLoaiKhuyenMai() == 0){
            return giaBan * km.getGiaTriKhuyenMai() ;
        }

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

        if(v.getLoaiVoucher() == Voucher_DTO.LOAI_PHAN_TRAM)
            return sauKM * v.getGiaTriVoucher();

        if(v.getLoaiVoucher() == Voucher_DTO.LOAI_TIEN_MAT)
            return Math.min(v.getGiaTriVoucher(), sauKM);

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

        int moc10k = (int)(thanhTien / 10000);

        int heSo = 1;

        switch (kh.getHang()){
            case "BAC": heSo = 2; break;
            case "VANG": heSo = 3; break;
            case "KIMCUONG": heSo = 5; break;
        }

        return moc10k * heSo;
    }

    protected void tinhTongTien(){

        tinhThanhTienSauCung();
    }

    public boolean luuHoaDon(){

        try{
            hoaDon.setNgayHoanThanh(LocalDateTime.now());
            hoaDon.setTrangThai(HoaDonBan_DTO.TT_HOAN_THANH);
            hoaDon.setTinhTrangThanhToan(HoaDonBan_DTO.TT_DA_THANH_TOAN);

            boolean ok = hoaDonDAO.insert(hoaDon);

            if(!ok)
                throw new RuntimeException("Insert hóa đơn thất bại");

            System.out.println("Insert hóa đơn: " + hoaDon.getMa());
            for(ChiTietHoaDonBan_DTO ct : hoaDon.getDs_chiTietHDB()){

                ct.setMaHDB(hoaDon.getMa());

                boolean okCT = ctDAO.insert(ct);

                if(!okCT)
                    throw new RuntimeException("Insert chi tiết hóa đơn thất bại");
                LoHang_DTO lo = loBus.getById(ct.getMaLo());

                if(lo == null)
                    throw new RuntimeException("Không tìm thấy lô hàng");

                Map<LoHang_DTO,Integer> map = new HashMap<>();

                map.put(lo, ct.getSoLuong());

                loBus.banSanPham(map, ct.getMaSP());
                if(ct.getMaKhuyenMai() != null
                        && hoaDon.getMaKhachHang() != null){

                    KhachHang_KM_BUS.getInstance()
                            .truLuotSuDung(
                                    ct.getMaKhuyenMai(),
                                    hoaDon.getMaKhachHang()
                            );
                }
            }
            if(hoaDon.getMaVoucher() != null
                    && hoaDon.getMaKhachHang() != null){

                KhachHang_Voucher_BUS.getInstance()
                        .truLuotSuDung(
                                hoaDon.getMaVoucher(),
                                hoaDon.getMaKhachHang()
                        );
            }

            if(dungDiemThuong && hoaDon.getMaKhachHang() != null){

                int diemSuDung =
                        (int)(hoaDon.getDiemThuongQuyDoi() / 10000) * 500;

                KhachHang_BUS.getInstance().truDiemThuong(
                        hoaDon.getMaKhachHang(),
                        diemSuDung
                );
            }
            if(hoaDon.getMaKhachHang() != null){
                congDiemKhach();
            }
            refreshData();

            return true;

        }catch(Exception e){

            System.out.println("=== LỖI LƯU HÓA ĐƠN ===");
            e.printStackTrace();
        }

        return false;
    }

    public void xoaSanPhamKeToa(){

        for(int i = hoaDon.getDs_chiTietHDB().size()-1; i >= 0; i--){

            ChiTietHoaDonBan_DTO ct = hoaDon.getDs_chiTietHDB().get(i);

            SanPham_DTO sp =
                    spBus.getById(ct.getMaSP());

            if(sp.getKeDon() == SanPham_DTO.KD_CO){
                hoaDon.getDs_chiTietHDB().remove(i);
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
    public void xoaSanPham(String maSP){
        hoaDon.getDs_chiTietHDB().removeIf(x -> x.getMaSP().equals(maSP));
        tinhTongTien();
    }

    protected boolean matchKeyword(
            HoaDonBan_DTO hd,
            String kieuTim,
            String keyword)
    {

        if(keyword == null || keyword.isBlank())
            return true;

        switch (kieuTim){

            case "Mã hóa đơn":
                return hd.getMa().toLowerCase().contains(keyword);

            case "SĐT":

                String sdt =
                        getSDT(hd.getMaKhachHang());

                return sdt != null &&
                        sdt.contains(keyword);

            case "Tên khách hàng":

                String ten =

                        getTenKH(hd.getMaKhachHang());

                return ten != null &&
                        ten.toLowerCase().contains(keyword);
        }

        return true;
    }

    protected boolean matchTrangThai(
            HoaDonBan_DTO hd,
            Integer trangThai)
    {

        if(trangThai == null)
            return true;

        return hd.getTrangThai() == trangThai;
    }

    protected boolean matchThanhToan(
            HoaDonBan_DTO hd,
            Integer tinhTrangThanhToan)
    {

        if(tinhTrangThanhToan == null)
            return true;

        return hd.getTinhTrangThanhToan()
                == tinhTrangThanhToan;
    }

    protected boolean matchLoai(
            HoaDonBan_DTO hd,
            Integer loaiHD)
    {

        if(loaiHD == null)
            return true;

        return hd.getLoaiHDB() == loaiHD;
    }

    protected boolean matchNgay(
            HoaDonBan_DTO hd,
            LocalDateTime tuNgay,
            LocalDateTime denNgay)
    {

        if(tuNgay == null || denNgay == null)
            return true;

        LocalDateTime ngayLap =
                hd.getNgayLap();

        return !ngayLap.isBefore(tuNgay) &&
                !ngayLap.isAfter(denNgay);
    }

    protected boolean matchGia(
            HoaDonBan_DTO hd,
            Integer mucGia)
    {

        if(mucGia == null)
            return true;

        double gia = hd.getThanhTien();

        switch (mucGia){

            case 0:
                return gia < 500000;

            case 1:
                return gia >= 500000 &&
                        gia <= 1000000;

            case 2:
                return gia > 1000000 &&
                        gia <= 3000000;

            case 3:
                return gia > 3000000;
        }

        return true;
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

            if(matchKeyword(hd,kieuTim,keyword) &&
                    matchTrangThai(hd,trangThai) &&
                    matchThanhToan(hd,tinhTrangThanhToan) &&
                    matchLoai(hd,loaiHD) &&
                    matchNgay(hd,tuNgay,denNgay) &&
                    matchGia(hd,mucGia))
            {
                result.add(hd);
            }
        }

        return result;
    }
    public void capNhatSoLuong(String maSP, int soLuong, String tenKM, boolean coToa){

        hoaDon.getDs_chiTietHDB().removeIf(ct -> ct.getMaSP().equals(maSP));

        themSanPham(maSP, soLuong, tenKM, coToa);

        tinhTongTien();
    }

    public void refreshData(){
        listCache = hoaDonDAO.getAll();
    }

}

