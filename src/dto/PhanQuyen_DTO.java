package dto;

public class PhanQuyen_DTO {
    private String maQuyen;
    private String tenQuyen;
    private String moTa;
    private int trangThai;

    public PhanQuyen_DTO(String maQuyen, String tenQuyen, String moTa, int trangThai) {
        this.maQuyen = maQuyen;
        this.tenQuyen = tenQuyen;
        this.moTa = moTa;
        this.trangThai = trangThai;
    }

    public String getMaQuyen() {
        return maQuyen;
    }

    public void setMaQuyen(String maQuyen) {
        this.maQuyen = maQuyen;
    }

    public String getTenQuyen() {
        return tenQuyen;
    }

    public void setTenQuyen(String tenQuyen) {
        this.tenQuyen = tenQuyen;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}
