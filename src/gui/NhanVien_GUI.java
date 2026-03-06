package gui;

import javax.swing.*;
import java.awt.*;
import bus.NhanVien_BUS;
import com.toedter.calendar.JDateChooser;
import dto.NhanVien_DTO;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
public class NhanVien_GUI extends JPanel {
    private JPanel panel_nhanVien;
    private JButton btnXuatExcel;
    private JButton btnNhapExcel;
    private JButton btnTimKiem;
    private JButton btn_thoat;
    private JComboBox cmb_locTrangThai;
    private JTable table1;
    private JPanel panelThongTin;
    private JTextField txt_maNV;
    private JTextField txt_tenNV;
    private JButton btn_capNhat;
    private JButton btn_them;
    private JTextField txt_diaChi;
    private JTextField txt_sdt;
    private JRadioButton rd_nu;
    private JRadioButton rd_nam;
    private JTextField textField4;
    private JTextField txt_luongCoBan;
    private JButton btn_luu;
    private JButton btn_huy;
    private JDateChooser JDate_ngayVaoLam;
    private JDateChooser JDate_ngaySinh;
    private JComboBox cmb_timKiem;
    private JComboBox cmb_chucVu;
    private JComboBox cmb_trangThai;
    private boolean isAdding = false;
    private boolean isUpdating = false;
    private DefaultTableModel model;
    private ButtonGroup groupGioiTinh;
    private JComboBox<String> cmb_chonChucVu;
    private JComboBox<String> cmb_chonTrangThai;
    private JTextField txt_namSinh;
    private NhanVien_BUS bus = NhanVien_BUS.getInstance();
    public NhanVien_GUI() {
        this.setLayout(new BorderLayout());
        this.add(panel_nhanVien, BorderLayout.CENTER);
        cmb_timKiem.setEditable(true);
        initTable();
        initComponent();
        loadTable(bus.getAll());
        setViewMode();
        xuLySuKien();
    }

