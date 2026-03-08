package bus;

import dao.ChucNang_DAO;
import dto.ChucNang_DTO;
import java.util.ArrayList;

public class ChucNang_BUS {

    private static ChucNang_BUS instance;
    private ChucNang_DAO dao = new ChucNang_DAO();
    private ArrayList<ChucNang_DTO> list;

    private ChucNang_BUS() {
        list = dao.getAll();
    }

    public static ChucNang_BUS getInstance() {
        if (instance == null) {
            instance = new ChucNang_BUS();
        }
        return instance;
    }

    public ChucNang_DTO getByName(String ten){

        for(ChucNang_DTO cn : list){
            if(cn.getTenCN().equalsIgnoreCase(ten)){
                return cn;
            }
        }

        return null;
    }

    public ChucNang_DTO getById(String ma){

        for(ChucNang_DTO cn : list){
            if(cn.getMaCN().equalsIgnoreCase(ma)){
                return cn;
            }
        }

        return null;
    }
    public ArrayList<ChucNang_DTO> getAll() {
        return list;
    }
}