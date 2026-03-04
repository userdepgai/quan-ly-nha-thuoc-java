package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GIOHANG {

    private JPanel MainPanel;
    private JPanel ItemPanel;
    private JPanel promoPanel;
    private JCheckBox tấtCảCheckBox;
    private JScrollPane scrollKhuyenMai;
    private JScrollPane scrollGioHang;
    private JPanel pnlDanhSachSP;

    private List<ProductItem> listGioHang = new ArrayList<>();

    public GIOHANG() {
        // 1. Ẩn panel Khuyến mãi lúc mới mở lên
        if (promoPanel != null) {
            promoPanel.setVisible(false);
        }

        if (pnlDanhSachSP != null) {
            pnlDanhSachSP.setLayout(new BoxLayout(pnlDanhSachSP, BoxLayout.Y_AXIS));
            pnlDanhSachSP.setBackground(Color.WHITE);
        }

        // Cài đặt cuộn mượt mà cho giỏ hàng
        if (scrollGioHang != null) {
            scrollGioHang.getVerticalScrollBar().setUnitIncrement(16);
            scrollGioHang.setBorder(BorderFactory.createEmptyBorder());
        }

        // 3. Thêm dữ liệu mẫu vào giỏ hàng để test
        listGioHang.add(new ProductItem("SP01", "Áo thun nam Basic", 150000, 1));
        listGioHang.add(new ProductItem("SP02", "Quần Jean ống rộng", 350000, 2));
        listGioHang.add(new ProductItem("SP03", "Giày Sneaker trắng", 500000, 1));

        // 4. Hiển thị danh sách sản phẩm ra màn hình
        renderGioHang();
    }

    // ==============================================================
    // HÀM VẼ LẠI DANH SÁCH SẢN PHẨM VÀO GIAO DIỆN
    // ==============================================================
    private void renderGioHang() {
        if (pnlDanhSachSP == null) return;

        pnlDanhSachSP.removeAll(); // Xóa sạch giao diện cũ

        for (ProductItem item : listGioHang) {
            JPanel cardSP = taoTheSanPham(item);
            pnlDanhSachSP.add(cardSP);
            pnlDanhSachSP.add(Box.createVerticalStrut(10)); // Khoảng cách giữa các sản phẩm
        }

        pnlDanhSachSP.revalidate();
        pnlDanhSachSP.repaint();
    }

    // ==============================================================
    // HÀM TẠO 1 CHIẾC THẺ SẢN PHẨM
    // ==============================================================
    private JPanel taoTheSanPham(ProductItem item) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#E0E0E0"), 1), // Viền xám nhạt tinh tế
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        // --- 1. BÊN TRÁI: Checkbox + Ảnh sản phẩm ---
        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlLeft.setOpaque(false);
        pnlLeft.add(new JCheckBox());

        JLabel lblAnh = new JLabel("ẢNH");
        lblAnh.setPreferredSize(new Dimension(80, 80));
        lblAnh.setOpaque(true);
        lblAnh.setBackground(Color.decode("#F5F5F5"));
        lblAnh.setForeground(Color.decode("#9E9E9E"));
        lblAnh.setHorizontalAlignment(SwingConstants.CENTER);
        lblAnh.setBorder(BorderFactory.createLineBorder(Color.decode("#EEEEEE")));
        pnlLeft.add(lblAnh);

        // --- 2. Ở GIỮA: Thông tin (Mã SP, Tên, Giá gốc) ---
        JPanel pnlInfo = new JPanel();
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
        pnlInfo.setOpaque(false);
        pnlInfo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel lblMa = new JLabel("Mã SP: " + item.ma);
        lblMa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMa.setForeground(Color.decode("#888888")); // Xám trung tính

        JLabel lblTen = new JLabel(item.ten);
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTen.setForeground(Color.decode("#222222")); // Đen xám dịu mắt

        // Format giá gốc bằng HTML để ép màu chính xác
        JLabel lblGiaGoc = new JLabel();
        lblGiaGoc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        if (item.giaSale < item.gia) {
            lblGiaGoc.setText("<html><font color='#999999'>Giá gốc: <strike>" + String.format("%,.0fđ", item.gia) + "</strike></font></html>");
        } else {
            lblGiaGoc.setText("<html><font color='#555555'>Giá gốc: " + String.format("%,.0fđ", item.gia) + "</font></html>");
        }

        pnlInfo.add(lblMa);
        pnlInfo.add(Box.createVerticalStrut(5));
        pnlInfo.add(lblTen);
        pnlInfo.add(Box.createVerticalStrut(5));
        pnlInfo.add(lblGiaGoc);

        // --- 3. BÊN PHẢI: Số lượng, Xóa (Trái) & Giá sale, Nút KM (Dưới) ---
        JPanel pnlRight = new JPanel(new BorderLayout());
        pnlRight.setOpaque(false);

        // Góc trên cùng bên phải
        JPanel pnlRightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlRightTop.setOpaque(false);

        JSpinner spinSL = new JSpinner(new SpinnerNumberModel(item.soLuong, 1, 100, 1));
        spinSL.setPreferredSize(new Dimension(50, 25));
        spinSL.addChangeListener(e -> {
            item.soLuong = (int) spinSL.getValue();
        });

        JButton btnXoa = new JButton("Xóa");
        btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnXoa.setForeground(Color.decode("#D32F2F")); // Đỏ mượt Material Design
        btnXoa.setContentAreaFilled(false);
        btnXoa.setBorderPainted(false);
        btnXoa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXoa.setMargin(new Insets(0, 0, 0, 0));

        btnXoa.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(MainPanel,
                    "Bỏ sản phẩm này khỏi giỏ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                listGioHang.remove(item);
                renderGioHang();
            }
        });

        pnlRightTop.add(spinSL);
        pnlRightTop.add(btnXoa);

        // Góc dưới cùng bên phải
        JPanel pnlRightBottom = new JPanel();
        pnlRightBottom.setLayout(new BoxLayout(pnlRightBottom, BoxLayout.Y_AXIS));
        pnlRightBottom.setOpaque(false);

        JLabel lblGiaSale = new JLabel();
        lblGiaSale.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Tăng size lên 16 cho nổi bật
        lblGiaSale.setForeground(Color.decode("#EE4D2D")); // Màu đỏ cam chuẩn E-commerce
        lblGiaSale.setAlignmentX(Component.RIGHT_ALIGNMENT);
        if (item.giaSale < item.gia) {
            lblGiaSale.setText("Sale: " + String.format("%,.0fđ", item.giaSale));
        } else {
            lblGiaSale.setText(" "); // Dùng space để giữ bố cục không bị xô lệch
        }

        JButton btnKhuyenMai = new JButton("Mã giảm giá ▼");
        btnKhuyenMai.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnKhuyenMai.setForeground(Color.decode("#1A73E8")); // Màu xanh dương Google hiện đại
        btnKhuyenMai.setContentAreaFilled(false);
        btnKhuyenMai.setBorderPainted(false);
        btnKhuyenMai.setFocusPainted(false);
        btnKhuyenMai.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnKhuyenMai.setMargin(new Insets(0, 0, 0, 0));
        btnKhuyenMai.setAlignmentX(Component.RIGHT_ALIGNMENT);

        btnKhuyenMai.addActionListener(e -> {
            if (promoPanel != null) {
                boolean isVisible = promoPanel.isVisible();
                promoPanel.setVisible(!isVisible);

                if (!isVisible) {
                    btnKhuyenMai.setText("Mã giảm giá ▲");
                    loadDanhSachKhuyenMai(item, lblGiaGoc, lblGiaSale, btnKhuyenMai);
                } else {
                    btnKhuyenMai.setText("Mã giảm giá ▼");
                }

                if (MainPanel != null) {
                    MainPanel.revalidate();
                    MainPanel.repaint();
                }
            }
        });

        pnlRightBottom.add(lblGiaSale);
        pnlRightBottom.add(Box.createVerticalStrut(5));
        pnlRightBottom.add(btnKhuyenMai);

        pnlRight.add(pnlRightTop, BorderLayout.NORTH);
        pnlRight.add(pnlRightBottom, BorderLayout.SOUTH);

        card.add(pnlLeft, BorderLayout.WEST);
        card.add(pnlInfo, BorderLayout.CENTER);
        card.add(pnlRight, BorderLayout.EAST);

        return card;
    }

    // ==============================================================
    // CÁC HÀM TẠO KHUYẾN MÃI (ĐÃ CẬP NHẬT MÀU VÀ LOGIC)
    // ==============================================================
    private JPanel taoTheKhuyenMai(String maKM, String moTa, double mucGiam, boolean isPhanTram,
                                   ProductItem item, JLabel lblGiaGoc, JLabel lblGiaSale, JButton btnKhuyenMai) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#E0E0E0"), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);

        JPanel pnlText = new JPanel();
        pnlText.setLayout(new BoxLayout(pnlText, BoxLayout.Y_AXIS));
        pnlText.setBackground(Color.WHITE);

        JLabel lblMa = new JLabel("Mã: " + maKM);
        lblMa.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMa.setForeground(Color.decode("#EE4D2D")); // Đồng bộ màu đỏ cam E-commerce

        JLabel lblMoTa = new JLabel(moTa);
        lblMoTa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMoTa.setForeground(Color.decode("#444444"));

        pnlText.add(lblMa);
        pnlText.add(Box.createVerticalStrut(5));
        pnlText.add(lblMoTa);

        JPanel pnlButton = new JPanel(new BorderLayout());
        pnlButton.setBackground(Color.WHITE);
        JButton btnApDung = new JButton("ÁP DỤNG");
        btnApDung.setBackground(Color.decode("#28A745")); // Xanh lá cây
        btnApDung.setForeground(Color.WHITE);
        btnApDung.setFocusPainted(false);
        btnApDung.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlButton.add(btnApDung, BorderLayout.CENTER);

        btnApDung.addActionListener(e -> {
            // Tính toán giá sale mới
            double tienGiam = isPhanTram ? (item.gia * mucGiam / 100) : mucGiam;
            item.giaSale = item.gia - tienGiam;
            if (item.giaSale < 0) item.giaSale = 0;

            // Cập nhật lại UI thẻ sản phẩm
            lblGiaSale.setText("Sale: " + String.format("%,.0fđ", item.giaSale));
            lblGiaGoc.setText("<html><font color='#999999'>Giá gốc: <strike>" + String.format("%,.0fđ", item.gia) + "</strike></font></html>");

            JOptionPane.showMessageDialog(MainPanel, "Đã áp dụng mã: " + maKM + "\nCho: " + item.ten);

            // Đóng panel khuyến mãi và trả lại trạng thái nút
            if (promoPanel != null) {
                promoPanel.setVisible(false);
                btnKhuyenMai.setText("Mã giảm giá ▼");
            }

        });

        card.add(pnlText, BorderLayout.CENTER);
        card.add(pnlButton, BorderLayout.EAST);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        return card;
    }

    private void loadDanhSachKhuyenMai(ProductItem item, JLabel lblGiaGoc, JLabel lblGiaSale, JButton btnKhuyenMai) {
        if (scrollKhuyenMai == null) return;

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        listPanel.add(taoTheKhuyenMai("GIAM10K", "Giảm thẳng 10.000đ", 10000, false, item, lblGiaGoc, lblGiaSale, btnKhuyenMai));
        listPanel.add(Box.createVerticalStrut(10));
        listPanel.add(taoTheKhuyenMai("SALE20", "Giảm 20% cho sản phẩm này", 20, true, item, lblGiaGoc, lblGiaSale, btnKhuyenMai));
        listPanel.add(Box.createVerticalStrut(10));
        listPanel.add(taoTheKhuyenMai("GIAM50K", "Giảm mạnh 50.000đ", 50000, false, item, lblGiaGoc, lblGiaSale, btnKhuyenMai));

        scrollKhuyenMai.setViewportView(listPanel);
        scrollKhuyenMai.getVerticalScrollBar().setUnitIncrement(16);
        scrollKhuyenMai.revalidate();
        scrollKhuyenMai.repaint();
        if (promoPanel != null) {
            promoPanel.revalidate();
        }
    }

    // ==============================================================
    // GETTER CHÍNH CHO GIAO DIỆN
    // ==============================================================
    public JPanel getMainPanel() {
        return MainPanel;
    }

    class ProductItem {
        String ma, ten;
        double gia;
        double giaSale;
        int soLuong;

        public ProductItem(String ma, String ten, double gia, int soLuong) {
            this.ma = ma;
            this.ten = ten;
            this.gia = gia;
            this.giaSale = gia;
            this.soLuong = soLuong;
        }
    }
}