package gui.HOADON_GUI;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.awt.BorderLayout;
import bus.HoaDonBan_BUS;
import dto.ChiTietHoaDonBan_DTO;



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
    private JComboBox cbKM;
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

    private HoaDonBan_BUS bus = HoaDonBan_BUS.getInstance();

    private DefaultTableModel modelBang;

    public LapHoaDon_GUI() {
        khoiTaoBang();
        suKienNut();

        this.setLayout(new BorderLayout());
        this.add(panel_LapHoaDon, BorderLayout.CENTER);

    }


    /* ================== KHỞI TẠO BẢNG ================== */

    private void khoiTaoBang() {
        modelBang = new DefaultTableModel(
                new String[]{
                        "STT","Mã sản phẩm","Tên sản phẩm","Công dụng","Đơn vị tính", "Số lượng","Giá bán", "Khuyến mãi","Thành tiền"
                }, 0
        );
        tableTTSP.setModel(modelBang);
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
                }
            }
        });

        /* ===== XOÁ ===== */
        btnXoa.addActionListener(e -> {
            int dong = tableTTSP.getSelectedRow();
            if (dong >= 0) {
                int xacNhan = JOptionPane.showConfirmDialog(
                        null,"Xoá sản phẩm?",
                        "Xác nhận",JOptionPane.YES_NO_OPTION);

                if (xacNhan == JOptionPane.YES_OPTION) {
                    modelBang.removeRow(dong);
                    tinhTien();
                }
            }
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
    }



    /* ================== HÀM THÊM ================== */

    private void themSanPham() {

        String ma = cbMaSP.getSelectedItem()+"";
        String ten = cbTenSP.getSelectedItem()+"";

        if(ma.equals("") && ten.equals("")) {
            JOptionPane.showMessageDialog(null,"Chọn sản phẩm!");
            return;
        }

        int stt = modelBang.getRowCount()+1;

        double gia = 10000;
        double km = 0;
        double thanhTien = gia - km;

        modelBang.addRow(new Object[]{
                stt,ma,ten,"Thuốc ho","Hộp",1,gia,km,thanhTien
        });

        tinhTien();
    }

    /* ================== SỬA ================== */

    private void suaSanPham() {

        int dong = tableTTSP.getSelectedRow();
        if(dong < 0) return;

        modelBang.setValueAt(cbMaSP.getSelectedItem(),dong,1);
        modelBang.setValueAt(cbTenSP.getSelectedItem(),dong,2);

        JOptionPane.showMessageDialog(null,"Đã sửa!");
    }

    /* ================== TÍNH TIỀN ================== */

    private void tinhTien() {

        double tong = 0;

        for(int i=0;i<modelBang.getRowCount();i++) {
            tong += Double.parseDouble(
                    modelBang.getValueAt(i,6).toString());
        }

        txtTongTien.setText(tong+"");

        double gtdt = txtGTDT.getText().isEmpty()?0:
                Double.parseDouble(txtGTDT.getText());

        double gtkm = txtTongGTKM.getText().isEmpty()?0:
                Double.parseDouble(txtTongGTKM.getText());

        double voucher = 0;
        double vat = 0.1; // VAT 10%

        double thanhTien =
                (tong - gtdt - gtkm - voucher)
                        + (tong*vat);

        txtThanhTien.setText(String.valueOf(thanhTien));

        tinhTienThoi();
    }

    /* ================== TIỀN THỐI ================== */

    private void tinhTienThoi() {
        try {
            double nhan =
                    Double.parseDouble(txtTienNhan.getText());
            double thanh =
                    Double.parseDouble(txtThanhTien.getText());

            txtTienThoi.setText((nhan-thanh)+"");
        } catch(Exception ignored){}
    }

    private void createUIComponents() {
        JDateChooser1 = new com.toedter.calendar.JDateChooser();
        JDateChooser1.setDateFormatString("dd/MM/yyyy");
    }

}

