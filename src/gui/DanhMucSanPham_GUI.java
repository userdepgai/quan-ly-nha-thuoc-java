package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DanhMucSanPham_GUI extends JPanel {
    private JPanel panelDanhMucSanPham;
    private JLabel labelDanhMucSanPham;
    private JButton btnXuatExcel;
    private JButton btnNhapExcel;
    private JTable tableDanhMuc;
    private JButton btnCapNhat;
    private JButton btnThemDanhMuc;
    private JTable tableDanhSachThuocTinh;
    private JLabel labelMaDanhMuc;
    private JLabel labelTenDanhMuc;
    private JLabel labelTrangThai;
    private JTextField txtMaDanhMuc;
    private JTextField txtTenDanhMuc;
    private JPanel panelBoLoc;
    private JLabel labelTimKiem;
    private JComboBox cmbTimTheo;
    private JLabel labelNhapThongTin;
    private JTextField txtNhapThongTin;
    private JPanel panelTieuDe;
    private JPanel panelDanhSachDanhMuc;
    private JPanel panelThongTinChiTiet;
    private JPanel panelThongTinDanhMuc;
    private JPanel panelDanhSachThuocTinh;
    private JPanel panelCapNhat;
    private JLabel labelLocTrangThai;
    private JComboBox cmbTrangThai;
    private JLabel labelDanhMucHienCo;
    private JTextField txtDanhMucHienCo;
    private JTable tableThuocTinh;
    private JButton btnThoat;
    private JComboBox cmbLocTrangThai;
    private JButton btnTimKiem;
    private DefaultTableModel modelDanhMuc;
    private DefaultTableModel modelThuocTinh;


    public DanhMucSanPham_GUI(){
        this.setLayout(new BorderLayout());
        if (panelDanhMucSanPham != null) {
            this.add(panelDanhMucSanPham, BorderLayout.CENTER);
        }
        initTable_DanhMuc();
        initTable_ThuocTinh();
    }

    private void initTable_DanhMuc() {
        String[] headers = {"STT", "Mã Danh Mục", "Tên Danh Mục", "Trạng Thái"};
        modelDanhMuc = new DefaultTableModel(headers, 0); // '0' là số hàng ban đầu

        // 2. Gán model vào table
        tableDanhMuc.setModel(modelDanhMuc);
    }
    private void initTable_ThuocTinh() {
        String[] headers = {"STT", "Mã Thuộc Tính", "Tên Thuộc Tính", "Kiểu Thuộc Tính", "Danh Mục"};
        modelThuocTinh = new DefaultTableModel(headers, 0); // '0' là số hàng ban đầu

        // 2. Gán model vào table
        tableThuocTinh.setModel(modelThuocTinh);

    }
}