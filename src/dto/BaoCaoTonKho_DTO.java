package dto;

import java.util.Date;

public class BaoCaoTonKho_DTO {
    private String maSP;
    private String tenSP;
    private String phanLoai;
    private int tonKho;
    private Date hanSuDung;
    private Date ngayLap;
    private String kho;

    public BaoCaoTonKho_DTO() {}

    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public String getPhanLoai() { return phanLoai; }
    public void setPhanLoai(String phanLoai) { this.phanLoai = phanLoai; }

    public int getTonKho() { return tonKho; }
    public void setTonKho(int tonKho) { this.tonKho = tonKho; }

    public Date getHanSuDung() { return hanSuDung; }
    public void setHanSuDung(Date hanSuDung) { this.hanSuDung = hanSuDung; }

    public Date getNgayLap() { return ngayLap; }
    public void setNgayLap(Date ngayLap) { this.ngayLap = ngayLap; }

    public String getKho() { return kho;  }
    public void setKho(String kho) { this.kho = kho; }
}