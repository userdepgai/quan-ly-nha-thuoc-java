package dto;

public class GioiHanUuDai_DTO {

    protected int soLuotToiDa;
    protected int soLuotConLai;
    protected String maKH;

    public GioiHanUuDai_DTO() {
    }

    public GioiHanUuDai_DTO(int soLuotToiDa, int soLuotConLai, String maKH) {
        this.soLuotToiDa = soLuotToiDa;
        this.soLuotConLai = soLuotConLai;
        this.maKH = maKH;
    }

    public int getSoLuotToiDa() {
        return soLuotToiDa;
    }

    public void setSoLuotToiDa(int soLuotToiDa) {
        this.soLuotToiDa = soLuotToiDa;
    }

    public int getSoLuotConLai() {
        return soLuotConLai;
    }

    public void setSoLuotConLai(int soLuotConLai) {
        this.soLuotConLai = soLuotConLai;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }
}
