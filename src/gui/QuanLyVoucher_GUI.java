package gui;

import bus.Voucher_BUS;
import dto.Voucher_DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class QuanLyVoucher_GUI extends JPanel {
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
    private JComboBox<String> cmbLoaiVoucher;
    private JLabel labelLoaiVoucher;
    private JLabel labelGiaTriVoucher;
    private JTextField txtGiaTriVoucher;
    private JLabel labelNgayBatDau;
    private JTextField txtNgayBatDau;
    private JTextField txtNgayKetThuc;
    private JLabel labelNgayKetThuc;
    private JComboBox cmbTrangThai; // ComboBox lọc trạng thái (theo thiết kế form của bạn)
    private JLabel labelTimKiem;
    private JComboBox cmbTimTheo;
    private JComboBox cmbLoaiGiam;
    private JLabel labelLoaiGiam;
    private JLabel labelLocTrangThai;
    private JComboBox<String> cmbLocTrangThai;
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
    private JLabel labelTrangThai;
    private DefaultTableModel modelVoucher;

    // KHAI BÁO BUS VÀ ĐỊNH DẠNG
    private final Voucher_BUS vBUS = Voucher_BUS.getInstance();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private final DecimalFormat df = new DecimalFormat("#,###");

    public QuanLyVoucher_GUI(){
        this.setLayout(new BorderLayout());
        if (panelQuanLyVoucher != null) {
            this.add(panelQuanLyVoucher, BorderLayout.CENTER);
        }

        initTable_Voucher();
        initComboBox_Voucher();

        // Đổ dữ liệu lên bảng
        loadDataToTable_Voucher();

        // Gắn sự kiện
        addEvents();
    }

    private void initTable_Voucher() {
        String[] headers = {"STT", "Mã Voucher", "Tên Voucher", "Loại Voucher", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Trạng Thái"};
        modelVoucher = new DefaultTableModel(headers, 0);
        tableVoucher.setModel(modelVoucher);
    }

    private void initComboBox_Voucher() {
        // 1. Nạp dữ liệu cho ô Loại Voucher (Chi tiết)
        if (cmbLoaiVoucher != null) {
            cmbLoaiVoucher.removeAllItems();
            cmbLoaiVoucher.addItem("Phần trăm");
            cmbLoaiVoucher.addItem("Tiền mặt");
        }

        // 2. Nạp dữ liệu cho ô Trạng thái (Chi tiết - Ô đang bị lỗi của bạn)
        if (cmbTrangThai != null) {
            cmbTrangThai.removeAllItems();
            cmbTrangThai.addItem("Đang áp dụng");
            cmbTrangThai.addItem("Ngưng áp dụng");
        }

        // 3. Nạp dữ liệu cho ô comboBox1 (Ô lọc Trạng thái ở phía trên)
        if (cmbLocTrangThai != null) {
            cmbLocTrangThai.removeAllItems();
            cmbLocTrangThai.addItem("Tất cả");
            cmbLocTrangThai.addItem("Đang áp dụng");
            cmbLocTrangThai.addItem("Ngưng áp dụng");
        }
    }

    // ==============================================================
    // HÀM ĐỔ DỮ LIỆU LÊN BẢNG
    // ==============================================================
    private void loadDataToTable_Voucher() {
        modelVoucher.setRowCount(0);
        ArrayList<Voucher_DTO> list = vBUS.getAll();

        int stt = 1;
        for (Voucher_DTO v : list) {
            String loaiStr = (v.getLoaiVoucher() == 0) ? "Phần trăm" : "Tiền mặt";
            String trangThaiStr = (v.getTrangThai() == 1) ? "Đang áp dụng" : "Ngưng áp dụng";

            modelVoucher.addRow(new Object[]{
                    stt++,
                    v.getMa(),
                    v.getTen(),
                    loaiStr,
                    (v.getNgayBatDau() != null) ? sdf.format(v.getNgayBatDau()) : "",
                    (v.getNgayKetThuc() != null) ? sdf.format(v.getNgayKetThuc()) : "",
                    trangThaiStr
            });
        }

        if (txtVoucherHienCo != null) {
            txtVoucherHienCo.setText(String.valueOf(list.size()));
        }
    }

    // ==============================================================
    // HÀM BẮT SỰ KIỆN CLICK CHUỘT
    // ==============================================================
    private void addEvents() {
        tableVoucher.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableVoucher.getSelectedRow();
                if (row >= 0) {
                    // Lấy mã từ cột 1 của bảng
                    String ma = modelVoucher.getValueAt(row, 1).toString();

                    // Gọi BUS lấy đối tượng đầy đủ
                    Voucher_DTO v = vBUS.getById(ma);

                    if (v != null) {
                        // Đổ dữ liệu cơ bản
                        if (txtMaVoucher != null) txtMaVoucher.setText(v.getMa());
                        if (txtTenVoucher != null) txtTenVoucher.setText(v.getTen());
                        if (txtDonToiThieu != null) txtDonToiThieu.setText(String.format("%.0f", v.getDonToiThieu()));

                        // Đổ ngày tháng
                        if (txtNgayBatDau != null)
                            txtNgayBatDau.setText(v.getNgayBatDau() != null ? sdf.format(v.getNgayBatDau()) : "");
                        if (txtNgayKetThuc != null)
                            txtNgayKetThuc.setText(v.getNgayKetThuc() != null ? sdf.format(v.getNgayKetThuc()) : "");

                        // Đổ Loại Voucher và Giá trị (Dịch thuật 0.1 -> 10)
                        if (cmbLoaiVoucher != null) cmbLoaiVoucher.setSelectedIndex(v.getLoaiVoucher());

                        if (txtGiaTriVoucher != null) {
                            if (v.getLoaiVoucher() == 0) { // Phần trăm
                                txtGiaTriVoucher.setText(String.valueOf((int)(v.getGiaTriVoucher() * 100)));
                            } else { // Tiền mặt
                                txtGiaTriVoucher.setText(String.format("%.0f", v.getGiaTriVoucher()));
                            }
                        }

                        // Đổ Trạng thái
                        if (cmbTrangThai != null) {
                            String trangThaiStr = (v.getTrangThai() == 1) ? "Đang áp dụng" : "Ngưng áp dụng";
                            cmbTrangThai.setSelectedItem(trangThaiStr);
                        }

                        // Khóa ô mã không cho sửa
                        if (txtMaVoucher != null) txtMaVoucher.setEditable(false);
                    }
                }
            }
        });
    }
}