package gui;

import bus.DanhMuc_BUS;
import bus.ThuocTinhDanhMuc_BUS;
import dto.DanhMuc_DTO;
import dto.ThuocTinhDanhMuc_DTO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Locale;

public class DanhMuc_GUI extends JPanel {
    // --- KHAI BÁO BIẾN GIAO DIỆN ---
    private JPanel panelDanhMucSanPham;
    private JLabel labelDanhMucSanPham;
    private JButton btnXuatExcel;
    private JButton btnNhapExcel;
    private JTable tableDanhMuc;
    private JButton btnCapNhat;
    private JButton btnThem;
    private JTable tableThuocTinh; // Đã sửa tên biến cho khớp logic
    private JLabel labelMaDanhMuc;
    private JLabel labelTenDanhMuc;
    private JLabel labelTrangThai;
    private JTextField txtMaDanhMuc;
    private JTextField txtTenDanhMuc;
    private JPanel panelBoLoc;
    private JLabel labelTimKiem;
    private JComboBox<String> cmbTimTheo; // Không dùng nhưng giữ lại theo thiết kế
    private JPanel panelTieuDe;
    private JPanel panelDanhSachDanhMuc;
    private JPanel panelThongTinChiTiet;
    private JPanel panelThongTinDanhMuc;
    private JPanel panelDanhSachThuocTinh;
    private JPanel panelCapNhat;
    private JLabel labelLocTrangThai;
    private JComboBox<String> cmbTrangThai; // Combo nhập liệu
    private JLabel labelDanhMucHienCo;
    private JTextField txtDanhMucHienCo;
    private JComboBox<String> cmbLocTrangThai; // Combo lọc
    private JLabel labelNhapThongTin;
    private JTextField txtNhapThongTin; // Ô tìm kiếm
    private JButton btnTimKiem;
    private JButton btnThoat;
    private JButton btnHuy;
    private JButton btnLuu;

    private DefaultTableModel modelDanhMuc;
    private DefaultTableModel modelThuocTinh;

    // --- BIẾN LOGIC ---
    private DanhMuc_BUS dmBUS = DanhMuc_BUS.getInstance();
    private final ThuocTinhDanhMuc_BUS ttBUS = ThuocTinhDanhMuc_BUS.getInstance();
    private String chucNangHienTai = ""; // "THEM" hoặc "SUA"

    public DanhMuc_GUI() {
        this.setLayout(new BorderLayout());
        if (panelDanhMucSanPham != null) {
            this.add(panelDanhMucSanPham, BorderLayout.CENTER);
        }

        // 1. Khởi tạo bảng
        initTable_DanhMuc();
        initTable_ThuocTinh();

        // 2. Khởi tạo dữ liệu cho ComboBox
        initComboBoxData();

        // 3. Load dữ liệu ban đầu lên bảng
        loadDataToTable(dmBUS.getAll());

        // 4. Gắn sự kiện cho các nút
        addEvents();

        // 5. Khóa form ban đầu (Chỉ cho xem, không cho sửa)
        lockForm(true);
    }

