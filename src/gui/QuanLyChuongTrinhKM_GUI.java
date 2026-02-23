package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class QuanLyChuongTrinhKM_GUI extends JPanel {
    private JPanel panelTieuDe;
    private JLabel label_tieuDe;
    private JButton btnNhapExcel;
    private JButton btnXuatExcel;
    private JPanel panelDanhSachChuongTrinhKhuyenMai;
    private JTable tableChuongTrinhKhuyenMai;
    private JPanel panelThongTinChiTiet;
    private JPanel panelChuongTrinhKhuyenMai;
    private JLabel labelMaChuongTrinhKhuyenMai;
    private JLabel labelTenChuongTrinhKhuyenMai;
    private JLabel labelTrangThai;
    private JTextField txtMaChuongTrinhKhuyenMai;
    private JTextField txtTenChuongTrinhKhuyenMai;
    private JComboBox cmbTrangThai;
    private JPanel panelDanhSachKhuyenMai;
    private JTable tableKhuyenMai;
    private JPanel panelCapNhat;
    private JButton btnCapNhat;
    private JButton btnThemThuocTinh;
    private JPanel panelBoLoc;
    private JLabel labelTimTheo;
    private JComboBox cmbTimTheo;
    private JLabel labelNhapThongTin;
    private JTextField txtNhapThongTin;
    private JLabel labelLocTrangThai;
    private JComboBox cmbLocTrangThai;
    private JButton btnTimKiem;
    private JButton btnThoat;
    private JPanel panelQuanLyChuongTrinhKhuyenMai;
    private JLabel labelLocNgayBatDau;
    private JTextField txtLocNgayBatDau;
    private JLabel labelLocNgayKetThuc;
    private JTextField txtLocNgayKetThuc;
    private JLabel labelNgayBatDau;
    private JTextField txtNgayBatDau;
    private JLabel labelNgayKetThuc;
    private JTextField txtNgayKetThuc;
    private JLabel labelChuongTrinhKhuyenMaiHienCo;
    private JTextField txtChuongTrinhKhuyenMaiHienCo;
    private DefaultTableModel modelChuongTrinhKhuyenMai;
    private DefaultTableModel modelKhuyenMai;

    public QuanLyChuongTrinhKM_GUI(){
        this.setLayout(new BorderLayout());
        if (panelQuanLyChuongTrinhKhuyenMai != null) {
            this.add(panelQuanLyChuongTrinhKhuyenMai, BorderLayout.CENTER);
        }
        initTable_ChuongTrinhKhuyenMai();
        initTable_KhuyenMai();
    }

    private void initTable_ChuongTrinhKhuyenMai() {
        String[] headers = {"STT", "Mã Khuyến Mãi", "Tên Khuyến Mãi", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Trạng Thái"};
        modelChuongTrinhKhuyenMai = new DefaultTableModel(headers, 0); // '0' là số hàng ban đầu

        // 2. Gán model vào table
        tableChuongTrinhKhuyenMai.setModel(modelChuongTrinhKhuyenMai);
    }
    private void initTable_KhuyenMai() {
        String[] headers = {"STT", "Mã Khuyến Mãi", "Tên Khuyến Mãi", "Loại Khuyến Mãi", "Trạng Thái"};
        modelKhuyenMai = new DefaultTableModel(headers, 0); // '0' là số hàng ban đầu

        // 2. Gán model vào table
        tableKhuyenMai.setModel(modelKhuyenMai);
    }

}
