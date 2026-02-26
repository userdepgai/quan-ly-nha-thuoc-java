package gui;

import dto.KhachHang_DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.time.LocalDate;
import bus.KhachHang_BUS;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
public class KhachHang_GUI extends JPanel {
    private boolean isAdding = false;
    private boolean isUpdating = false;
    private JPanel panel_khachHang;
    private JButton btnXuatExcel;
    private JButton btnNhapExcel;
    private JButton btn_timKiem;
    private JButton btn_thoat;
    private JComboBox cmb_locTrangThai;
    private JButton btn_them;
    private JPanel panelThongTin;
    private JTextField txt_maKH;
    private JTextField txt_tenKH;
    private JTextField txt_ngaySinh;
    private JTextField txt_diaChi;
    private JTextField txt_sdt;
    private JRadioButton rd_nu;
    private JRadioButton rd_nam;
    private JTextField textField4;
    private JTextField txt_diemThuong;
    private JTextField txt_hang;
    private JButton btn_hang;
    private JTextField txt_ngayDKThanhVien;
    private JTable table_dsKhachHang;
    private JScrollPane src_dsKhachHang;
    private JTextField txt_diemHang;
    private JButton btn_huy;
    private JButton btn_luu;
    private JLabel txt_gioiTinh;
    private JTextField txt_timKiem;
    private DefaultTableModel model_dsKhachHang;
    private KhachHang_BUS khBus = KhachHang_BUS.getInstance();

    public KhachHang_GUI() {
        this.setLayout(new BorderLayout());
        this.add(panel_khachHang, BorderLayout.CENTER);
        textField4.setBorder(null);
        initTable();
        loadTableFromList(khBus.getAll());

        xuLySuKien();
    }

    private void initTable() {
        String[] columns = {
                "STT",
                "Mã khách hàng",
                "Tên khách hàng",
                "Ngày sinh",
                "Địa chỉ",
                "Giới tính",
                "Số điện thoại",
                "Điểm hạng",
                "Điểm thưởng",
                "Hạng",
                "Ngày ĐK thành viên"
        };
        model_dsKhachHang = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table_dsKhachHang.setModel(model_dsKhachHang);

        table_dsKhachHang.getTableHeader().setResizingAllowed(false);
        table_dsKhachHang.getTableHeader().setReorderingAllowed(false);

        table_dsKhachHang.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table_dsKhachHang.getColumnModel().getColumn(0).setPreferredWidth(90);
        table_dsKhachHang.getColumnModel().getColumn(1).setPreferredWidth(180);
        table_dsKhachHang.getColumnModel().getColumn(2).setPreferredWidth(300);
        table_dsKhachHang.getColumnModel().getColumn(3).setPreferredWidth(400);
        table_dsKhachHang.getColumnModel().getColumn(4).setPreferredWidth(188);

        table_dsKhachHang.setRowHeight(25);
        table_dsKhachHang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        src_dsKhachHang.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        src_dsKhachHang.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
    }

