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
    private Voucher_BUS vchBUs = Voucher_BUS.getInstance();
    private NhanVien_BUS nvBus = NhanVien_BUS.getInstance();

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

        Voucher_DTO vch = vchBUs.getById(maVoucher);

        return vch != null ? vch.getTen() : maVoucher;
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

        hoaDon.setMa(getNextID());
        hoaDon.setMaNhanVien(maNV);
        hoaDon.setNgayLap(LocalDateTime.now());
        hoaDon.setTrangThai(HoaDonBan_DTO.TT_HOAN_THANH);
        hoaDon.setTongGiaTriKhuyenMai(0);

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





        ChiTietHoaDonBan_DTO ct = new ChiTietHoaDonBan_DTO();
        ct.setMaKhuyenMai(maKM);
        ct.setMaSP(maSP);
        ct.setSoLuong(soLuong);
        ct.setGiaBan(giaBan);
        double giaSauKM = tinhGiaSauKhuyenMai(giaBan, maKM);
        ct.setGiaBanSauApKM(giaSauKM);
        double thanhTien = giaSauKM * soLuong;

        ct.setThanhTien(thanhTien);

        dsChiTietHDB.add(ct);

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

    /*
    public ArrayList<Voucher_DTO> goiYVoucher(){

    if(hoaDon == null) return new ArrayList<>();

    double thanhTienTam =
            hoaDon.getTongTienGoc()
            - hoaDon.getTongGiaTriKhuyenMai();

    return vchBus.getDSVoucherSapXepTotNhat(thanhTienTam);
}

     */


    /*

    public void apDungVoucher(String tenVoucher){

        if(tenVoucher == null){
            hoaDon.setMaVoucher(null);
            return;
        }

        Voucher_DTO v = vchBUs.getByTen(tenVoucher);

        if(v != null){
            hoaDon.setMaVoucher(v.getMaVoucher());
            hoaDon.setGiaTriVoucher(v.getGiaTri());
        }

        tinhThanhTienSauCung();
    }
    */

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
                        (qc.getSlHopTrongThung() * qc.getSlTrongHop());

        return giaMotSP * (1 + sp.getLoiNhuan());
    }
    public double getGiaBanSP(String maSP){

        // lấy các lô còn tồn
        ArrayList<LoHang_DTO> dsLo =
                loBus.getLoConBanByMaSP(maSP);

        if(dsLo == null || dsLo.isEmpty())
            return 0;

        double giaNhapMax = 0;

        for(LoHang_DTO lo : dsLo){
            giaNhapMax = Math.max(
                    giaNhapMax,
                    lo.getGiaNhap()
            );
        }

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

        Voucher_DTO v = vchBUs.getById(maVoucher);
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

        double vat = truocVAT * 0.05;

        double phiShip = 0;

        if(hoaDon instanceof HoaDonOnline_DTO online){
            phiShip = online.getPhiVanChuyen();
        }

        double thanhTien = truocVAT + vat + phiShip;

        thanhTien = Math.max(0, thanhTien);

        hoaDon.setTongTienGoc(tongGoc);
        hoaDon.setTongGiaTriKhuyenMai(tongKM);
        hoaDon.setDiemThuongQuyDoi(diem);
        hoaDon.setThueVAT(vat);
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
    public boolean coTheDungDiemThuong(){

        if(hoaDon.getMaKhachHang() == null)
            return false;

        KhachHang_DTO kh =
                khBus.getById(hoaDon.getMaKhachHang());

        return kh != null;
    }


    // =====================================================
    // LƯU HÓA ĐƠN
    // =====================================================
    public boolean luuHoaDon(){

        Connection conn = null;

        try{
            conn = DBConnection.DBConnection.getConnection();
            conn.setAutoCommit(false);

            hoaDonDAO.insert(conn, hoaDon);

            for(ChiTietHoaDonBan_DTO ct : dsChiTietHDB){

                ct.setMaHDB(hoaDon.getMa());
                ctDAO.insert(conn, ct);

                truKhoFIFO(ct.getMaSP(), ct.getSoLuong());

                // ===== trừ lượt KM =====
                if(ct.getMaKhuyenMai() != null
                        && hoaDon.getMaKhachHang() != null){

                    KhachHang_KM_BUS.getInstance()
                            .truLuotSuDung(
                                    ct.getMaKhuyenMai(),
                                    hoaDon.getMaKhachHang()
                            );
                }
            }

            congDiemKhach();

            conn.commit();
            refreshData();
            return true;

        }catch(Exception e){
            try{
                if(conn!=null) conn.rollback();
            }catch(Exception ignored){}
            e.printStackTrace();
        }
        return false;
    }

    protected void truKhoFIFO(String maSP, int soLuong){

        Map<LoHang_DTO, Integer> dsTru =
                loBus.phanBoLoDeBan(maSP, soLuong);

        if(dsTru.isEmpty()){
            throw new RuntimeException("Không đủ tồn kho");
        }

        for(Map.Entry<LoHang_DTO,Integer> entry : dsTru.entrySet()){

            LoHang_DTO lo = entry.getKey();
            int soLuongTru = entry.getValue();

            lo.setSoLuongConLai(
                    lo.getSoLuongConLai() - soLuongTru
            );

            loBus.capNhat(lo);
        }
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

    public void refreshData(){
        listCache = hoaDonDAO.getAll();
    }
}

