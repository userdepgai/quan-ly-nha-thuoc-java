package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ThuocTinhDanhMuc_GUI extends JPanel{
    private JButton btnCapNhat;
    private JButton btnThemThuocTinh;
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
    private JLabel labelNhapThongTin;
    private JTextField txtNhapThongTin;
    private JLabel labelLocDanhMuc;
    private JLabel labelLocTrangThai;
    private JComboBox cmbLocTrangThai;
    private JButton btnTimKiem;
    private JButton btnThoat;
    private JLabel labelThuocTinhHienCo;
    private JTextField txtThuocTinhHienCo;
    private JLabel labelMaGiaTri;
    private JLabel labelNoiDungGiaTri;
    private JButton btnThemGiaTri;
    private JTextField textField1;
    private JTextField textField2;
    private JButton btnSuaGiaTri;
    private JButton btnHuyGiaTri;
    private JScrollPane tableDanhSachGiaTriThuocTinh;
    private DefaultTableModel modelThuocTinh;
    private DefaultTableModel modelGiaTriThuocTinh;

    public ThuocTinhDanhMuc_GUI(){
        this.setLayout(new BorderLayout());
        if (panelQuanLyThuocTinh != null) {
            this.add(panelQuanLyThuocTinh, BorderLayout.CENTER);
        }
        initTable_ThuocTinh();
        initTable_GiaTriThuocTinh();
    }

    private void initTable_ThuocTinh() {
        String[] headers = {"STT", "Mã Thuộc Tính", "Tên Thuộc Tính", "Kiểu Thuộc Tính", "Danh Mục", "Trạng Thái"};
        modelThuocTinh = new DefaultTableModel(headers, 0); // '0' là số hàng ban đầu

        // 2. Gán model vào table
        tableThuocTinh.setModel(modelThuocTinh);
    }
    private void initTable_GiaTriThuocTinh() {
        String[] headers = {"STT", "Mã Giá Trị", "Nội Dung Giá Trị", "Thuộc Tính", "Trạng Thái"};
        modelGiaTriThuocTinh = new DefaultTableModel(headers, 0); // '0' là số hàng ban đầu

        // 2. Gán model vào table
        tableGiaTriThuocTinh.setModel(modelGiaTriThuocTinh);
    }
}
