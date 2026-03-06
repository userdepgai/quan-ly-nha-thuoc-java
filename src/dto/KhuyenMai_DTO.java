package dto;

public class KhuyenMai_DTO {
    // --- HẰNG SỐ TRẠNG THÁI ---
    public static final int TT_NGUNG_AP_DUNG = 0;
    public static final int TT_DANG_AP_DUNG = 1;
    public static final String NGUNG_AP_DUNG = "Ngưng áp dụng";
    public static final String DANG_AP_DUNG = "Đang áp dụng";

    // --- HẰNG SỐ LOẠI KM ---
    public static final int LOAI_PHAN_TRAM = 0;
    public static final int LOAI_TIEN_MAT = 1;
    public static final String PHAN_TRAM = "Phần trăm";
    public static final String TIEN_MAT = "Tiền mặt";

    // --- HẰNG SỐ ĐỐI TƯỢNG ---
    public static final int DT_DANH_MUC = 0;
    public static final int DT_SAN_PHAM = 1;
    public static final String DANH_MUC = "Danh mục";
    public static final String SAN_PHAM = "Sản phẩm";

    private String maKM;
    private String tenKM;
    private int loaiKhuyenMai;
    private double giaTriKhuyenMai;
    private int trangThai;
    private int doiTuongApDung;
    private String maChuongTrinh;
    private String maSanPham;
    private String maDanhMuc;

    public KhuyenMai_DTO() {}

    public KhuyenMai_DTO(String maKM, String tenKM, int loaiKhuyenMai, double giaTriKhuyenMai,
                         int trangThai, int doiTuongApDung,
                         String maChuongTrinh, String maSanPham, String maDanhMuc) {
        this.maKM = maKM;
        this.tenKM = tenKM;
        this.loaiKhuyenMai = loaiKhuyenMai;
        this.giaTriKhuyenMai = giaTriKhuyenMai;
        this.trangThai = trangThai;
        this.doiTuongApDung = doiTuongApDung;
        this.maChuongTrinh = maChuongTrinh;
        this.maSanPham = maSanPham;
        this.maDanhMuc = maDanhMuc;
    }

    // --- GETTER & SETTER ---
    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }

    public String getTenKM() { return tenKM; }
    public void setTenKM(String tenKM) { this.tenKM = tenKM; }

    public int getLoaiKhuyenMai() { return loaiKhuyenMai; }
    public void setLoaiKhuyenMai(int loaiKhuyenMai) { this.loaiKhuyenMai = loaiKhuyenMai; }

    public double getGiaTriKhuyenMai() { return giaTriKhuyenMai; }
    public void setGiaTriKhuyenMai(double giaTriKhuyenMai) { this.giaTriKhuyenMai = giaTriKhuyenMai; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    public int getDoiTuongApDung() { return doiTuongApDung; }
    public void setDoiTuongApDung(int doiTuongApDung) { this.doiTuongApDung = doiTuongApDung; }

    public String getMaChuongTrinh() { return maChuongTrinh; }
    public void setMaChuongTrinh(String maChuongTrinh) { this.maChuongTrinh = maChuongTrinh; }

    public String getMaSanPham() { return maSanPham; }
    public void setMaSanPham(String maSanPham) { this.maSanPham = maSanPham; }

    public String getMaDanhMuc() { return maDanhMuc; }
    public void setMaDanhMuc(String maDanhMuc) { this.maDanhMuc = maDanhMuc; }

    // --- CÁC HÀM HỖ TRỢ LOGIC HIỂN THỊ ---

    public String getTrangThaiText() {
        return (this.trangThai == TT_DANG_AP_DUNG) ? DANG_AP_DUNG : NGUNG_AP_DUNG;
    }

    public String getLoaiKMText() {
        return (loaiKhuyenMai == LOAI_PHAN_TRAM) ? PHAN_TRAM : TIEN_MAT;
    }

    public String getDoiTuongText() {
        return (doiTuongApDung == DT_SAN_PHAM) ? SAN_PHAM : DANH_MUC;
    }

    public static int parseTrangThaiFromText(String text) {
        if (DANG_AP_DUNG.equals(text)) return TT_DANG_AP_DUNG;
        return TT_NGUNG_AP_DUNG;
    }
}