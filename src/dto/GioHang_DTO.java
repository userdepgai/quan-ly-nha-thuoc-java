package dto;

public class GioHang_DTO {

    private String maGH;
    private String maKH;

    public GioHang_DTO() {
    }

    public GioHang_DTO(String maGH, String maKH) {
        this.maGH = maGH;
        this.maKH = maKH;
    }

    public String getMaGH() {
        return maGH;
    }

    public void setMaGH(String maGH) {
        this.maGH = maGH;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }
}