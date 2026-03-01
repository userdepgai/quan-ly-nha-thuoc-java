package gui;

import bus.*;
import dto.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class QuanLySanPham_GUI extends JPanel {
    private JPanel panelTieuDe;
    private JLabel label_tieuDe;
    private JButton btnNhapExcel;
    private JButton btnXuatExcel;
    private JPanel panelBoLoc;
    private JLabel labelTimKiem;
    private JComboBox cmbTimTheo;
    private JPanel panelDanhSachSanPham;
    private JTable tableSanPham;
    private JPanel panelThongTinChiTiet;
    private JLabel labelMaSanPham;
    private JLabel labelTenSanPham;
    private JTextField txtMaSanPham;
    private JTextField txtTenSanPham;
    private JComboBox cmbDonViTinh;
    private JLabel labelDonViTinh;
    private JLabel labelLoiNhuan;
    private JTextField txtLoiNhuan;
    private JLabel labelDanhMuc;
    private JLabel labelApDungDanhMuc;
    private JLabel labelQuyCach;
    private JComboBox cmbDanhMuc;
    private JButton btnCapNhat;
    private JButton btnThem;
    private JPanel panelCapNhat;
    private JLabel labelLocDanhMuc;
    private JComboBox cmbLocDanhMuc;
    private JPanel panelQuanLySanPham;
    private JButton btnThemHinhAnh;
    private JPanel panelHinhAnh;
    private JLabel labelHinhAnh;
    private JLabel labelSoLuongTonKho;
    private JTextField txtSoLuongTonKho;
    private JTextField txtSPTrongHop;
    private JTextField txtHopTrongThung;
    private JLabel labelTrangThai;
    private JComboBox cmbTrangThai;
    private JTextArea txtAreaThuocTinhRieng;
    private JLabel labelHopTrongThung;
    private JLabel labelSPTrongHop;
    private JPanel panelQuyCach;
    private JLabel labelLinkHinhAnh;
    private JTextField txtLinkHinhAnh;
    private JLabel labelLocLoiNhuan;
    private JComboBox cmbLocLoiNhuan;
    private JLabel labelNhapThongTin;
    private JTextField txtNhapThongTin;
    private JButton btnTimKiem;
    private JButton btnThoat;
    private JLabel labelKeDon;
    private JLabel labelLocTrangThai;
    private JComboBox cmbLocTrangThai;
    private JLabel labelSanPhamHienCo;
    private JTextField txtSanPhamHienCo;
    private JComboBox cmbKeDon;
    private JButton btnHuy;
    private JButton btnLuu;
    private DefaultTableModel modelSanPham;

    private final SanPham_BUS spBUS = SanPham_BUS.getInstance();
    private final QuyCach_BUS qcBUS = QuyCach_BUS.getInstance();
    private final DanhMuc_BUS dmBUS = DanhMuc_BUS.getInstance();
    private final ThuocTinhDanhMuc_BUS ttBus = ThuocTinhDanhMuc_BUS.getInstance();
    private final GiaTriThuocTinh_BUS gtBus = GiaTriThuocTinh_BUS.getInstance();
    private final GiaTriThuocTinh_SP_BUS gtspBus = GiaTriThuocTinh_SP_BUS.getInstance();

    private String chucNangHienTai = "";

    public QuanLySanPham_GUI() {
        this.setLayout(new BorderLayout());
        if (panelQuanLySanPham != null) {
            this.add(panelQuanLySanPham, BorderLayout.CENTER);
        }

        initTable();
        initComboBoxData(); // Quan trọng: Đổ danh sách DM vào combo
        loadDataToTable(spBUS.getAll());
        addEvents();
        lockForm(true);
    }

    private void initTable() {
        String[] headers = {"STT", "Mã SP", "Tên Sản Phẩm", "ĐVT", "Lợi Nhuận", "Kê đơn", "Danh Mục", "Hình Ảnh", "Trạng Thái"};
        modelSanPham = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableSanPham.setModel(modelSanPham);
    }

    private void initComboBoxData() {
        // Đổ danh mục từ DB vào ComboBox lọc và ComboBox nhập liệu
        cmbDanhMuc.removeAllItems();
        cmbLocDanhMuc.removeAllItems();
        cmbLocDanhMuc.addItem("Tất cả");

        for (DanhMuc_DTO dm : dmBUS.getAll()) {
            cmbDanhMuc.addItem(dm.getTenDM());
            cmbLocDanhMuc.addItem(dm.getTenDM());
        }

        // Đổ ĐVT mẫu
        String[] dvt = {"Viên", "Vỉ", "Hộp", "Chai", "Tuýp", "Lọ"};

        cmbDonViTinh.setModel(new DefaultComboBoxModel<>(dvt));
        cmbKeDon.removeAllItems();
        cmbKeDon.addItem("Có");
        cmbKeDon.addItem("Không");

        cmbTrangThai.removeAllItems();
        cmbTrangThai.addItem("Đang bán");
        cmbTrangThai.addItem("Ngừng bán");

        if (cmbLocTrangThai != null) {
            cmbLocTrangThai.removeAllItems();
            cmbLocTrangThai.addItem("Tất cả");
            cmbLocTrangThai.addItem("Đang bán");
            cmbLocTrangThai.addItem("Ngừng bán");
        }
    }

    private void loadDataToTable(ArrayList<SanPham_DTO> list) {
        modelSanPham.setRowCount(0);
        int stt = 1;
        for (SanPham_DTO sp : list) {
            // Dịch mã Danh mục thành Tên danh mục
            DanhMuc_DTO dm = dmBUS.getById(sp.getMaDM());
            String tenDM = (dm != null) ? dm.getTenDM() : sp.getMaDM();

            modelSanPham.addRow(new Object[]{
                    stt++,
                    sp.getMaSP(),
                    sp.getTenSP(),
                    sp.getDonViTinh(),
                    sp.getLoiNhuan(),
                    sp.getKeDon() == 1 ? "Có" : "Không",
                    tenDM,
                    sp.getHinhAnh(),
                    sp.getTrangThai() == 1 ? "Đang bán" : "Ngừng bán"
            });
        }
        txtSanPhamHienCo.setText(String.valueOf(list.size()));
    }

    private void addEvents() {
        tableSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableSanPham.getSelectedRow();
                if (row >= 0) {
                    String maSP = tableSanPham.getValueAt(row, 1).toString();
                    SanPham_DTO sp = spBUS.getById(maSP);
                    if (sp != null) {
                        fillData(sp);
                        lockForm(true);
                    }
                }
            }
        });

        btnThem.addActionListener(e -> {
            clearForm();
            txtMaSanPham.setText(spBUS.getNextId());
            chucNangHienTai = "THEM";
            lockForm(false);
        });

        btnCapNhat.addActionListener(e -> {
            if (txtMaSanPham.getText().isEmpty()) return;
            chucNangHienTai = "SUA";
            lockForm(false);
        });

        btnLuu.addActionListener(e -> saveSanPham());
        btnHuy.addActionListener(e -> { lockForm(true); clearForm(); });

        btnTimKiem.addActionListener(e -> thucHienLoc());
    }

    private void fillData(SanPham_DTO sp) {
        txtMaSanPham.setText(sp.getMaSP());
        txtTenSanPham.setText(sp.getTenSP());
        cmbDonViTinh.setSelectedItem(sp.getDonViTinh());
        txtLoiNhuan.setText(String.valueOf(sp.getLoiNhuan()));
        cmbKeDon.setSelectedItem(sp.getKeDon());
        cmbTrangThai.setSelectedItem(sp.getTrangThai());
        cmbKeDon.setSelectedItem(sp.getKeDon() == 1 ? "Có" : "Không");
        cmbTrangThai.setSelectedItem(sp.getTrangThai() == 1 ? "Đang bán" : "Ngừng bán");
        txtLinkHinhAnh.setText(sp.getHinhAnh());

        // Fill Danh mục (Dùng tên để chọn combo)
        DanhMuc_DTO dm = dmBUS.getById(sp.getMaDM());
        if (dm != null) cmbDanhMuc.setSelectedItem(dm.getTenDM());

        // Fill Quy cách
        QuyCach_DTO qc = qcBUS.getById(sp.getMaQC());
        if (qc != null) {
            txtSPTrongHop.setText(String.valueOf(qc.getSlTrongHop()));
            txtHopTrongThung.setText(String.valueOf(qc.getSlHopTrongThung()));
        }

        // --- XỬ LÝ THUỘC TÍNH RIÊNG ---
        StringBuilder sb = new StringBuilder();
        ArrayList<GiaTriThuocTinh_SP_DTO> listGTSP = gtspBus.getByMaSP(sp.getMaSP());
        for (GiaTriThuocTinh_SP_DTO gtsp : listGTSP) {
            ThuocTinhDanhMuc_DTO tt = ttBus.getById(gtsp.getMaThuocTinh());
            GiaTriThuocTinh_DTO gt = gtBus.getById(gtsp.getMaGiaTri());
            if (tt != null && gt != null) {
                sb.append("• ").append(tt.getTenThuocTinh()).append(": ").append(gt.getNdGiaTri()).append("\n");
            }
        }
        txtAreaThuocTinhRieng.setText(sb.toString());

        // Load ảnh (giả định có hàm load ảnh)
        updateImagePreview(sp.getHinhAnh());
    }

    private void thucHienLoc() {
        String keyword = txtNhapThongTin.getText();
        String tenDM = cmbLocDanhMuc.getSelectedItem().toString();
        String maDM = null;

        // Tìm mã DM từ tên DM đã chọn
        for(DanhMuc_DTO dm : dmBUS.getAll()) {
            if(dm.getTenDM().equals(tenDM)) maDM = dm.getMaDM();
        }

        Integer trangThai = null;
        if(cmbLocTrangThai.getSelectedIndex() == 1) trangThai = 1;
        if(cmbLocTrangThai.getSelectedIndex() == 2) trangThai = 0;

        ArrayList<SanPham_DTO> dsLoc = spBUS.timKiem(keyword, maDM, trangThai);
        loadDataToTable(dsLoc);
    }

    private void saveSanPham() {
        // Thu thập dữ liệu và gọi spBUS tương tự code cũ của bạn...
        // Lưu ý khi lấy mã DM từ ComboBox tên DM:
        String tenDMChon = cmbDanhMuc.getSelectedItem().toString();
        String maDM = "";
        for(DanhMuc_DTO d : dmBUS.getAll()) {
            if(d.getTenDM().equals(tenDMChon)) maDM = d.getMaDM();
        }

        // Tiến hành build DTO và gọi BUS.them/capNhat
    }

    private void updateImagePreview(String path) {
        // Logic hiển thị icon vào labelHinhAnh...
    }

    private void lockForm(boolean lock) {
        txtMaSanPham.setEditable(!lock);
        txtTenSanPham.setEditable(!lock);
        txtLoiNhuan.setEditable(!lock);
        txtSPTrongHop.setEditable(!lock);
        txtHopTrongThung.setEditable(!lock);
        cmbDonViTinh.setEnabled(!lock);
        cmbDanhMuc.setEnabled(!lock);
        cmbKeDon.setEnabled(!lock);
        cmbTrangThai.setEnabled(!lock);
        btnLuu.setEnabled(!lock);
        btnHuy.setEnabled(!lock);
        btnThemHinhAnh.setEnabled(!lock);
        txtAreaThuocTinhRieng.setEditable(!lock);
    }

    private void clearForm() {
        txtMaSanPham.setText("");
        txtTenSanPham.setText("");
        txtLoiNhuan.setText("");
        txtSPTrongHop.setText("");
        txtHopTrongThung.setText("");
        txtAreaThuocTinhRieng.setText("");
        txtLinkHinhAnh.setText("");
    }
}