package dto;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PhieuNhapKho_DTO extends HoaDon_DTO{
    private String maKVLT;
    private String maNCC;
    private ArrayList<ChiTietPhieuNhapKho_DTO> ds_chiTietPNK;

    public PhieuNhapKho_DTO() {
        this.ds_chiTietPNK = new ArrayList<>();
    }

    public PhieuNhapKho_DTO(String ma, LocalDateTime ngayLap, LocalDateTime ngayHoanThanh, double thanhTien, int trangThai, String maNhanVien, String maKVLT, String maNCC, ArrayList<ChiTietPhieuNhapKho_DTO> ds_chiTietPNK) {
        super(ma, ngayLap, ngayHoanThanh, thanhTien, trangThai, maNhanVien);
        this.maKVLT = maKVLT;
        this.maNCC = maNCC;
        this.ds_chiTietPNK = ds_chiTietPNK;
    }

    public PhieuNhapKho_DTO(String maKVLT, String maNCC, ArrayList<ChiTietPhieuNhapKho_DTO> ds_chiTietPNK) {
        this.maKVLT = maKVLT;
        this.maNCC = maNCC;
        this.ds_chiTietPNK = ds_chiTietPNK;
    }

    public String getMaKVLT() {
        return maKVLT;
    }

    public void setMaKVLT(String maKVLT) {
        this.maKVLT = maKVLT;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public ArrayList<ChiTietPhieuNhapKho_DTO> getDs_chiTietPNK() {
        return ds_chiTietPNK;
    }

    public void setDs_chiTietPNK(ArrayList<ChiTietPhieuNhapKho_DTO> ds_chiTietPNK) {
        this.ds_chiTietPNK = ds_chiTietPNK;
    }
}
