package dto;

public class KhuyenMai_DTO {

    private String maKM;
    private String tenKM;
    private int trangThai;
    private int doiTuongApDung; // Trong SQL là INT (0: Sản phẩm, 1: Danh mục)
    private int loaiKhuyenMai;  // Trong SQL là INT (0: phần trăm, 1: tiền mặt)
    private double giaTriKhuyenMai;

    private String maChuongTrinh;
    private String maSanPham;
    private String maDanhMuc;

    public KhuyenMai_DTO() {
    }

    public KhuyenMai_DTO(String maKM, String tenKM, int trangThai,
                         int doiTuongApDung, int loaiKhuyenMai,
                         double giaTriKhuyenMai,
                         String maChuongTrinh,
                         String maSanPham,
                         String maDanhMuc) {
        this.maKM = maKM;
        this.tenKM = tenKM;
        this.trangThai = trangThai;
        this.doiTuongApDung = doiTuongApDung;
        this.loaiKhuyenMai = loaiKhuyenMai;
        this.giaTriKhuyenMai = giaTriKhuyenMai;
        this.maChuongTrinh = maChuongTrinh;
        this.maSanPham = maSanPham;
        this.maDanhMuc = maDanhMuc;
    }

    // Getters and Setters
    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }

    public String getTenKM() { return tenKM; }
    public void setTenKM(String tenKM) { this.tenKM = tenKM; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    public int getDoiTuongApDung() { return doiTuongApDung; }
    public void setDoiTuongApDung(int doiTuongApDung) { this.doiTuongApDung = doiTuongApDung; }

    public int getLoaiKhuyenMai() { return loaiKhuyenMai; }
    public void setLoaiKhuyenMai(int loaiKhuyenMai) { this.loaiKhuyenMai = loaiKhuyenMai; }

    public double getGiaTriKhuyenMai() { return giaTriKhuyenMai; }
    public void setGiaTriKhuyenMai(double giaTriKhuyenMai) { this.giaTriKhuyenMai = giaTriKhuyenMai; }

    public String getMaChuongTrinh() { return maChuongTrinh; }
    public void setMaChuongTrinh(String maChuongTrinh) { this.maChuongTrinh = maChuongTrinh; }

    public String getMaSanPham() { return maSanPham; }
    public void setMaSanPham(String maSanPham) { this.maSanPham = maSanPham; }

    public String getMaDanhMuc() { return maDanhMuc; }
    public void setMaDanhMuc(String maDanhMuc) { this.maDanhMuc = maDanhMuc; }
}