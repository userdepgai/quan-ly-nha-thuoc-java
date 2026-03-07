package gui;

import bus.TaiKhoan_BUS;

import javax.swing.*;
import java.awt.*;

public class DoiMatKhau_GUI extends JPanel {

    private JPasswordField txtMatKhauHienTai, txtMatKhauMoi, txtXacNhan;
    private JButton btnDoiMatKhau, btnLuu, btnHuy;
    private JCheckBox chkShowPassword;

    private boolean isEditing = false;

    public DoiMatKhau_GUI() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===== HEADER =====
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));
        headerPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("ĐỔI MẬT KHẨU");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(25,118,210));

        JLabel lblDesc = new JLabel("Thay đổi mật khẩu của bạn");
        lblDesc.setForeground(Color.GRAY);

        headerPanel.add(lblTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0,10)));
        headerPanel.add(lblDesc);

        add(headerPanel, BorderLayout.NORTH);

        // ===== FORM =====
        JPanel panelForm = new JPanel();
        panelForm.setLayout(new BoxLayout(panelForm, BoxLayout.Y_AXIS));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10,40,20,40));
        panelForm.setBackground(Color.WHITE);

        panelForm.add(createField("Mật khẩu hiện tại",
                txtMatKhauHienTai = new JPasswordField()));

        panelForm.add(createField("Mật khẩu mới",
                txtMatKhauMoi = new JPasswordField()));

        panelForm.add(createField("Xác nhận mật khẩu",
                txtXacNhan = new JPasswordField()));

        // checkbox show password
        chkShowPassword = new JCheckBox("Hiện mật khẩu");
        chkShowPassword.setBackground(Color.WHITE);

        chkShowPassword.addActionListener(e -> togglePassword());

        panelForm.add(chkShowPassword);

        add(panelForm, BorderLayout.CENTER);

        // ===== BUTTON =====
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.setBorder(BorderFactory.createEmptyBorder(10,10,10,30));
        panelButton.setBackground(Color.WHITE);

        btnDoiMatKhau = new JButton("Đổi mật khẩu");
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");

        panelButton.add(btnDoiMatKhau);
        panelButton.add(btnLuu);
        panelButton.add(btnHuy);

        add(panelButton, BorderLayout.SOUTH);

        // ===== ACTION =====
        btnDoiMatKhau.addActionListener(e -> setEditMode());
        btnLuu.addActionListener(e -> xuLyDoiMatKhau());
        btnHuy.addActionListener(e -> xuLyHuy());

        // trạng thái ban đầu
        setViewMode();
    }

    private JPanel createField(String label, JComponent component) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE,60));

        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(160,30));

        component.setPreferredSize(new Dimension(300,30));

        panel.add(lbl, BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
        panel.add(Box.createRigidArea(new Dimension(0,10)), BorderLayout.SOUTH);

        return panel;
    }

    // =========================
    // TRẠNG THÁI VIEW
    // =========================

    private void setViewMode() {

        isEditing = false;

        txtMatKhauHienTai.setEditable(false);
        txtMatKhauMoi.setEditable(false);
        txtXacNhan.setEditable(false);

        btnLuu.setVisible(false);
        btnHuy.setVisible(false);
        btnDoiMatKhau.setVisible(true);
    }

    // =========================
    // TRẠNG THÁI EDIT
    // =========================

    private void setEditMode() {

        isEditing = true;

        txtMatKhauHienTai.setEditable(true);
        txtMatKhauMoi.setEditable(true);
        txtXacNhan.setEditable(true);

        btnLuu.setVisible(true);
        btnHuy.setVisible(true);
        btnDoiMatKhau.setVisible(false);
    }

    // =========================
    // XỬ LÝ LƯU
    // =========================

    private void xuLyDoiMatKhau() {

        String mkHienTai = new String(txtMatKhauHienTai.getPassword());
        String mkMoi = new String(txtMatKhauMoi.getPassword());
        String xacNhan = new String(txtXacNhan.getPassword());

        if (mkHienTai.isEmpty() || mkMoi.isEmpty() || xacNhan.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Vui lòng nhập đầy đủ thông tin");
            return;
        }

        if (!mkMoi.equals(xacNhan)) {
            JOptionPane.showMessageDialog(this,"Xác nhận mật khẩu không khớp");
            return;
        }

        boolean result = TaiKhoan_BUS.getInstance().doiMatKhau(mkHienTai,mkMoi);

        if(result){
            JOptionPane.showMessageDialog(this,"Đổi mật khẩu thành công");
            clearForm();
            setViewMode();
        }else{
            JOptionPane.showMessageDialog(this,"Mật khẩu hiện tại không đúng");
        }
    }

    // =========================
    // HỦY
    // =========================

    private void xuLyHuy() {
        clearForm();
        setViewMode();
    }

    // =========================
    // SHOW PASSWORD
    // =========================

    private void togglePassword() {

        if(chkShowPassword.isSelected()){

            txtMatKhauHienTai.setEchoChar((char)0);
            txtMatKhauMoi.setEchoChar((char)0);
            txtXacNhan.setEchoChar((char)0);

        }else{

            txtMatKhauHienTai.setEchoChar('•');
            txtMatKhauMoi.setEchoChar('•');
            txtXacNhan.setEchoChar('•');
        }
    }

    // =========================
    // CLEAR
    // =========================

    private void clearForm(){

        txtMatKhauHienTai.setText("");
        txtMatKhauMoi.setText("");
        txtXacNhan.setText("");
    }
}