    private void xuLySuKien() {
        table_dsKhachHang.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                hienChiTiet();
        });

        btn_them.addActionListener((ActionEvent e) -> {

            btn_them.setEnabled(false);

            isAdding = true;
            isUpdating = false;

            txt_maKH.setText(khBus.getNextId());
            txt_maKH.setEditable(false);

            txt_tenKH.setText("");
            txt_tenKH.setEditable(true);

            txt_ngaySinh.setText("");
            txt_ngaySinh.setEditable(true);

            txt_diaChi.setText("");
            txt_diaChi.setEditable(true);

            rd_nam.setSelected(true);
            rd_nu.setSelected(false);

            txt_sdt.setText("");
            txt_sdt.setEditable(true);

            txt_diemHang.setText("0");
            txt_diemHang.setEditable(false);

            txt_diemThuong.setText("0");
            txt_diemThuong.setEditable(false);

            txt_hang.setText("");
            txt_hang.setEditable(false);

            txt_ngayDKThanhVien.setText(LocalDate.now().toString());
            txt_ngayDKThanhVien.setEditable(false);

            btn_luu.setVisible(true);
            btn_huy.setVisible(true);

        });

        btn_luu.addActionListener(e ->

        {

            String ma = txt_maKH.getText();
            String ten = txt_tenKH.getText();
            String ngaySinh = txt_ngaySinh.getText();
            String diaChi = txt_diaChi.getText();
            String gioiTinh = rd_nam.isSelected() ? "Nam" : "Nữ";
            String sdt = txt_sdt.getText();
            int diemHang = Integer.parseInt(txt_diemHang.getText());
            int diemThuong = Integer.parseInt(txt_diemThuong.getText());
            String hang = txt_hang.getText();
            String ngayDKThanhVien = txt_ngayDKThanhVien.getText();

            KhachHang_DTO kh = new KhachHang_DTO(
                    ma, ten, ngaySinh, diaChi,
                    gioiTinh, sdt,
                    diemHang, diemThuong,
                    hang, ngayDKThanhVien
            );

            boolean result = false;

            if (kiemTraHopLe(kh)) {
                if (isAdding)
                    result = khBus.them(kh);

                if (result) {
                    JOptionPane.showMessageDialog(this, "Lưu thành công");
                    loadTableFromList(khBus.getAll());
                    resetState();
                } else {
                    JOptionPane.showMessageDialog(this, "Lưu thất bại");
                }
            }

            btn_them.setEnabled(true);
        });


        btn_huy.addActionListener(e ->
        {

            btn_them.setEnabled(true);

            resetState();
        });


        txt_timKiem.addKeyListener(new

                                           KeyAdapter() {
                                               @Override
                                               public void keyReleased(KeyEvent e) {

                                                   String keyword = txt_timKiem.getText().trim();

                                                   if (keyword.isEmpty()) {
                                                       loadTableFromList(khBus.getAll());
                                                       return;
                                                   }

                                                   ArrayList<KhachHang_DTO> list =
                                                           khBus.timKiem(keyword);

                                                   loadTableFromList(list);
                                               }
                                           });


        btn_timKiem.addActionListener(e ->

        {

            String keyword = txt_timKiem.getText().trim();

            ArrayList<KhachHang_DTO> list =
                    khBus.timKiem(keyword);

            loadTableFromList(list);
        });


        btn_thoat.addActionListener(e ->

        {

            txt_timKiem.setText("");

            loadTableFromList(khBus.getAll());
        });

    }

    private void hienChiTiet() {

        int row = table_dsKhachHang.getSelectedRow();

        if (row >= 0) {

            txt_maKH.setText(table_dsKhachHang.getValueAt(row, 1).toString());
            txt_tenKH.setText(table_dsKhachHang.getValueAt(row, 2).toString());
            txt_ngaySinh.setText(table_dsKhachHang.getValueAt(row, 3).toString());
            txt_diaChi.setText(table_dsKhachHang.getValueAt(row, 4).toString());
            String gioiTinh = table_dsKhachHang.getValueAt(row, 5).toString();

            if (gioiTinh.equalsIgnoreCase("Nam")) {
                rd_nam.setSelected(true);
            } else {
                rd_nu.setSelected(true);
            }
            txt_sdt.setText(table_dsKhachHang.getValueAt(row, 6).toString());
            txt_diemHang.setText(table_dsKhachHang.getValueAt(row, 7).toString());
            txt_diemThuong.setText(table_dsKhachHang.getValueAt(row, 8).toString());
            txt_hang.setText(table_dsKhachHang.getValueAt(row, 9).toString());
            txt_ngayDKThanhVien.setText(table_dsKhachHang.getValueAt(row, 10).toString());
        }
    }


    private void loadTableFromList(ArrayList<KhachHang_DTO> list) {
        model_dsKhachHang.setRowCount(0); // Chỉ để 1 dòng này ở đầu hàm
        int stt = 1;

        for (KhachHang_DTO kh : list) { // Chỉ dùng 1 vòng lặp duy nhất

            model_dsKhachHang.addRow(new Object[]{
                    stt++,
                    kh.getMa(),
                    kh.getTen(),
                    kh.getNgaySinh(),
                    kh.isGioiTinh(),
                    kh.getSdt(),
                    kh.getDiemHang(),
                    kh.getDiemThuong(),
                    kh.getHang(),
                    kh.getNgayDKThanhVien()
            });
        }
    }
     private   void resetState () {

            clearForm();

            txt_tenKH.setEditable(false);
            txt_ngaySinh.setEditable(false);
            txt_diaChi.setEditable(false);
            txt_gioiTinh.setEnabled(false);
            txt_sdt.setEditable(false);

            btn_luu.setVisible(false);
            btn_huy.setVisible(false);

            isAdding = false;
        }


    private void clearForm() {

        txt_maKH.setText("");
        txt_tenKH.setText("");
        txt_ngaySinh.setText("");
        txt_diaChi.setText("");
        txt_sdt.setText("");
        txt_diemHang.setText("");
        txt_diemThuong.setText("");
        txt_hang.setText("");
        txt_ngayDKThanhVien.setText("");

        txt_gioiTinh.setPreferredSize(new Dimension(0, 0));
    }
    private boolean kiemTraHopLe(KhachHang_DTO kh) {
        if (kh.getTen().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên khách hàng không được để trống!");
            return false;
        }
        return true;
    }
}