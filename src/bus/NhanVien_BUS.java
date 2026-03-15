package bus;

import dao.NhanVien_DAO;
import dto.NhanVien_DTO;
import dto.PhanQuyenChucNang_DTO;
import dto.PhanQuyen_DTO;
import dto.TaiKhoan_DTO;
import utils.ExcelNhanVien;
import utils.ExcelTaiKhoan;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class NhanVien_BUS {

    private static NhanVien_BUS instance;
    private NhanVien_DAO dao = new NhanVien_DAO();
    private ArrayList<NhanVien_DTO> listCache;

    private NhanVien_BUS() {
        listCache = dao.getAll();
    }

    public static NhanVien_BUS getInstance() {
        if (instance == null)
            instance = new NhanVien_BUS();
        return instance;
    }

    public ArrayList<NhanVien_DTO> getAll() {
        listCache = dao.getAll();
        return listCache;
    }

    public String getNextId() {
        return dao.getNextId();
    }

    public void refreshData() {
        listCache = dao.getAll();
    }

    public boolean them(NhanVien_DTO nv) {

        if (!kiemTraHopLe(nv, false)) return false;

        boolean result = dao.them(nv);
        if (result) refreshData();

        return result;
    }

    public boolean capNhat(NhanVien_DTO nv) {

        if (!kiemTraHopLe(nv, true)) return false;

        boolean result = dao.capNhat(nv);
        if (result) refreshData();

        return result;
    }

    public boolean kiemTraHopLe(NhanVien_DTO nv, boolean isUpdate) {

        if (nv.getTen().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên không được để trống");
            return false;
        }

        if (!nv.getSdt().matches("\\d{10}")) {
            JOptionPane.showMessageDialog(null, "SĐT phải 10 số");
            return false;
        }
        for (NhanVien_DTO item : listCache) {

            if (item.getSdt().equals(nv.getSdt())) {
                if (isUpdate && item.getMa().equals(nv.getMa())) {
                    continue;
                }

                JOptionPane.showMessageDialog(null, "SĐT đã tồn tại");
                return false;
            }
        }

        if (nv.getLuongCoBan() < 0) {
            JOptionPane.showMessageDialog(null, "Lương không hợp lệ");
            return false;
        }

        return true;
    }
    public ArrayList<NhanVien_DTO> timKiem(String keyword,
                                           String chucVu,
                                           Integer trangThai){

        ArrayList<NhanVien_DTO> result = new ArrayList<>();

        for(NhanVien_DTO nv : listCache){

            boolean match = true;

            if(keyword != null && !keyword.isEmpty()){
                String key = keyword.toLowerCase();

                if(!(nv.getMa().toLowerCase().contains(key) ||
                        nv.getTen().toLowerCase().contains(key) ||
                        nv.getSdt().contains(key))){
                    match = false;
                }
            }

            if(chucVu != null){
                if(!nv.getChucVu().equals(chucVu)){
                    match = false;
                }
            }

            if(trangThai != null){
                if(nv.getTrangThai() != trangThai){
                    match = false;
                }
            }

            if(match){
                result.add(nv);
            }
        }

        return result;
    }
    public NhanVien_DTO getById(String maNV) {
        for (NhanVien_DTO nv : listCache) {
            if (nv.getMa().equals(maNV))
                return nv;
        }
        return null;
    }
    public String getDiaChiByMaDC(String maDC) {
        return dao.getDiaChiByMaDC(maDC);
    }
    public NhanVien_DTO getBysdt(String sdt) {
        for (NhanVien_DTO nv : listCache) {
            if (nv.getSdt().equals(sdt)) {
                return nv;
            }
        }

        return null;
    }

    public ArrayList<PhanQuyen_DTO> getQuyenKhongCoCuaHang(){
        ArrayList<PhanQuyen_DTO> result = new ArrayList<>();
        for(PhanQuyen_DTO q : PhanQuyen_BUS.getInstance().getAll()){
            if(PhanQuyenChucNang_BUS.getInstance()
                    .khongCoChucNang(q.getMaQuyen(),"CUAHANG")){

                result.add(q);
            }
        }
        return result;
    }
    public ArrayList<PhanQuyen_DTO> getQuyenNhanVienHoatDong(){
        ArrayList<PhanQuyen_DTO> result = new ArrayList<>();
        for(PhanQuyen_DTO q : getQuyenKhongCoCuaHang()){
            if(q.getTrangThai() == PhanQuyen_DTO.TT_HOAT_DONG){
                result.add(q);
            }
        }
        return result;
    }
    public void taoTaiKhoanNhanVien(String sdt,String maQuyen){
        TaiKhoan_BUS tkBus = TaiKhoan_BUS.getInstance();
        TaiKhoan_DTO tk = new TaiKhoan_DTO();
        tk.setMaTK(tkBus.getNextID());
        tk.setSdt(sdt);
        tk.setMatKhau("123");
        tk.setMaQuyen(maQuyen);
        tk.setNgayKichHoat(LocalDate.now());
        tk.setTrangThai(TaiKhoan_DTO.TT_MO);

        tkBus.them(tk);
    }
    public void capNhatQuyenTaiKhoan(String sdt,String maQuyenCu,String maQuyenMoi){
        TaiKhoan_BUS tkBus = TaiKhoan_BUS.getInstance();
        for(TaiKhoan_DTO tk : tkBus.getAll()){
            if(tk.getSdt().equals(sdt) && tk.getMaQuyen().equals(maQuyenCu)){
                tk.setMaQuyen(maQuyenMoi);
                tkBus.capNhat(tk);
                break;
            }
        }
    }
    public ArrayList<String> getTenQuyenKhongCoCuaHangVaHoatDong(){
        ArrayList<String> result = new ArrayList<>();
        for(PhanQuyen_DTO pq : getQuyenNhanVienHoatDong()){
                result.add(pq.getTenQuyen());
        }
        return result;
    }
    public boolean exportExcel(String path){
        return ExcelNhanVien.exportExcel(getAll(),path);
    }
}