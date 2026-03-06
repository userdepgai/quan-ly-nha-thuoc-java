package gui;

import dto.ProductItem;
import bus.GioHangManager;

import javax.swing.*;
import java.awt.*;

public class GIOHANG extends JPanel {

    private JPanel MainPanel;
    private JPanel ItemPanel;
    private JPanel promoPanel;
    private JCheckBox tấtCảCheckBox;
    private JScrollPane scrollVoucher;
    private JScrollPane scrollGioHang;
    private JPanel pnlDanhSachSP;
    private JButton btnMuaHang;
    private JTextField textTtien;

    private double voucherToanDon = 0;

    public GIOHANG() {
        this.setLayout(new BorderLayout());
        if (MainPanel != null) {
            this.add(MainPanel, BorderLayout.CENTER);
            MainPanel.setBackground(Color.decode("#F5F5F5"));
        }

        if (promoPanel != null) {
            promoPanel.setVisible(true);
            promoPanel.setBackground(Color.WHITE);
        }

        if (pnlDanhSachSP != null) {
            pnlDanhSachSP.setLayout(new BoxLayout(pnlDanhSachSP, BoxLayout.Y_AXIS));
            pnlDanhSachSP.setBackground(Color.decode("#F5F5F5"));
        }

        if (scrollGioHang != null) {
            scrollGioHang.getVerticalScrollBar().setUnitIncrement(16);
            scrollGioHang.setBorder(BorderFactory.createEmptyBorder());
            scrollGioHang.getViewport().setBackground(Color.decode("#F5F5F5"));
        }

        if (textTtien != null) {
            textTtien.setEditable(false);
            textTtien.setFont(new Font("Segoe UI", Font.BOLD, 18));
            textTtien.setForeground(Color.decode("#EE4D2D"));
            textTtien.setHorizontalAlignment(JTextField.RIGHT);
            textTtien.setBorder(null);
            textTtien.setOpaque(false);
        }

        if (btnMuaHang != null) {
            btnMuaHang.setText("Mua Hàng");
            btnMuaHang.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btnMuaHang.setBackground(Color.decode("#EE4D2D"));
            btnMuaHang.setForeground(Color.WHITE);
            btnMuaHang.setFocusPainted(false);
            btnMuaHang.setBorderPainted(false);
            btnMuaHang.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btnMuaHang.addActionListener(e -> {
                if (GioHangManager.danhSachGioHang.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống!");
                } else {
                    JOptionPane.showMessageDialog(this, "Thanh Toán thành công!\nBạn đã trả: " + textTtien.getText());
                    GioHangManager.danhSachGioHang.clear();
                    loadData();
                }
            });
        }

        loadDanhSachVoucher();
        loadData();
    }

    private void tinhTongTien() {
        if (textTtien == null) return;

        double tongTienHang = 0;
        for (ProductItem item : GioHangManager.danhSachGioHang) {
            tongTienHang += (item.giaSale * item.soLuong);
        }

        double tongThanhToan = tongTienHang - voucherToanDon;
        if (tongThanhToan < 0) tongThanhToan = 0;

        textTtien.setText(String.format("%,.0f VNĐ", tongThanhToan));
    }

    public void loadData() {
        voucherToanDon = 0;
        renderGioHang();
    }

    private void renderGioHang() {
        if (pnlDanhSachSP == null) return;
        pnlDanhSachSP.removeAll();

        for (ProductItem item : GioHangManager.danhSachGioHang) {
            JPanel cardSP = taoTheSanPham(item);
            pnlDanhSachSP.add(cardSP);
            pnlDanhSachSP.add(Box.createVerticalStrut(10));
        }

        pnlDanhSachSP.revalidate();
        pnlDanhSachSP.repaint();

        tinhTongTien();
    }

