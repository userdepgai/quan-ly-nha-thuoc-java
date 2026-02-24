package gui.HOADON_GUI;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DuyetDonHang_GUI extends JPanel{
    private JPanel panel_DuyetDonHang;
    private JButton btnXuatExcel;
    private JButton btnNhapExcel;
    private JTextField txtHienCo;
    private JTable tableDSHDChoCN;
    private JTable tableTTCTDonDat;
    private JTextField txtThanhTienCT;
    private JTextField txtTTHienTai;
    private JButton btnCapNhat;
    private JComboBox cbTrangThaiCN;
    private JTextField txtNgayLap;
    private JTextField txtTenKH;
    private JTextField txtSDT;
    private JTextField txtDCGiaoHang;
    private JTextField txtTongTien;
    private JTextField txtVoucher;
    private JTextField txtDiemThuong;
    private JTextField txtTongGTKM;
    private JTextField txtThueVAT;
    private JTextField txtPhiVC;
    private JTextField txtThanhTien;
    private JTextField txtTTTT;
    private JComboBox cbTimTheo;
    private JComboBox cbGia;
    private JTextField txtNhapTT;
    private JComboBox cbTTTT;
    private JComboBox cbTrangThai;
    private JButton btnTiemKiem;
    private JButton btnReset;
    private JLabel labelTimTheo;
    private JLabel labelGia;
    private JLabel labelNhapTT;
    private JLabel labelTTTT;
    private JLabel labelTrangThai;
    private JLabel labelTuNgay;
    private JLabel labelDenNgay;
    private JLabel labelHienCo;
    private JLabel labelNgayLap;
    private JLabel labelTenKH;
    private JLabel labelSDT;
    private JLabel labelDCGIaoHang;
    private JLabel labelTongTien;
    private JTextField txtGhiChu;
    private JLabel labelVoucher;
    private JLabel labelDiemThuong;
    private JLabel labelPhiVC;
    private JLabel labelThueVAT;
    private JLabel labelThanhTien;
    private JLabel labelTrangThaiHienTai;
    private JLabel labelGhiChu;
    private JLabel labelThahTienCT;
    private JLabel labelTrangThaiCN;
    private JPanel JPanel;
    private JLabel labelTongGTKM;
    private JLabel labelTTTTXem;
    private JLabel labelDuyetDonHang;
    private JPanel labelTimKimHoaDon;

    private DefaultTableModel modelHoaDon;
    private DefaultTableModel modelChiTiet;

    public DuyetDonHang_GUI() {
        setLayout(new BorderLayout());
        add(panel_DuyetDonHang, BorderLayout.CENTER);

        khoiTaoBangHoaDon();
        khoiTaoBangChiTiet();
        suKienBangHoaDon();
        suKienCapNhat();
    }

    private void khoiTaoBangHoaDon() {
        modelHoaDon = new DefaultTableModel(
                new String[]{
                        "STT", "Mã hóa đơn", "Tên KH",
                        "Ngày lập", "NV lập",
                        "SDT", "TT thanh toán",
                        "Trạng thái", "Thành tiền"
                }, 0
        );

        tableDSHDChoCN.setModel(modelHoaDon);

        modelHoaDon.addRow(new Object[]{
                1, "HD001", "Nguyễn Văn A",
                "16/02/2026", "NV01",
                "0909123456", "Chưa TT",
                "Chờ duyệt", 200000
        });
    }

    private void khoiTaoBangChiTiet() {
        modelChiTiet = new DefaultTableModel(
                new String[]{
                        "STT", "Mã SP", "Tên SP",
                        "SL", "Giá bán",
                        "Khuyến mãi",
                        "Giá sau KM",
                        "Thành tiền"
                }, 0
        );

        tableTTCTDonDat.setModel(modelChiTiet);
    }

    private void suKienBangHoaDon() {
        tableDSHDChoCN.getSelectionModel()
                .addListSelectionListener(e -> {

                    int row = tableDSHDChoCN.getSelectedRow();
                    if (row < 0) return;

                    String maHD =
                            modelHoaDon.getValueAt(row, 1).toString();

                    loadChiTietHoaDon(maHD);
                });
    }

    private void loadChiTietHoaDon(String maHD) {

        modelChiTiet.setRowCount(0);

        modelChiTiet.addRow(new Object[]{
                1, "SP01", "Paracetamol",
                2, 10000,
                0, 10000, 20000
        });

        txtTenKH.setText("Nguyễn Văn A");
        txtSDT.setText("0909123456");
        txtNgayLap.setText("16/02/2026");
        txtTongTien.setText("200000");
    }

    private void suKienCapNhat() {

        btnCapNhat.addActionListener(e -> {

            int row = tableDSHDChoCN.getSelectedRow();

            if (row < 0) {
                JOptionPane.showMessageDialog(
                        null,
                        "Chọn hóa đơn trước!"
                );
                return;
            }

            String maHD =
                    modelHoaDon.getValueAt(row, 1).toString();

            String trangThai =
                    cbTrangThaiCN.getSelectedItem() + "";

            JOptionPane.showMessageDialog(
                    null,
                    "Đã cập nhật trạng thái: " + trangThai
            );
        });
    }
}
