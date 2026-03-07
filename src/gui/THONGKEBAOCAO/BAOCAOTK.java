package gui.THONGKEBAOCAO;

import bus.BaoCao_BUS;
import dto.BaoCaoTonKho_DTO;
import DBConnection.DBConnection;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BAOCAOTK extends JPanel {
    private JPanel panelMain;
    private JButton btnXuat;
    private JButton btnNhap;
    private JComboBox<String> comboBoxDM;
    private JButton btnTimKiem;
    private JButton btnThoat;
    private JPanel pnlNgayBatDau;
    private JPanel pnlNgayKetThuc;
    private JTextField textTBD;
    private JTextField textLN;
    private JTable tableBaoCao;
    private JDateChooser jdBatDau;
    private JDateChooser jdKetThuc;

    private DefaultTableModel tableModel;

    private BaoCao_BUS baoCaoBUS;
    private HashMap<String, String> mapDanhMuc = new HashMap<>();

    public BAOCAOTK() {
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
        baoCaoBUS = new BaoCao_BUS();

        initTableAndFields();
        loadDanhMucToComboBox();
        initEvents();
        loadDefaultData();
    }

    private void initTableAndFields() {
        String[] columnNames = {
                "STT", "Mã sản phẩm", "Tên sản phẩm", "Phân loại", "Tồn kho", "Hạn sử dụng", "Ngày lập", "Kho"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        if (tableBaoCao != null) {
            tableBaoCao.setModel(tableModel);
            tableBaoCao.setFont(new Font("Arial", Font.PLAIN, 14));
            tableBaoCao.setRowHeight(30);
            tableBaoCao.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
            tableBaoCao.getTableHeader().setBackground(new Color(220, 230, 241));
            tableBaoCao.getTableHeader().setReorderingAllowed(false);

            tableBaoCao.getColumnModel().getColumn(0).setPreferredWidth(5);
            tableBaoCao.getColumnModel().getColumn(1).setPreferredWidth(100);
            tableBaoCao.getColumnModel().getColumn(2).setPreferredWidth(200);
            tableBaoCao.getColumnModel().getColumn(3).setPreferredWidth(150);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            tableBaoCao.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            tableBaoCao.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            tableBaoCao.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
            tableBaoCao.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
            tableBaoCao.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
            tableBaoCao.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
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
    }

    private void loadDanhMucToComboBox() {
        if (comboBoxDM == null) return;
        comboBoxDM.removeAllItems();
        mapDanhMuc.clear();

        comboBoxDM.addItem("Tất cả Sản Phẩm");
        mapDanhMuc.put("Tất cả Sản Phẩm", "ALL");

        DBConnection dbConnection = new DBConnection();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT Ma_DM, Ten_DM FROM DANHMUC WHERE TrangThai = 1");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String maDM = rs.getString("Ma_DM");
                String tenDM = rs.getString("Ten_DM");
                comboBoxDM.addItem(tenDM);
                mapDanhMuc.put(tenDM, maDM);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvents() {
        if (btnTimKiem != null) {
            btnTimKiem.addActionListener(e -> {
                Date tuNgay = jdBatDau.getDate();
                Date denNgay = jdKetThuc.getDate();

                if (tuNgay == null || denNgay == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ ngày tháng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (tuNgay.after(denNgay)) {
                    JOptionPane.showMessageDialog(this, "Ngày bắt đầu không thể lớn hơn ngày kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String tenDMDangChon = comboBoxDM != null && comboBoxDM.getSelectedItem() != null ? comboBoxDM.getSelectedItem().toString() : "Tất cả Sản Phẩm";
                String maDanhMucToSQL = mapDanhMuc.getOrDefault(tenDMDangChon, "ALL");
                List<BaoCaoTonKho_DTO> list = baoCaoBUS.baoCaoTonKho(tuNgay, denNgay, maDanhMucToSQL);

                if (list == null || list.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không có dữ liệu tồn kho trong thời gian này!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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

        if (btnXuat != null) {
            btnXuat.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Chức năng Xuất Excel đang được cập nhật!");
            });
        }
    }

    private void loadDefaultData() {
        try {
            Date today = new Date();
            jdKetThuc.setDate(today);
            Date startDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020");
            jdBatDau.setDate(startDate);
            List<BaoCaoTonKho_DTO> list = baoCaoBUS.baoCaoTonKho(startDate, today, "ALL");
            if (list != null) {
                loadDataToTable(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDataToTable(List<BaoCaoTonKho_DTO> list) {
        tableModel.setRowCount(0);

        if (list == null || list.isEmpty()) {
            if (textTBD != null) textTBD.setText("0");
            if (textLN != null) textLN.setText("0");
            return;
        }

        int stt = 1;
        long tongTonKho = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat df = new DecimalFormat("#,###");

        for (BaoCaoTonKho_DTO tk : list) {
            Object[] row = {
                    stt++,
                    tk.getMaSP(),
                    tk.getTenSP(),
                    tk.getPhanLoai(),
                    df.format(tk.getTonKho()),
                    tk.getHanSuDung() != null ? sdf.format(tk.getHanSuDung()) : "",
                    tk.getNgayLap() != null ? sdf.format(tk.getNgayLap()) : "",
                    tk.getKho()
            };
            tableModel.addRow(row);
            tongTonKho += tk.getTonKho();
        }
        if (textTBD != null) textTBD.setText(df.format(list.size()));
        if (textLN != null) textLN.setText(df.format(tongTonKho));
    }

    public JPanel getPanelMain() {
        return panelMain;
    }
}