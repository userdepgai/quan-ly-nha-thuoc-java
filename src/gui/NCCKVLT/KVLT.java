package gui.NCCKVLT;

import DAO.KhuVucLuuTru_DAO;
import dto.KhuVucLuuTru_DTO;
import BUS.KhuVucLuuTru_BUS;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Locale;

public class KVLT extends JPanel {

    private JButton btnXuat;
    private JButton btnNhap;
    private JTextField textLoc;
    private JComboBox comboBoxTrangThai;
    private JButton btnTimKiem;
    private JButton btnThoat;
    private JTable tableDanhSach;
    private JTextField textMa;
    private JTextField textTen;
    private JTextField textSucChua;
    private JTextField textHienCo;
    private JTextField textDCHI;
    private JComboBox comboBoxTthai;
    private JTable tableChiTiet;
    private JButton btnCapNhat;
    private JButton btnThemKV;
    private JPanel panelMain;
    private DefaultTableModel modelKVLT;

    private KhuVucLuuTru_BUS bus = KhuVucLuuTru_BUS.getInstance();

    public KVLT() {
        $$$setupUI$$$();
        this.setLayout(new BorderLayout());
        if (panelMain != null) {
            this.add(panelMain, BorderLayout.CENTER);
        }

        setupTableData();
        loadDataToTableKVLT();
        bus.refreshData();
        setKhoaForm(true);
        addEvents();
    }

