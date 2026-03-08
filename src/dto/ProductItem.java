package dto;
public class ProductItem {
    public String ma;
    public String ten;
    public double gia;
    public double giaSale;
    public int soLuong;
    public String maDM;
    public ProductItem(String ma, String ten, double gia, int soLuong, String maDM) {
        this.ma = ma;
        this.ten = ten;
        this.gia = gia;
        this.giaSale = gia;
        this.soLuong = soLuong;
        this.maDM = maDM;
    }
}