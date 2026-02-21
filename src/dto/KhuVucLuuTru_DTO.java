package dto;
import java.sql.Date;

public class KhuVucLuuTru_DTO {
    private String maKVLT;
    private String tenKVLT;
    private Date ngayLapKho;
    private int sucChua;
    private int trangThai;
    private DIACHI_DTO diaChi;


    public KhuVucLuuTru_DTO() {
    }

    public KhuVucLuuTru_DTO(String maKVLT, String tenKVLT, Date ngayLapKho, int sucChua, int trangThai, DIACHI_DTO diaChi) {
        this.maKVLT = maKVLT;
        this.tenKVLT = tenKVLT;
        this.ngayLapKho = ngayLapKho;
        this.sucChua = sucChua;
        this.trangThai = trangThai;
        this.diaChi = diaChi;
    }



    public String getMaKVLT() {
        return maKVLT;
    }

    public void setMaKVLT(String maKVLT) {
        this.maKVLT = maKVLT;
    }

    public String getTenKVLT() {
        return tenKVLT;
    }

    public void setTenKVLT(String tenKVLT) {
        this.tenKVLT = tenKVLT;
    }


    public Date getNgayLapKho() {
        return ngayLapKho;
    }


    public void setNgayLapKho(Date ngayLapKho) {
        this.ngayLapKho = ngayLapKho;
    }

    public int getSucChua() {
        return sucChua;
    }

    public void setSucChua(int sucChua) {
        this.sucChua = sucChua;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public DIACHI_DTO getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(DIACHI_DTO diaChi) {
        this.diaChi = diaChi;
    }
}