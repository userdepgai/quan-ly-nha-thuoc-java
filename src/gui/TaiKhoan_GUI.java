package gui;

import bus.TaiKhoan_BUS;
import dto.PhanQuyen_DTO;
import dto.TaiKhoan_DTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;

public class TaiKhoan_GUI extends JPanel{
    private JPanel panel_taiKhoan;
    private JComboBox cmb_locQuyen;
    private JButton btnTimKiem;
    private JButton btnThoat;
    private JButton btnXuatExcel;
    private JButton btnNhapExcel;
    private JTable table_dsTaiKhoan;
    private JTextField textField4;
    private JTextField txt_tenNguoiDung;
    private JTextField txt_sdt;
    private JTextField txt_matKhau;
    private JPanel panelThongTin;
    private JPanel panelTable;
    private JButton btn_capNhat;
    private JLabel label_tieuDe;
    private JScrollPane src_dsTaiKhoan;
    private JLabel label_ten;
    private JLabel label_sdt;
    private JTextField txt_ma;
    private JTextField txt_quyen;
    private JTextField txt_ngayKichHoat;
    private JComboBox cmb_trangThai;
    private JButton btn_huy;
    private JButton btn_luu;
    private JLabel label_maTK;
    private JPanel lebel_sdt;
    private JLabel label_mk;
    private JLabel label_quyen;
    private JLabel label_ngayKichHoat;
    private JLabel label_trangThai;
    private JComboBox cmb_locTrangThai;
    private JTextField txt_ndTimKiem;
    private JComboBox cmb_quyen;

    private TaiKhoan_BUS bus = TaiKhoan_BUS.getInstance();
    private DefaultTableModel model;
    private JPopupMenu popupGoiY = new JPopupMenu();


    public TaiKhoan_GUI() {
        this.setLayout(new BorderLayout());
        this.add(panel_taiKhoan, BorderLayout.CENTER);

        fromEdit();
        initTable();
        loadTableFromList(bus.getAll());
        xuLyXuKien();
    }

    private void fromEdit() {

        cmb_trangThai.setModel(new DefaultComboBoxModel<>(
                new String[]{"-- Chọn trạng thái --", "Mở", "Khóa"}
        ));
        cmb_locTrangThai.setModel(new DefaultComboBoxModel<>(
                new String[]{"-- Chọn trạng thái --", "Mở", "Khóa"}
        ));
        loadComboQuyen();
        setViewMode();
    }
    private void initTable() {
        String[] columns = {"STT", "Mã tài khoản", "Tên người dùng", "SDT", "Mật khẩu", "Ngày kích hoạt", "Quyền", "Trạng thái"};
        model = new DefaultTableModel(columns,0) {
            @Override
            public boolean isCellEditable(int row, int column) {    return false;   }
        };

        table_dsTaiKhoan.setModel(model);
        table_dsTaiKhoan.getTableHeader().setResizingAllowed(false);
        table_dsTaiKhoan.getTableHeader().setReorderingAllowed(false);

        table_dsTaiKhoan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table_dsTaiKhoan.getColumnModel().getColumn(0).setPreferredWidth(50);
        table_dsTaiKhoan.getColumnModel().getColumn(1).setPreferredWidth(120);
        table_dsTaiKhoan.getColumnModel().getColumn(2).setPreferredWidth(188);
        table_dsTaiKhoan.getColumnModel().getColumn(3).setPreferredWidth(140);
        table_dsTaiKhoan.getColumnModel().getColumn(4).setPreferredWidth(200);
        table_dsTaiKhoan.getColumnModel().getColumn(5).setPreferredWidth(150);
        table_dsTaiKhoan.getColumnModel().getColumn(6).setPreferredWidth(180);
        table_dsTaiKhoan.getColumnModel().getColumn(7).setPreferredWidth(128);

        table_dsTaiKhoan.setRowHeight(25);
        table_dsTaiKhoan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        src_dsTaiKhoan.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        src_dsTaiKhoan.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
    }
    private void loadTableFromList(ArrayList<TaiKhoan_DTO> list) {
        model.setRowCount(0);
        int stt = 1;
        for(TaiKhoan_DTO tk : list) {
            String tenNguoiDung;
            String tenQuyen = bus.getTenQuyen(tk.getMaQuyen());
            if(tenQuyen.equals("Khách hàng")) {
                tenNguoiDung = bus.getNameKhachHang(tk.getSdt());
            } else {
                tenNguoiDung = bus.getNameNhanVien(tk.getSdt());
            }

            model.addRow(new Object[]{
                    stt++,
                    tk.getMaTK(),
                    tenNguoiDung,
                    tk.getSdt(),
                    tk.getMatKhau(),
                    tk.getNgayKichHoat(),
                   tenQuyen,
                   tk.getTrangThai() == 1 ? "Mở" : "Khóa"
            });
        }
    }

