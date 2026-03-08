package gui.HOADON_GUI;

import bus.HoaDonOnline_BUS;
import bus.KhachHang_BUS;
import bus.SanPham_BUS;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;import bus.HoaDonBan_BUS;
import dto.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class QuanLyHoaDonBan_GUI extends JPanel {
    private JPanel panel_QuanLyHDB;
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
    private JPopupMenu popupGoiY = new JPopupMenu();

    private DefaultTableModel modelHoaDon;
    private DefaultTableModel modelChiTiet;
    private HoaDonBan_BUS hoaDonBUS = HoaDonBan_BUS.getInstance();

    public QuanLyHoaDonBan_GUI() {
        setLayout(new BorderLayout());
        add(panel_QuanLyHDB, BorderLayout.CENTER);

        khoiTaoBangHoaDon();
        khoiTaoBangChiTiet();
        khoaThongTin();
        formEdit();
        suKienChonHoaDon();
        suKienTimKiem();
        suKienReset();
        suKienGoiY();
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
        tableDSHD.getColumnModel().getColumn(9).setPreferredWidth(120);
        tableDSHD.getColumnModel().getColumn(10).setPreferredWidth(130);

        tableDSHD.getTableHeader().setResizingAllowed(false);
        tableDSHD.getTableHeader().setReorderingAllowed(false);
        tableDSHD.setRowHeight(25);
        tableDSHD.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        srcDSHD.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        srcDSHD.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));

        loadTableFromList(hoaDonBUS.getAllHoaDon());
    }
    private void loadTableFromList(java.util.List<HoaDonBan_DTO> list){

        modelHoaDon.setRowCount(0);

        int stt = 1;

        DateTimeFormatter fmt =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for(HoaDonBan_DTO hd : list){

            modelHoaDon.addRow(new Object[]{
                    stt++,
                    hd.getMa(),
                    hoaDonBUS.getTenKH(hd.getMaKhachHang()),
                    hd.getNgayLap()==null ? "" :
                            hd.getNgayLap().format(fmt),
                    hoaDonBUS.getSDT(hd.getMaKhachHang()),
                    hd.getMaNhanVien(),
                    hd.getLoaiHDBText(),
                    hd.getKeToaText(),
                    hd.getTinhTrangThanhToanText(),
                    hd.getTrangThaiText(),
                    formatTien(hd.getThanhTien())
            });
        }


        txtHienCo.setText(String.valueOf(modelHoaDon.getRowCount()));
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

                hienThiNeuCo(
                        labelPhiVC,
                        txtPhiVC,
                        formatTien(online.getPhiVanChuyen())
                );

                hienThiNeuCo(
                        labelDCGiaoHang,
                        txtDCGiaoHang,
                        HoaDonOnline_BUS.getInstance()
                                .getDiaChiDayDu(online.getMaDiaChiGiaoHang())
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
                    formatTien(ct.getGiaBan()),
                    hoaDonBUS.getTenKhuyenMai(ct.getMaKhuyenMai()),
                    formatTien(ct.getGiaBanSauApKM()),
                    formatTien(ct.getThanhTien())
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

        txtTongTien.setText(formatTien(hd.getTongTienGoc()));
        txtTongGTKM.setText(formatTien(hd.getTongGiaTriKhuyenMai()));
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMaximumFractionDigits(0);
        txtThueVAT.setText(percent.format(hd.getThueVAT()));
        txtThanhTien.setText(formatTien(hd.getThanhTien()));
        txtThanhTienCT.setText(formatTien(hd.getThanhTien()));
        hienThiNeuCo(
                labelVoucher,
                txtVoucher,
                hoaDonBUS.getTenVoucher(hd.getMaVoucher())
        );

        hienThiNeuCo(
                labelDiemThuong,
                txtDiemThuong,
                String.valueOf(hd.getDiemThuongQuyDoi())
        );

        hienThiNeuCo(
                labelGhiChu,
                txtGhiChu,
                hd.getGhiChu()
        );
    }
    private void hienPhiVanChuyen(boolean hien){

        labelPhiVC.setVisible(hien);
        txtPhiVC.setVisible(hien);
    }
    private void hienDiaChi(boolean hien){

        labelDCGiaoHang.setVisible(hien);
        txtDCGiaoHang.setVisible(hien);
    }
    private void khoaThongTin(){

        txtHienCo.setEditable(false);
        txtNgayLap.setEditable(false);
        txtTenKH.setEditable(false);
        txtSDT.setEditable(false);
        txtDCGiaoHang.setEditable(false);
        txtGhiChu.setEditable(false);

        txtTongTien.setEditable(false);
        txtVoucher.setEditable(false);
        txtDiemThuong.setEditable(false);
        txtTongGTKM.setEditable(false);
        txtThueVAT.setEditable(false);
        txtPhiVC.setEditable(false);
        txtThanhTien.setEditable(false);
        txtThanhTienCT.setEditable(false);
    }
    private void hienThiNeuCo(JLabel label, JTextField txt, String value){

        if(value == null || value.trim().isEmpty()){
            label.setVisible(false);
            txt.setVisible(false);
        }else{
            label.setVisible(true);
            txt.setVisible(true);
            txt.setText(value);
        }
    }
    private void formEdit(){

        cbTrangThai.setModel(new DefaultComboBoxModel<>(new String[]{
                "Tất cả",
                HoaDonBan_DTO.CHO_DUYET,
                HoaDonBan_DTO.DA_DUYET,
                HoaDonBan_DTO.DANG_GIAO,
                HoaDonBan_DTO.HOAN_THANH,
                HoaDonBan_DTO.DA_HUY,
                HoaDonBan_DTO.YEU_CAU_HOAN
        }));

        cbTTTT.setModel(new DefaultComboBoxModel<>(new String[]{
                "Tất cả",
                HoaDonBan_DTO.CHUA_THANH_TOAN,
                HoaDonBan_DTO.DA_THANH_TOAN,
                HoaDonBan_DTO.DA_HOAN_TIEN
        }));

        cbLoaiHD.setModel(new DefaultComboBoxModel<>(new String[]{
                "Tất cả",
                HoaDonBan_DTO.TAI_QUAY,
                HoaDonBan_DTO.TRUC_TUYEN
        }));
        loadCBTimTheo();
        loadCBGia();


    }
    private void loadCBTimTheo(){

        cbTimTheo.setModel(new DefaultComboBoxModel<>(new String[]{
                "Mã hóa đơn",
                "SĐT",
                "Tên khách hàng"
        }));

    }
    private void loadCBGia(){

        cbGia.setModel(new DefaultComboBoxModel<>(new String[]{
                "Tất cả",
                "Dưới 500.000",
                "500.000 - 1.000.000",
                "1.000.000 - 3.000.000",
                "Trên 3.000.000"
        }));

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



    private void createUIComponents() {
        JDateChooser1 = new com.toedter.calendar.JDateChooser();
        JDateChooser1.setDateFormatString("dd/MM/yyyy");

        JDateChooser2 = new com.toedter.calendar.JDateChooser();
        JDateChooser2.setDateFormatString("dd/MM/yyyy");
    }
    private void timKiemHoaDon(){

        String kieuTim = cbTimTheo.getSelectedItem().toString();
        String keyword = txtNhapTT.getText().trim();

        Integer trangThai = cbTrangThai.getSelectedIndex() == 0 ?
                null : cbTrangThai.getSelectedIndex()-1;

        Integer thanhToan = cbTTTT.getSelectedIndex() == 0 ?
                null : cbTTTT.getSelectedIndex()-1;

        Integer loaiHD = cbLoaiHD.getSelectedIndex() == 0 ?
                null : cbLoaiHD.getSelectedIndex()-1;

        Integer mucGia = cbGia.getSelectedIndex() == 0 ?
                null : cbGia.getSelectedIndex()-1;

        java.time.LocalDateTime tuNgay = null;
        java.time.LocalDateTime denNgay = null;

        if(JDateChooser1.getDate()!=null && JDateChooser2.getDate()==null){
            JOptionPane.showMessageDialog(this,"Phải chọn đến ngày");
            return;
        }

        if(JDateChooser2.getDate()!=null && JDateChooser1.getDate()==null){
            JOptionPane.showMessageDialog(this,"Phải chọn từ ngày");
            return;
        }

        if(JDateChooser1.getDate()!=null){

            tuNgay = JDateChooser1.getDate()
                    .toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate()
                    .atStartOfDay();

            denNgay = JDateChooser2.getDate()
                    .toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate()
                    .atTime(23,59,59);
        }

        var list = hoaDonBUS.timKiem(
                kieuTim,
                keyword,
                trangThai,
                thanhToan,
                loaiHD,
                mucGia,
                tuNgay,
                denNgay
        );

        loadTableFromList(list);
    }

    private void suKienTimKiem(){

        btnTimKiem.addActionListener(e -> timKiemHoaDon());

    }
    private void suKienReset(){

        btnReset.addActionListener(e -> {

            txtNhapTT.setText("");

            cbTimTheo.setSelectedIndex(0);
            cbTrangThai.setSelectedIndex(0);
            cbTTTT.setSelectedIndex(0);
            cbLoaiHD.setSelectedIndex(0);
            cbGia.setSelectedIndex(0);

            JDateChooser1.setDate(null);
            JDateChooser2.setDate(null);

            loadTableFromList(hoaDonBUS.getAllHoaDon());

            txtHienCo.setText(
                    String.valueOf(modelHoaDon.getRowCount())
            );

        });
    }
    private void hienThiGoiY(ArrayList<HoaDonBan_DTO> list){

        popupGoiY.removeAll();
        popupGoiY.setLayout(new GridLayout(0,1));

        String kieuTim = cbTimTheo.getSelectedItem().toString();

        for(HoaDonBan_DTO hd : list){

            String text = "";

            if(kieuTim.equals("Mã hóa đơn")){
                text = hd.getMa();
            }

            else if(kieuTim.equals("SĐT")){
                text = hoaDonBUS.getSDT(hd.getMaKhachHang());
            }

            else if(kieuTim.equals("Tên khách hàng")){
                text = hoaDonBUS.getTenKH(hd.getMaKhachHang());
            }

            final String value = text;

            JButton btn = new JButton(text);
            btn.setHorizontalAlignment(SwingConstants.LEFT);

            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            btn.addActionListener(e -> {
                txtNhapTT.setText(value);
                popupGoiY.setVisible(false);
            });

            popupGoiY.add(btn);
        }

        if(list.size() > 0)
            popupGoiY.show(txtNhapTT,0,txtNhapTT.getHeight());
        else
            popupGoiY.setVisible(false);
    }
    private ArrayList<HoaDonBan_DTO> timKiemGoiY(){

        String kieuTim = cbTimTheo.getSelectedItem().toString();
        String keyword = txtNhapTT.getText().trim().toLowerCase();

        ArrayList<HoaDonBan_DTO> ketQua = new ArrayList<>();

        if(keyword.isEmpty()) return ketQua;

        for(HoaDonBan_DTO hd : hoaDonBUS.getAllHoaDon()){

            if(kieuTim.equals("Mã hóa đơn")){

                if(hd.getMa().toLowerCase().contains(keyword))
                    ketQua.add(hd);
            }

            else if(kieuTim.equals("SĐT")){

                String sdt = hoaDonBUS.getSDT(hd.getMaKhachHang());

                if(sdt != null && sdt.toLowerCase().contains(keyword))
                    ketQua.add(hd);
            }

            else if(kieuTim.equals("Tên khách hàng")){

                String ten = hoaDonBUS.getTenKH(hd.getMaKhachHang());

                if(ten != null && ten.toLowerCase().contains(keyword))
                    ketQua.add(hd);
            }

            if(ketQua.size() == 8) break; // chỉ hiện tối đa 8 gợi ý
        }

        return ketQua;
    }
    private void suKienGoiY(){

        txtNhapTT.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {

                if(!txtNhapTT.getText().trim().isEmpty())
                    hienThiGoiY(timKiemGoiY());
                else
                    popupGoiY.setVisible(false);

            }
        });

    }
    private String formatTien(double tien){

        java.text.NumberFormat nf =
                java.text.NumberFormat.getInstance(
                        new java.util.Locale("vi","VN")
                );

        nf.setMaximumFractionDigits(0);
        nf.setMinimumFractionDigits(0);

        return nf.format(Math.round(tien)) + " đ";
    }
    public void reloadDanhSach(){
        loadTableFromList(hoaDonBUS.getAllHoaDon());
    }
}
