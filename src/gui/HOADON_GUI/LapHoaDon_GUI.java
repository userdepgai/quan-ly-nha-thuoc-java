package gui.HOADON_GUI;

import bus.KhachHang_BUS;
import bus.SanPham_BUS;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

import bus.HoaDonBan_BUS;
import dto.*;
import utils.PDFExporter;
import utils.Session;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LapHoaDon_GUI extends JPanel{
    private JPanel panel_LapHoaDon;
    private JCheckBox chbToaBacSi;
    private JCheckBox chbDiemThuong;
    private JTextField txtTenKhachHang;
    private JTextField txtSoDienThoai;
    private JTextField txtNhanVienLap;
    private JComboBox cbMaSP;
    private JComboBox cbTenSP;
    private JComboBox cbKhuyenMai;
    private JButton btnThem;
    private JButton btnXoa;
    private JButton btnSua;
    private JTextField txtTongTien;
    private JTextField txtGTDT;
    private JTextField txtTongGTKM;
    private JTextField txtThanhTien;
    private JComboBox cbVoucher;
    private JTextField txtTienNhan;
    private JTextField txtTienThoi;
    private JButton btnChuyenKhoan;
    private JCheckBox cbDaChyenKhoan;
    private JTable tableTTSP;
    private JButton btnLuu;
    private JLabel labelTenKH;
    private JLabel labelSoDienThoai;
    private JLabel labelNhanVienLap;
    private JLabel labelNgayLap;
    private JLabel labelMaSP;
    private JLabel labelSoLuong;
    private JLabel labelTenSP;
    private JLabel labelKM;
    private JLabel labelVoucher;
    private JLabel labelTongTien;
    private JLabel labelGTDT;
    private JLabel labelTongGTKM;
    private JLabel labelTienNhan;
    private JLabel labelTienThoi;
    private JSpinner snSoLuong;
    private JButton btnHuy;
    private JLabel labelThanhTien;
    private JLabel labelThueVat;
    private JTextField txtThueVat;
    private JTextField txtNgayLap;
    private JLabel labelGhiChu;
    private JTextField txtGhiChu;
    private JScrollPane srcTTSP;
    private JButton btnXuatPDF;
    private JButton btnTaoHoaDon;

    private boolean dangDongBo = false;
    private boolean daLuuHoaDon = false;
    private Runnable onHoaDonSaved;
    private HoaDonBan_BUS bus = HoaDonBan_BUS.getInstance();
    // ===== BUS =====
    private SanPham_BUS spBus = SanPham_BUS.getInstance();

    private DefaultTableModel modelBang;

    public void setOnHoaDonSaved(Runnable action){
        this.onHoaDonSaved = action;
    }
    public LapHoaDon_GUI() {
        this.setLayout(new BorderLayout());
        this.add(panel_LapHoaDon, BorderLayout.CENTER);


        khoiTaoHoaDon();
        khoiTaoThanhPhan();
        khoiTaoBang();
        khoiTaoDuLieu();
        suKienNut();
        capNhatTrangThaiNut();
        themSuKienNhapCombo();


    }
    private void khoiTaoHoaDon(){

        if(bus.getHoaDon() == null && Session.getCurrentUser() != null){
            bus.taoHoaDonMoi();
        }

        HoaDonBan_DTO hd = bus.getHoaDon();

        if(hd != null){
            txtNhanVienLap.setText(hd.getMaNhanVien());
        }
        txtNgayLap.setText(
                new java.text.SimpleDateFormat("dd/MM/yyyy")
                        .format(new java.util.Date())
        );

    }
    private void khoiTaoThanhPhan(){

        btnThem.setEnabled(false);

        cbMaSP.setEditable(true);
        cbTenSP.setEditable(true);

        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);

        snSoLuong.setModel(new SpinnerNumberModel(0,0,999,1));
        snSoLuong.setEnabled(false);

        khoaTextField();
    }
    private void capNhatTrangThaiNut(){

        if(daLuuHoaDon){

            btnLuu.setEnabled(false);
            btnHuy.setEnabled(false);

            btnXuatPDF.setVisible(true);
            btnTaoHoaDon.setVisible(true);

            btnXuatPDF.setEnabled(true);
            btnTaoHoaDon.setEnabled(true);

        }else{

            btnLuu.setEnabled(true);
            btnHuy.setEnabled(true);

            btnXuatPDF.setVisible(false);
            btnTaoHoaDon.setVisible(false);
        }
    }

    private void khoiTaoDuLieu(){

        loadComboBox();

        loadCBVoucher();

        capNhatThongTinHoaDon();

    }
    private void khoaTextField(){

        txtTongTien.setEditable(false);
        txtGTDT.setEditable(false);
        txtTongGTKM.setEditable(false);
        txtThanhTien.setEditable(false);
        txtTienThoi.setEditable(false);
        txtThueVat.setEditable(false);

    }
    private void khoiTaoBang() {
        modelBang = new DefaultTableModel(
                new String[]{
                        "STT","Mã sản phẩm","Tên sản phẩm","Thuộc tính riêng",
                        "Giá bán","Khuyến mãi","Giá sau khuyến mãi","Số lượng","Thành tiền"
                }, 0
        ){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableTTSP.setModel(modelBang);

        tableTTSP.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableTTSP.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableTTSP.getColumnModel().getColumn(1).setPreferredWidth(110);
        tableTTSP.getColumnModel().getColumn(2).setPreferredWidth(200);
        tableTTSP.getColumnModel().getColumn(3).setPreferredWidth(150);
        tableTTSP.getColumnModel().getColumn(4).setPreferredWidth(130);
        tableTTSP.getColumnModel().getColumn(5).setPreferredWidth(180);
        tableTTSP.getColumnModel().getColumn(6).setPreferredWidth(130);
        tableTTSP.getColumnModel().getColumn(7).setPreferredWidth(80);
        tableTTSP.getColumnModel().getColumn(8).setPreferredWidth(130);


        tableTTSP.getTableHeader().setResizingAllowed(false);
        tableTTSP.getTableHeader().setReorderingAllowed(false);
        tableTTSP.setRowHeight(25);
        tableTTSP.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        srcTTSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        srcTTSP.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
    }
    private void capNhatThongTinHoaDon(){

        HoaDonBan_DTO hd = bus.getHoaDon();
        if(hd == null || bus.getDsTam().isEmpty()){
            txtTongTien.setText("");
            txtTongGTKM.setText("");
            txtGTDT.setText("");
            txtThanhTien.setText("");
            txtThueVat.setText("");
            txtTienThoi.setText("");
            return;
        }

        txtTongTien.setText(formatTien(hd.getTongTienGoc()));
        txtTongGTKM.setText(formatTien(hd.getTongGiaTriKhuyenMai()));
        txtGTDT.setText(formatTien(hd.getDiemThuongQuyDoi()));
        txtThueVat.setText("5%");
        txtThanhTien.setText(formatTien(hd.getThanhTien()));

        tinhTienThoi();
    }
    // ===============================
// LOAD DATA COMBOBOX
// ===============================
    private void loadComboBox(){

        loadCBMaSP();
        loadCBTenSP();
        //loadCBKhuyenMai();
    }
    private void loadCBMaSP(){

        cbMaSP.removeAllItems();
        cbMaSP.addItem("-- Chọn --");

        for(SanPham_DTO sp : spBus.getAll()){
            cbMaSP.addItem(sp.getMaSP());
        }
    }
    private void loadCBTenSP(){

        cbTenSP.removeAllItems();
        cbTenSP.addItem("-- Chọn --");

        for(SanPham_DTO sp : spBus.getAll()){
            cbTenSP.addItem(sp.getTenSP());
        }
    }

    private void loadCBKhuyenMai(
            String maSP,
            String maDanhMuc,
            double giaBan)
    {

        cbKhuyenMai.removeAllItems();

        cbKhuyenMai.addItem("Chọn khuyến mãi");


        for(KhuyenMai_DTO km :
                bus.goiYKhuyenMai(maSP, maDanhMuc, giaBan))
        {
            cbKhuyenMai.addItem(km.getTenKM());
        }

        cbKhuyenMai.setSelectedIndex(0);
    }

    private void loadCBVoucher(){

        cbVoucher.removeAllItems();

        cbVoucher.addItem("Không voucher");

        if(bus.getHoaDon() == null) return;

        if(bus.getHoaDon().getMaKhachHang() == null){
            cbVoucher.setSelectedIndex(0);
            return;
        }

        for(Voucher_DTO v : bus.goiYVoucher()){
            cbVoucher.addItem(v.getTen());
        }

        cbVoucher.setSelectedIndex(0);
    }

    private void themSuKienNhapCombo(){

        JTextField txtMa =
                (JTextField) cbMaSP.getEditor().getEditorComponent();

        JTextField txtTen =
                (JTextField) cbTenSP.getEditor().getEditorComponent();

        txtMa.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){

                String text = txtMa.getText().trim();

                goiYMaSP(text);

                dongBoTheoMa();
            }
        });

        txtTen.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){

                String text = txtTen.getText().trim();

                goiYTenSP(text);

                dongBoTheoTen();
            }
        });
    }
    private void dongBoTheoMa(){

        if(dangDongBo){
            dangDongBo = false;
            return;
        }

        String ma = ((JTextField)
                cbMaSP.getEditor().getEditorComponent())
                .getText().trim();

        if(ma.isEmpty()) return;

        SanPham_DTO sp = spBus.getById(ma);

        if(sp == null){

            dangDongBo = true;
            cbTenSP.setSelectedIndex(0);
            dangDongBo = false;
            return;
        }

        dangDongBo = true;
        cbTenSP.setSelectedItem(sp.getTenSP());
        dangDongBo = false;

        // ===== LOAD KHUYẾN MÃI =====
        int sl = (int) snSoLuong.getValue();

        double giaBan = bus.getGiaBanSP(ma, sl);

        loadCBKhuyenMai(
                ma,
                sp.getMaDM(),
                giaBan
        );
    }
    private void dongBoTheoTen(){

        if(dangDongBo) return;

        String ten = ((JTextField)
                cbTenSP.getEditor().getEditorComponent())
                .getText().trim();

        if(ten.isEmpty()) return;

        SanPham_DTO sp = spBus.getByTenSP(ten);

        if(sp == null){

            dangDongBo = true;
            cbMaSP.setSelectedIndex(0);
            dangDongBo = false;

            return;
        }

        dangDongBo = true;
        cbMaSP.setSelectedItem(sp.getMaSP());
        dangDongBo = false;
        int sl = (int) snSoLuong.getValue();

        double giaBan = bus.getGiaBanSP(sp.getMaSP(), sl);

        loadCBKhuyenMai(
                sp.getMaSP(),
                sp.getMaDM(),
                giaBan
        );
    }
    private void loadTableFromBUS(){

        modelBang.setRowCount(0);

        int stt = 1;

        Map<String, Integer> tongSL = new LinkedHashMap<>();

        Map<String, ChiTietHoaDonBan_DTO> ctMau = new HashMap<>();

        for(ChiTietHoaDonBan_DTO ct : bus.getDsTam()){

            String maSP = ct.getMaSP();

            tongSL.merge(maSP, ct.getSoLuong(), Integer::sum);


            if(!ctMau.containsKey(maSP)){
                ctMau.put(maSP, ct);
            }
        }


        for(String maSP : tongSL.keySet()){

            ChiTietHoaDonBan_DTO ct = ctMau.get(maSP);

            int soLuong = tongSL.get(maSP);

            modelBang.addRow(new Object[]{
                    stt++,
                    maSP,
                    bus.getTenSP(maSP),
                    bus.getDonViTinh(maSP),
                    formatTien(ct.getGiaBan()),
                    bus.getTenKhuyenMai(ct.getMaKhuyenMai()),
                    formatTien(ct.getGiaBanSauApKM()),
                    soLuong,
                    formatTien(ct.getGiaBanSauApKM() * soLuong)
            });
        }

        capNhatThongTinHoaDon();
    }
    /* ================== KHỞI TẠO BẢNG ================== */


    private void tinhTienThoi() {

        try {

            String sTienNhan = txtTienNhan.getText()
                    .replace(".", "")
                    .replace("đ", "")
                    .trim();

            String sThanhTien = txtThanhTien.getText()
                    .replace(".", "")
                    .replace("đ", "")
                    .trim();

            if (sTienNhan.isEmpty() || sThanhTien.isEmpty()) {
                txtTienThoi.setText("");
                return;
            }

            double tienNhan = Double.parseDouble(sTienNhan);
            double thanhTien = Double.parseDouble(sThanhTien);

            double tienThoi = tienNhan - thanhTien;

            if (tienThoi < 0) {
                txtTienThoi.setText("Không đủ");
            } else {
                txtTienThoi.setText(formatTien(tienThoi));
            }

        } catch (Exception e) {
            txtTienThoi.setText("");
        }
    }
    /* ================== SỰ KIỆN NÚT ================== */

    private void suKienNut() {

        /* ===== THÊM ===== */
        btnThem.addActionListener(e -> themSanPham());

        /* ===== CLICK BẢNG ĐỔ DỮ LIỆU ===== */
        tableTTSP.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int dong = tableTTSP.getSelectedRow();
                if (dong >= 0) {
                    cbMaSP.setSelectedItem(modelBang.getValueAt(dong,1));
                    cbTenSP.setSelectedItem(modelBang.getValueAt(dong,2));
                    snSoLuong.setValue(
                            Integer.parseInt(
                                    modelBang.getValueAt(dong,7).toString()
                            )
                    );

                    btnSua.setEnabled(true);
                    btnXoa.setEnabled(true);
                    btnThem.setEnabled(false);
                    hienTongTien();
                    tinhTienThoi();
                }
            }
        });

        /* ===== XOÁ ===== */
        btnXoa.addActionListener(e -> {

            int row = tableTTSP.getSelectedRow();
            if(row < 0) return;

            String maSP =
                    modelBang.getValueAt(row,1).toString();

            bus.xoaSanPham(maSP);

            loadTableFromBUS();
            loadCBVoucher();
            txtTienNhan.setText("");
            txtTienThoi.setText("");
            cbDaChyenKhoan.setSelected(false);
            resetFormSanPham();
        });

        /* ===== SỬA ===== */
        btnSua.addActionListener(e -> suaSanPham());

        /* ===== CHECK CHUYỂN KHOẢN ===== */
        cbDaChyenKhoan.addActionListener(e -> {

            if(cbDaChyenKhoan.isSelected()){

                txtTienNhan.setText(txtThanhTien.getText());
                txtTienThoi.setText("0 đ");

            }else{

                txtTienNhan.setText("");
                txtTienThoi.setText("");

            }
        });

        /* ===== NHẬP TIỀN NHẬN ===== */
        txtTienNhan.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {

                try {

                    String input = txtTienNhan.getText()
                            .replace(".", "")
                            .replace("đ", "")
                            .trim();

                    if (input.isEmpty()) {
                        txtTienThoi.setText("");
                        return;
                    }

                    tinhTienThoi();

                } catch (Exception ex) {
                    txtTienThoi.setText("");
                }
            }
        });

        btnLuu.addActionListener(e -> {

            if(!validateThongTin()) return;

            try{

                double tienNhan = Double.parseDouble(
                        txtTienNhan.getText()
                                .replace(".", "")
                                .replace("đ","")
                                .trim()
                );

                double thanhTien = bus.getHoaDon().getThanhTien();

                bus.getHoaDon().setTienNhan(tienNhan);
                bus.getHoaDon().setTienThoi(tienNhan - thanhTien);
                bus.getHoaDon().setTinhTrangThanhToan(1);
                bus.getHoaDon().setKeToa(chbToaBacSi.isSelected());
                bus.getHoaDon().setGhiChu(txtGhiChu.getText().trim());

                boolean ok = bus.luuHoaDon();

                if(!ok){
                 JOptionPane.showMessageDialog(this,"Lưu hóa đơn thất bại!");
                return;
                }

                JOptionPane.showMessageDialog(this,"Lưu hóa đơn thành công!");

                if(onHoaDonSaved != null){
                    onHoaDonSaved.run();
                }

                daLuuHoaDon = true;
                capNhatTrangThaiNut();
                cbDaChyenKhoan.setSelected(false);

            }catch(Exception ex){
                JOptionPane.showMessageDialog(this,"Tiền nhận không hợp lệ");
            }

        });

        btnTaoHoaDon.addActionListener(e -> {

            bus.taoHoaDonMoi();

            resetFormHoaDon();

            daLuuHoaDon = false;
            capNhatTrangThaiNut();

            khoiTaoHoaDon();


        });

        cbMaSP.addActionListener(e -> {
            if(dangDongBo) return;

            Object obj = cbMaSP.getSelectedItem();
            if(obj == null) return;

            String maSP = obj.toString().trim();

            if(maSP.equals("-- Chọn --") || maSP.isEmpty())
                return;

            SanPham_DTO sp = spBus.getById(maSP);

            if(sp != null){
                dangDongBo = true;
                cbTenSP.setSelectedItem(sp.getTenSP());
                dangDongBo = false;
                int sl = (int) snSoLuong.getValue();

                double giaBan = bus.getGiaBanSP(maSP, sl);

                loadCBKhuyenMai(
                        maSP,
                        sp.getMaDM(),
                        giaBan
                );


            }
            snSoLuong.setEnabled(true);
            snSoLuong.setValue(1);
            btnThem.setEnabled(true);
        });
        cbTenSP.addActionListener(e -> {

            if(dangDongBo) return;

            Object obj = cbTenSP.getSelectedItem();
            if(obj == null) return;

            String tenSP = obj.toString().trim();

            SanPham_DTO sp = spBus.getByTenSP(tenSP);

            if(sp != null){

                dangDongBo = true;
                cbMaSP.setSelectedItem(sp.getMaSP());
                dangDongBo = false;
                int sl = (int) snSoLuong.getValue();

                double giaBan = bus.getGiaBanSP(sp.getMaSP(), sl);

                loadCBKhuyenMai(
                        sp.getMaSP(),
                        sp.getMaDM(),
                        giaBan
                );
            }

            snSoLuong.setEnabled(true);
            snSoLuong.setValue(1);
            btnThem.setEnabled(true);
        });

        cbVoucher.addActionListener(e -> {

            if(bus.getHoaDon() == null) return;

            String ten = (String) cbVoucher.getSelectedItem();

            if("Không voucher".equals(ten))
                ten = null;

            try{
                bus.apDungVoucher(ten);
                capNhatThongTinHoaDon();
            }catch(Exception ex){

                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Voucher không hợp lệ",
                        JOptionPane.WARNING_MESSAGE
                );
                cbVoucher.setSelectedIndex(0);

                bus.apDungVoucher(null);

                capNhatThongTinHoaDon();
            }
        });

        chbToaBacSi.addActionListener(e -> {

            if(!chbToaBacSi.isSelected()){

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Bỏ kê toa sẽ xóa các thuốc kê toa đã thêm. Tiếp tục?",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION
                );

                if(confirm == JOptionPane.YES_OPTION){
                    bus.xoaSanPhamKeToa();
                    loadTableFromBUS();
                    capNhatThongTinHoaDon();
                }else{
                    chbToaBacSi.setSelected(true);
                }
            }

        });
        txtSoDienThoai.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){

                String sdt = txtSoDienThoai.getText().trim();

                if(sdt.isEmpty()){
                    txtTenKhachHang.setText("");
                    bus.getHoaDon().setMaKhachHang(null);
                    loadCBVoucher();
                    return;
                }

                KhachHang_DTO kh =
                        KhachHang_BUS.getInstance().getBysdt(sdt);

                if(kh != null){

                    txtTenKhachHang.setText(kh.getTen());
                    bus.getHoaDon().setMaKhachHang(kh.getMa());
                    chbDiemThuong.setSelected(false);
                    bus.setDungDiemThuong(false);
                    loadCBVoucher();

                }else{

                    txtTenKhachHang.setText("");
                    bus.getHoaDon().setMaKhachHang(null);
                    loadCBVoucher();
                }
            }
        });
        chbDiemThuong.addActionListener(e -> {

            // nếu người dùng tick dùng điểm
            if(chbDiemThuong.isSelected()){

                // kiểm tra hóa đơn có khách hàng chưa
                if(bus.getHoaDon().getMaKhachHang() == null){

                    JOptionPane.showMessageDialog(
                            this,
                            "Không thể sử dụng điểm thưởng.\nVui lòng nhập số điện thoại khách hàng hợp lệ."
                    );

                    chbDiemThuong.setSelected(false);
                    return;
                }


                bus.setDungDiemThuong(true);

            }else{


                bus.setDungDiemThuong(false);

            }

            capNhatThongTinHoaDon();
        });
        btnHuy.addActionListener(e -> {
            if(daLuuHoaDon) return;
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Hủy hóa đơn đang lập?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
            );

            if(confirm != JOptionPane.YES_OPTION) return;

            bus.taoHoaDonMoi();

            resetFormHoaDon();

            khoiTaoHoaDon();
        });

        btnXuatPDF.addActionListener(e -> {

            try {

                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Chọn nơi lưu hóa đơn");

                int option = chooser.showSaveDialog(this);

                if (option == JFileChooser.APPROVE_OPTION) {

                    String path = chooser.getSelectedFile().getAbsolutePath();

                    if (!path.endsWith(".pdf")) {
                        path += ".pdf";
                    }

                    String voucher = "";
                    if (cbVoucher.getSelectedItem() != null) {
                        voucher = cbVoucher.getSelectedItem().toString();
                    }

                    LocalDateTime ngayLap = bus.getHoaDon().getNgayLap();

                    DateTimeFormatter f =
                            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                    String ngayLapStr = ngayLap.format(f);

                    PDFExporter.exportHoaDon(

                            tableTTSP,
                            bus.getHoaDon().getMa(),
                            bus.getTenNV(txtNhanVienLap.getText()),
                            ngayLapStr,
                            txtTenKhachHang.getText(),
                            txtSoDienThoai.getText(),
                            chbToaBacSi.isSelected(),
                            txtTongTien.getText(),
                            txtTongGTKM.getText(),
                            txtGTDT.getText(),
                            txtThanhTien.getText(),
                            voucher,
                            path
                    );

                    JOptionPane.showMessageDialog(this, "Xuất PDF thành công!");

                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Xuất PDF thất bại!");
            }

        });
    }



    /* ================== HÀM THÊM ================== */

    private void themSanPham() {
        String maSP =
                ((JTextField) cbMaSP.getEditor()
                        .getEditorComponent())
                        .getText().trim();

        SanPham_DTO sp = spBus.getById(maSP);

        if(sp == null){
            JOptionPane.showMessageDialog(null,
                    "Vui lòng chọn sản phẩm ");
            return;
        }

        try{


            int soLuong = (int) snSoLuong.getValue();

            // ===== lấy KM user chọn =====
            String tenKM = null;

            if(cbKhuyenMai.getSelectedIndex() > 0){
                tenKM = cbKhuyenMai.getSelectedItem().toString();


            }

            // ⭐ gọi đúng BUS
            boolean coToa = chbToaBacSi.isSelected();

            bus.themSanPham(maSP, soLuong, tenKM, coToa);

            loadCBVoucher();
            loadTableFromBUS();
            resetFormSanPham();
            tinhTienThoi();


        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
    }
    private void resetFormSanPham(){

        dangDongBo = true;

        cbMaSP.setSelectedIndex(0);
        cbTenSP.setSelectedIndex(0);

        cbKhuyenMai.removeAllItems();
        cbKhuyenMai.addItem("Chọn khuyến mãi");

        dangDongBo = false;

        tableTTSP.clearSelection();

        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);

        snSoLuong.setValue(0);
        snSoLuong.setEnabled(false);
        btnThem.setEnabled(false);
    }
    /* ================== SỬA ================== */

    private void suaSanPham() {

        int dong = tableTTSP.getSelectedRow();
        if(dong < 0) return;

        String maSP = modelBang.getValueAt(dong,1).toString();
        int soLuong = (int) snSoLuong.getValue();

        boolean coToa = chbToaBacSi.isSelected();

        String tenKM = null;

        if(cbKhuyenMai.getSelectedIndex() > 0){

            // ⭐ kiểm tra SĐT trước khi cho sửa KM
            String sdt = txtSoDienThoai.getText().trim();

            if(sdt.isEmpty()){
                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng nhập SĐT để kiểm tra lượt sử dụng khuyến mãi!",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            tenKM = cbKhuyenMai.getSelectedItem().toString();
        }


        try{

            bus.capNhatSoLuong(maSP, soLuong, tenKM, coToa);

            loadTableFromBUS();
            loadCBVoucher();
            tinhTienThoi();
            resetFormSanPham();

        }catch(Exception ex){

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Không thể áp dụng khuyến mãi",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void hienTongTien(){

        HoaDonBan_DTO hd = bus.getHoaDon();

        if(hd == null) return;

        txtTongTien.setText(formatTien(hd.getTongTienGoc()));

        txtTongGTKM.setText(formatTien(hd.getTongGiaTriKhuyenMai()));

        txtThanhTien.setText(formatTien(hd.getThanhTien()));
    }

    private void resetFormHoaDon(){

        modelBang.setRowCount(0);

        txtSoDienThoai.setText("");
        txtTenKhachHang.setText("");

        chbDiemThuong.setSelected(false);
        chbToaBacSi.setSelected(false);

        txtTienNhan.setText("");
        txtTienThoi.setText("");

        txtGhiChu.setText("");

        txtTongTien.setText("");
        txtGTDT.setText("");
        txtTongGTKM.setText("");
        txtThanhTien.setText("");
        txtThueVat.setText("");

        cbDaChyenKhoan.setSelected(false);

        loadCBVoucher();

        resetFormSanPham();

        capNhatThongTinHoaDon();
    }
    private boolean validateThongTin(){

        if(bus.getDsTam().isEmpty()){
            JOptionPane.showMessageDialog(this,"Chưa có sản phẩm");
            return false;
        }

        // Nếu KHÔNG chuyển khoản thì mới kiểm tra tiền nhận
        if(!cbDaChyenKhoan.isSelected()){

            if(txtTienNhan.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(this,"Nhập tiền nhận");
                return false;
            }

            try{

                double tienNhan = Double.parseDouble(
                        txtTienNhan.getText()
                                .replace(".", "")
                                .replace("đ", "")
                                .trim()
                );

                double thanhTien = bus.getHoaDon().getThanhTien();

                // chỉ lỗi khi tiền nhận nhỏ hơn
                if(tienNhan < thanhTien){
                    JOptionPane.showMessageDialog(
                            this,
                            "Vui lòng nhập lại số tiền!"
                    );
                    return false;
                }

            }catch(Exception e){

                JOptionPane.showMessageDialog(this,"Tiền nhận không hợp lệ");
                return false;
            }

        }

        return true;
    }
    private void goiYMaSP(String text){

        cbMaSP.removeAllItems();

        for(SanPham_DTO sp : spBus.getAll()){
            if(sp.getMaSP().toLowerCase().contains(text.toLowerCase())){
                cbMaSP.addItem(sp.getMaSP());
            }
        }

        JTextField editor = (JTextField) cbMaSP.getEditor().getEditorComponent();
        editor.setText(text);

        if(cbMaSP.getItemCount() > 0){
            cbMaSP.showPopup();
        }
    }

    private void goiYTenSP(String text){

        cbTenSP.removeAllItems();

        for(SanPham_DTO sp : spBus.getAll()){
            if(sp.getTenSP().toLowerCase().contains(text.toLowerCase())){
                cbTenSP.addItem(sp.getTenSP());
            }
        }

        JTextField editor = (JTextField) cbTenSP.getEditor().getEditorComponent();
        editor.setText(text);

        if(cbTenSP.getItemCount() > 0){
            cbTenSP.showPopup();
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
}
