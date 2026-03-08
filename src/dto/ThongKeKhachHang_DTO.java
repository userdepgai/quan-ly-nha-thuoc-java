package dto;

public class ThongKeKhachHang_DTO {
    private String maKH;
    private String tenKH;
    private String sdt;
    private int soLanMuaHang;
    private double tongTienChiTieu;
    private String xepHang;

    public ThongKeKhachHang_DTO() {}

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getTenKH() { return tenKH; }
    public void setTenKH(String tenKH) { this.tenKH = tenKH; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public int getSoLanMuaHang() { return soLanMuaHang; }
    public void setSoLanMuaHang(int soLanMuaHang) { this.soLanMuaHang = soLanMuaHang; }

    public double getTongTienChiTieu() { return tongTienChiTieu; }
    public void setTongTienChiTieu(double tongTienChiTieu) { this.tongTienChiTieu = tongTienChiTieu; }

    public String getXepHang() { return xepHang; }
    public void setXepHang(String xepHang) { this.xepHang = xepHang; }
}