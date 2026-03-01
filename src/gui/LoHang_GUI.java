package gui;

import bus.LoHang_BUS;
import dto.LoHang_DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class LoHang_GUI extends JPanel{
    private JTable table_dsLo;
    private JTextField txtMaLo;
    private JTextField txtNhaCungCap;
    private JTextField txtKVLT;
    private JButton btnCapNhat;
    private JButton btnThem;
    private JButton btnXuatExcel;
    private JButton btnNhapExcel;
    private JTextField textField6;
    private JTextField txtHienCo;
    private JComboBox comboBox4;
    private JComboBox comboBox6;
    private JComboBox comboBox7;
    private JComboBox comboBox8;
    private JPanel panel_loHang;
    private JComboBox cmbPNK;
    private JComboBox cmbSanPham;
    private JTextField txtMaSanPham;
    private JTextField txtGiaNhap;
    private JTextField txtThanhTien;
    private JButton btnThoat;
    private JButton btnTimKiem;
    private JTextField textField13;
    private JScrollPane src_dsLo;
    private JLabel label_tieuDe;
    private JLabel labelLocMaLo;
    private JLabel labelHienCo;
    private JLabel labelLocMaSanPham;
    private JLabel labelLocNhaCungCap;
    private JLabel labelLocKVLT;
    private JLabel labelLocKhoangGia;
    private JLabel lableTinhTrangHSD;
    private JComboBox comboBox3;
    private JLabel labelLocTrangThai;
    private JLabel labelMaLo;
    private JLabel labelNhaCungCap;
    private JLabel label;
    private JTextField txtLocMaPNK;
    private JLabel labelLocMaPNK;
    private JLabel labelPNK;
    private JLabel labelSanPham;
    private JComboBox cmbTrangThai;
    private JTextField txtHSD;
    private JTextField txtSoLuong;
    private JButton btnHuy;
    private JButton btnLuu;
    private JPanel labelKVLT;
    private JLabel labelSoLuong;
    private JLabel labelHSD;
    private JLabel labeltrangThai;
    private JLabel labelMaSanPham;
    private JLabel labelGiaNhap;
    private JLabel labelThanhTien;

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
                "Mã PNK",
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
        table_dsLo.getColumnModel().getColumn(1).setPreferredWidth(75);
        table_dsLo.getColumnModel().getColumn(2).setPreferredWidth(75);
        table_dsLo.getColumnModel().getColumn(3).setPreferredWidth(150);
        table_dsLo.getColumnModel().getColumn(4).setPreferredWidth(100);
        table_dsLo.getColumnModel().getColumn(5).setPreferredWidth(70);
        table_dsLo.getColumnModel().getColumn(6).setPreferredWidth(70);
        table_dsLo.getColumnModel().getColumn(7).setPreferredWidth(90);
        table_dsLo.getColumnModel().getColumn(8).setPreferredWidth(100);
        table_dsLo.getColumnModel().getColumn(9).setPreferredWidth(150);
        table_dsLo.getColumnModel().getColumn(10).setPreferredWidth(150);
        table_dsLo.getColumnModel().getColumn(11).setPreferredWidth(80);

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
                    lo.getMaPnk(),
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
