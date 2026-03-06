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
    private JButton btnNhapExcel;
    private JButton btnXuatExcel;
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
    private JComboBox cmbTrangThai; // ComboBox lọc trạng thái (theo thiết kế form của bạn)
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
    private JLabel labelLoaiGiam;
    private JComboBox cmbLoaiGiam;
    private JLabel labelLocNgayBatDau;
    private JPanel LocNgayBatDau;
    private JDateChooser jdLocNgayBatDau;
    private JPanel LocNgayKetThuc;
    private JDateChooser jdLocNgayKetThuc;
    private JLabel labelSoLuotSuDung;
    private JTextField txtSoLuotSuDung;
    private DefaultTableModel modelVoucher;
    private JPopupMenu popupGoiY = new JPopupMenu();


    // KHAI BÁO BUS VÀ ĐỊNH DẠNG
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

        // Đổ dữ liệu lên bảng
        loadDataToTable_Voucher(vBUS.getAll());

        // Gắn sự kiện
        addEvents();
        setViewMode();
    }

    private void initTable_Voucher() {
        String[] headers = {"STT", "Mã Voucher", "Tên Voucher", "Loại Voucher", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Trạng Thái"};
        modelVoucher = new DefaultTableModel(headers, 0);
        tableVoucher.setModel(modelVoucher);
    }

    private void initComboBox_Voucher() {
        // 1. Nạp dữ liệu cho ô Loại Voucher (Chi tiết)
        if (cmbLoaiVoucher != null) {
            cmbLoaiVoucher.removeAllItems();
            cmbLoaiVoucher.addItem("Phần trăm");
            cmbLoaiVoucher.addItem("Tiền mặt");
        }

        // 2. Nạp dữ liệu cho ô Trạng thái (Chi tiết - Ô đang bị lỗi của bạn)
        if (cmbTrangThai != null) {
            cmbTrangThai.removeAllItems();
            cmbTrangThai.addItem("Đang áp dụng");
            cmbTrangThai.addItem("Ngưng áp dụng");
        }

        // 3. Nạp dữ liệu cho ô comboBox1 (Ô lọc Trạng thái ở phía trên)
        if (cmbLocTrangThai != null) {
            cmbLocTrangThai.removeAllItems();
            cmbLocTrangThai.addItem("Tất cả");
            cmbLocTrangThai.addItem("Đang áp dụng");
            cmbLocTrangThai.addItem("Ngưng áp dụng");
        }
    }

    // ==============================================================
    // HÀM ĐỔ DỮ LIỆU LÊN BẢNG
    // ==============================================================
    // Sửa: Thêm tham số ArrayList<Voucher_DTO> list vào đây
    private void loadDataToTable_Voucher(ArrayList<Voucher_DTO> list) {
        modelVoucher.setRowCount(0);
        // Không gọi vBUS.getAll() ở đây nữa, mà dùng trực tiếp tham số 'list' truyền vào
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

    // ==============================================================
    // HÀM BẮT SỰ KIỆN CLICK CHUỘT
    // ==============================================================

    private void addEvents() {
        tableVoucher.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Không fill lại data khi đang chế độ Thêm hoặc Sửa để tránh mất dữ liệu đang nhập
                if (isAdding || isUpdating) return;

                int row = tableVoucher.getSelectedRow();
                if (row >= 0) {
                    fillDataFromTable(row);
                }
            }
        });

        // Các sự kiện nút bấm
        btnThem.addActionListener(e -> setAddMode());
        btnCapNhat.addActionListener(e -> setUpdateMode());
        btnLuu.addActionListener(e -> saveVoucher());
        btnHuy.addActionListener(e -> { setViewMode(); clearForm(); });

        // Sự kiện lọc (Thêm vào nếu chưa có)
        ActionListener locAction = e -> thucHienLoc();
        cmbLocTrangThai.addActionListener(locAction);
        cmbTimTheo.addActionListener(locAction);
    }

    // ==============================================================
    // HÀM ĐỔ DỮ LIỆU LÊN FORM CHI TIẾT (Đã sửa lỗi hiển thị)
    // ==============================================================
    private void fillDataFromTable(int row) {
        String ma = modelVoucher.getValueAt(row, 1).toString();
        Voucher_DTO v = vBUS.getById(ma);
        if (v == null) return;

        // 1. Đổ dữ liệu text cơ bản
        txtMaVoucher.setText(v.getMa());
        txtTenVoucher.setText(v.getTen());
        txtGiaTriVoucher.setText(String.valueOf(v.getGiaTriVoucher()));
        txtDonToiThieu.setText(String.valueOf((long)v.getDonToiThieu()));

        // 2. FIX LỖI HIỂN THỊ NGÀY: Đổ vào JDateChooser
        if (v.getNgayBatDau() != null) jdNgayBatDau.setDate(v.getNgayBatDau());
        if (v.getNgayKetThuc() != null) jdNgayKetThuc.setDate(v.getNgayKetThuc());

        // 3. Đổ ComboBox dựa trên Text từ DTO
        cmbLoaiVoucher.setSelectedItem(v.getLoaiVoucherText());

        // Xử lý Trạng thái: Nếu DTO trả về "Chưa diễn ra" -> Chọn "Đang áp dụng" trên Combo
        String ttText = v.getTrangThaiText();
        if (ttText.equals("Chưa diễn ra")) {
            cmbTrangThai.setSelectedItem(Voucher_DTO.DANG_AP_DUNG);
        } else {
            cmbTrangThai.setSelectedItem(ttText);
        }

        // 4. HIỂN THỊ SỐ LƯỢT SỬ DỤNG
        // Vì Voucher này phát cho tất cả KH cùng 1 mức tối đa,
        // ta lấy đại diện 1 khách hàng hoặc viết hàm lấy SoLuongToiDa trong Voucher_DAO
        // Ở đây tôi giả định bạn lấy từ bảng trung gian thông qua BUS đã viết:
        int soLuot = bus.KhachHang_Voucher_BUS.getInstance().getSoLuotConLai(v.getMa(), "KH000001");
        // Lưu ý: Bạn nên viết thêm hàm getSoLuotToiDa(maVoucher) trong Voucher_DAO để lấy chính xác hơn.
        txtSoLuotSuDung.setText(String.valueOf(soLuot));

        // Khóa ô mã
        txtMaVoucher.setEditable(false);
    }
    private void createUIComponents() {
        // Khởi tạo 4 bộ chọn ngày
        jdLocNgayBatDau = new JDateChooser();
        jdLocNgayKetThuc = new JDateChooser();
        jdNgayBatDau = new JDateChooser();
        jdNgayKetThuc = new JDateChooser();

        // Định dạng chung
        String format = "dd/MM/yyyy";
        jdLocNgayBatDau.setDateFormatString(format);
        jdLocNgayKetThuc.setDateFormatString(format);
        jdNgayBatDau.setDateFormatString(format);
        jdNgayKetThuc.setDateFormatString(format);

        // Gắn vào các Panel (Nhớ tích chọn "Custom Create" trong .form cho 4 JPanel này)
        LocNgayBatDau = new JPanel(new BorderLayout());
        LocNgayBatDau.add(jdLocNgayBatDau);

        LocNgayKetThuc = new JPanel(new BorderLayout());
        LocNgayKetThuc.add(jdLocNgayKetThuc);

        panelNgayBatDau = new JPanel(new BorderLayout());
        panelNgayBatDau.add(jdNgayBatDau);

        panelNgayKetThuc = new JPanel(new BorderLayout());
        panelNgayKetThuc.add(jdNgayKetThuc);
    }
    private void thucHienLoc() {
        String keyword = txtNhapThongTin.getText();
        String timTheo = (String) cmbTimTheo.getSelectedItem();
        String locTrangThai = (String) cmbLocTrangThai.getSelectedItem();

        // Gọi 1 dòng duy nhất từ BUS
        ArrayList<Voucher_DTO> dsLoc = vBUS.timKiemNangCao(keyword, timTheo, locTrangThai);

        loadDataToTable_Voucher(dsLoc);
    }

    private void saveVoucher() {
        try {
            if (!validateForm()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            // 1. Thu thập dữ liệu từ GUI
            Voucher_DTO v = new Voucher_DTO();
            v.setMa(txtMaVoucher.getText());
            v.setTen(txtTenVoucher.getText());
            v.setLoaiVoucher(cmbLoaiVoucher.getSelectedIndex());
            v.setGiaTriVoucher(Double.parseDouble(txtGiaTriVoucher.getText().trim()));
            v.setDonToiThieu(Double.parseDouble(txtDonToiThieu.getText().trim()));
            v.setNgayBatDau(new java.sql.Date(jdNgayBatDau.getDate().getTime()));
            v.setNgayKetThuc(new java.sql.Date(jdNgayKetThuc.getDate().getTime()));

            // Gán trạng thái thông qua hàm parse của DTO
            if (!v.setTrangThaiFromText((String) cmbTrangThai.getSelectedItem())) {
                JOptionPane.showMessageDialog(this, "Không thể kích hoạt Voucher đã hết hạn!");
                return;
            }

            // 2. Gọi BUS xử lý
            boolean success;
            if (isAdding) {
                int soLuot = Integer.parseInt(txtSoLuotSuDung.getText().trim());
                success = vBUS.them(v, soLuot); // Logic phân phối khách hàng nằm trong này
            } else {
                success = vBUS.capNhat(v);
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Lưu thành công!");
                loadDataToTable_Voucher(vBUS.getAll());
                setViewMode();
                clearForm();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!");
        }
    }

    private void setViewMode() {
        isAdding = isUpdating = false;
        txtTenVoucher.setEditable(false); txtGiaTriVoucher.setEditable(false);
        txtDonToiThieu.setEditable(false); txtSoLuotSuDung.setEditable(false);
        jdNgayBatDau.setEnabled(false); jdNgayKetThuc.setEnabled(false);
        cmbLoaiVoucher.setEnabled(false); cmbTrangThai.setEnabled(false);
        btnLuu.setVisible(false); btnHuy.setVisible(false);
        btnThem.setEnabled(true); btnCapNhat.setEnabled(true);
        tableVoucher.setEnabled(true);
    }

    private void setAddMode() {
        isAdding = true; isUpdating = false;
        clearForm();
        txtTenVoucher.setEditable(true); txtGiaTriVoucher.setEditable(true);
        txtDonToiThieu.setEditable(true); txtSoLuotSuDung.setEditable(true);
        jdNgayBatDau.setEnabled(true); jdNgayKetThuc.setEnabled(true);
        cmbLoaiVoucher.setEnabled(true); cmbTrangThai.setEnabled(true);
        txtMaVoucher.setText(vBUS.getNextId());
        txtMaVoucher.setEditable(false);
        cmbTrangThai.setEnabled(false);
        btnLuu.setVisible(true); btnHuy.setVisible(true);
        btnThem.setEnabled(false); btnCapNhat.setEnabled(false);
        tableVoucher.setEnabled(false);
    }

    private void setUpdateMode() {
        if (tableVoucher.getSelectedRow() < 0) return;
        isAdding = false; isUpdating = true;
        txtTenVoucher.setEditable(true); txtGiaTriVoucher.setEditable(true);
        txtDonToiThieu.setEditable(true); txtSoLuotSuDung.setEditable(true);
        jdNgayBatDau.setEnabled(true); jdNgayKetThuc.setEnabled(true);
        cmbLoaiVoucher.setEnabled(true); cmbTrangThai.setEnabled(true);
        txtMaVoucher.setEditable(false);
        txtSoLuotSuDung.setEditable(false); // Không cho sửa lượt sau khi đã phát
        btnLuu.setVisible(true); btnHuy.setVisible(true);
        btnThem.setEnabled(false); btnCapNhat.setEnabled(false);
        tableVoucher.setEnabled(false);
    }
    private void clearForm() {
        txtMaVoucher.setText("");
        txtTenVoucher.setText("");
        txtGiaTriVoucher.setText("");
        txtDonToiThieu.setText("");
        txtSoLuotSuDung.setText("1");
        jdNgayBatDau.setDate(null);
        jdNgayKetThuc.setDate(null);
    }
    private boolean validateForm() {
        if (txtTenVoucher.getText().isEmpty()) return false;
        if (jdNgayBatDau.getDate() == null || jdNgayKetThuc.getDate() == null) return false;
        if (jdNgayBatDau.getDate().after(jdNgayKetThuc.getDate())) return false;
        return true;
    }
}