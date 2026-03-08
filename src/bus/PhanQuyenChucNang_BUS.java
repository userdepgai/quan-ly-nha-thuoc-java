package bus;

import dao.PhanQuyenChucNang_DAO;
import dto.PhanQuyenChucNang_DTO;

import java.util.ArrayList;

public class PhanQuyenChucNang_BUS {

    private static PhanQuyenChucNang_BUS instance;
    private PhanQuyenChucNang_DAO dao = new PhanQuyenChucNang_DAO();

    private PhanQuyenChucNang_BUS(){}

    public static PhanQuyenChucNang_BUS getInstance() {

        if(instance == null) {
            instance = new PhanQuyenChucNang_BUS();
        }

        return instance;
    }

    public boolean hasPermission(String maQuyen, String maCN) {
        return dao.hasPermission(maQuyen, maCN);
    }

    public ArrayList<String> getChucNangByQuyen(String maQuyen) {
        return dao.getChucNangByQuyen(maQuyen);
    }

    public boolean add(String maQuyen, String maCN) {

        PhanQuyenChucNang_DTO dto = new PhanQuyenChucNang_DTO(maQuyen, maCN);

        return dao.add(dto);
    }

    public void updatePermissions(String maQuyen, ArrayList<String> listMaChucNang){

        dao.deleteByMaQuyen(maQuyen);

        for(String maCN : listMaChucNang){
            dao.insert(maQuyen, maCN);
        }
    }

    public boolean delete(String maQuyen, String maCN) {
        return dao.delete(maQuyen, maCN);
    }

}