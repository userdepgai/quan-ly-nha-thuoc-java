package gui;

import bus.QuyCach_BUS;
import bus.SanPham_BUS;
import dto.QuyCach_DTO;
import dto.SanPham_DTO;

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

    private SanPham_BUS spBUS = SanPham_BUS.getInstance();
    private QuyCach_BUS qcBUS = QuyCach_BUS.getInstance();
    private String chucNangHienTai = "";

    public QuanLySanPham_GUI() {
        this.setLayout(new BorderLayout());
        if (panelQuanLySanPham != null) {
            this.add(panelQuanLySanPham, BorderLayout.CENTER);
        }
        initTable();
        loadDataToTable(spBUS.getAll());
        addEvents();
        lockForm(true); // Mặc định khóa
    }

    private void initTable() {
        String[] headers = {"STT", "Mã SP", "Tên Sản Phẩm", "ĐVT", "Lợi Nhuận", "Kê đơn", "Danh Mục", "Hình Ảnh", "Trạng Thái"};
        // KHI KHỞI TẠO MODEL, GHI ĐÈ HÀM isCellEditable
        modelSanPham = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Trả về false để chặn chỉnh sửa tất cả các ô
            }
        };
        tableSanPham.setModel(modelSanPham);
    }

    private void loadDataToTable(ArrayList<SanPham_DTO> list) {
        modelSanPham.setRowCount(0);
        int stt = 1;
        for (SanPham_DTO sp : list) {
            modelSanPham.addRow(new Object[]{
                    stt++,
                    sp.getMaSP(),
                    sp.getTenSP(),
                    sp.getDonViTinh(),
                    sp.getLoiNhuan(),
                    sp.getKeDon() == 1 ? "Có" : "Không",
                    sp.getMaDM(), // Có thể dùng DanhMuc_BUS để lấy tên nếu cần
                    sp.getHinhAnh(),
                    sp.getTrangThai() == 1 ? "Đang bán" : "Ngừng bán"
            });
        }
        txtSanPhamHienCo.setText(String.valueOf(list.size()));
    }

    private void addEvents() {
        // 1. Click bảng -> Đổ dữ liệu
        tableSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableSanPham.getSelectedRow();
                if (row == -1) return;

                String maSP = tableSanPham.getValueAt(row, 1).toString();
                SanPham_DTO sp = spBUS.getById(maSP);
                if (sp != null) {
                    fillData(sp);
                    lockForm(true); // Chỉ xem, không sửa
                }
            }
        });

        // 2. Nút Thêm
        btnThem.addActionListener(e -> {
            clearForm();
            txtMaSanPham.setText(spBUS.getNextId());
            chucNangHienTai = "THEM";
            lockForm(false);
        });

        // 3. Nút Cập nhật
        btnCapNhat.addActionListener(e -> {
            if (txtMaSanPham.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Chọn sản phẩm cần sửa");
                return;
            }
            chucNangHienTai = "SUA";
            lockForm(false);
        });

        // 4. Nút Lưu
        btnLuu.addActionListener(e -> saveSanPham());

        // 5. Nút Hủy
        btnHuy.addActionListener(e -> {
            clearForm();
            lockForm(true);
        });

        // 6. Nút Tìm kiếm
        btnTimKiem.addActionListener(e -> {
            String keyword = txtNhapThongTin.getText();
            // Lấy các điều kiện lọc từ GUI...
            // spBUS.timKiem(...)
        });
    }

    // Đổ dữ liệu từ DTO lên Form
    private void fillData(SanPham_DTO sp) {
        txtMaSanPham.setText(sp.getMaSP());
        txtTenSanPham.setText(sp.getTenSP());
        cmbDonViTinh.setSelectedItem(sp.getDonViTinh());
        txtLoiNhuan.setText(String.valueOf(sp.getLoiNhuan()));
        cmbKeDon.setSelectedItem(sp.getKeDon() == 1 ? "Có" : "Không");
        cmbDanhMuc.setSelectedItem(sp.getMaDM());
        txtLinkHinhAnh.setText(sp.getHinhAnh());
        cmbTrangThai.setSelectedItem(sp.getTrangThai() == 1 ? "Đang bán" : "Ngừng bán");

        // --- XỬ LÝ QUY CÁCH (Quan trọng) ---
        QuyCach_DTO qc = qcBUS.getById(sp.getMaQC()); // Lấy thông tin quy cách
        if (qc != null) {
            txtSPTrongHop.setText(String.valueOf(qc.getSlTrongHop()));
            txtHopTrongThung.setText(String.valueOf(qc.getSlHopTrongThung()));
        } else {
            txtSPTrongHop.setText("0");
            txtHopTrongThung.setText("0");
        }
    }

    // Lưu dữ liệu (Thêm hoặc Sửa)
    private void saveSanPham() {
        SanPham_DTO sp = new SanPham_DTO();
        sp.setMaSP(txtMaSanPham.getText());
        sp.setTenSP(txtTenSanPham.getText());
        sp.setDonViTinh(cmbDonViTinh.getSelectedItem().toString());
        sp.setMaDM(cmbDanhMuc.getSelectedItem().toString()); // Lấy mã danh mục
        sp.setHinhAnh(txtLinkHinhAnh.getText());

        // Parse số liệu
        int slTrongHop = 0;
        int slHopTrongThung = 0;
        try {
            sp.setLoiNhuan(Double.parseDouble(txtLoiNhuan.getText()));
            slTrongHop = Integer.parseInt(txtSPTrongHop.getText());
            slHopTrongThung = Integer.parseInt(txtHopTrongThung.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ cho Lợi nhuận và Quy cách!");
            return;
        }

        sp.setKeDon(cmbKeDon.getSelectedItem().equals("Có") ? 1 : 0);
        sp.setTrangThai(cmbTrangThai.getSelectedItem().equals("Đang bán") ? 1 : 0);

        // Gọi BUS
        boolean result = false;
        if (chucNangHienTai.equals("THEM")) {
            // Truyền cả thông tin quy cách để BUS tự xử lý tạo/tìm QC
            result = spBUS.themSanPham(sp, slTrongHop, slHopTrongThung);
        } else if (chucNangHienTai.equals("SUA")) {
            result = spBUS.capNhatSanPham(sp, slTrongHop, slHopTrongThung);
        }

        if (result) {
            JOptionPane.showMessageDialog(this, "Thành công!");
            loadDataToTable(spBUS.getAll());
            lockForm(true);
            clearForm();
        }
    }

    private void lockForm(boolean lock) {
        txtMaSanPham.setEditable(!lock);
        txtTenSanPham.setEditable(!lock);
        txtLoiNhuan.setEditable(!lock);
        txtSPTrongHop.setEditable(!lock);
        txtHopTrongThung.setEditable(!lock);
        txtLinkHinhAnh.setEditable(!lock);
        cmbDonViTinh.setEnabled(!lock);
        cmbDanhMuc.setEnabled(!lock);
        cmbKeDon.setEnabled(!lock);
        cmbTrangThai.setEnabled(!lock);
        txtAreaThuocTinhRieng.setEditable(!lock);

        btnLuu.setEnabled(!lock);
        btnHuy.setEnabled(!lock);
        btnThem.setEnabled(lock);
        btnCapNhat.setEnabled(lock);
    }

    private void clearForm() {
        txtMaSanPham.setText("");
        txtTenSanPham.setText("");
        txtLoiNhuan.setText("");
        txtSPTrongHop.setText("");
        txtHopTrongThung.setText("");
        // Reset combos...
    }
}