    private void setupTableData() {
        String[] columns = {
                "STT", "Mã KV", "Tên Khu Vực", "Sức Chứa", "Hiện Có", "Ngày Lập", "Địa Chỉ", "Trạng Thái"
        };

        modelKVLT = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa trực tiếp trên bảng
            }
        };

        if (tableDanhSach != null) {
            tableDanhSach.setModel(modelKVLT);
            setupTableProperties(tableDanhSach);
        }

        String[] colChiTiet = {"STT", "Mã Sản Phẩm", "Tên Sản Phẩm", "Số Lượng", "Lô Hàng"};
        DefaultTableModel model2 = new DefaultTableModel(colChiTiet, 0);
        if (tableChiTiet != null) {
            tableChiTiet.setModel(model2);
        }
    }

    public void loadDataToTableKVLT() {
        if (modelKVLT == null) return;
        modelKVLT.setRowCount(0);
        bus.refreshData();
        ArrayList<KhuVucLuuTru_DTO> list = bus.getAll();
        int stt = 1;

        for (KhuVucLuuTru_DTO kv : list) {
            String trangThaiText = "";
            switch (kv.getTrangThai()) {
                case 1:
                    trangThaiText = "Còn trống";
                    break;
                case 2:
                    trangThaiText = "Đã đầy";
                    break;
                case 3:
                    trangThaiText = "Bảo trì";
                    break;
                default:
                    trangThaiText = "Không xác định";
                    break;
            }

            modelKVLT.addRow(new Object[]{
                    stt++,
                    kv.getMaKVLT(),
                    kv.getTenKVLT(),
                    kv.getSucChua(),
                    kv.getHienCo(),
                    kv.getNgayLapKho(),
                    kv.getDiaChi(),
                    trangThaiText
            });
        }
    }

    private void setupTableProperties(JTable table) {
        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true);
        if (table.getColumnCount() > 0) {
            TableColumn sttCol = table.getColumnModel().getColumn(0);
            sttCol.setPreferredWidth(35);
            sttCol.setMaxWidth(50);
            sttCol.setMinWidth(30);
            sttCol.setResizable(false);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            sttCol.setCellRenderer(centerRenderer);
        }
        comboBoxTthai.setModel(new DefaultComboBoxModel<>(new String[]{
                "Còn trống", "Đã đầy", "Bảo trì"
        }));
    }


    private void addEvents() {
        tableDanhSach.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                hienThiChiTiet();
            }
        });

        btnThemKV.addActionListener(e -> themKhuVuc());
        btnCapNhat.addActionListener(e -> capNhatKhuVuc());
        btnTimKiem.addActionListener(e -> timKiemKhuVuc());
        btnThoat.addActionListener(e -> thoatForm());

    }


    private void setKhoaForm(boolean isLocked) {
        textMa.setEditable(!isLocked);
        textTen.setEditable(!isLocked);
        textSucChua.setEditable(!isLocked);
        textHienCo.setEditable(!isLocked);
        textDCHI.setEditable(!isLocked);
        comboBoxTthai.setEnabled(!isLocked);
    }

    private String taoMaKhuVucMoi() {
        ArrayList<KhuVucLuuTru_DTO> list = bus.getAll();
        if (list == null || list.isEmpty()) return "KV001";

        int maxId = 0;
        for (KhuVucLuuTru_DTO kv : list) {
            String ma = kv.getMaKVLT();
            if (ma != null && ma.startsWith("KV") && ma.length() > 2) {
                try {
                    int so = Integer.parseInt(ma.substring(2));
                    if (so > maxId) maxId = so;
                } catch (Exception e) {
                }
            }
        }
        maxId++;
        return String.format("KV%03d", maxId);
    }

    private void lamMoiForm() {
        textMa.setText("");
        textMa.setEditable(true);
        textTen.setText("");
        textSucChua.setText("");
        textHienCo.setText("0");
        textDCHI.setText("");
        comboBoxTthai.setSelectedIndex(0);
        tableDanhSach.clearSelection();
    }

    private void hienThiChiTiet() {
        int selectedRow = tableDanhSach.getSelectedRow();
        if (selectedRow >= 0) {
            textMa.setText(modelKVLT.getValueAt(selectedRow, 1).toString());
            textTen.setText(modelKVLT.getValueAt(selectedRow, 2).toString());
            textSucChua.setText(modelKVLT.getValueAt(selectedRow, 3).toString());
            textHienCo.setText(modelKVLT.getValueAt(selectedRow, 4).toString());

            Object diaChiObj = modelKVLT.getValueAt(selectedRow, 6);
            textDCHI.setText(diaChiObj != null ? diaChiObj.toString() : "");

            String trangThai = modelKVLT.getValueAt(selectedRow, 7).toString();
            if (trangThai.equals("Còn trống")) comboBoxTthai.setSelectedIndex(0);
            else if (trangThai.equals("Đã đầy")) comboBoxTthai.setSelectedIndex(1);
            else comboBoxTthai.setSelectedIndex(2);

            // Khóa form lại để tránh ấn nhầm
            setKhoaForm(true);
            btnThemKV.setText("Thêm ");
            btnThemKV.setEnabled(true);
            btnCapNhat.setText("Cập Nhật ");
            btnCapNhat.setEnabled(true);
        }
    }


    private void themKhuVuc() {
        if (btnThemKV.getText().trim().equals("Thêm")) {
            setKhoaForm(false);
            lamMoiForm();
            textMa.setText(taoMaKhuVucMoi());
            textMa.setEditable(false);

            btnThemKV.setText("Xác nhận Thêm");
            btnCapNhat.setEnabled(false);
        } else {
            try {
                KhuVucLuuTru_DTO kv = new KhuVucLuuTru_DTO();
                kv.setMaKVLT(textMa.getText().trim());
                kv.setTenKVLT(textTen.getText().trim());


                int sucChua = textSucChua.getText().trim().isEmpty() ? 0 : Integer.parseInt(textSucChua.getText().trim());
                kv.setSucChua(sucChua);

                kv.setHienCo(0);
                //kv.setDiaChi(textDCHI.getText().trim());
                kv.setNgayLapKho(new Date(System.currentTimeMillis()));

                int trangThaiSo = comboBoxTthai.getSelectedIndex() + 1;
                kv.setTrangThai(trangThaiSo);
                if (bus.kiemTraHopLe(kv)) {
                    if (bus.insert(kv)) {
                        JOptionPane.showMessageDialog(this, "Thêm khu vực thành công!");
                        loadDataToTableKVLT();
                        lamMoiForm();

                        setKhoaForm(true);
                        btnThemKV.setText("Thêm ");
                        btnCapNhat.setEnabled(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "Thêm thất bại. Vui lòng kiểm tra lại!");
                    }
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Sức chứa phải là con số hợp lệ!");
            }
        }
    }

    private void capNhatKhuVuc() {
        int selectedRow = tableDanhSach.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khu vực cần cập nhật trên bảng!");
            return;
        }

        if (btnCapNhat.getText().trim().equals("Cập Nhật")) {
            setKhoaForm(false);
            textMa.setEditable(false); // Tuyệt đối không cho sửa mã
            btnCapNhat.setText("Xác nhận Sửa");
            btnThemKV.setEnabled(false);
        } else {
            try {
                KhuVucLuuTru_DTO kv = new KhuVucLuuTru_DTO();
                kv.setMaKVLT(textMa.getText().trim());
                kv.setTenKVLT(textTen.getText().trim());
                kv.setSucChua(Integer.parseInt(textSucChua.getText().trim()));
                kv.setHienCo(Integer.parseInt(textHienCo.getText().trim()));
                //kv.setDiaChi(textDCHI.getText().trim());

                Object ngayLapObj = modelKVLT.getValueAt(selectedRow, 5);
                if (ngayLapObj instanceof Date) {
                    kv.setNgayLapKho((Date) ngayLapObj);
                } else {
                    kv.setNgayLapKho(new Date(System.currentTimeMillis()));
                }

                int trangThaiSo = comboBoxTthai.getSelectedIndex() + 1;
                kv.setTrangThai(trangThaiSo);

                if (bus.update(kv)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    loadDataToTableKVLT();

                    setKhoaForm(true);
                    btnCapNhat.setText("Cập Nhật ");
                    btnThemKV.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Sức chứa và Hiện có phải là số nguyên!");
            }
        }
    }

    private void timKiemKhuVuc() {
        String tuKhoa = textLoc.getText().trim().toLowerCase();

        String trangThaiLoc = "";
        if (comboBoxTrangThai.getSelectedItem() != null) {
            trangThaiLoc = comboBoxTrangThai.getSelectedItem().toString();
        }

        if (trangThaiLoc.equals("CON_TRONG")) trangThaiLoc = "Còn trống";
        else if (trangThaiLoc.equals("DAY")) trangThaiLoc = "Đã đầy";
        else if (trangThaiLoc.equals("BAO_TRI")) trangThaiLoc = "Bảo trì";

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelKVLT);
        tableDanhSach.setRowSorter(sorter);

        java.util.List<RowFilter<Object, Object>> filters = new ArrayList<>();

        if (!tuKhoa.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + tuKhoa));
        }

        if (!trangThaiLoc.trim().isEmpty()) {
            filters.add(RowFilter.regexFilter("^" + trangThaiLoc + "$", 7));
        }

        sorter.setRowFilter(RowFilter.andFilter(filters));
    }

    private void thoatForm() {
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thoát?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }


    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new BorderLayout(0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 4, new Insets(5, 5, 5, 0), -1, -1));
        panelMain.add(panel1, BorderLayout.CENTER);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        btnXuat = new JButton();
        btnXuat.setText("Xuất Excel");
        panel2.add(btnXuat, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnNhap = new JButton();
        btnNhap.setText("Nhập Excel");
        panel2.add(btnNhap, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Segoe UI", Font.BOLD, 28, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("QUẢN LÝ KHU VỰC LƯU TRỮ  ");
        panel2.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 7, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(null, "Bộ Lọc", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label2 = new JLabel();
        label2.setText("Lọc Theo:");
        panel3.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(0, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        textLoc = new JTextField();
        textLoc.setText("");
        panel3.add(textLoc, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Trạng Thái:");
        panel3.add(label3, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxTrangThai = new JComboBox();
        panel3.add(comboBoxTrangThai, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        btnTimKiem = new JButton();
        btnTimKiem.setText("Tìm Kiếm");
        panel3.add(btnTimKiem, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnThoat = new JButton();
        btnThoat.setText("Thoát");
        panel3.add(btnThoat, new com.intellij.uiDesigner.core.GridConstraints(0, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 400), null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "DANH SÁCH KHU VỰC LƯU TRỮ  ", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        tableDanhSach = new JTable();
        tableDanhSach.setPreferredScrollableViewportSize(new Dimension(450, 600));
        scrollPane1.setViewportView(tableDanhSach);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder(null, "Thông Tin ", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label4 = new JLabel();
        label4.setText("Mã KVLT:");
        panel5.add(label4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textMa = new JTextField();
        panel5.add(textMa, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Tên KVLT:");
        panel5.add(label5, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textTen = new JTextField();
        textTen.setText("");
        panel5.add(textTen, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Sức Chứa");
        panel5.add(label6, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textSucChua = new JTextField();
        panel5.add(textSucChua, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Hiện có:");
        panel5.add(label7, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textHienCo = new JTextField();
        panel5.add(textHienCo, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Địa Chỉ: ");
        panel5.add(label8, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textDCHI = new JTextField();
        panel5.add(textDCHI, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Trạng Thái");
        panel5.add(label9, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxTthai = new JComboBox();
        panel5.add(comboBoxTthai, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel6, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel6.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane2.setBorder(BorderFactory.createTitledBorder(null, "Danh Sách Sản Phẩm", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        tableChiTiet = new JTable();
        scrollPane2.setViewportView(tableChiTiet);
        btnCapNhat = new JButton();
        btnCapNhat.setText("Cập Nhật ");
        panel1.add(btnCapNhat, new com.intellij.uiDesigner.core.GridConstraints(4, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnThemKV = new JButton();
        btnThemKV.setText("Thêm ");
        panel1.add(btnThemKV, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
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
        return panelMain;
    }

    public JPanel getPanelMain() {
        return panelMain;
    }


}