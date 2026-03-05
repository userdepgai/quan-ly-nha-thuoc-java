package gui.THONGKEBAOCAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import com.toedter.calendar.JDateChooser;

public class BAOCAODOANHTHU  extends JPanel {
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

    public BAOCAODOANHTHU() {
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

        this.setLayout(new BorderLayout());
        this.add(panelMain);
        String[] columnNames = {
                "STT",
                "Ngày bán",
                "Số lượng hóa đơn",
                "Số lượng SP đã bán",
                "Tổng tiền hàng",
                "Giảm giá",
                "Doanh thu thuần",
                "Lợi nhuận gộp"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Trả về false để người dùng không thể sửa trực tiếp trên bảng
                return false;
            }
        };

        // 3. Gán model vào JTable
        tableBaoCao.setModel(tableModel);

        // (Tùy chọn) Căn chỉnh lại chiều rộng của cột STT cho nhỏ lại
        tableBaoCao.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableBaoCao.getColumnModel().getColumn(0).setMaxWidth(40);
    }

    // Phương thức ví dụ để thêm dữ liệu (bạn có thể gọi sau khi truy vấn Database)
    public void addRowData(Object[] rowData) {
        tableModel.addRow(rowData);
    }
    // Hàm này dùng để trả về JPanel gốc chứa toàn bộ giao diện của form

}