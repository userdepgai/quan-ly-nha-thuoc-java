package bus;

import dao.*;
import dto.*;


import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.*;

/**
 * BUS Hóa Đơn Bán
 */
public class HoaDonBan_BUS {

    private static HoaDonBan_BUS instance;

    protected HoaDonBan_DAO hoaDonDAO = new HoaDonBan_DAO();
    protected ChiTietHoaDonBan_DAO ctDAO = new ChiTietHoaDonBan_DAO();
    protected LoHang_DAO loDAO = new LoHang_DAO();
    protected SanPham_DAO spDAO = new SanPham_DAO();
    protected QuyCach_DAO qcDAO = new QuyCach_DAO();
    protected KhachHang_DAO khDAO = new KhachHang_DAO();
    // cache khách hàng
    private HashMap<String, KhachHang_DTO> mapKH = new HashMap<>();

    private HoaDonBan_DTO hoaDon;
    private ArrayList<ChiTietHoaDonBan_DTO> dsCT = new ArrayList<>();

    protected HoaDonBan_BUS(){}

    public static HoaDonBan_BUS getInstance(){
        if(instance == null)
            instance = new HoaDonBan_BUS();
        return instance;
    }

    // =====================================================
    // LOAD KHÁCH HÀNG
    // =====================================================
    public void loadKhachHang(){
        mapKH.clear();
        for(KhachHang_DTO kh : khDAO.getAll()){
            mapKH.put(kh.getMa(), kh);
        }
    }

    public String getTenKH(String maKH){
        if(maKH == null) return "";
        KhachHang_DTO kh = mapKH.get(maKH);
        return kh != null ? kh.getTen() : maKH;
    }

    public String getSDT(String maKH){
        if(maKH == null) return "";
        KhachHang_DTO kh = mapKH.get(maKH);
        return kh != null ? kh.getSdt() : "";
    }
    public String getTenSP(String maSP){

        return spDAO.getAll()
                .stream()
                .filter(x -> x.getMaSP().equals(maSP))
                .map(SanPham_DTO::getTenSP)
                .findFirst()
                .orElse(maSP);
    }

    public ArrayList<HoaDonBan_DTO> getAllHoaDon(){
        return hoaDonDAO.getAll();
    }


