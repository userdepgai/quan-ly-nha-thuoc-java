package gui;

import dto.KhachHang_DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.time.LocalDate;
import bus.KhachHang_BUS;

import java.awt.event.*;

import com.toedter.calendar.JDateChooser;

public class KhachHang_GUI extends JPanel {
    private boolean isAdding = false;
    private boolean isUpdating = false;
    private JPanel panel_khachHang;
    private JButton btn_XuatExcel;
    private JButton btn_NhapExcel;
    private JButton btn_timKiem;
    private JButton btn_thoat;
    private JComboBox cmb_locTrangThai;
    private JButton btn_them;
    private JPanel panelThongTin;
    private JTextField txt_maKH;
    private JTextField txt_tenKH;
    private JTextField txt_diaChi;
    private JTextField txt_sdt;
    private JRadioButton rd_nu;
    private JRadioButton rd_nam;
    private JTextField txt_diemThuong;
    private JTable table_dsKhachHang;
    private JScrollPane src_dsKhachHang;
    private JTextField txt_diemHang;
    private JButton btn_huy;
    private JButton btn_luu;
    private JLabel txt_gioiTinh;
    private JTextField txt_timKiem;
    private JDateChooser JDate_ngaySinh;
    private JDateChooser JDate_ngayDKThanhVien;
    private JComboBox cmb_hang;
    private JComboBox cmb_timKiem;
    private JComboBox cmb_chonHang;
    private JTextField txt_ngaySin;
    private DefaultTableModel model_dsKhachHang;
    private KhachHang_BUS khBus = KhachHang_BUS.getInstance();
    private DefaultTableModel model;
    private ButtonGroup groupGioiTinh;
    public KhachHang_GUI() {

        this.setLayout(new BorderLayout());
        this.add(panel_khachHang, BorderLayout.CENTER);

        initTable();
        groupGioiTinh = new ButtonGroup();
        groupGioiTinh.add(rd_nam);
        groupGioiTinh.add(rd_nu);
        cmb_timKiem.setEditable(true);
        loadSuggestData("Tất cả");
        khBus.refreshData();
        loadTable(khBus.getAll());
        xuLySuKien();
    }


