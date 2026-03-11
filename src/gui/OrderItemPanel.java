package gui;

import bus.HoaDonBan_BUS;
import dto.ChiTietHoaDonBan_DTO;
import dto.HoaDonOnline_DTO;
import dto.HoaDonBan_DTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OrderItemPanel extends JPanel {
    private final DecimalFormat df = new DecimalFormat("#,### VNĐ");
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public OrderItemPanel(HoaDonOnline_DTO hd) {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(225, 225, 225), 1),
                new EmptyBorder(10, 15, 10, 15)
        ));

        this.setMaximumSize(new Dimension(800, 160));
        this.setPreferredSize(new Dimension(500, 160));

        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setOpaque(false);

        JLabel lblMa = new JLabel("Đơn hàng: " + hd.getMa());
        lblMa.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMa.setForeground(new Color(51, 51, 51));

        JLabel lblStatus = new JLabel(hd.getTrangThaiText());
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblStatus.setForeground(getStatusColor(hd.getTrangThai()));

        pnlTop.add(lblMa, BorderLayout.WEST);
        pnlTop.add(lblStatus, BorderLayout.EAST);

        JPanel pnlMid = new JPanel();
        pnlMid.setLayout(new BoxLayout(pnlMid, BoxLayout.Y_AXIS));
        pnlMid.setOpaque(false);
        pnlMid.setBorder(new EmptyBorder(8, 0, 8, 0));

        JLabel lblNgay = new JLabel("Ngày đặt: " + hd.getNgayLap().format(dtf));
        lblNgay.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblShip = new JLabel("Phí vận chuyển: " + df.format(hd.getPhiVanChuyen()));
        lblShip.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblShip.setForeground(Color.GRAY);

        JLabel lblTotal = new JLabel("Tổng thanh toán: " + df.format(hd.getThanhTien()));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTotal.setForeground(new Color(220, 53, 69));

        pnlMid.add(lblNgay);
        pnlMid.add(Box.createVerticalStrut(5));
        pnlMid.add(lblShip);
        pnlMid.add(Box.createVerticalStrut(10));
        pnlMid.add(lblTotal);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlBottom.setOpaque(false);

        JButton btnChiTiet = new JButton("Xem chi tiết sản phẩm");
        styleDetailButton(btnChiTiet);
        btnChiTiet.addActionListener(e -> showOrderDetails(hd));

        pnlBottom.add(btnChiTiet);

        this.add(pnlTop, BorderLayout.NORTH);
        this.add(pnlMid, BorderLayout.CENTER);
        this.add(pnlBottom, BorderLayout.SOUTH);
    }

    private void styleDetailButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    private void showOrderDetails(HoaDonOnline_DTO hd) {
        ArrayList<dto.ChiTietHoaDonBan_DTO> listCT = HoaDonBan_BUS.getInstance().getChiTietHoaDon(hd.getMa());

        StringBuilder sb = new StringBuilder();
        sb.append("DANH SÁCH SẢN PHẨM TRONG ĐƠN ").append(hd.getMa()).append(":\n\n");

        for (dto.ChiTietHoaDonBan_DTO ct : listCT) {
            String tenSP = HoaDonBan_BUS.getInstance().getTenSP(ct.getMaSP());
            sb.append(String.format(" • %s\n", tenSP));
            sb.append(String.format("   Số lượng: %d  |  Đơn giá: %s\n",
                    ct.getSoLuong(), df.format(ct.getGiaBanSauApKM())));
            sb.append("   ----------------------------\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(450, 350));

        JOptionPane.showMessageDialog(this, scroll, "Chi tiết đơn hàng", JOptionPane.INFORMATION_MESSAGE);
    }

    private Color getStatusColor(int status) {
        return switch (status) {
            case HoaDonBan_DTO.TT_CHO_DUYET -> new Color(255, 152, 0);
            case HoaDonBan_DTO.TT_HOAN_THANH -> new Color(46, 125, 50);
            case HoaDonBan_DTO.TT_DA_HUY -> new Color(211, 47, 47);
            case HoaDonBan_DTO.TT_DANG_GIAO -> new Color(2, 136, 209);
            default -> Color.BLACK;
        };
    }
}