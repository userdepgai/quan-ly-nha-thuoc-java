package gui;

import bus.PhanQuyen_BUS;
import dto.PhanQuyen_DTO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Locale;

public class PhanQuyen_GUI extends JPanel {
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
    private DefaultTableModel model_dsQuyen;

    private boolean isAdding = false;
    private boolean isUpdating = false;

    private JPopupMenu popupGoiY = new JPopupMenu();

    private PhanQuyen_BUS pqBus = PhanQuyen_BUS.getInstance();

    public PhanQuyen_GUI() {
        this.setLayout(new BorderLayout());
        this.add(panelPhanQuyen, BorderLayout.CENTER);

        formEdit();
        initTable();
        loadTableFromList(pqBus.getAll());

        xuLySuKien();
    }

    private void formEdit() {
        cmb_trangThai.setModel(new DefaultComboBoxModel<>(
                new String[]{"-- Chọn trạng thái --", "Hoạt động", "Ngưng hoạt động"}
        ));
        cmb_locTrangThai.setModel(new DefaultComboBoxModel<>(
                new String[]{"Tất cả", "Hoạt động", "Ngưng hoạt động"}
        ));
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

    private void xuLySuKien() {
        table_dsQuyen.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                hienChiTiet();
        });

        btn_them.addActionListener(e -> {
            btn_them.setEnabled(false);
            btn_capNhat.setEnabled(false);

            isAdding = true;
            isUpdating = false;

            txt_maQuyen.setText(pqBus.getNextId());
            txt_maQuyen.setEditable(false);

            txt_tenQuyen.setText("");
            txt_tenQuyen.setEditable(true);
            txt_moTa.setText("");
            txt_moTa.setEditable(true);

            cmb_trangThai.setSelectedIndex(1);
            cmb_trangThai.setEnabled(false);

            btn_luu.setVisible(true);
            btn_huy.setVisible(true);
        });

