package bus;

import dao.GioHang_DAO;
import dto.ChiTietGioHang_DTO;
import dto.GioHang_DTO;
import java.util.*;

public class GioHang_BUS {

    private static GioHang_BUS instance;
    private GioHang_DAO ghDAO = new GioHang_DAO();
    private ArrayList<GioHang_DTO> listCache;

    private List<ChiTietGioHang_DTO> danhSachGioHang = new ArrayList<>();
    private GioHang_BUS(){
        listCache = ghDAO.getAll();
    }

    public static GioHang_BUS getInstance(){
        if(instance == null){
            instance = new GioHang_BUS();
        }
        return instance;
    }
    public ArrayList<GioHang_DTO> getAll(){
        return listCache;
    }
    public String getNextId(){
        return ghDAO.getNextId();
    }

    public GioHang_DTO getByMaKH(String maKH){
        for(GioHang_DTO gh : listCache){
            if(gh.getMaKH().equals(maKH))
                return gh;
        }
        return null;
    }
    public boolean them(GioHang_DTO gh){
        boolean result = ghDAO.them(gh);
        if(result)
            refreshData();
        return result;
    }

    public void refreshData(){
        listCache = ghDAO.getAll();
    }
    public List<ChiTietGioHang_DTO> getDanhSachChiTiet() {
        return danhSachGioHang;
    }

    public void themGioHang(ChiTietGioHang_DTO spMoi) {
        boolean daTonTai = false;
        for (ChiTietGioHang_DTO item : danhSachGioHang) {
            if (item.getMaSP().equals(spMoi.getMaSP())) {
                item.setSoLuong(item.getSoLuong() + spMoi.getSoLuong());
                daTonTai = true;
                break;
            }
        }
        if (!daTonTai) {
            danhSachGioHang.add(spMoi);
        }
    }
    public void xoaSachGioHang() {
        danhSachGioHang.clear();
    }

    public boolean taoGioHangChoKhach(String maKH){
        GioHang_DTO gh = getByMaKH(maKH);
        if(gh != null){
            return true;
        }
        String maGH = getNextId();
        GioHang_DTO gioHang = new GioHang_DTO(maGH, maKH);
        return them(gioHang);
    }
}