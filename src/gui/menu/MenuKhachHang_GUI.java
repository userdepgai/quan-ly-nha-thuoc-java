package gui.menu;

import javax.swing.*;
import java.awt.*;

import bus.TaiKhoan_BUS;
import dto.MenuItem;
import dto.TaiKhoan_DTO;
import gui.*;
import utils.Session;

public class MenuKhachHang_GUI extends JFrame{

    private JList<MenuItem> menuList;
    private DefaultListModel<MenuItem> menuModel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private TaiKhoan_BUS taiKhoanBus = TaiKhoan_BUS.getInstance();
    public MenuKhachHang_GUI() {
        setTitle("Trang chủ khách hàng");
        setSize(800,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        intitUI();
    }

    private void intitUI() {
        menuModel = new DefaultListModel<>();

        if(Session.isLoggedIn()) {
            menuModel.addElement(new MenuItem(getNameUser(Session.getCurrentUser().getSdt()), null, icon("account.png"), false));
        } else {
            menuModel.addElement(new MenuItem("Nguyễn Gia Thịnh", null, icon("account.png"), false));
        }

        menuModel.addElement(new MenuItem("HỆ THỐNG", null,null,true));
        menuModel.addElement(new MenuItem("Trang chủ","trangChu",icon("dashboard.png"),false));
        menuModel.addElement(new MenuItem("Giỏ hàng","gioHang",icon("dashboard.png"),false));
        menuModel.addElement(new MenuItem("Chờ giao hàng","choGiaoHang",icon("dashboard.png"),false));
        menuModel.addElement(new MenuItem("Lịch sử mua hàng","lichSuMuaHang",icon("dashboard.png"),false));

        menuModel.addElement(new MenuItem("HỒ SƠ", null, null,true));
        menuModel.addElement(new MenuItem("Thông tin cá nhân", "thongTinCaNhan", icon("khachHang.png"),false));
        menuModel.addElement(new MenuItem("Địa chỉ", "diaChi", icon("nhanVien.png"),false));
        menuModel.addElement(new MenuItem("Đổi mật khẩu", "doiMatKhau", icon("nhaCungCap.png"),false));

        menuModel.addElement(new MenuItem("Đăng xuất", "dangXuat", icon("logout.png"), false));

        menuList = new JList<>(menuModel);
        menuList.setCellRenderer(new SidebarRenderer());
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuList.setFixedCellHeight(45);

        JScrollPane src = new JScrollPane(menuList);
        src.setPreferredSize(new Dimension(200, 0));
        src.setMinimumSize(new Dimension(100, 30));
        src.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        src.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        src.getVerticalScrollBar().setUnitIncrement(5);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createContent("Trang chu"),"trangChu");
        contentPanel.add(createContent("Gio hang"), "gioHang");
        contentPanel.add(createContent("Cho giao hang"),"choGiaoHang");
        contentPanel.add(createContent("Lich su mua hang"),"lichSuMuaHang");

        contentPanel.add(new ThongTinCaNhanKhachHang_GUI(),"thongTinCaNhan");
        contentPanel.add(createContent("Dia chi"),"diaChi");
        contentPanel.add(createContent("Doi mat khau"),"doiMatKhau");

        menuList.addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()) {
                MenuItem item = menuList.getSelectedValue();
                if(item != null && !item.isGroup) {
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
                        return;
                    }
                    cardLayout.show(contentPanel, item.cardName);
                }
            }
        });

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                src,
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

    private String getNameUser(String sdt) {
        return taiKhoanBus.getNameKhachHang(sdt);
    }

    public static void main(String[] args) {
        new MenuKhachHang_GUI().setVisible(true);
    }
}
