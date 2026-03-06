package gui;

import bus.ChuongTrinhKM_BUS;
import bus.DanhMuc_BUS;
import bus.KhuyenMai_BUS;
import bus.SanPham_BUS;
import dto.ChuongTrinhKM_DTO;
import dto.DanhMuc_DTO;
import dto.KhuyenMai_DTO;
import dto.SanPham_DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class QuanLyKhuyenMai_GUI extends JPanel {
    private JButton btnCapNhat;
    private JButton btnThem;
    private JLabel labelMaKhuyenMai;
    private JLabel labelTenKhuyenMai;
    private JTextField txtMaKhuyenMai;
    private JTextField txtTenKhuyenMai;
    private JComboBox<String> cmbLoaiKhuyenMai;
    private JLabel labelLoaiKhuyenMai;
    private JLabel labelGiaTriKhuyenMai;
    private JTextField txtGiaTriKhuyenMai;
    private JLabel labelDoiTuongApDung;

    // --- SỬA TỪ TEXTFIELD SANG COMBOBOX ---
    private JLabel labelApDungDanhMuc;
    private JComboBox<String> cmbApDungDanhMuc;
    private JLabel labelApDungSanPham;
    private JComboBox<String> cmbApDungSanPham;
    // --------------------------------------

    private JPanel panelDanhSachKhuyenMai;
    private JTable tableKhuyenMai;
    private JPanel panelTieuDe;
    private JLabel label_tieuDe;
    private JButton btnNhapExcel;
    private JButton btnXuatExcel;
    private JPanel panelBoLoc;
    private JLabel labelTimKiem;
    private JComboBox<String> cmbTimTheo;
    private JComboBox<String> cmbLocTrangThai;
    private JLabel labelLocTrangThai;
    private JComboBox<String> cmbLocLoaiKhuyenMai;
    private JLabel labelLocLoaiKhuyenMai;
    private JLabel labelLocDoiTuongApDung;
    private JPanel panelQuanLyKhuyenMai;
    private JComboBox<String> cmbLocDoiTuongApDung;
    private JLabel labelChuongTrinhKhuyenMai;
    private JComboBox<String> cmbChuongTrinhKhuyenMai;
    private JComboBox<String> cmbLocChuongTrinhKhuyenMai;
    private JLabel labelLocChuongTrinhKhuyenMai;
    private JPanel panelThongTinChiTiet;

    private JLabel labelTrangThai;
    private JComboBox<String> cmbTrangThai;
    private JComboBox<String> cmbDoiTuongApDung;
    private JPanel panelCapNhat;
    private JButton btnThoat;
    private JButton btnTimkiem;
    private JTextField txtNhapThongTin;
    private JLabel labelNhapThongTin;
    private JTextField txtKhuyenMaiHienCo;
    private JLabel labelKhuyenMaiHienCo;
    private JTextField txtSoLuotSuDung;
    private JLabel labelSoLuotSuDung;
    private JButton btnHuy;
    private JButton btnLuu;
    private DefaultTableModel modelKhuyenMai;

    // --- BUS ---
    private final KhuyenMai_BUS kmBUS = KhuyenMai_BUS.getInstance();
    private final ChuongTrinhKM_BUS ctkmBUS = ChuongTrinhKM_BUS.getInstance();
    private final DanhMuc_BUS dmBUS = DanhMuc_BUS.getInstance();
    private final SanPham_BUS spBUS = SanPham_BUS.getInstance();

    private final DecimalFormat df = new DecimalFormat("#,###.##");
    private boolean isAdding = false;
    private boolean isUpdating = false;
    private JPopupMenu popupGoiY = new JPopupMenu();
    private boolean isSearching = false;

    public QuanLyKhuyenMai_GUI(){
        this.setLayout(new BorderLayout());
        if (panelQuanLyKhuyenMai != null) {
            this.add(panelQuanLyKhuyenMai, BorderLayout.CENTER);
        }

        initTable_KhuyenMai();
        initComboBoxData();

        loadDataToTable_KhuyenMai(kmBUS.getAll());
        addEvents();
        setViewMode();
    }

    private void initTable_KhuyenMai() {
        String[] headers = {
                "STT", "Mã KM", "Tên KM", "Loại KM", "Giá Trị",
                "Đối Tượng", "Chi Tiết Áp Dụng", "Chương Trình KM", "Trạng Thái"
        };
        modelKhuyenMai = new DefaultTableModel(headers, 0);
        tableKhuyenMai.setModel(modelKhuyenMai);
    }

    private void initComboBoxData() {
        // 1. Loại khuyến mãi
        cmbLoaiKhuyenMai.removeAllItems();
        cmbLoaiKhuyenMai.addItem(KhuyenMai_DTO.PHAN_TRAM);
        cmbLoaiKhuyenMai.addItem(KhuyenMai_DTO.TIEN_MAT);

        // 2. Trạng thái
        cmbTrangThai.removeAllItems();
        cmbTrangThai.addItem(KhuyenMai_DTO.DANG_AP_DUNG);
        cmbTrangThai.addItem(KhuyenMai_DTO.NGUNG_AP_DUNG);

        // 3. Đối tượng áp dụng
        cmbDoiTuongApDung.removeAllItems();
        cmbDoiTuongApDung.addItem(KhuyenMai_DTO.DANH_MUC); // Index 0
        cmbDoiTuongApDung.addItem(KhuyenMai_DTO.SAN_PHAM); // Index 1

        // 4. Chương trình KM
        cmbChuongTrinhKhuyenMai.removeAllItems();
        for (ChuongTrinhKM_DTO ct : ctkmBUS.getAll()) {
            cmbChuongTrinhKhuyenMai.addItem(ct.getMa() + " - " + ct.getTen());
        }

        // 5. DANH MỤC (Đổ dữ liệu vào ComboBox)
        if (cmbApDungDanhMuc != null) {
            cmbApDungDanhMuc.removeAllItems();
            for (DanhMuc_DTO dm : dmBUS.getAll()) {
                // Hiển thị: "DM001 - Thuốc giảm đau"
                cmbApDungDanhMuc.addItem(dm.getMaDM() + " - " + dm.getTenDM());
            }
        }

        // 6. SẢN PHẨM (Đổ dữ liệu vào ComboBox)
        if (cmbApDungSanPham != null) {
            cmbApDungSanPham.removeAllItems();
            for (SanPham_DTO sp : spBUS.getAll()) {
                // Hiển thị: "SP001 - Panadol"
                cmbApDungSanPham.addItem(sp.getMaSP() + " - " + sp.getTenSP());
            }
        }

        // Mặc định lúc đầu: Chọn Danh mục -> Khóa Sản phẩm
        if (cmbApDungSanPham != null) cmbApDungSanPham.setEnabled(false);
        // 7. Init ComboBox Tìm theo
        cmbTimTheo.setModel(new DefaultComboBoxModel<>(new String[]{"Tất cả", "Mã khuyến mãi", "Tên khuyến mãi"}));

        // 8. Init ComboBox Lọc Loại
        cmbLocLoaiKhuyenMai.setModel(new DefaultComboBoxModel<>(new String[]{"Tất cả", KhuyenMai_DTO.PHAN_TRAM, KhuyenMai_DTO.TIEN_MAT}));

        // 9. Init ComboBox Lọc Trạng thái
        cmbLocTrangThai.setModel(new DefaultComboBoxModel<>(new String[]{"Tất cả", KhuyenMai_DTO.DANG_AP_DUNG, KhuyenMai_DTO.NGUNG_AP_DUNG}));

        // 10. Init ComboBox Lọc Đối tượng
        cmbLocDoiTuongApDung.setModel(new DefaultComboBoxModel<>(new String[]{"Tất cả", KhuyenMai_DTO.DANH_MUC, KhuyenMai_DTO.SAN_PHAM}));

        // 11. Init ComboBox Lọc Chương trình
        cmbLocChuongTrinhKhuyenMai.removeAllItems();
        cmbLocChuongTrinhKhuyenMai.addItem("Tất cả");
        for (ChuongTrinhKM_DTO ct : ctkmBUS.getAll()) {
            cmbLocChuongTrinhKhuyenMai.addItem(ct.getMa() + " - " + ct.getTen());
        }

    }
    // Hàm thực hiện lọc tổng hợp
    private ArrayList<KhuyenMai_DTO> thucHienLoc() {
        String keyword = txtNhapThongTin.getText().trim().toLowerCase();
        String timTheo = cmbTimTheo.getSelectedItem().toString();

        String locLoai = cmbLocLoaiKhuyenMai.getSelectedItem().toString();
        String locTrangThai = cmbLocTrangThai.getSelectedItem().toString();
        String locDoiTuong = cmbLocDoiTuongApDung.getSelectedItem().toString();
        String locCTKM = cmbLocChuongTrinhKhuyenMai.getSelectedItem().toString();

        ArrayList<KhuyenMai_DTO> dsGoc = kmBUS.getAll();
        ArrayList<KhuyenMai_DTO> ketQua = new ArrayList<>();

        for (KhuyenMai_DTO km : dsGoc) {
            // 1. Kiểm tra Từ khóa & Loại tìm kiếm
            boolean matchSearch = false;
            if (timTheo.equals("Tất cả")) {
                matchSearch = km.getMaKM().toLowerCase().contains(keyword) || km.getTenKM().toLowerCase().contains(keyword);
            } else if (timTheo.equals("Mã khuyến mãi")) {
                matchSearch = km.getMaKM().toLowerCase().contains(keyword);
            } else {
                matchSearch = km.getTenKM().toLowerCase().contains(keyword);
            }

            // 2. Kiểm tra Loại KM
            boolean matchLoai = locLoai.equals("Tất cả") || km.getLoaiKMText().equals(locLoai);

            // 3. Kiểm tra Trạng thái (Lấy trạng thái hiển thị thông minh)
            ChuongTrinhKM_DTO ct = ctkmBUS.getById(km.getMaChuongTrinh());
            int statusCT = (ct != null) ? ct.getTrangThai() : 0;
            String ttHienThi = km.getTrangThaiHienThi(statusCT);

            // Dùng startsWith để "Ngưng áp dụng" khớp được với "Ngưng áp dụng (Theo CT)"
            boolean matchTrangThai = locTrangThai.equals("Tất cả") || ttHienThi.startsWith(locTrangThai);

            // 4. Kiểm tra Đối tượng
            boolean matchDoiTuong = locDoiTuong.equals("Tất cả") || km.getDoiTuongText().equals(locDoiTuong);

            // 5. Kiểm tra Chương trình
            boolean matchCT = locCTKM.equals("Tất cả") || locCTKM.startsWith(km.getMaChuongTrinh());

            if (matchSearch && matchLoai && matchTrangThai && matchDoiTuong && matchCT) {
                ketQua.add(km);
            }
        }
        return ketQua;
    }

    // Hàm hiển thị gợi ý
    private void hienThiGoiY(ArrayList<KhuyenMai_DTO> list) {
        popupGoiY.setVisible(false);
        popupGoiY.removeAll();
        if (list.isEmpty() || txtNhapThongTin.getText().trim().isEmpty()) return;

        int itemsToShow = Math.min(list.size(), 5); // Hiện tối đa 5 gợi ý
        for (int i = 0; i < itemsToShow; i++) {
            KhuyenMai_DTO km = list.get(i);
            String textHienThi = km.getMaKM() + " - " + km.getTenKM();
            JMenuItem item = new JMenuItem(textHienThi);
            item.setPreferredSize(new Dimension(txtNhapThongTin.getWidth(), 30));

            item.addActionListener(e -> {
                txtNhapThongTin.setText(km.getMaKM()); // Hoặc km.getTenKM() tùy bạn
                loadDataToTable_KhuyenMai(thucHienLoc());
                popupGoiY.setVisible(false);
            });
            popupGoiY.add(item);
        }
        popupGoiY.show(txtNhapThongTin, 0, txtNhapThongTin.getHeight());
        txtNhapThongTin.requestFocus();
    }

    private void loadDataToTable_KhuyenMai(ArrayList<KhuyenMai_DTO> list) {
        modelKhuyenMai.setRowCount(0);
        int stt = 1;
        for (KhuyenMai_DTO km : list) {
            ChuongTrinhKM_DTO ct = ctkmBUS.getById(km.getMaChuongTrinh());
            int statusCT = (ct != null) ? ct.getTrangThai() : 0;

            String loaiKMStr = km.getLoaiKMText();
            String trangThaiHienThi = km.getTrangThaiHienThi(statusCT);
            String doiTuongStr = km.getDoiTuongText();

            // HIỂN THỊ NGUYÊN BẢN GIÁ TRỊ (Ví dụ 0.05 hoặc 20000)
            double giaTriGoc = km.getGiaTriKhuyenMai();
            String giaTriStr = (km.getLoaiKhuyenMai() == KhuyenMai_DTO.LOAI_PHAN_TRAM)
                    ? String.valueOf(giaTriGoc) // Hiện 0.05
                    : df.format(giaTriGoc) + " VNĐ";

            String chiTietApDung = "";
            if (km.getDoiTuongApDung() == KhuyenMai_DTO.DT_DANH_MUC) {
                DanhMuc_DTO dm = dmBUS.getById(km.getMaDanhMuc());
                chiTietApDung = (dm != null) ? dm.getTenDM() : km.getMaDanhMuc();
            } else {
                SanPham_DTO sp = spBUS.getById(km.getMaSanPham());
                chiTietApDung = (sp != null) ? sp.getTenSP() : km.getMaSanPham();
            }

            modelKhuyenMai.addRow(new Object[]{
                    stt++, km.getMaKM(), km.getTenKM(), loaiKMStr, giaTriStr,
                    doiTuongStr, chiTietApDung, (ct != null ? ct.getTen() : ""), trangThaiHienThi
            });
        }
        if (txtKhuyenMaiHienCo != null) txtKhuyenMaiHienCo.setText(String.valueOf(list.size()));
    }

    // 2. SỬA HÀM ĐỔ DỮ LIỆU LÊN FORM KHI CLICK BẢNG
    private void fillDataFromTable(int row) {
        String maKM = modelKhuyenMai.getValueAt(row, 1).toString();
        KhuyenMai_DTO km = kmBUS.getById(maKM);
        if (km == null) return;

        txtMaKhuyenMai.setText(km.getMaKM());
        txtTenKhuyenMai.setText(km.getTenKM());

        // HIỂN THỊ 0.05 LÊN TEXTFIELD
        txtGiaTriKhuyenMai.setText(String.valueOf(km.getGiaTriKhuyenMai()));

        cmbLoaiKhuyenMai.setSelectedItem(km.getLoaiKMText());

        ChuongTrinhKM_DTO ctCha = ctkmBUS.getById(km.getMaChuongTrinh());
        int trangThaiCha = (ctCha != null) ? ctCha.getTrangThai() : 0;
        cmbTrangThai.setSelectedItem(km.getTrangThaiHienThi(trangThaiCha));

        cmbDoiTuongApDung.setSelectedItem(km.getDoiTuongText());
        setSelectedComboBoxItem(cmbChuongTrinhKhuyenMai, km.getMaChuongTrinh());

        if (km.getDoiTuongApDung() == KhuyenMai_DTO.DT_DANH_MUC) {
            setSelectedComboBoxItem(cmbApDungDanhMuc, km.getMaDanhMuc());
            cmbApDungDanhMuc.setEnabled(true);
            cmbApDungSanPham.setEnabled(false);
        } else {
            setSelectedComboBoxItem(cmbApDungSanPham, km.getMaSanPham());
            cmbApDungSanPham.setEnabled(true);
            cmbApDungDanhMuc.setEnabled(false);
        }
    }

    private void saveKhuyenMai() {
        try {
            String ma = txtMaKhuyenMai.getText().trim();
            String ten = txtTenKhuyenMai.getText().trim();

            // LẤY TRỰC TIẾP GIÁ TRỊ (VÍ DỤ: 0.05)
            double giaTri = Double.parseDouble(txtGiaTriKhuyenMai.getText().trim());

            String textLoai = (String) cmbLoaiKhuyenMai.getSelectedItem();
            int loai = textLoai.equals(KhuyenMai_DTO.PHAN_TRAM) ? KhuyenMai_DTO.LOAI_PHAN_TRAM : KhuyenMai_DTO.LOAI_TIEN_MAT;

            int trangThai = KhuyenMai_DTO.parseTrangThaiFromText((String) cmbTrangThai.getSelectedItem());

            String textDoiTuong = (String) cmbDoiTuongApDung.getSelectedItem();
            int doiTuong = textDoiTuong.equals(KhuyenMai_DTO.SAN_PHAM) ? KhuyenMai_DTO.DT_SAN_PHAM : KhuyenMai_DTO.DT_DANH_MUC;

            String maCT = cmbChuongTrinhKhuyenMai.getSelectedItem().toString().split(" - ")[0];
            String maSP = (doiTuong == KhuyenMai_DTO.DT_SAN_PHAM) ? cmbApDungSanPham.getSelectedItem().toString().split(" - ")[0] : null;
            String maDM = (doiTuong == KhuyenMai_DTO.DT_DANH_MUC) ? cmbApDungDanhMuc.getSelectedItem().toString().split(" - ")[0] : null;

            KhuyenMai_DTO km = new KhuyenMai_DTO(ma, ten, loai, giaTri, trangThai, doiTuong, maCT, maSP, maDM);

            boolean success;
            if (isAdding) {
                // Lấy số lượt từ textfield
                int soLuot = Integer.parseInt(txtSoLuotSuDung.getText().trim());
                success = kmBUS.them(km, soLuot);
            } else {
                // Cập nhật không truyền số lượt (theo yêu cầu)
                success = kmBUS.capNhat(km);
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Lưu thành công!");
                setViewMode();
                loadDataToTable_KhuyenMai(kmBUS.getAll());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ cho giá trị và số lượt!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void addEvents() {
        btnThem.addActionListener(e -> setAddMode());

        btnCapNhat.addActionListener(e -> setUpdateMode());

        btnLuu.addActionListener(e -> saveKhuyenMai());

        // Sự kiện khi gõ phím vào ô tìm kiếm để hiện gợi ý
        txtNhapThongTin.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() != java.awt.event.KeyEvent.VK_UP &&
                        e.getKeyCode() != java.awt.event.KeyEvent.VK_DOWN &&
                        e.getKeyCode() != java.awt.event.KeyEvent.VK_ENTER) {
                    hienThiGoiY(thucHienLoc());
                }
            }
        });

        // Nút Tìm kiếm
        btnTimkiem.addActionListener(e -> {
            loadDataToTable_KhuyenMai(thucHienLoc());
        });

        // Reset bộ lọc (Nút Thoát/Làm mới)
        btnThoat.addActionListener(e -> {
            cmbTimTheo.setSelectedIndex(0);
            cmbLocLoaiKhuyenMai.setSelectedIndex(0);
            cmbLocTrangThai.setSelectedIndex(0);
            cmbLocDoiTuongApDung.setSelectedIndex(0);
            cmbLocChuongTrinhKhuyenMai.setSelectedIndex(0);
            txtNhapThongTin.setText("");
            loadDataToTable_KhuyenMai(kmBUS.getAll());
        });

        btnHuy.addActionListener(e -> {
            setViewMode();
            clearForm();
            // Nếu hủy khi đang chọn dòng, fill lại data cũ
            int row = tableKhuyenMai.getSelectedRow();
            if (row >= 0) fillDataFromTable(row);
        });

        // 1. Click vào bảng
        tableKhuyenMai.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isAdding || isUpdating) return; // Đang nhập liệu thì không cho click dòng khác

                int row = tableKhuyenMai.getSelectedRow();
                if (row >= 0) {
                    // Chỉ gọi hàm này là đủ, không cần viết logic format giá trị ở đây nữa
                    fillDataFromTable(row);
                }
            }
        });

        // 2. Thay đổi Đối tượng áp dụng (Danh mục <-> Sản phẩm)
        if (cmbDoiTuongApDung != null) {
            cmbDoiTuongApDung.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (cmbDoiTuongApDung.getSelectedIndex() == 0) { // Chọn Danh mục
                        if (cmbApDungDanhMuc != null) cmbApDungDanhMuc.setEnabled(true);
                        if (cmbApDungSanPham != null) {
                            cmbApDungSanPham.setEnabled(false);
                            cmbApDungSanPham.setSelectedIndex(-1); // Reset
                        }
                    } else { // Chọn Sản phẩm
                        if (cmbApDungDanhMuc != null) {
                            cmbApDungDanhMuc.setEnabled(false);
                            cmbApDungDanhMuc.setSelectedIndex(-1); // Reset
                        }
                        if (cmbApDungSanPham != null) cmbApDungSanPham.setEnabled(true);
                    }
                }
            });
            // Tự động lọc khi thay đổi ComboBox bộ lọc
            ActionListener al = e -> loadDataToTable_KhuyenMai(thucHienLoc());
            cmbLocLoaiKhuyenMai.addActionListener(al);
            cmbLocTrangThai.addActionListener(al);
            cmbLocDoiTuongApDung.addActionListener(al);
            cmbLocChuongTrinhKhuyenMai.addActionListener(al);
        }

    }

    private void setViewMode() {
        isAdding = isUpdating = false;

        // Khóa tất cả các ô nhập liệu
        txtMaKhuyenMai.setEditable(false);
        txtTenKhuyenMai.setEditable(false);
        txtGiaTriKhuyenMai.setEditable(false);
        txtSoLuotSuDung.setEditable(false);

        cmbLoaiKhuyenMai.setEnabled(false);
        cmbTrangThai.setEnabled(false);
        cmbDoiTuongApDung.setEnabled(false);
        cmbChuongTrinhKhuyenMai.setEnabled(false);
        cmbApDungDanhMuc.setEnabled(false);
        cmbApDungSanPham.setEnabled(false);

        // Điều khiển nút bấm
        btnLuu.setVisible(false);
        btnHuy.setVisible(false);
        btnThem.setEnabled(true);
        btnCapNhat.setEnabled(true);
        tableKhuyenMai.setEnabled(true);
    }

    private void setAddMode() {
        isAdding = true;
        isUpdating = false;
        clearForm(); // Xóa sạch form trước khi thêm

        // Tự động sinh mã và khóa lại
        txtMaKhuyenMai.setText(kmBUS.getNextId());
        txtMaKhuyenMai.setEditable(false);

        // Cho phép nhập các thông tin khác
        txtTenKhuyenMai.setEditable(true);
        txtGiaTriKhuyenMai.setEditable(true);

        // THÊM: Cho phép nhập số lượt sử dụng khi tạo mới
        txtSoLuotSuDung.setEditable(true);
        txtSoLuotSuDung.setText("1"); // Mặc định là 1

        cmbLoaiKhuyenMai.setEnabled(true);
        cmbTrangThai.setSelectedIndex(0); // Mặc định Đang áp dụng
        cmbTrangThai.setEnabled(false);  // Khi thêm mới thường mặc định bật luôn
        cmbDoiTuongApDung.setEnabled(true);
        cmbChuongTrinhKhuyenMai.setEnabled(true);

        boolean isDM = cmbDoiTuongApDung.getSelectedIndex() == 0;
        cmbApDungDanhMuc.setEnabled(isDM);
        cmbApDungSanPham.setEnabled(!isDM);
        if (isDM) cmbApDungSanPham.setSelectedIndex(-1);
        else cmbApDungDanhMuc.setSelectedIndex(-1);

        btnLuu.setVisible(true);
        btnHuy.setVisible(true);
        btnThem.setEnabled(false);
        btnCapNhat.setEnabled(false);
        tableKhuyenMai.setEnabled(false); // Khóa bảng khi đang thêm
    }

    private void setUpdateMode() {
        if (tableKhuyenMai.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần cập nhật!");
            return;
        }
        isAdding = false;
        isUpdating = true;

        txtMaKhuyenMai.setEditable(false); // Không sửa mã
        txtTenKhuyenMai.setEditable(true);
        txtGiaTriKhuyenMai.setEditable(true);

        // YÊU CẦU: Cập nhật không được sửa số lượt sử dụng
        txtSoLuotSuDung.setEditable(false);

        cmbLoaiKhuyenMai.setEnabled(true);
        cmbTrangThai.setEnabled(true); // Cho phép tắt/mở trạng thái
        cmbDoiTuongApDung.setEnabled(true);
        cmbChuongTrinhKhuyenMai.setEnabled(true);

        boolean isDM = cmbDoiTuongApDung.getSelectedIndex() == 0;
        cmbApDungDanhMuc.setEnabled(isDM);
        cmbApDungSanPham.setEnabled(!isDM);
        if (isDM) cmbApDungSanPham.setSelectedIndex(-1);
        else cmbApDungDanhMuc.setSelectedIndex(-1);

        btnLuu.setVisible(true);
        btnHuy.setVisible(true);
        btnThem.setEnabled(false);
        btnCapNhat.setEnabled(false);
        tableKhuyenMai.setEnabled(false);
    }

    private void clearForm() {
        txtMaKhuyenMai.setText("");
        txtTenKhuyenMai.setText("");
        txtGiaTriKhuyenMai.setText("");
        txtSoLuotSuDung.setText("");
        cmbLoaiKhuyenMai.setSelectedIndex(0);
        cmbDoiTuongApDung.setSelectedIndex(0);
        cmbTrangThai.setSelectedIndex(0);
        cmbApDungDanhMuc.setSelectedIndex(-1);
        cmbApDungSanPham.setSelectedIndex(-1);
    }

    private void setSelectedComboBoxItem(JComboBox<String> comboBox, String codeToFind) {
        if(codeToFind == null) { comboBox.setSelectedIndex(-1); return; }
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).startsWith(codeToFind + " -")) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
    }
}