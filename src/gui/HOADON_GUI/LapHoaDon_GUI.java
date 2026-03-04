package gui.HOADON_GUI;

import bus.KhuyenMai_BUS;
import bus.SanPham_BUS;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.awt.BorderLayout;
import bus.HoaDonBan_BUS;
import dto.ChiTietHoaDonBan_DTO;
import dto.HoaDonBan_DTO;
import dto.KhuyenMai_DTO;
import dto.SanPham_DTO;


public class LapHoaDon_GUI extends JPanel{

    private JPanel panel_LapHoaDon;
    private JButton btnXuatExcel;
    private JButton btnNhapExcel;
    private JCheckBox cbToaBacSi;
    private JCheckBox cbDiemThuong;
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
    private JPanel labelThanhTien;
    private JLabel labelTienNhan;
    private JLabel labelTienThoi;
    private JDateChooser JDateChooser1;
    private JSpinner snSoLuong;

    private boolean dangDongBo = false;

    private HoaDonBan_BUS bus = HoaDonBan_BUS.getInstance();
    // ===== BUS =====
    private SanPham_BUS spBus = SanPham_BUS.getInstance();
    private KhuyenMai_BUS kmBus = KhuyenMai_BUS.getInstance();

    private DefaultTableModel modelBang;

    public LapHoaDon_GUI() {
        cbMaSP.setEditable(true);
        cbTenSP.setEditable(true);
        khoiTaoBang();
        suKienNut();
        loadComboBox();

        this.setLayout(new BorderLayout());
        this.add(panel_LapHoaDon, BorderLayout.CENTER);

        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);

    }
    private void khoiTaoBang() {
        modelBang = new DefaultTableModel(
                new String[]{
                        "STT","Mã sản phẩm","Tên sản phẩm","Đơn vị tính", "Giá bán", "Khuyến mãi","Giá sau khuyến mãi","Số lượng","Thành tiền"
                }, 0
        );
        tableTTSP.setModel(modelBang);

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
        loadCBKhuyenMai();
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
    private void loadCBKhuyenMai(){

        cbKhuyenMai.removeAllItems();
        cbKhuyenMai.addItem(null);

        for(KhuyenMai_DTO km : kmBus.getAll()){
            cbKhuyenMai.addItem(km.getTenKM());
        }
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

                    btnSua.setEnabled(true);
                    btnXoa.setEnabled(true);
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
            hienTongTien();
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

            String tenSP = bus.getTenSP(maSP);

            if(tenSP != null){
                dangDongBo = true;
                cbTenSP.setSelectedItem(tenSP);
                dangDongBo = false;
            }
        });
        cbTenSP.addActionListener(e -> {

            if(dangDongBo) return;

            Object obj = cbTenSP.getSelectedItem();
            if(obj == null) return;

            String tenSP = obj.toString().trim();

            for(SanPham_DTO sp : spBus.getAll()){

                if(sp.getTenSP().equalsIgnoreCase(tenSP)){

                    dangDongBo = true;
                    cbMaSP.setSelectedItem(sp.getMaSP());
                    dangDongBo = false;

                    break;
                }
            }
        });
        /*
        cbVoucher.addActionListener(e -> {

            String ten =
                    (String) cbVoucher.getSelectedItem();

            bus.apDungVoucher(ten);
            hienTongTien();
        });
        */

    }



    /* ================== HÀM THÊM ================== */

    private void themSanPham() {

        try{
            String maSP = cbMaSP.getSelectedItem()+"";

            int soLuong = (int) snSoLuong.getValue();

            // ===== lấy KM user chọn =====
            String tenKM = null;

            if(cbKhuyenMai.getSelectedIndex() > 0){
                 tenKM = cbKhuyenMai.getSelectedItem().toString();


            }

            // ⭐ gọi đúng BUS
            bus.themSanPham(maSP, soLuong, tenKM);

            loadTableFromBUS();
            resetFormSanPham();
            hienTongTien();

        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
    }
    private void resetFormSanPham(){

        dangDongBo = true;

        cbMaSP.setSelectedIndex(0);
        cbTenSP.setSelectedIndex(0);
        cbKhuyenMai.setSelectedIndex(0);

        dangDongBo = false;

        tableTTSP.clearSelection();

        // khóa lại nút
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
    }
    /* ================== SỬA ================== */

    private void suaSanPham() {

        int dong = tableTTSP.getSelectedRow();
        if(dong < 0) return;

        modelBang.setValueAt(cbMaSP.getSelectedItem(),dong,1);
        modelBang.setValueAt(cbTenSP.getSelectedItem(),dong,2);

        JOptionPane.showMessageDialog(null,"Đã sửa!");
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

    private void createUIComponents() {
        JDateChooser1 = new com.toedter.calendar.JDateChooser();
        JDateChooser1.setDateFormatString("dd/MM/yyyy");
    }

}

