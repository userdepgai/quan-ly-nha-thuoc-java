package gui.menu;

import javax.swing.*;
import java.awt.*;
import dto.MenuItem;
import gui.*;
import gui.HOADON_GUI.LapHoaDon_GUI;
import gui.HOADON_GUI.XuatHoaDon_GUI;

public class Menu extends JFrame {
    private JList<MenuItem> menuList;
    private DefaultListModel<MenuItem> menuModel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public Menu() {
        setTitle("Admin Dashboard");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {

        menuModel = new DefaultListModel<>();

        menuModel.addElement(new MenuItem("Thông tin cá nhân", "thongTinCaNhanFrame", icon("account.png"), false));
        menuModel.addElement(new MenuItem("TỔNG QUAN & ĐIỀU HÀNH", null, null, true));
        menuModel.addElement(new MenuItem("Dashboard", "dashboard", icon("dashboard.png"), false));
        menuModel.addElement(new MenuItem("Thống kê", "thongke", icon("thongKe.png"), false));
        menuModel.addElement(new MenuItem("Báo cáo", "baocao", icon("baoCao.png"),false));

        menuModel.addElement(new MenuItem("BÁN HÀNG & HÓA ĐƠN", null, null,true));
        menuModel.addElement(new MenuItem("Bán hàng", "banhang", icon("banHang.png"),false));
        menuModel.addElement(new MenuItem("Duyệt hóa đơn online", "duyethd", icon("duyetHoaDon.png"),false));
        menuModel.addElement(new MenuItem("Quản lý hóa đơn", "hoadon", icon("hoaDon.png"),false));

        menuModel.addElement(new MenuItem("QUẢN LÝ CON NGƯỜI", null, null,true));
        menuModel.addElement(new MenuItem("Khách hàng", "khachhang", icon("khachHang.png"),false));
        menuModel.addElement(new MenuItem("Nhân viên", "nhanvien", icon("nhanVien.png"),false));
        menuModel.addElement(new MenuItem("Nhà cung cấp", "nhacungcap", icon("nhaCungCap.png"),false));

        menuModel.addElement(new MenuItem("DANH MỤC & SẢN PHẨM", null, null,true));
        menuModel.addElement(new MenuItem("Danh mục sản phẩm", "danhMuc", icon("danhMuc.png"),false));
        menuModel.addElement(new MenuItem("Thuộc tính danh mục", "thuocTinhDanhMuc", icon("thuocTinhDanhMuc.png"),false));
        menuModel.addElement(new MenuItem("Quản lý sản phẩm", "sanPham", icon("sanPham.png"),false));

        menuModel.addElement(new MenuItem("KHO & LƯU TRỮ", null, null,true));
        menuModel.addElement(new MenuItem("Khu vực lưu trữ", "luutru", icon("khuVucLuuTru.png"),false));
        menuModel.addElement(new MenuItem("Lô hàng", "lohang", icon("loHang.png"),false));
        menuModel.addElement(new MenuItem("Phiếu nhập", "phieunhap", icon("phieuNhap.png"),false));

        menuModel.addElement(new MenuItem("KHUYẾN MÃI", null, null,true));
        menuModel.addElement(new MenuItem("Chương trình", "chuongTrinhKhuyenMai", icon("chuongTrinhKhuyenMai.png"),false));
        menuModel.addElement(new MenuItem("Khuyến mãi sản phẩm", "khuyenMai", icon("khuyenMai.png"),false));
        menuModel.addElement(new MenuItem("Voucher", "voucher", icon("voucher.png"),false));

        menuModel.addElement(new MenuItem("HỆ THỐNG", null, null,true));
        menuModel.addElement(new MenuItem("Quản lý tài khoản", "taiKhoanDangNhap", icon("taiKhoanDangNhap.png"),false));
        menuModel.addElement(new MenuItem("Phân quyền", "phanQuyen", icon("phanQuyen.png"),false));

        menuList = new JList<>(menuModel);
        menuList.setCellRenderer(new SidebarRenderer());
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuList.setFixedCellHeight(45);

        JScrollPane sidebarScroll = new JScrollPane(menuList);
        sidebarScroll.setPreferredSize(new Dimension(200, 0));
        sidebarScroll.setMinimumSize(new Dimension(100, 30));
        sidebarScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        sidebarScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScroll.getVerticalScrollBar().setUnitIncrement(5);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(new DashBoard_GUI(), "dashboard");
        contentPanel.add(new ThongTinCaNhanNhanVien_GUI(), "thongke");
        contentPanel.add(createContent("Báo cáo"), "baocao");

        contentPanel.add(new LapHoaDon_GUI(), "banhang");
        contentPanel.add(createContent("Duyệt hóa đơn online"), "duyethd");
        contentPanel.add(new XuatHoaDon_GUI(), "hoadon");

        contentPanel.add(createContent("Danh mục sản phẩm"), "danhMuc");
        contentPanel.add(createContent("Thuộc tính danh mục"), "thuocTinhDanhMuc");
        contentPanel.add(createContent("Quản lý sản phẩm"), "sanPham");

        contentPanel.add(createContent("Khách hàng"), "khachhang");
        contentPanel.add(createContent("Nhân viên"), "nhanvien");
        contentPanel.add(createContent("Nhà cung cấp"), "nhacungcap");

        contentPanel.add(createContent("Khu vực lưu trữ"), "luutru");
        contentPanel.add(new LoHang_GUI(), "lohang");
        contentPanel.add(new PhieuNhap_GUI(), "phieunhap");

        contentPanel.add(createContent("Quản lý chương trình"), "chuongTrinhKhuyenMai");
        contentPanel.add(createContent("Khuyến mãi sản phẩm"), "khuyenMai");
        contentPanel.add(createContent("Quản lý voucher"), "voucher");

        contentPanel.add(new TaiKhoan_GUI(), "taiKhoanDangNhap");
        contentPanel.add(new PhanQuyen_GUI(), "phanQuyen");

        menuList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                MenuItem item = menuList.getSelectedValue();
                if (item != null && !item.isGroup) {
//                    cardLayout.show(contentPanel, item.cardName);
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
    private Icon icon(String name) {
        return new ImageIcon(getClass().getResource("/icons/" + name));
    }

}

