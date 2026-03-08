package bus;

import dao.GioHang_DAO;
import dto.GioHang_DTO;
import java.util.*;

public class GioHang_BUS {

    private static GioHang_BUS instance;

    private GioHang_DAO ghDAO = new GioHang_DAO();

    private ArrayList<GioHang_DTO> listCache;

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
}