package gui.HOADON_GUI;

import bus.HoaDonOnline_BUS;
import bus.KhachHang_BUS;
import bus.SanPham_BUS;
import com.toedter.calendar.JDateChooser;
import dto.HoaDonOnline_DTO;

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
    private JDateChooser JDateChooser1;
    private JDateChooser JDateChooser2;
    private JScrollPane scrTTCTDonDat;
    private JScrollPane scrDSHDChoCN;

    private DefaultTableModel modelHoaDon;
    private DefaultTableModel modelChiTiet;
    private HoaDonOnline_BUS bus =
            HoaDonOnline_BUS.getInstance();
    private KhachHang_BUS khBUS = KhachHang_BUS.getInstance();
    private SanPham_BUS spBUS = SanPham_BUS.getInstance();

    public DuyetDonHang_GUI() {
        setLayout(new BorderLayout());
        add(panel_DuyetDonHang, BorderLayout.CENTER);

        khoiTaoBangHoaDon();
        khoiTaoBangChiTiet();


        loadDanhSachHoaDonOnline();

        suKienBangHoaDon();
        //suKienCapNhat();

    }

    private void khoiTaoBangHoaDon() {
        modelHoaDon = new DefaultTableModel(
                new String[]{
                        "STT", "Mã Hóa đơn ", "Tên Khách hàng",
                        "Ngày lập", "SDT","Mã nhân viên lập",
                        "Thanh toán", "Trạng thái",
                        "Thành tiền"
                }, 0
        ){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 🔥 khóa toàn bộ bảng
            }
        };
        tableDSHDChoCN.setModel(modelHoaDon);
        tableDSHDChoCN.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tableDSHDChoCN.getColumnModel().getColumn(0).setPreferredWidth(50);   // STT
        tableDSHDChoCN.getColumnModel().getColumn(1).setPreferredWidth(100);  // Mã HD
        tableDSHDChoCN.getColumnModel().getColumn(2).setPreferredWidth(200);  // Tên KH
        tableDSHDChoCN.getColumnModel().getColumn(3).setPreferredWidth(200);  // Ngày lập
        tableDSHDChoCN.getColumnModel().getColumn(4).setPreferredWidth(100);  // SDT
        tableDSHDChoCN.getColumnModel().getColumn(5).setPreferredWidth(100);  // NV
        tableDSHDChoCN.getColumnModel().getColumn(6).setPreferredWidth(140);  // Thanh toán
        tableDSHDChoCN.getColumnModel().getColumn(7).setPreferredWidth(120);  // Trạng thái
        tableDSHDChoCN.getColumnModel().getColumn(8).setPreferredWidth(150); // Thành tiền

        tableDSHDChoCN.getTableHeader().setResizingAllowed(false);
        tableDSHDChoCN.getTableHeader().setReorderingAllowed(false);
        tableDSHDChoCN.setRowHeight(25);
        tableDSHDChoCN.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrDSHDChoCN.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrDSHDChoCN.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));

        tableDSHDChoCN.setCellSelectionEnabled(false);
        tableDSHDChoCN.setRowSelectionAllowed(true);
        tableDSHDChoCN.setColumnSelectionAllowed(false);


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
        ){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableTTCTDonDat.setModel(modelChiTiet);
        tableTTCTDonDat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tableTTCTDonDat.getColumnModel().getColumn(0).setPreferredWidth(30);
        tableTTCTDonDat.getColumnModel().getColumn(1).setPreferredWidth(70);
        tableTTCTDonDat.getColumnModel().getColumn(2).setPreferredWidth(139);
        tableTTCTDonDat.getColumnModel().getColumn(3).setPreferredWidth(30);
        tableTTCTDonDat.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableTTCTDonDat.getColumnModel().getColumn(5).setPreferredWidth(100);
        tableTTCTDonDat.getColumnModel().getColumn(6).setPreferredWidth(90);
        tableTTCTDonDat.getColumnModel().getColumn(7).setPreferredWidth(90);


        tableTTCTDonDat.getTableHeader().setResizingAllowed(false);
        tableTTCTDonDat.getTableHeader().setReorderingAllowed(false);
        tableTTCTDonDat.setRowHeight(25);
        tableTTCTDonDat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrTTCTDonDat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrTTCTDonDat.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));


    }
    private void loadDanhSachHoaDonOnline(){

        modelHoaDon.setRowCount(0);

        int stt = 1;

        for(HoaDonOnline_DTO hd : bus.getDanhSachDuyetOnline()){

            modelHoaDon.addRow(new Object[]{
                    stt++,
                    hd.getMa(),
                    bus.getTenKH(hd.getMaKhachHang()),
                    hd.getNgayLap(),
                    bus.getSDT(hd.getMaKhachHang()),
                    hd.getMaNhanVien(),
                    hd.getTinhTrangThanhToanText(),
                    hd.getTrangThaiText(),
                    hd.getThanhTien()
            });
        }

    }

    private void suKienBangHoaDon() {

        tableDSHDChoCN.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (e.getValueIsAdjusting()) return;

                    int row = tableDSHDChoCN.getSelectedRow();
                    if (row < 0) return;

                    String maHD =
                            modelHoaDon.getValueAt(row, 1).toString();

                    loadChiTietHoaDon(maHD);
                });
    }
    private void loadChiTietHoaDon(String maHD) {

        modelChiTiet.setRowCount(0);

        var ds = bus.getChiTietHoaDon(maHD);

        int stt = 1;

        for(var ct : ds){

            modelChiTiet.addRow(new Object[]{
                    stt++,
                    ct.getMaSP(),
                    bus.getTenSP(ct.getMaSP()),
                    ct.getSoLuong(),
                    ct.getGiaBan(),
                    ct.getMaKhuyenMai(),
                    ct.getGiaBanSauApKM(),
                    ct.getThanhTien()
            });
        }

        HoaDonOnline_DTO hd = bus.getHoaDonOnline(maHD);

        txtTenKH.setText(bus.getTenKH(hd.getMaKhachHang()));
        txtSDT.setText(bus.getSDT(hd.getMaKhachHang()));
        txtNgayLap.setText(hd.getNgayLap().toString());
        txtTongTien.setText(String.valueOf(hd.getTongTienGoc()));
        txtPhiVC.setText(String.valueOf(hd.getPhiVanChuyen()));
        txtThueVAT.setText(String.valueOf(hd.getThueVAT()));
        txtThanhTien.setText(String.valueOf(hd.getThanhTien()));

    }

    private void suKienCapNhat() {

        btnCapNhat.addActionListener(e -> {

            int row = tableDSHDChoCN.getSelectedRow();

            if (row < 0) {
                JOptionPane.showMessageDialog(null,
                        "Chọn hóa đơn trước!");
                return;
            }

            // ===== LẤY MÃ HD =====
            String maHD =
                    modelHoaDon.getValueAt(row, 1).toString();

            // ===== LẤY DTO THẬT (KHÔNG ĐỌC TỪ TABLE) =====
            HoaDonOnline_DTO hd =
                    bus.getHoaDonOnline(maHD);

            if (hd == null) {
                JOptionPane.showMessageDialog(null,
                        "Không tìm thấy hóa đơn!");
                return;
            }

            int trangThaiHienTai = hd.getTrangThai();
            int trangThaiMoi;

            // ===== LOGIC CHUYỂN TRẠNG THÁI =====
            switch (trangThaiHienTai) {

                case HoaDonOnline_DTO.TT_CHO_DUYET ->
                        trangThaiMoi = HoaDonOnline_DTO.TT_DA_DUYET;

                case HoaDonOnline_DTO.TT_DA_DUYET ->
                        trangThaiMoi = HoaDonOnline_DTO.TT_DANG_GIAO;

                case HoaDonOnline_DTO.TT_DANG_GIAO ->
                        trangThaiMoi = HoaDonOnline_DTO.TT_HOAN_THANH;

                case HoaDonOnline_DTO.TT_YEU_CAU_HOAN ->
                        trangThaiMoi = HoaDonOnline_DTO.TT_DA_HUY;

                case HoaDonOnline_DTO.TT_HOAN_THANH,
                     HoaDonOnline_DTO.TT_DA_HUY -> {

                    JOptionPane.showMessageDialog(null,
                            "Không thể cập nhật trạng thái này");
                    return;
                }

                default -> {
                    JOptionPane.showMessageDialog(null,
                            "Trạng thái không hợp lệ");
                    return;
                }
            }

            // ===== GỌI BUS =====
            bus.capNhatTrangThai(maHD, trangThaiMoi);

            JOptionPane.showMessageDialog(null,
                    "Cập nhật thành công!");

            loadDanhSachHoaDonOnline();
        });
    }



    private void createUIComponents() {
        JDateChooser1 = new com.toedter.calendar.JDateChooser();
        JDateChooser1.setDateFormatString("dd/MM/yyyy");

        JDateChooser2 = new com.toedter.calendar.JDateChooser();
        JDateChooser2.setDateFormatString("dd/MM/yyyy");
    }
}