    private void xuLyXuKien(){
        table_dsTaiKhoan.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting())
                hienChiTiet();
        });

        txt_ndTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!txt_ndTimKiem.getText().trim().isEmpty())
                        hienThiGoiY(timKiem());
                else popupGoiY.setVisible(false);
            }
        });

        btnTimKiem.addActionListener(e -> {
            loadTableFromList(timKiem());
        });

        btnThoat.addActionListener(e -> {
            cmb_locTrangThai.setSelectedIndex(0);
            cmb_locQuyen.setSelectedIndex(0);
            txt_ndTimKiem.setText("");
            loadTableFromList(bus.getAll());
        });

        btn_capNhat.addActionListener(e -> {
            if (table_dsTaiKhoan.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản");
                return;
            }
            setUpdateMode();
        });
        btn_luu.addActionListener(e -> {
            xuLyCapNhatTaiKhoan();
        });

        btn_huy.addActionListener(e -> {
            clearForm();
            setViewMode();
        });
    }

    private TaiKhoan_DTO layDuLieuTaiKhoan() {
        String ma = txt_ma.getText().trim();
        String sdt = txt_sdt.getText().trim();
        String matKhau = txt_matKhau.getText().trim();

        LocalDate ngayKichHoat;
        try {
            ngayKichHoat = LocalDate.parse(txt_ngayKichHoat.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ngày kích hoạt không hợp lệ (yyyy-MM-dd)");
            return null;
        }
        String tt = cmb_trangThai.getSelectedItem().toString();

        int trangThai;
        if (tt.equals("Mở")) {
            trangThai = 1;
        } else if (tt.equals("Khóa")) {
            trangThai = 0;
        } else {
            trangThai = -1;
        }

        String tenQuyen = cmb_quyen.getSelectedItem().toString();
        String maQuyen = bus.getMaQuyen(tenQuyen);
        return new TaiKhoan_DTO(ma, sdt, matKhau,maQuyen, ngayKichHoat, trangThai);
    }

    private void loadComboQuyen() {
        cmb_quyen.removeAllItems();
        cmb_quyen.addItem("-- Chọn quyền --");
        cmb_locQuyen.addItem("-- Chọn quyền --");

        for (String ten : bus.getTenQuyenHoatDong()) {
            cmb_quyen.addItem(ten);
            cmb_locQuyen.addItem(ten);
        }
        for (String ten : bus.getTenQuyenNgungHoatDong()) {
            cmb_quyen.addItem(ten);
            cmb_locQuyen.addItem(ten);
        }
    }
    private void loadComboQuyenHoatDong() {
        cmb_quyen.removeAllItems();
        cmb_quyen.addItem("-- Chọn quyền --");
        for (String ten : bus.getTenQuyenHoatDong()) {
            cmb_quyen.addItem(ten);
        }
    }
    private void setUpdateMode() {
        lockTable();

        txt_ma.setEditable(false);
        txt_ngayKichHoat.setEditable(false);
        txt_sdt.setEditable(false);
        txt_matKhau.setEditable(true);

        int row = table_dsTaiKhoan.getSelectedRow();
        if (row < 0) return;
        String sdt = table_dsTaiKhoan.getValueAt(row, 3).toString();
        TaiKhoan_DTO tk = bus.getBySDT(sdt);
        if (tk == null) return;
        String tenQuyenHienTai = bus.getTenQuyen(tk.getMaQuyen());

        loadComboQuyenHoatDong();

        boolean tonTai = false;
        for (int i = 0; i < cmb_quyen.getItemCount(); i++) {
            if (cmb_quyen.getItemAt(i).equals(tenQuyenHienTai)) {
                tonTai = true;
                break;
            }
        }

        if (!tonTai) {
            cmb_quyen.addItem(tenQuyenHienTai);
        }
        cmb_quyen.setSelectedItem(
                tonTai ? tenQuyenHienTai : tenQuyenHienTai
        );

        cmb_quyen.setEnabled(true);
        cmb_trangThai.setEnabled(true);

        btn_luu.setVisible(true);
        btn_huy.setVisible(true);
        btn_capNhat.setEnabled(false);
    }
    private void xuLyCapNhatTaiKhoan() {
        TaiKhoan_DTO tk = layDuLieuTaiKhoan();
        if (!kiemTraHopLeTaiKhoan(tk)) return;
        if (bus.capNhat(tk)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công");
            loadTableFromList(bus.getAll());
            clearForm();
            setViewMode();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
        }
    }
    private ArrayList<TaiKhoan_DTO> timKiem() {
        String keyword = txt_ndTimKiem.getText().trim();
        Integer trangThai = null;
        if(cmb_locTrangThai.getSelectedIndex() == 1) {
            trangThai = 1;
        } else if (cmb_locTrangThai.getSelectedIndex() == 2){
            trangThai = 0;
        }
        Object selected = cmb_locQuyen.getSelectedItem();

        String quyen = (selected != null && !selected.equals("-- Chọn quyền --"))
                ? selected.toString()
                : null;
        return bus.timKiem(keyword, quyen, trangThai);
    }

    private void hienChiTiet() {
        int row = table_dsTaiKhoan.getSelectedRow();
        if(row >= 0){
            String sdt = table_dsTaiKhoan.getValueAt(row, 3).toString();
            TaiKhoan_DTO tk = bus.getBySDT(sdt);
            if(tk == null) return;
            txt_ma.setText(tk.getMaTK());
            txt_sdt.setText(tk.getSdt());
            txt_matKhau.setText(tk.getMatKhau());
            txt_ngayKichHoat.setText(String.valueOf(tk.getNgayKichHoat()));
            String tenQuyen = bus.getTenQuyen(tk.getMaQuyen());
            cmb_quyen.setSelectedItem(tenQuyen);
            if(tenQuyen.equals("Khách hàng")) {
                txt_tenNguoiDung.setText(bus.getNameKhachHang(sdt));
            } else {
                txt_tenNguoiDung.setText(bus.getNameNhanVien(sdt));
            }

            cmb_trangThai.setSelectedItem(
                    tk.getTrangThai() == 1 ? "Mở" : "Khóa"
            );
        }
    }

    private boolean kiemTraHopLeTaiKhoan(TaiKhoan_DTO tk) {

        if (tk == null) return false;
        if (tk.getMaTK() == null || tk.getMaTK().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã tài khoản không được để trống");
            return false;
        }
        if (tk.getSdt() == null || tk.getSdt().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống");
            return false;
        }
        if (!tk.getSdt().matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(this, "SĐT phải gồm 10 số và bắt đầu bằng 0");
            return false;
        }
        if (tk.getMatKhau() == null || tk.getMatKhau().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không được để trống");
            return false;
        }

        if (tk.getMatKhau().length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu phải từ 6 ký tự trở lên");
            return false;
        }

        if (tk.getNgayKichHoat() == null) {
            JOptionPane.showMessageDialog(this, "Ngày kích hoạt không hợp lệ");
            return false;
        }
        if (tk.getTrangThai() == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn trạng thái");
            return false;
        }
        return true;
    }

    private void hienThiGoiY(ArrayList<TaiKhoan_DTO> list) {
        popupGoiY.setVisible(false);
        popupGoiY.removeAll();

        if(list.isEmpty()) return;

        JPanel panelNoiDung = new JPanel();
        panelNoiDung.setLayout(new BoxLayout(panelNoiDung, BoxLayout.Y_AXIS));
        panelNoiDung.setBackground(Color.WHITE);

        for(TaiKhoan_DTO tk : list) {
            String sdt = tk.getSdt().toLowerCase();
            String ten = "";

            String tenQuyen = bus.getTenQuyen(tk.getMaQuyen());

            if(tenQuyen != null && tenQuyen.equals("Khách hàng")){
                String tenKH = bus.getNameKhachHang(tk.getSdt());
                if(tenKH != null) ten = tenKH.toLowerCase();
            }else{
                String tenNV = bus.getNameNhanVien(tk.getSdt());
                if(tenNV != null) ten = tenNV.toLowerCase();
            }

            JButton btnItem = new JButton(tk.getSdt() + " - " + ten);
            btnItem.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnItem.setHorizontalAlignment(SwingConstants.LEFT);
            btnItem.setMargin(new Insets(2,10,2,10));
            btnItem.setBorderPainted(false);
            btnItem.setContentAreaFilled(false);
            btnItem.setFocusPainted(false);
            btnItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnItem.setPreferredSize(new Dimension(txt_ndTimKiem.getWidth(),30));
            btnItem.setMaximumSize(new Dimension(txt_ndTimKiem.getWidth(),30));

            btnItem.addActionListener(e -> {
                txt_ndTimKiem.setText(tk.getSdt());
                popupGoiY.setVisible(false);
            });

            btnItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btnItem.setContentAreaFilled(true);
                    btnItem.setBackground(new Color(240,240,240));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btnItem.setContentAreaFilled(false);
                }
            });

            panelNoiDung.add(btnItem);
        }

        JScrollPane src = new JScrollPane(panelNoiDung);
        src.setBorder(null);
        src.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));
        src.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

        int height = Math.min(list.size() * 30, 150);
        src.setPreferredSize(new Dimension(txt_ndTimKiem.getWidth(), height));

        popupGoiY.setFocusable(false);
        popupGoiY.add(src);
        popupGoiY.show(txt_ndTimKiem,0,txt_ndTimKiem.getHeight());

        src.setFocusable(false);
    }
    private void setViewMode() {
        unlockTable();
        loadComboQuyen();
        txt_ma.setEditable(false);
        txt_sdt.setEditable(false);
        txt_tenNguoiDung.setEditable(false);
        txt_matKhau.setEditable(false);
        txt_ngayKichHoat.setEditable(false);

        cmb_quyen.setEnabled(false);
        cmb_trangThai.setEnabled(false);

        btn_luu.setVisible(false);
        btn_huy.setVisible(false);

        btn_capNhat.setEnabled(true);

    }
    private void clearForm() {
        txt_ma.setText("");
        txt_sdt.setText("");
        txt_tenNguoiDung.setText("");
        txt_matKhau.setText("");
        txt_ngayKichHoat.setText("");
        cmb_quyen.setSelectedIndex(-1);
        cmb_trangThai.setSelectedIndex(0);
    }
    private void lockTable() {
        table_dsTaiKhoan.setRowSelectionAllowed(false);
        table_dsTaiKhoan.setEnabled(false);
    }

    private void unlockTable() {
        table_dsTaiKhoan.setRowSelectionAllowed(true);
        table_dsTaiKhoan.setEnabled(true);
    }
}
