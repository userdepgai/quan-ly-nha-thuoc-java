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
    private JTextField txtMaGiaTri;
    private JTextField txtNDGiaTri;
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
    private JTextField txtSanPham;
    private JLabel labelSanPham;
    private JLabel labelTrangThaiGiaTri;
    private JComboBox cmbTrangThaiGiaTri;
    private JScrollPane tableDanhSachGiaTriThuocTinh;

    private DefaultTableModel modelThuocTinh;
    private DefaultTableModel modelGiaTriThuocTinh;

    private final DanhMuc_BUS dmBUS = DanhMuc_BUS.getInstance();
    private final ThuocTinhDanhMuc_BUS ttBUS = ThuocTinhDanhMuc_BUS.getInstance();
    private GiaTriThuocTinh_BUS gtBUS = GiaTriThuocTinh_BUS.getInstance();
    private final bus.GiaTriThuocTinh_SP_BUS gtSpBUS = bus.GiaTriThuocTinh_SP_BUS.getInstance();
    private final bus.SanPham_BUS spBUS = bus.SanPham_BUS.getInstance();

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
        String[] headers = {"STT", "Mã Thuộc Tính", "Tên Thuộc Tính", "Danh Mục", "Trạng Thái"};
        modelThuocTinh = new DefaultTableModel(headers, 0);
        tableThuocTinh.setModel(modelThuocTinh);
    }

    private void initTable_GiaTriThuocTinh() {
        String[] headers = {"STT", "Mã Giá Trị", "Nội Dung Giá Trị", "Thuộc Tính", "Sản phẩm", "Trạng Thái"};
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

        if(cmbTrangThaiGiaTri != null) {
            cmbTrangThaiGiaTri.removeAllItems();
            cmbTrangThaiGiaTri.addItem("Đang sử dụng");
            cmbTrangThaiGiaTri.addItem("Ngưng sử dụng");
        }

        for (DanhMuc_DTO dm : dmBUS.getAll()) {
            cmbDanhMuc.addItem(dm.getTenDM());
            cmbLocDanhMuc.addItem(dm.getTenDM());
        }
    }

    private void loadDataToTable_ThuocTinh() {
        modelThuocTinh.setRowCount(0); // Xóa trắng bảng
        ArrayList<ThuocTinhDanhMuc_DTO> list = ttBUS.getAll();

        int stt = 1;
        for (ThuocTinhDanhMuc_DTO tt : list) {
            String trangThai = (tt.getTrangThai() == 1) ? "Đang hoạt động" : "Ngưng hoạt động";

            // Dịch mã Danh mục thành Tên danh mục
            DanhMuc_DTO dm = dmBUS.getById(tt.getMaDM());
            String tenDM = (dm != null) ? dm.getTenDM() : tt.getMaDM();

            modelThuocTinh.addRow(new Object[]{
                    stt++,
                    tt.getMaThuocTinh(),
                    tt.getTenThuocTinh(),
                    tenDM,
                    trangThai
            });
        }
    }

    private void loadDataToTable_GiaTriThuocTinh(String maThuocTinh) {
        modelGiaTriThuocTinh.setRowCount(0); // Xóa trắng bảng dưới
        ArrayList<GiaTriThuocTinh_DTO> list = gtBUS.getByMaThuocTinh(maThuocTinh);

        int stt = 1;
        for (GiaTriThuocTinh_DTO gt : list) {
            String trangThai = (gt.getTrangThai() == 1) ? "Đang sử dụng" : "Ngưng sử dụng";

            // --- BẮT ĐẦU ĐOẠN CODE LẤY TÊN SẢN PHẨM ---

            // 1. Tìm trong bảng trung gian xem có những SP nào dùng Mã Giá Trị này
            // (Hàm này bạn đã thêm vào BUS ở bước trước)
            ArrayList<dto.GiaTriThuocTinh_SP_DTO> dsLienKet = gtSpBUS.getByMaGTTT(gt.getMaGiaTri());

            for (dto.GiaTriThuocTinh_SP_DTO lienKet : dsLienKet) {
                // Lấy tên sản phẩm
                dto.SanPham_DTO sp = spBUS.getById(lienKet.getMaSP());
                String tenSP = (sp != null) ? sp.getTenSP() : lienKet.getMaSP();

                modelGiaTriThuocTinh.addRow(new Object[]{
                        stt++,
                        gt.getMaGiaTri(),
                        gt.getNdGiaTri(),
                        gt.getMaThuocTinh(),
                        tenSP, // Hiển thị tên cụ thể của từng sản phẩm
                        trangThai
                });
            }
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
                    String tenDM = modelThuocTinh.getValueAt(row, 3).toString();
                    String trangThai = modelThuocTinh.getValueAt(row, 4).toString();



                    // Đẩy dữ liệu lên các ô Textfield (Cập nhật thông tin chi tiết)
                    if(txtMaDanhMuc != null) txtMaDanhMuc.setText(maThuocTinh);
                    if(txtTenDanhMuc != null) txtTenDanhMuc.setText(tenThuocTinh);
                    if(cmbDanhMuc != null) cmbDanhMuc.setSelectedItem(tenDM);
                    if(cmbTrangThai != null) cmbTrangThai.setSelectedItem(trangThai);

                    // Xóa trắng ô nhập của bảng con dưới
                    if(txtMaGiaTri != null) txtMaGiaTri.setText("");
                    if(txtNDGiaTri != null) txtNDGiaTri.setText("");
                    if(txtSanPham != null) txtSanPham.setText("");
                    if(cmbTrangThaiGiaTri != null) cmbTrangThaiGiaTri.setSelectedIndex(0);

                    // TỰ ĐỘNG LOAD DỮ LIỆU XUỐNG BẢNG CON
                    loadDataToTable_GiaTriThuocTinh(maThuocTinh);

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
                    String sanPham = modelGiaTriThuocTinh.getValueAt(row, 4).toString();
                    String trangThaiGiaTri = modelGiaTriThuocTinh.getValueAt(row, 5).toString();

                    // Đẩy dữ liệu lên các ô text của phần "Danh sách Giá trị thuộc tính"
                    if(txtMaGiaTri != null) txtMaGiaTri.setText(maGiaTri);
                    if(txtNDGiaTri != null) txtNDGiaTri.setText(noiDung);
                    if(txtSanPham != null) txtSanPham.setText(sanPham);
                    if(cmbTrangThaiGiaTri != null) cmbTrangThaiGiaTri.setSelectedItem(trangThaiGiaTri);
                }
            }
        });
    }
}