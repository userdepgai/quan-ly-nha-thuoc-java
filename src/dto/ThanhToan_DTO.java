package dto;

public class ThanhToan_DTO {
    private String tenKH;
    private String sdt;
    private String diaChiChiTiet;

    public ThanhToan_DTO() {
    }

    public ThanhToan_DTO(String tenKH, String sdt, String diaChiChiTiet) {
        this.tenKH = tenKH;
        this.sdt = sdt;
        this.diaChiChiTiet = diaChiChiTiet;
    }

    public String getTenKH() { return tenKH; }
    public void setTenKH(String tenKH) { this.tenKH = tenKH; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getDiaChiChiTiet() { return diaChiChiTiet; }
    public void setDiaChiChiTiet(String diaChiChiTiet) { this.diaChiChiTiet = diaChiChiTiet; }
}