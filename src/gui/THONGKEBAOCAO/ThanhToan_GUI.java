package gui.THONGKEBAOCAO;

import dto.ProductItem;
import dto.Voucher_DTO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Kế thừa JPanel để có thể lật trang (CardLayout)
public class ThanhToan_GUI extends JPanel {

    private JPanel mainPanel;

    private JTextField textTen;
    private JTextField textDiaChi;
    private JTextField textSDT;
    private JButton voucherButton;
    private JComboBox<String> comboBoxPthucTt;
    private JTextField textTienHang;
    private JTextField textPhiVanChuyen;
    private JTextField textTongGiamGia;
    private JTextField textTongThanhToan; // Ô này là Tổng Thanh Toán cuối cùng
    private JButton đặtHàngButton;
    private JTextField textField1;

    // --- CÁC BIẾN ĐỂ LƯU DỮ LIỆU TỪ GIỎ HÀNG TRUYỀN SANG ---
    private List<ProductItem> danhSachMua;
    private double tienHangGoc;
    private Voucher_DTO voucherApDung;
    private final double PHI_VAN_CHUYEN = 15000; // Cố định phí ship 15k theo bản vẽ

    public ThanhToan_GUI() {
        // Thêm mainPanel vào JPanel hiện tại (Cần thiết nếu dùng IntelliJ GUI Builder)
        if (mainPanel != null) {
            this.setLayout(new BorderLayout());
            this.add(mainPanel, BorderLayout.CENTER);
        }

        caiDatGiaoDienBanDau();
        ganSuKien();
    }

    // ==============================================================
    // 1. HÀM CÀI ĐẶT GIAO DIỆN (Khóa textfield, setup combobox)
    // ==============================================================
    private void caiDatGiaoDienBanDau() {
        // Khóa các ô tính tiền, không cho người dùng tự sửa
        textTienHang.setEditable(false);
        textPhiVanChuyen.setEditable(false);
        textTongGiamGia.setEditable(false);
        textTongThanhToan.setEditable(false);

        // Đổi màu chữ cho nổi bật
        textTongThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        textTongThanhToan.setForeground(Color.RED);

        // Thêm các phương thức thanh toán vào ComboBox
        if (comboBoxPthucTt != null) {
            comboBoxPthucTt.addItem("Thanh toán tiền mặt (COD)");
            comboBoxPthucTt.addItem("Chuyển khoản ngân hàng");
            comboBoxPthucTt.addItem("Ví MoMo");
        }
    }

    // ==============================================================
    // 2. HÀM NHẬN DỮ LIỆU TỪ GIỎ HÀNG (Sẽ được GIOHANG.java gọi)
    // ==============================================================
    public void setDuLieuThanhToan(List<ProductItem> dsMua, double tongTien, Voucher_DTO voucher) {
        this.danhSachMua = dsMua;
        this.tienHangGoc = tongTien;
        this.voucherApDung = voucher;

        // Sau khi nhận xong dữ liệu thì tiến hành tính tiền và in ra
        tinhToanVaHienThi();
    }

    // ==============================================================
    // 3. HÀM TÍNH TOÁN & HIỂN THỊ TIỀN
    // ==============================================================
    private void tinhToanVaHienThi() {
        // 1. Hiện tổng tiền hàng
        textTienHang.setText(String.format("%,.0f VNĐ", tienHangGoc));

        // 2. Hiện phí vận chuyển
        textPhiVanChuyen.setText(String.format("%,.0f VNĐ", PHI_VAN_CHUYEN));

        // 3. Tính tiền giảm từ Voucher (nếu có)
        double tienGiam = 0;
        if (voucherApDung != null) {
            if (voucherApDung.getLoaiVoucher() == 0) {
                // Giảm theo %
                tienGiam = tienHangGoc * voucherApDung.getGiaTriVoucher();
            } else {
                // Giảm tiền mặt thẳng
                tienGiam = voucherApDung.getGiaTriVoucher();
            }
            voucherButton.setText("🏷️ Đã áp dụng: " + voucherApDung.getMa());
        } else {
            voucherButton.setText("Chọn Voucher");
        }

        textTongGiamGia.setText(String.format("- %,.0f VNĐ", tienGiam));

        // 4. TÍNH TỔNG THANH TOÁN CUỐI CÙNG
        double tongThanhToan = tienHangGoc + PHI_VAN_CHUYEN - tienGiam;
        if (tongThanhToan < 0) tongThanhToan = 0; // Tránh trường hợp voucher giảm lố tiền

        textTongThanhToan.setText(String.format("%,.0f VNĐ", tongThanhToan));
    }

