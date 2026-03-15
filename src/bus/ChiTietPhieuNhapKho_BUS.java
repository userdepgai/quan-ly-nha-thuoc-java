package bus;

import dao.*;
import dto.*;

import java.util.*;

public class ChiTietPhieuNhapKho_BUS {

    private static ChiTietPhieuNhapKho_BUS instance;
    private ChiTietPhieuNhapKho_DAO ctDao = new ChiTietPhieuNhapKho_DAO();

    private ChiTietPhieuNhapKho_BUS(){}

    public static ChiTietPhieuNhapKho_BUS getInstance() {
        if(instance == null) {
            instance = new ChiTietPhieuNhapKho_BUS();
        }
        return instance;
    }

    public ArrayList<ChiTietPhieuNhapKho_DTO> getByMaPNK(String maPNK) {
        return ctDao.getByMaPNK(maPNK);
    }

    public boolean them(ChiTietPhieuNhapKho_DTO ct) {
        if(ct.getSoLuong() <= 0) {
            return false;
        }
        return ctDao.them(ct);
    }

    public boolean xoa(String maPNK, String maSP) {
        return ctDao.xoa(maPNK,maSP);
    }
    public boolean capNhat(ChiTietPhieuNhapKho_DTO ct){return ctDao.capNhat(ct);}
}