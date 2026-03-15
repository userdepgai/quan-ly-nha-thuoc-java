package bus;

import dao.PhanQuyenChucNang_DAO;
import dto.PhanQuyenChucNang_DTO;

import java.util.ArrayList;

public class PhanQuyenChucNang_BUS {

    private static PhanQuyenChucNang_BUS instance;

    private PhanQuyenChucNang_DAO dao = new PhanQuyenChucNang_DAO();

    private ArrayList<PhanQuyenChucNang_DTO> list;

    private PhanQuyenChucNang_BUS(){
        list = dao.getAll();
    }

    public static PhanQuyenChucNang_BUS getInstance(){

        if(instance == null){
            instance = new PhanQuyenChucNang_BUS();
        }

        return instance;
    }

    public void reload(){
        list = dao.getAll();
    }
    public ArrayList<PhanQuyenChucNang_DTO> getAll(){
        return list;
    }
    public boolean hasPermission(String maQuyen, String maCN){
        for(PhanQuyenChucNang_DTO pq : list){
            if(pq.getMaQuyen().equals(maQuyen)
                    && pq.getMaCN().equals(maCN)){
                return true;
            }
        }
        return false;
    }
    public ArrayList<String> getChucNangByQuyen(String maQuyen){
        ArrayList<String> result = new ArrayList<>();
        for(PhanQuyenChucNang_DTO pq : list){
            if(pq.getMaQuyen().equals(maQuyen)){
                result.add(pq.getMaCN());
            }
        }
        return result;
    }
    public boolean add(String maQuyen, String maCN){
        PhanQuyenChucNang_DTO dto = new PhanQuyenChucNang_DTO(maQuyen, maCN);
        boolean ok = dao.add(dto);
        if(ok){
            list.add(dto);
        }
        return ok;
    }
    public boolean delete(String maQuyen, String maCN){
        boolean ok = dao.delete(maQuyen, maCN);
        if(ok){
            list.removeIf(p ->
                    p.getMaQuyen().equals(maQuyen)
                            && p.getMaCN().equals(maCN));
        }
        return ok;
    }

    public void updatePermissions(String maQuyen, ArrayList<String> listMaChucNang){
        dao.deleteByMaQuyen(maQuyen);
        list.removeIf(p -> p.getMaQuyen().equals(maQuyen));

        for(String maCN : listMaChucNang){

            dao.insert(maQuyen, maCN);

            list.add(new PhanQuyenChucNang_DTO(maQuyen, maCN));
        }
    }
    public boolean coChucNang(String maQuyen, String maCN){
        ArrayList<String> list = dao.getChucNangByQuyen(maQuyen);
        return list.contains(maCN);
    }
    public boolean khongCoChucNang(String maQuyen, String maCN){
        return !coChucNang(maQuyen, maCN);
    }

}