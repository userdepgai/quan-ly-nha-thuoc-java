package gui;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;

public class PhieuNhap_GUI extends JPanel{
    private JButton button1;
    private JButton button2;
    private JTextField textField1;
    private JTextField textField2;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JComboBox comboBox5;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JTextField textField3;
    private JComboBox comboBox7;
    private JComboBox comboBox6;
    private JComboBox comboBox8;
    private JComboBox comboBox9;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTable table1;
    private JPanel panel_phieuNhap;
    private JPanel panel_thongTinPhieu;
    private JTable table2;
    private JButton button7;
    private JButton button8;
    private JPanel panel_boLoc;
    private JDateChooser JDateChooser1;
    private JDateChooser JDateChooser2;
    private JPanel date_bdNgayLap;
    private JPanel date_ktNgayLap;
    private JDateChooser JDateChooser3;
    private JDateChooser JDateChooser4;

    public PhieuNhap_GUI() {
        this.setLayout(new BorderLayout());
        this.add(panel_phieuNhap, BorderLayout.CENTER);

    }
    private void createUIComponents() {
        JDateChooser1 = new com.toedter.calendar.JDateChooser();
        JDateChooser1.setDateFormatString("dd/MM/yyyy");

        JDateChooser2 = new com.toedter.calendar.JDateChooser();
        JDateChooser2.setDateFormatString("dd/MM/yyyy");

        JDateChooser3 = new com.toedter.calendar.JDateChooser();
        JDateChooser3.setDateFormatString("dd/MM/yyyy");

        JDateChooser4 = new com.toedter.calendar.JDateChooser();
        JDateChooser4.setDateFormatString("dd/MM/yyyy");
    }
}
