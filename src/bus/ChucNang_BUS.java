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

    public ArrayList<ChucNang_DTO> getAll() {
        return list;
    }
}