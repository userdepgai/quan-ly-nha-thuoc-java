package gui;

import bus.HoaDonBan_BUS;
import bus.HoaDonOnline_BUS;
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

    private LichSuDonHang_GUI parentGUI;

    public OrderItemPanel(HoaDonOnline_DTO hd, LichSuDonHang_GUI parentGUI) {
        this.parentGUI = parentGUI;

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

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBottom.setOpaque(false);

        JButton btnChiTiet = new JButton("Xem chi tiết");
        styleButton(btnChiTiet, Color.WHITE, Color.BLACK, new Color(200, 200, 200));
        btnChiTiet.addActionListener(e -> showOrderDetails(hd));
        pnlBottom.add(btnChiTiet);

        int status = hd.getTrangThai();

        if (status == HoaDonBan_DTO.TT_CHO_DUYET) {
            JButton btnHuy = new JButton("Hủy đơn hàng");
            styleButton(btnHuy, Color.WHITE, new Color(220, 53, 69), new Color(220, 53, 69));
            btnHuy.addActionListener(e -> xuLyHuyDon(hd.getMa()));
            pnlBottom.add(btnHuy);

        } else if (status == HoaDonBan_DTO.TT_HOAN_THANH) {
            JButton btnHoan = new JButton("Yêu cầu hoàn hàng");
            styleButton(btnHoan, Color.WHITE, new Color(255, 140, 0), new Color(255, 140, 0));
            btnHoan.addActionListener(e -> xuLyHoanHang(hd.getMa()));
            pnlBottom.add(btnHoan);
        }

        this.add(pnlTop, BorderLayout.NORTH);
        this.add(pnlMid, BorderLayout.CENTER);
        this.add(pnlBottom, BorderLayout.SOUTH);
    }

    private void xuLyHuyDon(String maHD) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn hủy đơn hàng " + maHD + " không?",
                "Xác nhận hủy", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                HoaDonOnline_BUS.getInstance().khachHuyDon(maHD);
                JOptionPane.showMessageDialog(this, "Hủy đơn hàng thành công!");
                parentGUI.loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void xuLyHoanHang(String maHD) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn muốn gửi yêu cầu hoàn hàng cho đơn " + maHD + "?",
                "Xác nhận hoàn hàng", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                HoaDonOnline_BUS.getInstance().hoanHang(maHD);
                JOptionPane.showMessageDialog(this, "Đã gửi yêu cầu hoàn hàng thành công!");
                parentGUI.loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void styleButton(JButton btn, Color bg, Color fg, Color border) {
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(border, 1, true),
                new EmptyBorder(5, 12, 5, 12)
        ));
    }

    private void showOrderDetails(HoaDonOnline_DTO hd) {
        ArrayList<ChiTietHoaDonBan_DTO> listCT = HoaDonBan_BUS.getInstance().getChiTietHoaDon(hd.getMa());

        StringBuilder sb = new StringBuilder();
        sb.append("DANH SÁCH SẢN PHẨM TRONG ĐƠN ").append(hd.getMa()).append(":\n\n");

        for (ChiTietHoaDonBan_DTO ct : listCT) {
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
            case HoaDonBan_DTO.TT_YEU_CAU_HOAN -> new Color(156, 39, 176);
            default -> Color.BLACK;
        };
    }
}