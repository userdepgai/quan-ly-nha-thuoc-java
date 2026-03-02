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
    private DefaultTableModel modelKhuyenMai;

    // --- BUS ---
    private final KhuyenMai_BUS kmBUS = KhuyenMai_BUS.getInstance();
    private final ChuongTrinhKM_BUS ctkmBUS = ChuongTrinhKM_BUS.getInstance();
    private final DanhMuc_BUS dmBUS = DanhMuc_BUS.getInstance();
    private final SanPham_BUS spBUS = SanPham_BUS.getInstance();

    private final DecimalFormat df = new DecimalFormat("#,###");

    public QuanLyKhuyenMai_GUI(){
        this.setLayout(new BorderLayout());
        if (panelQuanLyKhuyenMai != null) {
            this.add(panelQuanLyKhuyenMai, BorderLayout.CENTER);
        }

        initTable_KhuyenMai();
        initComboBoxData();

        loadDataToTable_KhuyenMai();
        addEvents();
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
        if (cmbLoaiKhuyenMai != null) {
            cmbLoaiKhuyenMai.removeAllItems();
            cmbLoaiKhuyenMai.addItem("Phần trăm");
            cmbLoaiKhuyenMai.addItem("Tiền mặt");
        }

        // 2. Trạng thái
        if (cmbTrangThai != null) {
            cmbTrangThai.removeAllItems();
            cmbTrangThai.addItem("Đang áp dụng");
            cmbTrangThai.addItem("Ngưng áp dụng");
        }

        // 3. Đối tượng áp dụng
        if (cmbDoiTuongApDung != null) {
            cmbDoiTuongApDung.removeAllItems();
            cmbDoiTuongApDung.addItem("Danh mục");
            cmbDoiTuongApDung.addItem("Sản phẩm");
        }

        // 4. Chương trình KM
        if (cmbChuongTrinhKhuyenMai != null) {
            cmbChuongTrinhKhuyenMai.removeAllItems();
            for (ChuongTrinhKM_DTO ct : ctkmBUS.getAll()) {
                cmbChuongTrinhKhuyenMai.addItem(ct.getMa() + " - " + ct.getTen());
            }
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
    }

    private void loadDataToTable_KhuyenMai() {
        modelKhuyenMai.setRowCount(0);
        ArrayList<KhuyenMai_DTO> list = kmBUS.getAll();

        int stt = 1;
        for (KhuyenMai_DTO km : list) {
            String loaiKM = (km.getLoaiKhuyenMai() == 0) ? "Phần trăm" : "Tiền mặt";
            String trangThai = (km.getTrangThai() == 1) ? "Đang áp dụng" : "Ngưng áp dụng";

            // --- XỬ LÝ HIỂN THỊ GIÁ TRỊ (Nhân 100 nếu là phần trăm) ---
            double giaTriGoc = km.getGiaTriKhuyenMai();
            String giaTriStr = "";
            if (km.getLoaiKhuyenMai() == 0) {
                giaTriStr = df.format(giaTriGoc * 100) + "%"; // 0.1 -> 10%
            } else {
                giaTriStr = df.format(giaTriGoc) + " VNĐ";
            }

            String doiTuong = "";
            String chiTietApDung = "";

            if (km.getDoiTuongApDung() == 0) {
                doiTuong = "Danh mục";
                // Lấy tên để hiển thị cho đẹp
                DanhMuc_DTO dm = dmBUS.getById(km.getMaDanhMuc());
                chiTietApDung = (dm != null) ? dm.getTenDM() : km.getMaDanhMuc();
            } else {
                doiTuong = "Sản phẩm";
                SanPham_DTO sp = spBUS.getById(km.getMaSanPham());
                chiTietApDung = (sp != null) ? sp.getTenSP() : km.getMaSanPham();
            }

            ChuongTrinhKM_DTO ct = ctkmBUS.getById(km.getMaChuongTrinh());
            String tenCT = (ct != null) ? ct.getTen() : km.getMaChuongTrinh();

            modelKhuyenMai.addRow(new Object[]{
                    stt++, km.getMaKM(), km.getTenKM(), loaiKM, giaTriStr,
                    doiTuong, chiTietApDung, tenCT, trangThai
            });
        }

        if (txtKhuyenMaiHienCo != null) {
            txtKhuyenMaiHienCo.setText(String.valueOf(list.size()));
        }
    }

    private void addEvents() {
        // 1. Click vào bảng
        tableKhuyenMai.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableKhuyenMai.getSelectedRow();
                if (row >= 0) {
                    String maKM = modelKhuyenMai.getValueAt(row, 1).toString();
                    KhuyenMai_DTO km = kmBUS.getById(maKM);

                    if (km != null) {
                        if (txtMaKhuyenMai != null) txtMaKhuyenMai.setText(km.getMaKM());
                        if (txtTenKhuyenMai != null) txtTenKhuyenMai.setText(km.getTenKM());

                        // --- ĐÃ SỬA: XỬ LÝ HIỂN THỊ GIÁ TRỊ LÊN FORM ---
                        if (txtGiaTriKhuyenMai != null) {
                            if (km.getLoaiKhuyenMai() == 0) {
                                // Nếu là phần trăm (VD: lưu trong DB là 0.1) -> x100 để hiển thị '10'
                                double phanTram = km.getGiaTriKhuyenMai() * 100;
                                // Dùng kiểu nguyên (long) nếu số phần trăm là số chẵn, cho đẹp mắt
                                if (phanTram == (long) phanTram) {
                                    txtGiaTriKhuyenMai.setText(String.format("%d", (long) phanTram));
                                } else {
                                    txtGiaTriKhuyenMai.setText(String.valueOf(phanTram));
                                }
                            } else {
                                // Nếu là tiền mặt -> Hiển thị nguyên số không phần thập phân
                                txtGiaTriKhuyenMai.setText(String.format("%.0f", km.getGiaTriKhuyenMai()));
                            }
                        }

                        if (cmbLoaiKhuyenMai != null) {
                            // 0: Phần trăm, 1: Tiền mặt
                            cmbLoaiKhuyenMai.setSelectedIndex(km.getLoaiKhuyenMai() == 0 ? 0 : 1);
                        }
                        if (cmbTrangThai != null) {
                            // 1: Hoạt động, 0: Ngưng hoạt động
                            cmbTrangThai.setSelectedIndex(km.getTrangThai() == 1 ? 0 : 1);
                        }

                        // Chọn đúng Chương trình trong ComboBox
                        if (cmbChuongTrinhKhuyenMai != null) {
                            setSelectedComboBoxItem(cmbChuongTrinhKhuyenMai, km.getMaChuongTrinh());
                        }

                        // Xử lý ComboBox Đối tượng
                        if (cmbDoiTuongApDung != null) {
                            if (km.getDoiTuongApDung() == 0) {
                                // Chọn Danh mục
                                cmbDoiTuongApDung.setSelectedIndex(0);

                                // Mở ComboBox Danh mục, chọn đúng mã
                                if (cmbApDungDanhMuc != null) {
                                    cmbApDungDanhMuc.setEnabled(true);
                                    setSelectedComboBoxItem(cmbApDungDanhMuc, km.getMaDanhMuc());
                                }

                                // Khóa ComboBox Sản phẩm
                                if (cmbApDungSanPham != null) {
                                    cmbApDungSanPham.setSelectedIndex(-1);
                                    cmbApDungSanPham.setEnabled(false);
                                }
                            } else {
                                // Chọn Sản phẩm
                                cmbDoiTuongApDung.setSelectedIndex(1);

                                // Khóa Danh mục
                                if (cmbApDungDanhMuc != null) {
                                    cmbApDungDanhMuc.setSelectedIndex(-1);
                                    cmbApDungDanhMuc.setEnabled(false);
                                }

                                // Mở Sản phẩm, chọn đúng mã
                                if (cmbApDungSanPham != null) {
                                    cmbApDungSanPham.setEnabled(true);
                                    setSelectedComboBoxItem(cmbApDungSanPham, km.getMaSanPham());
                                }
                            }
                        }
                    }
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
        }
    }

    // Hàm phụ: Tìm và chọn item trong ComboBox khi biết Mã
    private void setSelectedComboBoxItem(JComboBox<String> comboBox, String codeToFind) {
        if(codeToFind == null) return;
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            String item = comboBox.getItemAt(i);
            // So sánh "DM001" với "DM001 - Tên..."
            if (item.startsWith(codeToFind + " -")) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
    }
}