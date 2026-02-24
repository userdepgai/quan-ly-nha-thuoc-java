package gui;

import bus.TaiKhoan_BUS;
import dto.TaiKhoan_DTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TaiKhoan_GUI extends JPanel{
    private JPanel panel_taiKhoan;
    private JComboBox cmb_quyen;
    private JButton btnTimKiem;
    private JButton btnThoat;
    private JButton btnXuatExcel;
    private JButton btnNhapExcel;
    private JTable table_dsTaiKhoan;
    private JTextField textField4;
    private JTextField txt_tenNguoiDung;
    private JTextField txt_sdt;
    private JTextField txt_matKhau;
    private JPanel panelThongTin;
    private JPanel panelTable;
    private JButton btn_capNhat;
    private JButton btn_them;
    private JLabel label_tieuDe;
    private JScrollPane src_dsTaiKhoan;
    private JLabel label_ten;
    private JLabel label_sdt;
    private JTextField txt_ma;
    private JTextField txt_quyen;
    private JTextField txt_ngayKichHoat;
    private JComboBox cmb_trangThai;
    private JButton btn_huy;
    private JButton btn_luu;
    private JLabel label_maTK;
    private JPanel lebel_sdt;
    private JLabel label_mk;
    private JLabel label_quyen;
    private JLabel label_ngayKichHoat;
    private JLabel label_trangThai;
    private JComboBox cmb_locTrangThai;
    private JTextField txt_ndTimKiem;

    private TaiKhoan_BUS bus = TaiKhoan_BUS.getInstance();
    private DefaultTableModel model;
    private boolean isAdding = false;
    private boolean isUpdating = false;
    private JPopupMenu popupGoiY = new JPopupMenu();


    public TaiKhoan_GUI() {
        this.setLayout(new BorderLayout());
        this.add(panel_taiKhoan, BorderLayout.CENTER);

        fromEdit();
        initTable();
        loadTableFromList(bus.getAll());
        xuLyXuKien();
    }

    private void fromEdit() {

        cmb_trangThai.setModel(new DefaultComboBoxModel<>(
                new String[]{"-- Chọn trạng thái --", "Mở", "Đóng"}
        ));
    }
    private void initTable() {
        String[] columns = {"STT", "Mã tài khoản", "Tên người dùng", "SDT", "Mật khẩu", "Ngày kích hoạt", "Quyền", "Trạng thái"};
        model = new DefaultTableModel(columns,0) {
            @Override
            public boolean isCellEditable(int row, int column) {    return false;   }
        };

        table_dsTaiKhoan.setModel(model);
        table_dsTaiKhoan.getTableHeader().setResizingAllowed(false);
        table_dsTaiKhoan.getTableHeader().setReorderingAllowed(false);

        table_dsTaiKhoan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table_dsTaiKhoan.getColumnModel().getColumn(0).setPreferredWidth(50);
        table_dsTaiKhoan.getColumnModel().getColumn(1).setPreferredWidth(120);
        table_dsTaiKhoan.getColumnModel().getColumn(2).setPreferredWidth(188);
        table_dsTaiKhoan.getColumnModel().getColumn(3).setPreferredWidth(140);
        table_dsTaiKhoan.getColumnModel().getColumn(4).setPreferredWidth(200);
        table_dsTaiKhoan.getColumnModel().getColumn(5).setPreferredWidth(150);
        table_dsTaiKhoan.getColumnModel().getColumn(6).setPreferredWidth(180);
        table_dsTaiKhoan.getColumnModel().getColumn(7).setPreferredWidth(128);

        table_dsTaiKhoan.setRowHeight(25);
        table_dsTaiKhoan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        src_dsTaiKhoan.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        src_dsTaiKhoan.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
    }
    private void loadTableFromList(ArrayList<TaiKhoan_DTO> list) {
        model.setRowCount(0);
        int stt = 1;
        for(TaiKhoan_DTO tk : list) {
            model.addRow(new Object[]{
                    stt++,
                    tk.getMaTK(),
                    "",
                    tk.getSdt(),
                    tk.getMatKhau(),
                    tk.getNgayKichHoat(),
                   bus.getTenQuyen(tk.getMaQuyen()),
                   tk.getTrangThai() == 1 ? "Mở" : "Khóa"
            });
        }
    }

    private void xuLyXuKien(){
        table_dsTaiKhoan.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting())
                hienChiTiet();
        });
    }
    private void hienChiTiet() {
        int row = table_dsTaiKhoan.getSelectedRow();
        if(row >= 0){
            txt_ma.setText(table_dsTaiKhoan.getValueAt(row,1).toString());
            // ten
            txt_sdt.setText(table_dsTaiKhoan.getValueAt(row,3).toString());
            txt_matKhau.setText(table_dsTaiKhoan.getValueAt(row,4).toString());
            txt_quyen.setText(table_dsTaiKhoan.getValueAt(row,6).toString());
            txt_ngayKichHoat.setText(table_dsTaiKhoan.getValueAt(row,5).toString());

            String trangThai = table_dsTaiKhoan.getValueAt(row, 7).toString();
            cmb_trangThai.setSelectedItem(trangThai);
        }
    }

    private void hienThiGoiY(ArrayList<TaiKhoan_DTO> list) {
        popupGoiY.setVisible(false);
        popupGoiY.removeAll();

        if(list.isEmpty()) return;

        JPanel panelNoiDung = new JPanel();
        panelNoiDung.setLayout(new BoxLayout(panelNoiDung, BoxLayout.Y_AXIS));
        panelNoiDung.setBackground(Color.WHITE);

        for(TaiKhoan_DTO tk : list) {
            JButton btnItem = new JButton(tk.getSdt() + " - " + ".....");
            btnItem.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnItem.setHorizontalAlignment(SwingConstants.LEFT);
            btnItem.setMargin(new Insets(2,10,2,10));
            btnItem.setBorderPainted(false);
            btnItem.setContentAreaFilled(false);
            btnItem.setFocusPainted(false);
            btnItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnItem.setPreferredSize(new Dimension(txt_ndTimKiem.getWidth(),30));
            btnItem.setMaximumSize(new Dimension(txt_ndTimKiem.getWidth(),30));

            btnItem.addActionListener(e -> {
                txt_ndTimKiem.setText(tk.getSdt() + " - " + "....");
                popupGoiY.setVisible(false);
            });

            btnItem.addMouseListener(new MouseAdapter() {
                public void mouserEntered(MouseEvent e) {
                    btnItem.setContentAreaFilled(true);
                    btnItem.setBackground(new Color(240,240,240));
                }
                public void mouserExited(MouseEvent e) { btnItem.setContentAreaFilled(false);}
            });

            panelNoiDung.add(btnItem);
        }

        JScrollPane src = new JScrollPane(panelNoiDung);
        src.setBorder(null);
        src.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));
        src.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

        int height = Math.min(list.size() * 30, 150);
        src.setPreferredSize(new Dimension(txt_ndTimKiem.getWidth(), height));

        popupGoiY.setFocusable(false);
        popupGoiY.add(src);
        popupGoiY.show(txt_ndTimKiem,0,txt_ndTimKiem.getHeight());

        src.setFocusable(false);
    }
}
