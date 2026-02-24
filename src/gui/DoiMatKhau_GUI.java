package gui;

import gui.menu.Menu;
import gui.menu.MenuThongTinCaNhan_GUI;

import javax.swing.*;
import java.awt.*;

public class DoiMatKhau_GUI extends JPanel {

    private JPasswordField txtMatKhauHienTai, txtMatKhauMoi, txtXacNhan;
    private JButton btnDoiMatKhau, btnHuy;

    public DoiMatKhau_GUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));
        headerPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("ĐỔI MẬT KHẨU");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(25, 118, 210));

        JLabel lblDesc = new JLabel("Thay đổi mật khẩu của bạn");
        lblDesc.setForeground(Color.GRAY);

        headerPanel.add(lblTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(lblDesc);

        add(headerPanel, BorderLayout.NORTH);

        JPanel panelForm = new JPanel();
        panelForm.setLayout(new BoxLayout(panelForm, BoxLayout.Y_AXIS));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));
        panelForm.setBackground(Color.WHITE);

        panelForm.add(createField("Mật khẩu hiện tại",
                txtMatKhauHienTai = new JPasswordField()));

        panelForm.add(createField("Mật khẩu mới",
                txtMatKhauMoi = new JPasswordField()));

        panelForm.add(createField("Xác nhận mật khẩu mới",
                txtXacNhan = new JPasswordField()));

        add(panelForm, BorderLayout.CENTER);

        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 30));
        panelButton.setBackground(Color.WHITE);

        btnDoiMatKhau = new JButton("Đổi mật khẩu");
        btnHuy = new JButton("Hủy");

        panelButton.add(btnDoiMatKhau);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuThongTinCaNhan_GUI().setVisible(true));
    }
}