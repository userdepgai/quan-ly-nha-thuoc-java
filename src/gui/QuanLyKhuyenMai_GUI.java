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
            String ttHienThi = km.getTrangThaiText(); // Lấy trực tiếp từ con
            boolean matchTrangThai = locTrangThai.equals("Tất cả") || ttHienThi.equals(locTrangThai);

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

        // 1. Lấy tiêu chí tìm kiếm hiện tại từ ComboBox bộ lọc
        String timTheo = (String) cmbTimTheo.getSelectedItem();

        int itemsToShow = Math.min(list.size(), 5); // Hiện tối đa 5 gợi ý
        for (int i = 0; i < itemsToShow; i++) {
            KhuyenMai_DTO km = list.get(i);
            String textHienThi = km.getMaKM() + " - " + km.getTenKM();
            JMenuItem item = new JMenuItem(textHienThi);
            item.setPreferredSize(new Dimension(txtNhapThongTin.getWidth(), 30));

            item.addActionListener(e -> {
                // 2. KIỂM TRA LOGIC ĐỂ ĐIỀN TEXT
                if ("Tên khuyến mãi".equals(timTheo)) {
                    txtNhapThongTin.setText(km.getTenKM()); // Điền Tên nếu đang tìm theo tên
                } else {
                    txtNhapThongTin.setText(km.getMaKM());  // Điền Mã nếu tìm theo mã hoặc tất cả
                }

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

            // Dùng hàm trạng thái thực tế vừa viết trong DTO
            String trangThaiHienThi = km.getTrangThaiThucTe(ct);

            String loaiKMStr = km.getLoaiKMText();
            String doiTuongStr = km.getDoiTuongText();

            double giaTriGoc = km.getGiaTriKhuyenMai();
            String giaTriStr = (km.getLoaiKhuyenMai() == KhuyenMai_DTO.LOAI_PHAN_TRAM)
                    ? String.valueOf(giaTriGoc) : df.format(giaTriGoc) + " VNĐ";

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
        txtGiaTriKhuyenMai.setText(String.valueOf(km.getGiaTriKhuyenMai()));
        cmbLoaiKhuyenMai.setSelectedItem(km.getLoaiKMText());

        // QUAN TRỌNG: Hiển thị trạng thái "GỐC" của chính nó lên Combo
        // Để Admin có thể sửa thành "Đang áp dụng" kể cả khi cha đang tắt (đợi cha bật lên là nó tự chạy)
        cmbTrangThai.setSelectedItem(km.getTrangThaiText());

        cmbDoiTuongApDung.setSelectedItem(km.getDoiTuongText());
        setSelectedComboBoxItem(cmbChuongTrinhKhuyenMai, km.getMaChuongTrinh());

        // Hiển thị số lượt tối đa
        int soLuotMax = bus.KhachHang_KM_BUS.getInstance().getSoLuotToiDa(km.getMaKM(), "KH000001");
        txtSoLuotSuDung.setText(String.valueOf(soLuotMax));

        // Thiết lập trạng thái hiển thị dựa trên dữ liệu của dòng vừa click
        if (km.getDoiTuongApDung() == KhuyenMai_DTO.DT_DANH_MUC) {
            setSelectedComboBoxItem(cmbApDungDanhMuc, km.getMaDanhMuc());
            cmbApDungDanhMuc.setEnabled(true);
            cmbApDungSanPham.setEnabled(false);
            cmbApDungSanPham.setSelectedIndex(-1);
        } else {
            setSelectedComboBoxItem(cmbApDungSanPham, km.getMaSanPham());
            cmbApDungSanPham.setEnabled(true);
            cmbApDungDanhMuc.setEnabled(false);
            cmbApDungDanhMuc.setSelectedIndex(-1);
        }
    }

    private void saveKhuyenMai() {
        try {
            // 1. Lấy dữ liệu cơ bản
            String ma = txtMaKhuyenMai.getText().trim();
            String ten = txtTenKhuyenMai.getText().trim();
            double giaTri = Double.parseDouble(txtGiaTriKhuyenMai.getText().trim());

            // 2. Chuyển đổi ComboBox sang kiểu int (Dùng các hàm parse của DTO)
            String selectedLoai = (String) cmbLoaiKhuyenMai.getSelectedItem();
            int loai = selectedLoai.equals(KhuyenMai_DTO.PHAN_TRAM) ? KhuyenMai_DTO.LOAI_PHAN_TRAM : KhuyenMai_DTO.LOAI_TIEN_MAT;

            String textTrangThai = (String) cmbTrangThai.getSelectedItem();
            int trangThai = KhuyenMai_DTO.parseTrangThaiFromText(textTrangThai);
            String maCT = cmbChuongTrinhKhuyenMai.getSelectedItem().toString().split(" - ")[0];

            String selectedDoiTuong = (String) cmbDoiTuongApDung.getSelectedItem();
            int doiTuong = selectedDoiTuong.equals(KhuyenMai_DTO.SAN_PHAM) ? KhuyenMai_DTO.DT_SAN_PHAM : KhuyenMai_DTO.DT_DANH_MUC;

            // 3. Xử lý lấy mã CT, SP, DM (Có kiểm tra null)
            Object itemCT = cmbChuongTrinhKhuyenMai.getSelectedItem();
            if (itemCT == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn Chương trình khuyến mãi!");
                return;
            }

            String maSP = (doiTuong == KhuyenMai_DTO.DT_SAN_PHAM && cmbApDungSanPham.getSelectedItem() != null)
                    ? cmbApDungSanPham.getSelectedItem().toString().split(" - ")[0] : null;

            String maDM = (doiTuong == KhuyenMai_DTO.DT_DANH_MUC && cmbApDungDanhMuc.getSelectedItem() != null)
                    ? cmbApDungDanhMuc.getSelectedItem().toString().split(" - ")[0] : null;

            // 4. Tạo DTO với thứ tự tham số đúng như Constructor đã sửa
            // Thứ tự: ma, ten, loai, giaTri, trangThai, doiTuong, maCT, maSP, maDM
            KhuyenMai_DTO km = new KhuyenMai_DTO(ma, ten, loai, giaTri, trangThai, doiTuong, maCT, maSP, maDM);

            // 5. Gọi BUS thực hiện
            boolean success;
            if (isAdding) {
                int soLuot = Integer.parseInt(txtSoLuotSuDung.getText().trim());
                success = kmBUS.them(km, soLuot);
            } else {
                success = kmBUS.capNhat(km);
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Lưu thành công!");
                loadDataToTable_KhuyenMai(kmBUS.getAll());
                setViewMode();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Lưu thất bại!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá trị khuyến mãi hoặc số lượt không hợp lệ!");
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
        // Khóa tất cả
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

        btnLuu.setVisible(false);
        btnHuy.setVisible(false);
        btnThem.setEnabled(true);
        btnCapNhat.setEnabled(true);
        tableKhuyenMai.setEnabled(true);
    }

    private void setAddMode() {
        isAdding = true; isUpdating = false;
        clearForm();
        txtMaKhuyenMai.setText(kmBUS.getNextId());
        txtTenKhuyenMai.setEditable(true);
        txtGiaTriKhuyenMai.setEditable(true);

        txtSoLuotSuDung.setEditable(true);
        txtSoLuotSuDung.setText("1");

        cmbLoaiKhuyenMai.setEnabled(true);
        cmbTrangThai.setSelectedIndex(0);
        cmbTrangThai.setEnabled(true);
        cmbDoiTuongApDung.setEnabled(true);
        cmbChuongTrinhKhuyenMai.setEnabled(true);
        boolean checkDM = cmbDoiTuongApDung.getSelectedIndex() == 0;
        cmbApDungDanhMuc.setEnabled(checkDM);
        cmbApDungSanPham.setEnabled(!checkDM);

        btnLuu.setVisible(true);
        btnHuy.setVisible(true);
        btnThem.setEnabled(false);
        btnCapNhat.setEnabled(false);
        tableKhuyenMai.setEnabled(false);
    }

    private void setUpdateMode() {
        if (tableKhuyenMai.getSelectedRow() < 0) return;
        isAdding = false; isUpdating = true;

        txtTenKhuyenMai.setEditable(true);
        txtGiaTriKhuyenMai.setEditable(true);

        // KHÓA Ô SỐ LƯỢT KHI CẬP NHẬT (Dữ liệu đã xuống SQL)
        txtSoLuotSuDung.setEditable(false);

        cmbLoaiKhuyenMai.setEnabled(true);
        cmbTrangThai.setEnabled(true);
        cmbDoiTuongApDung.setEnabled(true);
        cmbChuongTrinhKhuyenMai.setEnabled(true);
        boolean checkDM = cmbDoiTuongApDung.getSelectedIndex() == 0;
        cmbApDungDanhMuc.setEnabled(checkDM);
        cmbApDungSanPham.setEnabled(!checkDM);

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