    // ==============================================================
    // 1. KHU VỰC VOUCHER TOÀN ĐƠN (SỔ XUỐNG Y HỆT KHUYẾN MÃI SP)
    // ==============================================================
    private void loadDanhSachVoucher() {
        if (scrollVoucher == null) return;

        // Khung bọc ngoài giống hệt thẻ sản phẩm
        JPanel wrapperVoucher = new JPanel(new BorderLayout());
        wrapperVoucher.setBackground(Color.WHITE);
        wrapperVoucher.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#E0E0E0"), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Nút bấm "Chọn Voucher" (Nằm bên trái hoặc phải tùy ý, ở đây để bên trái cho dễ nhìn)
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTop.setOpaque(false);

        JButton btnToggleVoucher = new JButton("Chọn Voucher toàn đơn ▼");
        btnToggleVoucher.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnToggleVoucher.setForeground(Color.decode("#1A73E8")); // Màu xanh dương để phân biệt
        btnToggleVoucher.setContentAreaFilled(false);
        btnToggleVoucher.setBorderPainted(false);
        btnToggleVoucher.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlTop.add(btnToggleVoucher);

        // Khung sổ xuống chứa các thẻ Voucher
        JPanel pnlKhuyenMaiCollapse = new JPanel();
        pnlKhuyenMaiCollapse.setLayout(new BoxLayout(pnlKhuyenMaiCollapse, BoxLayout.Y_AXIS));
        pnlKhuyenMaiCollapse.setVisible(false); // Ẩn mặc định
        pnlKhuyenMaiCollapse.setBackground(Color.decode("#F9F9F9"));
        pnlKhuyenMaiCollapse.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Thêm các mã vào danh sách
        pnlKhuyenMaiCollapse.add(taoTheVoucher("VOUCHER50K", "Giảm 50.000đ cho tổng đơn hàng", 50000, btnToggleVoucher, pnlKhuyenMaiCollapse));
        pnlKhuyenMaiCollapse.add(Box.createVerticalStrut(5));
        pnlKhuyenMaiCollapse.add(taoTheVoucher("FREESHIP", "Miễn phí vận chuyển (Giảm 30K)", 30000, btnToggleVoucher, pnlKhuyenMaiCollapse));

        // Sự kiện khi bấm sẽ sổ xuống hoặc thu lại
        btnToggleVoucher.addActionListener(e -> {
            boolean isVis = pnlKhuyenMaiCollapse.isVisible();
            pnlKhuyenMaiCollapse.setVisible(!isVis);

            String currentText = btnToggleVoucher.getText();
            if (isVis) {
                btnToggleVoucher.setText(currentText.replace("▲", "▼"));
            } else {
                btnToggleVoucher.setText(currentText.replace("▼", "▲"));
            }

            wrapperVoucher.revalidate();
            wrapperVoucher.repaint();
        });

        wrapperVoucher.add(pnlTop, BorderLayout.NORTH);
        wrapperVoucher.add(pnlKhuyenMaiCollapse, BorderLayout.CENTER);

        // Tạo 1 panel bọc ngoài cùng để nó đẩy lên trên cùng của JScrollPane
        JPanel panelContainer = new JPanel(new BorderLayout());
        panelContainer.setBackground(Color.WHITE);
        panelContainer.add(wrapperVoucher, BorderLayout.NORTH);

        scrollVoucher.setViewportView(panelContainer);
        scrollVoucher.setBorder(null);
    }

    private JPanel taoTheVoucher(String maKM, String moTa, double mucGiam, JButton btnToggleVoucher, JPanel pnlCollapse) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#E0E0E0"), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        card.setBackground(Color.WHITE);

        JLabel lblInfo = new JLabel("<html><font color='#1A73E8'><b>" + maKM + "</b></font> - " + moTa + "</html>");

        JButton btnApDung = new JButton("Áp dụng");
        btnApDung.setBackground(Color.decode("#1A73E8"));
        btnApDung.setForeground(Color.WHITE);
        btnApDung.setFocusPainted(false);
        btnApDung.setBorderPainted(false);
        btnApDung.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnApDung.addActionListener(e -> {
            voucherToanDon = mucGiam;
            tinhTongTien();

            // Ẩn khung đi và đổi text nút
            pnlCollapse.setVisible(false);
            btnToggleVoucher.setText("Đã chọn Voucher: " + maKM + " ▼");

            JOptionPane.showMessageDialog(this, "Đã áp dụng mã " + maKM + " cho toàn bộ đơn hàng!");
        });

