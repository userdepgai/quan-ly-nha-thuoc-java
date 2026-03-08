package gui;

import dto.SanPham_DTO;
import javax.swing.*;
import java.awt.*;

public class SanPhamCard_GUI extends JPanel {

    private JPanel panel_card;
    private JButton btn_themGioHang;
    private JLabel lblHinhAnh;
    private JTextField txt_maSanPham;
    private JTextField txt_tenSanPham;
    private JTextField txt_gia;
    private JButton btn_xemThem;

    public SanPhamCard_GUI() {
        this.setLayout(new BorderLayout());
        this.add(panel_card, BorderLayout.CENTER);
    }

    public SanPhamCard_GUI(SanPham_DTO sp, double giaBan) {
        this();

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setPreferredSize(new Dimension(180, 260));
        lblHinhAnh.setPreferredSize(new Dimension(120, 120));
        lblHinhAnh.setHorizontalAlignment(SwingConstants.CENTER);
        lblHinhAnh.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        txt_maSanPham.setText(sp.getMaSP());
        txt_tenSanPham.setText(sp.getTenSP());

        // ===== tính giá bán =====
        txt_gia.setText(String.format("%,.0f VNĐ", giaBan));
        txt_maSanPham.setEditable(false);
        txt_tenSanPham.setEditable(false);
        txt_gia.setEditable(false);

        setHinhAnh(sp.getHinhAnh());
        // 1. Tạo một MouseAdapter chung để dùng cho tất cả thành phần
        java.awt.event.MouseAdapter commonClick = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Mở frame chi tiết khi click bất cứ đâu trên card
                ChiTietSanPhamCard_GUI frame = new ChiTietSanPhamCard_GUI(sp, giaBan);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.toFront();
            }
        };
        lblHinhAnh.addMouseListener(commonClick);
        panel_card.addMouseListener(commonClick);
        panel_card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblHinhAnh.setCursor(new Cursor(Cursor.HAND_CURSOR));
// 3. (Tùy chọn) Đổi con trỏ chuột thành hình bàn tay để người dùng biết là click được
        Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
        lblHinhAnh.setCursor(handCursor);
        txt_maSanPham.setCursor(handCursor);
        txt_tenSanPham.setCursor(handCursor);
        txt_gia.setCursor(handCursor);
        btn_xemThem.addActionListener(e -> {

            ChiTietSanPhamCard_GUI frame = new ChiTietSanPhamCard_GUI(sp, giaBan);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        });
    }
    private void setHinhAnh(String path) {

        if (path == null || path.isEmpty()) {
            lblHinhAnh.setText("No Image");
            return;
        }

        ImageIcon icon = new ImageIcon(path);

        Image img = icon.getImage();

        Image newImg = img.getScaledInstance(
                120,
                120,
                Image.SCALE_SMOOTH
        );

        lblHinhAnh.setIcon(new ImageIcon(newImg));
    }
}