package gui;

import bus.KhuVucLuuTru_BUS;
import bus.PhieuNhapKho_BUS;
import com.toedter.calendar.JDateChooser;
import dto.ChiTietPhieuNhapKho_DTO;
import dto.KhuVucLuuTru_DTO;
import dto.LoHang_DTO;
import dto.PhieuNhapKho_DTO;
import gui.NCCKVLT.KVLT;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

public class PhieuNhap_GUI extends JPanel {
    private JButton btnXuatPDF;
    private JTextField txtLocMaPNK;
    private JTextField txtHienCo;
    private JComboBox cmbLocNV;
    private JComboBox cmbLocNCC;
    private JComboBox cmbLocKVLT;
    private JComboBox cmbLocTrangThai;
    private JButton btnThoat;
    private JButton btnTimKiem;
    private JButton btnCapNhat;
    private JButton btnThem;
    private JTextField txtMaPNK;
    private JComboBox cmbKVLT;
    private JComboBox cmbNCC;
    private JComboBox cmbTrangThai;
    private JTable tableDSPNK;
    private JPanel panel_phieuNhap;
    private JPanel panel_thongTinPhieu;
    private JTable tableCTPNK;
    private JPanel panel_boLoc;
    private JDateChooser JDateChooser1;
    private JDateChooser JDateChooser2;
    private JPanel date_bdNgayLap;
    private JPanel date_ktNgayLap;
    private JDateChooser JDateChooser3;
    private JDateChooser JDateChooser4;
    private JLabel labelMaPNK;
    private JLabel labelNhanVien;
    private JComboBox cmbSanPham;
    private JTextField txtMaSP;
    private JTextField txtGiaNhap;
    private JLabel labelNCC;
    private JLabel labelKVLT;
    private JLabel labelTrangThai;
    private JLabel labelNgayHoanThanh;
    private JLabel labelThanhTien;
    private JLabel labelSanPham;
    private JLabel labelMaSP;
    private JLabel labelGiaNhap;
    private JTextField txtTongTien;
    private JButton btnHuySP;
    private JButton btnLuuSP;
    private JLabel labelTongTien;
    private JScrollPane srcDS;
    private JScrollPane srcCT;
    private JLabel labelLocMaPNK;
    private JLabel labelHienCo;
    private JLabel labellocNLBD;
    private JLabel labelLocNLKT;
    private JLabel lableLocNHTBD;
    private JLabel labelLocNHTKT;
    private JLabel labelLocNhanVien;
    private JLabel labelLocNCC;
    private JLabel labelLocSP;
    private JTextField txtLocMaSP;
    private JLabel labelLocKVLT;
    private JLabel labelLocTrangThai;
    private JButton btnXoa;
    private JTextField txtSoLuong;
    private JLabel labelSoLuong;
    private JTextField txtNgayHoanThanh;
    private JTextField txtThanhTien;
    private JTextField txtNgayLap;
    private JButton btnHuyPhieu;
    private JButton btnLuuPhieu;
    private JButton btnThemSP;
    private JLabel labelNgayLap;
    private JTextField txtNhanVien;
    private boolean isAddingPNK = false;
    private boolean isUpdatingPNK = false;
    private boolean isLoadingCombo = false;

    private PhieuNhapKho_BUS bus = PhieuNhapKho_BUS.getInstance();
    private DefaultTableModel modelDSPNK;
    private DefaultTableModel modelCTPNK;

    public PhieuNhap_GUI() {
        this.setLayout(new BorderLayout());
        this.add(panel_phieuNhap, BorderLayout.CENTER);

        formEdit();

        xuLySuKien();
    }
    private void formEdit() {
        initTableDSPNK();
        initTableCTPNK();
        loadTableDSPNKFromList();

        taoCmbLocNhanVien();
        taoCmbLocNCC();
        taoCmbLocKVLT();
        taoCmbLocTrangThai();

        setViewMode();
    }

