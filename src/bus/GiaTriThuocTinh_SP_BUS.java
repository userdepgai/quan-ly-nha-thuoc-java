package bus;

import dao.GiaTriThuocTinh_SP_DAO;
import dto.GiaTriThuocTinh_SP_DTO;
import java.util.ArrayList;

public class GiaTriThuocTinh_SP_BUS {
    private static GiaTriThuocTinh_SP_BUS instance;
    private final GiaTriThuocTinh_SP_DAO dao = GiaTriThuocTinh_SP_DAO.getInstance();
    private ArrayList<GiaTriThuocTinh_SP_DTO> listCache;

    private GiaTriThuocTinh_SP_BUS() { refreshData(); }

    public static GiaTriThuocTinh_SP_BUS getInstance() {
        if (instance == null) instance = new GiaTriThuocTinh_SP_BUS();
        return instance;
    }

    public void refreshData() { listCache = dao.getAll(); }

    public ArrayList<GiaTriThuocTinh_SP_DTO> getAll() {
        if (listCache == null) refreshData();
        return listCache;
    }

    // Lấy danh sách liên kết theo mã thuộc tính (Để đổ bảng giá trị bên dưới khi click bảng trên)
    public ArrayList<GiaTriThuocTinh_SP_DTO> getByMaTT(String maTT) {
        ArrayList<GiaTriThuocTinh_SP_DTO> res = new ArrayList<>();
        if (maTT == null) return res;
        for (GiaTriThuocTinh_SP_DTO item : getAll()) {
            if (item.getMaThuocTinh().equalsIgnoreCase(maTT)) res.add(item);
        }
        return res;
    }

    // Lấy danh sách thuộc tính của 1 sản phẩm cụ thể (Dùng cho Hóa đơn/Chi tiết SP)
    public ArrayList<GiaTriThuocTinh_SP_DTO> getByMaSP(String maSP) {
        ArrayList<GiaTriThuocTinh_SP_DTO> res = new ArrayList<>();
        for (GiaTriThuocTinh_SP_DTO item : getAll()) {
            if (item.getMaSP().equalsIgnoreCase(maSP)) res.add(item);
        }
        return res;
    }

    public boolean them(GiaTriThuocTinh_SP_DTO dto) {
        if (dao.them(dto)) {
            refreshData();
            return true;
        }
        return false;
    }

    public boolean capNhat(GiaTriThuocTinh_SP_DTO dto) {
        if (dao.capNhat(dto)) {
            refreshData();
            return true;
        }
        return false;
    }

    public boolean xoa(String maGT, String maSP, String maTT) {
        if (dao.xoa(maGT, maSP, maTT)) {
            refreshData();
            return true;
        }
        return false;
    }

    public boolean themLienKetSP(String maSP, String maTT, String maGT, int trangThai) {
        GiaTriThuocTinh_SP_DTO dto = new GiaTriThuocTinh_SP_DTO(maSP, maTT, maGT, trangThai);
        if (dao.them(dto)) {
            refreshData();
            return true;
        }
        return false;
    }
}