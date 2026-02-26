package gui;

import javax.swing.*;
import java.awt.*;
import bus.NhanVien_BUS;
import dto.NhanVien_DTO;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import bus.NhanVien_BUS;
import dto.NhanVien_DTO;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
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

        System.out.println("NhanVien_GUI da duoc tao");

        loadTable();
    }
    private void loadTable() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);

        ArrayList<NhanVien_DTO> list = NhanVien_BUS.getInstance().getAll();
        System.out.println("So nhan vien: " + list.size());

        for (NhanVien_DTO nv : list) {
            model.addRow(new Object[]{
                    nv.getMa(),
                    nv.getTen(),
                    nv.getSdt(),
                    nv.getChucVu(),
                    nv.getLuongCoBan()
            });
        }
    }
}