    private void initTableDSPNK() {
        String[] columns = {
                "STT",
                "Mã PNK",
                "Nhân viên lập",
                "Nhà cung cấp",
                "KVLT",
                "Ngày lập",
                "Ngày hoàn thành",
                "Thành tiền",
                "Trạng thái"
        };
        modelDSPNK = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableDSPNK.setModel(modelDSPNK);

        tableDSPNK.getTableHeader().setResizingAllowed(false);
        tableDSPNK.getTableHeader().setReorderingAllowed(false);

        tableDSPNK.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tableDSPNK.getColumnModel().getColumn(0).setPreferredWidth(55);
        tableDSPNK.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableDSPNK.getColumnModel().getColumn(2).setPreferredWidth(155);
        tableDSPNK.getColumnModel().getColumn(3).setPreferredWidth(189);
        tableDSPNK.getColumnModel().getColumn(4).setPreferredWidth(170);
        tableDSPNK.getColumnModel().getColumn(5).setPreferredWidth(125);
        tableDSPNK.getColumnModel().getColumn(6).setPreferredWidth(125);
        tableDSPNK.getColumnModel().getColumn(7).setPreferredWidth(125);
        tableDSPNK.getColumnModel().getColumn(8).setPreferredWidth(110);

        tableDSPNK.setRowHeight(25);
        tableDSPNK.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        srcDS.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        srcDS.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
    }
    private void initTableCTPNK() {

        String[] columns = {
                "STT",
                "Mã SP",
                "Mã lô",
                "Số lượng",
                "Giá nhập",
                "Tổng tiền"
        };

        modelCTPNK = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableCTPNK.setModel(modelCTPNK);

        tableCTPNK.getTableHeader().setResizingAllowed(false);
        tableCTPNK.getTableHeader().setReorderingAllowed(false);

        tableCTPNK.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        tableCTPNK.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableCTPNK.getColumnModel().getColumn(1).setPreferredWidth(90);
        tableCTPNK.getColumnModel().getColumn(2).setPreferredWidth(90);
        tableCTPNK.getColumnModel().getColumn(3).setPreferredWidth(80);
        tableCTPNK.getColumnModel().getColumn(4).setPreferredWidth(115);
        tableCTPNK.getColumnModel().getColumn(5).setPreferredWidth(115);

        tableCTPNK.setRowHeight(25);
        tableCTPNK.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        srcCT.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        srcCT.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
    }

