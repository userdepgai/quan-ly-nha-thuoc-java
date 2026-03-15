package gui;

import bus.HoaDonOnline_BUS;
import dto.HoaDonOnline_DTO;
import com.toedter.calendar.JDateChooser;
import utils.Session;
import dto.TaiKhoan_DTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class LichSuDonHang_GUI extends JPanel {
    private JDateChooser jdNgayBatDau, jdNgayKetThuc;
    private JPanel pnlDanhSachCard;
    private final HoaDonOnline_BUS hdoBus = HoaDonOnline_BUS.getInstance();

    public LichSuDonHang_GUI() {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(245, 245, 245));
        initGUI();
        loadData();
    }

    private void initGUI() {
        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel lblTitle = new JLabel("LỊCH SỬ MUA HÀNG ONLINE");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(25, 118, 210));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlFilter.setOpaque(false);
        jdNgayBatDau = new JDateChooser();
        jdNgayKetThuc = new JDateChooser();
        jdNgayBatDau.setDateFormatString("dd/MM/yyyy");
        jdNgayKetThuc.setDateFormatString("dd/MM/yyyy");
        // Mặc định 1 tháng gần đây
        jdNgayKetThuc.setDate(new java.util.Date());
        jdNgayBatDau.setDate(new java.util.Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000));

        JButton btnLoc = new JButton("Lọc đơn hàng");
        btnLoc.addActionListener(e -> loadData());

        pnlFilter.add(new JLabel("Từ:")); pnlFilter.add(jdNgayBatDau);
        pnlFilter.add(new JLabel("Đến:")); pnlFilter.add(jdNgayKetThuc);
        pnlFilter.add(btnLoc);

        pnlHeader.add(lblTitle);
        pnlHeader.add(pnlFilter);

        pnlDanhSachCard = new JPanel();
        pnlDanhSachCard.setLayout(new BoxLayout(pnlDanhSachCard, BoxLayout.Y_AXIS));
        pnlDanhSachCard.setBackground(new Color(245, 245, 245));

        JScrollPane scroll = new JScrollPane(pnlDanhSachCard);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(15);

        this.add(pnlHeader, BorderLayout.NORTH);
        this.add(scroll, BorderLayout.CENTER);
    }

    public void loadData() {
        pnlDanhSachCard.removeAll();

        TaiKhoan_DTO user = Session.getCurrentUser();
        if (user == null) return;

        LocalDateTime tuNgay = jdNgayBatDau.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime denNgay = jdNgayKetThuc.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        ArrayList<HoaDonOnline_DTO> list = hdoBus.timKiemHoaDonOnline(
                "SĐT", user.getSdt(), null, null, null, tuNgay, denNgay
        );

        if (list.isEmpty()) {
            pnlDanhSachCard.add(new JLabel("Không có đơn hàng nào."));
        } else {
            for (HoaDonOnline_DTO hd : list) {
                pnlDanhSachCard.add(Box.createVerticalStrut(10));
                OrderItemPanel card = new OrderItemPanel(hd,this);
                card.setAlignmentX(Component.CENTER_ALIGNMENT);
                pnlDanhSachCard.add(card);
            }
            pnlDanhSachCard.add(Box.createVerticalGlue());
        }

        pnlDanhSachCard.revalidate();
        pnlDanhSachCard.repaint();
    }
}