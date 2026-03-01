package gui;

import bus.ChuongTrinhKM_BUS;
import bus.KhuyenMai_BUS;
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
    private JLabel labelLocTrangThai;
    private JComboBox<String> cmbLocTrangThai;
    private JPanel panelQuanLyChuongTrinhKhuyenMai;
    private JLabel labelLocNgayBatDau;
    private JLabel labelLocNgayKetThuc;
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
    private JTextField txtLocNgayBatDau;
    private JTextField txtLocNgayKetThuc;
    private JLabel labelMoTa;
    private JTextField txtMoTa;
    // ------------------------------------------

    private DefaultTableModel modelChuongTrinhKhuyenMai;
    private DefaultTableModel modelKhuyenMai;

    // KHAI BÁO BUS
    private final ChuongTrinhKM_BUS ctkmBUS = ChuongTrinhKM_BUS.getInstance();
    private final KhuyenMai_BUS kmBUS = KhuyenMai_BUS.getInstance();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public QuanLyChuongTrinhKM_GUI(){
        this.setLayout(new BorderLayout());
        if (panelQuanLyChuongTrinhKhuyenMai != null) {
            this.add(panelQuanLyChuongTrinhKhuyenMai, BorderLayout.CENTER);
        }

        initTable_ChuongTrinhKhuyenMai();
        initTable_KhuyenMai();
        initComboBox();

        // Đổ dữ liệu ban đầu lên bảng cha
        loadDataToTable_CTKM();

        // Gắn sự kiện (Click chuột, Tìm kiếm...)
        addEvents();
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
        // Cấu hình ComboBox nhập liệu
        if(cmbTrangThai != null) {
            cmbTrangThai.removeAllItems();
            cmbTrangThai.addItem("Đang áp dụng");
            cmbTrangThai.addItem("Ngưng áp dụng");
        }

        // Cấu hình ComboBox bộ lọc
        if(cmbLocTrangThai != null) {
            cmbLocTrangThai.removeAllItems();
            cmbLocTrangThai.addItem("Tất cả");
            cmbLocTrangThai.addItem("Đang áp dụng");
            cmbLocTrangThai.addItem("Ngưng áp dụng");
        }
    }

    // ==========================================================
    // HÀM 1: ĐỔ DỮ LIỆU LÊN BẢNG CHƯƠNG TRÌNH (BẢNG TRÊN)
    // ==========================================================
    private void loadDataToTable_CTKM() {
        modelChuongTrinhKhuyenMai.setRowCount(0);
        ArrayList<ChuongTrinhKM_DTO> list = ctkmBUS.getAll();

        int stt = 1;
        for (ChuongTrinhKM_DTO ct : list) {
            String trangThai = (ct.getTrangThai() == 1) ? "Đang áp dụng" : "Ngưng áp dụng";

            modelChuongTrinhKhuyenMai.addRow(new Object[]{
                    stt++,
                    ct.getMa(),
                    ct.getTen(),
                    (ct.getNgayBatDau() != null) ? sdf.format(ct.getNgayBatDau()) : "",
                    (ct.getNgayKetThuc() != null) ? sdf.format(ct.getNgayKetThuc()) : "",
                    trangThai
            });
        }

        if(txtChuongTrinhKhuyenMaiHienCo != null) {
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

                        // Đổ ngày tháng (dùng định dạng dd/MM/yyyy)
                        if(txtNgayBatDau != null)
                            txtNgayBatDau.setText(ctkmFull.getNgayBatDau() != null ? sdf.format(ctkmFull.getNgayBatDau()) : "");
                        if(txtNgayKetThuc != null)
                            txtNgayKetThuc.setText(ctkmFull.getNgayKetThuc() != null ? sdf.format(ctkmFull.getNgayKetThuc()) : "");

                        // --- ĐỔ DỮ LIỆU MÔ TẢ ---
                        if(txtMoTa != null) txtMoTa.setText(ctkmFull.getMoTa());

                        // Đổ trạng thái
                        if(cmbTrangThai != null) {
                            String trangThaiStr = (ctkmFull.getTrangThai() == 1) ? "Đang áp dụng" : "Ngưng áp dụng";
                            cmbTrangThai.setSelectedItem(trangThaiStr);
                        }

                        // 4. Load bảng con khuyến mãi chi tiết
                        loadDataToTable_KhuyenMai(maCT);

                        // Khóa ô mã không cho sửa khi đang chọn một dòng có sẵn
                        if(txtMaChuongTrinhKhuyenMai != null) txtMaChuongTrinhKhuyenMai.setEditable(false);
                    }
                }
            }
        });

        // Sự kiện nút Thoát
        if (btnThoat != null) {
            btnThoat.addActionListener(e -> {
                // Thêm logic đóng tab hoặc thoát ở đây
                setVisible(false);
            });
        }
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
}