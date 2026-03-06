package gui;

import bus.ChuongTrinhKM_BUS;
import bus.KhuyenMai_BUS;
import com.toedter.calendar.JDateChooser;
import dto.ChuongTrinhKM_DTO;
import dto.KhuyenMai_DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class QuanLyChuongTrinhKM_GUI extends JPanel {
    private JPanel panelTieuDe;
    private JLabel label_tieuDe;
    private JButton btnNhapExcel;
    private JButton btnXuatExcel;
    private JPanel panelDanhSachChuongTrinhKhuyenMai;
    private JTable tableChuongTrinhKhuyenMai;
    private JPanel panelThongTinChiTiet;
    private JPanel panelChuongTrinhKhuyenMai;
    private JLabel labelMaChuongTrinhKhuyenMai;
    private JLabel labelTenChuongTrinhKhuyenMai;
    private JLabel labelTrangThai;
    private JTextField txtMaChuongTrinhKhuyenMai;
    private JTextField txtTenChuongTrinhKhuyenMai;
    private JComboBox<String> cmbTrangThai;
    private JPanel panelDanhSachKhuyenMai;
    private JTable tableKhuyenMai;
    private JPanel panelCapNhat;
    private JButton btnCapNhat;
    private JButton btnThem;
    private JPanel panelBoLoc;
    private JLabel labelTimTheo;
    private JComboBox<String> cmbTimTheo;
    private JComboBox<String> cmbLocTrangThai;
    private JPanel panelQuanLyChuongTrinhKhuyenMai;
    private JLabel labelNgayBatDau;
    private JLabel labelNgayKetThuc;
    private JButton btnThoat;
    private JButton btnTimKiem;
    private JTextField txtNhapThongTin;
    private JLabel labelNhapThongTin;
    private JLabel labelChuongTrinhKhuyenMaiHienCo;
    private JTextField txtChuongTrinhKhuyenMaiHienCo;

    // --- SỬ DỤNG JTextField CHO NGÀY THÁNG ---
    private JTextField txtNgayBatDau;
    private JTextField txtNgayKetThuc;
    private JLabel labelMoTa;
    private JTextField txtMoTa;
    private JLabel labelLocNgayKetThuc;
    private JLabel labelLocNgayBatDau;
    private JLabel labelLocTrangThai;
    private JDateChooser jdLocNgayBatDau;
    private JPanel LocNgayBatDau;
    private JPanel LocNgayKetThuc;
    private JDateChooser jdLocNgayKetThuc;
    private JDateChooser jdNgayKetThuc;
    private JDateChooser jdNgayBatDau;
    private JPanel panelNgayBatDau;
    private JPanel panelNgayKetThuc;
    private JButton btnHuy;
    private JButton btnLuu;
    // ------------------------------------------

    private DefaultTableModel modelChuongTrinhKhuyenMai;
    private DefaultTableModel modelKhuyenMai;
    private JPopupMenu popupGoiY = new JPopupMenu();

    // KHAI BÁO BUS
    private final ChuongTrinhKM_BUS ctkmBUS = ChuongTrinhKM_BUS.getInstance();
    private final KhuyenMai_BUS kmBUS = KhuyenMai_BUS.getInstance();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private boolean isAdding = false;
    private boolean isUpdating = false;

    public QuanLyChuongTrinhKM_GUI(){
        this.setLayout(new BorderLayout());
        if (panelQuanLyChuongTrinhKhuyenMai != null) {
            this.add(panelQuanLyChuongTrinhKhuyenMai, BorderLayout.CENTER);
        }

        initTable_ChuongTrinhKhuyenMai();
        initTable_KhuyenMai();
        initComboBox();

        // Đổ dữ liệu ban đầu lên bảng cha
        loadDataToTable_CTKM(ctkmBUS.getAll());

        // Gắn sự kiện (Click chuột, Tìm kiếm...)
        addEvents();
        setViewMode();
    }

    private void initTable_ChuongTrinhKhuyenMai() {
        String[] headers = {"STT", "Mã Chương Trình", "Tên Chương Trình", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Trạng Thái"};
        modelChuongTrinhKhuyenMai = new DefaultTableModel(headers, 0);
        tableChuongTrinhKhuyenMai.setModel(modelChuongTrinhKhuyenMai);
    }

    private void initTable_KhuyenMai() {
        String[] headers = {"STT", "Mã Khuyến Mãi", "Tên Khuyến Mãi", "Loại Khuyến Mãi", "Trạng Thái"};
        modelKhuyenMai = new DefaultTableModel(headers, 0);
        tableKhuyenMai.setModel(modelKhuyenMai);
    }

    private void initComboBox() {
        // ComboBox nhập liệu: Chỉ có 2 trạng thái cơ bản để Admin chọn "Chủ đích"
        cmbTrangThai.removeAllItems();
        cmbTrangThai.addItem(ChuongTrinhKM_DTO.DANG_AP_DUNG);
        cmbTrangThai.addItem(ChuongTrinhKM_DTO.NGUNG_AP_DUNG);

        // Khởi tạo ComboBox Tìm theo
        if (cmbTimTheo != null) {
            cmbTimTheo.setModel(new DefaultComboBoxModel<>(new String[]{
                    "Tất cả", "Mã chương trình", "Tên chương trình"
            }));
        }

        // Cấu hình ComboBox bộ lọc Trạng thái
        if (cmbLocTrangThai != null) {
            cmbLocTrangThai.removeAllItems();
            cmbLocTrangThai.addItem("Tất cả");
            cmbLocTrangThai.addItem(ChuongTrinhKM_DTO.DANG_AP_DUNG);
            cmbLocTrangThai.addItem(ChuongTrinhKM_DTO.NGUNG_AP_DUNG);
            cmbLocTrangThai.addItem("Chưa diễn ra");
        }
    }

    // ==========================================================
    // HÀM 1: ĐỔ DỮ LIỆU LÊN BẢNG CHƯƠNG TRÌNH (BẢNG TRÊN)
    // ==========================================================
    private void loadDataToTable_CTKM(ArrayList<ChuongTrinhKM_DTO> list) {
        modelChuongTrinhKhuyenMai.setRowCount(0);
        int stt = 1;
        for (ChuongTrinhKM_DTO ct : list) {
            modelChuongTrinhKhuyenMai.addRow(new Object[]{
                    stt++,
                    ct.getMa(),
                    ct.getTen(),
                    (ct.getNgayBatDau() != null) ? sdf.format(ct.getNgayBatDau()) : "",
                    (ct.getNgayKetThuc() != null) ? sdf.format(ct.getNgayKetThuc()) : "",
                    ct.getTrangThaiText()
            });
        }
        if (txtChuongTrinhKhuyenMaiHienCo != null) {
            txtChuongTrinhKhuyenMaiHienCo.setText(String.valueOf(list.size()));
        }
    }

    // ==========================================================
    // HÀM 2: ĐỔ DỮ LIỆU LÊN BẢNG CHI TIẾT KHUYẾN MÃI (BẢNG DƯỚI)
    // ==========================================================
    private void loadDataToTable_KhuyenMai(String maCTKM) {
        modelKhuyenMai.setRowCount(0);
        ArrayList<KhuyenMai_DTO> list = kmBUS.getByMaCTKM(maCTKM);

        int stt = 1;
        for (KhuyenMai_DTO km : list) {
            String trangThai = (km.getTrangThai() == 1) ? "Đang áp dụng" : "Ngưng áp dụng";
            // Dịch thuật: 0 là Phần trăm, 1 là Tiền mặt (dựa theo logic INT đã sửa ở SQL trước đó)
            String loaiKMStr = (km.getLoaiKhuyenMai() == 0) ? "Phần trăm" : "Tiền mặt";

            modelKhuyenMai.addRow(new Object[]{
                    stt++,
                    km.getMaKM(),
                    km.getTenKM(),
                    loaiKMStr,
                    trangThai
            });
        }
    }

    private void addEvents() {
        // Sự kiện click bảng Chương Trình KM
        tableChuongTrinhKhuyenMai.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableChuongTrinhKhuyenMai.getSelectedRow();
                if (row >= 0) {
                    // 1. Lấy mã chương trình từ cột số 1 của bảng
                    String maCT = modelChuongTrinhKhuyenMai.getValueAt(row, 1).toString();

                    // 2. Gọi BUS để lấy đối tượng DTO đầy đủ (vì bảng không chứa Mô tả)
                    ChuongTrinhKM_DTO ctkmFull = ctkmBUS.getById(maCT);

                    if (ctkmFull != null) {
                        // 3. Đổ dữ liệu lên các ô Textfield & ComboBox chi tiết
                        if(txtMaChuongTrinhKhuyenMai != null) txtMaChuongTrinhKhuyenMai.setText(ctkmFull.getMa());
                        if(txtTenChuongTrinhKhuyenMai != null) txtTenChuongTrinhKhuyenMai.setText(ctkmFull.getTen());

                        // --- ĐỔ DỮ LIỆU MÔ TẢ ---
                        if(txtMoTa != null) txtMoTa.setText(ctkmFull.getMoTa());

                        jdNgayBatDau.setDate(ctkmFull.getNgayBatDau());
                        jdNgayKetThuc.setDate(ctkmFull.getNgayKetThuc());

                        String smartStatus = ctkmFull.getTrangThaiText();

                        // Nếu chương trình hết hạn hoặc Admin đã tắt -> hiện Ngưng áp dụng
                        if (smartStatus.equals("Chưa diễn ra")) {
                            cmbTrangThai.setSelectedItem(ChuongTrinhKM_DTO.DANG_AP_DUNG);
                        } else {
                            cmbTrangThai.setSelectedItem(smartStatus);
                        }


                        // 4. Load bảng con khuyến mãi chi tiết
                        loadDataToTable_KhuyenMai(maCT);

                        // Khóa ô mã không cho sửa khi đang chọn một dòng có sẵn
                        if(txtMaChuongTrinhKhuyenMai != null) txtMaChuongTrinhKhuyenMai.setEditable(false);
                    }
                }
            }
        });
        btnThem.addActionListener(e -> setAddMode());
        btnCapNhat.addActionListener(e -> {
            if (tableChuongTrinhKhuyenMai.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần cập nhật");
                return;
            }
            setUpdateMode();
        });
        btnLuu.addActionListener(e -> saveChuongTrinh());

        btnHuy.addActionListener(e -> {
            setViewMode();
            clearForm();
        });

        // 1. Ô nhập liệu (Gợi ý và lọc trực tiếp)
        txtNhapThongTin.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() != java.awt.event.KeyEvent.VK_UP &&
                        e.getKeyCode() != java.awt.event.KeyEvent.VK_DOWN &&
                        e.getKeyCode() != java.awt.event.KeyEvent.VK_ENTER) {
                    ArrayList<ChuongTrinhKM_DTO> result = thucHienLoc();
                    hienThiGoiY(result);
                    loadDataToTable_CTKM(result);
                }
            }
        });

        // 2. Tự động lọc khi đổi ComboBox
        java.awt.event.ActionListener locAction = e -> loadDataToTable_CTKM(thucHienLoc());
        cmbLocTrangThai.addActionListener(locAction);
        cmbTimTheo.addActionListener(locAction);

        // 3. Sự kiện thay đổi Ngày bắt đầu lọc
        jdLocNgayBatDau.addPropertyChangeListener("date", evt -> {
            java.util.Date bd = jdLocNgayBatDau.getDate();
            java.util.Date kt = jdLocNgayKetThuc.getDate();

            if (bd != null && kt != null && kt.before(bd)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được sau ngày kết thúc!", "Lỗi chọn ngày", JOptionPane.WARNING_MESSAGE);
                jdLocNgayBatDau.setDate(null); // Xóa ngày vừa chọn sai
            } else {
                loadDataToTable_CTKM(thucHienLoc());
            }
        });

        // 4. Sự kiện thay đổi Ngày kết thúc lọc
        jdLocNgayKetThuc.addPropertyChangeListener("date", evt -> {
            java.util.Date bd = jdLocNgayBatDau.getDate();
            java.util.Date kt = jdLocNgayKetThuc.getDate();

            if (bd != null && kt != null && kt.before(bd)) {
                JOptionPane.showMessageDialog(this, "Ngày kết thúc không được trước ngày bắt đầu!", "Lỗi chọn ngày", JOptionPane.WARNING_MESSAGE);
                jdLocNgayKetThuc.setDate(null); // Xóa ngày vừa chọn sai
            } else {
                loadDataToTable_CTKM(thucHienLoc());
            }
        });

        // 5. Nút Thoát (Sửa thành Reset bộ lọc)
        btnThoat.addActionListener(e -> {
            cmbTimTheo.setSelectedIndex(0);
            cmbLocTrangThai.setSelectedIndex(0);
            jdLocNgayBatDau.setDate(null);
            jdLocNgayKetThuc.setDate(null);
            txtNhapThongTin.setText("");
            loadDataToTable_CTKM(ctkmBUS.getAll());
        });
    }
    private ArrayList<ChuongTrinhKM_DTO> thucHienLoc() {
        String keyword = txtNhapThongTin.getText().trim().toLowerCase();
        String timTheo = cmbTimTheo.getSelectedItem().toString();
        String locTrangThai = cmbLocTrangThai.getSelectedItem().toString();

        java.util.Date userDateBD = jdLocNgayBatDau.getDate();
        java.util.Date userDateKT = jdLocNgayKetThuc.getDate();

        // Chặn logic ngay tại hàm lọc: Nếu ngày bị ngược, không trả về kết quả nào
        if (userDateBD != null && userDateKT != null && userDateKT.before(userDateBD)) {
            return new ArrayList<>();
        }

        ArrayList<ChuongTrinhKM_DTO> dsFull = ctkmBUS.getAll();
        ArrayList<ChuongTrinhKM_DTO> dsLoc = new ArrayList<>();

        for (ChuongTrinhKM_DTO ct : dsFull) {
            // 1. Lọc theo Từ khóa (Giữ nguyên)
            boolean matchKey = false;
            if (timTheo.equals("Tất cả")) {
                matchKey = ct.getMa().toLowerCase().contains(keyword) || ct.getTen().toLowerCase().contains(keyword);
            } else if (timTheo.equals("Mã chương trình")) {
                matchKey = ct.getMa().toLowerCase().contains(keyword);
            } else {
                matchKey = ct.getTen().toLowerCase().contains(keyword);
            }

            // 2. Lọc theo Trạng thái (Giữ nguyên)
            boolean matchStatus = locTrangThai.equals("Tất cả") || ct.getTrangThaiText().equals(locTrangThai);

            // 3. Lọc theo Thời gian (Overlap)
            boolean matchDate = true;
            java.sql.Date progStart = ct.getNgayBatDau();
            java.sql.Date progEnd = ct.getNgayKetThuc();

            if (userDateBD != null && userDateKT != null) {
                // Logic: Chương trình phải KHÔNG kết thúc trước khi lọc bắt đầu
                // VÀ KHÔNG bắt đầu sau khi lọc kết thúc
                if (progEnd.before(userDateBD) || progStart.after(userDateKT)) {
                    matchDate = false;
                }
            } else if (userDateBD != null) {
                if (progEnd.before(userDateBD)) matchDate = false;
            } else if (userDateKT != null) {
                if (progStart.after(userDateKT)) matchDate = false;
            }

            if (matchKey && matchStatus && matchDate) {
                dsLoc.add(ct);
            }
        }
        return dsLoc;
    }

    private void hienThiGoiY(ArrayList<ChuongTrinhKM_DTO> list) {
        popupGoiY.setVisible(false);
        popupGoiY.removeAll();
        if (list.isEmpty() || txtNhapThongTin.getText().trim().isEmpty()) return;

        int count = 0;
        for (ChuongTrinhKM_DTO ct : list) {
            if (count >= 5) break;

            // Hiển thị nội dung gợi ý tùy theo "Tìm theo"
            String textHienThi = ct.getMa() + " - " + ct.getTen();
            JMenuItem item = new JMenuItem(textHienThi);
            item.setPreferredSize(new Dimension(txtNhapThongTin.getWidth(), 30));

            item.addActionListener(e -> {
                // Nếu tìm theo Mã thì điền Mã, tìm theo Tên thì điền Tên
                if (cmbTimTheo.getSelectedIndex() == 2) { // Tên chương trình
                    txtNhapThongTin.setText(ct.getTen());
                } else {
                    txtNhapThongTin.setText(ct.getMa());
                }
                loadDataToTable_CTKM(thucHienLoc());
                popupGoiY.setVisible(false);
            });
            popupGoiY.add(item);
            count++;
        }
        popupGoiY.show(txtNhapThongTin, 0, txtNhapThongTin.getHeight());
        txtNhapThongTin.requestFocus();
    }

    // ==========================================================
    // HÀM TIỆN ÍCH: CHUYỂN STRING SANG SQL DATE
    // (Dùng để lấy dữ liệu từ txtNgayBatDau khi bấm nút LƯU/THÊM)
    // ==========================================================
    private java.sql.Date chuyenStringSangDate(String ngayStr) {
        try {
            if (ngayStr == null || ngayStr.trim().isEmpty()) return null;
            sdf.setLenient(false); // Kiểm tra tính hợp lệ của ngày (ví dụ 32/01 sẽ lỗi)
            java.util.Date date = sdf.parse(ngayStr);
            return new java.sql.Date(date.getTime());
        } catch (ParseException e) {
            return null;
        }
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
    private void saveChuongTrinh() {
        if (!validateForm()) return;

        ChuongTrinhKM_DTO ct = new ChuongTrinhKM_DTO();
        ct.setMa(txtMaChuongTrinhKhuyenMai.getText());
        ct.setTen(txtTenChuongTrinhKhuyenMai.getText());
        ct.setMoTa(txtMoTa.getText());
        ct.setNgayBatDau(new java.sql.Date(jdNgayBatDau.getDate().getTime()));
        ct.setNgayKetThuc(new java.sql.Date(jdNgayKetThuc.getDate().getTime()));

        String choice = (String) cmbTrangThai.getSelectedItem();
        boolean canSet = ct.setTrangThaiFromText(choice);

        if (!canSet) {
            JOptionPane.showMessageDialog(this, "Không thể bật chương trình đã quá hạn!");
            return;
        }

        boolean success = isAdding ? ctkmBUS.them(ct) : ctkmBUS.capNhat(ct);
        if (success) {
            JOptionPane.showMessageDialog(this, "Lưu chương trình thành công!");
            loadDataToTable_CTKM(ctkmBUS.getAll());

            // THÊM: Nếu đang cập nhật, load lại cả bảng khuyến mãi bên cạnh
            // để thấy trạng thái con đã bị đổi (nếu có)
            if (!isAdding) {
                loadDataToTable_KhuyenMai(ct.getMa());
            }

            setViewMode();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Lưu thất bại!");
        }
    }


    private boolean validateForm() {
        if (txtTenChuongTrinhKhuyenMai.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên chương trình không được để trống");
            return false;
        }
        if (jdNgayBatDau.getDate() == null || jdNgayKetThuc.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ ngày bắt đầu và kết thúc");
            return false;
        }
        if (jdNgayBatDau.getDate().after(jdNgayKetThuc.getDate())) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được sau ngày kết thúc");
            return false;
        }
        return true;
    }

    private void setViewMode() {
        isAdding = false;
        isUpdating = false;
        lockForm(true);
        btnThem.setEnabled(true);
        btnCapNhat.setEnabled(true);
        btnLuu.setVisible(false);
        btnHuy.setVisible(false);
        tableChuongTrinhKhuyenMai.setEnabled(true);
    }

    private void setAddMode() {
        isAdding = true;
        isUpdating = false;
        clearForm();
        lockForm(false);
        txtMaChuongTrinhKhuyenMai.setText(ctkmBUS.getNextId());
        txtMaChuongTrinhKhuyenMai.setEditable(false);

        cmbTrangThai.setSelectedItem(ChuongTrinhKM_DTO.DANG_AP_DUNG);

        btnThem.setEnabled(false);
        btnCapNhat.setEnabled(false);
        btnLuu.setVisible(true);
        btnHuy.setVisible(true);
        tableChuongTrinhKhuyenMai.setEnabled(false);
    }

    private void setUpdateMode() {
        isAdding = false;
        isUpdating = true;
        lockForm(false);
        txtMaChuongTrinhKhuyenMai.setEditable(false);
        cmbTrangThai.setEnabled(true); // Cho phép Admin thay đổi (Bật/Tắt)

        btnThem.setEnabled(false);
        btnCapNhat.setEnabled(false);
        btnLuu.setVisible(true);
        btnHuy.setVisible(true);
        tableChuongTrinhKhuyenMai.setEnabled(false);
    }

    private void lockForm(boolean lock) {
        txtTenChuongTrinhKhuyenMai.setEditable(!lock);
        txtMoTa.setEditable(!lock);
        jdNgayBatDau.setEnabled(!lock);
        jdNgayKetThuc.setEnabled(!lock);
        cmbTrangThai.setEnabled(!lock);
    }

    private void clearForm() {
        txtMaChuongTrinhKhuyenMai.setText("");
        txtTenChuongTrinhKhuyenMai.setText("");
        txtMoTa.setText("");
        jdNgayBatDau.setDate(null);
        jdNgayKetThuc.setDate(null);
        cmbTrangThai.setSelectedIndex(0);
        modelKhuyenMai.setRowCount(0);
    }

}