    // ================= INIT TABLE =================
        private void initTable() {
            cmb_hang.setModel(new DefaultComboBoxModel<>(
                    new String[]{
                            "Đồng",
                            "Bạc",
                            "Vàng",
                            "Kim cương"
                    }
            ));
            cmb_chonHang.setModel(new DefaultComboBoxModel<>(
                    new String[]{
                            "--chọn hạng--",
                            "Đồng",
                            "Bạc",
                            "Vàng",
                            "Kim cương"
                    }
            ));

            cmb_locTrangThai.setModel(new DefaultComboBoxModel<>(
                    new String[]{
                            "Tất cả",
                            "Mã KH",
                            "Tên KH",
                            "SĐT"
                    }
            ));
            String[] cols = {
                    "STT",
                    "Mã KH", "Tên KH", "SĐT",
                    "Ngày sinh", "Giới tính",
                    "Điểm thưởng", "Điểm hạng",
                    "Hạng", "Ngày ĐK"
            };

            model = new DefaultTableModel(cols, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            table_dsKhachHang.setModel(model);
            // ===== TẮT GIÃN ĐỀU =====
            table_dsKhachHang.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

            // ===== SET ĐỘ RỘNG CỘT =====
            table_dsKhachHang.getColumnModel().getColumn(0).setPreferredWidth(50);   // STT
            table_dsKhachHang.getColumnModel().getColumn(1).setPreferredWidth(90);   // Mã KH
            table_dsKhachHang.getColumnModel().getColumn(2).setPreferredWidth(160);  // Tên KH
            table_dsKhachHang.getColumnModel().getColumn(3).setPreferredWidth(110);  // SĐT
            table_dsKhachHang.getColumnModel().getColumn(4).setPreferredWidth(110);  // Ngày sinh
            table_dsKhachHang.getColumnModel().getColumn(5).setPreferredWidth(80);   // Giới tính
            table_dsKhachHang.getColumnModel().getColumn(6).setPreferredWidth(100);  // Điểm thưởng
            table_dsKhachHang.getColumnModel().getColumn(7).setPreferredWidth(100);  // Điểm hạng
            table_dsKhachHang.getColumnModel().getColumn(8).setPreferredWidth(100);  // Hạng
            table_dsKhachHang.getColumnModel().getColumn(9).setPreferredWidth(110);  // Ngày ĐK
        }

        // ================= LOAD TABLE =================
        private void loadTable(ArrayList<KhachHang_DTO> list) {

            model.setRowCount(0);

            int stt = 1;

            for (KhachHang_DTO kh : list) {

                model.addRow(new Object[]{
                        stt++,
                        kh.getMa(),
                        kh.getTen(),
                        kh.getSdt(),
                        kh.getNgaySinh(),
                        kh.isGioiTinh() ? "Nam" : "Nữ",
                        kh.getDiemThuong(),
                        kh.getDiemHang(),
                        kh.getHang(),
                        kh.getNgayDKThanhVien()
                });
            }
        }
        // ================= AUTO MÃ =================
        private void autoMaKH() {
            txt_maKH.setText(khBus.getNextId());
            txt_maKH.setEditable(false);
        }

        // ================= LẤY DỮ LIỆU FORM =================
        private KhachHang_DTO getFormData() {

            try {
                String ma = txt_maKH.getText();
                String ten = txt_tenKH.getText();
                String sdt = txt_sdt.getText();

                if (JDate_ngaySinh.getDate() == null) {
                    JOptionPane.showMessageDialog(this,
                            "Vui lòng chọn ngày sinh (định dạng yyyy-MM-dd)");
                    return null;
                }

                LocalDate ngaySinh = JDate_ngaySinh.getDate()
                        .toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();
                boolean gioiTinh = rd_nam.isSelected();

                int diemThuong = Integer.parseInt(txt_diemThuong.getText());
                int diemHang = Integer.parseInt(txt_diemHang.getText());

                String hang = cmb_hang.getSelectedItem().toString();
                if (JDate_ngayDKThanhVien.getDate() == null) {
                    JOptionPane.showMessageDialog(this,
                            "Vui lòng chọn ngày đăng ký thành viên");
                    return null;
                }
                LocalDate ngayDKThanhVien = JDate_ngayDKThanhVien.getDate()
                        .toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();

                return new KhachHang_DTO(
                        ma, ten, sdt,
                        ngaySinh, gioiTinh,
                        diemThuong, diemHang,
                        hang, ngayDKThanhVien
                );

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Dữ liệu không hợp lệ");
                return null;
            }
        }

        // ================= CLEAR FORM =================
        private void clearForm() {

        txt_tenKH.setText("");
        txt_sdt.setText("");

        JDate_ngaySinh.setDate(null);
        JDate_ngayDKThanhVien.setDate(null);

        txt_diemThuong.setText("0");
        txt_diemHang.setText("0");

        cmb_hang.setSelectedItem("Đồng");

        rd_nu.setSelected(true);
    }

        // ================= XỬ LÝ SỰ KIỆN =================
        private void xuLySuKien() {
            // ===== AUTO SUGGEST KHI GÕ =====
            JTextField editor = (JTextField) cmb_timKiem.getEditor().getEditorComponent();

            editor.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {

                    String text = editor.getText();
                    String loai = cmb_locTrangThai.getSelectedItem().toString();

                    DefaultComboBoxModel<String> newModel = new DefaultComboBoxModel<>();

                    for (KhachHang_DTO kh : khBus.getAll()) {

                        switch (loai) {

                            case "Mã KH":
                                if (kh.getMa().toLowerCase().contains(text.toLowerCase()))
                                    newModel.addElement(kh.getMa());
                                break;

                            case "Tên KH":
                                if (kh.getTen().toLowerCase().contains(text.toLowerCase()))
                                    newModel.addElement(kh.getTen());
                                break;

                            case "SĐT":
                                if (kh.getSdt().contains(text))
                                    newModel.addElement(kh.getSdt());
                                break;

                            default:
                                if (kh.getMa().contains(text) ||
                                        kh.getTen().toLowerCase().contains(text.toLowerCase()) ||
                                        kh.getSdt().contains(text)) {

                                    newModel.addElement(kh.getMa());
                                }
                        }
                    }

                    cmb_timKiem.setModel(newModel);
                    editor.setText(text);
                    cmb_timKiem.showPopup();
                }
            });

