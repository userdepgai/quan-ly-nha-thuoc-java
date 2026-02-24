package gui;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DangNhapGUI extends JFrame {

    private JTextField txtSDT;
    private JPasswordField txtMatKhau;
    private JCheckBox chkLuuTaiKhoan;
    private JCheckBox chkHienMatKhau;
    private JButton btnDangNhap;
    private JButton btnDangKy;

    public DangNhapGUI() {
        initUI();
    }

    private void initUI() {
        setTitle("Nhà Thuốc Xì Trum - Đăng nhập");
        setSize(400, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Cái Động Bán Thuốc");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon icon = new ImageIcon(getClass().getResource("/images/logo.jpg"));
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);

        JLabel lblLogo = new JLabel(scaledIcon);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDangNhap = new JLabel("Đăng nhập");
        lblDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblDangNhap.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel("Enter your phone number to sign up for this app");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(Color.GRAY);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtSDT = new JTextField();
        txtSDT.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtSDT.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSDT.setBorder(BorderFactory.createTitledBorder("Số điện thoại"));

        txtMatKhau = new JPasswordField();
        txtMatKhau.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMatKhau.setBorder(BorderFactory.createTitledBorder("Mật khẩu"));

        chkLuuTaiKhoan = new JCheckBox("Lưu tài khoản");
        chkHienMatKhau = new JCheckBox("Hiện mật khẩu");
        chkLuuTaiKhoan.setBackground(Color.WHITE);
        chkHienMatKhau.setBackground(Color.WHITE);

        JPanel panelCheck = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCheck.setBackground(Color.WHITE);
        panelCheck.add(chkLuuTaiKhoan);
        panelCheck.add(chkHienMatKhau);

        btnDangNhap = new JButton("Đăng nhập");
        btnDangNhap.setBackground(Color.BLACK);
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDangNhap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        btnDangKy = new JButton("Đăng ký");
        btnDangKy.setBackground(Color.BLACK);
        btnDangKy.setForeground(Color.WHITE);
        btnDangKy.setFocusPainted(false);
        btnDangKy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDangKy.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        panel.add(lblTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(lblLogo);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(lblDangNhap);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblDesc);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(txtSDT);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(txtMatKhau);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(panelCheck);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnDangNhap);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnDangKy);

        btnDangNhap.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDangKy.setAlignmentX(Component.CENTER_ALIGNMENT);

        xuLyXuKien();

        add(panel);
    }

    private void xuLyXuKien() {
        chkHienMatKhau.addActionListener(e ->
                txtMatKhau.setEchoChar(chkHienMatKhau.isSelected() ? (char) 0 : '•')
        );

        btnDangKy.addActionListener(e -> {
            new DangKyGUI().setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DangNhapGUI().setVisible(true);
        });
    }
}