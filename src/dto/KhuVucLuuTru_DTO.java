package dto;
import DAO.DIACHI;

import java.sql.Date;

public class KhuVucLuuTru_DTO {
    private String maKVLT;
    private String tenKVLT;
    private Date ngayLapKho;
    private int sucChua;
    private int hienCo;
    private int trangThai;
    private DIACHI diaChi;


    public KhuVucLuuTru_DTO() {
    }

    public KhuVucLuuTru_DTO(String maKVLT, String tenKVLT,int hienCo, Date ngayLapKho, int sucChua, int trangThai, DIACHI diaChi) {
        this.maKVLT = maKVLT;
        this.tenKVLT = tenKVLT;
        this.ngayLapKho = ngayLapKho;
        this.hienCo=hienCo;
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
    public int getHienCo(){
        return hienCo;
    }
    public void setHienCo(int hienCo){
            this.hienCo=hienCo;
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

    public DIACHI getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(DIACHI diaChi) {
        this.diaChi = diaChi;
    }
}