                cmb_locTrangThai.addActionListener(e -> {
                String loai = cmb_locTrangThai.getSelectedItem().toString();
                loadSuggestData(loai);
            });
            // ===== THÊM =====
            btn_them.addActionListener(e -> {

                isAdding = true;
                isUpdating = false;

                clearForm();
                autoMaKH();

                // 🔥 set mặc định khi bấm thêm
                txt_diemThuong.setText("0");
                txt_diemHang.setText("0");
                cmb_hang.setSelectedItem("Đồng");

                txt_maKH.setEditable(false);

                btn_luu.setVisible(true);
                btn_huy.setVisible(true);
                btn_them.setEnabled(false);
            });
            // ===== LƯU =====
            btn_luu.addActionListener(e -> {

                KhachHang_DTO kh = getFormData();
                if (kh == null) return;

                boolean result = false;

                if (isAdding) {
                    result = khBus.them(kh);
                }

                if (result) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công");

                    khBus.refreshData();
                    loadTable(khBus.getAll());

                    resetState();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại");

                };
            });

            // ===== CLICK TABLE =====
            table_dsKhachHang.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {

                    int row = table_dsKhachHang.getSelectedRow();
                    if (row < 0) return;

                    txt_maKH.setText(model.getValueAt(row, 1).toString());
                    txt_tenKH.setText(model.getValueAt(row, 2).toString());
                    txt_sdt.setText(model.getValueAt(row, 3).toString());

                    // ===== NGÀY SINH =====
                    try {
                        Object ngaySinhObj = model.getValueAt(row, 4);
                        if (ngaySinhObj != null) {
                            java.sql.Date sqlDate = java.sql.Date.valueOf(ngaySinhObj.toString());
                            JDate_ngaySinh.setDate(sqlDate);
                        } else {
                            JDate_ngaySinh.setDate(null);
                        }
                    } catch (Exception ex) {
                        JDate_ngaySinh.setDate(null);
                    }

                    // ===== GIỚI TÍNH =====
                    if (model.getValueAt(row, 4).toString().equals("Nam"))
                        rd_nam.setSelected(true);
                    else
                        rd_nu.setSelected(true);

                    // ===== ĐIỂM =====
                    txt_diemThuong.setText(String.valueOf(model.getValueAt(row, 6)));
                    txt_diemHang.setText(String.valueOf(model.getValueAt(row, 7)));

                    // ===== HẠNG =====
                    cmb_hang.setSelectedItem(model.getValueAt(row, 8).toString());

                    // ===== NGÀY ĐĂNG KÝ =====
                    try {
                        Object ngayDKObj = model.getValueAt(row, 9);
                        if (ngayDKObj != null) {
                            java.sql.Date sqlDateDK = java.sql.Date.valueOf(ngayDKObj.toString());
                            JDate_ngayDKThanhVien.setDate(sqlDateDK);
                        } else {
                            JDate_ngayDKThanhVien.setDate(null);
                        }
                    } catch (Exception ex) {
                        JDate_ngayDKThanhVien.setDate(null);
                    }
                }
            });

            // ===== TÌM KIẾM =====
            btn_timKiem.addActionListener(e -> {

                String keyword = cmb_timKiem.getEditor().getItem().toString();
                String loai = cmb_locTrangThai.getSelectedItem().toString();

                loadTable(khBus.timKiem(keyword, loai));
            });

            // ===== HỦY =====
            btn_huy.addActionListener(e -> {
                resetState();
            });
            btn_thoat.addActionListener(e -> {
                ((JTextField) cmb_timKiem.getEditor().getEditorComponent()).setText("");
                khBus.refreshData();
                loadTable(khBus.getAll());
                clearForm();
            });

        }
    private void resetState() {

        clearForm();

        btn_luu.setVisible(false);
        btn_huy.setVisible(false);

        btn_them.setEnabled(true);

        isAdding = false;
        isUpdating = false;
    }
    private void createUIComponents() {
        JDate_ngaySinh = new JDateChooser();
        JDate_ngaySinh.setDateFormatString("yyyy-MM-dd");
        JDate_ngayDKThanhVien = new JDateChooser();
        JDate_ngayDKThanhVien.setDateFormatString("yyyy-MM-dd");

    }
    private void loadSuggestData(String loai) {

        DefaultComboBoxModel<String> modelSuggest = new DefaultComboBoxModel<>();

        for (KhachHang_DTO kh : khBus.getAll()) {

            switch (loai) {
                case "Mã KH":
                    modelSuggest.addElement(kh.getMa());
                    break;

                case "Tên KH":
                    modelSuggest.addElement(kh.getTen());
                    break;

                case "SĐT":
                    modelSuggest.addElement(kh.getSdt());
                    break;

                default:
                    modelSuggest.addElement(kh.getMa());
                    modelSuggest.addElement(kh.getTen());
                    modelSuggest.addElement(kh.getSdt());
            }
        }

        cmb_timKiem.setModel(modelSuggest);
        cmb_timKiem.setSelectedItem(null);
        ((JTextField) cmb_timKiem.getEditor().getEditorComponent()).setText("");
    }
    }