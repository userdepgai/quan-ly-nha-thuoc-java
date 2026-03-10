package bus;

import dao.LoHang_DAO;
import dto.*;

import javax.swing.*;
import java.time.LocalDate;
import java.util.*;

public class LoHang_BUS {
    private static LoHang_BUS instance;
    private LoHang_DAO dao = new LoHang_DAO();
    private ArrayList<LoHang_DTO> listCache;

    private LoHang_BUS() {
        refreshData();
    }
    public static LoHang_BUS getInstance() {
        if (instance == null) {
            instance = new LoHang_BUS();
        }
        return instance;
    }
    public ArrayList<LoHang_DTO> getAll() {
        return new ArrayList<>(listCache);
    }

    public String getNextID() {
        return dao.getNextID();
    }

    public boolean them(LoHang_DTO lo) {
        boolean result = dao.them(lo);
        if (result) refreshData();
        return result;
    }

    public boolean capNhat(LoHang_DTO lo) {
        boolean result = dao.capNhat(lo);
        if (result) refreshData();
        return result;
    }

    public boolean capNhatTrangThai(String maLo, int trangThai) {
        boolean result = dao.capNhatTrangThai(maLo, trangThai);
        if (result) refreshData();
        return result;
    }

    public LoHang_DTO getById(String maLo) {
        for (LoHang_DTO lo : listCache) {
            if (lo.getMaLo().equals(maLo)) return lo;
        }
        return null;
    }
    public void capNhatTrangThaiLo(){
        LocalDate today = LocalDate.now();
        for(LoHang_DTO lo : listCache){
            int trangThai = LoHang_DTO.TK_BINH_THUONG;
            if(lo.getSoLuongSPCL() <= 0){
                trangThai = LoHang_DTO.TK_HET_HANG;
            }
            else if(lo.getHsd().isBefore(today)){
                trangThai = LoHang_DTO.TK_HET_HAN;
            }
            else if(lo.getHsd().minusDays(30).isBefore(today)){
                trangThai = LoHang_DTO.TK_SAP_HET_HAN;
            }
            if(lo.getTrangThaiTonKho() != trangThai){

                lo.setTrangThaiTonKho(trangThai);

                dao.capNhat(lo);
            }
        }
    }

    public String getNameNCC(String maNCC) {
        NhaCungCap_DTO ncc = NhaCungCap_BUS.getInstance().getById(maNCC);
        return ncc != null ? ncc.getTenNCC() : "";
    }

    public String getMaNCCByName(String nameNCC) {
        NhaCungCap_DTO ncc = NhaCungCap_BUS.getInstance().getByName(nameNCC);
        return ncc != null ? ncc.getMaNCC() : "";
    }

    public String getNameKVLT(String maKVLT) {
        KhuVucLuuTru_DTO kv = KhuVucLuuTru_BUS.getInstance().getById(maKVLT);
        return kv != null ? kv.getTenKVLT() : "";
    }

    public String getMaKVLTByName(String nameKVLT) {
        KhuVucLuuTru_DTO kv = KhuVucLuuTru_BUS.getInstance().getByName(nameKVLT);
        return kv != null ? kv.getMaKVLT() : "";
    }

    public String getNameSP(String maSP) {
        SanPham_DTO sp = SanPham_BUS.getInstance().getById(maSP);
        return sp != null ? sp.getTenSP() : "";
    }

    public String getTenNCCByPNK(String maPNK) {
        PhieuNhapKho_DTO pnk = PhieuNhapKho_BUS.getInstance().getById(maPNK);
        if (pnk == null) return "";
        return getNameNCC(pnk.getMaNCC());
    }

    public String getMaNCCByPNK(String maPNK) {
        PhieuNhapKho_DTO pnk = PhieuNhapKho_BUS.getInstance().getById(maPNK);
        return pnk != null ? pnk.getMaNCC() : "";
    }

    public String getTenKVLTByPNK(String maPNK) {
        PhieuNhapKho_DTO pnk = PhieuNhapKho_BUS.getInstance().getById(maPNK);
        if (pnk == null) return "";
        return getNameKVLT(pnk.getMaKVLT());
    }

    public String getMaSPByName(String name) {
        return PhieuNhapKho_BUS.getInstance().getMaSpByTen(name);
    }

    public int getSoLuongChiTiet(String maPNK, String maSP) {
        PhieuNhapKho_DTO pnk = PhieuNhapKho_BUS.getInstance().getById(maPNK);
        if (pnk == null) return -1;

        for (ChiTietPhieuNhapKho_DTO ct : pnk.getDs_chiTietPNK()) {
            if (ct.getMaSP().equals(maSP)) return ct.getSoLuong();
        }

        return -1;
    }

