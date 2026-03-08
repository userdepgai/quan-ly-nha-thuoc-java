package gui.THONGKEBAOCAO;

import bus.ThongKe_BUS;
import dto.ThongKe_DTO;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class THONGKEDOANHTHU extends JPanel {
    private JPanel panelMain;
    private JButton btnXuat;
    private JButton btnNhap;
    private JPanel pnlNgayBatDau;
    private JPanel pnlNgayKetThuc;

    private JDateChooser jdBatDau;
    private JDateChooser jdKetThuc;

    private JComboBox<String> comboBoxDM;
    private JButton btnThoat;
    private JButton btnTimKiem;
    private JTextField textTBD;
    private JTextField textLN;
    private JTable tableThongKe;

    private DefaultTableModel tableModel;
    private ThongKe_BUS thongKeBUS;

    public THONGKEDOANHTHU()  {
        this.setLayout(new BorderLayout());
        this.add(panelMain);

        jdBatDau = new JDateChooser();
        jdBatDau.setDateFormatString("dd/MM/yyyy");

        jdKetThuc = new JDateChooser();
        jdKetThuc.setDateFormatString("dd/MM/yyyy");

        if (pnlNgayBatDau != null) {
            pnlNgayBatDau.setLayout(new BorderLayout());
            pnlNgayBatDau.add(jdBatDau, BorderLayout.CENTER);
        }
        if (pnlNgayKetThuc != null) {
            pnlNgayKetThuc.setLayout(new BorderLayout());
            pnlNgayKetThuc.add(jdKetThuc, BorderLayout.CENTER);
        }

        initTableAndFields();

        thongKeBUS = new ThongKe_BUS();

        initEvents();
        loadDefaultData();
    }

    private void initTableAndFields() {

        String[] columnNames = {"STT", "Mã Sản Phẩm", "Giá Nhập", "Giá Bán", "SL Bán", "Lợi Nhuận"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        if (tableThongKe != null) {
            tableThongKe.setModel(tableModel);
            tableThongKe.setFont(new Font("Arial", Font.PLAIN, 14));
            tableThongKe.setRowHeight(30);
            tableThongKe.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
            tableThongKe.getTableHeader().setBackground(new Color(220, 230, 241));
            tableThongKe.getTableHeader().setReorderingAllowed(false);
            tableThongKe.getTableHeader().setResizingAllowed(false);

            tableThongKe.getColumnModel().getColumn(0).setPreferredWidth(50);
            tableThongKe.getColumnModel().getColumn(1).setPreferredWidth(150);
            tableThongKe.getColumnModel().getColumn(2).setPreferredWidth(130);
            tableThongKe.getColumnModel().getColumn(3).setPreferredWidth(130);
            tableThongKe.getColumnModel().getColumn(4).setPreferredWidth(80);
            tableThongKe.getColumnModel().getColumn(5).setPreferredWidth(150);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

            tableThongKe.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            tableThongKe.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            tableThongKe.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
            tableThongKe.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
            tableThongKe.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
            tableThongKe.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);

            tableThongKe.setSelectionBackground(new Color(173, 216, 230));
            tableThongKe.setSelectionForeground(Color.BLACK);
        }

        if (textTBD != null) {
            textTBD.setEditable(false);
            textTBD.setFont(new Font("Arial", Font.BOLD, 14));
            textTBD.setForeground(Color.RED);
            textTBD.setHorizontalAlignment(JTextField.RIGHT);
        }
        if (textLN != null) {
            textLN.setEditable(false);
            textLN.setFont(new Font("Arial", Font.BOLD, 14));
            textLN.setForeground(new Color(0, 153, 0));
            textLN.setHorizontalAlignment(JTextField.RIGHT);
        }
    }

    private void initEvents() {
        if (btnTimKiem != null) {
            btnTimKiem.addActionListener(e -> {
                Date tuNgay = jdBatDau.getDate();
                Date denNgay = jdKetThuc.getDate();

                if (tuNgay == null || denNgay == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ Ngày bắt đầu và Ngày kết thúc!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (tuNgay.after(denNgay)) {
                    JOptionPane.showMessageDialog(this, "Ngày bắt đầu không thể lớn hơn ngày kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String danhMuc = comboBoxDM != null && comboBoxDM.getSelectedItem() != null ? comboBoxDM.getSelectedItem().toString() : "ALL";

                List<ThongKe_DTO> danhSachThongKe = thongKeBUS.thongKeDoanhThu(tuNgay, denNgay, danhMuc);
                loadDataToTable(danhSachThongKe);
            });
        }

        if (btnThoat != null) {
            btnThoat.addActionListener(e -> {
                if (comboBoxDM != null) comboBoxDM.setSelectedIndex(0);

                loadDefaultData();
            });
        }

    }







    private void loadDefaultData() {
        try {
            Date today = new Date();
            jdKetThuc.setDate(today);
            Date startDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020");
            jdBatDau.setDate(startDate);
            List<ThongKe_DTO> danhSachMacDinh = thongKeBUS.thongKeDoanhThu(startDate, today, "ALL");
            if (danhSachMacDinh != null) {
                loadDataToTable(danhSachMacDinh); // Dùng lại hàm loadDataToTable cho gọn
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDataToTable(List<ThongKe_DTO> listTk) {
        tableModel.setRowCount(0);
        int stt = 1;
        double tongDoanhThu = 0;
        double tongLoiNhuan = 0;
        DecimalFormat df = new DecimalFormat("#,###");

        for (ThongKe_DTO tk : listTk) {
            Object[] row = {
                    stt++,
                    tk.getMaSanPham(),
                    df.format(tk.getGiaNhap()),
                    df.format(tk.getGiaBan()),
                    tk.getSoLuongBan(),
                    df.format(tk.getLoiNhuan())
            };
            tableModel.addRow(row);

            tongDoanhThu += (tk.getGiaBan() * tk.getSoLuongBan());
            tongLoiNhuan += tk.getLoiNhuan();
        }

        if (textTBD != null) textTBD.setText(df.format(tongDoanhThu) + " VNĐ");
        if (textLN != null) textLN.setText(df.format(tongLoiNhuan) + " VNĐ");
    }

    public JPanel getPanelMain() {
        return panelMain;
    }
}