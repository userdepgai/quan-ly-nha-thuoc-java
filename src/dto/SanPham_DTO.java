package dto;

import java.util.ArrayList;

public class SanPham_DTO {
    private String maSP;
    private String tenSP;
    private int donViTinh;
    private String hinhAnh;
    private double loiNhuan;
    private int trangThai;

    // ORM: Liên kết 1-1 với Danh mục và Quy cách
    private DanhMucSanPham_DTO danhMuc;
    private QuyCach_DTO quyCach;

    // ORM: Liên kết 1-n (Một sản phẩm có danh sách các nhà cung cấp)
    private ArrayList<NCC_SP_DTO> sp_dsNhaCungCap;

    public SanPham_DTO() {
        this.sp_dsNhaCungCap = new ArrayList<>(); // Khởi tạo danh sách tránh NullPointerException
    }

    // Getters and Setters
    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public int getDonViTinh() { return donViTinh; }
    public void setDonViTinh(int donViTinh) { this.donViTinh = donViTinh; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public double getLoiNhuan() { return loiNhuan; }
    public void setLoiNhuan(double loiNhuan) { this.loiNhuan = loiNhuan; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    public DanhMucSanPham_DTO getDanhMuc() { return danhMuc; }
    public void setDanhMuc(DanhMucSanPham_DTO danhMuc) { this.danhMuc = danhMuc; }

    public QuyCach_DTO getQuyCach() { return quyCach; }
    public void setQuyCach(QuyCach_DTO quyCach) { this.quyCach = quyCach; }

    public ArrayList<NCC_SP_DTO> getSp_dsNhaCungCap() { return sp_dsNhaCungCap; }
    public void setSp_dsNhaCungCap(ArrayList<NCC_SP_DTO> sp_dsNhaCungCap) {
        this.sp_dsNhaCungCap = sp_dsNhaCungCap;
    }
}