        btn_capNhat.addActionListener(e -> {
            int row = table_dsQuyen.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần cập nhật");
                return;
            }
            btn_them.setEnabled(false);
            btn_capNhat.setEnabled(false);

            isAdding = false;
            isUpdating = true;

            txt_tenQuyen.setEditable(true);
            txt_moTa.setEditable(true);
            cmb_trangThai.setEnabled(true);

            btn_luu.setVisible(true);
            btn_huy.setVisible(true);
        });

        btn_luu.addActionListener(e -> {
            String ma = txt_maQuyen.getText();
            String ten = txt_tenQuyen.getText();
            String moTa = txt_moTa.getText();
            int trangThai = -1;

            if (cmb_trangThai.getSelectedIndex() == 1) {
                trangThai = 1;
            } else if (cmb_trangThai.getSelectedIndex() == 2) {
                trangThai = 0;
            }

            PhanQuyen_DTO quyen = new PhanQuyen_DTO(ma, ten, moTa, trangThai);

            boolean result = false;

            if (kiemTraHopLe(quyen)) {
                if (isAdding)
                    result = pqBus.them(quyen);
                else if (isUpdating)
                    result = pqBus.capNhat(quyen);

                if (result) {
                    JOptionPane.showMessageDialog(this, "Lưu thành công");
                    loadTableFromList(pqBus.getAll());
                    resetState();
                } else {
                    if (pqBus.kiemTraHopLe(quyen))
                        JOptionPane.showMessageDialog(this, "Lưu thất bại");
                }
            }

            btn_them.setEnabled(true);
            btn_capNhat.setEnabled(true);
        });

        btn_huy.addActionListener(e -> {
            btn_them.setEnabled(true);
            btn_capNhat.setEnabled(true);

            resetState();
        });

        txt_ndTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String keyword = txt_ndTimKiem.getText().trim();

                if (keyword.isEmpty()) {
                    popupGoiY.setVisible(false);
                    loadTableFromList(pqBus.getAll());
                    return;
                }

                Integer trangThai = null;
                if (cmb_locTrangThai.getSelectedIndex() == 1) {
                    trangThai = 1;
                } else if (cmb_locTrangThai.getSelectedIndex() == 2) {
                    trangThai = 0;
                }
                ArrayList<PhanQuyen_DTO> list =
                        pqBus.timKiem(keyword, trangThai);

                hienThiGoiY(list);
            }
        });

        btn_timKiem.addActionListener(e -> {
            String keyword = txt_ndTimKiem.getText().trim();

            Integer trangThai = null;
            if (cmb_locTrangThai.getSelectedIndex() == 1) {
                trangThai = 1;
            } else if (cmb_locTrangThai.getSelectedIndex() == 2) {
                trangThai = 0;
            }

            ArrayList<PhanQuyen_DTO> list = pqBus.timKiem(keyword, trangThai);
            loadTableFromList(list);
        });

        btn_thoat.addActionListener(e -> {
            cmb_locTrangThai.setSelectedIndex(0);
            txt_ndTimKiem.setText("");

            loadTableFromList(pqBus.getAll());
        });
    }

    private void hienChiTiet() {
        int row = table_dsQuyen.getSelectedRow();

        if (row >= 0) {
            txt_maQuyen.setText(table_dsQuyen.getValueAt(row, 1).toString());
            txt_tenQuyen.setText(table_dsQuyen.getValueAt(row, 2).toString());
            txt_moTa.setText(table_dsQuyen.getValueAt(row, 3).toString());

            String trangThai = table_dsQuyen.getValueAt(row, 4).toString();
            cmb_trangThai.setSelectedItem(trangThai);
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
                    quyen.getTrangThai() == 1 ? "Hoạt động" : "Ngưng hoạt động"
            });
        }
    }

    private void resetState() {
        clearForm();

        txt_tenQuyen.setEditable(false);
        txt_tenQuyen.setEnabled(true);
        txt_moTa.setEditable(false);
        txt_moTa.setEnabled(true);

        cmb_trangThai.setEditable(false);
        cmb_trangThai.setEnabled(false);

        btn_luu.setVisible(false);
        btn_huy.setVisible(false);

        isAdding = false;
        isUpdating = false;
    }

    private void clearForm() {
        txt_maQuyen.setText("");
        txt_tenQuyen.setText("");
        txt_moTa.setText("");

        cmb_trangThai.setSelectedIndex(0);
    }

    public boolean kiemTraHopLe(PhanQuyen_DTO quyen) {
        if (quyen.getTenQuyen().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên quyền không được để trống");
            return false;
        }
        if (quyen.getMoTa().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mô tả không được để trống");
            return false;
        }
        if (quyen.getTrangThai() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn trạng thái");
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelPhanQuyen = new JPanel();
        panelPhanQuyen.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(10, 5, 10, 5), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelPhanQuyen.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 50), null, new Dimension(-1, 150), 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        label_tieuDe = new JLabel();
        Font label_tieuDeFont = this.$$$getFont$$$("Segoe UI", Font.BOLD, 24, label_tieuDe.getFont());
        if (label_tieuDeFont != null) label_tieuDe.setFont(label_tieuDeFont);
        label_tieuDe.setText("QUẢN LÝ QUYỀN");
        panel2.add(label_tieuDe, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btn_xuatExcel = new JButton();
        btn_xuatExcel.setText("Xuất excel");
        panel2.add(btn_xuatExcel, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btn_nhapExcel = new JButton();
        btn_nhapExcel.setText("Nhập excel");
        panel2.add(btn_nhapExcel, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        panel_boLoc = new JPanel();
        panel_boLoc.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 5, new Insets(10, 10, 10, 10), -1, -1));
        panel1.add(panel_boLoc, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel_boLoc.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Bộ lọc", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel_boLoc.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        txt_ndTimKiem = new JTextField();
        panel_boLoc.add(txt_ndTimKiem, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btn_timKiem = new JButton();
        btn_timKiem.setText("Tìm kiếm");
        panel_boLoc.add(btn_timKiem, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btn_thoat = new JButton();
        btn_thoat.setText("Thoát");
        panel_boLoc.add(btn_thoat, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmb_locTrangThai = new JComboBox();
        panel_boLoc.add(cmb_locTrangThai, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelPhanQuyen.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Danh sách quyền", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        src_dsQuyen = new JScrollPane();
        panel3.add(src_dsQuyen, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table_dsQuyen = new JTable();
        table_dsQuyen.setRequestFocusEnabled(true);
        src_dsQuyen.setViewportView(table_dsQuyen);
        panel_thongTinChiTiet = new JPanel();
        panel_thongTinChiTiet.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelPhanQuyen.add(panel_thongTinChiTiet, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel_thongTinChiTiet.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btn_capNhat = new JButton();
        btn_capNhat.setText("Cập nhật");
        panel4.add(btn_capNhat, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        panel4.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btn_them = new JButton();
        btn_them.setText("Thêm");
        panel4.add(btn_them, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(10, 10, 10, 10), -1, -1));
        panel_thongTinChiTiet.add(panel5, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Thông tin chi tiết", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel6, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        label_maQuyen = new JLabel();
        label_maQuyen.setText("Mã quyền:");
        panel6.add(label_maQuyen, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        label_tenQuyen = new JLabel();
        label_tenQuyen.setText("Tên quyền:");
        panel6.add(label_tenQuyen, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        label_trangThai = new JLabel();
        label_trangThai.setText("Trạng thái:");
        panel6.add(label_trangThai, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txt_maQuyen = new JTextField();
        txt_maQuyen.setEditable(false);
        txt_maQuyen.setEnabled(true);
        panel6.add(txt_maQuyen, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txt_tenQuyen = new JTextField();
        txt_tenQuyen.setEditable(false);
        txt_tenQuyen.setEnabled(true);
        panel6.add(txt_tenQuyen, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        cmb_trangThai = new JComboBox();
        cmb_trangThai.setEnabled(false);
        panel6.add(cmb_trangThai, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel7, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("");
        panel7.add(label1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel8, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btn_luu = new JButton();
        btn_luu.setText("Lưu");
        btn_luu.setVisible(false);
        panel8.add(btn_luu, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        panel8.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btn_huy = new JButton();
        btn_huy.setActionCommand("Hủy");
        btn_huy.setText("Hủy");
        btn_huy.setVisible(false);
        panel8.add(btn_huy, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        src_moTa = new JScrollPane();
        panel7.add(src_moTa, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, new Dimension(400, -1), 0, false));
        txt_moTa = new JTextArea();
        txt_moTa.setEditable(false);
        txt_moTa.setEnabled(true);
        txt_moTa.setLineWrap(true);
        txt_moTa.setWrapStyleWord(true);
        src_moTa.setViewportView(txt_moTa);
        label_moTa = new JLabel();
        label_moTa.setText("Mô tả");
        panel7.add(label_moTa, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("");
        panel5.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelPhanQuyen;
    }
}