    // ================= INIT =================
    private void initComponent() {

        groupGioiTinh = new ButtonGroup();
        groupGioiTinh.add(rd_nam);
        groupGioiTinh.add(rd_nu);

        cmb_trangThai.setModel(new DefaultComboBoxModel<>(
                new String[]{"Đang làm", "Nghỉ"}
        ));

        cmb_chucVu.setModel(new DefaultComboBoxModel<>(
                new String[]{"Quản lý hệ thống", "Nhân viên bán hàng", "Nhân viên quản lý kho"}
        ));
        cmb_chonChucVu.setModel(new DefaultComboBoxModel<>(
                new String[]{
                        "--chọn chức vụ--",
                        "Quản lý hệ thống",
                        "Nhân viên bán hàng",
                        "Nhân viên quản lý kho"
                }
        ));

        cmb_chonTrangThai.setModel(new DefaultComboBoxModel<>(
                new String[]{
                        "--chọn trạng thái--",
                        "Đang làm",
                        "Nghỉ"
                }
        ));
        txt_namSinh.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '-' && c != '\b') {
                    e.consume();
                }
            }
        });
    }

    private void initTable() {
        cmb_locTrangThai.setModel(new DefaultComboBoxModel<>(
                new String[]{
                        "Tất cả",
                        "Mã NV",
                        "Tên NV",
                        "SĐT"
                }
        ));
        String[] cols = {
                "STT",
                "Mã NV", "Tên NV", "SĐT",
                "Ngày sinh", "Giới tính",
                "Chức vụ", "Lương",
                "Ngày vào làm", "Trạng thái"
        };

        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table1.setModel(model);
        // ===== TẮT GIÃN ĐỀU =====
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        // ===== SET ĐỘ RỘNG CỘT =====
        table1.getColumnModel().getColumn(0).setPreferredWidth(50);   // STT
        table1.getColumnModel().getColumn(1).setPreferredWidth(90);   // Mã NV
        table1.getColumnModel().getColumn(2).setPreferredWidth(160);  // Tên NV
        table1.getColumnModel().getColumn(3).setPreferredWidth(110);  // SĐT
        table1.getColumnModel().getColumn(4).setPreferredWidth(110);  // Ngày sinh
        table1.getColumnModel().getColumn(5).setPreferredWidth(80);   // Giới tính
        table1.getColumnModel().getColumn(6).setPreferredWidth(160);  // Chức vụ
        table1.getColumnModel().getColumn(7).setPreferredWidth(120);  // Lương
        table1.getColumnModel().getColumn(8).setPreferredWidth(110);  // Ngày vào làm
        table1.getColumnModel().getColumn(9).setPreferredWidth(90);   // Trạng thái
    }
    // ================= LOAD TABLE =================
    private void loadTable(ArrayList<NhanVien_DTO> list) {

        model.setRowCount(0);

        int stt = 1;

        for (NhanVien_DTO nv : list) {
            model.addRow(new Object[]{
                    stt++,
                    nv.getMa(),
                    nv.getTen(),
                    nv.getSdt(),
                    nv.getNgaySinh(),
                    nv.isGioiTinh() ? "Nam" : "Nữ",
                    nv.getChucVu(),
                    String.format("%,.0f", nv.getLuongCoBan()),
                    nv.getNgayVaoLam(),
                    nv.getTrangThai() == 0 ? "Đang làm" : "Nghỉ"
            });
        }

    }

    // ================= GET FORM DATA =================
    private NhanVien_DTO getFormData(String ma) {

        try {
            String ten = txt_tenNV.getText().trim();
            String sdt = txt_sdt.getText().trim();

            // ====== VALIDATE NGÀY SINH yyyy-MM-dd ======
            String ngaySinhText = txt_namSinh.getText().trim();
            if (!ngaySinhText.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this,
                        "Ngày sinh phải đúng định dạng yyyy-MM-dd");
                return null;
            }

            LocalDate ngaySinh = LocalDate.parse(ngaySinhText);

            boolean gioiTinh = rd_nam.isSelected();

            String chucVu = cmb_chucVu.getSelectedItem().toString();

            LocalDate ngayVaoLam = JDate_ngayVaoLam.getDate()
                    .toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();

            double luong = Double.parseDouble(
                    txt_luongCoBan.getText().replace(",", "")
            );

            int trangThai = cmb_trangThai.getSelectedIndex();

            return new NhanVien_DTO(
                    ma, ten, sdt,
                    ngaySinh, gioiTinh,
                    chucVu, ngayVaoLam,
                    luong, trangThai,
                    null
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Dữ liệu không hợp lệ hoặc ngày không tồn tại!");
            return null;
        }
    }

    // ================= CLEAR =================
    private void clearForm() {

        txt_maNV.setText("");
        txt_tenNV.setText("");
        txt_sdt.setText("");
        txt_luongCoBan.setText("");
        txt_namSinh.setText("");
        txt_diaChi.setText("");
        JDate_ngayVaoLam.setDate(null);

        rd_nu.setSelected(true);
        cmb_chucVu.setSelectedIndex(0);
        cmb_trangThai.setSelectedIndex(0);
        }
    private void lockTable() {
        table1.setRowSelectionAllowed(false);
        table1.setEnabled(false);
    }
    private void setViewMode() {

        unlockTable();

        txt_maNV.setEditable(false);
        txt_tenNV.setEditable(false);
        txt_sdt.setEditable(false);
        txt_diaChi.setEditable(false);
        txt_luongCoBan.setEditable(false);
        txt_namSinh.setEditable(false);

        rd_nam.setEnabled(false);
        rd_nu.setEnabled(false);

        cmb_chucVu.setEnabled(false);
        cmb_trangThai.setEnabled(false);

        JDate_ngayVaoLam.setEnabled(false);

        btn_luu.setVisible(false);
        btn_huy.setVisible(false);

        btn_them.setEnabled(true);
        btn_capNhat.setEnabled(true);
    }
    private void setUpdateMode() {

        lockTable();

        txt_tenNV.setEditable(true);
        txt_sdt.setEditable(true);
        txt_diaChi.setEditable(true);
        txt_luongCoBan.setEditable(true);
        txt_namSinh.setEditable(true);

        rd_nam.setEnabled(true);
        rd_nu.setEnabled(true);

        cmb_chucVu.setEnabled(true);
        cmb_trangThai.setEnabled(true);

        JDate_ngayVaoLam.setEnabled(true);

        btn_luu.setVisible(true);
        btn_huy.setVisible(true);

        btn_them.setEnabled(false);
        btn_capNhat.setEnabled(false);
    }
    private void unlockTable() {
        table1.setRowSelectionAllowed(true);
        table1.setEnabled(true);
    }
    private void resetState() {

        clearForm();

        btn_luu.setVisible(false);
        btn_huy.setVisible(false);
        txt_maNV.setEditable(true);
        isAdding = false;
        isUpdating = false;
    }

    // ================= EVENTS =================
    private void xuLySuKien() {
        JTextField editor = (JTextField) cmb_timKiem.getEditor().getEditorComponent();

        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {

                String text = editor.getText().toLowerCase();
                String loai = cmb_locTrangThai.getSelectedItem().toString();

                DefaultComboBoxModel<String> newModel = new DefaultComboBoxModel<>();

                for (NhanVien_DTO nv : NhanVien_BUS.getInstance().getAll()) {

                    switch (loai) {

                        case "Mã NV":
                            if (nv.getMa().toLowerCase().contains(text))
                                newModel.addElement(nv.getMa());
                            break;

                        case "Tên NV":
                            if (nv.getTen().toLowerCase().contains(text))
                                newModel.addElement(nv.getTen());
                            break;

                        case "SĐT":
                            if (nv.getSdt().contains(text))
                                newModel.addElement(nv.getSdt());
                            break;

                        case "Tất cả":
                            if (nv.getMa().toLowerCase().contains(text))
                                newModel.addElement(nv.getMa());

                            if (nv.getTen().toLowerCase().contains(text))
                                newModel.addElement(nv.getTen());

                            if (nv.getSdt().contains(text))
                                newModel.addElement(nv.getSdt());
                            break;
                    }
                }

                cmb_timKiem.setModel(newModel);
                cmb_timKiem.setSelectedItem(text);
                cmb_timKiem.showPopup();
            }
        });
        // CLICK TABLE
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int row = table1.getSelectedRow();
                if (row < 0) return;

                txt_maNV.setText(model.getValueAt(row, 1).toString());
                txt_tenNV.setText(model.getValueAt(row, 2).toString());
                txt_sdt.setText(model.getValueAt(row, 3).toString());

                try {
                    txt_namSinh.setText(model.getValueAt(row, 4).toString());

                    java.sql.Date nvl =
                            java.sql.Date.valueOf(model.getValueAt(row, 8).toString());
                    JDate_ngayVaoLam.setDate(nvl);

                } catch (Exception ex) {
                    JDate_ngaySinh.setDate(null);
                    JDate_ngayVaoLam.setDate(null);
                }

                if (model.getValueAt(row, 5).toString().equals("Nam"))
                    rd_nam.setSelected(true);
                else
                    rd_nu.setSelected(true);

                cmb_chucVu.setSelectedItem(model.getValueAt(row, 6).toString());
                txt_luongCoBan.setText(model.getValueAt(row, 7).toString());
                cmb_trangThai.setSelectedItem(model.getValueAt(row, 9).toString());

                if (row < 0) return;

                NhanVien_DTO nv = bus.getAll().get(row);

                String maDC = nv.getMaDiaChi();

                String diaChi = NhanVien_BUS.getInstance().getDiaChiByMaDC(maDC);

                txt_diaChi.setText(diaChi);
            }
        });
        // đổi loại tìm kiếm
        cmb_locTrangThai.addActionListener(e -> {
            String loai = cmb_locTrangThai.getSelectedItem().toString();
            loadSuggestData(loai);
        });
        btnTimKiem.addActionListener(e -> {
            loadTable(timKiem());
        });
        // THÊM
        btn_them.addActionListener(e -> {

            isAdding = true;
            isUpdating = false;

            String ma = bus.getNextId();
            txt_maNV.setText(ma);
            txt_maNV.setEditable(false);
            setUpdateMode();
            btn_luu.setVisible(true);
            btn_huy.setVisible(true);
        });

        // CẬP NHẬT
        btn_capNhat.addActionListener(e -> {

            if (table1.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this,"Vui lòng chọn nhân viên");
                return;
            }

            isUpdating = true;
            isAdding = false;

            setUpdateMode();
        });
        // ===== LƯU =====
        btn_luu.addActionListener(e -> {

            String ma = txt_maNV.getText();
            if (ma.isEmpty()) return;

            NhanVien_DTO nv = getFormData(ma);
            if (nv == null) return;

            boolean result = false;

            if (isAdding) {
                result = bus.them(nv);
            }

            if (isUpdating) {
                result = bus.capNhat(nv);
            }

            if (result) {
                JOptionPane.showMessageDialog(this, "Thành công");
                loadTable(bus.getAll());
                resetState();
                setViewMode();   // THÊM
            }
        });

