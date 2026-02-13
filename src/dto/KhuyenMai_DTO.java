package dto;

public class KhuyenMai_DTO {
    private String maKM;
    private String tenKM;
    private String loaiKM;
    private double giaTriKM;
    private int trangThai;
    private int doiTuongApDung;
    private String maCTKM;
    private String maSP;
    private String maDM;

    public KhuyenMai_DTO() {
    }

    public KhuyenMai_DTO(String maKM, String tenKM, String loaiKM, double giaTriKM, int trangThai, int doiTuongApDung, String maCTKM, String maSP, String maDM) {
        this.maKM = maKM;
        this.tenKM = tenKM;
        this.loaiKM = loaiKM;
        this.giaTriKM = giaTriKM;
        this.trangThai = trangThai;
        this.doiTuongApDung = doiTuongApDung;
        this.maCTKM = maCTKM;
        this.maSP = maSP;
        this.maDM = maDM;
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }

    public String getTenKM() {
        return tenKM;
    }

    public void setTenKM(String tenKM) {
        this.tenKM = tenKM;
    }

    public String getLoaiKM() {
        return loaiKM;
    }

    public void setLoaiKM(String loaiKM) {
        this.loaiKM = loaiKM;
    }

    public double getGiaTriKM() {
        return giaTriKM;
    }

    public void setGiaTriKM(double giaTriKM) {
        this.giaTriKM = giaTriKM;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public int getDoiTuongApDung() {
        return doiTuongApDung;
    }

    public void setDoiTuongApDung(int doiTuongApDung) {
        this.doiTuongApDung = doiTuongApDung;
    }

    public String getMaCTKM() {
        return maCTKM;
    }

    public void setMaCTKM(String maCTKM) {
        this.maCTKM = maCTKM;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getMaDM() {
        return maDM;
    }

    public void setMaDM(String maDM) {
        this.maDM = maDM;
    }
}