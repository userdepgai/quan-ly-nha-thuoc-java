package gui;

import bus.ChucNang_BUS;
import bus.PhanQuyenChucNang_BUS;
import bus.PhanQuyen_BUS;
import dto.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PhanQuyen_GUI extends JPanel{
    private JPanel panelPhanQuyen;
    private JTable table_dsQuyen;
    private JButton btn_xuatExcel;
    private JButton btn_nhapExcel;
    private JLabel label_tieuDe;
    private JPanel panel_boLoc;
    private JTextField txt_maQuyen;
    private JTextField txt_tenQuyen;
    private JTextField textField4;
    private JButton btn_luu;
    private JButton btn_capNhat;
    private JButton btn_them;
    private JTextField txt_ndTimKiem;
    private JButton btn_timKiem;
    private JButton btn_thoat;
    private JComboBox cmb_locTrangThai;
    private JScrollPane src_dsQuyen;
    private JLabel label_maQuyen;
    private JLabel label_tenQuyen;
    private JLabel label_trangThai;
    private JComboBox cmb_trangThai;
    private JLabel label_moTa;
    private JTextArea txt_moTa;
    private JScrollPane src_moTa;
    private JPanel panel_thongTinChiTiet;
    private JButton btn_huy;
    private JPanel panelChucNang;
    private DefaultTableModel model_dsQuyen;

    private JTable tableChucNang;
    private DefaultTableModel modelChucNang;

    private ChucNang_BUS chucNangBus = ChucNang_BUS.getInstance();
    private PhanQuyenChucNang_BUS pqcnBus = PhanQuyenChucNang_BUS.getInstance();

    private JPanel panelChucNangDefault;

    private boolean isAdding = false;
    private boolean isUpdating = false;

    private JPopupMenu popupGoiY = new JPopupMenu();

    private PhanQuyen_BUS pqBus = PhanQuyen_BUS.getInstance();

    public PhanQuyen_GUI(){
        this.setLayout(new BorderLayout());
        this.add(panelPhanQuyen, BorderLayout.CENTER);

        panelChucNangDefault = new JPanel(new BorderLayout());
        panelChucNangDefault.add(new JLabel("Chọn quyền để xem chức năng", SwingConstants.CENTER));
        panelChucNang.setPreferredSize(new Dimension(420, 220));
        panelChucNang.setMinimumSize(new Dimension(420, 220));
        panelChucNang.setMaximumSize(new Dimension(420, 220));

        panelChucNang.setLayout(new BorderLayout());
        panelChucNang.add(panelChucNangDefault, BorderLayout.CENTER);

        formEdit();
        initTable();
        loadTableFromList(pqBus.getAll());

        xuLySuKien();
    }

    private void formEdit() {
        cmb_trangThai.setModel(new DefaultComboBoxModel<>(new String[]{
                "-- Chọn trạng thái --",
                PhanQuyen_DTO.HOAT_DONG,
                PhanQuyen_DTO.NGUNG_HOAT_DONG
        }));

        cmb_locTrangThai.setModel(new DefaultComboBoxModel<>(new String[]{
                "Tất cả",
                PhanQuyen_DTO.HOAT_DONG,
                PhanQuyen_DTO.NGUNG_HOAT_DONG
        }));

        setViewMode();
    }

    private void initTable() {
        String[] columns = {
                "STT",
                "Mã quyền",
                "Tên quyền",
                "Mô tả",
                "Trạng thái"
        };
        model_dsQuyen = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table_dsQuyen.setModel(model_dsQuyen);

        table_dsQuyen.getTableHeader().setResizingAllowed(false);
        table_dsQuyen.getTableHeader().setReorderingAllowed(false);

        table_dsQuyen.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table_dsQuyen.getColumnModel().getColumn(0).setPreferredWidth(90);
        table_dsQuyen.getColumnModel().getColumn(1).setPreferredWidth(180);
        table_dsQuyen.getColumnModel().getColumn(2).setPreferredWidth(300);
        table_dsQuyen.getColumnModel().getColumn(3).setPreferredWidth(400);
        table_dsQuyen.getColumnModel().getColumn(4).setPreferredWidth(188);

        table_dsQuyen.setRowHeight(25);
        table_dsQuyen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        src_dsQuyen.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        src_dsQuyen.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
    }

    private void hienPanelChucNang(String maQuyen) {
        panelChucNang.removeAll();
        String[] columns = {"Tên chức năng", "Chọn"};
        modelChucNang = new DefaultTableModel(columns,0){
            @Override
            public Class<?> getColumnClass(int column) {
                if(column == 1) return Boolean.class;
                return String.class;
            }
            @Override
            public boolean isCellEditable(int row,int column){
                return column == 1;
            }
        };
        tableChucNang = new JTable(modelChucNang);

        tableChucNang.setRowHeight(22);
        tableChucNang.getColumnModel().getColumn(0).setPreferredWidth(250);
        tableChucNang.getColumnModel().getColumn(1).setPreferredWidth(60);
        tableChucNang.getColumnModel().getColumn(1).setMaxWidth(60);
        tableChucNang.getColumnModel().getColumn(1).setMinWidth(60);
        ArrayList<ChucNang_DTO> listCN = chucNangBus.getAll();
        for(ChucNang_DTO cn : listCN){
            boolean checked = false;
            if(maQuyen != null){
                checked = pqcnBus.hasPermission(maQuyen, cn.getMaCN());
            }
            modelChucNang.addRow(new Object[]{
                    cn.getTenCN(),
                    checked
            });
        }

        JScrollPane scroll = new JScrollPane(tableChucNang);
        scroll.setPreferredSize(new Dimension(420, 220));

        panelChucNang.removeAll();
        panelChucNang.add(scroll, BorderLayout.CENTER);
        panelChucNang.revalidate();
        panelChucNang.repaint();;
    }
    private ArrayList<String> getChucNangDuocChon(){

        ArrayList<String> list = new ArrayList<>();

        for(int i=0;i<modelChucNang.getRowCount();i++){

            Boolean checked = (Boolean) modelChucNang.getValueAt(i,1);

            if(Boolean.TRUE.equals(checked)){

                String ten = modelChucNang.getValueAt(i,0).toString();

                ChucNang_DTO cn = chucNangBus.getByName(ten);

                if(cn!=null)
                    list.add(cn.getMaCN());
            }
        }

        return list;
    }

    private void xuLySuKien() {
        table_dsQuyen.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting())
                hienChiTiet();
        });

        btn_them.addActionListener(e -> {
            setAddMode();
        });

        btn_capNhat.addActionListener(e -> {
            if (table_dsQuyen.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần cập nhật");
                return;
            }
            setUpdateMode();
        });

        btn_luu.addActionListener(e -> {
            if (isAdding) {
                xuLyThem();
            } else if (isUpdating) {
                xuLyCapNhat();
            }
        });

        btn_huy.addActionListener(e -> {
            btn_them.setEnabled(true);
            btn_capNhat.setEnabled(true);

            setViewMode();
            clearForm();
        });

        txt_ndTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(!txt_ndTimKiem.getText().trim().isEmpty())
                    hienThiGoiY(timKiem());
                else popupGoiY.setVisible(false);
            }
        });

        btn_timKiem.addActionListener(e -> {
            loadTableFromList(timKiem());
        });

        btn_thoat.addActionListener(e -> {
            cmb_locTrangThai.setSelectedIndex(0);
            txt_ndTimKiem.setText("");

            loadTableFromList(pqBus.getAll());
        });
    }

    private void setAddMode() {

        lockTable();

        isAdding = true;
        isUpdating = false;

        txt_maQuyen.setText(pqBus.getNextId());
        txt_maQuyen.setEditable(false);

        txt_tenQuyen.setText("");
        txt_tenQuyen.setEditable(true);

        txt_moTa.setText("");
        txt_moTa.setEditable(true);

        cmb_trangThai.setSelectedItem(PhanQuyen_DTO.HOAT_DONG);
        cmb_trangThai.setEnabled(false);

        hienPanelChucNang(null);

        btn_luu.setVisible(true);
        btn_huy.setVisible(true);

        btn_them.setEnabled(false);
        btn_capNhat.setEnabled(false);
    }

    private void setUpdateMode() {
        lockTable();

        isAdding = false;
        isUpdating = true;

        txt_maQuyen.setEditable(false);
        txt_tenQuyen.setEditable(true);
        txt_moTa.setEditable(true);

        cmb_trangThai.setEnabled(true);

        hienPanelChucNang(txt_maQuyen.getText());

        btn_luu.setVisible(true);
        btn_huy.setVisible(true);

        btn_them.setEnabled(false);
        btn_capNhat.setEnabled(false);
    }
    private void xuLyThem() {
        PhanQuyen_DTO quyen = layDuLieuTuForm();

        if (!kiemTraHopLe(quyen)) return;

        boolean result = pqBus.them(quyen);

        if (result) {
            ArrayList<String> listCN = getChucNangDuocChon();
            pqcnBus.updatePermissions(quyen.getMaQuyen(), listCN);

            JOptionPane.showMessageDialog(this, "Thêm thành công");
            loadTableFromList(pqBus.getAll());
            clearForm();
            setViewMode();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại");
        }
    }
    private void xuLyCapNhat() {
        PhanQuyen_DTO quyen = layDuLieuTuForm();

        if (!kiemTraHopLe(quyen)) return;

        boolean result = pqBus.capNhat(quyen);

        if (result) {
            ArrayList<String> listCN = getChucNangDuocChon();
            pqcnBus.updatePermissions(quyen.getMaQuyen(), listCN);

            JOptionPane.showMessageDialog(this, "Cập nhật thành công");
            loadTableFromList(pqBus.getAll());
            clearForm();
            setViewMode();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
        }
    }
    private PhanQuyen_DTO layDuLieuTuForm() {
        String ma = txt_maQuyen.getText().trim();
        String ten = txt_tenQuyen.getText().trim();
        String moTa = txt_moTa.getText().trim();
        String textTrangThai = (String) cmb_trangThai.getSelectedItem();
        int trangThai = PhanQuyen_DTO.parseTrangThaiFromText(textTrangThai);
        return new PhanQuyen_DTO(ma, ten, moTa, trangThai);
    }

    private ArrayList<PhanQuyen_DTO> timKiem() {
        String keyword = txt_ndTimKiem.getText().trim();
        String textTrangThai = (String) cmb_locTrangThai.getSelectedItem();
        Integer trangThai = null;
        if (!textTrangThai.equals("Tất cả")) {
            trangThai = PhanQuyen_DTO.parseTrangThaiFromText(textTrangThai);
        }
        return pqBus.timKiem(keyword, trangThai);
    }
    private void hienChiTiet() {
        int row = table_dsQuyen.getSelectedRow();
        if (row >= 0) {
            txt_maQuyen.setText(table_dsQuyen.getValueAt(row, 1).toString());
            txt_tenQuyen.setText(table_dsQuyen.getValueAt(row, 2).toString());
            txt_moTa.setText(table_dsQuyen.getValueAt(row, 3).toString());
            String trangThaiText = table_dsQuyen.getValueAt(row, 4).toString();
            cmb_trangThai.setSelectedItem(trangThaiText);
        }
    }


    private void loadTableFromList(ArrayList<PhanQuyen_DTO> list) {
        model_dsQuyen.setRowCount(0);
        int stt = 1;
        for (PhanQuyen_DTO quyen : list) {
            model_dsQuyen.addRow(new Object[]{
                    stt++,
                    quyen.getMaQuyen(),
                    quyen.getTenQuyen(),
                    quyen.getMoTa(),
                    quyen.getTrangThaiText()
            });
        }
    }

    private void clearForm() {
        txt_maQuyen.setText("");
        txt_tenQuyen.setText("");
        txt_moTa.setText("");

        cmb_trangThai.setSelectedIndex(0);
    }

    public boolean kiemTraHopLe(PhanQuyen_DTO quyen) {

        if (quyen.getTenQuyen().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên quyền không được để trống");
            return false;
        }

        if (quyen.getMoTa().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mô tả không được để trống");
            return false;
        }

        if (quyen.getTrangThai() == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn trạng thái");
            return false;
        }

        return true;
    }

    private void hienThiGoiY(ArrayList<PhanQuyen_DTO> list) {
        popupGoiY.setVisible(false);
        popupGoiY.removeAll();

        if (list.isEmpty()) return;

        JPanel panelNoiDung = new JPanel();
        panelNoiDung.setLayout(new BoxLayout(panelNoiDung, BoxLayout.Y_AXIS));
        panelNoiDung.setBackground(Color.WHITE);

        for (PhanQuyen_DTO pq : list) {
            JButton btnItem = new JButton(pq.getMaQuyen() + " - " + pq.getTenQuyen());
            btnItem.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnItem.setHorizontalAlignment(SwingConstants.LEFT);
            btnItem.setMargin(new Insets(2, 10, 2, 10));
            btnItem.setBorderPainted(false);
            btnItem.setContentAreaFilled(false);
            btnItem.setFocusPainted(false);
            btnItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnItem.setPreferredSize(new Dimension(txt_ndTimKiem.getWidth(), 30));
            btnItem.setMaximumSize(new Dimension(txt_ndTimKiem.getWidth(), 30));

            btnItem.addActionListener(e -> {
                txt_ndTimKiem.setText(pq.getMaQuyen());
                popupGoiY.setVisible(false);
            });
            btnItem.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    btnItem.setContentAreaFilled(true);
                    btnItem.setBackground(new Color(240, 240, 240));
                }
                public void mouseExited(MouseEvent evt) {
                btnItem.setContentAreaFilled(false);
            }
            });

            panelNoiDung.add(btnItem);
        }

        JScrollPane scrollPane = new JScrollPane(panelNoiDung);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

        int height = Math.min(list.size() * 30, 150);
        scrollPane.setPreferredSize(new Dimension(txt_ndTimKiem.getWidth(), height));

        popupGoiY.setFocusable(false);
        popupGoiY.add(scrollPane);
        popupGoiY.show(txt_ndTimKiem, 0, txt_ndTimKiem.getHeight());

        scrollPane.setFocusable(false);
    }

    private void setViewMode() {
        unlockTable();

        txt_maQuyen.setEditable(false);
        txt_tenQuyen.setEditable(false);
        txt_moTa.setEditable(false);

        cmb_trangThai.setEnabled(false);

        btn_luu.setVisible(false);
        btn_huy.setVisible(false);

        btn_them.setEnabled(true);
        btn_capNhat.setEnabled(true);

        panelChucNang.removeAll();
        panelChucNang.add(panelChucNangDefault, BorderLayout.CENTER);
        panelChucNang.revalidate();
        panelChucNang.repaint();

        isAdding = false;
        isUpdating = false;
    }
    private void lockTable() {
        table_dsQuyen.setRowSelectionAllowed(false);
        table_dsQuyen.setEnabled(false);
    }

    private void unlockTable() {
        table_dsQuyen.setRowSelectionAllowed(true);
        table_dsQuyen.setEnabled(true);
    }
}
