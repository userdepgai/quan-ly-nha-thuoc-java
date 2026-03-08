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
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Locale;

public class DanhMuc_GUI extends JPanel {
    // --- KHAI BÁO BIẾN GIAO DIỆN ---
    private JPanel panelDanhMucSanPham;
    private JLabel labelDanhMucSanPham;
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
    private JPopupMenu popupGoiY = new JPopupMenu();

    // --- BIẾN LOGIC ---
    private DanhMuc_BUS dmBUS = DanhMuc_BUS.getInstance();
    private final ThuocTinhDanhMuc_BUS ttBUS = ThuocTinhDanhMuc_BUS.getInstance();
    private boolean isAdding = false;
    private boolean isUpdating = false;


    public DanhMuc_GUI() {
        this.setLayout(new BorderLayout());
        if (panelDanhMucSanPham != null) {
            this.add(panelDanhMucSanPham, BorderLayout.CENTER);
        }

        initTable_DanhMuc();
        initTable_ThuocTinh();
        initComboBoxData();
        loadDataToTable(dmBUS.getAll());
        addEvents();

        // Khởi tạo ở chế độ xem
        setViewMode();
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
        String[] headers = {"STT", "Mã Thuộc Tính", "Tên Thuộc Tính", "Danh Mục"};
        modelThuocTinh = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableThuocTinh.setModel(modelThuocTinh);
    }

    private void initComboBoxData() {
        // Form nhập liệu
        cmbTrangThai.setModel(new DefaultComboBoxModel<>(new String[]{
                DanhMuc_DTO.HOAT_DONG,
                DanhMuc_DTO.NGUNG_HOAT_DONG
        }));

        // Bộ lọc trạng thái
        cmbLocTrangThai.setModel(new DefaultComboBoxModel<>(new String[]{
                "Tất cả",
                DanhMuc_DTO.HOAT_DONG,
                DanhMuc_DTO.NGUNG_HOAT_DONG
        }));

        // Bộ lọc tiêu chí tìm kiếm
        cmbTimTheo.setModel(new DefaultComboBoxModel<>(new String[]{
                "Tất cả", "Mã danh mục", "Tên danh mục"
        }));
    }

    private void loadDataToTable(ArrayList<DanhMuc_DTO> list) {
        modelDanhMuc.setRowCount(0);
        int stt = 1;
        for (DanhMuc_DTO dm : list) {
            modelDanhMuc.addRow(new Object[]{
                    stt++,
                    dm.getMaDM(),
                    dm.getTenDM(),
                    dm.getTrangThaiText()
            });
        }
        if (txtDanhMucHienCo != null) txtDanhMucHienCo.setText(String.valueOf(list.size()));
    }

    // --- LOAD DỮ LIỆU THUỘC TÍNH THEO DANH MỤC ---
    private void loadDataToTable_ThuocTinh(String maDM) {
        modelThuocTinh.setRowCount(0); // Xóa dữ liệu cũ trên bảng thuộc tính

        // Lấy danh sách thuộc tính thuộc danh mục này từ BUS
        ArrayList<ThuocTinhDanhMuc_DTO> dsTT = dmBUS.getThuocTinhByMaDM(maDM);

        int stt = 1;
        for (ThuocTinhDanhMuc_DTO tt : dsTT) {
            // Chuyển đổi kiểu thuộc tính (0 -> Combobox, 1 -> Nhập giá trị)

            modelThuocTinh.addRow(new Object[]{
                    stt++,
                    tt.getMaThuocTinh(),
                    tt.getTenThuocTinh(),
                    tt.getMaDM()
            });
        }
    }

