package dto;

public class KhachHang_KM_DTO extends GioiHanUuDai_DTO {

    private String maKM;

    public KhachHang_KM_DTO() {
        super();
    }

    public KhachHang_KM_DTO(int soLuotToiDa, int soLuotConLai,
                            String maKH, String maKM) {
        super(soLuotToiDa, soLuotConLai, maKH);
        this.maKM = maKM;
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }
}
