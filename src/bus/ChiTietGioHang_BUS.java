package bus;

import dao.ChiTietGioHang_DAO;
import dto.ChiTietGioHang_DTO;
import java.util.*;

public class ChiTietGioHang_BUS {

    private ChiTietGioHang_DAO ctDAO = new ChiTietGioHang_DAO();

    public ArrayList<ChiTietGioHang_DTO> getByMaGH(String maGH){
        return ctDAO.getByMaGH(maGH);
    }

    public boolean them(ChiTietGioHang_DTO ct){
        return ctDAO.them(ct);
    }

    public boolean capNhatSoLuong(ChiTietGioHang_DTO ct){
        return ctDAO.capNhatSoLuong(ct);
    }

    public boolean xoa(String maGH, String maSP){
        return ctDAO.xoa(maGH, maSP);
    }
    public ChiTietGioHang_DTO getById(String maGH, String maSP){
        return ctDAO.getById(maGH,maSP);
    }
}