    // ==============================================================
    // 4. HÀM GẮN SỰ KIỆN NÚT BẤM (Nút Đặt Hàng)
    // ==============================================================
    private void ganSuKien() {
        if (đặtHàngButton != null) {
            đặtHàngButton.addActionListener(e -> {
                // Kiểm tra xem khách đã nhập đủ thông tin giao hàng chưa
                String ten = textTen.getText().trim();
                String sdt = textSDT.getText().trim();
                String diaChi = textDiaChi.getText().trim();

                if (ten.isEmpty() || sdt.isEmpty() || diaChi.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Tên, Số điện thoại và Địa chỉ!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!sdt.matches("\\d{10,11}")) {
                    JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ (phải từ 10-11 số)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Nếu mọi thứ OK -> Ghi dữ liệu vào Database (Bảng HOADON)
                // Code lưu Database bạn sẽ viết ở đây...

                JOptionPane.showMessageDialog(this, "🎉 Đặt hàng thành công!\nCảm ơn " + ten + " đã mua sắm.");

                // Sau khi đặt xong, có thể clear dữ liệu hoặc quay về trang chủ
            });
        }
    }
    // ==============================================================
    // HÀM MAIN ĐỂ CHẠY TEST ĐỘC LẬP GIAO DIỆN NÀY
    // ==============================================================
    public static void main(String[] args) {
        // Chạy giao diện trong luồng an toàn của Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Đổi giao diện sang giao diện mặc định của hệ điều hành cho đẹp
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 1. Tạo một cửa sổ (Frame) để chứa cái Panel Thanh Toán
            JFrame frame = new JFrame("TEST GIAO DIỆN THANH TOÁN");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 600); // Kích thước cửa sổ test
            frame.setLocationRelativeTo(null); // Cho hiển thị ra giữa màn hình

            // 2. Khởi tạo Panel Thanh Toán của bạn
            ThanhToan_GUI pnlThanhToan = new ThanhToan_GUI();

            // 3. TẠO DỮ LIỆU GIẢ ĐỂ TEST (Giả lập khách đã chọn hàng từ Giỏ Hàng)
            java.util.List<ProductItem> mockDanhSach = new java.util.ArrayList<>();

            // Giả sử tạo 1 món hàng (Bạn có thể phải sửa lại cách gán giá trị tùy vào class ProductItem của bạn nhé)
            // Truyền 5 tham số vào: Mã SP, Tên SP, Giá Sale, Số Lượng, Mã Danh Mục (hoặc ảnh)
            ProductItem sp1 = new ProductItem("SP000009", "Kem chống nắng Vichy", 37120.0, 2, "DM001");

            // Giả lập tổng tiền từ giỏ hàng ném sang (37.120 x 2)
            double mockTongTien = 74240;

            // Giả lập Voucher (Ở đây mình truyền null tức là không xài voucher.
            // Nếu bạn muốn test voucher thì khởi tạo new Voucher_DTO() rồi truyền vào nhé)
            Voucher_DTO mockVoucher = null;

            // 4. Bơm dữ liệu giả vào Panel
            pnlThanhToan.setDuLieuThanhToan(mockDanhSach, mockTongTien, mockVoucher);

            // 5. Gắn Panel vào Frame và hiển thị lên
            frame.add(pnlThanhToan);
            frame.setVisible(true);
        });
    }
}