package dto;

public class PhanQuyenChucNang_DTO {

    private String maQuyen;
    private String maCN;

    public PhanQuyenChucNang_DTO() {}

    public PhanQuyenChucNang_DTO(String maQuyen, String maCN) {
        this.maQuyen = maQuyen;
        this.maCN = maCN;
    }

    public String getMaQuyen() {
        return maQuyen;
    }

    public void setMaQuyen(String maQuyen) {
        this.maQuyen = maQuyen;
    }

    public String getMaCN() {
        return maCN;
    }

    public void setMaCN(String maCN) {
        this.maCN = maCN;
    }
}