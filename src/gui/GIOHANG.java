package gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;

public class GIOHANG {

    private JPanel ItemPanel;
    private JPanel TopPanel;
    private JPanel promoPanel;
    private JTextField textMa;
    private JTextField textTen;
    private JTextField textGGoc;
    private JButton lblArrow;
    private JPanel MainPanel;
    private JCheckBox tấtCảCheckBox;
    private JCheckBox checkBox1;
    private JSpinner spinner1;
    private JTextField textField1;
    private JScrollPane scrollKhuyenMai;

    public GIOHANG() {
        promoPanel.setVisible(false);
        // Làm đẹp nút bấm
        lblArrow.setFocusPainted(false);
        lblArrow.setBorderPainted(false);
        lblArrow.setContentAreaFilled(false);
        lblArrow.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblArrow.setFont(new Font("Arial", Font.BOLD, 16));

        lblArrow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblArrow.setContentAreaFilled(true);
                lblArrow.setBackground(new Color(230, 230, 230));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblArrow.setContentAreaFilled(false);
            }
        });

        // Xử lý khi bấm mũi tên
        lblArrow.addActionListener(e -> {
            boolean isVisible = promoPanel.isVisible();

            // Đảo trạng thái hiển thị
            promoPanel.setVisible(!isVisible);

            // Đổi text mũi tên và LOAD DỮ LIỆU
            if (!isVisible) {
                lblArrow.setText("▲");
                loadDanhSachKhuyenMai(); // GỌI HÀM LẤY KHUYẾN MÃI RA ĐÂY
            } else {
                lblArrow.setText("▼");
            }

            // Cập nhật lại giao diện panel chính
            MainPanel.revalidate();
            MainPanel.repaint();



        });
    }

    // ==============================================================
    // 1. HÀM TẠO GIAO DIỆN CHO 1 CHIẾC THẺ VOUCHER
    // ==============================================================
    private JPanel taoTheKhuyenMai(String maKM, String moTa, double donToiThieu) {
        // Tạo thẻ chính
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);

        // Khối text bên trái
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

        // Khối nút bấm bên phải
        JPanel pnlButton = new JPanel(new BorderLayout());
        pnlButton.setBackground(Color.WHITE);
        JButton btnApDung = new JButton("ÁP DỤNG");
        btnApDung.setBackground(new Color(40, 167, 69));
        btnApDung.setForeground(Color.WHITE);
        btnApDung.setFocusPainted(false);
        pnlButton.add(btnApDung, BorderLayout.CENTER);

        // Xử lý khi bấm nút ÁP DỤNG trên thẻ
        btnApDung.addActionListener(e -> {
            JOptionPane.showMessageDialog(MainPanel, "Bạn đã áp dụng mã: " + maKM + "\n" + moTa);

            // Áp dụng xong thì tự động đóng khung cuộn lại
            promoPanel.setVisible(false);
            lblArrow.setText("▼");

            // Ép form co lại cho gọn
            Window window = SwingUtilities.getWindowAncestor(MainPanel);
            if (window != null) {
                window.pack();
            }
        });

        card.add(pnlText, BorderLayout.CENTER);
        card.add(pnlButton, BorderLayout.EAST);

        // Giới hạn chiều cao để thẻ không bị dãn xấu xí
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        return card;
    }

    // ==============================================================
    // 2. HÀM ĐỔ DỮ LIỆU MẪU VÀO THANH CUỘN (SCROLL PANE)
    // ==============================================================
    private void loadDanhSachKhuyenMai() {
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        // --- BẮT ĐẦU THÊM DỮ LIỆU MẪU ---
        listPanel.add(taoTheKhuyenMai("GIAM10K", "Giảm thẳng 10.000đ vào tổng hóa đơn", 100000));
        listPanel.add(Box.createVerticalStrut(10));

        listPanel.add(taoTheKhuyenMai("SALE20", "Giảm 20% tối đa 50K cho khách mới", 250000));
        listPanel.add(Box.createVerticalStrut(10));

        listPanel.add(taoTheKhuyenMai("FREESHIP", "Miễn phí vận chuyển toàn quốc", 300000));
        listPanel.add(Box.createVerticalStrut(10));

        listPanel.add(taoTheKhuyenMai("VIP100", "Giảm 100K cho khách hàng thành viên VIP", 1000000));
        listPanel.add(Box.createVerticalStrut(10));

        listPanel.add(taoTheKhuyenMai("VALENTINE", "Giảm 14% nhân dịp Lễ tình nhân", 500000));
        // --- KẾT THÚC DỮ LIỆU MẪU ---

        // Nhét danh sách dọc này vào JScrollPane
        scrollKhuyenMai.setViewportView(listPanel);

        // Tăng tốc độ cuộn chuột cho mượt hơn
        scrollKhuyenMai.getVerticalScrollBar().setUnitIncrement(16);


        scrollKhuyenMai.revalidate();
        scrollKhuyenMai.repaint();
        promoPanel.revalidate();
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }

}