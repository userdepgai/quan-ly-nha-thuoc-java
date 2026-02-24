package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DangKyGUI extends JFrame {
    private JButton btnTaoTK;
    private JButton btnBack;
    private JTextField txtSDT;
    private JTextField txtHoTen;
    private JTextField txtNgaySinh;

    public DangKyGUI() {
        setTitle("Đăng ký - Nhà Thuốc Xì Trum");
        setSize(400, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Đăng ký");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel("Enter your phone number to sign up for this app");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(Color.GRAY);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtSDT = createField("Số điện thoại");
        txtHoTen = createField("Họ và tên");

        txtNgaySinh = new JTextField();
        txtNgaySinh.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtNgaySinh.setBorder(BorderFactory.createTitledBorder("Ngày sinh (dd/MM/yy)"));

        JRadioButton rdoNam = new JRadioButton("Nam");
        JRadioButton rdoNu = new JRadioButton("Nữ");
        rdoNam.setBackground(Color.WHITE);
        rdoNu.setBackground(Color.WHITE);

        ButtonGroup group = new ButtonGroup();
        group.add(rdoNam);
        group.add(rdoNu);

        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        genderPanel.setBackground(Color.WHITE);
        genderPanel.add(new JLabel("Giới tính: "));
        genderPanel.add(rdoNam);
        genderPanel.add(rdoNu);

        JPasswordField txtMatKhau = new JPasswordField();
        txtMatKhau.setBorder(BorderFactory.createTitledBorder("Mật khẩu"));
        txtMatKhau.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JPasswordField txtXacNhan = new JPasswordField();
        txtXacNhan.setBorder(BorderFactory.createTitledBorder("Nhập lại mật khẩu"));
        txtXacNhan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        btnTaoTK = new JButton("Tạo tài khoản");
        btnTaoTK.setBackground(Color.BLACK);
        btnTaoTK.setForeground(Color.WHITE);
        btnTaoTK.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTaoTK.setFocusPainted(false);
        btnTaoTK.setMaximumSize(new Dimension(250, 45));
        btnTaoTK.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnBack = new JButton("Quay lại đăng nhập");
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setForeground(Color.BLUE);
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblDesc);
        panel.add(Box.createVerticalStrut(20));
        panel.add(txtSDT);
        panel.add(Box.createVerticalStrut(10));
        panel.add(txtHoTen);
        panel.add(Box.createVerticalStrut(10));
        panel.add(txtNgaySinh);
        panel.add(Box.createVerticalStrut(10));
        panel.add(genderPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(txtMatKhau);
        panel.add(Box.createVerticalStrut(10));
        panel.add(txtXacNhan);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnTaoTK);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnBack);

        xuLyXuKien();

        add(panel);
    }

    private JTextField createField(String title) {
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createTitledBorder(title));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return field;
    }
    private void xuLyXuKien() {
        btnBack.addActionListener(e -> {
            new DangNhapGUI().setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DangKyGUI().setVisible(true));
    }
}