    // --- KHỞI TẠO ---
    private void initTable_DanhMuc() {
        String[] headers = {"STT", "Mã Danh Mục", "Tên Danh Mục", "Trạng Thái"};
        modelDanhMuc = new DefaultTableModel(headers, 0) {
            @Override // Ghi đè để chặn sửa trực tiếp trên ô
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableDanhMuc.setModel(modelDanhMuc);
        tableDanhMuc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void initTable_ThuocTinh() {
        String[] headers = {"STT", "Mã Thuộc Tính", "Tên Thuộc Tính", "Kiểu Thuộc Tính", "Danh Mục"};
        modelThuocTinh = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableThuocTinh.setModel(modelThuocTinh);
    }

    private void initComboBoxData() {
        // Combo cho form nhập liệu
        cmbTrangThai.removeAllItems();
        cmbTrangThai.addItem("Đang hoạt động");
        cmbTrangThai.addItem("Ngừng hoạt động");

        // Combo cho bộ lọc
        cmbLocTrangThai.removeAllItems();
        cmbLocTrangThai.addItem("Tất cả");
        cmbLocTrangThai.addItem("Đang hoạt động");
        cmbLocTrangThai.addItem("Ngừng hoạt động");
    }

    // --- LOAD DỮ LIỆU ---
    private void loadDataToTable(ArrayList<DanhMuc_DTO> list) {
        modelDanhMuc.setRowCount(0); // Xóa hết dữ liệu cũ
        int stt = 1;
        for (DanhMuc_DTO dm : list) {
            modelDanhMuc.addRow(new Object[]{
                    stt++,
                    dm.getMaDM(),
                    dm.getTenDM(),
                    dm.getTrangThai() == 1 ? "Đang hoạt động" : "Ngừng hoạt động"
            });
        }
        // Cập nhật số lượng hiển thị
        if (txtDanhMucHienCo != null) {
            txtDanhMucHienCo.setText(String.valueOf(list.size()));
        }
    }

    // --- LOAD DỮ LIỆU THUỘC TÍNH THEO DANH MỤC ---
    private void loadDataToTable_ThuocTinh(String maDM) {
        modelThuocTinh.setRowCount(0); // Xóa dữ liệu cũ trên bảng thuộc tính

        // Lấy danh sách thuộc tính thuộc danh mục này từ BUS
        ArrayList<ThuocTinhDanhMuc_DTO> dsTT = dmBUS.getThuocTinhByMaDM(maDM);

        int stt = 1;
        for (ThuocTinhDanhMuc_DTO tt : dsTT) {
            // Chuyển đổi kiểu thuộc tính (0 -> Combobox, 1 -> Nhập giá trị)
            String kieuStr = (tt.getKieuThuocTinh() == 0) ? "Combobox (Chọn)" : "Nhập giá trị";

            modelThuocTinh.addRow(new Object[]{
                    stt++,
                    tt.getMaThuocTinh(),
                    tt.getTenThuocTinh(),
                    kieuStr,
                    tt.getMaDM()
            });
        }
    }

    // --- XỬ LÝ SỰ KIỆN ---
    private void addEvents() {
        // 1. Sự kiện Click vào bảng Danh Mục
        tableDanhMuc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableDanhMuc.getSelectedRow();
                if (row == -1) return;

                String maDM = tableDanhMuc.getValueAt(row, 1).toString();

                DanhMuc_DTO dm = dmBUS.getById(maDM);
                if (dm != null) {
                    fillForm(dm);
                    lockForm(true);

                    // --- GỌI HÀM ĐỔ DỮ LIỆU LÊN BẢNG THUỘC TÍNH BÊN PHẢI ---
                    loadDataToTable_ThuocTinh(maDM);
                }
            }
        });

        // 2. Nút THÊM
        btnThem.addActionListener(e -> {
            xoaTrangForm();
            // Tự động sinh mã mới
            txtMaDanhMuc.setText(dmBUS.getNextId());

            chucNangHienTai = "THEM";
            lockForm(false); // Mở khóa để nhập
            txtTenDanhMuc.requestFocus();
        });

        // 3. Nút CẬP NHẬT
        btnCapNhat.addActionListener(e -> {
            if (txtMaDanhMuc.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục cần sửa!");
                return;
            }
            chucNangHienTai = "SUA";
            lockForm(false); // Mở khóa để sửa
            txtTenDanhMuc.requestFocus();
        });

        // 4. Nút LƯU
        btnLuu.addActionListener(e -> xuLyLuu());

        // 5. Nút HỦY
        btnHuy.addActionListener(e -> {
            xoaTrangForm();
            lockForm(true);
            chucNangHienTai = "";
        });

        // 6. Nút TÌM KIẾM
        btnTimKiem.addActionListener(e -> {
            String keyword = txtNhapThongTin.getText();
            Integer trangThai = null;

            // Index 0: Tất cả, 1: Hoạt động, 2: Ngừng
            if (cmbLocTrangThai.getSelectedIndex() == 1) trangThai = 1;
            if (cmbLocTrangThai.getSelectedIndex() == 2) trangThai = 0;

            ArrayList<DanhMuc_DTO> ketQua = dmBUS.timKiem(keyword, trangThai);
            loadDataToTable(ketQua);
        });