    public void loadTableDSPNKFromList() {
        ArrayList<PhieuNhapKho_DTO> list = bus.getAll();
        modelDSPNK.setRowCount(0);
        if (list == null || list.isEmpty()) {
            return;
        }
        int stt = 1;
        for (PhieuNhapKho_DTO pnk : list) {
            String tenNV = bus.getNameNV(pnk.getMaNhanVien());
            String tenNCC = bus.getNameNCC(pnk.getMaNCC());
            String tenKVLT = bus.getNameKVLT(pnk.getMaKVLT());
            String ngayLap = "";
            if (pnk.getNgayLap() != null) {
                ngayLap = pnk.getNgayLap().toLocalDate().toString();
            }
            String ngayHoanThanh = "";
            if (pnk.getNgayHoanThanh() != null) {
                ngayHoanThanh = pnk.getNgayHoanThanh().toLocalDate().toString();
            }
            modelDSPNK.addRow(new Object[]{
                    stt++,
                    pnk.getMa(),
                    tenNV,
                    tenNCC,
                    tenKVLT,
                    ngayLap,
                    ngayHoanThanh,
                    formatTien(pnk.getThanhTien()),
                    pnk.getTrangThaiText()
            });
        }
    }
    private void loadTableDSPNKFromList(ArrayList<PhieuNhapKho_DTO> list) {
        modelDSPNK.setRowCount(0);
        if (list == null || list.isEmpty()) {
            return;
        }
        int stt = 1;
        for (PhieuNhapKho_DTO pnk : list) {
            String tenNV = bus.getNameNV(pnk.getMaNhanVien());
            String tenNCC = bus.getNameNCC(pnk.getMaNCC());
            String tenKVLT = bus.getNameKVLT(pnk.getMaKVLT());
            String ngayLap = "";
            if (pnk.getNgayLap() != null) {
                ngayLap = pnk.getNgayLap().toLocalDate().toString();
            }
            String ngayHoanThanh = "";
            if (pnk.getNgayHoanThanh() != null) {
                ngayHoanThanh = pnk.getNgayHoanThanh().toLocalDate().toString();
            }
            modelDSPNK.addRow(new Object[]{
                    stt++,
                    pnk.getMa(),
                    tenNV,
                    tenNCC,
                    tenKVLT,
                    ngayLap,
                    ngayHoanThanh,
                    formatTien(pnk.getThanhTien()),
                    pnk.getTrangThaiText()
            });
        }
    }
    private void xuLySuKien() {
        tableDSPNK.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienChiTietDS();
            }
        });
        tableCTPNK.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienChiTietCT();
            }
        });
        btnTimKiem.addActionListener(e -> {
            xuLyTimKiem();
        });
        btnThoat.addActionListener(e->{
            xuLyThoat();
        });
        txtSoLuong.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                tinhTongTien();
            }
        });
        xuKienKhiChonCmbSP();

        btnThem.addActionListener(e -> {
            xuLyThemPNK();
        });
        btnCapNhat.addActionListener(e -> {
            xuLyCapNhatPNK();
        });
        btnThemSP.addActionListener(e -> {
            xuLyThemSP();
        });
        btnLuuPhieu.addActionListener(e -> {
            if (isAddingPNK)
                xuLyLuuPhieu();
            else if (isUpdatingPNK) {
                xuLyLuuPhieuCapNhat();
            }
        });
        btnHuyPhieu.addActionListener(e -> {
            xuLyHuyPhieu();
        });
        btnLuuSP.addActionListener(e -> {
            xuLyLuuSP();
        });
        btnHuySP.addActionListener(e -> {
            xuLyHuySP();
        });
        btnXoa.addActionListener(e -> {
            xuLyXoaSP();
        });
    }
    private void xuLyTimKiem() {
        String maPNK = txtLocMaPNK.getText().trim();
        String maSP  = txtLocMaSP.getText().trim();

        String maNV = null;
        if (!cmbLocNV.getSelectedItem().toString().equals("Tất cả") || cmbLocNV.getSelectedIndex() != 0) {
            maNV = bus.getMaNhanVienByTen(cmbLocNV.getSelectedItem().toString());
        }

        String maNCC = null;
        if (!cmbLocNCC.getSelectedItem().toString().equals("Tất cả") || cmbLocNCC.getSelectedIndex() != 0) {
            maNCC = bus.getMaNCCByTen(cmbLocNCC.getSelectedItem().toString());
        }

        String maKVLT = null;
        if (!cmbLocKVLT.getSelectedItem().toString().equals("Tất cả") || cmbLocKVLT.getSelectedIndex() != 0) {
            maKVLT = bus.getMaKVLTByTen(cmbLocKVLT.getSelectedItem().toString());
        }

        Integer trangThai = null;
        if (!cmbLocTrangThai.getSelectedItem().toString().equals("Tất cả") || cmbLocTrangThai.getSelectedIndex() != 0) {
            String tt = cmbLocTrangThai.getSelectedItem().toString();

            if (tt.equals(PhieuNhapKho_DTO.CHUAN_BI))
                trangThai = PhieuNhapKho_DTO.TT_CHUAN_BI;
            else if (tt.equals(PhieuNhapKho_DTO.CHO))
                trangThai = PhieuNhapKho_DTO.TT_CHO;
            else if (tt.equals(PhieuNhapKho_DTO.HOAN_THANH))
                trangThai = PhieuNhapKho_DTO.TT_HOAN_THANH;
        }
        LocalDateTime ngayLapFrom = getDate(JDateChooser1);
        LocalDateTime ngayLapTo   = getDate(JDateChooser2);
        LocalDateTime ngayHTFrom  = getDate(JDateChooser3);
        LocalDateTime ngayHTTo    = getDate(JDateChooser4);
        ArrayList<PhieuNhapKho_DTO> result = bus.timKiem(
                maPNK,
                maSP,
                maNCC,
                maNV,
                maKVLT,
                trangThai,
                ngayLapFrom,
                ngayLapTo,
                ngayHTFrom,
                ngayHTTo
        );
        loadTableDSPNKFromList(result);
        txtHienCo.setText(String.valueOf(result.size()));
    }
    private void xuLyThoat() {
        txtLocMaPNK.setText("");
        txtLocMaSP.setText("");
        txtHienCo.setText("");

        cmbLocNV.setSelectedIndex(0);
        cmbLocNCC.setSelectedIndex(0);
        cmbLocKVLT.setSelectedIndex(0);
        cmbLocTrangThai.setSelectedIndex(0);

        JDateChooser1.setDate(null);
        JDateChooser2.setDate(null);
        JDateChooser3.setDate(null);
        JDateChooser4.setDate(null);

        ArrayList<PhieuNhapKho_DTO> list = bus.getAll();
        loadTableDSPNKFromList();

        txtHienCo.setText(String.valueOf(list.size()));

        tableDSPNK.clearSelection();
        modelCTPNK.setRowCount(0);

        clearFormTTP();
        clearFormCT();
    }
    private void xuLyThemPNK() {
        lockTableDS();
        clearFormTTP();
        clearFormCT();

        modelCTPNK.setRowCount(0);
        isAddingPNK = true;
        isUpdatingPNK = false;

        txtMaPNK.setText(bus.getNextId());
        txtNhanVien.setText(bus.getTenNVDangDangNhap());
        txtNgayLap.setText(LocalDate.now().toString());

        cmbKVLT.removeAllItems();
        cmbKVLT.setEnabled(true);
        taoCmbKVLTConHoatDong();

        cmbNCC.removeAllItems();
        cmbNCC.setEnabled(true);
        taoCmbNCCConHopTac();

        cmbTrangThai.removeAllItems();
        cmbTrangThai.addItem(PhieuNhapKho_DTO.CHUAN_BI);

        btnLuuPhieu.setVisible(true);
        btnHuyPhieu.setVisible(true);

        btnCapNhat.setEnabled(false);
        btnThem.setEnabled(false);
    }
    private void xuLyCapNhatPNK() {
        int row = tableDSPNK.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần cập nhật");
            return;
        }
        lockTableDS();

        isAddingPNK = false;
        isUpdatingPNK = true;
        PhieuNhapKho_DTO pnk = bus.getById(tableDSPNK.getValueAt(row,1).toString());

        if(pnk.getTrangThai() == PhieuNhapKho_DTO.TT_CHUAN_BI){
            cmbKVLT.removeAllItems();
            cmbKVLT.setEnabled(true);
            taoCmbKVLTConHoatDong();
        }


        String tenKho = bus.getNameKVLT(pnk.getMaKVLT());
        cmbKVLT.setSelectedItem(tenKho);

        if(pnk.getDs_chiTietPNK().isEmpty()){
            cmbNCC.removeAllItems();
            cmbNCC.setEnabled(true);
            taoCmbNCCConHopTac();
            cmbNCC.setSelectedItem(bus.getNameNCC(pnk.getMaNCC()));
        }
        cmbTrangThai.removeAllItems();
        if(pnk.getTrangThai() == PhieuNhapKho_DTO.TT_CHUAN_BI){
            cmbTrangThai.addItem(PhieuNhapKho_DTO.CHUAN_BI);
            cmbTrangThai.addItem(PhieuNhapKho_DTO.CHO);
            cmbTrangThai.setEnabled(true);
        } else if(pnk.getTrangThai() == PhieuNhapKho_DTO.TT_CHO){
            cmbTrangThai.addItem(PhieuNhapKho_DTO.CHO);
            cmbTrangThai.addItem(PhieuNhapKho_DTO.HOAN_THANH);
            cmbTrangThai.setEnabled(true);
        } else {
            cmbTrangThai.addItem(PhieuNhapKho_DTO.HOAN_THANH);
        }

        btnThemSP.setVisible(true);
        btnLuuPhieu.setVisible(true);
        btnHuyPhieu.setVisible(true);

        btnCapNhat.setEnabled(false);
        btnThem.setEnabled(false);
    }
    private void xuLyLuuPhieuCapNhat() {
        int row = tableDSPNK.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần cập nhật");
            return;
        }
        String maPNK = txtMaPNK.getText().trim();
        PhieuNhapKho_DTO pnk = bus.getById(maPNK);
        int trangThaiCu = pnk.getTrangThai();

        String trangThaiMoiStr = cmbTrangThai.getSelectedItem().toString();
        int trangThaiMoi = PhieuNhapKho_DTO.parseTrangThaiFromText(trangThaiMoiStr);

        ArrayList<ChiTietPhieuNhapKho_DTO> dsCT = bus.getChiTiet(maPNK);

        if (trangThaiCu == PhieuNhapKho_DTO.TT_CHUAN_BI &&
                trangThaiMoi == PhieuNhapKho_DTO.TT_CHO) {
            if (dsCT.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không thể chuyển sang trạng thái CHỜ khi danh sách chi tiết rỗng");
                return;
            }
            if(!bus.kiemTraDuSucChua(pnk)) {
                JOptionPane.showMessageDialog(this,
                        "Kho không đủ sức chứa. Vui lòng chọn kho khác!");
                return;
            }
            KhuVucLuuTru_BUS kvltBus = KhuVucLuuTru_BUS.getInstance();
            for (ChiTietPhieuNhapKho_DTO ct : dsCT){
                kvltBus.capNhatSoThung(pnk.getMaKVLT(),ct.getSoLuong());
            }
        }

        if (trangThaiCu == PhieuNhapKho_DTO.TT_CHO &&
                trangThaiMoi == PhieuNhapKho_DTO.TT_HOAN_THANH) {
            for (ChiTietPhieuNhapKho_DTO ct : dsCT) {
                if (ct.getMaLo() == null || ct.getMaLo().trim().isEmpty() || ct.getMaLo().equals("")) {
                    JOptionPane.showMessageDialog(this,
                            "Tất cả chi tiết phải có mã lô trước khi hoàn thành phiếu");
                    return;
                }
            }
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn cập nhật phiếu?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        pnk.setTrangThai(trangThaiMoi);

        boolean result = bus.capNhat(pnk);

        if (result) {
//            if (trangThaiCu == PhieuNhapKho_DTO.TT_CHO &&
//                    trangThaiMoi == PhieuNhapKho_DTO.TT_HOAN_THANH) {
//                for (ChiTietPhieuNhapKho_DTO ct : dsCT) {
//                    bus.capNhatTrangThaiLoHang(ct.getMaLo(), LoHang_DTO.TT_HOAN_THANH);
//                    bus.congHienCoKho(pnk.getMaKVLT(),ct.getSoLuong());
//                }
//            }
            JOptionPane.showMessageDialog(this, "Cập nhật phiếu thành công");
            loadTableDSPNKFromList();
            setViewMode();

        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật phiếu thất bại");
        }
    }
    private void xuLyLuuPhieu() {
        if (!kiemTraFormThongTin())  return;
        String maPNK = txtMaPNK.getText().trim();
        String tenNCC = cmbNCC.getSelectedItem().toString();
        String tenKVLT = cmbKVLT.getSelectedItem().toString();


        String maNCC = bus.getMaNCCByTen(tenNCC);
        String maKVLT = bus.getMaKVLTByTen(tenKVLT);
        PhieuNhapKho_DTO pnk = new PhieuNhapKho_DTO();

        pnk.setMa(maPNK);
        pnk.setNgayLap(LocalDateTime.now());
        pnk.setTrangThai(PhieuNhapKho_DTO.TT_CHUAN_BI);
        pnk.setMaNhanVien(bus.getMaNVDangDangNhap());
        pnk.setMaNCC(maNCC);
        pnk.setMaKVLT(maKVLT);
        if (bus.them(pnk)) {
            JOptionPane.showMessageDialog(this, "Lưu phiếu thành công");
            btnThemSP.setVisible(true);
            btnThemSP.setEnabled(true);
            loadTableDSPNKFromList();
        } else {
            JOptionPane.showMessageDialog(this, "Lưu thất bại");
        }
    }
    private void xuLyHuyPhieu() {
        txtMaPNK.setText("");
        txtThanhTien.setText("");

        cmbNCC.removeAllItems();
        cmbKVLT.removeAllItems();
        cmbSanPham.removeAllItems();

        txtNgayLap.setText("");
        txtNgayHoanThanh.setText("");
        txtMaSP.setText("");
        txtSoLuong.setText("");
        txtGiaNhap.setText("");
        txtTongTien.setText("");

        modelCTPNK.setRowCount(0);
        btnThemSP.setEnabled(true);

        setViewMode();
    }
    private void xuLyThemSP() {

        btnXoa.setVisible(true);
        btnLuuSP.setVisible(true);
        btnHuySP.setVisible(true);

        clearFormCT();

        taoCmbSPCuaNCC(bus.getMaNCCByTen(cmbNCC.getSelectedItem().toString()));

        cmbSanPham.setEnabled(true);
        txtSoLuong.setEditable(true);

        btnThemSP.setEnabled(false);
    }
    private void xuKienKhiChonCmbSP() {
        cmbSanPham.addActionListener(e -> {
            if (isLoadingCombo) return;
            if (cmbSanPham.getSelectedIndex() == 0) {
                txtMaSP.setText("");
                txtGiaNhap.setText("");
                txtTongTien.setText("");
                return;
            }
            String maNCC = bus.getMaNCCByTen(cmbNCC.getSelectedItem().toString());
            String tenSP = cmbSanPham.getSelectedItem().toString();
            String maSP = bus.getMaSpByTen(tenSP);
            txtMaSP.setText(maSP);
            double giaNhap = bus.getGiaNhapTrongNCCSP(maNCC,maSP);
            txtGiaNhap.setText(formatTien(giaNhap));
            txtGiaNhap.setEditable(false);
            tinhTongTien();
        });
    }
    private void xuLyLuuSP() {
        if (!kiemTraFormChiTiet()) return;
        String maPNK = txtMaPNK.getText().trim();
        PhieuNhapKho_DTO pnk = PhieuNhapKho_BUS.getInstance().getById(maPNK);
        String maSP = txtMaSP.getText().trim();

        int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
        double giaNhap = parseTien(txtGiaNhap.getText());
        double tongTien = soLuong * giaNhap;

        int rowIndex = timDongSanPham(maSP);

        if (rowIndex != -1) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Sản phẩm đã tồn tại trong phiếu.\nBạn có muốn thay thế thông tin không?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int soLuongCu = 0;
                for(ChiTietPhieuNhapKho_DTO ct : pnk.getDs_chiTietPNK()){
                    if(ct.getMaSP().equals(maSP))
                        soLuongCu = ct.getSoLuong();
                }
                if(soLuong > soLuongCu){
                    if(!bus.kiemTraDuSucChua(pnk, soLuong-soLuongCu)){
                        JOptionPane.showMessageDialog(this, "Kho không đủ sức chứa");
                        return;
                    }
                }

                modelCTPNK.setValueAt(soLuong, rowIndex, 3);
                modelCTPNK.setValueAt(String.format("%,.0f", giaNhap), rowIndex, 4);
                modelCTPNK.setValueAt(String.format("%,.0f", tongTien), rowIndex, 5);

                ChiTietPhieuNhapKho_DTO ct = new ChiTietPhieuNhapKho_DTO();
                ct.setMaPNK(maPNK);
                ct.setMaSP(maSP);
                ct.setSoLuong(soLuong);

                boolean result = bus.capNhatChiTiet(ct);

                capNhatTongTienPhieu();

                JOptionPane.showMessageDialog(this, "Đã cập nhật sản phẩm");
            }
            return;
        }

        if (!bus.kiemTraDuSucChua(pnk,soLuong)) {
            JOptionPane.showMessageDialog(this, "Kho không đủ sức chứa");
            return;
        }

        ChiTietPhieuNhapKho_DTO ct = new ChiTietPhieuNhapKho_DTO();
        ct.setMaPNK(maPNK);
        ct.setMaSP(maSP);
        ct.setSoLuong(soLuong);

        boolean result = bus.themChiTiet(ct);

        if (result) {
            int stt = modelCTPNK.getRowCount() + 1;
            modelCTPNK.addRow(new Object[]{
                    stt,
                    maSP,
                    null,
                    soLuong,
                    formatTien(giaNhap),
                    formatTien(tongTien)
            });

            capNhatTongTienPhieu();

            JOptionPane.showMessageDialog(this, "Lưu sản phẩm thành công");

            cmbSanPham.setSelectedIndex(0);
            txtMaSP.setText("");
            txtSoLuong.setText("");
            txtGiaNhap.setText("");
            txtTongTien.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Lưu sản phẩm thất bại");
        }
    }
    private void xuLyHuySP() {
        cmbSanPham.setSelectedIndex(0);
        txtMaSP.setText("");
        txtSoLuong.setText("");
        txtGiaNhap.setText("");
        txtTongTien.setText("");

        tableCTPNK.clearSelection();
//        cmbSanPham.requestFocus();
    }
    private void xuLyXoaSP() {
        int row = tableCTPNK.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa sản phẩm này khỏi phiếu?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        String maPNK = txtMaPNK.getText().trim();
        String maSP = modelCTPNK.getValueAt(row, 1).toString();

        boolean result = bus.xoaChiTiet(maPNK,maSP);
        if(result) {
            modelCTPNK.removeRow(row);
            for (int i = 0; i < modelCTPNK.getRowCount(); i++) {
                modelCTPNK.setValueAt(i + 1, i, 0);
            }
            capNhatTongTienPhieu();

            xuLyHuySP();
            JOptionPane.showMessageDialog(this, "Đã xóa sản phẩm khỏi phiếu");
        } else {
            JOptionPane.showMessageDialog(this, "Xóa sản phẩm khỏi phiếu thất bại");
        }

    }
    private void hienChiTietDS() {
        int row = tableDSPNK.getSelectedRow();
        if(row < 0) return;
        PhieuNhapKho_DTO pnk = bus.getById(tableDSPNK.getValueAt(row,1).toString());
        taoCmbKVLTTheoMa(pnk.getMaKVLT());
        taoCmbNCCTheoMa(pnk.getMaNCC());
        taoCmbTrangThai(pnk.getTrangThai());

        txtMaPNK.setText(pnk.getMa());
        txtNgayLap.setText(tableDSPNK.getValueAt(row,5).toString());
        txtNgayHoanThanh.setText(tableDSPNK.getValueAt(row, 6).toString());
        txtThanhTien.setText(tableDSPNK.getValueAt(row, 7).toString());
        txtNhanVien.setText(bus.getNameNV(pnk.getMaNhanVien()));

        loadTableCTPNKFromList(pnk.getDs_chiTietPNK());
        clearFormCT();

    }
    private void loadTableCTPNKFromList(ArrayList<ChiTietPhieuNhapKho_DTO> list) {
        modelCTPNK.setRowCount(0);
        if (list == null || list.isEmpty()) {
            return;
        }
        int stt = 1;
        for (ChiTietPhieuNhapKho_DTO ct : list) {
            LoHang_DTO lo = bus.getLoById(ct.getMaLo());
            String maLo = "";
            String giaNhap = "";
            String tongTien = "";
            if (lo != null) {
                maLo = lo.getMaLo();
                giaNhap = formatTien(lo.getGiaNhap());
                tongTien = formatTien(lo.getThanhTien());
            }
            modelCTPNK.addRow(new Object[]{ stt++, ct.getMaSP(), maLo, ct.getSoLuong(),
                    giaNhap,
                    tongTien
            });
        }
    }
    private void hienChiTietCT() {
        int row = tableCTPNK.getSelectedRow();
        if (row < 0) return;

        isLoadingCombo = true;

        String maSP = tableCTPNK.getValueAt(row,1).toString();

        txtMaSP.setText(maSP);
        txtSoLuong.setText(tableCTPNK.getValueAt(row,3).toString());
        txtGiaNhap.setText(tableCTPNK.getValueAt(row,4).toString());
        txtTongTien.setText(tableCTPNK.getValueAt(row,5).toString());

        String maNCC = bus.getMaNCCByTen(cmbNCC.getSelectedItem().toString());
        taoCmbSPCuaNCC(maNCC);

        String tenSP = bus.getNameSP(maSP);

        cmbSanPham.setSelectedItem(tenSP);

        cmbSanPham.setEnabled(true);
        btnHuySP.setEnabled(true);

        isLoadingCombo = false;
    }
    private void taoCmbSPTheoMa(String maSP) {
        cmbSanPham.removeAllItems();
        cmbSanPham.addItem(bus.getNameSP(maSP));
    }
    private void taoCmbKVLTTheoMa(String maKVLT) {
        cmbKVLT.removeAllItems();
        cmbKVLT.addItem(bus.getNameKVLT(maKVLT));
    }
    private void taoCmbNCCTheoMa(String maNCC) {
        cmbNCC.removeAllItems();
        cmbNCC.addItem(bus.getNameNCC(maNCC));
    }
    private void taoCmbTrangThai(Integer trangThai) {
        cmbTrangThai.removeAllItems();
        if(trangThai == PhieuNhapKho_DTO.TT_CHUAN_BI)
            cmbTrangThai.addItem(PhieuNhapKho_DTO.CHUAN_BI);
        else if(trangThai == PhieuNhapKho_DTO.TT_CHO)
            cmbTrangThai.addItem(PhieuNhapKho_DTO.CHO);
        else if (trangThai == PhieuNhapKho_DTO.TT_HOAN_THANH)
            cmbTrangThai.addItem(PhieuNhapKho_DTO.HOAN_THANH);
    }
    private void taoCmbLocNhanVien() {
        cmbLocNV.removeAllItems();
        cmbLocNV.addItem("Tất cả");
        for (String ten : bus.getDsTenNhanVien()) {
            cmbLocNV.addItem(ten);
        }
    }
    private void taoCmbLocNCC() {
        cmbLocNCC.removeAllItems();
        cmbLocNCC.addItem("Tất cả");
        for (String ten : bus.getDsTenNCC()) {
            cmbLocNCC.addItem(ten);
        }
    }
    private void taoCmbLocKVLT() {
        cmbLocKVLT.removeAllItems();
        cmbLocKVLT.addItem("Tất cả");

        for (String ten : bus.getDsTenKVLT()) {
            cmbLocKVLT.addItem(ten);
        }
    }
    private void taoCmbLocTrangThai() {
        cmbLocTrangThai.removeAllItems();
        cmbLocTrangThai.addItem("Tất cả");
        cmbLocTrangThai.addItem(PhieuNhapKho_DTO.CHUAN_BI);
        cmbLocTrangThai.addItem(PhieuNhapKho_DTO.CHO);
        cmbLocTrangThai.addItem(PhieuNhapKho_DTO.HOAN_THANH);
    }
    private void taoCmbKVLTConHoatDong() {
        cmbKVLT.removeAllItems();
        cmbKVLT.addItem("-- Chọn kho --");
        for(String ten : bus.getTenKVLTConSuDung())
            cmbKVLT.addItem(ten);
    }
    private void taoCmbNCCConHopTac() {
        cmbNCC.removeAllItems();
        cmbNCC.addItem("-- Chọn nhà cung cấp --");
        for(String ten : bus.getTenNCCConHopTac())
            cmbNCC.addItem(ten);
    }
    private void taoCmbSPCuaNCC(String maNCC) {
        cmbSanPham.removeAllItems();
        cmbSanPham.addItem("-- Chọn sản phẩm --");
        for(String ten : bus.getDSTenSPCuaNCC(maNCC))
            cmbSanPham.addItem(ten);
    }
    private LocalDateTime getDate(JDateChooser chooser) {
        if (chooser.getDate() == null) return null;
        return chooser.getDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();
    }
    private void clearFormTTP() {
        txtNgayHoanThanh.setText("");
        txtNgayLap.setText("");
        txtMaPNK.setText("");
        txtThanhTien.setText("");
        txtNhanVien.setText("");

        cmbNCC.removeAllItems();
        cmbKVLT.removeAllItems();
        cmbTrangThai.removeAllItems();
    }
    private void clearFormCT() {
        txtMaSP.setText("");
        txtSoLuong.setText("");
        txtGiaNhap.setText("");
        txtTongTien.setText("");

        cmbSanPham.removeAllItems();
    }
    private void setViewMode() {
        unlockTableDS();
        unlockTableCT();

        isUpdatingPNK = false;
        isAddingPNK = false;

        txtMaPNK.setEditable(false);
        txtGiaNhap.setEditable(false);
        txtThanhTien.setEditable(false);
        txtTongTien.setEditable(false);
        txtMaSP.setEditable(false);
        txtSoLuong.setEditable(false);
        txtNgayLap.setEditable(false);
        txtNgayHoanThanh.setEditable(false);
        txtNhanVien.setEditable(false);

        cmbTrangThai.setEnabled(false);
        cmbKVLT.removeAllItems();
        cmbKVLT.setEnabled(false);
        cmbNCC.removeAllItems();
        cmbNCC.setEnabled(false);
        cmbSanPham.removeAllItems();
        cmbSanPham.setEnabled(false);

        btnThemSP.setVisible(false);
        btnLuuPhieu.setVisible(false);
        btnHuyPhieu.setVisible(false);

        btnXoa.setVisible(false);
        btnLuuSP.setVisible(false);
        btnHuySP.setVisible(false);

        btnCapNhat.setEnabled(true);
        btnThem.setEnabled(true);
    }
    private void tinhTongTien() {
        try {
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            double giaNhap = parseTien(txtGiaNhap.getText());
            double tongTien = soLuong * giaNhap;
            txtTongTien.setText(formatTien(tongTien));
        } catch (Exception e) {
            txtTongTien.setText("");
        }
    }
    private int timDongSanPham(String maSP) {
        for (int i = 0; i < modelCTPNK.getRowCount(); i++) {
            if (modelCTPNK.getValueAt(i, 1).toString().equals(maSP)) {
                return i;
            }
        }
        return -1;
    }
    private void capNhatTongTienPhieu() {
        double tongPhieu = 0;
        for (int i = 0; i < modelCTPNK.getRowCount(); i++) {
            String value = modelCTPNK.getValueAt(i, 5).toString().replace(",", "");
            tongPhieu += parseTien(value);
        }
        txtThanhTien.setText(formatTien(tongPhieu));
    }
    private boolean kiemTraSanPhamTonTai(String maSP) {
        DefaultTableModel model = (DefaultTableModel) tableCTPNK.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 1).equals(maSP)) {
                return true;
            }
        }
        return false;
    }

    private boolean kiemTraFormThongTin() {
        if (cmbNCC.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp");
            cmbNCC.requestFocus();
            return false;
        }
        if (txtNhanVien.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng đăng nhập");
            return false;
        }

        if (cmbKVLT.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khu vực lưu trữ");
            cmbKVLT.requestFocus();
            return false;
        }
        return true;
    }
    private boolean kiemTraFormChiTiet() {
        if (cmbSanPham.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm");
            cmbSanPham.requestFocus();
            return false;
        }

        if (txtSoLuong.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng");
            txtSoLuong.requestFocus();
            return false;
        }
        int soLuong;
        try {
            soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0");
                txtSoLuong.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số");
            txtSoLuong.requestFocus();
            return false;
        }
        return true;
    }

    private void lockTableDS() {
        tableDSPNK.setEnabled(false);
    }
    private void unlockTableDS() {
        tableDSPNK.setEnabled(true);
    }
    private void lockTableCT() {
        tableCTPNK.setEnabled(false);
    }
    private void unlockTableCT() {
        tableCTPNK.setEnabled(true);
    }

    private void createUIComponents() {
        JDateChooser1 = new JDateChooser();
        JDateChooser1.setDateFormatString("dd/MM/yyyy");

        JDateChooser2 = new JDateChooser();
        JDateChooser2.setDateFormatString("dd/MM/yyyy");

        JDateChooser3 = new JDateChooser();
        JDateChooser3.setDateFormatString("dd/MM/yyyy");

        JDateChooser4 = new JDateChooser();
        JDateChooser4.setDateFormatString("dd/MM/yyyy");
    }

    private String formatTien(double tien){
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi","VN"));
        String kq = nf.format(tien);
        kq = kq.replace("₫", "đ");
        return kq;
    }
    private double parseTien(String tien) {

        if (tien == null || tien.isEmpty()) return 0;
        tien = tien.replaceAll("[^0-9]", "");
        if (tien.isEmpty()) return 0;

        return Double.parseDouble(tien);
    }
}
