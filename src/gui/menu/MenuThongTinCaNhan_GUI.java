package gui.menu;

import javax.swing.*;
import java.awt.*;
import dto.MenuItem;
import gui.ThongTinCaNhanNhanVien_GUI;
import gui.*;

public class MenuThongTinCaNhan_GUI extends JFrame {

    private JList<MenuItem> menuList;
    private DefaultListModel<MenuItem> menuModel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MenuThongTinCaNhan_GUI() {
        setTitle("Tài khoản cá nhân");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {

        menuModel = new DefaultListModel<>();

        menuModel.addElement(new MenuItem("TÀI KHOẢN", null, null, true));
        menuModel.addElement(new MenuItem("Hồ sơ cá nhân", "hoso", null, false));
        menuModel.addElement(new MenuItem("Đổi mật khẩu", "doimatkhau", null, false));

        menuList = new JList<>(menuModel);
        menuList.setCellRenderer(new SidebarRenderer()); // dùng renderer của em
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuList.setFixedCellHeight(45);

        JPanel sidebarPanel = new JPanel(new BorderLayout());

        JScrollPane sidebarScroll = new JScrollPane(menuList);
        sidebarScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        sidebarPanel.add(sidebarScroll, BorderLayout.CENTER);

        JButton btnBack = new JButton("← Quay lại");
        btnBack.setFocusPainted(false);
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnBack.setPreferredSize(new Dimension(200, 45));

        btnBack.addActionListener(e -> {
            new Menu().setVisible(true);
            dispose();
        });

        sidebarPanel.add(btnBack, BorderLayout.SOUTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(new ThongTinCaNhanNhanVien_GUI(), "hoso");
        contentPanel.add(new DoiMatKhau_GUI(), "doimatkhau");

        menuList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                MenuItem item = menuList.getSelectedValue();
                if (item != null && !item.isGroup) {
                    cardLayout.show(contentPanel, item.cardName);
                }
            }
        });

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                sidebarPanel,
                contentPanel
        );
        splitPane.setDividerLocation(220);
        splitPane.setDividerSize(2);

        add(splitPane);

        menuList.setSelectedIndex(1);
    }

    private JPanel createContent(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private Icon icon(String name) {
        return new ImageIcon(getClass().getResource("/icons/" + name));
    }
}