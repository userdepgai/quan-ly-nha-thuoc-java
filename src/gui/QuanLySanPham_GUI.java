package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class QuanLySanPham_GUI extends JPanel {
    private JPanel panelTieuDe;
    private JLabel label_tieuDe;
    private JButton btnNhapExcel;
    private JButton btnXuatExcel;
    private JPanel panelBoLoc;
    private JLabel labelTimKiem;
    private JComboBox cmbTimTheo;
    private JLabel labelNhapThongTin;
    private JTextField txtNhapThongTin;
    private JButton btnTimkiem;
    private JLabel labelLocPhanTramLoiNhuan;
    private JButton btnThoat;
    private JLabel labelLocTrangThai;
    private JComboBox cmbLocTrangThai;
    private JComboBox cmbLocPhanTramLoiNhuan;
    private JLabel labelSanPhamHienCo;
    private JTextField txtSanPhamHienCo;
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
    private JButton btnThemKhuyenMai;
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
    private JTextField textField1;
    private JLabel labelTrangThai;
    private JComboBox cmbTrangThai;
    private JTextArea txtAreaThuocTinhRieng;
    private JLabel labelHopTrongThung;
    private JLabel labelSPTrongHop;
    private JPanel panelQuyCach;
    private JLabel labelLinkHinhAnh;
    private JTextField txtLinkHinhAnh;
    private DefaultTableModel modelSanPham;

    public QuanLySanPham_GUI(){
        this.setLayout(new BorderLayout());
        if (panelQuanLySanPham != null) {
            this.add(panelQuanLySanPham, BorderLayout.CENTER);
        }
        initTable_SanPham();
    }

    private void initTable_SanPham() {
        String[] headers = {"STT", "Mã Sản Phẩm", "Tên Sản Phẩm", "Đơn Vị Tính", "Lợi Nhuận", "Danh Mục", "Hình Ảnh", "Trạng Thái"};
        modelSanPham = new DefaultTableModel(headers, 0); // '0' là số hàng ban đầu

        // 2. Gán model vào table
        tableSanPham.setModel(modelSanPham);
    }
}
