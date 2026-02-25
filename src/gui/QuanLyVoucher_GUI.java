package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class QuanLyVoucher_GUI extends JPanel{
    private JLabel label_tieuDe;
    private JPanel panelBoLoc;
    private JButton btnCapNhat;
    private JButton btnThem;
    private JTextField txtMaVoucher;
    private JTextField txtTenVoucher;
    private JPanel panelQuanLyVoucher;
    private JButton btnNhapExcel;
    private JButton btnXuatExcel;
    private JTable tableVoucher;
    private JLabel labelMaVoucher;
    private JLabel labelTenVoucher;
    private JComboBox cmbLoaiVoucher;
    private JLabel labelLoaiVoucher;
    private JLabel labelGiaTriVoucher;
    private JTextField txtGiaTriVoucher;
    private JLabel labelNgayBatDau;
    private JTextField txtNgayBatDau;
    private JTextField txtNgayKetThuc;
    private JLabel labelNgayKetThuc;
    private JComboBox comboBox1;
    private JLabel labelTimKiem;
    private JComboBox cmbTimTheo;
    private JComboBox cmbLoaiGiam;
    private JLabel labelLoaiGiam;
    private JLabel labelTrangThai;
    private JComboBox cmbTrangThai;
    private JTextField txtLocNgayBatDau;
    private JTextField txtLocNgayKetThuc;
    private JLabel labelLocNgayBatDau;
    private JLabel labelLocNgayKetThuc;
    private JPanel panelDanhSachVoucher;
    private JPanel panelTieuDe;
    private JPanel panelCapNhat;
    private JPanel panelThongTinChiTiet;
    private JButton btnThoat;
    private JButton btnTimkiem;
    private JTextField txtNhapThongTin;
    private JLabel labelNhapThongTin;
    private JTextField txtVoucherHienCo;
    private JLabel labelVoucherHienCo;
    private JTextField txtDonToiThieu;
    private JLabel labelDonToiThieu;
    private DefaultTableModel modelVoucher;

    public QuanLyVoucher_GUI(){
        this.setLayout(new BorderLayout());
        if (panelQuanLyVoucher != null) {
            this.add(panelQuanLyVoucher, BorderLayout.CENTER);
        }
        initTable_Voucher();
    }

    private void initTable_Voucher() {
        String[] headers = {"STT", "Mã Voucher", "Tên Voucher", "Loại Voucher", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Trạng Thái"};
        modelVoucher = new DefaultTableModel(headers, 0); // '0' là số hàng ban đầu

        // 2. Gán model vào table
        tableVoucher.setModel(modelVoucher);
    }

}