    public double getGiaNhap(String maNCC, String maSP) {

        ArrayList<SanPhamNCC_DTO> list =
                SanPhamNCC_BUS.getInstance().getByMaNCC(maNCC);

        for (SanPhamNCC_DTO sp : list)
            if (sp.getMaSanPham().equals(maSP))
                return sp.getGiaNhap();

        return -1;
    }

    public ArrayList<String> getTenSPChuaCoLoByPNK(String maPNK) {

        ArrayList<String> result = new ArrayList<>();

        PhieuNhapKho_DTO pnk = PhieuNhapKho_BUS.getInstance().getById(maPNK);

        if (pnk == null) return result;

        for (ChiTietPhieuNhapKho_DTO ct : pnk.getDs_chiTietPNK()) {
            if (ct.getMaLo() == null || ct.getMaLo().isEmpty()) {
                result.add(getNameSP(ct.getMaSP()));
            }
        }

        return result;
    }

    public ArrayList<String> getDSTenNCC() {

        ArrayList<String> list = new ArrayList<>();

        for (NhaCungCap_DTO ncc : NhaCungCap_BUS.getInstance().getAll()) {
            list.add(ncc.getTenNCC());
        }

        return list;
    }

    public ArrayList<String> getDSTenKVLT() {

        ArrayList<String> list = new ArrayList<>();

        for (KhuVucLuuTru_DTO kv : KhuVucLuuTru_BUS.getInstance().getAll()) {
            list.add(kv.getTenKVLT());
        }

        return list;
    }

    public ArrayList<String> getPNKTrangThaiChuanBi() {

        ArrayList<String> list = new ArrayList<>();

        for (PhieuNhapKho_DTO pnk :
                PhieuNhapKho_BUS.getInstance().getAll()) {

            if (pnk.getTrangThai() == PhieuNhapKho_DTO.TT_CHUAN_BI)
                list.add(pnk.getMa());
        }

        return list;
    }

    public ArrayList<LoHang_DTO> getLoConBanByMaSP(String maSp) {
        ArrayList<LoHang_DTO> result = new ArrayList<>();
        if (maSp == null || maSp.isEmpty()) return result;
        LocalDate today = LocalDate.now();
        for (LoHang_DTO lo : listCache) {
            if (lo.getMaSp().equals(maSp)
                    && lo.getSoLuongSPCL() > 0
                    && lo.getTrangThai() == 1
                    && !lo.getHsd().isBefore(today)) {
                result.add(lo);
            }
        }
        result.sort(Comparator.comparing(LoHang_DTO::getHsd));
        return result;
    }

    public void truHienCoKho(String maKVLT, int soLuong) {

        KhuVucLuuTru_DTO kv = KhuVucLuuTru_BUS.getInstance().getById(maKVLT);

        if (kv == null) return;

        int hienCoMoi = Math.max(0, kv.getHienCo() - soLuong);

        kv.setHienCo(hienCoMoi);

        KhuVucLuuTru_BUS.getInstance().update(kv);
    }

    public void congHienCoKho(String maKVLT, int soLuong) {

        KhuVucLuuTru_DTO kv = KhuVucLuuTru_BUS.getInstance().getById(maKVLT);

        if (kv == null) return;

        kv.setHienCo(kv.getHienCo() + soLuong);

        KhuVucLuuTru_BUS.getInstance().update(kv);
    }

    public boolean kiemTraDuTon(String maSp, int soLuongCanBan){
        if(soLuongCanBan <= 0)
            return false;
        return getTongSPTonByMaSP(maSp) >= soLuongCanBan;
    }

    public Map<LoHang_DTO, Integer> phanBoLoDeBan(String maSp, int soLuongCanBan) {

        Map<LoHang_DTO, Integer> result = new LinkedHashMap<>();
        if (!kiemTraDuTon(maSp, soLuongCanBan))
            return result;
        int canBan = soLuongCanBan;
        for (LoHang_DTO lo : getLoConBanByMaSP(maSp)) {
            if (canBan <= 0) break;
            int ton = lo.getSoLuongSPCL();
            int tru = Math.min(ton, canBan);
            result.put(lo, tru);
            canBan -= tru;
        }
        return result;
    }
    public int getSLSPThungByMaSP(String maSP){
        SanPham_DTO sp = SanPham_BUS.getInstance().getById(maSP);
        String maQC = sp.getMaQC();
        return QuyCach_BUS.getInstance().getById(maQC).getSlspThung();
    }
    
