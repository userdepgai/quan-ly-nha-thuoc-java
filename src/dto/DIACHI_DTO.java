package dto;

public class DIACHI_DTO {
    private String maDiaChi;
    private String tinh;
    private String phuong;
    private String duong;
    private String soNha;

    public DIACHI_DTO(String maDiaChi, String tinh, String phuong, String duong, String soNha) {
        this.maDiaChi = maDiaChi;
        this.tinh = tinh;
        this.phuong = phuong;
        this.duong = duong;
        this.soNha = soNha;
    }
    public DIACHI_DTO() {
    }

    public String getMaDiaChi() {
        return maDiaChi;
    }

    public void setMaDiaChi(String maDiaChi) {
        this.maDiaChi = maDiaChi;
    }

    public String getTinh() {
        return tinh;
    }

    public void setTinh(String tinh) {
        this.tinh = tinh;
    }

    public String getPhuong() {
        return phuong;
    }

    public void setPhuong(String phuong) {
        this.phuong = phuong;
    }

    public String getDuong() {
        return duong;
    }

    public void setDuong(String duong) {
        this.duong = duong;
    }

    public String getSoNha() {
        return soNha;
    }

    public void setSoNha(String soNha) {
        this.soNha = soNha;
    }

    @Override
    public String toString() {
        return soNha + ", " + duong + ", " + phuong + ", " + tinh;
    }
}