        card.add(lblInfo, BorderLayout.CENTER);
        card.add(btnApDung, BorderLayout.EAST);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        return card;
    }

    // ==============================================================
    // 2. KHU VỰC SẢN PHẨM & KHUYẾN MÃI SP (GIỮ NGUYÊN)
    // ==============================================================
    private JPanel taoTheSanPham(ProductItem item) {
        JPanel cardWrapper = new JPanel(new BorderLayout());
        cardWrapper.setBackground(Color.WHITE);
        cardWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#E0E0E0"), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        cardWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        JPanel mainContent = new JPanel(new BorderLayout(15, 0));
        mainContent.setOpaque(false);

        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlLeft.setOpaque(false);
        pnlLeft.add(new JCheckBox());

        JLabel lblAnh = new JLabel("ẢNH");
        lblAnh.setPreferredSize(new Dimension(80, 80));
        lblAnh.setOpaque(true);
        lblAnh.setBackground(Color.decode("#F5F5F5"));
        lblAnh.setHorizontalAlignment(SwingConstants.CENTER);
        lblAnh.setBorder(BorderFactory.createLineBorder(Color.decode("#EEEEEE")));
        pnlLeft.add(lblAnh);

        JPanel pnlInfo = new JPanel();
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
        pnlInfo.setOpaque(false);
        pnlInfo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel lblMa = new JLabel("Mã SP: " + item.ma);
        lblMa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMa.setForeground(Color.decode("#888888"));

        JLabel lblTen = new JLabel(item.ten);
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel lblGiaGoc = new JLabel();
        lblGiaGoc.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblGiaSale = new JLabel();
        lblGiaSale.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblGiaSale.setForeground(Color.decode("#EE4D2D"));

        if (item.giaSale < item.gia) {
            lblGiaGoc.setText("<html><font color='#999999'>Giá gốc: <strike>" + String.format("%,.0fđ", item.gia) + "</strike></font></html>");
            lblGiaSale.setText("Sale: " + String.format("%,.0fđ", item.giaSale));
        } else {
            lblGiaGoc.setText("<html><font color='#555555'>Đơn giá: " + String.format("%,.0fđ", item.gia) + "</font></html>");
            lblGiaSale.setText(" ");
        }

        pnlInfo.add(lblMa);
        pnlInfo.add(Box.createVerticalStrut(5));
        pnlInfo.add(lblTen);
        pnlInfo.add(Box.createVerticalStrut(5));
        pnlInfo.add(lblGiaGoc);
        pnlInfo.add(lblGiaSale);

        JPanel pnlRight = new JPanel(new BorderLayout());
        pnlRight.setOpaque(false);

        JPanel pnlRightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlRightTop.setOpaque(false);

        JSpinner spinSL = new JSpinner(new SpinnerNumberModel(item.soLuong, 1, 100, 1));
        spinSL.setPreferredSize(new Dimension(50, 25));

        JButton btnXoa = new JButton("Xóa");
        btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnXoa.setForeground(Color.decode("#D32F2F"));
        btnXoa.setContentAreaFilled(false);
        btnXoa.setBorderPainted(false);
        btnXoa.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnXoa.addActionListener(e -> {
            GioHangManager.danhSachGioHang.remove(item);
            renderGioHang();
        });

        pnlRightTop.add(spinSL);
        pnlRightTop.add(btnXoa);

        JPanel pnlRightBottom = new JPanel();
        pnlRightBottom.setLayout(new BoxLayout(pnlRightBottom, BoxLayout.Y_AXIS));
        pnlRightBottom.setOpaque(false);

        JLabel lblThanhTienMoiMon = new JLabel("Cộng: " + String.format("%,.0fđ", item.giaSale * item.soLuong));
        lblThanhTienMoiMon.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblThanhTienMoiMon.setAlignmentX(Component.RIGHT_ALIGNMENT);

        spinSL.addChangeListener(e -> {
            item.soLuong = (int) spinSL.getValue();
            lblThanhTienMoiMon.setText("Cộng: " + String.format("%,.0fđ", item.giaSale * item.soLuong));
            tinhTongTien();
        });

        JButton btnKhuyenMaiSP = new JButton("Khuyến mãi SP ▼");
        btnKhuyenMaiSP.setForeground(Color.decode("#28A745"));
        btnKhuyenMaiSP.setContentAreaFilled(false);
        btnKhuyenMaiSP.setBorderPainted(false);
        btnKhuyenMaiSP.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnKhuyenMaiSP.setAlignmentX(Component.RIGHT_ALIGNMENT);

        pnlRightBottom.add(lblThanhTienMoiMon);
        pnlRightBottom.add(btnKhuyenMaiSP);

        pnlRight.add(pnlRightTop, BorderLayout.NORTH);
        pnlRight.add(pnlRightBottom, BorderLayout.SOUTH);

        mainContent.add(pnlLeft, BorderLayout.WEST);
        mainContent.add(pnlInfo, BorderLayout.CENTER);
        mainContent.add(pnlRight, BorderLayout.EAST);

        JPanel pnlKhuyenMaiCollapse = new JPanel();
        pnlKhuyenMaiCollapse.setLayout(new BoxLayout(pnlKhuyenMaiCollapse, BoxLayout.Y_AXIS));
        pnlKhuyenMaiCollapse.setVisible(false);
        pnlKhuyenMaiCollapse.setBackground(Color.decode("#F9F9F9"));
        pnlKhuyenMaiCollapse.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        pnlKhuyenMaiCollapse.add(taoTheKhuyenMaiSP("GIAM10%", "Giảm 10% giá SP", 10, true, item, lblGiaGoc, lblGiaSale, lblThanhTienMoiMon, pnlKhuyenMaiCollapse, btnKhuyenMaiSP));
        pnlKhuyenMaiCollapse.add(Box.createVerticalStrut(5));
        pnlKhuyenMaiCollapse.add(taoTheKhuyenMaiSP("TRU20K", "Giảm thẳng 20.000đ", 20000, false, item, lblGiaGoc, lblGiaSale, lblThanhTienMoiMon, pnlKhuyenMaiCollapse, btnKhuyenMaiSP));

        btnKhuyenMaiSP.addActionListener(e -> {
            boolean isVis = pnlKhuyenMaiCollapse.isVisible();
            pnlKhuyenMaiCollapse.setVisible(!isVis);
            btnKhuyenMaiSP.setText(isVis ? "Khuyến mãi SP ▼" : "Khuyến mãi SP ▲");

            pnlDanhSachSP.revalidate();
            pnlDanhSachSP.repaint();
        });

        cardWrapper.add(mainContent, BorderLayout.CENTER);
        cardWrapper.add(pnlKhuyenMaiCollapse, BorderLayout.SOUTH);

        return cardWrapper;
    }

    private JPanel taoTheKhuyenMaiSP(String maKM, String moTa, double mucGiam, boolean isPhanTram,
                                     ProductItem item, JLabel lblGiaGoc, JLabel lblGiaSale,
                                     JLabel lblThanhTienMoiMon, JPanel pnlCollapse, JButton btnToggle) {

        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#E0E0E0"), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        card.setBackground(Color.WHITE);

        JLabel lblInfo = new JLabel("<html><font color='#28A745'><b>" + maKM + "</b></font> - " + moTa + "</html>");

        JButton btnApDung = new JButton("Áp dụng");
        btnApDung.setBackground(Color.decode("#28A745"));
        btnApDung.setForeground(Color.WHITE);
        btnApDung.setFocusPainted(false);
        btnApDung.setBorderPainted(false);
        btnApDung.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnApDung.addActionListener(e -> {
            double tienGiam = isPhanTram ? (item.gia * mucGiam / 100) : mucGiam;
            item.giaSale = item.gia - tienGiam;
            if(item.giaSale < 0) item.giaSale = 0;

            lblGiaSale.setText("Sale: " + String.format("%,.0fđ", item.giaSale));
            lblGiaGoc.setText("<html><font color='#999999'>Giá gốc: <strike>" + String.format("%,.0fđ", item.gia) + "</strike></font></html>");
            lblThanhTienMoiMon.setText("Cộng: " + String.format("%,.0fđ", item.giaSale * item.soLuong));

            tinhTongTien();

            pnlCollapse.setVisible(false);
            btnToggle.setText("Khuyến mãi SP ▼");

            JOptionPane.showMessageDialog(this, "Đã áp dụng mã " + maKM + " cho SP: " + item.ten);
        });

        card.add(lblInfo, BorderLayout.CENTER);
        card.add(btnApDung, BorderLayout.EAST);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        return card;
    }
}