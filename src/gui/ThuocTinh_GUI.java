package gui;

import bus.*;
import dto.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ThuocTinh_GUI extends JPanel {
        private JButton btnCapNhat;
        private JButton btnThem;
        private JPanel panelDanhSachThuocTinh;
        private JTable tableThuocTinh;
        private JPanel panelTieuDe;
        private JLabel label_tieuDe;
        private JPanel panelCapNhat;
        private JPanel panelThongTinChiTiet;
        private JPanel panelThuocTinh;
        private JLabel labelMaThuocTinh;
        private JLabel labelTenThuocTinh;
        private JTextField txtMaDanhMuc;
        private JTextField txtTenDanhMuc;
        private JPanel panelGiaTriThuocTinh;
        private JTable tableGiaTriThuocTinh;
        private JLabel labelTrangThai;
        private JComboBox cmbTrangThai;
        private JPanel panelQuanLyThuocTinh;
        private JPanel panelBoLoc;
        private JLabel labelTimTheo;
        private JComboBox cmbLocDanhMuc;
        private JComboBox cmbTimTheo;
        private JLabel labelLocDanhMuc;
        private JLabel labelLocTrangThai;
        private JComboBox cmbLocTrangThai;
        private JLabel labelMaGiaTri;
        private JLabel labelNoiDungGiaTri;
        private JTextField txtMaGiaTri;
        private JTextField txtNDGiaTri;
        private JButton btnSuaGiaTri;
        private JButton btnThoat;
        private JButton btnTimKiem;
        private JTextField txtNhapThongTin;
        private JLabel labelNhapThongTin;
        private JTextField txtThuocTinhHienCo;
        private JLabel labelThuocTinhHienCo;
        private JLabel labelDanhMuc;
        private JComboBox cmbDanhMuc;
        private JTextField txtSanPham;
        private JLabel labelSanPham;
        private JLabel labelTrangThaiGiaTri;
        private JComboBox cmbTrangThaiGiaTri;
        private JButton btnLuu;
        private JButton btnHuy;
        private JButton btnHuyGiaTri;
        private JButton btnThemGiaTri;
        private JScrollPane tableDanhSachGiaTriThuocTinh;

    private DefaultTableModel modelThuocTinh, modelGiaTriThuocTinh;
    private JPopupMenu popupGoiY = new JPopupMenu();

    private final DanhMuc_BUS dmBUS = DanhMuc_BUS.getInstance();
    private final ThuocTinhDanhMuc_BUS ttBUS = ThuocTinhDanhMuc_BUS.getInstance();
    private final GiaTriThuocTinh_BUS gtBUS = GiaTriThuocTinh_BUS.getInstance();
    private final GiaTriThuocTinh_SP_BUS gtSpBUS = GiaTriThuocTinh_SP_BUS.getInstance();
    private final SanPham_BUS spBUS = SanPham_BUS.getInstance();

    private boolean isAdding = false;
    private boolean isUpdating = false;

    private boolean isAddingGiaTri = false;
    private boolean isUpdatingGiaTri = false;

    public ThuocTinh_GUI() {
        this.setLayout(new BorderLayout());
        if (panelQuanLyThuocTinh != null) {
            this.add(panelQuanLyThuocTinh, BorderLayout.CENTER);
        }

        initTable_ThuocTinh();
        initTable_GiaTriThuocTinh();
        initComboBox();

        // Đổ dữ liệu ban đầu
        loadDataToTable_ThuocTinh(ttBUS.getAll());
        addEvents();
        setViewMode();
    }

    private void initTable_ThuocTinh() {
        String[] headers = {"STT", "Mã Thuộc Tính", "Tên Thuộc Tính", "Danh Mục", "Trạng Thái"};
        modelThuocTinh = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableThuocTinh.setModel(modelThuocTinh);
    }

    private void initTable_GiaTriThuocTinh() {
        String[] headers = {"STT", "Mã Giá Trị", "Nội Dung Giá Trị", "Thuộc Tính", "Sản phẩm", "Trạng Thái"};
        modelGiaTriThuocTinh = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableGiaTriThuocTinh.setModel(modelGiaTriThuocTinh);
    }

    private void initComboBox() {
        // Chi tiết Thuộc tính
        cmbTrangThai.setModel(new DefaultComboBoxModel<>(new String[]{ThuocTinhDanhMuc_DTO.HOAT_DONG, ThuocTinhDanhMuc_DTO.NGUNG_HOAT_DONG}));

        // Chi tiết Giá trị SP
        cmbTrangThaiGiaTri.setModel(new DefaultComboBoxModel<>(new String[]{GiaTriThuocTinh_SP_DTO.DANG_SU_DUNG, GiaTriThuocTinh_SP_DTO.NGUNG_SU_DUNG}));

        // Bộ lọc
        cmbTimTheo.setModel(new DefaultComboBoxModel<>(new String[]{"Tất cả", "Mã Thuộc Tính", "Tên Thuộc Tính"}));
        cmbLocTrangThai.setModel(new DefaultComboBoxModel<>(new String[]{"Tất cả", ThuocTinhDanhMuc_DTO.HOAT_DONG, ThuocTinhDanhMuc_DTO.NGUNG_HOAT_DONG}));

        cmbLocDanhMuc.removeAllItems();
        cmbLocDanhMuc.addItem("Tất cả");
        cmbDanhMuc.removeAllItems();
        for (DanhMuc_DTO dm : dmBUS.getAll()) {
            cmbDanhMuc.addItem(dm.getTenDM());
            cmbLocDanhMuc.addItem(dm.getTenDM());
        }
    }

    private void loadDataToTable_ThuocTinh(ArrayList<ThuocTinhDanhMuc_DTO> list) {
        modelThuocTinh.setRowCount(0);
        int stt = 1;
        for (ThuocTinhDanhMuc_DTO tt : list) {
            DanhMuc_DTO dm = dmBUS.getById(tt.getMaDM());
            modelThuocTinh.addRow(new Object[]{
                    stt++, tt.getMaThuocTinh(), tt.getTenThuocTinh(),
                    (dm != null) ? dm.getTenDM() : tt.getMaDM(),
                    tt.getTrangThaiText()
            });
        }
        txtThuocTinhHienCo.setText(String.valueOf(list.size()));
    }

    // --- HÀM FIX LỖI ĐỔ DỮ LIỆU BẢNG DƯỚI ---
    private void loadDataToTable_GiaTriThuocTinh(String maThuocTinh) {
        modelGiaTriThuocTinh.setRowCount(0);

        // 1. Lấy danh sách liên kết từ BUS (Bảng trung gian GIATRITHUOCTINHSP)
        ArrayList<GiaTriThuocTinh_SP_DTO> dsLienKet = gtSpBUS.getByMaTT(maThuocTinh);

        int stt = 1;
        for (GiaTriThuocTinh_SP_DTO lienKet : dsLienKet) {
            // 2. Lấy nội dung chữ (Ví dụ: "Viên nén") từ BUS master
            GiaTriThuocTinh_DTO gtDef = gtBUS.getById(lienKet.getMaGiaTri());

            // 3. Lấy tên sản phẩm từ BUS Sản phẩm
            SanPham_DTO sp = spBUS.getById(lienKet.getMaSP());

            modelGiaTriThuocTinh.addRow(new Object[]{
                    stt++,
                    lienKet.getMaGiaTri(),
                    (gtDef != null) ? gtDef.getNdGiaTri() : "N/A",
                    lienKet.getMaThuocTinh(),
                    (sp != null) ? sp.getTenSP() : lienKet.getMaSP(),
                    lienKet.getTrangThaiText() // Logic trạng thái từ bảng liên kết
            });
        }
    }

    private void addEvents() {
        // Click bảng trên
        tableThuocTinh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isAdding || isUpdating) return;
                int row = tableThuocTinh.getSelectedRow();
                if (row >= 0) fillThuocTinhFromTable(row);
            }
        });

        // Click bảng dưới
        tableGiaTriThuocTinh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableGiaTriThuocTinh.getSelectedRow();
                if (row >= 0) fillGiaTriFromTable(row);
            }
        });

        btnThem.addActionListener(e -> setAddMode());
        btnCapNhat.addActionListener(e -> {
            if (tableThuocTinh.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn thuộc tính cần sửa!");
                return;
            }
            setUpdateMode();
        });

        btnLuu.addActionListener(e -> xuLyLuu());
        btnHuy.addActionListener(e -> { setViewMode(); resetFields(); });

        // Lọc và gợi ý
        txtNhapThongTin.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() != KeyEvent.VK_UP && e.getKeyCode() != KeyEvent.VK_DOWN) {
                    thucHienLoc();
                    hienThiGoiY();
                }
            }
        });

        ActionListener locAction = e -> thucHienLoc();
        cmbTimTheo.addActionListener(locAction);
        cmbLocTrangThai.addActionListener(locAction);
        cmbLocDanhMuc.addActionListener(locAction);
        btnTimKiem.addActionListener(locAction);
        btnThoat.addActionListener(e -> {
            txtNhapThongTin.setText("");
            cmbTimTheo.setSelectedIndex(0);
            cmbLocTrangThai.setSelectedIndex(0);
            cmbLocDanhMuc.setSelectedIndex(0);
            loadDataToTable_ThuocTinh(ttBUS.getAll());
        });
        btnThemGiaTri.addActionListener(e -> {
            if (!isAddingGiaTri) {
                if (txtMaDanhMuc.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn Thuộc tính ở bảng trên trước!");
                    return;
                }
                setGiaTriAddMode();
            } else {
                xuLyLuuGiaTri(true);
            }
        });

        btnSuaGiaTri.addActionListener(e -> {
            if (!isUpdatingGiaTri) {
                if (tableGiaTriThuocTinh.getSelectedRow() < 0) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn giá trị cần sửa ở bảng dưới!");
                    return;
                }
                setGiaTriUpdateMode();
            } else {
                xuLyLuuGiaTri(false);
            }
        });

        btnHuyGiaTri.addActionListener(e -> {
            setGiaTriViewMode();
            // Fill lại data nếu đang chọn
            int row = tableGiaTriThuocTinh.getSelectedRow();
            if (row >= 0) fillGiaTriFromTable(row);
        });
    }

    private void fillThuocTinhFromTable(int row) {
        String maTT = modelThuocTinh.getValueAt(row, 1).toString();
        ThuocTinhDanhMuc_DTO tt = ttBUS.getById(maTT);
        if (tt == null) return;

        txtMaDanhMuc.setText(tt.getMaThuocTinh()); // txtMaDanhMuc của bạn chứa MaThuocTinh
        txtTenDanhMuc.setText(tt.getTenThuocTinh());
        cmbTrangThai.setSelectedItem(tt.getTrangThaiText());

        DanhMuc_DTO dm = dmBUS.getById(tt.getMaDM());
        if (dm != null) cmbDanhMuc.setSelectedItem(dm.getTenDM());

        // KÍCH HOẠT ĐỔ DỮ LIỆU BẢNG DƯỚI
        loadDataToTable_GiaTriThuocTinh(maTT);
    }

    private void fillGiaTriFromTable(int row) {
        txtMaGiaTri.setText(modelGiaTriThuocTinh.getValueAt(row, 1).toString());
        txtNDGiaTri.setText(modelGiaTriThuocTinh.getValueAt(row, 2).toString());
        txtSanPham.setText(modelGiaTriThuocTinh.getValueAt(row, 4).toString());
        cmbTrangThaiGiaTri.setSelectedItem(modelGiaTriThuocTinh.getValueAt(row, 5).toString());
    }

    private void thucHienLoc() {
        String keyword = txtNhapThongTin.getText().trim();
        String timTheo = (String) cmbTimTheo.getSelectedItem();

        String tenDM = (String) cmbLocDanhMuc.getSelectedItem();
        String maDM = null;
        if (!tenDM.equals("Tất cả")) {
            for(DanhMuc_DTO d : dmBUS.getAll()) if(d.getTenDM().equals(tenDM)) maDM = d.getMaDM();
        }

        String ttStr = (String) cmbLocTrangThai.getSelectedItem();
        Integer trangThai = ttStr.equals("Tất cả") ? null : ThuocTinhDanhMuc_DTO.parseTrangThaiFromText(ttStr);

        // Gọi hàm tìm kiếm nâng cao từ BUS
        ArrayList<ThuocTinhDanhMuc_DTO> dsLoc = ttBUS.timKiemNangCao(keyword, timTheo, maDM, trangThai);
        loadDataToTable_ThuocTinh(dsLoc);
    }

    private void hienThiGoiY() {
        popupGoiY.setVisible(false); popupGoiY.removeAll();
        String text = txtNhapThongTin.getText().trim();
        if (text.isEmpty()) return;

        String timTheo = (String) cmbTimTheo.getSelectedItem();
        ArrayList<ThuocTinhDanhMuc_DTO> ds = ttBUS.timKiemNangCao(text, timTheo, null, null);

        if (ds.isEmpty()) return;
        for (int i = 0; i < Math.min(ds.size(), 5); i++) {
            ThuocTinhDanhMuc_DTO tt = ds.get(i);
            JMenuItem item = new JMenuItem(tt.getMaThuocTinh() + " - " + tt.getTenThuocTinh());
            item.addActionListener(e -> {
                txtNhapThongTin.setText(timTheo.equals("Tên Thuộc Tính") ? tt.getTenThuocTinh() : tt.getMaThuocTinh());
                thucHienLoc();
                popupGoiY.setVisible(false);
            });
            popupGoiY.add(item);
        }
        popupGoiY.show(txtNhapThongTin, 0, txtNhapThongTin.getHeight());
        txtNhapThongTin.requestFocus();
    }

    private void xuLyLuu() {
        if (txtTenDanhMuc.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên thuộc tính không được để trống!");
            return;
        }

        ThuocTinhDanhMuc_DTO tt = new ThuocTinhDanhMuc_DTO();
        tt.setMaThuocTinh(txtMaDanhMuc.getText());
        tt.setTenThuocTinh(txtTenDanhMuc.getText().trim());

        // Lấy mã Danh mục
        String tenDM = (String) cmbDanhMuc.getSelectedItem();
        for(DanhMuc_DTO dm : dmBUS.getAll()) {
            if(dm.getTenDM().equals(tenDM)) { tt.setMaDM(dm.getMaDM()); break; }
        }

        tt.setTrangThai(ThuocTinhDanhMuc_DTO.parseTrangThaiFromText((String) cmbTrangThai.getSelectedItem()));

        boolean success = isAdding ? ttBUS.them(tt) : ttBUS.capNhat(tt);
        if (success) {
            JOptionPane.showMessageDialog(this, "Lưu thành công!");
            loadDataToTable_ThuocTinh(ttBUS.getAll());
            setViewMode();
        }
    }

    private void setViewMode() {
        isAdding = isUpdating = false;
        txtTenDanhMuc.setEditable(false);
        cmbDanhMuc.setEnabled(false); cmbTrangThai.setEnabled(false);
        btnLuu.setVisible(false); btnHuy.setVisible(false);
        btnThem.setEnabled(true); btnCapNhat.setEnabled(true);
        tableThuocTinh.setEnabled(true);
    }

    private void setAddMode() {
        isAdding = true; isUpdating = false;
        resetFields();
        txtMaDanhMuc.setText(ttBUS.getNextId());
        txtTenDanhMuc.setEditable(true);
        cmbDanhMuc.setEnabled(true); cmbTrangThai.setEnabled(false);
        cmbTrangThai.setSelectedItem(ThuocTinhDanhMuc_DTO.HOAT_DONG);
        btnLuu.setVisible(true); btnHuy.setVisible(true);
        btnThem.setEnabled(false); btnCapNhat.setEnabled(false);
        tableThuocTinh.setEnabled(false);
    }

    private void setUpdateMode() {
        isAdding = false; isUpdating = true;
        txtTenDanhMuc.setEditable(true);
        cmbDanhMuc.setEnabled(true); cmbTrangThai.setEnabled(true);
        btnLuu.setVisible(true); btnHuy.setVisible(true);
        btnThem.setEnabled(false); btnCapNhat.setEnabled(false);
        tableThuocTinh.setEnabled(false);
    }

    private void resetFields() {
        txtMaDanhMuc.setText(""); txtTenDanhMuc.setText("");
        txtMaGiaTri.setText(""); txtNDGiaTri.setText(""); txtSanPham.setText("");
        modelGiaTriThuocTinh.setRowCount(0);
    }
    // --- HÀM ĐIỀU KHIỂN GIAO DIỆN GIÁ TRỊ ---

    private void setGiaTriViewMode() {
        isAddingGiaTri = false;
        isUpdatingGiaTri = false;

        lockGiaTriForm(true);

        // Chế độ xem: Hiện Thêm & Sửa, ẨN Hủy
        btnThemGiaTri.setVisible(true);
        btnSuaGiaTri.setVisible(true);
        btnHuyGiaTri.setVisible(false);

        tableGiaTriThuocTinh.setEnabled(true);

        // Làm mới giao diện để nút Hủy biến mất ngay lập tức
        this.revalidate();
        this.repaint();
    }

    private void setGiaTriAddMode() {
        isAddingGiaTri = true;
        isUpdatingGiaTri = false;

        txtMaGiaTri.setText(gtBUS.getNextId());
        txtNDGiaTri.setText("");
        txtSanPham.setText("");
        txtNDGiaTri.requestFocus();

        lockGiaTriForm(false);

        // Chế độ Thêm: Hiện Thêm (để Lưu) & Hủy, ẨN Sửa
        btnThemGiaTri.setVisible(true);
        btnSuaGiaTri.setVisible(false);
        btnHuyGiaTri.setVisible(true);

        tableGiaTriThuocTinh.setEnabled(false);

        this.revalidate();
        this.repaint();
    }

    private void setGiaTriUpdateMode() {
        isAddingGiaTri = false;
        isUpdatingGiaTri = true;

        lockGiaTriForm(false);
        txtMaGiaTri.setEditable(false);
        txtNDGiaTri.setEditable(false);
        txtSanPham.setEditable(false);

        // Chế độ Sửa: Hiện Sửa (để Lưu) & Hủy, ẨN Thêm
        btnThemGiaTri.setVisible(false);
        btnSuaGiaTri.setVisible(true);
        btnHuyGiaTri.setVisible(true);

        tableGiaTriThuocTinh.setEnabled(false);

        this.revalidate();
        this.repaint();
    }

    private void lockGiaTriForm(boolean lock) {
        txtMaGiaTri.setEditable(!lock);
        txtNDGiaTri.setEditable(!lock);
        txtSanPham.setEditable(!lock);
        cmbTrangThaiGiaTri.setEnabled(!lock);
    }

    // --- LOGIC LƯU GIÁ TRỊ (GỌI BUS) ---

    private void xuLyLuuGiaTri(boolean isNew) {
        String maTT = txtMaDanhMuc.getText(); // Mã thuộc tính cha
        String ndGiaTri = txtNDGiaTri.getText().trim();
        String tenSP = txtSanPham.getText().trim();
        int trangThai = GiaTriThuocTinh_SP_DTO.parseTrangThaiFromText(cmbTrangThaiGiaTri.getSelectedItem().toString());

        if (ndGiaTri.isEmpty() || tenSP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Nội dung và Tên sản phẩm!");
            return;
        }

        // 1. Tìm sản phẩm theo tên
        SanPham_DTO sp = spBUS.getByTenSP(tenSP);
        if (sp == null) {
            JOptionPane.showMessageDialog(this, "Sản phẩm không tồn tại!");
            return;
        }

        // 2. Xử lý logic tại BUS
        // Kiểm tra xem nội dung giá trị này đã có trong từ điển chưa
        String maGT = gtBUS.getMaByNoiDung(maTT, ndGiaTri);
        if (maGT == null) {
            // Nếu chưa có -> Thêm mới vào bảng GIATRITHUOCTINH
            maGT = gtBUS.getNextId();
            gtBUS.them(new GiaTriThuocTinh_DTO(maGT, ndGiaTri, maTT));
        }

        // 3. Lưu liên kết SP
        boolean res;
        if (isNew) {
            res = gtSpBUS.themLienKetSP(sp.getMaSP(), maTT, maGT, trangThai);
        } else {
            res = gtSpBUS.capNhat(new GiaTriThuocTinh_SP_DTO(sp.getMaSP(), maTT, maGT, trangThai));
        }

        if (res) {
            JOptionPane.showMessageDialog(this, "Thao tác thành công!");
            loadDataToTable_GiaTriThuocTinh(maTT);
            setGiaTriViewMode();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi! Có thể sản phẩm này đã được gán giá trị này rồi.");
        }
    }
}