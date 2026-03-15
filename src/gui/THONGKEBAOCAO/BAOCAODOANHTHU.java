package gui.THONGKEBAOCAO;

import bus.BaoCao_BUS;
import dto.BaoCaoDoanhThu_DTO;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BAOCAODOANHTHU extends JPanel {
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

    public BAOCAODOANHTHU() {
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
        initEvents();
        loadDefaultData();
    }

    private void initTableAndFields() {
        String[] columnNames = {
                "STT", "Ngày bán", "Số lượng hóa đơn", "Số lượng SP đã bán",
                "Tổng tiền hàng", "Giảm giá", "Doanh thu thuần", "Lợi nhuận gộp"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (tableBaoCao != null) {
            tableBaoCao.setModel(tableModel);
            tableBaoCao.setFont(new Font("Arial", Font.PLAIN, 14));
            tableBaoCao.setRowHeight(30);
            tableBaoCao.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
            tableBaoCao.getTableHeader().setBackground(new Color(220, 230, 241));
            tableBaoCao.getTableHeader().setReorderingAllowed(false);
            tableBaoCao.getTableHeader().setResizingAllowed(false);
            tableBaoCao.getColumnModel().getColumn(0).setPreferredWidth(40);
            tableBaoCao.getColumnModel().getColumn(0).setMaxWidth(40);
            tableBaoCao.getColumnModel().getColumn(1).setPreferredWidth(100);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

            tableBaoCao.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            tableBaoCao.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            tableBaoCao.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            tableBaoCao.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            tableBaoCao.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
            tableBaoCao.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
            tableBaoCao.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
            tableBaoCao.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
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

    private void initEvents() {
        if (btnTimKiem != null) {
            btnTimKiem.addActionListener(e -> {
                Date tuNgay = jdBatDau.getDate();
                Date denNgay = jdKetThuc.getDate();

                if (tuNgay == null || denNgay == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ ngày bắt đầu và ngày kết thúc!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (tuNgay.after(denNgay)) {
                    JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được lớn hơn ngày kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                List<BaoCaoDoanhThu_DTO> list = baoCaoBUS.baoCaoDoanhThuTheoNgay(tuNgay, denNgay);

                if (list == null || list.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không có dữ liệu doanh thu trong khoảng thời gian này!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
                loadDataToTable(list);
            });
        }

        if (btnThoat != null) {
            btnThoat.addActionListener(e -> {
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

            List<BaoCaoDoanhThu_DTO> list = baoCaoBUS.baoCaoDoanhThuTheoNgay(startDate, today);
            if (list != null) {
                loadDataToTable(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDataToTable(List<BaoCaoDoanhThu_DTO> list) {
        tableModel.setRowCount(0);

        if (list == null || list.isEmpty()) {
            if (textTBD != null) textTBD.setText("0");
            if (textLN != null) textLN.setText("0 VNĐ");
            return;
        }

        int stt = 1;
        long tongSoHoaDon = 0;
        double tongDoanhThuThuan = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat df = new DecimalFormat("#,###");
        for (BaoCaoDoanhThu_DTO dto : list) {
            Object[] row = {
                    stt++,
                    dto.getNgayBan() != null ? sdf.format(dto.getNgayBan()) : "",
                    dto.getSoLuongHoaDon(),
                    dto.getSoLuongSPDaBan(),
                    df.format(dto.getTongTienHang()),
                    df.format(dto.getGiamGia()),
                    df.format(dto.getDoanhThuThuan()),
                    df.format(dto.getLoiNhuanGop())
            };
            tableModel.addRow(row);

            tongSoHoaDon += dto.getSoLuongHoaDon();
            tongDoanhThuThuan += dto.getDoanhThuThuan();
        }

        if (textTBD != null) textTBD.setText(df.format(tongSoHoaDon));
        if (textLN != null) textLN.setText(df.format(tongDoanhThuThuan) + " VNĐ");
    }

    public JPanel getPanelMain() {
        return panelMain;
    }
}