package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ThongTinCaNhanKhachHang_GUI extends JPanel {

    private JTextField txtHoTen, txtSDT, txtNgaySinh, txtNgayDangKy;
    private JTextField txtHang, txtDiemThuong, txtDiemHang;
    private JRadioButton rdoNam, rdoNu;
    private JButton btnThemKH, btnCapNhatKH;

    private JTable tblDiaChi;
    private DefaultTableModel modelDiaChi;

    private JTextField txtTinh, txtPhuong, txtDuong, txtSoNha;
    private JCheckBox chkMacDinh;
    private JButton btnThemDC, btnCapNhatDC;

    public ThongTinCaNhanKhachHang_GUI() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(createHeader(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("THÔNG TIN KHÁCH HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(25,118,210));

        panel.add(lblTitle);
        return panel;
    }
    private JPanel createMainPanel() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Color.WHITE);

        JPanel infoPanel = createCustomerInfoPanel();
        JScrollPane tablePanel = createTablePanel();
        JPanel detailPanel = createDetailPanel();

        JSplitPane splitTop = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                infoPanel,
                tablePanel
        );
        splitTop.setResizeWeight(0.4);
        splitTop.setDividerSize(5);

        JSplitPane splitMain = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                splitTop,
                detailPanel
        );
        splitMain.setResizeWeight(0.75);
        splitMain.setDividerSize(5);

        main.add(splitMain, BorderLayout.CENTER);
        return main;
    }

    private JPanel createCustomerInfoPanel() {

        JPanel panel = new JPanel(new GridLayout(1,2,40,0));
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin cá nhân"));
        panel.setBackground(Color.WHITE);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(Color.WHITE);

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBackground(Color.WHITE);

        txtHoTen = new JTextField();
        txtSDT = new JTextField();
        txtNgaySinh = new JTextField();
        txtNgayDangKy = new JTextField();
        txtHang = new JTextField();
        txtDiemThuong = new JTextField();
        txtDiemHang = new JTextField();

        rdoNam = new JRadioButton("Nam");
        rdoNu = new JRadioButton("Nữ");
        rdoNam.setBackground(Color.WHITE);
        rdoNu.setBackground(Color.WHITE);

        ButtonGroup group = new ButtonGroup();
        group.add(rdoNam);
        group.add(rdoNu);

        left.add(createField("Họ tên", txtHoTen));
        left.add(createField("Số điện thoại", txtSDT));
        left.add(createField("Ngày sinh", txtNgaySinh));

        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setBackground(Color.WHITE);
        genderPanel.add(new JLabel("Giới tính"));
        genderPanel.add(rdoNam);
        genderPanel.add(rdoNu);

        left.add(genderPanel);

        right.add(createField("Ngày đăng ký", txtNgayDangKy));
        right.add(createField("Hạng", txtHang));
        right.add(createField("Điểm thưởng", txtDiemThuong));
        right.add(createField("Điểm hạng", txtDiemHang));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);

        btnThemKH = new JButton("Thêm");
        btnCapNhatKH = new JButton("Cập nhật");

        btnPanel.add(btnThemKH);
        btnPanel.add(btnCapNhatKH);

        right.add(btnPanel);

        panel.add(left);
        panel.add(right);

        return panel;
    }
    private JScrollPane createTablePanel() {
        String[] column = {"STT", "Tỉnh", "Phường", "Đường", "Số nhà", "Mặc định"};
        modelDiaChi = new DefaultTableModel(column, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblDiaChi = new JTable(modelDiaChi);
        tblDiaChi.getTableHeader().setReorderingAllowed(false);
        tblDiaChi.getTableHeader().setResizingAllowed(false);
        tblDiaChi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tblDiaChi.setRowHeight(28);

        tblDiaChi.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblDiaChi.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblDiaChi.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblDiaChi.getColumnModel().getColumn(3).setPreferredWidth(150);
        tblDiaChi.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblDiaChi.getColumnModel().getColumn(5).setPreferredWidth(90);

        JScrollPane scroll = new JScrollPane(tblDiaChi);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách địa chỉ"));
        scroll.setPreferredSize(new Dimension(100, 300));

        return scroll;
    }
    private JPanel createDetailPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Chi tiết địa chỉ"));
        panel.setBackground(Color.WHITE);

        txtTinh = new JTextField();
        txtPhuong = new JTextField();
        txtDuong = new JTextField();
        txtSoNha = new JTextField();

        chkMacDinh = new JCheckBox("Đặt làm địa chỉ mặc định");
        chkMacDinh.setBackground(Color.WHITE);

        panel.add(createField("Tỉnh", txtTinh));
        panel.add(createField("Phường", txtPhuong));
        panel.add(createField("Đường", txtDuong));
        panel.add(createField("Số nhà", txtSoNha));
        panel.add(chkMacDinh);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);

        btnThemDC = new JButton("Thêm");
        btnCapNhatDC = new JButton("Cập nhật");

        btnPanel.add(btnThemDC);
        btnPanel.add(btnCapNhatDC);

        panel.add(btnPanel);

        return panel;
    }
    
    private JPanel createField(String label, JComponent comp) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(120,30));

        panel.add(lbl, BorderLayout.WEST);
        panel.add(comp, BorderLayout.CENTER);

        return panel;
    }
}