    public boolean banSanPham(String maSp,int soLuongBan){
        Map<LoHang_DTO,Integer> phanBo = phanBoLoDeBan(maSp,soLuongBan);
        if(phanBo.isEmpty()) return false;
        int slspThung = getSLSPThungByMaSP(maSp);
        for(Map.Entry<LoHang_DTO,Integer> e : phanBo.entrySet()){
            LoHang_DTO lo = e.getKey();
            int tru = e.getValue();
            int thungCu = lo.getSoLuongConLai();
            int spMoi = lo.getSoLuongSPCL() - tru;
            lo.setSoLuongSPCL(spMoi);
            int thungMoi = (int)Math.ceil((double)spMoi / slspThung);
            lo.setSoLuongConLai(thungMoi);
            dao.capNhat(lo);
            int chenhLech = thungMoi - thungCu;
            if(chenhLech != 0){
                KhuVucLuuTru_BUS kvbus = KhuVucLuuTru_BUS.getInstance();
                kvbus.getInstance().capNhatSoThung(lo.getMaKvlt(), chenhLech);
                kvbus.refreshData();
            }
        }
        refreshData();
        return true;
    }
    public void hoanTraSanPham(Map<LoHang_DTO,Integer> danhSachTra, String maSp){
        int slspThung = getSLSPThungByMaSP(maSp);
        for(Map.Entry<LoHang_DTO,Integer> e : danhSachTra.entrySet()){
            LoHang_DTO lo = e.getKey();
            int cong = e.getValue();
            int thungCu = lo.getSoLuongConLai();
            int spMoi = lo.getSoLuongSPCL() + cong;
            lo.setSoLuongSPCL(spMoi);
            int thungMoi = (int)Math.ceil((double)spMoi / slspThung);
            lo.setSoLuongConLai(thungMoi);
            dao.capNhat(lo);
            int chenhLech = thungMoi - thungCu;
            if(chenhLech != 0){
                KhuVucLuuTru_BUS kvbus = KhuVucLuuTru_BUS.getInstance();
                kvbus.getInstance().capNhatSoThung(lo.getMaKvlt(), chenhLech);
                kvbus.refreshData();
            }
        }
        refreshData();
    }
    public double getGiaNhapCaoNhatTrongLoChon(Map<LoHang_DTO, Integer> dsLo) {
        if (dsLo == null || dsLo.isEmpty()) return -1;
        double maxGiaNhap = Double.MIN_VALUE;
        for (LoHang_DTO lo : dsLo.keySet()) {
            maxGiaNhap = Math.max(maxGiaNhap, lo.getGiaNhap());
        }
        return maxGiaNhap;
    }

    public void congTonKhiHuy(Map<LoHang_DTO, Integer> dsLo) {
        if (dsLo == null) return;
        for (Map.Entry<LoHang_DTO, Integer> e : dsLo.entrySet()) {
            LoHang_DTO lo = e.getKey();
            int sl = e.getValue();
            lo.congSoLuongConLai(sl);
        }
        refreshData();
    }

    public int getTongTonByMaSP(String maSp) {
        ArrayList<LoHang_DTO> list = getByMaSP(maSp);
        int tong = 0;
        for (LoHang_DTO lo : list) {
            tong += lo.getSoLuongConLai();
        }
        return tong;
    }
    public int getTongSPTonByMaSP(String maSp) {
        int tong = 0;
        for (LoHang_DTO lo : getByMaSP(maSp)) {
            tong += lo.getSoLuongSPCL();
        }
        return tong;
    }
    public double getGiaNhapThapNhatByMaSP(String maSP) {
        ArrayList<LoHang_DTO> list = getByMaSP(maSP);
        double min = Double.MAX_VALUE;
        for (LoHang_DTO lo : list) {
            if (lo.getGiaNhap() < min) {
                min = lo.getGiaNhap();
            }
        }
        return min == Double.MAX_VALUE ? 0 : min;
    }

