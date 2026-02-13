package dto;

public class KhuVucLuuTru_DTO {
    private String maKVLT;
    private String tenKVLT;
    private String ngayLapKho;
    private int sucChua;
    private int trangThai;
    private String maDC;

    public KhuVucLuuTru_DTO(String maKVLT, String tenKVLT, String ngayLapKho, int sucChua, int trangThai, String maDC) {
        this.maKVLT = maKVLT;
        this.tenKVLT = tenKVLT;
        this.ngayLapKho = ngayLapKho;
        this.sucChua = sucChua;
        this.trangThai = trangThai;
        this.maDC = maDC;
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

    public String getNgayLapKho() {
        return ngayLapKho;
    }
    public void setNgayLapKho(String ngayLapKho) {
        this.ngayLapKho = ngayLapKho;
    }

    public int getSucChua() {
        return sucChua; }
    public void setSucChua(int sucChua) {
        this.sucChua = sucChua;
    }

    public int getTrangThai() {
        return trangThai;
    }
    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaDC() {
        return maDC;
    }
    public void setMaDC(String maDC) {
        this.maDC = maDC;
    }
}