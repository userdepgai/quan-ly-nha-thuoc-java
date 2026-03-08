package gui;

import javax.swing.*;
import java.awt.*;

import bus.DiaChi_BUS;
import bus.ThongTinCaNhanNhanVien_BUS;
import com.toedter.calendar.JDateChooser;
import dto.DIACHI_DTO;
import dto.NhanVien_DTO;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ThongTinCaNhanNhanVien_GUI extends JPanel {

    private JTextField txtHoTen, txtSDT, txtNgayVaoLam, txtLuong;
    private JDateChooser ngaySinh;
    private JTextField txtTinh;
    private JTextField txtPhuong;
    private JTextField txtDuong;
    private JTextField txtSoNha;
    private JRadioButton rdoNam, rdoNu;
    private JComboBox<String> cboChucVu;
    private JButton btnLuu, btnHuy, btnCapNhat;

    private ThongTinCaNhanNhanVien_BUS bus = new ThongTinCaNhanNhanVien_BUS();
    private NhanVien_DTO currentNhanVien;

    public ThongTinCaNhanNhanVien_GUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        createPanel();
        loadThongTin();
        setEditable(false);

        btnLuu.setVisible(false);
        btnHuy.setVisible(false);
        xuLyXuKien();
    }
    private void createPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));
        headerPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("THÔNG TIN CÁ NHÂN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(25, 118, 210));

        btnCapNhat = new JButton("Cập nhật thông tin");
        btnCapNhat.setBackground(new Color(25, 118, 210));
        btnCapNhat.setForeground(Color.WHITE);
        btnCapNhat.setFocusPainted(false);

        JLabel lblDesc = new JLabel("Bạn có thể chỉnh thông tin cá nhân của mình");
        lblDesc.setForeground(Color.GRAY);

        headerPanel.add(lblTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(btnCapNhat);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(lblDesc);

        add(headerPanel, BorderLayout.NORTH);

        JPanel panelForm = new JPanel();
        panelForm.setLayout(new BoxLayout(panelForm, BoxLayout.Y_AXIS));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));
        panelForm.setBackground(Color.WHITE);

        panelForm.add(createField("Họ tên", txtHoTen = new JTextField()));
        panelForm.add(createField("Số điện thoại", txtSDT = new JTextField()));
        ngaySinh = new JDateChooser();
        ngaySinh.setDateFormatString("dd/MM/yyyy");

        panelForm.add(createField("Ngày sinh", ngaySinh));

        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setBackground(Color.WHITE);
        genderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel lblGender    = new JLabel("Giới tính");
        lblGender.setPreferredSize(new Dimension(120, 30));

        rdoNam = new JRadioButton("Nam");
        rdoNu = new JRadioButton("Nữ");
        rdoNam.setBackground(Color.WHITE);
        rdoNu.setBackground(Color.WHITE);

        ButtonGroup group = new ButtonGroup();
        group.add(rdoNam);
        group.add(rdoNu);

        genderPanel.add(lblGender);
        genderPanel.add(rdoNam);
        genderPanel.add(rdoNu);

        panelForm.add(genderPanel);
        panelForm.add(Box.createRigidArea(new Dimension(0, 10)));

        cboChucVu = new JComboBox<>(new String[]{"Nhân viên", "Quản lý", "Admin"});
        panelForm.add(createField("Chức vụ", cboChucVu));

        panelForm.add(createField("Ngày vào làm", txtNgayVaoLam = new JTextField()));
        panelForm.add(createField("Lương", txtLuong = new JTextField()));

        panelForm.add(createAddressPanel());

        JScrollPane scrollPane = new JScrollPane(panelForm);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);


        add(scrollPane, BorderLayout.CENTER);

        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 30));
        panelButton.setBackground(Color.WHITE);

        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        btnLuu.setVisible(false);
        btnHuy.setVisible(false);

        panelButton.add(btnLuu);
        panelButton.add(btnHuy);

        add(panelButton, BorderLayout.SOUTH);
    }
    private JPanel createAddressPanel() {

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220)),
                BorderFactory.createEmptyBorder(15,15,15,15)
        ));

        JLabel title = new JLabel("ĐỊA CHỈ");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(new Color(25,118,210));

        wrapper.add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(2,4,15,10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15,0,0,0));

        txtTinh = new JTextField();
        txtPhuong = new JTextField();
        txtDuong = new JTextField();
        txtSoNha = new JTextField();

        panel.add(new JLabel("Tỉnh / Thành"));
        panel.add(txtTinh);

        panel.add(new JLabel("Phường / Xã"));
        panel.add(txtPhuong);

        panel.add(new JLabel("Đường"));
        panel.add(txtDuong);

        panel.add(new JLabel("Số nhà"));
        panel.add(txtSoNha);

        wrapper.add(panel, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createField(String label, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(120, 30));

        component.setPreferredSize(new Dimension(300, 30));

        panel.add(lbl, BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
        panel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.SOUTH);

        return panel;
    }

    private void loadThongTin() {
        currentNhanVien = bus.getNhanVienDangNhap();
        if (currentNhanVien == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên đăng nhập");
            return;
        }
        txtHoTen.setText(currentNhanVien.getTen());
        txtSDT.setText(currentNhanVien.getSdt());

        if (currentNhanVien.getNgaySinh() != null) {
            Date date = Date.from(currentNhanVien.getNgaySinh()
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant());
            ngaySinh.setDate(date);
        }
        if (currentNhanVien.isGioiTinh())
            rdoNam.setSelected(true);
        else
            rdoNu.setSelected(true);

        cboChucVu.removeAllItems();
        cboChucVu.addItem(currentNhanVien.getChucVu());

        if (currentNhanVien.getNgayVaoLam() != null)
            txtNgayVaoLam.setText(currentNhanVien.getNgayVaoLam().toString());

        txtLuong.setText(String.valueOf(currentNhanVien.getLuongCoBan()));

        loadDiaChi();
    }
    private void loadDiaChi() {

        DiaChi_BUS diaChiBus = DiaChi_BUS.getInstance();

        DIACHI_DTO dc = diaChiBus.getById(currentNhanVien.getMaDiaChi());

        if (dc != null) {

            txtTinh.setText(dc.getTinh());
            txtPhuong.setText(dc.getPhuong());
            txtDuong.setText(dc.getDuong());
            txtSoNha.setText(dc.getSoNha());

        }
    }

    private void xuLyXuKien() {
        btnCapNhat.addActionListener(e -> {
            xuLyCapNhat();
        });
        btnLuu.addActionListener(e -> {
            xuLyLuu();
        });
        btnHuy.addActionListener(e -> {
            xuLyHuy();
        });
    }
    private void xuLyCapNhat() {

        setEditable(true);

        btnCapNhat.setEnabled(false);
        btnLuu.setVisible(true);
        btnHuy.setVisible(true);
    }
