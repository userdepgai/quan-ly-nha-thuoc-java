package gui.THONGKEBAOCAO;

import bus.ThongKe_BUS;
import dto.ThongKeKhachHang_DTO;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class THONGKEKHACHHANG extends JPanel {
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

    public THONGKEKHACHHANG()  {
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

        thongKeBUS = new ThongKe_BUS();

        initTableAndFields();
        initEvents();
        loadDefaultData();
    }

    private void initTableAndFields() {
        String[] columnNames = {
                "STT", "Mã KH", "Tên khách hàng", "Số điện thoại", "Số lần mua hàng", "Tổng tiền chi tiêu", "Xếp hạng"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        if (tableThongKe != null) {
            tableThongKe.setModel(tableModel);
            tableThongKe.setFont(new Font("Arial", Font.PLAIN, 14));

            // Cố định chiều cao dòng
            tableThongKe.setRowHeight(30);

            tableThongKe.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
            tableThongKe.getTableHeader().setBackground(new Color(220, 230, 241));

            // Khóa không cho người dùng kéo thả kéo giãn các cột lung tung
            tableThongKe.getTableHeader().setReorderingAllowed(false);
            tableThongKe.getTableHeader().setResizingAllowed(false);

            tableThongKe.getColumnModel().getColumn(0).setPreferredWidth(50);
            tableThongKe.getColumnModel().getColumn(1).setPreferredWidth(100);
            tableThongKe.getColumnModel().getColumn(2).setPreferredWidth(150);
            tableThongKe.getColumnModel().getColumn(5).setPreferredWidth(150);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

            tableThongKe.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            tableThongKe.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            tableThongKe.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            tableThongKe.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
            tableThongKe.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
            tableThongKe.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        }

        if (textTBD != null) {
            textTBD.setEditable(false);
            textTBD.setFont(new Font("Arial", Font.BOLD, 14));
            textTBD.setForeground(Color.BLUE);
            textTBD.setHorizontalAlignment(JTextField.RIGHT);
        }
        if (textLN != null) {
            textLN.setEditable(false);
            textLN.setFont(new Font("Arial", Font.BOLD, 14));
            textLN.setForeground(Color.RED);
            textLN.setHorizontalAlignment(JTextField.RIGHT);
        }

        if (comboBoxDM != null) {
            comboBoxDM.removeAllItems();
            comboBoxDM.addItem("Tất cả");
            comboBoxDM.addItem("DONG");
            comboBoxDM.addItem("BAC");
            comboBoxDM.addItem("VANG");
            comboBoxDM.addItem("KIM CUONG");
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

                String hangThanhVien = comboBoxDM != null && comboBoxDM.getSelectedItem() != null ? comboBoxDM.getSelectedItem().toString() : "Tất cả";

                List<ThongKeKhachHang_DTO> list = thongKeBUS.thongKeKhachHangVIP(tuNgay, denNgay, hangThanhVien);

                if (list == null || list.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không có khách hàng mua sắm trong khoảng thời gian này!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }

                loadDataToTable(list);
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

            List<ThongKeKhachHang_DTO> listMacDinh = thongKeBUS.thongKeKhachHangVIP(startDate, today, "Tất cả");
            if (listMacDinh != null) {
                loadDataToTable(listMacDinh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDataToTable(List<ThongKeKhachHang_DTO> list) {
        tableModel.setRowCount(0);

        if (list == null || list.isEmpty()) {
            if (textTBD != null) textTBD.setText("0");
            if (textLN != null) textLN.setText("0 VNĐ");
            return;
        }

        int stt = 1;
        double tongDoanhThuTatCa = 0;
        DecimalFormat df = new DecimalFormat("#,###");

        for (ThongKeKhachHang_DTO kh : list) {
            Object[] row = {
                    stt++,
                    kh.getMaKH(),
                    kh.getTenKH(),
                    kh.getSdt(),
                    kh.getSoLanMuaHang(),
                    df.format(kh.getTongTienChiTieu()),
                    kh.getXepHang()
            };
            tableModel.addRow(row);

            tongDoanhThuTatCa += kh.getTongTienChiTieu();
        }

        int tongSoKhach = list.size();
        double chiTieuTrungBinh = 0;
        if (tongSoKhach > 0) {
            chiTieuTrungBinh = tongDoanhThuTatCa / tongSoKhach;
        }

        if (textTBD != null) textTBD.setText(String.valueOf(tongSoKhach));
        if (textLN != null) textLN.setText(df.format(chiTieuTrungBinh) + " VNĐ");
    }

    public JPanel getPanelMain() {
        return panelMain;
    }
}