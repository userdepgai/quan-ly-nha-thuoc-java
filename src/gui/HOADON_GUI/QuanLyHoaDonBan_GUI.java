package gui.HOADON_GUI;

import bus.HoaDonOnline_BUS;
import bus.KhachHang_BUS;
import bus.SanPham_BUS;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;import bus.HoaDonBan_BUS;
import dto.*;
import java.time.format.DateTimeFormatter;

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
    private JScrollPane srcTTCTHD;
    private JScrollPane srcDSHD;

    private DefaultTableModel modelHoaDon;
    private DefaultTableModel modelChiTiet;
    private HoaDonBan_BUS hoaDonBUS = HoaDonBan_BUS.getInstance();
    private KhachHang_BUS khBUS = KhachHang_BUS.getInstance();
    private SanPham_BUS spBUS = SanPham_BUS.getInstance();

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
                        "STT", "Mã Hóa đơn ", "Tên Khách hàng",
                        "Ngày lập", "SDT","Mã nhân viên lập",
                        "Loại hóa đơn ","Kê toa",
                        "Thanh toán", "Trạng thái",
                        "Thành tiền"
                }, 0
        ){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableDSHD.setModel(modelHoaDon);

        tableDSHD.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tableDSHD.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableDSHD.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableDSHD.getColumnModel().getColumn(2).setPreferredWidth(150);
        tableDSHD.getColumnModel().getColumn(3).setPreferredWidth(120);
        tableDSHD.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableDSHD.getColumnModel().getColumn(5).setPreferredWidth(100);
        tableDSHD.getColumnModel().getColumn(6).setPreferredWidth(100);
        tableDSHD.getColumnModel().getColumn(7).setPreferredWidth(70);
        tableDSHD.getColumnModel().getColumn(8).setPreferredWidth(100);
        tableDSHD.getColumnModel().getColumn(9).setPreferredWidth(100);
        tableDSHD.getColumnModel().getColumn(10).setPreferredWidth(150);

        tableDSHD.getTableHeader().setResizingAllowed(false);
        tableDSHD.getTableHeader().setReorderingAllowed(false);
        tableDSHD.setRowHeight(25);
        tableDSHD.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        srcDSHD.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        srcDSHD.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));

        loadDanhSachHoaDon();
    }
    public void loadDanhSachHoaDon(){

        modelHoaDon.setRowCount(0);

        int stt = 1;

        DateTimeFormatter fmt =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for(HoaDonBan_DTO hd : hoaDonBUS.getAllHoaDon()){

            modelHoaDon.addRow(new Object[]{
                    stt++,
                    hd.getMa(),

                    // TÊN KH
                    hoaDonBUS.getTenKH(hd.getMaKhachHang()),

                    // NGÀY LẬP
                    hd.getNgayLap()==null ? "" :
                            hd.getNgayLap().format(fmt),

                    // SDT
                    hoaDonBUS.getSDT(hd.getMaKhachHang()),

                    // NV
                    hd.getMaNhanVien(),

                    // LOẠI HD
                    hd.getLoaiHDB()==1 ? "Online" : "Tại quầy",

                    // KÊ TOA
                    hd.isKeToa() ? "Có" : "Không",

                    // THANH TOÁN
                    hd.getTinhTrangThanhToanText(),

                    // TRẠNG THÁI
                    hd.getTrangThaiText(),

                    // THÀNH TIỀN
                    hd.getThanhTien()
            });
        }
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

        tableTTCTHD.setModel(modelChiTiet);

        tableTTCTHD.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tableTTCTHD.getColumnModel().getColumn(0).setPreferredWidth(30);
        tableTTCTHD.getColumnModel().getColumn(1).setPreferredWidth(70);
        tableTTCTHD.getColumnModel().getColumn(2).setPreferredWidth(139);
        tableTTCTHD.getColumnModel().getColumn(3).setPreferredWidth(30);
        tableTTCTHD.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableTTCTHD.getColumnModel().getColumn(5).setPreferredWidth(100);
        tableTTCTHD.getColumnModel().getColumn(6).setPreferredWidth(90);
        tableTTCTHD.getColumnModel().getColumn(7).setPreferredWidth(90);

        tableTTCTHD.getTableHeader().setResizingAllowed(false);
        tableTTCTHD.getTableHeader().setReorderingAllowed(false);
        tableTTCTHD.setRowHeight(25);
        tableTTCTHD.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        srcTTCTHD.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        srcTTCTHD.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
    }

    private void suKienChonHoaDon() {

        tableDSHD.getSelectionModel()
                .addListSelectionListener(e -> {

                    if(e.getValueIsAdjusting()) return;

                    int row = tableDSHD.getSelectedRow();
                    if (row < 0) return;

                    String maHD =
                            modelHoaDon.getValueAt(row, 1).toString();

                    hienChiTiet(maHD);
                });
    }

    private void hienChiTiet(String maHD) {

        modelChiTiet.setRowCount(0);

        HoaDonBan_DTO hd = hoaDonBUS
                .getAllHoaDon()
                .stream()
                .filter(x -> x.getMa().equals(maHD))
                .findFirst()
                .orElse(null);

        if(hd == null) return;

        // ===== HIỆN / ẨN PHÍ VẬN CHUYỂN + ĐỊA CHỈ =====
        if(hd.getLoaiHDB() == 1){ // ONLINE

            hienPhiVanChuyen(true);
            hienDiaChi(true);

            HoaDonOnline_DTO online =
                    HoaDonOnline_BUS.getInstance()
                            .getHoaDonOnline(maHD);

            if(online != null){

                txtPhiVC.setText(
                        String.valueOf(online.getPhiVanChuyen())
                );

                txtDCGiaoHang.setText(
                        online.getMaDiaChiGiaoHang()
                );
            }

        }else{ // OFFLINE

            hienPhiVanChuyen(false);
            hienDiaChi(false);

            txtPhiVC.setText("");
            txtDCGiaoHang.setText("");
        }

        var dsCT = hoaDonBUS.getChiTietHoaDon(maHD);

        int stt = 1;

        for(ChiTietHoaDonBan_DTO ct : dsCT){

            modelChiTiet.addRow(new Object[]{
                    stt++,
                    ct.getMaSP(),
                    hoaDonBUS.getTenSP(ct.getMaSP()),
                    ct.getSoLuong(),
                    ct.getGiaBan(),
                    hoaDonBUS.getTenKhuyenMai(ct.getMaKhuyenMai()),
                    ct.getGiaBanSauApKM(),
                    ct.getThanhTien()
            });
        }

        DateTimeFormatter fmt =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // ===== THÔNG TIN HÓA ĐƠN =====
        txtTenKH.setText(
                hoaDonBUS.getTenKH(hd.getMaKhachHang())
        );

        txtSDT.setText(
                hoaDonBUS.getSDT(hd.getMaKhachHang())
        );

        txtNgayLap.setText(
                hd.getNgayLap()==null ? "" :
                        hd.getNgayLap().format(fmt)
        );

        txtTongTien.setText(String.valueOf(hd.getTongTienGoc()));
        txtTongGTKM.setText(String.valueOf(hd.getTongGiaTriKhuyenMai()));
        txtThueVAT.setText(String.valueOf(hd.getThueVAT()));
        txtThanhTien.setText(String.valueOf(hd.getThanhTien()));
        txtThanhTienCT.setText(String.valueOf(hd.getThanhTien()));
        txtVoucher.setText(hoaDonBUS.getTenVoucher(hd.getMaVoucher()));
        txtDiemThuong.setText(String.valueOf(hd.getDiemThuongQuyDoi()));
    }

    private void hienPhiVanChuyen(boolean hien){

        labelPhiVC.setVisible(hien);
        txtPhiVC.setVisible(hien);
    }
    private void hienDiaChi(boolean hien){

        labelDCGiaoHang.setVisible(hien);
        txtDCGiaoHang.setVisible(hien);
    }
    private void createUIComponents() {
        JDateChooser1 = new com.toedter.calendar.JDateChooser();
        JDateChooser1.setDateFormatString("dd/MM/yyyy");

        JDateChooser2 = new com.toedter.calendar.JDateChooser();
        JDateChooser2.setDateFormatString("dd/MM/yyyy");
    }
    public void reloadDanhSach(){
        loadDanhSachHoaDon();
    }
}
