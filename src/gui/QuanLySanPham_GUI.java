package gui;

import bus.*;
import dto.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.File;

public class QuanLySanPham_GUI extends JPanel {
    private JPanel panelTieuDe;
    private JLabel label_tieuDe;
    private JButton btnNhapExcel;
    private JButton btnXuatExcel;
    private JPanel panelBoLoc;
    private JLabel labelTimKiem;
    private JComboBox cmbTimTheo;
    private JPanel panelDanhSachSanPham;
    private JTable tableSanPham;
    private JPanel panelThongTinChiTiet;
    private JLabel labelMaSanPham;
    private JLabel labelTenSanPham;
    private JTextField txtMaSanPham;
    private JTextField txtTenSanPham;
    private JComboBox cmbDonViTinh;
    private JLabel labelDonViTinh;
    private JLabel labelLoiNhuan;
    private JTextField txtLoiNhuan;
    private JLabel labelDanhMuc;
    private JLabel labelApDungDanhMuc;
    private JLabel labelQuyCach;
    private JComboBox cmbDanhMuc;
    private JButton btnCapNhat;
    private JButton btnThem;
    private JPanel panelCapNhat;
    private JLabel labelLocDanhMuc;
    private JComboBox cmbLocDanhMuc;
    private JPanel panelQuanLySanPham;
    private JButton btnThemHinhAnh;
    private JPanel panelHinhAnh;
    private JLabel labelHinhAnh;
    private JLabel labelSoLuongTonKho;
    private JTextField txtSoLuongTonKho;
    private JTextField txtSPTrongHop;
    private JTextField txtHopTrongThung;
    private JLabel labelTrangThai;
    private JComboBox cmbTrangThai;
    private JTextArea txtAreaThuocTinhRieng;
    private JLabel labelHopTrongThung;
    private JLabel labelSPTrongHop;
    private JPanel panelQuyCach;
    private JLabel labelLinkHinhAnh;
    private JTextField txtLinkHinhAnh;
    private JLabel labelLocLoiNhuan;
    private JComboBox cmbLocLoiNhuan;
    private JLabel labelNhapThongTin;
    private JTextField txtNhapThongTin;
    private JButton btnTimKiem;
    private JButton btnThoat;
    private JLabel labelKeDon;
    private JLabel labelLocTrangThai;
    private JComboBox cmbLocTrangThai;
    private JLabel labelSanPhamHienCo;
    private JTextField txtSanPhamHienCo;
    private JComboBox cmbKeDon;
    private JButton btnHuy;
    private JButton btnLuu;
    private DefaultTableModel modelSanPham;

    private JPopupMenu popupGoiY = new JPopupMenu();

    private final SanPham_BUS spBUS = SanPham_BUS.getInstance();
    private final QuyCach_BUS qcBUS = QuyCach_BUS.getInstance();
    private final DanhMuc_BUS dmBUS = DanhMuc_BUS.getInstance();
    private final ThuocTinhDanhMuc_BUS ttBus = ThuocTinhDanhMuc_BUS.getInstance();
    private final GiaTriThuocTinh_BUS gtBus = GiaTriThuocTinh_BUS.getInstance();
    private final GiaTriThuocTinh_SP_BUS gtspBus = GiaTriThuocTinh_SP_BUS.getInstance();


    private boolean isAdding = false;
    private boolean isUpdating = false;

    public QuanLySanPham_GUI() {
        this.setLayout(new BorderLayout());
        if (panelQuanLySanPham != null) {
            this.add(panelQuanLySanPham, BorderLayout.CENTER);
        }

        initTable();
        initComboBoxData();
        loadDataToTable(spBUS.getAll());
        addEvents();
        setViewMode(); // Mặc định khóa form
    }

    private void initTable() {
        String[] headers = {"STT", "Mã SP", "Tên Sản Phẩm", "ĐVT", "Lợi Nhuận", "Kê đơn", "Danh Mục", "Hình Ảnh", "Trạng Thái"};
        modelSanPham = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSanPham.setModel(modelSanPham);

        // Cấu hình độ rộng cột nếu cần...
    }

