/*package bus;

import dao.*;
import dto.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


 * =====================================================
 * HOA DON BAN BUS (OFFLINE)
 * CORE BUSINESS LOGIC
 * =====================================================

public class HoaDonBan_BUS {

    private HoaDonBan_DAO hoaDonDAO = new HoaDonBan_DAO();
    private ChiTietHoaDonBan_DAO ctDAO = new ChiTietHoaDonBan_DAO();
    private LoHang_DAO loDAO = new LoHang_DAO();
    private KhachHang_DAO khDAO = new KhachHang_DAO();

    private HoaDonBan_DTO hoaDon;
    private ArrayList<ChiTietHoaDonBan_DTO> dsCT = new ArrayList<>();

     =====================================================
       KHỞI TẠO HÓA ĐƠN
     =====================================================
    public void taoHoaDonMoi(String maNV) {

        hoaDon = new HoaDonBan_DTO();
        hoaDon.setMa(sinhMaHD());
        hoaDon.setMaNhanVien(maNV);
        hoaDon.setNgayLap(LocalDateTime.now());
        hoaDon.setTrangThai("HOAN_THANH");

        dsCT.clear();
    }

     =====================================================
       SINH MÃ HD
     =====================================================
    private String sinhMaHD() {
        return "HDB" + System.currentTimeMillis();
    }

     =====================================================
       LOAD KHÁCH HÀNG THEO SĐT
     =====================================================
    public KhachHang_DTO timKhachTheoSDT(String sdt) {
        return khDAO.findByPhone(sdt);
    }

     =====================================================
       THÊM SẢN PHẨM (FIFO LÔ)
     =====================================================
    public boolean themSanPham(String maSP, int soLuong) {

        if (soLuong <= 0)
            throw new RuntimeException("Số lượng phải > 0");

        if (daTonTai(maSP))
            throw new RuntimeException("Sản phẩm đã tồn tại");

        List<LoHang_DTO> dsLo = loDAO.getLoConHang(maSP);

        if (dsLo.isEmpty())
            throw new RuntimeException("Sản phẩm đã hết hàng");

        dsLo.sort(Comparator.comparing(LoHang_DTO::getNgayNhap));

        int tongTon = dsLo.stream().mapToInt(LoHang_DTO::getSoLuongTon).sum();

        if (soLuong > tongTon)
            throw new RuntimeException("Không đủ tồn kho");

        double giaMax = timGiaLonNhat(dsLo, soLuong);

        ChiTietHoaDonBan_DTO ct = new ChiTietHoaDonBan_DTO();
        ct.setMaSP(maSP);
        ct.setSoLuong(soLuong);
        ct.setGiaBan(giaMax);
        ct.setThanhTien(giaMax * soLuong);

        dsCT.add(ct);

        tinhTongTien();

        return true;
    }

    private boolean daTonTai(String maSP) {
        return dsCT.stream().anyMatch(x -> x.getMaSP().equals(maSP));
    }

    =====================================================
       TÍNH GIÁ THEO LÔ (RULE QUAN TRỌNG)
     =====================================================
    private double timGiaLonNhat(List<LoHang_DTO> dsLo, int soLuong) {

        int canLay = soLuong;
        double giaMax = 0;

        for (LoHang_DTO lo : dsLo) {

            double gia = tinhGiaBan(lo);
            giaMax = Math.max(giaMax, gia);

            canLay -= lo.getSoLuongTon();

            if (canLay <= 0)
                break;
        }

        return giaMax;
    }

     =====================================================
       CÔNG THỨC GIÁ
     =====================================================
    private double tinhGiaBan(LoHang_DTO lo) {

        double giaNhapDonVi =
                lo.getThanhTienLo()
                        / lo.getSoLuongNhap()
                        / (lo.getSlHopTrongThung() * lo.getSlSpTrongHop());

        return giaNhapDonVi * (1 + lo.getLoiNhuan());
    }

     =====================================================
       TÍNH TỔNG TIỀN
     =====================================================
    private void tinhTongTien() {

        double tong = dsCT.stream()
                .mapToDouble(ChiTietHoaDonBan_DTO::getThanhTien)
                .sum();

        hoaDon.setTong(tong);
    }

    =====================================================
       ÁP VOUCHER
     =====================================================
    public void apVoucher(double giaTriVoucher) {

        double tong = hoaDon.getTongTien();

        if (tong - giaTriVoucher <= tong * 0.7)
            throw new RuntimeException("Không đủ điều kiện dùng voucher");

        hoaDon.setGiamVoucher(giaTriVoucher);
        tinhThanhTienSauCung();
    }

     =====================================================
       ÁP ĐIỂM
     =====================================================
    public void suDungDiem(int diem) {

        double tienGiam = diem * 10000;

        hoaDon.setGiamDiem(tienGiam);
        tinhThanhTienSauCung();
    }

     =====================================================
       VAT + FINAL
     =====================================================
    private void tinhThanhTienSauCung() {

        double tien =
                hoaDon.getTongTien()
                        - hoaDon.getGiamVoucher()
                        - hoaDon.getGiamDiem();

        double vat = tien * 0.05;

        hoaDon.setVat(vat);
        hoaDon.setThanhTien(tien + vat);
    }

     =====================================================
       LƯU HÓA ĐƠN
     =====================================================
    public void luuHoaDon() {

        hoaDonDAO.insert(hoaDon);

        for (ChiTietHoaDon_DTO ct : dsCT) {
            ct.setMaHD(hoaDon.getMaHD());
            ctDAO.insert(ct);

            loDAO.truKhoFIFO(ct.getMaSP(), ct.getSoLuong());
        }

        congDiemKhachHang();
    }

     =====================================================
       CỘNG ĐIỂM
     =====================================================
    private void congDiemKhachHang() {

        int diem = (int) (hoaDon.getThanhTien() / 10000);

        khDAO.congDiem(hoaDon.getMaKH(), diem);
    }

    public HoaDon_DTO getHoaDon() {
        return hoaDon;
    }

    public ArrayList<ChiTietHoaDon_DTO> getDsCT() {
        return dsCT;
    }
}

*/