    public ArrayList<LoHang_DTO> getByMaSP(String maSp) {
        ArrayList<LoHang_DTO> result = new ArrayList<>();
        for (LoHang_DTO lo : listCache) {
            if (lo.getMaSp().equals(maSp))
                if(lo.getTrangThai() == LoHang_DTO.TT_HOAN_THANH
                        && lo.getTrangThaiTonKho() != LoHang_DTO.TK_HET_HANG
                        && lo.getTrangThaiTonKho() != LoHang_DTO.TK_HET_HAN)
                result.add(lo);
        }
        return result;
    }
    public ArrayList<LoHang_DTO> timKiem(
            String maLo,
            String maPNK,
            String maSP,
            String maNCC,
            String maKVLT,
            Integer tinhTrangSP,
            Integer trangThai
    ) {

        ArrayList<LoHang_DTO> result = new ArrayList<>(listCache);

        result = filterMaLo(result, maLo);
        result = filterMaPNK(result, maPNK);
        result = filterMaSP(result, maSP);
        result = filterMaNCC(result, maNCC);
        result = filterMaKVLT(result, maKVLT);
        result = filterTinhTrangHSD(result, tinhTrangSP);
        result = filterTrangThai(result, trangThai);

        return result;
    }

    private ArrayList<LoHang_DTO> filterMaLo(List<LoHang_DTO> list, String maLo) {

        if (maLo == null || maLo.trim().isEmpty())
            return new ArrayList<>(list);

        ArrayList<LoHang_DTO> result = new ArrayList<>();

        for (LoHang_DTO lo : list) {
            if (lo.getMaLo().toLowerCase().contains(maLo.toLowerCase())) {
                result.add(lo);
            }
        }

        return result;
    }
    private ArrayList<LoHang_DTO> filterMaPNK(List<LoHang_DTO> list, String maPNK) {

        if (maPNK == null || maPNK.trim().isEmpty())
            return new ArrayList<>(list);

        ArrayList<LoHang_DTO> result = new ArrayList<>();

        for (LoHang_DTO lo : list) {
            if (lo.getMaPnk().toLowerCase().contains(maPNK.toLowerCase())) {
                result.add(lo);
            }
        }

        return result;
    }
    private ArrayList<LoHang_DTO> filterMaSP(List<LoHang_DTO> list, String maSP) {

        if (maSP == null || maSP.trim().isEmpty())
            return new ArrayList<>(list);

        ArrayList<LoHang_DTO> result = new ArrayList<>();

        for (LoHang_DTO lo : list) {
            if (lo.getMaSp().equalsIgnoreCase(maSP)) {
                result.add(lo);
            }
        }

        return result;
    }
    private ArrayList<LoHang_DTO> filterMaNCC(List<LoHang_DTO> list, String maNCC) {

        if (maNCC == null || maNCC.trim().isEmpty())
            return new ArrayList<>(list);

        ArrayList<LoHang_DTO> result = new ArrayList<>();

        for (LoHang_DTO lo : list) {
            if (lo.getMaNcc().equalsIgnoreCase(maNCC)) {
                result.add(lo);
            }
        }

        return result;
    }
    private ArrayList<LoHang_DTO> filterMaKVLT(List<LoHang_DTO> list, String maKVLT) {

        if (maKVLT == null || maKVLT.trim().isEmpty())
            return new ArrayList<>(list);

        ArrayList<LoHang_DTO> result = new ArrayList<>();

        for (LoHang_DTO lo : list) {
            if (lo.getMaKvlt().equalsIgnoreCase(maKVLT)) {
                result.add(lo);
            }
        }

        return result;
    }
    private ArrayList<LoHang_DTO> filterTinhTrangHSD(List<LoHang_DTO> list, Integer tinhTrang) {

        if (tinhTrang == null)
            return new ArrayList<>(list);

        ArrayList<LoHang_DTO> result = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (LoHang_DTO lo : list) {

            LocalDate hsd = lo.getHsd();

            switch (tinhTrang) {

                case LoHang_DTO.TK_HET_HAN:
                    if (hsd.isBefore(today))
                        result.add(lo);
                    break;

                case LoHang_DTO.TK_SAP_HET_HAN:
                    if (!hsd.isBefore(today) && !hsd.isAfter(today.plusDays(30)))
                        result.add(lo);
                    break;

                case LoHang_DTO.TK_HET_HANG:
                    if (lo.getSoLuongConLai() == 0)
                        result.add(lo);
                    break;
            }
        }

        return result;
    }
    private ArrayList<LoHang_DTO> filterTrangThai(List<LoHang_DTO> list, Integer trangThai) {

        if (trangThai == null)
            return new ArrayList<>(list);

        ArrayList<LoHang_DTO> result = new ArrayList<>();

        for (LoHang_DTO lo : list) {
            if (lo.getTrangThai() == trangThai) {
                result.add(lo);
            }
        }

        return result;
    }

    public void refreshData() {
        listCache = dao.getAll();
    }
}