package gui;

import javax.swing.*;
import java.awt.*;

public class NhanVien_GUI extends JPanel {
    private JPanel panel_nhanVien;
    private JButton btnXuatExcel;
    private JButton btnNhapExcel;
    private JTextField textField1;
    private JButton btnTimKiem;
    private JButton btnThoat;
    private JComboBox comboBox1;
    private JTable table1;
    private JPanel panelThongTin;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JButton cậpNhậtButton;
    private JButton thêmButton;
    private JTextField textField8;
    private JTextField textField9;
    private JRadioButton nữRadioButton;
    private JRadioButton namRadioButton;
    private JTextField textField4;
    private JTextField textField10;
    private JTextField textField2;
    private JTextField textField3;
    private JButton chứcVụButton;
    private JButton trạngTháiButton;

    public NhanVien_GUI() {
        this.setLayout(new BorderLayout());
        this.add(panel_nhanVien, BorderLayout.CENTER);
        textField4.setBorder(null);
    }
}