package gui.HOADON_GUI;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class QuanLyHoaDonBan_GUI extends JPanel {
    private JPanel panel_QuanLyHDB;
    private JButton btnXuatExcel;
    private JButton btnNhapExcel;
    private JComboBox cbTimTheo;
    private JComboBox cbGia;
    private JTextField txtNhapTT;
    private JComboBox cbLoaiHD;
    private JButton btnTimKiem;
    private JButton btnReset;
    private JTextField txtHienCo;
    private JTable tableDSHD;
    private JTextField txtNgayLap;
    private JTextField txtTenKH;
    private JTextField txtSDT;
    private JTextField txtDCGiaoHang;
    private JTextField txtGhiChu;
    private JTextField txtTongTien;
    private JTextField txtVoucher;
    private JTextField txtDiemThuong;
    private JTextField txtTongGTKM;
    private JTextField txtThueVAT;
    private JTextField txtPhiVC;
    private JTextField txtThanhTien;
    private JTable tableTTCTHD;
    private JLabel labelTimTheo;
    private JLabel labelNhapTT;
    private JComboBox cbTrangThai;
    private JComboBox cbTTTT;
    private JLabel labelTrangThai;
    private JLabel labelLoaiHD;
    private JLabel labelTTTT;
    private JLabel labelTuNgay;
    private JLabel labelDenNgay;
    private JLabel labelHienCo;
    private JTextField txtThanhTienCT;
    private JLabel labelNgayLap;
    private JLabel labelTenKH;
    private JLabel labelSDT;
    private JLabel labelGhiChu;
    private JLabel labelTongTIen;
    private JLabel labelVoucher;
    private JLabel labelDiemThuong;
    private JLabel labelTongGTKM;
    private JLabel labelPhiVC;
    private JLabel labelThueVAT;
    private JLabel labelThanhTien;
    private JLabel labelDCGiaoHang;
    private JLabel labelThanhTienCT;
    private JLabel labelGia;
    private JDateChooser JDateChooser1;
    private JDateChooser JDateChooser2;

    private DefaultTableModel modelHoaDon;
    private DefaultTableModel modelChiTiet;

    public QuanLyHoaDonBan_GUI() {
        setLayout(new BorderLayout());
        add(panel_QuanLyHDB, BorderLayout.CENTER);

        khoiTaoBangHoaDon();
        khoiTaoBangChiTiet();
        suKienChonHoaDon();
    }

    private void khoiTaoBangHoaDon() {
        modelHoaDon = new DefaultTableModel(
                new String[]{
                        "STT", "Mã HD", "Tên KH",
                        "Ngày lập", "SDT",
                        "Thanh toán", "Trạng thái",
                        "Thành tiền"
                }, 0
        );

        tableDSHD.setModel(modelHoaDon);

        // dữ liệu demo
        modelHoaDon.addRow(new Object[]{
                1, "HD001", "Nguyễn Văn A",
                "16/02/2026", "0909123456",
                "Đã TT", "Hoàn thành",
                350000
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

        tableTTCTHD.setModel(modelChiTiet);
    }

    private void suKienChonHoaDon() {
        tableDSHD.getSelectionModel()
                .addListSelectionListener(e -> {

                    int row = tableDSHD.getSelectedRow();
                    if (row < 0) return;

                    String maHD =
                            modelHoaDon.getValueAt(row, 1).toString();

                    hienChiTiet(maHD);
                });
    }

    private void hienChiTiet(String maHD) {

        modelChiTiet.setRowCount(0);

        // demo
        modelChiTiet.addRow(new Object[]{
                1, "SP01", "Paracetamol",
                2, 10000,
                0, 10000, 20000
        });

        txtTenKH.setText("Nguyễn Văn A");
        txtSDT.setText("0909123456");
        txtNgayLap.setText("16/02/2026");
        txtTongTien.setText("350000");
    }
    private void createUIComponents() {
        JDateChooser1 = new com.toedter.calendar.JDateChooser();
        JDateChooser1.setDateFormatString("dd/MM/yyyy");

        JDateChooser2 = new com.toedter.calendar.JDateChooser();
        JDateChooser2.setDateFormatString("dd/MM/yyyy");
    }
}
