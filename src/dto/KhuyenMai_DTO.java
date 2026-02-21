package dto;

public class KhuyenMai_DTO {
    private String maKM;
    private String tenKM;
    private int trangThai;
    private int doiTuongApDung;
    private int loaiKhuyenMai;
    private double giaTriKhuyenMai;

    // Kỹ thuật ORM: Tham chiếu đối tượng thay vì chỉ dùng ID
    private ChuongTrinhKM_DTO chuongTrinh;
    private SanPham_DTO sanPham;
    private DanhMucSanPham_DTO danhMuc;

    public KhuyenMai_DTO() {}

    public String getMaKMSP() { return maKM; }
    public void setMaKMSP(String maKM) { this.maKM = maKM; }

    public String getTenKMSP() { return tenKM; }
    public void setTenKMSP(String tenKM) { this.tenKM = tenKM; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    public int getDoiTuongApDung() { return doiTuongApDung; }
    public void setDoiTuongApDung(int doiTuongApDung) { this.doiTuongApDung = doiTuongApDung; }

    public int getLoaiKhuyenMai() { return loaiKhuyenMai; }
    public void setLoaiKhuyenMai(int loaiKhuyenMai) { this.loaiKhuyenMai = loaiKhuyenMai; }

    public double getGiaTriKhuyenMai() { return giaTriKhuyenMai; }
    public void setGiaTriKhuyenMai(double giaTriKhuyenMai) { this.giaTriKhuyenMai = giaTriKhuyenMai; }

    // Getter/Setter cho đối tượng liên kết
    public ChuongTrinhKM_DTO getChuongTrinh() { return chuongTrinh; }
    public void setChuongTrinh(ChuongTrinhKM_DTO chuongTrinh) { this.chuongTrinh = chuongTrinh; }
}