    private void initComboBoxData() {
        // 1. Load Danh mục
        cmbDanhMuc.removeAllItems();
        cmbLocDanhMuc.removeAllItems();
        cmbLocDanhMuc.addItem("Tất cả");

        for (DanhMuc_DTO dm : dmBUS.getAll()) {
            cmbDanhMuc.addItem(dm.getTenDM());
            cmbLocDanhMuc.addItem(dm.getTenDM());
        }

        // 2. Load ĐVT
        String[] dvt = {"Viên", "Vỉ", "Hộp", "Chai", "Tuýp", "Lọ"};
        cmbDonViTinh.setModel(new DefaultComboBoxModel<>(dvt));

        // 3. Load Kê đơn (Sử dụng hằng số từ DTO)
        cmbKeDon.setModel(new DefaultComboBoxModel<>(new String[]{
                SanPham_DTO.CO_KE_DON,
                SanPham_DTO.KHONG_KE_DON
        }));

        cmbTimTheo.setModel(new DefaultComboBoxModel<>(new String[]{
                "Tất cả",
                "Tên sản phẩm",
                "Mã sản phẩm"
        }));

        // Cấu hình: Lọc Lợi nhuận (Sắp xếp)
        cmbLocLoiNhuan.setModel(new DefaultComboBoxModel<>(new String[]{
                "Không sắp xếp",
                "Tăng dần",
                "Giảm dần"
        }));

        // 4. Load Trạng thái (Sử dụng hằng số từ DTO)
        cmbTrangThai.setModel(new DefaultComboBoxModel<>(new String[]{
                SanPham_DTO.DANG_BAN,
                SanPham_DTO.NGUNG_BAN
        }));

        // 5. Load Lọc trạng thái
        if (cmbLocTrangThai != null) {
            cmbLocTrangThai.setModel(new DefaultComboBoxModel<>(new String[]{
                    "Tất cả",
                    SanPham_DTO.DANG_BAN,
                    SanPham_DTO.NGUNG_BAN
            }));
        }
    }

    private void loadDataToTable(ArrayList<SanPham_DTO> list) {
        modelSanPham.setRowCount(0);
        int stt = 1;
        for (SanPham_DTO sp : list) {
            DanhMuc_DTO dm = dmBUS.getById(sp.getMaDM());
            String tenDM = (dm != null) ? dm.getTenDM() : sp.getMaDM();

            modelSanPham.addRow(new Object[]{
                    stt++,
                    sp.getMaSP(),
                    sp.getTenSP(),
                    sp.getDonViTinh(),
                    sp.getLoiNhuan(),
                    sp.getKeDonText(),
                    tenDM,
                    sp.getHinhAnh(),
                    sp.getTrangThaiText()
            });
        }
        txtSanPhamHienCo.setText(String.valueOf(list.size()));
    }