        // 7. Nút THOÁT (Hoặc Reset bộ lọc)
        btnThoat.addActionListener(e -> {
            txtNhapThongTin.setText("");
            cmbLocTrangThai.setSelectedIndex(0);
            loadDataToTable(dmBUS.getAll());
        });
    }

    // --- LOGIC NGHIỆP VỤ ---
    private void xuLyLuu() {
        // Thu thập dữ liệu
        DanhMuc_DTO dm = new DanhMuc_DTO();
        dm.setMaDM(txtMaDanhMuc.getText());
        dm.setTenDM(txtTenDanhMuc.getText());
        // Combo: 0 là "Đang hoạt động" (1), 1 là "Ngừng" (0)
        dm.setTrangThai(cmbTrangThai.getSelectedIndex() == 0 ? 1 : 0);

        boolean result = false;
        if (chucNangHienTai.equals("THEM")) {
            result = dmBUS.them(dm);
        } else if (chucNangHienTai.equals("SUA")) {
            result = dmBUS.capNhat(dm);
        }

        if (result) {
            JOptionPane.showMessageDialog(this, "Thao tác thành công!");
            loadDataToTable(dmBUS.getAll()); // Load lại bảng
            xoaTrangForm();
            lockForm(true);
        }
    }

    // --- HÀM HỖ TRỢ ---
    private void fillForm(DanhMuc_DTO dm) {
        txtMaDanhMuc.setText(dm.getMaDM());
        txtTenDanhMuc.setText(dm.getTenDM());
        // 1 -> index 0, 0 -> index 1
        cmbTrangThai.setSelectedIndex(dm.getTrangThai() == 1 ? 0 : 1);
    }

    private void xoaTrangForm() {
        txtMaDanhMuc.setText("");
        txtTenDanhMuc.setText("");
        cmbTrangThai.setSelectedIndex(0);
    }

    // Hàm khóa/mở khóa các component
    private void lockForm(boolean lock) {
        // Nếu lock = true (chế độ xem): Không cho sửa, ẩn nút Lưu/Hủy, hiện nút Thêm/Sửa
        txtTenDanhMuc.setEditable(!lock);
        cmbTrangThai.setEnabled(!lock);

        // Mã luôn luôn khóa (readonly)
        txtMaDanhMuc.setEditable(false);

        btnThem.setEnabled(lock);
        btnCapNhat.setEnabled(lock);

        btnLuu.setEnabled(!lock);
        btnHuy.setEnabled(!lock);

        // Khóa bảng khi đang sửa để tránh click lung tung
        tableDanhMuc.setEnabled(lock);
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
        panelDanhMucSanPham = new JPanel();
        panelDanhMucSanPham.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 3, new Insets(10, 5, 10, 5), -1, -1));
        panelTieuDe = new JPanel();
        panelTieuDe.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panelDanhMucSanPham.add(panelTieuDe, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelDanhMucSanPham = new JLabel();
        Font labelDanhMucSanPhamFont = this.$$$getFont$$$("Segoe UI", Font.BOLD, 24, labelDanhMucSanPham.getFont());
        if (labelDanhMucSanPhamFont != null) labelDanhMucSanPham.setFont(labelDanhMucSanPhamFont);
        labelDanhMucSanPham.setText("DANH MỤC SẢN PHẨM");
        panelTieuDe.add(labelDanhMucSanPham, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panelTieuDe.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnXuatExcel = new JButton();
        btnXuatExcel.setText("Xuất Excel");
        panelTieuDe.add(btnXuatExcel, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnNhapExcel = new JButton();
        btnNhapExcel.setText("Nhập Excel");
        panelTieuDe.add(btnNhapExcel, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelBoLoc = new JPanel();
        panelBoLoc.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 6, new Insets(10, 10, 10, 10), -1, -1));
        panelBoLoc.setEnabled(true);
        panelBoLoc.setInheritsPopupMenu(false);
        panelDanhMucSanPham.add(panelBoLoc, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelBoLoc.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Bộ lọc", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        labelTimKiem = new JLabel();
        labelTimKiem.setText("Tìm theo:");
        panelBoLoc.add(labelTimKiem, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmbTimTheo = new JComboBox();
        panelBoLoc.add(cmbTimTheo, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelLocTrangThai = new JLabel();
        labelLocTrangThai.setText("Trạng thái:");
        panelBoLoc.add(labelLocTrangThai, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelDanhMucHienCo = new JLabel();
        labelDanhMucHienCo.setText("Danh mục hiện có:");
        panelBoLoc.add(labelDanhMucHienCo, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtDanhMucHienCo = new JTextField();
        panelBoLoc.add(txtDanhMucHienCo, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        cmbLocTrangThai = new JComboBox();
        panelBoLoc.add(cmbLocTrangThai, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelNhapThongTin = new JLabel();
        labelNhapThongTin.setHorizontalAlignment(10);
        labelNhapThongTin.setText("Nhập thông tin tìm kiếm:");
        panelBoLoc.add(labelNhapThongTin, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNhapThongTin = new JTextField();
        panelBoLoc.add(txtNhapThongTin, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnTimKiem = new JButton();
        btnTimKiem.setEnabled(true);
        btnTimKiem.setText("Tìm kiếm");
        panelBoLoc.add(btnTimKiem, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnThoat = new JButton();
        btnThoat.setText("Thoát");
        panelBoLoc.add(btnThoat, new com.intellij.uiDesigner.core.GridConstraints(1, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelDanhSachDanhMuc = new JPanel();
        panelDanhSachDanhMuc.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelDanhMucSanPham.add(panelDanhSachDanhMuc, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(824, 800), null, 0, false));
        panelDanhSachDanhMuc.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Danh sách Danh mục", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JScrollPane scrollPane1 = new JScrollPane();
        panelDanhSachDanhMuc.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableDanhMuc = new JTable();
        scrollPane1.setViewportView(tableDanhMuc);
        panelThongTinChiTiet = new JPanel();
        panelThongTinChiTiet.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelDanhMucSanPham.add(panelThongTinChiTiet, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(824, 459), null, 0, false));
        panelThongTinDanhMuc = new JPanel();
        panelThongTinDanhMuc.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelThongTinDanhMuc.setEnabled(true);
        panelThongTinChiTiet.add(panelThongTinDanhMuc, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(346, 165), null, 0, false));
        panelThongTinDanhMuc.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Thông tin chi tiết", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelThongTinDanhMuc.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelMaDanhMuc = new JLabel();
        labelMaDanhMuc.setText("Mã danh mục");
        panel1.add(labelMaDanhMuc, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelTenDanhMuc = new JLabel();
        labelTenDanhMuc.setText("Tên danh mục");
        panel1.add(labelTenDanhMuc, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelTrangThai = new JLabel();
        labelTrangThai.setText("Trạng thái");
        panel1.add(labelTrangThai, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtMaDanhMuc = new JTextField();
        panel1.add(txtMaDanhMuc, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtTenDanhMuc = new JTextField();
        panel1.add(txtTenDanhMuc, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        cmbTrangThai = new JComboBox();
        panel1.add(cmbTrangThai, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelDanhSachThuocTinh = new JPanel();
        panelDanhSachThuocTinh.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelThongTinChiTiet.add(panelDanhSachThuocTinh, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelDanhSachThuocTinh.setBorder(BorderFactory.createTitledBorder(null, "Danh sách Thuộc tính", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JScrollPane scrollPane2 = new JScrollPane();
        panelDanhSachThuocTinh.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableThuocTinh = new JTable();
        scrollPane2.setViewportView(tableThuocTinh);
        panelCapNhat = new JPanel();
        panelCapNhat.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 6, new Insets(10, 10, 10, 10), -1, -1));
        panelDanhMucSanPham.add(panelCapNhat, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panelCapNhat.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 6, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnCapNhat = new JButton();
        btnCapNhat.setText("Cập nhật");
        panel2.add(btnCapNhat, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnThem = new JButton();
        btnThem.setText("Thêm");
        panel2.add(btnThem, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelDanhMucSanPham.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnHuy = new JButton();
        btnHuy.setText("Hủy");
        panel3.add(btnHuy, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnLuu = new JButton();
        btnLuu.setText("Lưu");
        panel3.add(btnLuu, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panelDanhMucSanPham.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
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
        return panelDanhMucSanPham;
    }
}