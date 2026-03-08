package gui.menu;

import javax.swing.*;
import java.awt.*;

import bus.TaiKhoan_BUS;
import dto.MenuItem;
import gui.*;
import gui.HOADON_GUI.DuyetDonHang_GUI;
import gui.HOADON_GUI.LapHoaDon_GUI;
import gui.HOADON_GUI.QuanLyHoaDonBan_GUI;
import gui.HOADON_GUI.XuatHoaDon_GUI;
import gui.NCCKVLT.KVLT;
import gui.NCCKVLT.NCC;
import gui.THONGKEBAOCAO.BAOCAODOANHTHU;
import gui.THONGKEBAOCAO.THONGKEDOANHTHU;
import gui.THONGKEBAOCAO.THONGKEKHACHHANG;
import gui.THONGKEBAOCAO.BAOCAOTK;
import utils.Session;

public class Menu extends JFrame {
    private JList<MenuItem> menuList;
    private DefaultListModel<MenuItem> menuModel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private TaiKhoan_BUS taiKhoanBus = TaiKhoan_BUS.getInstance();

    public Menu() {
        setTitle("Admin Dashboard");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {

        menuModel = new DefaultListModel<>();

        if(Session.isLoggedIn()) {
            menuModel.addElement(new MenuItem(
                    getNameUser(Session.getCurrentUser().getSdt()),
                    "thongTinCaNhanFrame",
                    icon("account.png"),
                    false
            ));
        }

        /* ================= TỔNG QUAN ================= */

        if(hasAny("DASHBOARD","THONGKE","BAOCAO")) {

            menuModel.addElement(new MenuItem("TỔNG QUAN & ĐIỀU HÀNH", null, null, true));

            addMenu("Dashboard","dashboard","dashboard.png","DASHBOARD");
            addMenu("Thống kê","thongke","thongKe.png","THONGKE");
            addMenu("Báo cáo","baocao","baoCao.png","BAOCAO");
        }

        /* ================= BÁN HÀNG ================= */

        if(hasAny("BANHANG","DUYETHD","HOADON")) {

<<<<<<< HEAD
            menuModel.addElement(new MenuItem("BÁN HÀNG & HÓA ĐƠN", null, null, true));

            addMenu("Bán hàng","banhang","banHang.png","BANHANG");
            addMenu("Duyệt hóa đơn online","duyethd","duyetHoaDon.png","DUYETHD");
            addMenu("Quản lý hóa đơn","hoadon","hoaDon.png","HOADON");
        }
=======
        menuModel.addElement(new MenuItem("HỆ THỐNG", null, null,true));
        menuModel.addElement(new MenuItem("Quản lý tài khoản", "taiKhoanDangNhap", icon("taiKhoanDangNhap.png"),false));
        menuModel.addElement(new MenuItem("Phân quyền", "phanQuyen", icon("phanQuyen.png"),false));
        menuModel.addElement(new MenuItem("Đăng xuất", "dangXuat", icon("logout.png"), false));
>>>>>>> ThanhTuyen

        /* ================= CON NGƯỜI ================= */

        if(hasAny("KHACHHANG","NHANVIEN","NHACUNGCAP")) {

            menuModel.addElement(new MenuItem("QUẢN LÝ CON NGƯỜI", null, null, true));

            addMenu("Khách hàng","khachhang","khachHang.png","KHACHHANG");
            addMenu("Nhân viên","nhanvien","nhanVien.png","NHANVIEN");
            addMenu("Nhà cung cấp","nhacungcap","nhaCungCap.png","NHACUNGCAP");
        }

        /* ================= DANH MỤC ================= */

        if(hasAny("DANHMUC","THUOCTINH","SANPHAM")) {

            menuModel.addElement(new MenuItem("DANH MỤC & SẢN PHẨM", null, null, true));

            addMenu("Danh mục","danhMuc","danhMuc.png","DANHMUC");
            addMenu("Thuộc tính","thuocTinhDanhMuc","thuocTinhDanhMuc.png","THUOCTINH");
            addMenu("Quản lý sản phẩm","sanPham","sanPham.png","SANPHAM");
        }

        /* ================= KHO ================= */

        if(hasAny("LUUTRU","LOHANG","PHIEUNHAP")) {

            menuModel.addElement(new MenuItem("KHO & LƯU TRỮ", null, null, true));

            addMenu("Khu vực lưu trữ","luutru","khuVucLuuTru.png","LUUTRU");
            addMenu("Lô hàng","lohang","loHang.png","LOHANG");
            addMenu("Phiếu nhập","phieunhap","phieuNhap.png","PHIEUNHAP");
        }

        /* ================= KHUYẾN MÃI ================= */

        if(hasAny("CHUONGTRINHKM","KHUYENMAI","VOUCHER")) {

            menuModel.addElement(new MenuItem("KHUYẾN MÃI", null, null, true));

            addMenu("Chương trình","chuongTrinhKhuyenMai","chuongTrinhKhuyenMai.png","CHUONGTRINHKM");
            addMenu("Khuyến mãi","khuyenMai","khuyenMai.png","KHUYENMAI");
            addMenu("Voucher","voucher","voucher.png","VOUCHER");
        }

        /* ================= HỆ THỐNG ================= */

        if(hasAny("TAIKHOAN","PHANQUYEN")) {

            menuModel.addElement(new MenuItem("HỆ THỐNG", null, null, true));

            addMenu("Quản lý tài khoản","taiKhoanDangNhap","taiKhoanDangNhap.png","TAIKHOAN");
            addMenu("Phân quyền","phanQuyen","phanQuyen.png","PHANQUYEN");
        }

        menuModel.addElement(new MenuItem("Đăng xuất","dangXuat",icon("logout.png"),false));
        menuList = new JList<>(menuModel);
        menuList.setCellRenderer(new SidebarRenderer());
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuList.setFixedCellHeight(45);
        JPopupMenu popupThongKe = createSubMenu(new String[][]{
                {"Thống kê Doanh thu", "thongke_doanhthu"},
                {"Thống kê Khách hàng VIP", "thongke_khachhang"}
        });

        JPopupMenu popupBaoCao = createSubMenu(new String[][]{
                {"Báo cáo Tồn kho", "baocao_tonkho"},
                {"Báo cáo Doanh thu", "baocao_doanhthu"}
        });

        menuList.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int index = menuList.locationToIndex(e.getPoint());
                if (index > -1) {
                    MenuItem item = menuModel.getElementAt(index);
                    Rectangle cellBounds = menuList.getCellBounds(index, index);

                    if ("thongke".equals(item.cardName)) {
                        popupBaoCao.setVisible(false);
                        if (!popupThongKe.isVisible()) {
                            popupThongKe.show(menuList, cellBounds.width, cellBounds.y);
                        }
                    } else if ("baocao".equals(item.cardName)) {
                        popupThongKe.setVisible(false);
                        if (!popupBaoCao.isVisible()) {
                            popupBaoCao.show(menuList, cellBounds.width, cellBounds.y);
                        }
                    } else {
                        popupThongKe.setVisible(false);
                        popupBaoCao.setVisible(false);
                    }
                } else {
                    popupThongKe.setVisible(false);
                    popupBaoCao.setVisible(false);
                }
            }
        });
        JScrollPane sidebarScroll = new JScrollPane(menuList);
        sidebarScroll.setPreferredSize(new Dimension(200, 0));
        sidebarScroll.setMinimumSize(new Dimension(100, 30));
        sidebarScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        sidebarScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScroll.getVerticalScrollBar().setUnitIncrement(5);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(new DashBoard_GUI(), "dashboard");
        contentPanel.add(new THONGKEDOANHTHU(), "thongke_doanhthu");
        contentPanel.add(new THONGKEKHACHHANG(), "thongke_khachhang");
        contentPanel.add(new BAOCAOTK(), "baocao_tonkho");
        contentPanel.add(new BAOCAODOANHTHU(), "baocao_doanhthu");


        QuanLyHoaDonBan_GUI qlHoaDonGUI = new QuanLyHoaDonBan_GUI();
        LapHoaDon_GUI lapHoaDonGUI = new LapHoaDon_GUI();
        DuyetDonHang_GUI duyetDonGUI = new DuyetDonHang_GUI();
        lapHoaDonGUI.setOnHoaDonSaved(() -> {
            qlHoaDonGUI.reloadDanhSach();
        });
        duyetDonGUI.setOnTrangThaiChanged(() -> {
            qlHoaDonGUI.reloadDanhSach();
        });

        contentPanel.add(lapHoaDonGUI, "banhang");
        contentPanel.add(duyetDonGUI, "duyethd");
        contentPanel.add(qlHoaDonGUI, "hoadon");

        contentPanel.add(new DanhMuc_GUI(), "danhMuc");
        contentPanel.add(new ThuocTinh_GUI(), "thuocTinhDanhMuc");
        contentPanel.add(new QuanLySanPham_GUI(), "sanPham");

        contentPanel.add(new KhachHang_GUI(), "khachhang");
        contentPanel.add(new NhanVien_GUI(), "nhanvien");
        contentPanel.add(new NCC(), "nhacungcap");

        contentPanel.add(new KVLT(), "luutru");
        contentPanel.add(new LoHang_GUI(), "lohang");
        contentPanel.add(new PhieuNhap_GUI(), "phieunhap");

        contentPanel.add(new QuanLyChuongTrinhKM_GUI(), "chuongTrinhKhuyenMai");
        contentPanel.add(new QuanLyKhuyenMai_GUI(), "khuyenMai");
        contentPanel.add(new QuanLyVoucher_GUI(), "voucher");

        contentPanel.add(new TaiKhoan_GUI(), "taiKhoanDangNhap");
        contentPanel.add(new PhanQuyen_GUI(), "phanQuyen");

        menuList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                MenuItem item = menuList.getSelectedValue();
                if (item != null && !item.isGroup) {
                    if ("dangXuat".equals(item.cardName)) {
                        int confirm = JOptionPane.showConfirmDialog(
                                this,
                                "Bạn có chắc muốn đăng xuất?",
                                "Xác nhận",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (confirm == JOptionPane.YES_OPTION) {
                            dispose();
                            new DangNhapGUI().setVisible(true);
                        }
                        Session.clear();
                        return;
                    }
                    if ("thongTinCaNhanFrame".equals(item.cardName)) {
                        new MenuThongTinCaNhan_GUI().setVisible(true);
                        dispose();
                    } else {
                        cardLayout.show(contentPanel, item.cardName);
                    }
                }
            }
        });

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                sidebarScroll,
                contentPanel
        );
        splitPane.setDividerLocation(200);
        splitPane.setDividerSize(2);

        add(splitPane);
        menuList.setSelectedIndex(2);
    }

    private JPanel createContent(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 28));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private String getNameUser(String sdt) {
        return taiKhoanBus.getNameNhanVien(sdt);
    }
    private Icon icon(String name) {
        java.net.URL imgURL = getClass().getResource("/icons/" + name);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Lỗi: Không tìm thấy ảnh tại /icons/" + name);
            return null;
        }
    }
