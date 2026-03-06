package dto; // Đổi tên 'dto' thành tên thư mục của bạn nếu cần

public class ProductItem {
    public String ma;
    public String ten;
    public double gia;
    public double giaSale;
    public int soLuong;

    public ProductItem(String ma, String ten, double gia, int soLuong) {
        this.ma = ma;
        this.ten = ten;
        this.gia = gia;
        this.giaSale = gia; // Mặc định giá sale bằng giá gốc
        this.soLuong = soLuong;
    }
}