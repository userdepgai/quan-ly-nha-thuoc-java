package gui.HOADON_GUI;

import javax.swing.*;

public class XuatHoaDon_GUI extends JDialog{
    private JPanel panel_XuatHoaDon;
    private JTextField textNgayInPhieu;
    private JTextField txtMaHD;
    private JTextField txtNVLap;
    private JTextField txtTenKH;
    private JTable tableTTSP;
    private JTextField txtTongTien;
    private JTextField txtVoucher;
    private JTextField txtGTDT;
    private JTextField txtTongGTKM;
    private JTextField txtThanhTien;
    private JTextField txtDiemThuongNhan;
    private JLabel labelHTNT;
    private JLabel labelNgayInPhieu;
    private JLabel labelTTHD;
    private JLabel labeMaHDl;
    private JLabel labelNVLap;
    private JLabel labelTenKH;
    private JLabel labelKeToa;
    private JPanel JPanel;
    private JLabel labelTongTien;
    private JLabel labelVoucher;
    private JLabel labelGTDT;
    private JLabel labelTongGTKM;
    private JLabel labelThanhTien;
    private JLabel labelDiemThuongNhan;
    private JLabel labelSDTKH;
    private JTextField txtSDTKH;
    private JButton btnXuatBDF;

    public XuatHoaDon_GUI(JFrame parent) {
        super(parent, "Xuất hóa đơn", true);

        setContentPane(panel_XuatHoaDon);
        setSize(500, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
