package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.toedter.calendar.JDateChooser;

import java.time.LocalDate;
import java.util.Date;
import java.time.ZoneId;

import dto.*;
import bus.*;

public class DangKyGUI extends JFrame {
    private JButton btnTaoTK;
    private JButton btnBack;
    private JTextField txtSDT;
    private JTextField txtHoTen;
    private JDateChooser dateNgaySinh;
    private JPasswordField txtMatKhau;
    private JPasswordField txtXacNhan;
    private JRadioButton rdoNam;
    private JRadioButton rdoNu;

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

        dateNgaySinh = new JDateChooser();
        dateNgaySinh.setDateFormatString("dd/MM/yyyy");
        dateNgaySinh.setBorder(BorderFactory.createTitledBorder("Ngày sinh"));
        dateNgaySinh.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        rdoNam = new JRadioButton("Nam");
        rdoNu = new JRadioButton("Nữ");

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

        txtMatKhau = new JPasswordField();
        txtMatKhau.setBorder(BorderFactory.createTitledBorder("Mật khẩu"));
        txtMatKhau.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        txtXacNhan = new JPasswordField();
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
        panel.add(dateNgaySinh);
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
        btnTaoTK.addActionListener(e -> dangKy());
    }
    private void dangKy() {
        if (!kiemTraDuLieu()) return;
        KhachHang_BUS khBus = KhachHang_BUS.getInstance();
        TaiKhoan_BUS tkBus = TaiKhoan_BUS.getInstance();

        String sdt = txtSDT.getText().trim();
        String ten = txtHoTen.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());

        TaiKhoan_DTO tkTonTai = tkBus.getBySDT(sdt);

        if (tkTonTai != null && tkTonTai.getMaQuyen().equals("Q001")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại đã đăng ký tài khoản khách hàng");
            return;
        }

        KhachHang_DTO kh = khBus.getBysdt(sdt);
        if (kh == null) {
            Date date = dateNgaySinh.getDate();
            LocalDate ngaySinh = date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            kh = new KhachHang_DTO();
            kh.setMa(khBus.getNextId());
            kh.setTen(ten);
            kh.setSdt(sdt);
            kh.setNgaySinh(ngaySinh);
            kh.setGioiTinh(rdoNam.isSelected());
            kh.setNgayDKThanhVien(LocalDate.now());
            kh.setDiemHang(0);
            kh.setDiemThuong(0);
            kh.setHang("DONG");
            if (!khBus.kiemTraHopLe(kh))
                return;
            if (!khBus.them(kh)) {
                JOptionPane.showMessageDialog(this, "Tạo khách hàng thất bại");
                return;
            }
        }

        TaiKhoan_DTO tk = new TaiKhoan_DTO();

        tk.setMaTK(tkBus.getNextID());
        tk.setSdt(sdt);
        tk.setMatKhau(matKhau);
        tk.setMaQuyen("Q001");
        tk.setNgayKichHoat(LocalDate.now());
        tk.setTrangThai(TaiKhoan_DTO.TT_MO);
        if (!tkBus.them(tk)) {
            JOptionPane.showMessageDialog(this,"Tạo tài khoản thất bại");
            return;
        }
        GioHang_BUS ghBus = GioHang_BUS.getInstance();
        GioHang_DTO gh = new GioHang_DTO();
        gh.setMaGH(ghBus.getNextId());
        gh.setMaKH(kh.getMa());
        if(!ghBus.them(gh))
            JOptionPane.showMessageDialog(this,"Tạo giỏ hàng không thành công");

        JOptionPane.showMessageDialog(this,"Đăng ký thành công");
        new DangNhapGUI().setVisible(true);
        dispose();
    }
    private boolean kiemTraDuLieu() {
        String sdt = txtSDT.getText().trim();
        String ten = txtHoTen.getText().trim();
        Date ngaySinh = dateNgaySinh.getDate();
        String matKhau = new String(txtMatKhau.getPassword());
        String xacNhan = new String(txtXacNhan.getPassword());
        if (sdt.isEmpty() || ten.isEmpty() ||  matKhau.isEmpty() || xacNhan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
            return false;
        }
        if (!sdt.matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải 10 số và bắt đầu bằng 0");
            return false;
        }
        if (ngaySinh == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày sinh");
            return false;
        }
        if (!rdoNam.isSelected() && !rdoNu.isSelected()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn giới tính");
            return false;
        }
        if (!matKhau.equals(xacNhan)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không đúng");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DangKyGUI().setVisible(true));
    }
}
