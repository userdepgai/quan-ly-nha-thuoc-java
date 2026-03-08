package bus;

import dto.KhachHang_DTO;
import dto.KhachHang_DiaChi_DTO;
import dto.TaiKhoan_DTO;
import utils.Session;

import java.util.ArrayList;
import bus.*;
import dto.*;
import dao.*;

public class ThongTinCaNhanKhachHang_BUS {

    private KhachHang_BUS khBus = KhachHang_BUS.getInstance();

    public KhachHang_DTO getKhachHangDangNhap(){
        TaiKhoan_DTO tk = Session.getCurrentUser();
        if(tk == null)
            return null;
        return khBus.getBysdt(tk.getSdt());
    }
    public boolean capNhatThongTin(KhachHang_DTO khMoi){
        KhachHang_DTO khCu = khBus.getBysdt(Session.getCurrentUser().getSdt());
        if(khCu == null)
            return false;
        String sdtCu = khCu.getSdt();
        String sdtMoi = khMoi.getSdt();

        if(!sdtCu.equals(sdtMoi)){
            if(kiemTraTonTaiSDT(sdtMoi))
                return false;
            boolean resultTK = TaiKhoan_BUS.getInstance().capNhatSDTKhachHang(sdtCu,sdtMoi);
            if(!resultTK)
                return false;
        }
        return khBus.capNhat(khMoi);
    }
    public boolean kiemTraTonTaiSDT(String sdt){
        for(KhachHang_DTO kh : KhachHang_BUS.getInstance().getAll()){
            if(kh.getSdt().equals(sdt))
                return true;
        }
        return false;
    }
//    public ArrayList<KhachHang_DiaChi_DTO> getKH_DC() {
//        KhachHang_DTO kh = getKhachHangDangNhap();
//        for(KhachHang_DiaChi_DTO khdc : )
//    }
}