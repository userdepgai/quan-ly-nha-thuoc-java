package gui;

import bus.DiaChi_BUS;
import bus.KhachHang_DiaChi_BUS;
import dto.DIACHI_DTO;
import dto.KhachHang_DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.time.LocalDate;
import bus.KhachHang_BUS;

import java.awt.event.*;

import com.toedter.calendar.JDateChooser;
import dto.KhachHang_DiaChi_DTO;

public class KhachHang_GUI extends JPanel {
    private boolean isAdding = false;
    private boolean isUpdating = false;
    private JPanel panel_khachHang;
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
    private JButton btnCapNhat;
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
        setTrangThaiBanDau();
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
            table_dsKhachHang.getTableHeader().setResizingAllowed(false);
            table_dsKhachHang.getTableHeader().setReorderingAllowed(false);

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

            table_dsKhachHang.setRowHeight(25);
            table_dsKhachHang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            src_dsKhachHang.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            src_dsKhachHang.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
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
                        hangToDisplay(kh.getHang()),
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

                String hang = convertHang(cmb_hang.getSelectedItem().toString());
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
        txt_diaChi.setText("");

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
            btn_them.addActionListener(e -> xuLyThem());

            btnCapNhat.addActionListener(e -> xuLyCapNhat());

            btn_luu.addActionListener(e -> xuLyLuu());

