package dto;

import java.sql.Date;

public class ChuongTrinhKM_DTO extends UuDai_DTO {
    private String moTa;

    public ChuongTrinhKM_DTO() {
        super();
    }

    public ChuongTrinhKM_DTO(String ma, String ten, Date ngayBatDau, Date ngayKetThuc, int trangThai, String moTa) {
        super(ma, ten, ngayBatDau, ngayKetThuc, trangThai);
        this.moTa = moTa;
    }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
}