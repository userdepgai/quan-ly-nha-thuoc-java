package dto;

public class ChiTietHoaDonBan_DTO {

    private String maHDB;
    private String maLo;
    private String maKhuyenMai;

    private int soLuong;
    private double giaBan;
    private double giaBanSauApKM;
    private double thanhTien;

    public ChiTietHoaDonBan_DTO() {
    }

    public ChiTietHoaDonBan_DTO(String maHDB, String maLo, String maKhuyenMai,
                                int soLuong, double giaBan,
                                double giaBanSauApKM, double thanhTien) {
        this.maHDB = maHDB;
        this.maLo = maLo;
        this.maKhuyenMai = maKhuyenMai;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        this.giaBanSauApKM = giaBanSauApKM;
        this.thanhTien = thanhTien;
    }

    public String getMaHDB() {
        return maHDB;
    }

    public void setMaHDB(String maHDB) {
        this.maHDB = maHDB;
    }

    public String getMaLo() {
        return maLo;
    }

    public void setMaLo(String maLo) {
        this.maLo = maLo;
    }

    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public double getGiaBanSauApKM() {
        return giaBanSauApKM;
    }

    public void setGiaBanSauApKM(double giaBanSauApKM) {
        this.giaBanSauApKM = giaBanSauApKM;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }
}