<<<<<<< HEAD
    private void addMenu(String title, String card, String icon, String function) {
        if (Session.hasFunction(function)) {
            menuModel.addElement(new MenuItem(title, card, icon(icon), false));
        }
    }
    private boolean hasAny(String... functions) {
        for (String f : functions) {
            if (Session.hasFunction(f)) return true;
        }
        return false;
=======
    // Hàm dùng chung để tạo Popup Menu xịn xò
    private JPopupMenu createSubMenu(String[][] items) {
        JPopupMenu popup = new JPopupMenu();
        popup.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        popup.setBackground(Color.WHITE);
        Font popupFont = new Font("Segoe UI", Font.PLAIN, 14);

        for (String[] itemData : items) {
            JMenuItem item = new JMenuItem(itemData[0]); // itemData[0] là Tên hiển thị
            String cardName = itemData[1];               // itemData[1] là Tên thẻ CardLayout để chuyển trang

            item.setFont(popupFont);
            item.setBackground(Color.WHITE);
            item.setPreferredSize(new Dimension(200, 45));
            item.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
            item.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Hiệu ứng Hover
            item.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    item.setBackground(new Color(240, 245, 250));
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    item.setBackground(Color.WHITE);
                }
            });

            // Sự kiện click chuyển trang
            item.addActionListener(e -> cardLayout.show(contentPanel, cardName));
            popup.add(item);
        }
        return popup;
>>>>>>> ThanhTuyen
    }
}

