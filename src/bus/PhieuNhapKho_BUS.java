package bus;

import dao.*;
import dto.*;
import utils.Session;

import java.time.LocalDateTime;
import java.util.*;

public class PhieuNhapKho_BUS {

    private static PhieuNhapKho_BUS instance;
    private PhieuNhapKho_DAO pnkDao = new PhieuNhapKho_DAO();
    private ArrayList<PhieuNhapKho_DTO> listCache;

    private NhanVien_BUS nhanVienBus;
    private NhaCungCap_BUS nhaCungCapBus;
    private KhuVucLuuTru_BUS khuVucLuuTruBus;
    private SanPhamNCC_BUS sanPhamNCCBus;
    private SanPham_BUS sanPhamBus ;
    private LoHang_BUS loHangBus ;

    private ChiTietPhieuNhapKho_DAO chiTietDao = new ChiTietPhieuNhapKho_DAO();
    private PhieuNhapKho_BUS() {
        sanPhamBus = SanPham_BUS.getInstance();
        sanPhamNCCBus = SanPhamNCC_BUS.getInstance();
        khuVucLuuTruBus = KhuVucLuuTru_BUS.getInstance();
        nhaCungCapBus = NhaCungCap_BUS.getInstance();
        nhanVienBus = NhanVien_BUS.getInstance();
        loHangBus = LoHang_BUS.getInstance();
        listCache = pnkDao.getAll();
    }

    public static PhieuNhapKho_BUS getInstance() {
        if (instance == null) {
            instance = new PhieuNhapKho_BUS();
        }
        return instance;
    }

    public ArrayList<PhieuNhapKho_DTO> getAll() {
        listCache = pnkDao.getAll();
        for (PhieuNhapKho_DTO pnk : listCache) {
            ArrayList<ChiTietPhieuNhapKho_DTO> dsChiTiet =
                    chiTietDao.getByMaPNK(pnk.getMa());
            pnk.setDs_chiTietPNK(dsChiTiet);
        }

        return listCache;
    }
    public ArrayList<ChiTietPhieuNhapKho_DTO> getChiTiet(String maPNK) {
        return chiTietDao.getByMaPNK(maPNK);
    }

    public boolean capNhatTrangThaiLoHang(String maLo, int trangThai){
        return loHangBus.capNhatTrangThai(maLo, trangThai);
    }
    public void congHienCoKho(String maKVLT, int soLuong) {
        KhuVucLuuTru_DTO kvlt = khuVucLuuTruBus.getById(maKVLT);
        if (kvlt == null) return;
        int hienCoMoi = kvlt.getHienCo() + soLuong;
        kvlt.setHienCo(hienCoMoi);

        khuVucLuuTruBus.update(kvlt);
        khuVucLuuTruBus.refreshData();
    }
    public String getNextId() {
        return pnkDao.getNextID();
    }

    public boolean them(PhieuNhapKho_DTO pnk) {
        boolean result = pnkDao.them(pnk);
        if (result) refreshData();
        return result;
    }

    public boolean capNhat(PhieuNhapKho_DTO pnk) {
        boolean result = pnkDao.capNhat(pnk);
        if (result) refreshData();
        return result;
    }
    public boolean themChiTiet(ChiTietPhieuNhapKho_DTO ct) {
        boolean result = chiTietDao.them(ct);
        if (result) {
            PhieuNhapKho_DTO pnk = getById(ct.getMaPNK());
            if (pnk != null) {
                pnk.getDs_chiTietPNK().add(ct);
            }
        }
        return result;
    }
//
    public boolean capNhatChiTiet(ChiTietPhieuNhapKho_DTO ct) {
        boolean result = chiTietDao.capNhat(ct);
        if (result) {
            PhieuNhapKho_DTO pnk = getById(ct.getMaPNK());
            if (pnk != null) {
                for (ChiTietPhieuNhapKho_DTO item : pnk.getDs_chiTietPNK()) {
                    if (item.getMaSP().equals(ct.getMaSP())) {
                        item.setSoLuong(ct.getSoLuong());
                        item.setMaLo(ct.getMaLo());
                        break;
                    }
                }
            }
        }
        return result;
    }
    public boolean xoaChiTiet(String maPNK, String maSP) {
        boolean result = chiTietDao.xoa(maPNK, maSP);
        if (result) {
            PhieuNhapKho_DTO pnk = getById(maPNK);
            if (pnk != null) {
                pnk.getDs_chiTietPNK()
                        .removeIf(ct -> ct.getMaSP().equals(maSP));
            }
        }
        return result;
    }
    public PhieuNhapKho_DTO getById(String maPNK) {
        for (PhieuNhapKho_DTO pnk : listCache) {
            if (pnk.getMa().equals(maPNK)) {
                return pnk;
            }
        }
        return null;
    }
    public LoHang_DTO getLoById(String maLo) {
        return loHangBus.getById(maLo);
    }