private void xuLyLuu() {
    try {
        NhanVien_DTO nv = getNhanVienFromForm();
        DIACHI_DTO dc = getDiaChiFromForm();
        boolean resultNhanVien = bus.capNhatThongTin(nv);

        if (!resultNhanVien) {
            JOptionPane.showMessageDialog(this,
                    "Cập nhật nhân viên thất bại (kiểm tra console)");
            return;
        }

        boolean resultDiaChi = DiaChi_BUS.getInstance().capNhat(dc);

        if (!resultDiaChi) {
            JOptionPane.showMessageDialog(this,
                    "Cập nhật địa chỉ thất bại");
            return;
        }

        JOptionPane.showMessageDialog(this, "Cập nhật thành công");
        currentNhanVien = nv;
        reloadForm();

        setEditable(false);
        btnCapNhat.setEnabled(true);
        btnLuu.setVisible(false);
        btnHuy.setVisible(false);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Lỗi dữ liệu: " + e.getMessage());
    }
}
    private void xuLyHuy() {
        reloadForm();

        setEditable(false);

        btnCapNhat.setVisible(true);
        btnCapNhat.setEnabled(true);
        btnLuu.setVisible(false);
        btnHuy.setVisible(false);
    }
    private DIACHI_DTO getDiaChiFromForm() {
        DIACHI_DTO dc = new DIACHI_DTO();
        dc.setMaDiaChi(currentNhanVien.getMaDiaChi());
        dc.setTinh(txtTinh.getText().trim());
        dc.setPhuong(txtPhuong.getText().trim());
        dc.setDuong(txtDuong.getText().trim());
        dc.setSoNha(txtSoNha.getText().trim());

        return dc;
    }
    private NhanVien_DTO getNhanVienFromForm() {

        NhanVien_DTO nv = new NhanVien_DTO();

        nv.setMa(currentNhanVien.getMa());
        nv.setMaDiaChi(currentNhanVien.getMaDiaChi());
        nv.setNgayVaoLam(currentNhanVien.getNgayVaoLam());
        nv.setChucVu(currentNhanVien.getChucVu());

        nv.setTen(txtHoTen.getText().trim());
        nv.setSdt(txtSDT.getText().trim());

        Date date = ngaySinh.getDate();
        if (date != null) {
            LocalDate localDate = date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            nv.setNgaySinh(localDate);
        }

        nv.setGioiTinh(rdoNam.isSelected());

        nv.setLuongCoBan(Double.parseDouble(txtLuong.getText().trim()));

        return nv;
    }
    private void setEditable(boolean enable) {
        txtHoTen.setEditable(enable);
        txtSDT.setEditable(enable);
        ngaySinh.setEnabled(enable);
        txtLuong.setEditable(false);

        txtTinh.setEditable(enable);
        txtPhuong.setEditable(enable);
        txtDuong.setEditable(enable);
        txtSoNha.setEditable(enable);

        rdoNam.setEnabled(enable);
        rdoNu.setEnabled(enable);

        cboChucVu.setEnabled(false);
        txtNgayVaoLam.setEditable(false);
    }
    private void clearForm() {

        txtHoTen.setText("");
        txtSDT.setText("");
        txtNgayVaoLam.setText("");
        txtLuong.setText("");

        ngaySinh.setDate(null);

        rdoNam.setSelected(false);
        rdoNu.setSelected(false);

        cboChucVu.removeAllItems();

        txtTinh.setText("");
        txtPhuong.setText("");
        txtDuong.setText("");
        txtSoNha.setText("");
    }
    private void reloadForm(){
        clearForm();
        loadThongTin();
    }

}
