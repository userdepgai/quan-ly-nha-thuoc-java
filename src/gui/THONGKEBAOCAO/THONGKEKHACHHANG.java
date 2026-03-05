package gui.THONGKEBAOCAO;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class THONGKEKHACHHANG extends JPanel {
    private JPanel panelMain;
    private JButton btnXuat;
    private JButton btnNhap;

    private JPanel pnlNgayBatDau;
    private JPanel pnlNgayKetThuc;

    private JDateChooser jdBatDau;
    private JDateChooser jdKetThuc;

    private JComboBox comboBoxDM;
    private JButton btnThoat;
    private JButton btnTimKiem;
    private JTextField textTBD;
    private JTextField textLN;
    private JTable tableThongKe;

    private DefaultTableModel tableModel;

    public THONGKEKHACHHANG()  {
        this.setLayout(new BorderLayout());
        this.add(panelMain);
        // 1. Khởi tạo bộ chọn ngày
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

        // 2. Khởi tạo bảng và các cài đặt mặc định
        initTableAndFields();
    }

    private void initTableAndFields() {
        // Cài đặt các cột cho bảng
        String[] columnNames = {
                "STT",
                "Mã KH",
                "Tên khách hàng",
                "Số điện thoại",
                "Số lần mua hàng",
                "Tổng tiền chi tiêu",
                "Xếp hạng"            
        };
        tableModel = new DefaultTableModel(columnNames, 0);

        if (tableThongKe != null) {
            tableThongKe.setModel(tableModel);
            // Thu hẹp cột STT cho đẹp
            tableThongKe.getColumnModel().getColumn(0).setPreferredWidth(40);
        }

        // Khóa các ô nhập liệu kết quả để người dùng không sửa bậy được
        if (textTBD != null) textTBD.setEditable(false);
        if (textLN != null) textLN.setEditable(false);

    }

    public JPanel getPanelMain() {
        return panelMain;
    }
}