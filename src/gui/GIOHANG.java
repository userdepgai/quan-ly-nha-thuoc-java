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
    // HÀM TẠO 1 CHIẾC THẺ SẢN PHẨM (CÓ TÍCH HỢP NÚT MÃ GIẢM GIÁ)
    // ==============================================================
    private JPanel taoTheSanPham(ProductItem item) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120)); // Tăng chiều cao để đủ chỗ cho nút

        // --- BÊN TRÁI: Checkbox + Ảnh sản phẩm ---
        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlLeft.setOpaque(false);
        pnlLeft.add(new JCheckBox());

        JLabel lblAnh = new JLabel("ẢNH");
        lblAnh.setPreferredSize(new Dimension(80, 80));
        lblAnh.setOpaque(true);
        lblAnh.setBackground(new Color(245, 245, 245));
        lblAnh.setHorizontalAlignment(SwingConstants.CENTER);
        lblAnh.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        pnlLeft.add(lblAnh);

        // --- Ở GIỮA: Thông tin (Tên, Giá, Nút Khuyến Mãi) ---
        JPanel pnlInfo = new JPanel();
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
        pnlInfo.setOpaque(false);

        JLabel lblTen = new JLabel(item.ten);
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lblGia = new JLabel(String.format("%,.0fđ", item.gia));
        lblGia.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblGia.setForeground(Color.RED);

        // Tạo nút bấm Khuyến Mãi ngay trong lòng sản phẩm
        JButton btnKhuyenMai = new JButton("Mã giảm giá ▼");
        btnKhuyenMai.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnKhuyenMai.setForeground(new Color(0, 102, 204)); // Màu xanh link
        btnKhuyenMai.setContentAreaFilled(false);
        btnKhuyenMai.setBorderPainted(false);
        btnKhuyenMai.setFocusPainted(false);
        btnKhuyenMai.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnKhuyenMai.setMargin(new Insets(0, 0, 0, 0));

        // Sự kiện khi nhấn nút "Mã giảm giá"
        btnKhuyenMai.addActionListener(e -> {
            if (promoPanel != null) {
                boolean isVisible = promoPanel.isVisible();
                promoPanel.setVisible(!isVisible);

                if (!isVisible) {
                    btnKhuyenMai.setText("Mã giảm giá ▲");
                    loadDanhSachKhuyenMai(); // Tải danh sách voucher
                } else {
                    btnKhuyenMai.setText("Mã giảm giá ▼");
                }

                // Cập nhật lại giao diện tổng
                if (MainPanel != null) {
                    MainPanel.revalidate();
                    MainPanel.repaint();
                }
            }
        });

        pnlInfo.add(Box.createVerticalStrut(5));
        pnlInfo.add(lblTen);
        pnlInfo.add(Box.createVerticalStrut(5));
        pnlInfo.add(lblGia);
        pnlInfo.add(Box.createVerticalStrut(5));
        pnlInfo.add(btnKhuyenMai); // Nhét nút vào panel giữa

        // --- BÊN PHẢI: Số lượng + Nút Xóa ---
        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 25));
        pnlRight.setOpaque(false);

        JSpinner spinSL = new JSpinner(new SpinnerNumberModel(item.soLuong, 1, 100, 1));
        spinSL.setPreferredSize(new Dimension(50, 25));
        spinSL.addChangeListener(e -> {
            item.soLuong = (int) spinSL.getValue();
        });

        JButton btnXoa = new JButton("Xóa");
        btnXoa.setForeground(Color.RED);
        btnXoa.setContentAreaFilled(false);
        btnXoa.setBorderPainted(false);
        btnXoa.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnXoa.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(MainPanel,
                    "Bỏ sản phẩm này khỏi giỏ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                listGioHang.remove(item);
                renderGioHang(); // Vẽ lại giao diện sau khi xóa
            }
        });

        pnlRight.add(spinSL);
        pnlRight.add(btnXoa);

        // Ghép 3 phần lại thành 1 thẻ hoàn chỉnh
        card.add(pnlLeft, BorderLayout.WEST);
        card.add(pnlInfo, BorderLayout.CENTER);
        card.add(pnlRight, BorderLayout.EAST);

        return card;
    }

    // ==============================================================
    // CÁC HÀM TẠO KHUYẾN MÃI (GIỮ NGUYÊN)
    // ==============================================================
    private JPanel taoTheKhuyenMai(String maKM, String moTa, double donToiThieu) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);

        JPanel pnlText = new JPanel();
        pnlText.setLayout(new BoxLayout(pnlText, BoxLayout.Y_AXIS));
        pnlText.setBackground(Color.WHITE);

        JLabel lblMa = new JLabel("Mã: " + maKM);
        lblMa.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMa.setForeground(new Color(220, 53, 69));

        JLabel lblMoTa = new JLabel(moTa);
        lblMoTa.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblDieuKien = new JLabel("Đơn tối thiểu: " + String.format("%,.0f", donToiThieu) + "đ");
        lblDieuKien.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblDieuKien.setForeground(Color.GRAY);

        pnlText.add(lblMa);
        pnlText.add(Box.createVerticalStrut(5));
        pnlText.add(lblMoTa);
        pnlText.add(Box.createVerticalStrut(5));
        pnlText.add(lblDieuKien);

        JPanel pnlButton = new JPanel(new BorderLayout());
        pnlButton.setBackground(Color.WHITE);
        JButton btnApDung = new JButton("ÁP DỤNG");
        btnApDung.setBackground(new Color(40, 167, 69));
        btnApDung.setForeground(Color.WHITE);
        btnApDung.setFocusPainted(false);
        pnlButton.add(btnApDung, BorderLayout.CENTER);

        btnApDung.addActionListener(e -> {
            JOptionPane.showMessageDialog(MainPanel, "Bạn đã áp dụng mã: " + maKM + "\n" + moTa);
            if (promoPanel != null) {
                promoPanel.setVisible(false);
            }
            Window window = SwingUtilities.getWindowAncestor(MainPanel);
            if (window != null) {
                window.pack();
            }
        });

        card.add(pnlText, BorderLayout.CENTER);
        card.add(pnlButton, BorderLayout.EAST);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        return card;
    }

    private void loadDanhSachKhuyenMai() {
        if (scrollKhuyenMai == null) return;

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        listPanel.add(taoTheKhuyenMai("GIAM10K", "Giảm thẳng 10.000đ vào tổng hóa đơn", 100000));
        listPanel.add(Box.createVerticalStrut(10));
        listPanel.add(taoTheKhuyenMai("SALE20", "Giảm 20% tối đa 50K cho khách mới", 250000));
        listPanel.add(Box.createVerticalStrut(10));
        listPanel.add(taoTheKhuyenMai("FREESHIP", "Miễn phí vận chuyển toàn quốc", 300000));

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

    // ==============================================================
    // CLASS PHỤ: LƯU TRỮ THÔNG TIN SẢN PHẨM
    // ==============================================================
    class ProductItem {
        String ma, ten;
        double gia;
        int soLuong;

        public ProductItem(String ma, String ten, double gia, int soLuong) {
            this.ma = ma;
            this.ten = ten;
            this.gia = gia;
            this.soLuong = soLuong;
        }
    }
}