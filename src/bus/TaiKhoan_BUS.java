package bus;

import dao.*;
import dto.*;
import utils.ExcelTaiKhoan;
import utils.Session;

import javax.swing.*;
import java.util.*;

public class TaiKhoan_BUS {
    private static TaiKhoan_BUS instance;
    private TaiKhoan_DAO dao = new TaiKhoan_DAO();
    private ArrayList<TaiKhoan_DTO> listCache;

    private PhanQuyen_BUS phanQuyenBus = PhanQuyen_BUS.getInstance();
    private NhanVien_BUS nhanVienBus = NhanVien_BUS.getInstance();
    private KhachHang_BUS khachHangBus = KhachHang_BUS.getInstance();

    private TaiKhoan_BUS() {
        listCache = dao.getAll();
    }
    public static TaiKhoan_BUS getInstance() {
        if (instance == null) {
            instance = new TaiKhoan_BUS();
        }
        return instance;
    }

    public ArrayList<TaiKhoan_DTO> getAll() {
        return dao.getAll();
    }

    public String getNextID() {
        return dao.getNextID();
    }

    public boolean them(TaiKhoan_DTO tk) {
        boolean result = dao.them(tk);
        if(result) refreshData();
        return result;
    }

    public boolean capNhat(TaiKhoan_DTO tk) {
        boolean result = dao.capNhat(tk);
        if(result) refreshData();
        return result;
    }

    public String getTenQuyen(String maQuyen) {
        PhanQuyen_DTO quyen = phanQuyenBus.getById(maQuyen);
        return quyen != null ? quyen.getTenQuyen() : "Không xác định";
    }
    public  String getMaQuyen(String tenQuyen) {
        PhanQuyen_DTO quyen = phanQuyenBus.getByName(tenQuyen);
        return quyen != null ? quyen.getMaQuyen() : "Không xác định";
    }
    public TaiKhoan_DTO getBySDT(String sdt) {
        for(TaiKhoan_DTO tk : listCache) {
            if(tk.getSdt().equals(sdt))
                return tk;
        }
        return null;
    }
    public String getNameKhachHang(String sdt){
        KhachHang_DTO kh = khachHangBus.getBysdt(sdt);
        return kh != null ? kh.getTen() : "Không tìm thấy";
    }
    public String getNameNhanVien(String sdt){
        NhanVien_DTO nv = nhanVienBus.getBysdt(sdt);
        return nv != null ? nv.getTen() : "Không tìm thấy";
    }

    public ArrayList<String> getTenQuyenHoatDong() {
        ArrayList<String> result = new ArrayList<>();

        for (PhanQuyen_DTO pq : phanQuyenBus.getQuyenHoatDong()) {
            result.add(pq.getTenQuyen());
        }
        return result;
    }
    public ArrayList<String> getTenQuyenNgungHoatDong() {
        ArrayList<String> result = new ArrayList<>();
        for (PhanQuyen_DTO pq : phanQuyenBus.getQuyenNgungHoatDong()) {
            result.add(pq.getTenQuyen());
        }
        return result;
    }
    public ArrayList<TaiKhoan_DTO> timKiem(String keyWord,String quyen, Integer trangThai) {
        ArrayList<TaiKhoan_DTO> result = new ArrayList<>();
        keyWord = keyWord.toLowerCase();
        for (TaiKhoan_DTO tk : listCache) {
            boolean matchTrangThai = (trangThai == null || tk.getTrangThai() == trangThai);
            boolean matchQuyen = (quyen == null || phanQuyenBus.getByName(quyen).getMaQuyen().equals(tk.getMaQuyen()));
            boolean matchKeyWord = tk.getSdt().toLowerCase().contains(keyWord) || keyWord == null;
            if ( matchKeyWord && matchQuyen && matchTrangThai)
                result.add(tk);
        }
        return result;
    }

    public ArrayList<TaiKhoan_DTO> dangNhap(String sdt, String matKhau) {
        ArrayList<TaiKhoan_DTO> result = new ArrayList<>();
        for (TaiKhoan_DTO tk : listCache) {
            if (tk.getSdt().equals(sdt)
                    && tk.getMatKhau().equals(matKhau)
                    && tk.getTrangThai() == 1) {
                result.add(tk);
            }
        }
        return result;
    }
    public boolean capNhatSDTNhanVien(String sdtCu, String sdtMoi) {
        for (TaiKhoan_DTO tk : listCache) {
            if (tk.getSdt().equals(sdtCu) && !tk.getMaQuyen().equals("Q001")) {
                boolean result = dao.capNhatSDT(tk.getMaTK(), sdtMoi);
                if (result) {
                    tk.setSdt(sdtMoi);
                }
            return result;
            }
        }
        return false;
    }
    public boolean capNhatSDTKhachHang(String sdtCu, String sdtMoi) {
        for (TaiKhoan_DTO tk : listCache) {
            if (tk.getSdt().equals(sdtCu) && tk.getMaQuyen().equals("Q001")) {
                boolean result = dao.capNhatSDT(tk.getMaTK(), sdtMoi);
                if (result) {
                    tk.setSdt(sdtMoi);
                }
                return result;
            }
        }
        return false;
    }
    public boolean tonTaiSDTNhanVien(String sdt) {
        for (TaiKhoan_DTO tk : listCache) {
            if (tk.getSdt().equals(sdt) && !tk.getMaQuyen().equals("Q001")) {
                return true;
            }
        }
        return false;
    }
    public boolean doiMatKhau(String mkHienTai, String mkMoi) {
        TaiKhoan_DTO tk = Session.getCurrentUser();
        if (tk == null)
            return false;
        if (!tk.getMatKhau().equals(mkHienTai))
            return false;
        boolean result = dao.doiMatKhau(tk.getSdt(), tk.getMaQuyen(), mkMoi);

        if (result) {
            tk.setMatKhau(mkMoi);
        }
        return result;
    }
    public void refreshData() {
        listCache = dao.getAll();
    }
    public boolean exportExcel(String path){
        return ExcelTaiKhoan.exportExcel(listCache,path);
    }
    public boolean importExcel(String path){
        ArrayList<TaiKhoan_DTO> list = ExcelTaiKhoan.importExcel(path);
        boolean ok = true;
        for(TaiKhoan_DTO tk : list){
            if(!dao.kiemTraMaTonTai(tk.getMaTK())) {
                if(!dao.them(tk)){
                    ok = false;
                }
            }
        }
        refreshData();

        return ok;
    }
}