    public String getNameNV(String maNV) {
        return nhanVienBus.getById(maNV).getTen();
    }
    public String getNameNCC(String maNCC) {
        return nhaCungCapBus.getById(maNCC).getTenNCC();
    }
    public String getNameKVLT(String maKVLT) {
        return khuVucLuuTruBus.getById(maKVLT).getTenKVLT();
    }
    public String getNameSP(String maSP) {
        return sanPhamBus.getById(maSP).getTenSP();
    }

    public String getMaNhanVienByTen(String tenNV) {
        for (NhanVien_DTO nv : nhanVienBus.getAll()) {
            if (nv.getTen().equals(tenNV)) {
                return nv.getMa();
            }
        }
        return null;
    }
    public String getMaNCCByTen(String tenNCC) {
        return nhaCungCapBus.getByName(tenNCC).getMaNCC();
    }
    public String getMaSpByTen(String tenSP) {
        for(SanPham_DTO sp : sanPhamBus.getAll())
            if(sp.getTenSP().equals(tenSP))
                return sp.getMaSP();
        return null;
    }
    public String getMaKVLTByTen(String tenKVLT) {
        return khuVucLuuTruBus.getByName(tenKVLT).getMaKVLT();
    }
    public String getTenNVDangDangNhap(){
        return nhanVienBus.getBysdt(Session.getCurrentUser().getSdt()).getTen();
    }
    public String getMaNVDangDangNhap() {
        return nhanVienBus.getBysdt(Session.getCurrentUser().getSdt()).getMa();
    }
    public double getGiaNhapTrongNCCSP(String maNCC,String maSP) {
        ArrayList<SanPhamNCC_DTO> dssp = sanPhamNCCBus.getByMaNCC(maNCC);
        for(SanPhamNCC_DTO sp : dssp)
            if(sp.getMaSanPham().equals(maSP))
                return sp.getGiaNhap();
        return -1;
    }
    public ArrayList<String> getDsTenNhanVien() {
        ArrayList<String> list = new ArrayList<>();
        for (NhanVien_DTO nv : nhanVienBus.getAll()) {
            list.add(nv.getTen());
        }
        return list;
    }
    public ArrayList<String> getDsTenNCC() {
        ArrayList<String> list = new ArrayList<>();
        for (NhaCungCap_DTO ncc : nhaCungCapBus.getAll()) {
            list.add(ncc.getTenNCC());
        }
        return list;
    }
    public ArrayList<String> getDsTenKVLT() {
        ArrayList<String> list = new ArrayList<>();
        for (KhuVucLuuTru_DTO kv : khuVucLuuTruBus.getAll()) {
            list.add(kv.getTenKVLT());
        }
        return list;
    }
    public ArrayList<SanPham_DTO> getSPByMaNCC(String maNCC) {
        ArrayList<SanPhamNCC_DTO> spNCC = sanPhamNCCBus.getByMaNCC(maNCC);
        ArrayList<SanPham_DTO> listSP = new ArrayList<>();
        for (SanPhamNCC_DTO spNcc : spNCC) {
            SanPham_DTO sp = sanPhamBus.getById(spNcc.getMaSanPham());
            listSP.add(sp);
        }
        return listSP;
    }
    public ArrayList<String> getDSTenSPCuaNCC(String maNCC) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<SanPham_DTO> dssp = getSPByMaNCC(maNCC);
        for(SanPham_DTO sp : dssp)
            list.add(sp.getTenSP());
        return list;
    }
    public ArrayList<NhaCungCap_DTO> getNCCConHopTac() {
        ArrayList<NhaCungCap_DTO> list = new ArrayList<>();
        for(NhaCungCap_DTO ncc : nhaCungCapBus.getAll()){
            if(ncc.getTrangThai() == 1)
                list.add(ncc);
        }
        return list;
    }
    public ArrayList<String> getTenNCCConHopTac(){
        ArrayList<String> list = new ArrayList<>();
        for (NhaCungCap_DTO ncc : getNCCConHopTac())
            list.add(ncc.getTenNCC());
        return list;
    }
    public ArrayList<KhuVucLuuTru_DTO> getKVLTConSuDung() {
        ArrayList<KhuVucLuuTru_DTO> list = new ArrayList<>();
        for(KhuVucLuuTru_DTO kvlt : khuVucLuuTruBus.getAll())
            if(kvlt.getTrangThai() == 1)
                list.add(kvlt);
        return list;
    }
    public ArrayList<String> getTenKVLTConSuDung(){
        ArrayList<String> list = new ArrayList<>();
        for (KhuVucLuuTru_DTO kvlt : getKVLTConSuDung())
            list.add(kvlt.getTenKVLT());
        return list;
    }
    public boolean kiemTraDuSucChua(PhieuNhapKho_DTO pnk) {
        KhuVucLuuTru_DTO kvlt = KhuVucLuuTru_BUS.getInstance().getById(pnk.getMaKVLT());
        int hienCo= kvlt.getHienCo();
        for(ChiTietPhieuNhapKho_DTO ct : pnk.getDs_chiTietPNK()) {
            hienCo += ct.getSoLuong();
            if(hienCo > kvlt.getSucChua())
                return false;
        }
        return true;
    }
    public boolean kiemTraDuSucChua(PhieuNhapKho_DTO pnk, int soLuong) {
        KhuVucLuuTru_DTO kvlt = KhuVucLuuTru_BUS.getInstance().getById(pnk.getMaKVLT());
        int hienCo= kvlt.getHienCo();
        for(ChiTietPhieuNhapKho_DTO ct : pnk.getDs_chiTietPNK()) {
            hienCo += ct.getSoLuong();
            if(hienCo > kvlt.getSucChua())
                return false;
        }
        hienCo += soLuong;
        if(hienCo > kvlt.getSucChua())
            return false;
        return true;
    }
    public ArrayList<PhieuNhapKho_DTO> timKiem(
            String maPNK,
            String maSP,
            String maNCC,
            String maNV,
            String maKVLT,
            Integer trangThai,
            LocalDateTime ngayLapFrom,
            LocalDateTime ngayLapTo,
            LocalDateTime ngayHTFrom,
            LocalDateTime ngayHTTo
    ) {
        ArrayList<PhieuNhapKho_DTO> result = new ArrayList<>(listCache);

        result = filterByMaPNK(result, maPNK);
        result = filterByMaSP(result, maSP);
        result = filterByNCC(result, maNCC);
        result = filterByNhanVien(result, maNV);
        result = filterByKVLT(result, maKVLT);
        result = filterByTrangThai(result, trangThai);
        result = filterByNgayLap(result, ngayLapFrom, ngayLapTo);
        result = filterByNgayHoanThanh(result, ngayHTFrom, ngayHTTo);
        return result;
    }
    private ArrayList<PhieuNhapKho_DTO> filterByMaPNK(ArrayList<PhieuNhapKho_DTO> list, String maPNK) {
        if (maPNK == null || maPNK.trim().isEmpty()) return list;
        ArrayList<PhieuNhapKho_DTO> result = new ArrayList<>();
        for (PhieuNhapKho_DTO pnk : list) {
            if (pnk.getMa().toLowerCase().contains(maPNK.toLowerCase())) {
                result.add(pnk);
            }
        }
        return result;
    }
    private ArrayList<PhieuNhapKho_DTO> filterByMaSP(ArrayList<PhieuNhapKho_DTO> list, String maSP) {
        if (maSP == null || maSP.trim().isEmpty()) return list;
        ArrayList<PhieuNhapKho_DTO> result = new ArrayList<>();
        for (PhieuNhapKho_DTO pnk : list) {
            for (ChiTietPhieuNhapKho_DTO ct : pnk.getDs_chiTietPNK()) {
                if (ct.getMaSP().toLowerCase().contains(maSP.toLowerCase())) {
                    result.add(pnk);
                    break;
                }
            }
        }
        return result;
    }
    private ArrayList<PhieuNhapKho_DTO> filterByNCC(ArrayList<PhieuNhapKho_DTO> list, String maNCC) {
        if (maNCC == null || maNCC.equals("Tất cả")) return list;
        ArrayList<PhieuNhapKho_DTO> result = new ArrayList<>();
        for (PhieuNhapKho_DTO pnk : list) {
            if (pnk.getMaNCC().equals(maNCC)) {
                result.add(pnk);
            }
        }
        return result;
    }
    private ArrayList<PhieuNhapKho_DTO> filterByNhanVien(ArrayList<PhieuNhapKho_DTO> list, String maNV) {
        if (maNV == null || maNV.equals("Tất cả")) return list;
        ArrayList<PhieuNhapKho_DTO> result = new ArrayList<>();
        for (PhieuNhapKho_DTO pnk : list) {
            if (pnk.getMaNhanVien().equals(maNV)) {
                result.add(pnk);
            }
        }
        return result;
    }
    private ArrayList<PhieuNhapKho_DTO> filterByKVLT(ArrayList<PhieuNhapKho_DTO> list, String maKVLT) {
        if (maKVLT == null || maKVLT.equals("Tất cả")) return list;
        ArrayList<PhieuNhapKho_DTO> result = new ArrayList<>();
        for (PhieuNhapKho_DTO pnk : list) {
            if (pnk.getMaKVLT().equals(maKVLT)) {
                result.add(pnk);
            }
        }
        return result;
    }
    private ArrayList<PhieuNhapKho_DTO> filterByTrangThai(ArrayList<PhieuNhapKho_DTO> list, Integer trangThai) {
        if (trangThai == null || trangThai == -1) return list;
        ArrayList<PhieuNhapKho_DTO> result = new ArrayList<>();
        for (PhieuNhapKho_DTO pnk : list) {
            if (pnk.getTrangThai() == trangThai) {
                result.add(pnk);
            }
        }
        return result;
    }
    private ArrayList<PhieuNhapKho_DTO> filterByNgayLap(
            ArrayList<PhieuNhapKho_DTO> list,
            LocalDateTime from,
            LocalDateTime to) {
        if (from == null && to == null) return list;
        ArrayList<PhieuNhapKho_DTO> result = new ArrayList<>();
        for (PhieuNhapKho_DTO pnk : list) {
            LocalDateTime ngay = pnk.getNgayLap();
            if ((from == null || !ngay.isBefore(from)) &&
                    (to == null || !ngay.isAfter(to))) {
                result.add(pnk);
            }
        }
        return result;
    }
    private ArrayList<PhieuNhapKho_DTO> filterByNgayHoanThanh(
            ArrayList<PhieuNhapKho_DTO> list,
            LocalDateTime from,
            LocalDateTime to) {
        if (from == null && to == null) return list;
        ArrayList<PhieuNhapKho_DTO> result = new ArrayList<>();
        for (PhieuNhapKho_DTO pnk : list) {
            if (pnk.getNgayHoanThanh() == null) continue;
            LocalDateTime ngay = pnk.getNgayHoanThanh();
            if ((from == null || !ngay.isBefore(from)) &&
                    (to == null || !ngay.isAfter(to))) {
                result.add(pnk);
            }
        }
        return result;
    }


    public ArrayList<PhieuNhapKho_DTO> locTheoKhoangNgay(LocalDateTime from, LocalDateTime to) {
        ArrayList<PhieuNhapKho_DTO> result = new ArrayList<>();

        for (PhieuNhapKho_DTO pnk : listCache) {
            LocalDateTime ngayLap = pnk.getNgayLap();

            if ((from == null || !ngayLap.isBefore(from)) &&
                    (to == null || !ngayLap.isAfter(to))) {
                result.add(pnk);
            }
        }

        return result;
    }
    public void refreshData() {
        listCache = pnkDao.getAll();
    }
}