    private void addEvents() {
        tableSanPham.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableSanPham.getSelectedRow();
                if (row >= 0) {
                    String maSP = tableSanPham.getValueAt(row, 1).toString();
                    SanPham_DTO sp = spBUS.getById(maSP);
                    if (sp != null) fillData(sp);
                }
            }
        });

        btnThem.addActionListener(e -> setAddMode());

        btnCapNhat.addActionListener(e -> {
            if (tableSanPham.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần cập nhật");
                return;
            }
            setUpdateMode();
        });

        btnLuu.addActionListener(e -> saveSanPham());

        btnHuy.addActionListener(e -> {
            clearForm();
            setViewMode();
        });

        txtNhapThongTin.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() != java.awt.event.KeyEvent.VK_UP &&
                        e.getKeyCode() != java.awt.event.KeyEvent.VK_DOWN &&
                        e.getKeyCode() != java.awt.event.KeyEvent.VK_ENTER) {
                    hienThiGoiY();
                    thucHienLoc();
                }
            }
        });

        // Sự kiện khi chọn các ComboBox lọc -> Tự động lọc ngay không cần bấm nút (Optional)
        cmbLocDanhMuc.addActionListener(e -> thucHienLoc());
        cmbLocTrangThai.addActionListener(e -> thucHienLoc());
        cmbLocLoiNhuan.addActionListener(e -> thucHienLoc());

        // Nút Tìm kiếm
        btnTimKiem.addActionListener(e -> thucHienLoc());

        // 2. Tự động lọc khi thay đổi các ComboBox
        ActionListener al = e -> thucHienLoc();
        cmbTimTheo.addActionListener(al);
        cmbLocDanhMuc.addActionListener(al);
        cmbLocTrangThai.addActionListener(al);
        cmbLocLoiNhuan.addActionListener(al);

        // 3. Nút Làm mới (Thoát lọc)
        btnThoat.addActionListener(e -> {
            txtNhapThongTin.setText("");
            cmbTimTheo.setSelectedIndex(0);
            cmbLocDanhMuc.setSelectedIndex(0);
            cmbLocTrangThai.setSelectedIndex(0);
            cmbLocLoiNhuan.setSelectedIndex(0);
            loadDataToTable(spBUS.getAll());
        });

        // --- SỰ KIỆN COPY ẢNH VÀO PROJECT ---
        btnThemHinhAnh.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Hình ảnh", "jpg", "png", "gif"));

            int res = fileChooser.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File sourceFile = fileChooser.getSelectedFile();

                try {
                    // 1. Xác định thư mục đích (src/images)
                    // Lấy thư mục gốc dự án + src/images
                    String destFolder = "src/images";
                    File folder = new File(destFolder);
                    if (!folder.exists()) folder.mkdirs(); // Tạo thư mục nếu chưa có

                    // 2. Tạo file đích (Giữ nguyên tên file gốc)
                    String fileName = sourceFile.getName();
                    File destFile = new File(destFolder + "/" + fileName);

                    // 3. Copy file (Nếu trùng tên thì ghi đè - REPLACE_EXISTING)
                    Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    // 4. Lưu đường dẫn TƯƠNG ĐỐI để đưa vào Database
                    // Lưu ý: DB bạn đang dùng tiền tố "img/" hay "images/" thì sửa ở đây cho khớp
                    String relativePath = "img/" + fileName;

                    // 5. Hiển thị lên giao diện
                    txtLinkHinhAnh.setText(relativePath);
                    updateImagePreview(relativePath);

                    JOptionPane.showMessageDialog(this, "Đã copy ảnh vào thư mục images!");

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi copy ảnh: " + ex.getMessage());
                }
            }
        });
    }


    // 4. HÀM THỰC HIỆN LỌC NÂNG CAO (KẾT HỢP TẤT CẢ TIÊU CHÍ)
    private void thucHienLoc() {
        String keyword = txtNhapThongTin.getText().trim();
        String timTheo = (String) cmbTimTheo.getSelectedItem();

        // Lấy mã Danh mục từ tên được chọn
        String tenDM = (String) cmbLocDanhMuc.getSelectedItem();
        String maDM = "Tất cả";
        if (!tenDM.equals("Tất cả")) {
            for (DanhMuc_DTO dm : dmBUS.getAll()) {
                if (dm.getTenDM().equals(tenDM)) {
                    maDM = dm.getMaDM();
                    break;
                }
            }
        }

        // Lấy trạng thái
        String ttStr = (String) cmbLocTrangThai.getSelectedItem();
        Integer trangThai = null;
        if (!ttStr.equals("Tất cả")) {
            trangThai = SanPham_DTO.parseTrangThaiFromText(ttStr);
        }

        String sortLN = (String) cmbLocLoiNhuan.getSelectedItem();

        // GỌI BUS XỬ LÝ
        ArrayList<SanPham_DTO> dsLoc = spBUS.timKiemNangCao(keyword, timTheo, maDM, trangThai, sortLN);

        // Đổ dữ liệu lên bảng (Hàm này bạn đã viết ở bước trước)
        loadDataToTable(dsLoc);
    }

    private void hienThiGoiY() {
        popupGoiY.setVisible(false);
        popupGoiY.removeAll();

        String textInput = txtNhapThongTin.getText().trim();
        if (textInput.isEmpty()) return;

        String timTheo = (String) cmbTimTheo.getSelectedItem();

        // Tận dụng hàm lọc của BUS để lấy danh sách gợi ý (bỏ qua lọc DM, TT và Sắp xếp)
        ArrayList<SanPham_DTO> dsGoiY = spBUS.timKiemNangCao(textInput, timTheo, "Tất cả", null, "Không sắp xếp");

        if (dsGoiY.isEmpty()) return;

        JPanel panelGoiY = new JPanel();
        panelGoiY.setLayout(new BoxLayout(panelGoiY, BoxLayout.Y_AXIS));
        panelGoiY.setBackground(Color.WHITE);

        int count = 0;
        for (SanPham_DTO sp : dsGoiY) {
            if (count >= 10) break;

            String hienThi = sp.getMaSP() + " - " + sp.getTenSP();
            JButton btnItem = new JButton(hienThi);
            // ... (Phần Style Button và Hover effect giữ nguyên như code của bạn) ...

            btnItem.addActionListener(e -> {
                // Nếu tìm theo "Tất cả" hoặc "Mã", điền Mã. Nếu tìm theo "Tên", điền Tên.
                if (timTheo.equals("Tên sản phẩm")) {
                    txtNhapThongTin.setText(sp.getTenSP());
                } else {
                    txtNhapThongTin.setText(sp.getMaSP());
                }
                popupGoiY.setVisible(false);
                thucHienLoc();
            });

            panelGoiY.add(btnItem);
            count++;
        }

        JScrollPane scrollPane = new JScrollPane(panelGoiY);
        scrollPane.setBorder(null);
        popupGoiY.add(scrollPane);
        popupGoiY.setFocusable(false);
        popupGoiY.show(txtNhapThongTin, 0, txtNhapThongTin.getHeight());
        txtNhapThongTin.requestFocus();
    }

    private void fillData(SanPham_DTO sp) {
        txtMaSanPham.setText(sp.getMaSP());
        txtTenSanPham.setText(sp.getTenSP());
        cmbDonViTinh.setSelectedItem(sp.getDonViTinh());
        txtLoiNhuan.setText(String.valueOf(sp.getLoiNhuan()));
        txtLinkHinhAnh.setText(sp.getHinhAnh());
        updateImagePreview(sp.getHinhAnh());

        // Set ComboBox dựa trên Text từ DTO (Rất gọn)
        cmbKeDon.setSelectedItem(sp.getKeDonText());
        cmbTrangThai.setSelectedItem(sp.getTrangThaiText());

        // Danh mục
        DanhMuc_DTO dm = dmBUS.getById(sp.getMaDM());
        if (dm != null) cmbDanhMuc.setSelectedItem(dm.getTenDM());

        // Quy cách
        QuyCach_DTO qc = qcBUS.getById(sp.getMaQC());
        if (qc != null) {
            txtSPTrongHop.setText(String.valueOf(qc.getSlTrongHop()));
            txtHopTrongThung.setText(String.valueOf(qc.getSlHopTrongThung()));
        }

        // Thuộc tính riêng
        StringBuilder sb = new StringBuilder();
        ArrayList<GiaTriThuocTinh_SP_DTO> listGTSP = gtspBus.getByMaSP(sp.getMaSP());
        for (GiaTriThuocTinh_SP_DTO gtsp : listGTSP) {
            ThuocTinhDanhMuc_DTO tt = ttBus.getById(gtsp.getMaThuocTinh());
            GiaTriThuocTinh_DTO gt = gtBus.getById(gtsp.getMaGiaTri());
            if (tt != null && gt != null) {
                sb.append("• ").append(tt.getTenThuocTinh()).append(": ").append(gt.getNdGiaTri()).append("\n");
            }
        }
        txtAreaThuocTinhRieng.setText(sb.toString());

        updateImagePreview(sp.getHinhAnh());
    }

    private void saveSanPham() {
        if (!validateForm()) return;

        // 1. Lấy dữ liệu từ form
        String maSP = txtMaSanPham.getText();
        String tenSP = txtTenSanPham.getText();
        String donViTinh = (String) cmbDonViTinh.getSelectedItem();
        double loiNhuan = Double.parseDouble(txtLoiNhuan.getText());
        String hinhAnh = txtLinkHinhAnh.getText();

        // CHUYỂN ĐỔI COMBOBOX TEXT -> INT (Dùng hàm parse của DTO)
        int keDon = SanPham_DTO.parseKeDonFromText((String) cmbKeDon.getSelectedItem());
        int trangThai = SanPham_DTO.parseTrangThaiFromText((String) cmbTrangThai.getSelectedItem());

        // --- LẤY THÔNG TIN QUY CÁCH TỪ GUI ---
        int slTrongHop = 0;
        int slHopTrongThung = 0;
        try {
            slTrongHop = Integer.parseInt(txtSPTrongHop.getText());
            slHopTrongThung = Integer.parseInt(txtHopTrongThung.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quy cách phải là số nguyên dương");
            return;
        }

        // Lấy mã danh mục
        String tenDM = (String) cmbDanhMuc.getSelectedItem();
        String maDM = "";
        for (DanhMuc_DTO d : dmBUS.getAll()) {
            if (d.getTenDM().equals(tenDM)) {
                maDM = d.getMaDM();
                break;
            }
        }

        // Lưu ý: maQC để null hoặc rỗng ở đây, BUS sẽ tự tính toán dựa trên 2 số liệu kia
        SanPham_DTO sp = new SanPham_DTO(maSP, tenSP, donViTinh, loiNhuan, hinhAnh, keDon, trangThai, maDM, null);

        // 2. Gọi BUS (Truyền thêm 2 tham số quy cách)
        boolean result = false;
        if (isAdding) {
            // GỌI HÀM MỚI VỚI 3 THAM SỐ
            result = spBUS.them(sp, slTrongHop, slHopTrongThung);
        } else if (isUpdating) {
            // GỌI HÀM MỚI VỚI 3 THAM SỐ
            result = spBUS.capNhat(sp, slTrongHop, slHopTrongThung);
        }

        // 3. Phản hồi
        if (result) {
            JOptionPane.showMessageDialog(this, (isAdding ? "Thêm" : "Cập nhật") + " thành công!");
            loadDataToTable(spBUS.getAll());
            clearForm();
            setViewMode();
        } else {
            JOptionPane.showMessageDialog(this, (isAdding ? "Thêm" : "Cập nhật") + " thất bại! Vui lòng kiểm tra lại.");
        }
    }

    private boolean validateForm() {
        if (txtTenSanPham.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống");
            return false;
        }
        try {
            Double.parseDouble(txtLoiNhuan.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lợi nhuận phải là số");
            return false;
        }
        return true;
    }


    private void setViewMode() {
        isAdding = false;
        isUpdating = false;
        lockForm(true);
        tableSanPham.setEnabled(true);
        btnThem.setEnabled(true);
        btnCapNhat.setEnabled(true);
        btnLuu.setVisible(false);
        btnHuy.setVisible(false);
    }

    private void setAddMode() {
        isAdding = true;
        isUpdating = false;
        clearForm();
        lockForm(false);

        txtMaSanPham.setText(spBUS.getNextId());
        txtMaSanPham.setEditable(false);
        txtSoLuongTonKho.setEditable(false);
        txtLinkHinhAnh.setEditable(false);
        txtAreaThuocTinhRieng.setEditable(false);
        cmbTrangThai.setSelectedItem(SanPham_DTO.DANG_BAN);

        tableSanPham.setEnabled(false);
        btnThem.setEnabled(false);
        btnCapNhat.setEnabled(false);
        btnLuu.setVisible(true);
        btnHuy.setVisible(true);
    }

    private void setUpdateMode() {
        isAdding = false;
        isUpdating = true;
        lockForm(false);

        txtMaSanPham.setEditable(false); // Không sửa mã

        tableSanPham.setEnabled(false);
        btnThem.setEnabled(false);
        btnCapNhat.setEnabled(false);
        btnLuu.setVisible(true);
        btnHuy.setVisible(true);
    }

    private void lockForm(boolean lock) {
        txtMaSanPham.setEditable(!lock);
        txtTenSanPham.setEditable(!lock);
        txtLoiNhuan.setEditable(!lock);
        txtSoLuongTonKho.setEditable(!lock);
        txtSPTrongHop.setEditable(!lock);
        txtHopTrongThung.setEditable(!lock);
        cmbDonViTinh.setEnabled(!lock);
        cmbDanhMuc.setEnabled(!lock);
        cmbKeDon.setEnabled(!lock);
        cmbTrangThai.setEnabled(!lock); // Cẩn thận: AddMode sẽ override cái này
        btnThemHinhAnh.setEnabled(!lock);
        txtAreaThuocTinhRieng.setEditable(!lock);
        txtLinkHinhAnh.setEditable(!lock);
    }

    private void clearForm() {
        txtMaSanPham.setText("");
        txtTenSanPham.setText("");
        txtLoiNhuan.setText("");
        txtSoLuongTonKho.setText("");
        txtSPTrongHop.setText("");
        txtHopTrongThung.setText("");
        txtAreaThuocTinhRieng.setText("");
        txtLinkHinhAnh.setText("");
        cmbTrangThai.setSelectedIndex(0);
        cmbDanhMuc.setSelectedIndex(0);
        cmbKeDon.setSelectedIndex(0);
    }

    private void updateImagePreview(String path) {
        // Kích thước hiển thị
        int w = labelHinhAnh.getWidth();
        int h = labelHinhAnh.getHeight();
        if (w == 0 || h == 0) { w = 180; h = 180; }

        labelHinhAnh.setText("");
        labelHinhAnh.setIcon(null);


        // 1. Kiểm tra path null
        if (path == null || path.trim().isEmpty()) {
            labelHinhAnh.setText("Chưa có ảnh");
            return;
        }

        try {
            // 2. XỬ LÝ ĐƯỜNG DẪN (Quan trọng)
            // Vì DB lưu "img/..." nhưng thư mục là "images", ta có thể fix cứng hoặc xử lý linh hoạt
            // Cách tốt nhất: Path đầu vào nên là "images/panadol.jpg" cho khớp.
            // Nhưng nếu DB lỡ lưu "img/" thì ta replace:
            String realPath = path;
            if (path.startsWith("img/")) {
                realPath = path.replace("img/", "images/");
            }

            // 3. Tìm file trong dự án
            // "src" dùng khi chạy trong IDE. Nếu build ra JAR thì cần xử lý khác, nhưng ở đây ta làm cho IDE trước.
            java.io.File f = new java.io.File("src/" + realPath);

            // Nếu không tìm thấy trong src, thử tìm ở thư mục gốc (trường hợp build xong)
            if (!f.exists()) {
                f = new java.io.File(realPath);
            }

            // 4. Kiểm tra tồn tại
            if (!f.exists()) {
                labelHinhAnh.setText("Chưa có ảnh");
                // Có thể load ảnh mặc định ở đây nếu muốn
                return;
            }

            // 5. Load và hiển thị
            ImageIcon icon = new ImageIcon(f.getAbsolutePath());
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            labelHinhAnh.setIcon(new ImageIcon(scaledImg));

        } catch (Exception e) {
            e.printStackTrace();
            labelHinhAnh.setText("Lỗi tải ảnh");
        }
    }
}