            btn_huy.addActionListener(e -> xuLyHuy());
            table_dsKhachHang.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {

                    int row = table_dsKhachHang.getSelectedRow();
                    if (row < 0) return;

                    hienThiChiTiet(row);
                }
            });


            btn_timKiem.addActionListener(e -> {

                String keyword = cmb_timKiem.getEditor().getItem().toString();
                String loai = cmb_locTrangThai.getSelectedItem().toString();

                loadTable(khBus.timKiem(keyword, loai));
            });

            btn_thoat.addActionListener(e -> {
                ((JTextField) cmb_timKiem.getEditor().getEditorComponent()).setText("");
                khBus.refreshData();
                loadTable(khBus.getAll());
                clearForm();
            });
        }

    private void hienThiChiTiet(int row) {

        String maKH = model.getValueAt(row, 1).toString();
        txt_maKH.setText(maKH);
        txt_tenKH.setText(model.getValueAt(row, 2).toString());
        txt_sdt.setText(model.getValueAt(row, 3).toString());

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

        if (model.getValueAt(row, 5).toString().equals("Nam"))
            rd_nam.setSelected(true);
        else
            rd_nu.setSelected(true);

        txt_diemThuong.setText(String.valueOf(model.getValueAt(row, 6)));
        txt_diemHang.setText(String.valueOf(model.getValueAt(row, 7)));

        cmb_hang.setSelectedItem(model.getValueAt(row, 8).toString());

        txt_diaChi.setText(khBus.getDiaChiMacDinhText(maKH));

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

    private void xuLyThem(){
        isAdding = true;
        isUpdating = false;
        clearForm();
        autoMaKH();
        enableEditThongTin();
        txt_diemThuong.setText("0");
        txt_diemHang.setText("0");
        cmb_hang.setSelectedItem("Đồng");
        JDate_ngayDKThanhVien.setDate(new java.util.Date());

        btn_them.setEnabled(false);
        btnCapNhat.setEnabled(false);

        btn_luu.setVisible(true);
        btn_huy.setVisible(true);
    }
    private void xuLyCapNhat(){
        int row = table_dsKhachHang.getSelectedRow();
        if(row < 0){
            JOptionPane.showMessageDialog(this,"Vui lòng chọn khách hàng");
            return;
        }
        isAdding = false;
        isUpdating = true;

        enableEditThongTin();
        txt_sdt.setEditable(false);
        btn_them.setEnabled(false);
        btnCapNhat.setEnabled(false);

        btn_luu.setVisible(true);
        btn_huy.setVisible(true);
    }
    private void xuLyLuu(){
        if(!kiemTraForm()) return;
        KhachHang_DTO kh = getFormData();
        if(kh == null) return;
        boolean result = false;
        if(isAdding){
            if(checkSDTTonTai(kh.getSdt())){
                JOptionPane.showMessageDialog(this,"SĐT đã tồn tại");
                return;
            }
            DIACHI_DTO dc = taoDiaChiTuText();
            if(dc == null) return;
            result = khBus.them(kh);
            if(result){
                KhachHang_DiaChi_DTO khdc = new KhachHang_DiaChi_DTO(kh.getMa(), dc.getMaDiaChi(), 1);
                KhachHang_DiaChi_BUS.getInstance().them(khdc);
            }
        }
        if(isUpdating){
            result = khBus.capNhat(kh);
            if(result){
                capNhatDiaChiTuText();
            }
        }
        if(result){
            JOptionPane.showMessageDialog(this,"Lưu thành công");
            khBus.refreshData();
            loadTable(khBus.getAll());
            setTrangThaiBanDau();
        }
    }
    private void xuLyHuy(){
        setTrangThaiBanDau();
    }
    private void setTrangThaiBanDau(){
        clearForm();

        txt_maKH.setEditable(false);
        txt_tenKH.setEditable(false);
        txt_sdt.setEditable(false);
        txt_diaChi.setEditable(false);

        JDate_ngaySinh.setEnabled(false);
        JDate_ngayDKThanhVien.setEnabled(false);

        rd_nam.setEnabled(false);
        rd_nu.setEnabled(false);

        txt_diemThuong.setEditable(false);
        txt_diemHang.setEditable(false);

        cmb_hang.setEnabled(false);

        btn_luu.setVisible(false);
        btn_huy.setVisible(false);

        btn_them.setEnabled(true);
        btnCapNhat.setEnabled(true);

        isAdding = false;
        isUpdating = false;
    }
    private void enableEditThongTin(){
        txt_tenKH.setEditable(true);
        txt_sdt.setEditable(true);
        txt_diaChi.setEditable(true);
        JDate_ngaySinh.setEnabled(true);
        rd_nam.setEnabled(true);
        rd_nu.setEnabled(true);
    }

    private boolean kiemTraForm(){
        if(txt_tenKH.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this,"Tên khách hàng không được trống");
            return false;
        }
        if(txt_sdt.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this,"SĐT không được trống");
            return false;
        }
        if(JDate_ngaySinh.getDate()==null){
            JOptionPane.showMessageDialog(this,"Vui lòng chọn ngày sinh");
            return false;
        }
        return true;
    }
    private boolean checkSDTTonTai(String sdt){
        for(KhachHang_DTO kh : khBus.getAll()){
            if(kh.getSdt().equals(sdt)){
                return true;
            }
        }
        return false;
    }
    private String convertHang(String hangHienThi){
        switch (hangHienThi){
            case "Đồng":
                return "DONG";
            case "Bạc":
                return "BAC";
            case "Vàng":
                return "VANG";
            case "Kim cương":
                return "KIMCUONG";
            default:
                return "DONG";
        }
    }
    private String hangToDisplay(String hang){
        switch (hang){
            case "DONG": return "Đồng";
            case "BAC": return "Bạc";
            case "VANG": return "Vàng";
            case "KIMCUONG": return "Kim cương";
        }
        return hang;
    }
    private DIACHI_DTO taoDiaChiTuText(){
        String input = txt_diaChi.getText().trim();
        String[] parts = input.split(",");
        if(parts.length != 4){
            JOptionPane.showMessageDialog(null,
                    "Địa chỉ phải đúng định dạng:\nSố nhà, Đường, Phường/Quận, Tỉnh/TP");
            return null;
        }
        String soNha = parts[0].trim();
        String duong = parts[1].trim();
        String phuong = parts[2].trim();
        String tinh = parts[3].trim();
        DiaChi_BUS busDC = DiaChi_BUS.getInstance();
        String maDC = busDC.getNextId();
        DIACHI_DTO dc = new DIACHI_DTO(maDC, tinh, phuong, duong, soNha);

        boolean ok = busDC.them(dc);

        if(!ok){
            JOptionPane.showMessageDialog(null,"Tạo địa chỉ thất bại");
            return null;
        }
        return dc;
    }
    private DIACHI_DTO capNhatDiaChiTuText(){

        String input = txt_diaChi.getText().trim();

        String[] parts = input.split(",");

        if(parts.length != 4){
            JOptionPane.showMessageDialog(null,
                    "Địa chỉ phải đúng định dạng:\nSố nhà, Đường, Phường, Tỉnh");
            return null;
        }

        String soNha = parts[0].trim();
        String duong = parts[1].trim();
        String phuong = parts[2].trim();
        String tinh = parts[3].trim();

        String maKH = txt_maKH.getText();

        String maDC = KhachHang_DiaChi_BUS.getInstance().getMacDinh(maKH).getMaDiaChi();

        DIACHI_DTO dc = new DIACHI_DTO(maDC,tinh,phuong,duong,soNha);
        boolean ok = DiaChi_BUS.getInstance().capNhat(dc);
        if(!ok){
            JOptionPane.showMessageDialog(null,"Cập nhật địa chỉ thất bại");
            return null;
        }

        return dc;
    }
}