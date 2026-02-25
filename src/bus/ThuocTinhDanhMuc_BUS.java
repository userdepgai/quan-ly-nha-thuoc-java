package bus;

import dao.ThuocTinhDanhMuc_DAO;
import dto.ThuocTinhDanhMuc_DTO;
import java.util.ArrayList;

public class ThuocTinhDanhMuc_BUS {
    private ThuocTinhDanhMuc_DAO ttDAO;

    public ThuocTinhDanhMuc_BUS() {
        ttDAO = new ThuocTinhDanhMuc_DAO();
    }

    public ArrayList<ThuocTinhDanhMuc_DTO> getAll() {
        return ttDAO.getAll();
    }

    public String getNextId() {
        return ttDAO.getNextId();
    }

    public boolean them(ThuocTinhDanhMuc_DTO tt) {
        if (tt.getTenThuocTinh().trim().isEmpty() || tt.getMaDM().trim().isEmpty()) {
            return false;
        }
        return ttDAO.them(tt);
    }

    public boolean capNhat(ThuocTinhDanhMuc_DTO tt) {
        if (tt.getTenThuocTinh().trim().isEmpty()) {
            return false;
        }
        return ttDAO.capNhat(tt);
    }
}