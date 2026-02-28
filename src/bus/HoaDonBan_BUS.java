package bus;

import dao.*;
import dto.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class HoaDonBan_BUS {

    /* ================= SINGLETON ================= */
    private static HoaDonBan_BUS instance;

    public static HoaDonBan_BUS getInstance() {
        if (instance == null)
            instance = new HoaDonBan_BUS();
        return instance;
    }

    /* ================= DAO ================= */
    protected HoaDonBan_DAO hoaDonDAO = new HoaDonBan_DAO();
    protected ChiTietHoaDonBan_DAO ctDAO = new ChiTietHoaDonBan_DAO();

    /* ================= CACHE ================= */
    protected ArrayList<HoaDonBan_DTO> dsHoaDon = new ArrayList<>();
    protected ArrayList<ChiTietHoaDonBan_DTO> dsChiTietTam = new ArrayList<>();


    /* =========================================================
                        LOAD DATA
    ========================================================= */
    public void loadData() {
        dsHoaDon = hoaDonDAO.getAll();
    }

    public ArrayList<HoaDonBan_DTO> getDanhSach() {
        return dsHoaDon;
    }


    /* =========================================================
                    TẠO HÓA ĐƠN OFFLINE
    ========================================================= */
    public HoaDonBan_DTO taoHoaDon(String maNV, String maKH) {

        HoaDonBan_DTO hd = new HoaDonBan_DTO();

        hd.setMa(hoaDonDAO.getNextID());
        hd.setNgayLap(LocalDateTime.now());
        hd.setMaNhanVien(maNV);
        hd.setMaKhachHang(maKH);

        hd.setTrangThai(1);           // hoàn thành
        hd.setTinhTrangThanhToan(1);  // đã thanh toán
        hd.setLoaiHDB(0);             // OFFLINE

        dsChiTietTam.clear();

        return hd;
    }


    /* =========================================================
                        GIỎ HÀNG
    ========================================================= */
    public ArrayList<ChiTietHoaDonBan_DTO> getGioHang() {
        return dsChiTietTam;
    }

    public void resetHoaDon() {
        dsChiTietTam.clear();
    }


    /* =========================================================
                    THÊM CHI TIẾT
    ========================================================= */
    public boolean themChiTiet(ChiTietHoaDonBan_DTO ct) {

        if (ct == null || ct.getSoLuong() <= 0)
            return false;

        // không cho trùng mã lô
        for (ChiTietHoaDonBan_DTO item : dsChiTietTam) {
            if (item.getMaLo().equals(ct.getMaLo()))
                return false;
        }

        // TODO kiểm tra tồn kho
        // Lo_BUS.getInstance().kiemTraSoLuong()

        dsChiTietTam.add(ct);
        return true;
    }

    public void suaChiTiet(int index, ChiTietHoaDonBan_DTO newCT) {
        if (index >= 0 && index < dsChiTietTam.size())
            dsChiTietTam.set(index, newCT);
    }

    public void xoaChiTiet(int index) {
        if (index >= 0 && index < dsChiTietTam.size())
            dsChiTietTam.remove(index);
    }


    /* =========================================================
                        TÍNH TIỀN
    ========================================================= */
    public double tinhTongTienGoc() {

        double tong = 0;

        for (ChiTietHoaDonBan_DTO ct : dsChiTietTam)
            tong += ct.getGiaBan() * ct.getSoLuong();

        return tong;
    }

    public double tinhTongKhuyenMai() {

        double giam = 0;

        for (ChiTietHoaDonBan_DTO ct : dsChiTietTam) {
            // giam += KhuyenMai_BUS.getInstance().tinhKM(ct);
        }

        return giam;
    }

    public double tinhThanhTien(double voucher, double diemSuDung) {

        double tong = tinhTongTienGoc();
        double km = tinhTongKhuyenMai();

        return tong - km - voucher - diemSuDung;
    }


    /* =========================================================
                    LƯU HÓA ĐƠN OFFLINE
    ========================================================= */
    public boolean luuHoaDon(HoaDonBan_DTO hd,
                             double tienNhan,
                             boolean chuyenKhoan) {

        if (hd == null || dsChiTietTam.isEmpty())
            return false;

        // bắt buộc có khách hàng
        if (hd.getMaKhachHang() == null ||
                hd.getMaKhachHang().isEmpty())
            return false;

        /* ===== tính tiền ===== */
        hd.setTongTienGoc(tinhTongTienGoc());

        if (chuyenKhoan)
            tienNhan = hd.getThanhTien();

        if (tienNhan < hd.getThanhTien())
            return false;

        hd.setTienNhan(tienNhan);
        hd.setTienThoi(tienNhan - hd.getThanhTien());

        /* ===== INSERT HEADER ===== */
        boolean ok = hoaDonDAO.them(hd);
        if (!ok) return false;

        /* ===== INSERT DETAIL ===== */
        for (ChiTietHoaDonBan_DTO ct : dsChiTietTam) {

            ct.setMaHDB(hd.getMa());

            boolean insertOK = ctDAO.insert(ct);
            if (!insertOK)
                return false;

            // ⭐ trừ tồn kho theo lô
            // Lo_BUS.getInstance()
            //      .truSoLuong(ct.getMaLo(), ct.getSoLuong());
        }

        dsHoaDon.add(hd);
        dsChiTietTam.clear();

        return true;
    }
}