    // --- XỬ LÝ SỰ KIỆN ---
    private void addEvents() {
        tableDanhMuc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Không cho phép click dòng khác khi đang nhập liệu
                if (isAdding || isUpdating) return;

                int row = tableDanhMuc.getSelectedRow();
                if (row == -1) return;
                String maDM = tableDanhMuc.getValueAt(row, 1).toString();
                DanhMuc_DTO dm = dmBUS.getById(maDM);
                if (dm != null) {
                    fillForm(dm);
                    loadDataToTable_ThuocTinh(maDM);
                }
            }
        });

        btnThem.addActionListener(e -> setAddMode());

        btnCapNhat.addActionListener(e -> {
            if (tableDanhMuc.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục cần sửa!");
                return;
            }
            setUpdateMode();
        });

        btnLuu.addActionListener(e -> xuLyLuu());

        btnHuy.addActionListener(e -> {
            setViewMode();
            int row = tableDanhMuc.getSelectedRow();
            if (row >= 0) fillForm(dmBUS.getById(tableDanhMuc.getValueAt(row, 1).toString()));
            else xoaTrangForm();
        });

        txtNhapThongTin.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() != java.awt.event.KeyEvent.VK_UP && e.getKeyCode() != java.awt.event.KeyEvent.VK_DOWN) {
                    thucHienLoc();
                    hienThiGoiY();
                }
            }
        });

        ActionListener locAction = e -> thucHienLoc();
        cmbTimTheo.addActionListener(locAction);
        cmbLocTrangThai.addActionListener(locAction);
        btnTimKiem.addActionListener(locAction);

        btnThoat.addActionListener(e -> {
            txtNhapThongTin.setText("");
            cmbTimTheo.setSelectedIndex(0);
            cmbLocTrangThai.setSelectedIndex(0);
            loadDataToTable(dmBUS.getAll());
        });
    }

    private void setViewMode() {
        isAdding = false;
        isUpdating = false;
        lockForm(true);
    }

    private void setAddMode() {
        isAdding = true;
        isUpdating = false;
        xoaTrangForm();
        txtMaDanhMuc.setText(dmBUS.getNextId());
        lockForm(false);
        txtTenDanhMuc.requestFocus();
    }

    private void setUpdateMode() {
        isAdding = false;
        isUpdating = true;
        lockForm(false);
        txtTenDanhMuc.requestFocus();
    }

    private void thucHienLoc() {
        String keyword = txtNhapThongTin.getText();
        String timTheo = (String) cmbTimTheo.getSelectedItem();

        String ttStr = (String) cmbLocTrangThai.getSelectedItem();
        Integer trangThai = ttStr.equals("Tất cả") ? null : DanhMuc_DTO.parseTrangThaiFromText(ttStr);

        ArrayList<DanhMuc_DTO> dsLoc = dmBUS.timKiemNangCao(keyword, timTheo, trangThai);
        loadDataToTable(dsLoc);
    }

    private void hienThiGoiY() {
        popupGoiY.setVisible(false);
        popupGoiY.removeAll();
        String textInput = txtNhapThongTin.getText().trim();
        if (textInput.isEmpty()) return;

        String timTheo = (String) cmbTimTheo.getSelectedItem();
        ArrayList<DanhMuc_DTO> dsGoiY = dmBUS.timKiemNangCao(textInput, timTheo, null);

        if (dsGoiY.isEmpty()) return;

        for (int i = 0; i < Math.min(dsGoiY.size(), 5); i++) {
            DanhMuc_DTO dm = dsGoiY.get(i);
            JMenuItem item = new JMenuItem(dm.getMaDM() + " - " + dm.getTenDM());
            item.addActionListener(e -> {
                // Nếu tìm tên thì điền tên, ngược lại (Mã hoặc Tất cả) thì điền Mã
                if (timTheo.equals("Tên danh mục")) {
                    txtNhapThongTin.setText(dm.getTenDM());
                } else {
                    txtNhapThongTin.setText(dm.getMaDM());
                }
                thucHienLoc();
                popupGoiY.setVisible(false);
            });
            popupGoiY.add(item);
        }
        popupGoiY.show(txtNhapThongTin, 0, txtNhapThongTin.getHeight());
        txtNhapThongTin.requestFocus();
    }

    // --- LOGIC NGHIỆP VỤ ---
    private void xuLyLuu() {
        if (txtTenDanhMuc.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên danh mục không được để trống!");
            return;
        }

        DanhMuc_DTO dm = new DanhMuc_DTO();
        dm.setMaDM(txtMaDanhMuc.getText());
        dm.setTenDM(txtTenDanhMuc.getText());
        dm.setTrangThai(DanhMuc_DTO.parseTrangThaiFromText((String) cmbTrangThai.getSelectedItem()));

        boolean result = false;
        if (isAdding) {
            result = dmBUS.them(dm);
        } else if (isUpdating) {
            result = dmBUS.capNhat(dm);
        }

        if (result) {
            JOptionPane.showMessageDialog(this, "Thao tác thành công!");
            loadDataToTable(dmBUS.getAll());
            setViewMode();
        } else {
            JOptionPane.showMessageDialog(this, "Thao tác thất bại!");
        }
    }

    private void fillForm(DanhMuc_DTO dm) {
        txtMaDanhMuc.setText(dm.getMaDM());
        txtTenDanhMuc.setText(dm.getTenDM());
        cmbTrangThai.setSelectedItem(dm.getTrangThaiText()); // Gọi text từ DTO
    }

    private void xoaTrangForm() {
        txtMaDanhMuc.setText("");
        txtTenDanhMuc.setText("");
        cmbTrangThai.setSelectedIndex(0);
    }

    // Hàm khóa/mở khóa các component
    private void lockForm(boolean lock) {
        // lock = true: Chế độ xem (Khóa form), lock = false: Chế độ nhập (Mở form)
        txtTenDanhMuc.setEditable(!lock);
        cmbTrangThai.setEnabled(!lock);
        txtMaDanhMuc.setEditable(false);

        // Ẩn hiện nút Lưu/Hủy theo yêu cầu
        btnLuu.setVisible(!lock);
        btnHuy.setVisible(!lock);

        // Bật/tắt nút Thêm/Sửa/Bảng
        btnThem.setEnabled(lock);
        btnCapNhat.setEnabled(lock);
        tableDanhMuc.setEnabled(lock);
    }

}