// ===== HỦY =====
        btn_huy.addActionListener(e -> {
            resetState();
            setViewMode();
        });
        // THOÁT
        btn_thoat.addActionListener(e -> {

            cmb_chonTrangThai.setSelectedIndex(0);
            cmb_chonChucVu.setSelectedIndex(0);

            ((JTextField)cmb_timKiem.getEditor()
                    .getEditorComponent()).setText("");

            loadTable(bus.getAll());
        });
    }

    private void createUIComponents() {

        JDate_ngaySinh = new com.toedter.calendar.JDateChooser();
        JDate_ngaySinh.setDateFormatString("yyyy-MM-dd");

        JDate_ngayVaoLam = new com.toedter.calendar.JDateChooser();
        JDate_ngayVaoLam.setDateFormatString("yyyy-MM-dd");
    }
    private void loadSuggestData(String loai) {

        DefaultComboBoxModel<String> modelSuggest = new DefaultComboBoxModel<>();

        for (NhanVien_DTO nv : bus.getAll()) {

            switch (loai) {

                case "Mã NV":
                    modelSuggest.addElement(nv.getMa());
                    break;

                case "Tên NV":
                    modelSuggest.addElement(nv.getTen());
                    break;

                case "SĐT":
                    modelSuggest.addElement(nv.getSdt());
                    break;

                case "Tất cả":
                    modelSuggest.addElement(nv.getMa());
                    modelSuggest.addElement(nv.getTen());
                    modelSuggest.addElement(nv.getSdt());
                    break;
            }
            cmb_timKiem.setModel(modelSuggest);
            cmb_timKiem.setSelectedItem(null);
            ((JTextField) cmb_timKiem.getEditor().getEditorComponent()).setText("");
        }

        cmb_timKiem.setModel(modelSuggest);
    }
    private ArrayList<NhanVien_DTO> timKiem() {

        String keyword = ((JTextField)cmb_timKiem.getEditor().getEditorComponent())
                .getText().trim();

        String chucVu = null;
        if(cmb_chonChucVu.getSelectedIndex() > 0){
            chucVu = cmb_chonChucVu.getSelectedItem().toString();
        }

        Integer trangThai = null;
        if(cmb_chonTrangThai.getSelectedIndex() == 1){
            trangThai = 0; // đang làm
        }
        else if(cmb_chonTrangThai.getSelectedIndex() == 2){
            trangThai = 1; // nghỉ
        }

        return bus.timKiem(keyword, chucVu, trangThai);
    }

}