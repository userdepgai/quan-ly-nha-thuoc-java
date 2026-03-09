package gui;

import bus.Voucher_BUS;
import com.toedter.calendar.JDateChooser;
import dto.Voucher_DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class QuanLyVoucher_GUI extends JPanel {
    private JLabel label_tieuDe;
    private JPanel panelBoLoc;
    private JButton btnCapNhat;
    private JButton btnThem;
    private JTextField txtMaVoucher;
    private JTextField txtTenVoucher;
    private JPanel panelQuanLyVoucher;
    private JTable tableVoucher;
    private JLabel labelMaVoucher;
    private JLabel labelTenVoucher;
    private JComboBox<String> cmbLoaiVoucher;
    private JLabel labelLoaiVoucher;
    private JLabel labelGiaTriVoucher;
    private JTextField txtGiaTriVoucher;
    private JLabel labelNgayBatDau;
    private JTextField txtNgayBatDau;
    private JTextField txtNgayKetThuc;
    private JLabel labelNgayKetThuc;
    private JComboBox cmbTrangThai;
    private JLabel labelTimKiem;
    private JComboBox cmbTimTheo;
    private JLabel labelLocTrangThai;
    private JComboBox<String> cmbLocTrangThai;
    private JPanel panelDanhSachVoucher;
    private JPanel panelTieuDe;
    private JPanel panelCapNhat;
    private JPanel panelThongTinChiTiet;
    private JButton btnThoat;
    private JButton btnTimkiem;
    private JTextField txtNhapThongTin;
    private JLabel labelNhapThongTin;
    private JTextField txtVoucherHienCo;
    private JLabel labelVoucherHienCo;
    private JTextField txtDonToiThieu;
    private JLabel labelDonToiThieu;
    private JLabel labelTrangThai;
    private JPanel panelNgayBatDau;
    private JDateChooser jdNgayBatDau;
    private JPanel panelNgayKetThuc;
    private JDateChooser jdNgayKetThuc;
    private JButton btnHuy;
    private JButton btnLuu;
    private JLabel labelLocLoaiVoucher;
    private JComboBox cmbLocLoaiVoucher;
    private JLabel labelLocNgayBatDau;
    private JPanel LocNgayBatDau;
    private JDateChooser jdLocNgayBatDau;
    private JPanel LocNgayKetThuc;
    private JDateChooser jdLocNgayKetThuc;
    private JLabel labelSoLuotSuDung;
    private JTextField txtSoLuotSuDung;
    private DefaultTableModel modelVoucher;
    private JPopupMenu popupGoiY = new JPopupMenu();


    private final Voucher_BUS vBUS = Voucher_BUS.getInstance();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private final DecimalFormat df = new DecimalFormat("#,###");
    private boolean isAdding = false;
    private boolean isUpdating = false;

    public QuanLyVoucher_GUI(){
        this.setLayout(new BorderLayout());
        if (panelQuanLyVoucher != null) {
            this.add(panelQuanLyVoucher, BorderLayout.CENTER);
        }

        initTable_Voucher();
        initComboBox_Voucher();

        loadDataToTable_Voucher(vBUS.getAll());

        addEvents();
        setViewMode();
    }

    private void initTable_Voucher() {
        String[] headers = {"STT", "Mã Voucher", "Tên Voucher", "Loại Voucher", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Trạng Thái"};
        modelVoucher = new DefaultTableModel(headers, 0);
        tableVoucher.setModel(modelVoucher);
    }

    private void initComboBox_Voucher() {
        if (cmbLoaiVoucher != null) {
            cmbLoaiVoucher.removeAllItems();
            cmbLoaiVoucher.addItem(Voucher_DTO.PHAN_TRAM);
            cmbLoaiVoucher.addItem(Voucher_DTO.TIEN_MAT);
        }

        if (cmbTrangThai != null) {
            cmbTrangThai.removeAllItems();
            cmbTrangThai.addItem(Voucher_DTO.DANG_AP_DUNG);
            cmbTrangThai.addItem(Voucher_DTO.NGUNG_AP_DUNG);
        }

        if (cmbTimTheo != null) {
            cmbTimTheo.removeAllItems();
            cmbTimTheo.addItem("Tất cả");
            cmbTimTheo.addItem("Mã Voucher");
            cmbTimTheo.addItem("Tên Voucher");
        }

        if (cmbLocTrangThai != null) {
            cmbLocTrangThai.removeAllItems();
            cmbLocTrangThai.addItem("Tất cả");
            cmbLocTrangThai.addItem(Voucher_DTO.DANG_AP_DUNG);
            cmbLocTrangThai.addItem(Voucher_DTO.NGUNG_AP_DUNG);
            cmbLocTrangThai.addItem("Chưa diễn ra");
        }

        if (cmbLocLoaiVoucher != null) {
            cmbLocLoaiVoucher.removeAllItems();
            cmbLocLoaiVoucher.addItem("Tất cả");
            cmbLocLoaiVoucher.addItem(Voucher_DTO.PHAN_TRAM);
            cmbLocLoaiVoucher.addItem(Voucher_DTO.TIEN_MAT);
        }
    }

    private void loadDataToTable_Voucher(ArrayList<Voucher_DTO> list) {
        modelVoucher.setRowCount(0);
        int stt = 1;
        for (Voucher_DTO v : list) {
            String loaiStr = v.getLoaiVoucherText();
            String trangThaiStr = v.getTrangThaiText();

            modelVoucher.addRow(new Object[]{
                    stt++,
                    v.getMa(),
                    v.getTen(),
                    loaiStr,
                    (v.getNgayBatDau() != null) ? sdf.format(v.getNgayBatDau()) : "",
                    (v.getNgayKetThuc() != null) ? sdf.format(v.getNgayKetThuc()) : "",
                    trangThaiStr
            });
        }

        if (txtVoucherHienCo != null) {
            txtVoucherHienCo.setText(String.valueOf(list.size()));
        }
    }


    private void addEvents() {
        tableVoucher.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isAdding || isUpdating) return;

                int row = tableVoucher.getSelectedRow();
                if (row >= 0) {
                    fillDataFromTable(row);
                }
            }
        });

        btnThem.addActionListener(e -> setAddMode());
        btnCapNhat.addActionListener(e -> {
            if (tableVoucher.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn Voucher muốn cập nhật từ danh sách!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            setUpdateMode();
        });
        btnLuu.addActionListener(e -> saveVoucher());
        btnHuy.addActionListener(e -> { setViewMode(); clearForm(); });

        txtNhapThongTin.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() != java.awt.event.KeyEvent.VK_UP &&
                        e.getKeyCode() != java.awt.event.KeyEvent.VK_DOWN &&
                        e.getKeyCode() != java.awt.event.KeyEvent.VK_ENTER) {

                    thucHienLoc();
                    hienThiGoiY(vBUS.timKiemNangCao(txtNhapThongTin.getText(),
                            (String)cmbTimTheo.getSelectedItem(), "Tất cả", "Tất cả", null, null));
                }
            }
        });

        jdLocNgayBatDau.addPropertyChangeListener("date", evt -> {
            java.util.Date bd = jdLocNgayBatDau.getDate();
            java.util.Date kt = jdLocNgayKetThuc.getDate();

            if (!vBUS.kiemTraLogicLocNgay(bd, kt, true)) {
                jdLocNgayBatDau.setDate(null);
            } else {
                loadDataToTable_Voucher(thucHienLoc());
            }
        });

        jdLocNgayKetThuc.addPropertyChangeListener("date", evt -> {
            java.util.Date bd = jdLocNgayBatDau.getDate();
            java.util.Date kt = jdLocNgayKetThuc.getDate();

            if (!vBUS.kiemTraLogicLocNgay(bd, kt, false)) {
                jdLocNgayKetThuc.setDate(null);
            } else {
                loadDataToTable_Voucher(thucHienLoc());
            }
        });

        ActionListener locAction = e -> thucHienLoc();
        cmbTimTheo.addActionListener(locAction);
        cmbLocTrangThai.addActionListener(locAction);
        if (cmbLocLoaiVoucher != null) cmbLocLoaiVoucher.addActionListener(locAction);

        btnTimkiem.addActionListener(e -> thucHienLoc());

        btnThoat.addActionListener(e -> {
            txtNhapThongTin.setText("");
            cmbTimTheo.setSelectedIndex(0);
            cmbLocTrangThai.setSelectedIndex(0);
            jdLocNgayBatDau.setDate(null);
            jdLocNgayKetThuc.setDate(null);
            loadDataToTable_Voucher(vBUS.getAll());
        });
    }

    private void hienThiGoiY(ArrayList<Voucher_DTO> list) {
        popupGoiY.setVisible(false);
        popupGoiY.removeAll();
        if (list.isEmpty() || txtNhapThongTin.getText().trim().isEmpty()) return;

        String timTheo = (String) cmbTimTheo.getSelectedItem();

        for (int i = 0; i < Math.min(list.size(), 5); i++) {
            Voucher_DTO v = list.get(i);
            JMenuItem item = new JMenuItem(v.getMa() + " - " + v.getTen());

            item.addActionListener(e -> {
                if (timTheo.equals("Tên Voucher")) {
                    txtNhapThongTin.setText(v.getTen());
                } else {
                    txtNhapThongTin.setText(v.getMa());
                }

                thucHienLoc();
                popupGoiY.setVisible(false);
            });
            popupGoiY.add(item);
        }
        popupGoiY.show(txtNhapThongTin, 0, txtNhapThongTin.getHeight());
        txtNhapThongTin.requestFocus();
    }

    private void fillDataFromTable(int row) {
        String ma = modelVoucher.getValueAt(row, 1).toString();
        Voucher_DTO v = vBUS.getById(ma);
        if (v == null) return;

        txtMaVoucher.setText(v.getMa());
        txtTenVoucher.setText(v.getTen());
        txtGiaTriVoucher.setText(String.valueOf(v.getGiaTriVoucher()));
        txtDonToiThieu.setText(String.valueOf((long)v.getDonToiThieu()));

        if (v.getNgayBatDau() != null) jdNgayBatDau.setDate(v.getNgayBatDau());
        if (v.getNgayKetThuc() != null) jdNgayKetThuc.setDate(v.getNgayKetThuc());

        cmbLoaiVoucher.setSelectedItem(v.getLoaiVoucherText());

        String ttText = v.getTrangThaiText();
        if (ttText.equals("Chưa diễn ra")) {
            cmbTrangThai.setSelectedItem(Voucher_DTO.DANG_AP_DUNG);
        } else {
            cmbTrangThai.setSelectedItem(ttText);
        }

        int soLuotMax = bus.KhachHang_Voucher_BUS.getInstance().getSoLuotToiDa(v.getMa(), "KH000001");
        txtSoLuotSuDung.setText(String.valueOf(soLuotMax));

        txtMaVoucher.setEditable(false);
    }
    private void createUIComponents() {
        jdLocNgayBatDau = new JDateChooser();
        jdLocNgayKetThuc = new JDateChooser();
        jdNgayBatDau = new JDateChooser();
        jdNgayKetThuc = new JDateChooser();

        String format = "dd/MM/yyyy";
        jdLocNgayBatDau.setDateFormatString(format);
        jdLocNgayKetThuc.setDateFormatString(format);
        jdNgayBatDau.setDateFormatString(format);
        jdNgayKetThuc.setDateFormatString(format);

        LocNgayBatDau = new JPanel(new BorderLayout());
        LocNgayBatDau.add(jdLocNgayBatDau);

        LocNgayKetThuc = new JPanel(new BorderLayout());
        LocNgayKetThuc.add(jdLocNgayKetThuc);

        panelNgayBatDau = new JPanel(new BorderLayout());
        panelNgayBatDau.add(jdNgayBatDau);

        panelNgayKetThuc = new JPanel(new BorderLayout());
        panelNgayKetThuc.add(jdNgayKetThuc);
    }

    private ArrayList<Voucher_DTO> thucHienLoc() {
        String keyword = txtNhapThongTin.getText();
        String timTheo = (String) cmbTimTheo.getSelectedItem();
        String locTrangThai = (String) cmbLocTrangThai.getSelectedItem();

        String locLoai = (cmbLocLoaiVoucher != null) ? (String) cmbLocLoaiVoucher.getSelectedItem() : "Tất cả";

        java.util.Date filterBD = jdLocNgayBatDau.getDate();
        java.util.Date filterKT = jdLocNgayKetThuc.getDate();

        ArrayList<Voucher_DTO> dsLoc = vBUS.timKiemNangCao(keyword, timTheo, locLoai, locTrangThai, filterBD, filterKT);
        loadDataToTable_Voucher(dsLoc);
        return dsLoc;
    }

    private void saveVoucher() {
        try {
            if (!validateForm()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ các thông tin bắt buộc!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Voucher_DTO v = new Voucher_DTO();
            v.setMa(txtMaVoucher.getText().trim());
            v.setTen(txtTenVoucher.getText().trim());
            v.setLoaiVoucher(cmbLoaiVoucher.getSelectedIndex());
            v.setGiaTriVoucher(Double.parseDouble(txtGiaTriVoucher.getText().trim()));
            v.setDonToiThieu(Double.parseDouble(txtDonToiThieu.getText().trim()));

            if (jdNgayBatDau.getDate() != null)
                v.setNgayBatDau(new java.sql.Date(jdNgayBatDau.getDate().getTime()));
            if (jdNgayKetThuc.getDate() != null)
                v.setNgayKetThuc(new java.sql.Date(jdNgayKetThuc.getDate().getTime()));

            String statusSelected = (String) cmbTrangThai.getSelectedItem();
            if (!v.setTrangThaiFromText(statusSelected)) {
                JOptionPane.showMessageDialog(this, "Không thể kích hoạt Voucher vì ngày kết thúc đã qua!", "Lỗi trạng thái", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success;
            if (isAdding) {
                int soLuot = Integer.parseInt(txtSoLuotSuDung.getText().trim());
                success = vBUS.them(v, soLuot);
            } else {
                success = vBUS.capNhat(v);
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Lưu dữ liệu thành công!");
                loadDataToTable_Voucher(vBUS.getAll());
                setViewMode();
                clearForm();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá trị voucher, Đơn tối thiểu hoặc Số lượt phải là số hợp lệ!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    private void setViewMode() {
        isAdding = isUpdating = false;
        txtTenVoucher.setEditable(false);
        txtGiaTriVoucher.setEditable(false);
        txtDonToiThieu.setEditable(false);
        txtSoLuotSuDung.setEditable(false);
        jdNgayBatDau.setEnabled(false);
        jdNgayKetThuc.setEnabled(false);
        cmbLoaiVoucher.setEnabled(false);
        cmbTrangThai.setEnabled(false);

        btnLuu.setVisible(false);
        btnHuy.setVisible(false);
        btnThem.setEnabled(true);
        btnCapNhat.setEnabled(true);
        tableVoucher.setEnabled(true);
    }

    private void setAddMode() {
        isAdding = true;
        isUpdating = false;
        clearForm();

        txtTenVoucher.setEditable(true);
        txtGiaTriVoucher.setEditable(true);
        txtDonToiThieu.setEditable(true);

        txtSoLuotSuDung.setEditable(true);
        txtSoLuotSuDung.setText("1");

        jdNgayBatDau.setEnabled(true);
        jdNgayKetThuc.setEnabled(true);
        cmbLoaiVoucher.setEnabled(true);
        cmbTrangThai.setEnabled(true);
        txtMaVoucher.setText(vBUS.getNextId());
        txtMaVoucher.setEditable(false);

        btnLuu.setVisible(true);
        btnHuy.setVisible(true);
        btnThem.setEnabled(false);
        btnCapNhat.setEnabled(false);
        tableVoucher.setEnabled(false);
    }

    private void setUpdateMode() {
        if (tableVoucher.getSelectedRow() < 0) return;
        isAdding = false;
        isUpdating = true;

        txtTenVoucher.setEditable(true);
        txtGiaTriVoucher.setEditable(true);
        txtDonToiThieu.setEditable(true);
        txtSoLuotSuDung.setEditable(false);
        jdNgayBatDau.setEnabled(true);
        jdNgayKetThuc.setEnabled(true);
        cmbLoaiVoucher.setEnabled(true);
        cmbTrangThai.setEnabled(true);
        txtMaVoucher.setEditable(false);

        btnLuu.setVisible(true);
        btnHuy.setVisible(true);
        btnThem.setEnabled(false);
        btnCapNhat.setEnabled(false);
        tableVoucher.setEnabled(false);
    }
    private void clearForm() {
        txtMaVoucher.setText("");
        txtTenVoucher.setText("");
        txtGiaTriVoucher.setText("");
        txtDonToiThieu.setText("");
        txtSoLuotSuDung.setText("");
        jdNgayBatDau.setDate(null);
        jdNgayKetThuc.setDate(null);
    }
    private boolean validateForm() {
        return !txtTenVoucher.getText().trim().isEmpty() &&
                !txtGiaTriVoucher.getText().trim().isEmpty() &&
                !txtDonToiThieu.getText().trim().isEmpty() &&
                jdNgayBatDau.getDate() != null &&
                jdNgayKetThuc.getDate() != null &&
                (isAdding ? !txtSoLuotSuDung.getText().trim().isEmpty() : true);
    }
}