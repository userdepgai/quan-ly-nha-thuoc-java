package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class QuanLyKhuyenMai_GUI extends JPanel {
    private JButton btnCapNhat;
    private JButton btnThem;
    private JLabel labelMaKhuyenMai;
    private JLabel labelTenKhuyenMai;
    private JTextField txtMaKhuyenMai;
    private JTextField txtTenKhuyenMai;
    private JComboBox cmbLoaiKhuyenMai;
    private JLabel labelLoaiKhuyenMai;
    private JLabel labelGiaTriKhuyenMai;
    private JTextField txtGiaTriKhuyenMai;
    private JLabel labelDoiTuongApDung;
    private JLabel labelApDungDanhMuc;
    private JTextField txtApDungDanhMuc;
    private JPanel panelDanhSachKhuyenMai;
    private JTable tableKhuyenMai;
    private JPanel panelTieuDe;
    private JLabel label_tieuDe;
    private JButton btnNhapExcel;
    private JButton btnXuatExcel;
    private JPanel panelBoLoc;
    private JLabel labelTimKiem;
    private JComboBox cmbTimTheo;
    private JComboBox cmbLocTrangThai;
    private JLabel labelLocTrangThai;
    private JComboBox cmbLocLoaiKhuyenMai;
    private JLabel labelLocLoaiKhuyenMai;
    private JLabel labelLocDoiTuongApDung;
    private JPanel panelQuanLyKhuyenMai;
    private JComboBox cmbLocDoiTuongApDung;
    private JLabel labelChuongTrinhKhuyenMai;
    private JComboBox cmbChuongTrinhKhuyenMai;
    private JComboBox cmbLocChuongTrinhKhuyenMai;
    private JLabel labelLocChuongTrinhKhuyenMai;
    private JPanel panelThongTinChiTiet;
    private JLabel labelApDungSanPham;
    private JTextField txtApDungSanPham;
    private JLabel labelTrangThai;
    private JComboBox cmbTrangThai;
    private JComboBox cmbDoiTuongApDung;
    private JPanel panelCapNhat;
    private JButton btnThoat;
    private JButton btnTimkiem;
    private JTextField txtNhapThongTin;
    private JLabel labelNhapThongTin;
    private JTextField txtKhuyenMaiHienCo;
    private JLabel labelKhuyenMaiHienCo;
    private DefaultTableModel modelKhuyenMai;

    public QuanLyKhuyenMai_GUI(){
        this.setLayout(new BorderLayout());
        if (panelQuanLyKhuyenMai != null) {
            this.add(panelQuanLyKhuyenMai, BorderLayout.CENTER);
        }
        initTable_KhuyenMai();
    }

    private void initTable_KhuyenMai() {
        String[] headers = {"STT", "Mã Khuyến Mãi", "Tên Khuyến Mãi", "Loại Khuyến Mãi", "Đối Tượng Áp Dụng", "Chương Trình Khuyến Mãi", "Trạng Thái"};
        modelKhuyenMai = new DefaultTableModel(headers, 0); // '0' là số hàng ban đầu

        // 2. Gán model vào table
        tableKhuyenMai.setModel(modelKhuyenMai);
    }
}
