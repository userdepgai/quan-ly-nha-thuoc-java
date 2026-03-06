package gui.HOADON_GUI;

import bus.KhachHang_BUS;
import bus.KhuyenMai_BUS;
import bus.SanPham_BUS;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

import bus.HoaDonBan_BUS;
import dto.*;
import utils.Session;


public class LapHoaDon_GUI extends JPanel{
    private JPanel panel_LapHoaDon;
    private JButton btnXuatExcel;
    private JButton btnNhapExcel;
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

    private boolean dangDongBo = false;
    private Runnable onHoaDonSaved;
    private HoaDonBan_BUS bus = HoaDonBan_BUS.getInstance();
    // ===== BUS =====
    private SanPham_BUS spBus = SanPham_BUS.getInstance();
    private KhuyenMai_BUS kmBus = KhuyenMai_BUS.getInstance();

    private DefaultTableModel modelBang;

    public void setOnHoaDonSaved(Runnable action){
        this.onHoaDonSaved = action;
    }
    public LapHoaDon_GUI() {

            if(bus.getHoaDon() == null && Session.getCurrentUser() != null){
                bus.taoHoaDonMoi();
            }

            HoaDonBan_DTO hd = bus.getHoaDon();
            if(hd != null){
                txtNhanVienLap.setText(hd.getMaNhanVien());
            }

        cbMaSP.setEditable(true);
        cbTenSP.setEditable(true);
        khoiTaoBang();
        suKienNut();
        loadComboBox();
        themSuKienNhapCombo();
        khoaTextField();
        //loadCBVoucher();
        txtNgayLap.setText(
                new java.text.SimpleDateFormat("dd/MM/yyyy")
                        .format(new java.util.Date())
        );
        capNhatThongTinHoaDon();
        this.setLayout(new BorderLayout());
        this.add(panel_LapHoaDon, BorderLayout.CENTER);
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        snSoLuong.setModel(
                new SpinnerNumberModel(0,0,999,1)
        );
        snSoLuong.setEnabled(false);

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
                        "STT","Mã sản phẩm","Tên sản phẩm","Đơn vị tính", "Giá bán", "Khuyến mãi","Giá sau khuyến mãi","Số lượng","Thành tiền"
                }, 0
        );
        tableTTSP.setModel(modelBang);

        tableTTSP.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableTTSP.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableTTSP.getColumnModel().getColumn(1).setPreferredWidth(120);
        tableTTSP.getColumnModel().getColumn(2).setPreferredWidth(200);
        tableTTSP.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableTTSP.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableTTSP.getColumnModel().getColumn(5).setPreferredWidth(150);
        tableTTSP.getColumnModel().getColumn(6).setPreferredWidth(150);
        tableTTSP.getColumnModel().getColumn(7).setPreferredWidth(80);
        tableTTSP.getColumnModel().getColumn(8).setPreferredWidth(120);

        tableTTSP.getTableHeader().setResizingAllowed(false);
        tableTTSP.getTableHeader().setReorderingAllowed(false);
        tableTTSP.setRowHeight(25);
        tableTTSP.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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


    /*
    private void loadCBVoucher(){

    cbVoucher.removeAllItems();

    cbVoucher.addItem("Không voucher");

    if(bus.getHoaDon() == null) return;

    for(Voucher_DTO v : bus.goiYVoucher()){
            cbVoucher.addItem(v.getTen());
}
    }

    cbVoucher.setSelectedIndex(0);
}
    */
    private void themSuKienNhapCombo(){

        JTextField txtMa =
                (JTextField) cbMaSP.getEditor().getEditorComponent();

        JTextField txtTen =
                (JTextField) cbTenSP.getEditor().getEditorComponent();

        txtMa.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){
                dongBoTheoMa();
            }
        });

        txtTen.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){
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
        double giaBan = bus.getGiaBanSP(ma);

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
        double giaBan = bus.getGiaBanSP(sp.getMaSP());

        loadCBKhuyenMai(
                sp.getMaSP(),
                sp.getMaDM(),
                giaBan
        );
    }
    private void loadTableFromBUS(){

        modelBang.setRowCount(0);

        int stt = 1;

        for(ChiTietHoaDonBan_DTO ct : bus.getDsTam()){

            modelBang.addRow(new Object[]{
                    stt++,
                    ct.getMaSP(),
                    bus.getTenSP(ct.getMaSP()),
                    bus.getDonViTinh(ct.getMaSP()),
                    ct.getGiaBan(),
                    bus.getTenKhuyenMai(ct.getMaKhuyenMai()),
                    ct.getGiaBanSauApKM(),
                    ct.getSoLuong(),
                    ct.getThanhTien()
            });
        }
        capNhatThongTinHoaDon();
    }
    /* ================== KHỞI TẠO BẢNG ================== */


    private void tinhTienThoi(){

        try{
            double tienNhan =
                    Double.parseDouble(txtTienNhan.getText());

            double thanhTien =
                    Double.parseDouble(txtThanhTien.getText());

            txtTienThoi.setText(
                    String.valueOf(tienNhan - thanhTien)
            );

        }catch(Exception ignored){}
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
            capNhatThongTinHoaDon();
        });

        /* ===== SỬA ===== */
        btnSua.addActionListener(e -> suaSanPham());

        /* ===== CHECK CHUYỂN KHOẢN ===== */
        cbDaChyenKhoan.addActionListener(e -> {
            if(cbDaChyenKhoan.isSelected()) {
                txtTienNhan.setText(txtThanhTien.getText());
                txtTienThoi.setText("0");
            }
        });

        /* ===== NHẬP TIỀN NHẬN ===== */
        txtTienNhan.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                tinhTienThoi();
            }
        });

        btnLuu.addActionListener(e -> {
            if(!validateThongTin()) return;
            boolean ok = bus.luuHoaDon();

            if(!ok){
                JOptionPane.showMessageDialog(this,
                        "Lưu hóa đơn thất bại!");
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Lưu hóa đơn thành công!");

            if(onHoaDonSaved != null){
                onHoaDonSaved.run();
            }
            bus.taoHoaDonMoi();
            modelBang.setRowCount(0);
            txtSoDienThoai.setText("");
            txtTenKhachHang.setText("");
            chbDiemThuong.setSelected(false);
            capNhatThongTinHoaDon();
            JFrame parent =
                    (JFrame) SwingUtilities.getWindowAncestor(this);

            new XuatHoaDon_GUI(parent).setVisible(true);
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
                double giaBan = bus.getGiaBanSP(maSP);

                loadCBKhuyenMai(
                        maSP,
                        sp.getMaDM(),
                        giaBan
                );


            }
            snSoLuong.setEnabled(true);
            snSoLuong.setValue(1);
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
                double giaBan = bus.getGiaBanSP(sp.getMaSP());

                loadCBKhuyenMai(
                        sp.getMaSP(),
                        sp.getMaDM(),
                        giaBan
                );
            }

            snSoLuong.setEnabled(true);
            snSoLuong.setValue(1);
        });
        /*
        cbVoucher.addActionListener(e -> {

            String ten =
                    (String) cbVoucher.getSelectedItem();

            bus.apDungVoucher(ten);
            capNhatThongTinHoaDon();
        });
        */
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

                // nếu chưa nhập gì
                if(sdt.isEmpty()){
                    txtTenKhachHang.setText("");
                    bus.getHoaDon().setMaKhachHang(null);
                    return;
                }

                KhachHang_DTO kh =
                        KhachHang_BUS.getInstance().getBysdt(sdt);

                if(kh != null){

                    // hiện tên khách
                    txtTenKhachHang.setText(kh.getTen());

                    // gán vào hóa đơn
                    bus.getHoaDon().setMaKhachHang(kh.getMa());

                }else{

                    // không tìm thấy
                    txtTenKhachHang.setText("");

                    bus.getHoaDon().setMaKhachHang(null);
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
                    "Mã sản phẩm không tồn tại");
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

            //loadCBVoucher();
            loadTableFromBUS();
            resetFormSanPham();
            capNhatThongTinHoaDon();

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
            tenKM = cbKhuyenMai.getSelectedItem().toString();
        }

        bus.xoaSanPham(maSP);
        bus.themSanPham(maSP, soLuong, tenKM, coToa);

        loadTableFromBUS();
    }
    private void hienTongTien(){

        HoaDonBan_DTO hd = bus.getHoaDon();

        if(hd == null) return;

        txtTongTien.setText(
                String.valueOf(hd.getTongTienGoc()));

        txtTongGTKM.setText(
                String.valueOf(hd.getTongGiaTriKhuyenMai()));

        txtThanhTien.setText(
                String.valueOf(hd.getThanhTien()));
    }
    private void capNhatThongTinHoaDon(){

        HoaDonBan_DTO hd = bus.getHoaDon();
        if(hd == null){
            txtTongTien.setText("0");
            txtTongGTKM.setText("0");
            txtGTDT.setText("0");
            txtThanhTien.setText("0");
            txtTienThoi.setText("0");
            return;
        }

        txtTongTien.setText(String.valueOf(hd.getTongTienGoc()));
        txtTongGTKM.setText(String.valueOf(hd.getTongGiaTriKhuyenMai()));
        txtGTDT.setText(String.valueOf(hd.getDiemThuongQuyDoi()));
        txtThanhTien.setText(String.valueOf(hd.getThanhTien()));

        tinhTienThoi();
    }

    private boolean validateThongTin(){

        if(bus.getDsTam().isEmpty()){
            JOptionPane.showMessageDialog(this,"Chưa có sản phẩm");
            return false;
        }


        if(txtTienNhan.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this,"Nhập tiền nhận");
            return false;
        }

        try{
            Double.parseDouble(txtTienNhan.getText());
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Tiền nhận không hợp lệ");
            return false;
        }

        return true;
    }
}

