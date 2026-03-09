package gui.HOADON_GUI;

import bus.HoaDonOnline_BUS;
import bus.KhachHang_BUS;
import bus.SanPham_BUS;
import com.toedter.calendar.JDateChooser;
import dto.HoaDonBan_DTO;
import dto.HoaDonOnline_DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DuyetDonHang_GUI extends JPanel{
    private JPanel panel_DuyetDonHang;
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
    private JComboBox cbTrangThai;
    private JButton btnTiemKiem;
    private JButton btnReset;
    private JLabel labelTimTheo;
    private JLabel labelGia;
    private JLabel labelNhapTT;
    private JLabel labelTrangThai;
    private JLabel labelTuNgay;
    private JLabel labelDenNgay;
    private JLabel labelHienCo;
    private JLabel labelNgayLap;
    private JLabel labelTenKH;
    private JLabel labelSDT;
    private JLabel labelDCGIaoHang;
    private JLabel labelTongTien;
    private JLabel labelVoucher;
    private JLabel labelDiemThuong;
    private JLabel labelPhiVC;
    private JLabel labelThueVAT;
    private JLabel labelThanhTien;
    private JLabel labelTrangThaiHienTai;
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
    private JLabel labelGhiChu;
    private JTextField txtGhiChu;
    private JLabel labelTTTT;
    private JComboBox cbTTTT;
    private JPopupMenu popupGoiY = new JPopupMenu();

    private DefaultTableModel modelHoaDon;
    private DefaultTableModel modelChiTiet;
    private HoaDonOnline_BUS bus =
            HoaDonOnline_BUS.getInstance();
    private Runnable onTrangThaiChanged;

    public void setOnTrangThaiChanged(Runnable action){
        this.onTrangThaiChanged = action;
    }
    public DuyetDonHang_GUI() {
        setLayout(new BorderLayout());
        add(panel_DuyetDonHang, BorderLayout.CENTER);

        khoiTaoBangHoaDon();
        khoiTaoBangChiTiet();
        khoaThongTin();
        formEdit();

        loadTableFromList(bus.getDanhSachDuyetOnline());

        suKienBangHoaDon();
        suKienTimKiem();
        suKienReset();;
        suKienCapNhat();
        suKienGoiY();;

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
                return false; //
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
    private void loadTableFromList(java.util.List<HoaDonOnline_DTO> list){

        modelHoaDon.setRowCount(0);

        int stt = 1;

        for(HoaDonOnline_DTO hd : list){

            modelHoaDon.addRow(new Object[]{
                    stt++,
                    hd.getMa(),
                    bus.getTenKH(hd.getMaKhachHang()),
                    hd.getNgayLap(),
                    bus.getSDT(hd.getMaKhachHang()),
                    hd.getMaNhanVien(),
                    hd.getTinhTrangThanhToanText(),
                    hd.getTrangThaiText(),
                    formatTien(hd.getThanhTien())
            });
        }

        txtHienCo.setText(String.valueOf(list.size()));
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
                    formatTien(ct.getGiaBan()),
                    ct.getMaKhuyenMai(),
                    formatTien(ct.getGiaBanSauApKM()),
                    formatTien(ct.getThanhTien())
            });
        }

        HoaDonOnline_DTO hd = bus.getHoaDonOnline(maHD);

        txtTenKH.setText(bus.getTenKH(hd.getMaKhachHang()));
        txtSDT.setText(bus.getSDT(hd.getMaKhachHang()));
        txtNgayLap.setText(hd.getNgayLap().toString());
        txtTongTien.setText(formatTien(hd.getTongTienGoc()));
        txtPhiVC.setText(formatTien(hd.getPhiVanChuyen()));
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMaximumFractionDigits(0);
        txtThueVAT.setText(percent.format(hd.getThueVAT()));
        txtThanhTien.setText(formatTien(hd.getThanhTien()));
        txtTTHienTai.setText(hd.getTrangThaiText());
        txtDCGiaoHang.setText(
                bus.getDiaChiDayDu(hd.getMaDiaChiGiaoHang())
        );
        txtThanhTienCT.setText(formatTien(hd.getThanhTien()));
        txtTTTT.setText(hd.getTinhTrangThanhToanText());


        hienThiNeuCo(
                labelVoucher,
                txtVoucher,
                bus.getTenVoucher(hd.getMaVoucher())
        );

        hienThiNeuCo(
                labelDiemThuong,
                txtDiemThuong,
                formatTien(hd.getDiemThuongQuyDoi())
        );

        hienThiNeuCo(
                labelTongGTKM,
                txtTongGTKM,
                formatTien(hd.getTongGiaTriKhuyenMai())
        );

        hienThiNeuCo(
                labelGhiChu,
                txtGhiChu,
                hd.getGhiChu()
        );

        loadTrangThaiCapNhat(hd);
    }
    private void khoaThongTin(){

        txtNgayLap.setEditable(false);
        txtTenKH.setEditable(false);
        txtSDT.setEditable(false);
        txtDCGiaoHang.setEditable(false);

        txtHienCo.setEditable(false);
        txtTongTien.setEditable(false);
        txtVoucher.setEditable(false);
        txtDiemThuong.setEditable(false);
        txtTongGTKM.setEditable(false);

        txtThueVAT.setEditable(false);
        txtPhiVC.setEditable(false);
        txtThanhTien.setEditable(false);

        txtTTTT.setEditable(false);
        txtTTHienTai.setEditable(false);

        txtThanhTienCT.setEditable(false);
        txtGhiChu.setEditable(false);
    }
    private void hienThiNeuCo(JLabel label, JTextField txt, String value){

        if(value == null || value.trim().isEmpty() || value.equals("0") || value.equals("0.0")){
            label.setVisible(false);
            txt.setVisible(false);
        }else{
            label.setVisible(true);
            txt.setVisible(true);
            txt.setText(value);
        }
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
    private void formEdit(){

        cbTrangThai.setModel(new DefaultComboBoxModel<>(new String[]{
                HoaDonOnline_DTO.CHO_DUYET,
                HoaDonOnline_DTO.DA_DUYET,
                HoaDonOnline_DTO.DANG_GIAO,
                HoaDonOnline_DTO.HOAN_THANH,
                HoaDonOnline_DTO.DA_HUY,
                HoaDonOnline_DTO.YEU_CAU_HOAN
        }));

        cbTrangThai.setSelectedIndex(-1); // để trống ban đầu

        cbTTTT.setModel(new DefaultComboBoxModel<>(new String[]{
                HoaDonOnline_DTO.CHUA_THANH_TOAN,
                HoaDonOnline_DTO.DA_THANH_TOAN,
                HoaDonOnline_DTO.DA_HOAN_TIEN
        }));

        cbTTTT.setSelectedIndex(-1);

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


    private void suKienCapNhat() {

        btnCapNhat.addActionListener(e -> {

            int row = tableDSHDChoCN.getSelectedRow();

            if (row < 0) {
                JOptionPane.showMessageDialog(null,"Chọn hóa đơn trước!");
                return;
            }

            String maHD = modelHoaDon.getValueAt(row,1).toString();

            HoaDonOnline_DTO hd = bus.getHoaDonOnline(maHD);

            if (hd == null) {
                JOptionPane.showMessageDialog(null,"Không tìm thấy hóa đơn!");
                return;
            }

            Object selected = cbTrangThaiCN.getSelectedItem();

            if(selected == null){
                JOptionPane.showMessageDialog(null,"Chọn trạng thái!");
                return;
            }

            String chon = selected.toString();

            try {

                switch (chon){

                    case "Duyệt" ->
                            bus.duyetDon(maHD);

                    case "Không duyệt" ->
                            bus.khongDuyetDon(maHD);

                    case "Giao hàng" ->
                            bus.giaoHang(maHD);

                    case "Hoàn thành" ->
                            bus.hoanThanh(maHD);

                    case "Ngưng giao" ->
                            bus.huyDonHang(maHD);

                    case "Duyệt hoàn" ->
                            bus.duyetHoanHang(maHD);

                    case "Không duyệt hoàn" ->
                            bus.khongDuyetHoanHang(maHD);

                    default -> {
                        JOptionPane.showMessageDialog(null,"Trạng thái không hợp lệ");
                        return;
                    }
                }

                JOptionPane.showMessageDialog(null,"Cập nhật thành công!");


                if(onTrangThaiChanged != null){
                    onTrangThaiChanged.run();
                }

                loadTableFromList(bus.getDanhSachDuyetOnline());
                for(int i=0;i<tableDSHDChoCN.getRowCount();i++){
                    if(tableDSHDChoCN.getValueAt(i,1).equals(maHD)){
                        tableDSHDChoCN.setRowSelectionInterval(i,i);
                        break;
                    }
                }
                loadChiTietHoaDon(maHD);


            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });
    }

    private void loadTrangThaiCapNhat(HoaDonOnline_DTO hd){

        cbTrangThaiCN.removeAllItems();

        int tt = hd.getTrangThai();

        switch (tt){

            // CHỜ DUYỆT
            case HoaDonOnline_DTO.TT_CHO_DUYET -> {
                cbTrangThaiCN.addItem("Duyệt");
                cbTrangThaiCN.addItem("Không duyệt");
            }

            // ĐÃ DUYỆT
            case HoaDonOnline_DTO.TT_DA_DUYET -> {
                cbTrangThaiCN.addItem("Giao hàng");
            }

            // ĐANG GIAO
            case HoaDonOnline_DTO.TT_DANG_GIAO -> {
                cbTrangThaiCN.addItem("Hoàn thành");
                cbTrangThaiCN.addItem("Ngưng giao");
            }

            // YÊU CẦU HOÀN HÀNG
            case HoaDonOnline_DTO.TT_YEU_CAU_HOAN -> {
                cbTrangThaiCN.addItem("Duyệt hoàn");
                cbTrangThaiCN.addItem("Không duyệt hoàn");
            }
            default -> cbTrangThaiCN.addItem("Không có thao tác");
        }
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

        Integer trangThai = null;

        if(cbTrangThai.getSelectedIndex() >= 0){

            switch (cbTrangThai.getSelectedIndex()) {
                case 0 -> trangThai = HoaDonOnline_DTO.TT_CHO_DUYET;
                case 1 -> trangThai = HoaDonOnline_DTO.TT_DA_DUYET;
                case 2 -> trangThai = HoaDonOnline_DTO.TT_DANG_GIAO;
                case 3 -> trangThai = HoaDonOnline_DTO.TT_HOAN_THANH;
                case 4 -> trangThai = HoaDonOnline_DTO.TT_DA_HUY;
                case 5 -> trangThai = HoaDonOnline_DTO.TT_YEU_CAU_HOAN;
            }

        }

        Integer thanhToan = null;

        if(cbTTTT.getSelectedIndex() >= 0){

            switch (cbTTTT.getSelectedIndex()) {
                case 0 -> thanhToan = HoaDonOnline_DTO.TT_CHUA_THANH_TOAN;
                case 1 -> thanhToan = HoaDonOnline_DTO.TT_DA_THANH_TOAN;
                case 2 -> thanhToan = HoaDonOnline_DTO.TT_DA_HOAN_TIEN;
            }

        }

        Integer mucGia =
                cbGia.getSelectedIndex()==0 ? null :
                        cbGia.getSelectedIndex()-1;

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

        var list = bus.timKiemHoaDonOnline(
                kieuTim,
                keyword,
                trangThai,
                thanhToan,
                mucGia,
                tuNgay,
                denNgay
        );

        loadTableFromList(list);
    }
    private void suKienTimKiem(){

        btnTiemKiem.addActionListener(e -> timKiemHoaDon());

    }
    private void suKienReset(){

        btnReset.addActionListener(e -> {

            txtNhapTT.setText("");

            cbTimTheo.setSelectedIndex(0);
            cbTrangThai.setSelectedIndex(-1);
            cbTTTT.setSelectedIndex(-1);
            cbGia.setSelectedIndex(0);

            JDateChooser1.setDate(null);
            JDateChooser2.setDate(null);
            popupGoiY.setVisible(false);
            modelChiTiet.setRowCount(0);

            loadTableFromList(bus.getDanhSachDuyetOnline());

            txtHienCo.setText(
                    String.valueOf(modelHoaDon.getRowCount())
            );
        });
    }
    private void hienThiGoiY(ArrayList<HoaDonOnline_DTO> list){

        popupGoiY.removeAll();
        popupGoiY.setLayout(new GridLayout(0,1));

        String kieuTim = cbTimTheo.getSelectedItem().toString();

        for(HoaDonOnline_DTO hd : list){

            String text = "";

            if(kieuTim.equals("Mã hóa đơn")){
                text = hd.getMa();
            }

            else if(kieuTim.equals("SĐT")){
                text = bus.getSDT(hd.getMaKhachHang());
            }

            else if(kieuTim.equals("Tên khách hàng")){
                text = bus.getTenKH(hd.getMaKhachHang());
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
    private ArrayList<HoaDonOnline_DTO> timKiemGoiY(){

        String kieuTim = cbTimTheo.getSelectedItem().toString();
        String keyword = txtNhapTT.getText().trim().toLowerCase();

        ArrayList<HoaDonOnline_DTO> ketQua = new ArrayList<>();

        if(keyword.isEmpty()) return ketQua;

        for(HoaDonOnline_DTO hd : bus.getDanhSachDuyetOnline()){

            if(kieuTim.equals("Mã hóa đơn")){

                if(hd.getMa().toLowerCase().contains(keyword))
                    ketQua.add(hd);
            }

            else if(kieuTim.equals("SĐT")){

                String sdt = bus.getSDT(hd.getMaKhachHang());

                if(sdt != null && sdt.toLowerCase().contains(keyword))
                    ketQua.add(hd);
            }

            else if(kieuTim.equals("Tên khách hàng")){

                String ten = bus.getTenKH(hd.getMaKhachHang());

                if(ten != null && ten.toLowerCase().contains(keyword))
                    ketQua.add(hd);
            }

            if(ketQua.size() == 8) break;
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
}
