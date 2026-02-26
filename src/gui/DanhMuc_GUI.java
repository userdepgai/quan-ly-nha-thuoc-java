package gui;

import bus.DanhMuc_BUS;
import bus.ThuocTinhDanhMuc_BUS;
import dto.DanhMuc_DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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

    public DanhMuc_GUI(){
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
        if(txtDanhMucHienCo != null) {
            txtDanhMucHienCo.setText(String.valueOf(list.size()));
        }
    }
    // --- LOAD DỮ LIỆU THUỘC TÍNH THEO DANH MỤC ---
    private void loadDataToTable_ThuocTinh(String maDM) {
        modelThuocTinh.setRowCount(0); // Xóa dữ liệu cũ trên bảng thuộc tính

        // Lấy danh sách thuộc tính thuộc danh mục này từ BUS
        ArrayList<dto.ThuocTinhDanhMuc_DTO> dsTT = dmBUS.getThuocTinhByMaDM(maDM);

        int stt = 1;
        for (dto.ThuocTinhDanhMuc_DTO tt : dsTT) {
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
}