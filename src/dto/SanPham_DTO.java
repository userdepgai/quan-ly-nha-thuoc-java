package dto;

public class SanPham_DTO {

    private String maSP;
    private String tenSP;
    private String donViTinh;
    private double loiNhuan;
    private String hinhAnh;
    private int keDon;
    private int trangThai;

    private String maDM;    // Khóa ngoại tham chiếu đến DanhMuc
    private String maQC;    // Khóa ngoại tham chiếu đến QuyCach

    // Constructor mặc định (Không tham số)
    public SanPham_DTO() {
    }

    // Constructor đầy đủ tham số (Cập nhật thêm keDon)
    public SanPham_DTO(String maSP, String tenSP, String donViTinh,
                       double loiNhuan, String hinhAnh, int keDon,
                       int trangThai, String maDM, String maQC) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.donViTinh = donViTinh;
        this.loiNhuan = loiNhuan;
        this.hinhAnh = hinhAnh;
        this.keDon = keDon;
        this.trangThai = trangThai;
        this.maDM = maDM;
        this.maQC = maQC;
    }

    // --- GETTER & SETTER ---

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public double getLoiNhuan() {
        return loiNhuan;
    }

    public void setLoiNhuan(double loiNhuan) {
        this.loiNhuan = loiNhuan;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    // Getter cho thuộc tính mới
    public int getKeDon() {
        return keDon;
    }

    // Setter cho thuộc tính mới
    public void setKeDon(int keDon) {
        this.keDon = keDon;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaDM() {
        return maDM;
    }

    public void setMaDM(String maDM) {
        this.maDM = maDM;
    }

    public String getMaQC() {
        return maQC;
    }

    public void setMaQC(String maQC) {
        this.maQC = maQC;
    }

    // Phương thức toString() hỗ trợ debug/hiển thị nhanh
    @Override
    public String toString() {
        return this.tenSP;
    }
}