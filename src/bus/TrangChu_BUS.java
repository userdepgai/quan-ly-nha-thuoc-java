package bus;

import dao.TrangChu_DAO;
import dto.SanPham_DTO;
import dto.QuyCach_DTO;
import java.util.ArrayList;

public class TrangChu_BUS {

    private static TrangChu_BUS instance;
    private TrangChu_DAO dao = new TrangChu_DAO();

    public static TrangChu_BUS getInstance() {

        if (instance == null)
            instance = new TrangChu_BUS();

        return instance;
    }

    public ArrayList<SanPham_DTO> getAll() {
        return dao.getAll();
    }

    public double getGiaNhap(String maSP){
        return dao.getGiaNhapMoiNhat(maSP);
    }
    public ArrayList<SanPham_DTO> timKiem(String keyword, String locTheo, String khoangGia, String loai){

        ArrayList<SanPham_DTO> list = getAll();
        ArrayList<SanPham_DTO> result = new ArrayList<>();

        for(SanPham_DTO sp : list){

            boolean match = true;

            if(!keyword.isEmpty()){

                if(locTheo.equals("Lọc theo tên") && !sp.getTenSP().toLowerCase().contains(keyword.toLowerCase()))
                    match = false;

                if(locTheo.equals("Lọc theo mã") && !sp.getMaSP().toLowerCase().contains(keyword.toLowerCase()))
                    match = false;
            }

            String maLoai = "";

            if(loai.equals("Thuốc")) maLoai = "DM01";
            if(loai.equals("Thực phẩm chức năng")) maLoai = "DM02";
            if(loai.equals("Vật tư y tế")) maLoai = "DM03";

            if(!loai.equals("Tất cả")){
                if(!sp.getMaDM().equals(maLoai))
                    match = false;
            }

            if(match){
                result.add(sp);
            }
        }

        return result;
    }
    public double getGiaBan(String maSP) {
        // 1. Lấy giá nhập mới nhất từ DAO
        double giaNhapLo = dao.getGiaNhapMoiNhat(maSP);

        // Nếu giá nhập lô chưa có hoặc bằng 0, không cần tính tiếp
        if (giaNhapLo <= 0) return 0;

        for (SanPham_DTO sp : dao.getAll()) {
            if (sp.getMaSP().equals(maSP)) {
                // 2. Lấy lợi nhuận (giả sử lưu dạng 0.1 cho 10%)
                double loiNhuan = sp.getLoiNhuan();

                // 3. Lấy quy cách đóng gói
                QuyCach_DTO qc = dao.getQuyCach(sp.getMaQC());

                // Kiểm tra an toàn: Nếu không có quy cách hoặc dữ liệu bằng 0 để tránh lỗi chia cho 0
                if (qc == null || qc.getSlTrongHop() <= 0 || qc.getSlHopTrongThung() <= 0) {
                    return giaNhapLo * (1 + loiNhuan);
                }

                // 4. Áp dụng công thức từ Zalo: GiaSP = (GiaNhapLo / (SL_hop * SLSP_trong_hop)) * (1 + loi_nhuan)
                // Cực kỳ quan trọng: Ép kiểu sang double khi tính tổng sản phẩm để tránh chia số nguyên
                double tongSoLuongSP = (double) qc.getSlTrongHop() * qc.getSlHopTrongThung();

                double giaBanMotDonVi = (giaNhapLo / tongSoLuongSP) * (1 + loiNhuan);

                return giaBanMotDonVi;
            }
        }
        return 0;
    }
}