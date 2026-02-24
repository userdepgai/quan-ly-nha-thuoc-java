package gui;

import javax.swing.*;
import java.awt.*;

public class ThongTinCaNhanNhanVien_GUI extends JPanel {

    private JTextField txtHoTen, txtSDT, txtNamSinh, txtNgayVaoLam, txtLuong, txtDiaChi;
    private JRadioButton rdoNam, rdoNu;
    private JComboBox<String> cboChucVu;
    private JButton btnLuu, btnHuy, btnCapNhat;

    public ThongTinCaNhanNhanVien_GUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));
        headerPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("THÔNG TIN CÁ NHÂN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(25, 118, 210));

        btnCapNhat = new JButton("Cập nhật thông tin");
        btnCapNhat.setBackground(new Color(25, 118, 210));
        btnCapNhat.setForeground(Color.WHITE);
        btnCapNhat.setFocusPainted(false);

        JLabel lblDesc = new JLabel("Bạn có thể chỉnh thông tin cá nhân của mình");
        lblDesc.setForeground(Color.GRAY);

        headerPanel.add(lblTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(btnCapNhat);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(lblDesc);

        add(headerPanel, BorderLayout.NORTH);

        JPanel panelForm = new JPanel();
        panelForm.setLayout(new BoxLayout(panelForm, BoxLayout.Y_AXIS));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));
        panelForm.setBackground(Color.WHITE);

        panelForm.add(createField("Họ tên", txtHoTen = new JTextField()));
        panelForm.add(createField("Số điện thoại", txtSDT = new JTextField()));
        panelForm.add(createField("Năm sinh", txtNamSinh = new JTextField()));

        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setBackground(Color.WHITE);
        genderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel lblGender = new JLabel("Giới tính");
        lblGender.setPreferredSize(new Dimension(120, 30));

        rdoNam = new JRadioButton("Nam");
        rdoNu = new JRadioButton("Nữ");
        rdoNam.setBackground(Color.WHITE);
        rdoNu.setBackground(Color.WHITE);

        ButtonGroup group = new ButtonGroup();
        group.add(rdoNam);
        group.add(rdoNu);

        genderPanel.add(lblGender);
        genderPanel.add(rdoNam);
        genderPanel.add(rdoNu);

        panelForm.add(genderPanel);
        panelForm.add(Box.createRigidArea(new Dimension(0, 10)));

        cboChucVu = new JComboBox<>(new String[]{"Nhân viên", "Quản lý", "Admin"});
        panelForm.add(createField("Chức vụ", cboChucVu));

        panelForm.add(createField("Ngày vào làm", txtNgayVaoLam = new JTextField()));
        panelForm.add(createField("Lương", txtLuong = new JTextField()));
        panelForm.add(createField("Địa chỉ", txtDiaChi = new JTextField()));

        add(panelForm, BorderLayout.CENTER);

        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 30));
        panelButton.setBackground(Color.WHITE);

        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");

        panelButton.add(btnLuu);
        panelButton.add(btnHuy);

        add(panelButton, BorderLayout.SOUTH);
    }

    private JPanel createField(String label, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(120, 30));

        component.setPreferredSize(new Dimension(300, 30));

        panel.add(lbl, BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
        panel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.SOUTH);

        return panel;
    }

}
