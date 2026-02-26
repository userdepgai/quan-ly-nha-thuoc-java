package gui;

import bus.DanhMuc_BUS;
import bus.GiaTriThuocTinh_BUS;
import bus.ThuocTinhDanhMuc_BUS;
import dto.DanhMuc_DTO;
import dto.GiaTriThuocTinh_DTO;
import dto.ThuocTinhDanhMuc_DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ThuocTinhDanhMuc_GUI extends JPanel{
    private JButton btnCapNhat;
    private JButton btnThem;
    private JPanel panelDanhSachThuocTinh;
    private JTable tableThuocTinh;
    private JPanel panelTieuDe;
    private JLabel label_tieuDe;
    private JButton btnNhapExcel;
    private JButton btnXuatExcel;
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
    private JButton btnThemGiaTri;
    private JTextField textField1;
    private JTextField textField2;
    private JButton btnSuaGiaTri;
    private JButton btnHuyGiaTri;
    private JButton btnThoat;
    private JButton btnTimKiem;
    private JTextField txtNhapThongTin;
    private JLabel labelNhapThongTin;
    private JTextField txtThuocTinhHienCo;
    private JLabel labelThuocTinhHienCo;
    private JButton btnHuy;
    private JButton btnLuu;
    private JLabel labelDanhMuc;
    private JComboBox cmbDanhMuc;
    private JScrollPane tableDanhSachGiaTriThuocTinh;

    private DefaultTableModel modelThuocTinh;
    private DefaultTableModel modelGiaTriThuocTinh;

    private final DanhMuc_BUS dmBUS = DanhMuc_BUS.getInstance();
    private final ThuocTinhDanhMuc_BUS ttBUS = ThuocTinhDanhMuc_BUS.getInstance();
    private GiaTriThuocTinh_BUS gtBUS = GiaTriThuocTinh_BUS.getInstance();

    public ThuocTinhDanhMuc_GUI() {
        this.setLayout(new BorderLayout());
        if (panelQuanLyThuocTinh != null) {
            this.add(panelQuanLyThuocTinh, BorderLayout.CENTER);
        }

        // Khởi tạo giao diện
        initTable_ThuocTinh();
        initTable_GiaTriThuocTinh();
        initComboBox();

        // Đổ dữ liệu lên bảng cha
        loadDataToTable_ThuocTinh();

        // Gắn các sự kiện (Click chuột)
        addEvents();
    }

    private void initTable_ThuocTinh() {
        String[] headers = {"STT", "Mã Thuộc Tính", "Tên Thuộc Tính", "Kiểu Thuộc Tính", "Danh Mục", "Trạng Thái"};
        modelThuocTinh = new DefaultTableModel(headers, 0);
        tableThuocTinh.setModel(modelThuocTinh);
    }

    private void initTable_GiaTriThuocTinh() {
        String[] headers = {"STT", "Mã Giá Trị", "Nội Dung Giá Trị", "Thuộc Tính", "Trạng Thái"};
        modelGiaTriThuocTinh = new DefaultTableModel(headers, 0);
        tableGiaTriThuocTinh.setModel(modelGiaTriThuocTinh);
    }

    private void initComboBox() {
        if(cmbTrangThai != null) {
            cmbTrangThai.removeAllItems();
            cmbTrangThai.addItem("Đang hoạt động");
            cmbTrangThai.addItem("Ngưng hoạt động");
        }
        // Đổ danh mục từ DB vào ComboBox lọc và ComboBox nhập liệu
        cmbDanhMuc.removeAllItems();
        cmbLocDanhMuc.removeAllItems();
        cmbLocDanhMuc.addItem("Tất cả");

        for (DanhMuc_DTO dm : dmBUS.getAll()) {
            cmbDanhMuc.addItem(dm.getTenDM());
            cmbLocDanhMuc.addItem(dm.getTenDM());
        }
    }

    // ==============================================================
    // HÀM 1: ĐỔ DỮ LIỆU LÊN BẢNG THUỘC TÍNH (BẢNG TRÊN)
    // ==============================================================
    private void loadDataToTable_ThuocTinh() {
        modelThuocTinh.setRowCount(0); // Xóa trắng bảng
        ArrayList<ThuocTinhDanhMuc_DTO> list = ttBUS.getAll();

        int stt = 1;
        for (ThuocTinhDanhMuc_DTO tt : list) {
            String kieu = (tt.getKieuThuocTinh() == 0) ? "Combobox (Chọn)" : "Nhập giá trị";
            String trangThai = (tt.getTrangThai() == 1) ? "Đang hoạt động" : "Ngưng hoạt động";
            // Dịch mã Danh mục thành Tên danh mục
            DanhMuc_DTO dm = dmBUS.getById(tt.getMaDM());
            String tenDM = (dm != null) ? dm.getTenDM() : tt.getMaDM();
            modelThuocTinh.addRow(new Object[]{
                    stt++,
                    tt.getMaThuocTinh(),
                    tt.getTenThuocTinh(),
                    kieu,
                    tenDM,
                    trangThai
            });
        }
    }

    // ==============================================================
    // HÀM 2: ĐỔ DỮ LIỆU LÊN BẢNG GIÁ TRỊ (BẢNG DƯỚI) DỰA VÀO MÃ TT
    // ==============================================================
    private void loadDataToTable_GiaTriThuocTinh(String maThuocTinh) {
        modelGiaTriThuocTinh.setRowCount(0); // Xóa trắng bảng dưới
        ArrayList<GiaTriThuocTinh_DTO> list = gtBUS.getByMaThuocTinh(maThuocTinh);

        int stt = 1;
        for (GiaTriThuocTinh_DTO gt : list) {
            String trangThai = (gt.getTrangThai() == 1) ? "Đang sử dụng" : "Ngưng sử dụng";

            // Đưa dữ liệu 1 dòng vào mảng Object để add vào bảng dưới
            modelGiaTriThuocTinh.addRow(new Object[]{
                    stt++,
                    gt.getMaGiaTri(),
                    gt.getNdGiaTri(),
                    gt.getMaThuocTinh(),
                    trangThai
            });
        }
    }

    // ==============================================================
    // HÀM 3: BẮT SỰ KIỆN CLICK CHUỘT
    // ==============================================================
    private void addEvents() {
        // 1. Click vào bảng Thuộc Tính (Bảng Trên)
        tableThuocTinh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableThuocTinh.getSelectedRow();
                if (row >= 0) {
                    // Lấy dữ liệu từ dòng được click
                    String maThuocTinh = modelThuocTinh.getValueAt(row, 1).toString();
                    String tenThuocTinh = modelThuocTinh.getValueAt(row, 2).toString();
                    String kieuThuocTinh = modelThuocTinh.getValueAt(row, 3).toString(); // Lấy Kiểu Thuộc Tính
                    String trangThai = modelThuocTinh.getValueAt(row, 5).toString();
                    String tenDM = modelThuocTinh.getValueAt(row, 4).toString();


                    // Đẩy dữ liệu lên các ô Textfield (Cập nhật thông tin chi tiết)
                    if(txtMaDanhMuc != null) txtMaDanhMuc.setText(maThuocTinh);
                    if(txtTenDanhMuc != null) txtTenDanhMuc.setText(tenThuocTinh);
                    if(cmbTrangThai != null) cmbTrangThai.setSelectedItem(trangThai);
                    if (cmbDanhMuc != null) cmbDanhMuc.setSelectedItem(tenDM);

                    // Xóa trắng ô nhập của bảng con dưới
                    if(textField1 != null) textField1.setText("");
                    if(textField2 != null) textField2.setText("");

                    // TỰ ĐỘNG LOAD DỮ LIỆU XUỐNG BẢNG CON
                    loadDataToTable_GiaTriThuocTinh(maThuocTinh);

                    // =========================================================
                    // RÀNG BUỘC: NẾU LÀ "NHẬP GIÁ TRỊ" THÌ CHỈ ĐƯỢC CÓ 1 DÒNG
                    // =========================================================
                    if (kieuThuocTinh.equals("Nhập giá trị")) {
                        // Nếu bảng dưới đã có từ 1 dòng trở lên -> Khóa nút Thêm
                        if (modelGiaTriThuocTinh.getRowCount() >= 1) {
                            if(btnThemGiaTri != null) btnThemGiaTri.setEnabled(false);
                        } else {
                            // Nếu chưa có dòng nào thì cho phép thêm 1 dòng đầu tiên
                            if(btnThemGiaTri != null) btnThemGiaTri.setEnabled(true);
                        }
                    } else {
                        // Nếu là Combobox (Chọn) -> Cho phép thêm nhiều dòng thoải mái
                        if(btnThemGiaTri != null) btnThemGiaTri.setEnabled(true);
                    }

                    // Luôn luôn mở khóa nút Sửa để có thể chỉnh sửa nội dung/trạng thái
                    if(btnSuaGiaTri != null) btnSuaGiaTri.setEnabled(true);
                }
            }
        });

        // 2. Click vào bảng Giá Trị Thuộc Tính (Bảng Dưới)
        tableGiaTriThuocTinh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableGiaTriThuocTinh.getSelectedRow();
                if (row >= 0) {
                    // Lấy dữ liệu từ dòng được click
                    String maGiaTri = modelGiaTriThuocTinh.getValueAt(row, 1).toString();
                    String noiDung = modelGiaTriThuocTinh.getValueAt(row, 2).toString();

                    // Đẩy dữ liệu lên các ô text của phần "Danh sách Giá trị thuộc tính"
                    if(textField1 != null) textField1.setText(maGiaTri); // Mã giá trị
                    if(textField2 != null) textField2.setText(noiDung);  // Nội dung giá trị
                }
            }
        });
    }
}