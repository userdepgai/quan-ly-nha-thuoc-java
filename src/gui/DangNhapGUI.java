package gui;
import bus.TaiKhoan_BUS;
import dto.TaiKhoan_DTO;
import gui.menu.Menu;
import gui.menu.MenuKhachHang_GUI;
import gui.menu.*;
import utils.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

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

        btnDangNhap.addActionListener(e -> xuLyDangNhap());
    }

    private void xuLyDangNhap() {
        String sdt = txtSDT.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword()).trim();

        if (!kiemTraDuLieuNhap(sdt, matKhau)) return;

        TaiKhoan_BUS bus = TaiKhoan_BUS.getInstance();
        ArrayList<TaiKhoan_DTO> list = bus.dangNhap(sdt, matKhau);
        if (!kiemTraKetQuaDangNhap(list)) return;

        TaiKhoan_DTO taiKhoanDuocChon = chonTaiKhoanNeuCoNhieuQuyen(list);

        if (taiKhoanDuocChon != null) {
            Session.setCurrentUser(taiKhoanDuocChon);
            moGiaoDienTheoQuyen(taiKhoanDuocChon);
        }
    }

    private boolean kiemTraDuLieuNhap(String sdt, String matKhau) {
        if (sdt.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
            return false;
        }
        if (!kiemTraSoDienThoai(sdt)) {
            JOptionPane.showMessageDialog(this,
                    "Số điện thoại phải gồm 10 số và bắt đầu bằng 0");
            return false;
        }

        return true;
    }
    private boolean kiemTraKetQuaDangNhap(ArrayList<TaiKhoan_DTO> list) {
        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Sai số điện thoại hoặc mật khẩu");
            return false;
        }
        return true;
    }

    private TaiKhoan_DTO chonTaiKhoanNeuCoNhieuQuyen(ArrayList<TaiKhoan_DTO> list) {
        if (list.size() == 1) {
            return list.get(0);
        }

        TaiKhoan_DTO tkKhach = null;
        TaiKhoan_DTO tkNhanVien = null;

        for (TaiKhoan_DTO tk : list) {
            if (tk.getMaQuyen().equalsIgnoreCase("Q001")) {
                tkKhach = tk;
            } else {
                tkNhanVien = tk;
            }
        }

        String[] options = {"Đăng nhập Khách hàng", "Đăng nhập Nhân viên"};

        int choice = JOptionPane.showOptionDialog(
                this,
                "Bạn muốn đăng nhập với quyền nào?",
                "Chọn quyền",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        if (choice == 0) return tkKhach;
        if (choice == 1) return tkNhanVien;

        return null;
    }
    private void moGiaoDienTheoQuyen(TaiKhoan_DTO tk) {

        if (tk.getMaQuyen().equalsIgnoreCase("Q001")) {
            new MenuKhachHang_GUI().setVisible(true);
        } else {
            new Menu().setVisible(true);
        }

        dispose();
    }
    public boolean kiemTraSoDienThoai(String sdt) {
        return sdt != null && sdt.matches("^0\\d{9}$");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DangNhapGUI().setVisible(true);
        });
    }
}