    public ArrayList<ChiTietHoaDonBan_DTO> getChiTietHoaDon(String maHD){

        if(maHD == null || maHD.isEmpty())
            return new ArrayList<>();

        return ctDAO.getByMaHD(maHD);
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
    public void taoHoaDonMoi(String maNV){

        hoaDon = new HoaDonBan_DTO();

        hoaDon.setMa(getNextID());
        hoaDon.setMaNhanVien(maNV);
        hoaDon.setNgayLap(LocalDateTime.now());
        hoaDon.setTrangThai(
                HoaDonBan_DTO.TT_HOAN_THANH
        );
        hoaDon.setTongGiaTriKhuyenMai(0);


        dsCT.clear();
    }

    // =====================================================
    // THÊM SẢN PHẨM FIFO
    // =====================================================
    public void themSanPham(String maSP, int soLuong){

        if(soLuong <= 0)
            throw new RuntimeException("Số lượng phải > 0");

        if(daTonTai(maSP))
            throw new RuntimeException("Sản phẩm đã tồn tại");

        List<LoHang_DTO> dsLo = layLoConHang(maSP);

        if(dsLo.isEmpty())
            throw new RuntimeException("Sản phẩm hết hàng");

        dsLo.sort(Comparator.comparing(LoHang_DTO::getHsd));

        int tongTon = dsLo.stream()
                .mapToInt(LoHang_DTO::getSoLuongConLai)
                .sum();

        if(soLuong > tongTon)
            throw new RuntimeException("Không đủ tồn");

        double giaBan = tinhGiaBanTheoLo(dsLo, maSP);

        ChiTietHoaDonBan_DTO ct = new ChiTietHoaDonBan_DTO();
        ct.setMaSP(maSP);
        ct.setSoLuong(soLuong);
        ct.setGiaBan(giaBan);
        ct.setThanhTien(giaBan * soLuong);

        dsCT.add(ct);

        tinhTongTien();
    }

    private boolean daTonTai(String maSP){
        return dsCT.stream().anyMatch(x -> x.getMaSP().equals(maSP));
    }

    private List<LoHang_DTO> layLoConHang(String maSP){

        ArrayList<LoHang_DTO> all = loDAO.getAll();
        List<LoHang_DTO> result = new ArrayList<>();

        for(LoHang_DTO lo : all){
            if(lo.getMaSp().equals(maSP)
                    && lo.getSoLuongConLai() > 0
                    && lo.getTrangThai() == 1){
                result.add(lo);
            }
        }
        return result;
    }

    private double tinhGiaBanTheoLo(List<LoHang_DTO> dsLo,String maSP){

        SanPham_DTO sp = spDAO.getAll()
                .stream()
                .filter(x->x.getMaSP().equals(maSP))
                .findFirst().orElseThrow();

        QuyCach_DTO qc = qcDAO.getAll()
                .stream()
                .filter(x->x.getMaQC().equals(sp.getMaQC()))
                .findFirst().orElseThrow();

        double giaMax = 0;

        for(LoHang_DTO lo : dsLo){

            double giaNhapDonVi =
                    lo.getGiaNhap() /
                            (qc.getSlHopTrongThung()*qc.getSlTrongHop());

            double giaBan =
                    giaNhapDonVi * (1 + sp.getLoiNhuan());

            giaMax = Math.max(giaMax, giaBan);
        }

        return giaMax;
    }

    // =====================================================
    // TÍNH TIỀN
    // =====================================================
    private void tinhTongTien(){

        double tong = dsCT.stream()
                .mapToDouble(ChiTietHoaDonBan_DTO::getThanhTien)
                .sum();

        hoaDon.setTongTienGoc(tong);

        tinhThanhTienSauCung();
    }

    private void tinhThanhTienSauCung(){

        double tienSauGiam =
                hoaDon.getTongTienGoc()
                        - hoaDon.getTongGiaTriKhuyenMai();

        double vat = tienSauGiam * 0.05;

        hoaDon.setThueVAT(vat);
        hoaDon.setThanhTien(tienSauGiam + vat);
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

            for(ChiTietHoaDonBan_DTO ct : dsCT){

                ct.setMaHDB(hoaDon.getMa());
                ctDAO.insert(conn, ct);

                truKhoFIFO(ct.getMaSP(), ct.getSoLuong());
            }

            congDiemKhach();

            conn.commit();
            return true;

        }catch(Exception e){
            try{
                if(conn!=null) conn.rollback();
            }catch(Exception ignored){}
            e.printStackTrace();
        }
        return false;
    }

    private void truKhoFIFO(String maSP,int soLuong){

        List<LoHang_DTO> dsLo = layLoConHang(maSP);
        dsLo.sort(Comparator.comparing(LoHang_DTO::getHsd));

        int canTru = soLuong;

        for(LoHang_DTO lo : dsLo){

            int ton = lo.getSoLuongConLai();

            if(ton >= canTru){
                lo.setSoLuongConLai(ton-canTru);
                loDAO.capNhat(lo);
                break;
            }else{
                lo.setSoLuongConLai(0);
                loDAO.capNhat(lo);
                canTru -= ton;
            }
        }
    }

    private void congDiemKhach(){

        if(hoaDon.getMaKhachHang()==null) return;

        int diem = (int)(hoaDon.getThanhTien()/10000);

        khDAO.congDiem(hoaDon.getMaKhachHang(),diem);
    }


    // =====================================================
    public HoaDonBan_DTO getHoaDon(){
        return hoaDon;
    }

    public ArrayList<ChiTietHoaDonBan_DTO> getDsCT(){
        return dsCT;
    }
}