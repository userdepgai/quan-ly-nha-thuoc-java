package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.toedter.calendar.JDateChooser;
import dto.*;
import bus.*;

public class ThongTinCaNhanKhachHang_GUI extends JPanel {

    private JTextField txtHoTen, txtSDT, txtNgayDangKy;
    private JDateChooser dcNgaySinh;
    private JTextField txtHang, txtDiemThuong, txtDiemHang;
    private JRadioButton rdoNam, rdoNu;
    private JButton btnCapNhatKH;

    private JTable tblDiaChi;
    private DefaultTableModel modelDiaChi;

    private JTextField txtTinh, txtPhuong, txtDuong, txtSoNha;
    private JCheckBox chkMacDinh;
    private JButton btnThemDC, btnCapNhatDC;
    private JButton btnLuuKH, btnHuyKH;
    private JButton btnLuuDC, btnHuyDC;
    private ThongTinCaNhanKhachHang_BUS bus = new ThongTinCaNhanKhachHang_BUS();
    private KhachHang_DTO currentKhachHang;

    public ThongTinCaNhanKhachHang_GUI() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(createHeader(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        setModelView();
        loadThongTin();
        xuLyXuKien();
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
        dcNgaySinh = new JDateChooser();
        dcNgaySinh.setDateFormatString("dd/MM/yyyy");
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
        left.add(createField("Ngày sinh", dcNgaySinh));

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

        btnCapNhatKH = new JButton("Cập nhật");
        btnLuuKH = new JButton("Lưu");
        btnHuyKH = new JButton("Hủy");

        btnPanel.add(btnCapNhatKH);
        btnPanel.add(btnLuuKH);
        btnPanel.add(btnHuyKH);

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
        btnLuuDC = new JButton("Lưu");
        btnHuyDC = new JButton("Hủy");

        btnPanel.add(btnThemDC);
        btnPanel.add(btnCapNhatDC);
        btnPanel.add(btnLuuDC);
        btnPanel.add(btnHuyDC);

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

    private void xuLyXuKien(){

        btnCapNhatKH.addActionListener(e -> xuLyCapNhatKH());

        btnLuuKH.addActionListener(e -> xuLyLuuKH());

        btnHuyKH.addActionListener(e -> xuLyHuyKH());
    }

    private void loadThongTin(){
        currentKhachHang = bus.getKhachHangDangNhap();
        if(currentKhachHang == null){
            JOptionPane.showMessageDialog(this,
                    "Không tìm thấy khách hàng");
            return;
        }
        txtHoTen.setText(currentKhachHang.getTen());
        txtSDT.setText(currentKhachHang.getSdt());
        if(currentKhachHang.getNgaySinh()!=null){
            Date date = Date.from(
                    currentKhachHang.getNgaySinh()
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant()
            );
            dcNgaySinh.setDate(date);
        }
        if(currentKhachHang.isGioiTinh())
            rdoNam.setSelected(true);
        else
            rdoNu.setSelected(true);
        txtNgayDangKy.setText(currentKhachHang.getNgayDKThanhVien().toString());
        txtHang.setText(currentKhachHang.getHang());
        txtDiemThuong.setText(String.valueOf(currentKhachHang.getDiemThuong()));
        txtDiemHang.setText(String.valueOf(currentKhachHang.getDiemHang()));
    }
    private KhachHang_DTO getKhachHangFromForm(){
        KhachHang_DTO kh = new KhachHang_DTO();
        kh.setMa(currentKhachHang.getMa());
        kh.setHang(currentKhachHang.getHang());
        kh.setDiemThuong(currentKhachHang.getDiemThuong());
        kh.setDiemHang(currentKhachHang.getDiemHang());
        kh.setNgayDKThanhVien(currentKhachHang.getNgayDKThanhVien());

        kh.setTen(txtHoTen.getText().trim());
        kh.setSdt(txtSDT.getText().trim());

        Date date = dcNgaySinh.getDate();
        if(date != null){
            LocalDate localDate =
                    date.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

            kh.setNgaySinh(localDate);
        }
        kh.setGioiTinh(rdoNam.isSelected());
        return kh;
    }
    private void xuLyCapNhatKH(){
        setEditableKH(true);

        btnCapNhatKH.setVisible(false);

        btnCapNhatDC.setEnabled(false);
        btnThemDC.setEnabled(false);

        btnLuuKH.setVisible(true);
        btnHuyKH.setVisible(true);
    }
    private void xuLyLuuKH(){
        if(!validateForm()) return;
        try{
            KhachHang_DTO kh = getKhachHangFromForm();
            boolean result = bus.capNhatThongTin(kh);
            if(!result){
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
                return;
            }
            JOptionPane.showMessageDialog(this, "Cập nhật thành công");

            currentKhachHang = kh;

            reloadForm();
            setEditableKH(false);

            btnCapNhatKH.setVisible(true);

            btnCapNhatDC.setEnabled(true);
            btnThemDC.setEnabled(true);

            btnLuuKH.setVisible(false);
            btnHuyKH.setVisible(false);
        }
        catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi dữ liệu");
        }
    }
    private void xuLyHuyKH(){
        reloadForm();
        setEditableKH(false);
        btnCapNhatKH.setVisible(true);
        btnCapNhatDC.setEnabled(true);
        btnThemDC.setEnabled(true);
        btnLuuKH.setVisible(false);
        btnHuyKH.setVisible(false);
    }
    private void reloadForm(){
        txtHoTen.setText(currentKhachHang.getTen());
        txtSDT.setText(currentKhachHang.getSdt());
        if(currentKhachHang.getNgaySinh()!=null){
            Date date = Date.from(
                    currentKhachHang.getNgaySinh()
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant()
            );
            dcNgaySinh.setDate(date);
        }
        if(currentKhachHang.isGioiTinh())
            rdoNam.setSelected(true);
        else
            rdoNu.setSelected(true);
    }
    private boolean validateForm(){
        if(txtHoTen.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this,"Tên không được để trống");
            return false;
        }
        if(!txtSDT.getText().matches("^0\\d{9}$")){
            JOptionPane.showMessageDialog(this,"Số điện thoại phải 10 số và bắt đầu bằng 0");
            return false;
        }
        if(dcNgaySinh.getDate()==null){
            JOptionPane.showMessageDialog(this,"Vui lòng chọn ngày sinh");
            return false;
        }
        return true;
    }
    private void setModelView(){
        setEditableKH(false);
        setEditableDiaChi(false);
        btnCapNhatKH.setVisible(true);
        btnThemDC.setVisible(true);
        btnCapNhatDC.setVisible(true);
        btnLuuKH.setVisible(false);
        btnHuyKH.setVisible(false);
        btnLuuDC.setVisible(false);
        btnHuyDC.setVisible(false);
    }
    private void setEditableKH(boolean enable){

        txtHoTen.setEditable(enable);
        txtSDT.setEditable(enable);
        dcNgaySinh.setEnabled(enable);

        txtNgayDangKy.setEditable(false);
        txtHang.setEditable(false);
        txtDiemThuong.setEditable(false);
        txtDiemHang.setEditable(false);

        rdoNam.setEnabled(enable);
        rdoNu.setEnabled(enable);
    }
    private void setEditableDiaChi(boolean enable){

        txtTinh.setEditable(enable);
        txtPhuong.setEditable(enable);
        txtDuong.setEditable(enable);
        txtSoNha.setEditable(enable);

        chkMacDinh.setEnabled(enable);
    }
}