package gui;

import bus.LoHang_BUS;
import com.toedter.calendar.JDateChooser;
import dto.LoHang_DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class LoHang_GUI extends JPanel{
    private JTable table_dsLo;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JTextField textField5;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JTextField textField6;
    private JTextField textField7;
    private JComboBox comboBox4;
    private JComboBox comboBox6;
    private JComboBox comboBox5;
    private JComboBox comboBox7;
    private JComboBox comboBox8;
    private JComboBox comboBox9;
    private JTextField textField8;
    private JPanel panel_loHang;
    private JComboBox comboBox10;
    private JComboBox comboBox11;
    private JTextField textField9;
    private JDateChooser JDateChooser1;
    private JTextField textField10;
    private JTextField textField11;
    private JTextField textField12;
    private JButton button7;
    private JButton button5;
    private JButton button6;
    private JButton button8;
    private JTextField textField13;
    private JScrollPane src_dsLo;

    private DefaultTableModel model;
    private LoHang_BUS bus = LoHang_BUS.getInstance();

    public LoHang_GUI(){
        this.setLayout(new BorderLayout());
        this.add(panel_loHang, BorderLayout.CENTER);

        initTable();
        loadTableFromList(bus.getAll());
    }

    private void initTable() {

        String[] columns = {
                "STT",
                "Mã lô",
                "Mã sản phẩm",
                "Giá nhập",
                "Số lượng",
                "Còn lại",
                "Hạn sử dụng",
                "Thành tiền",
                "Mã NCC",
                "Mã KVLT",
                "Trạng thái"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table_dsLo.setModel(model);

        table_dsLo.getTableHeader().setResizingAllowed(false);
        table_dsLo.getTableHeader().setReorderingAllowed(false);

        table_dsLo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table_dsLo.getColumnModel().getColumn(0).setPreferredWidth(50);
        table_dsLo.getColumnModel().getColumn(1).setPreferredWidth(80);
        table_dsLo.getColumnModel().getColumn(2).setPreferredWidth(160);
        table_dsLo.getColumnModel().getColumn(3).setPreferredWidth(100);
        table_dsLo.getColumnModel().getColumn(4).setPreferredWidth(80);
        table_dsLo.getColumnModel().getColumn(5).setPreferredWidth(80);
        table_dsLo.getColumnModel().getColumn(6).setPreferredWidth(100);
        table_dsLo.getColumnModel().getColumn(7).setPreferredWidth(100);
        table_dsLo.getColumnModel().getColumn(8).setPreferredWidth(160);
        table_dsLo.getColumnModel().getColumn(9).setPreferredWidth(160);
        table_dsLo.getColumnModel().getColumn(10).setPreferredWidth(89);

        src_dsLo.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        table_dsLo.setRowHeight(25);
        table_dsLo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void loadTableFromList(ArrayList<LoHang_DTO> list) {

        model.setRowCount(0);
        int stt = 1;

        for (LoHang_DTO lo : list) {

            String trangThai;

            if (lo.getTrangThai() == 1) {
                trangThai = "Còn hạn";
            } else if (lo.getTrangThai() == 0) {
                trangThai = "Hết hạn";
            } else {
                trangThai = "Không xác định";
            }

            model.addRow(new Object[]{
                    stt++,
                    lo.getMaLo(),
                    lo.getMaSp(),
                    lo.getGiaNhap(),
                    lo.getSoLuong(),
                    lo.getSoLuongConLai(),
                    lo.getHsd(),
                    lo.getThanhTien(),
                    lo.getMaNcc(),
                    lo.getMaKvlt(),
                    trangThai
            });
        }
    }
}
