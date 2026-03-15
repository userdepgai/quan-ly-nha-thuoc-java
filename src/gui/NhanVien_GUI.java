package gui;

import javax.swing.*;
import java.awt.*;

import bus.DiaChi_BUS;
import bus.NhanVien_BUS;
import bus.PhanQuyen_BUS;
import bus.TaiKhoan_BUS;
import com.toedter.calendar.JDateChooser;
import dto.DIACHI_DTO;
import dto.NhanVien_DTO;
import dto.PhanQuyen_DTO;

import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;

import java.time.LocalDate;

public class NhanVien_GUI extends JPanel {
    private JPanel panel_nhanVien;
    private JButton btnXuatExcel;
    private JButton btnTimKiem;
    private JButton btn_thoat;
    private JComboBox cmb_locTrangThai;
    private JTable table1;
    private JPanel panelThongTin;
    private JTextField txt_maNV;
    private JTextField txt_tenNV;
    private JButton btn_capNhat;
    private JButton btn_them;
    private JTextField txt_diaChi;
    private JTextField txt_sdt;
    private JRadioButton rd_nu;
    private JRadioButton rd_nam;
    private JTextField textField4;
    private JTextField txt_luongCoBan;
    private JButton btn_luu;
    private JButton btn_huy;
    private JDateChooser JDate_ngayVaoLam;
    private JDateChooser JDate_ngaySinh;
    private JComboBox cmb_timKiem;
    private JComboBox cmb_chucVu;
    private JComboBox cmb_trangThai;
    private DefaultTableModel model;
    private ButtonGroup groupGioiTinh;
    private JComboBox<String> cmb_chonChucVu;
    private JComboBox<String> cmb_chonTrangThai;
    private JScrollPane src_dsNhanVien;
    private JDateChooser JDateChooser1;

    private boolean isInsert = false;
    private boolean isUpdate = false;

    private NhanVien_BUS bus = NhanVien_BUS.getInstance();
    public NhanVien_GUI() {
        this.setLayout(new BorderLayout());
        this.add(panel_nhanVien, BorderLayout.CENTER);
        cmb_timKiem.setEditable(true);
        initTable();
        initComponent();
        loadTable(bus.getAll());
        setViewMode();
        xuLySuKien();
    }

    private void initComponent() {

        groupGioiTinh = new ButtonGroup();
        groupGioiTinh.add(rd_nam);
        groupGioiTinh.add(rd_nu);

        cmb_trangThai.setModel(new DefaultComboBoxModel<>(
                new String[]{"Đang làm", "Nghỉ"}
        ));

        taoCmbChucVu();
        taoCmbChonChucVu();

        cmb_chonTrangThai.setModel(new DefaultComboBoxModel<>(
                new String[]{
                        "--chọn trạng thái--",
                        "Đang làm",
                        "Nghỉ"
                }
        ));

    }
    private void taoCmbChucVu() {
        cmb_chucVu.removeAllItems();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for(PhanQuyen_DTO q : bus.getQuyenNhanVienHoatDong()){
            model.addElement(q.getTenQuyen());
        }
        cmb_chucVu.setModel(model);
    }
    private void taoCmbChonChucVu() {
        cmb_chonChucVu.removeAllItems();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("--Chọn chức vụ--");
        for(PhanQuyen_DTO q : bus.getQuyenKhongCoCuaHang()){
            model.addElement(q.getTenQuyen());
        }
        cmb_chonChucVu.setModel(model);
    }

    private void initTable() {
        cmb_locTrangThai.setModel(new DefaultComboBoxModel<>(
                new String[]{
                        "Tất cả",
                        "Mã NV",
                        "Tên NV",
                        "SĐT"
                }
        ));
        String[] cols = {
                "STT",
                "Mã NV", "Tên NV", "SĐT",
                "Ngày sinh", "Giới tính",
                "Chức vụ", "Lương",
                "Ngày vào làm", "Trạng thái"
        };

        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table1.setModel(model);
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        table1.getTableHeader().setResizingAllowed(false);
        table1.getTableHeader().setReorderingAllowed(false);

        table1.getColumnModel().getColumn(0).setPreferredWidth(50);   // STT
        table1.getColumnModel().getColumn(1).setPreferredWidth(90);   // Mã NV
        table1.getColumnModel().getColumn(2).setPreferredWidth(160);  // Tên NV
        table1.getColumnModel().getColumn(3).setPreferredWidth(110);  // SĐT
        table1.getColumnModel().getColumn(4).setPreferredWidth(110);  // Ngày sinh
        table1.getColumnModel().getColumn(5).setPreferredWidth(80);   // Giới tính
        table1.getColumnModel().getColumn(6).setPreferredWidth(160);  // Chức vụ
        table1.getColumnModel().getColumn(7).setPreferredWidth(120);  // Lương
        table1.getColumnModel().getColumn(8).setPreferredWidth(110);  // Ngày vào làm
        table1.getColumnModel().getColumn(9).setPreferredWidth(90);   // Trạng thái

        table1.setRowHeight(25);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        src_dsNhanVien.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        src_dsNhanVien.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
    }

    private void loadTable(ArrayList<NhanVien_DTO> list) {

        model.setRowCount(0);

        int stt = 1;

        for (NhanVien_DTO nv : list) {
            model.addRow(new Object[]{
                    stt++,
                    nv.getMa(),
                    nv.getTen(),
                    nv.getSdt(),
                    nv.getNgaySinh(),
                    nv.isGioiTinh() ? "Nam" : "Nữ",
                    nv.getChucVu(),
                    String.format("%,.0f", nv.getLuongCoBan()),
                    nv.getNgayVaoLam(),
                    nv.getTrangThai() == 1 ? "Đang làm" : "Nghỉ"
            });
        }

    }
    private void clearForm() {

        txt_maNV.setText("");
        txt_tenNV.setText("");
        txt_sdt.setText("");
        txt_luongCoBan.setText("");
        JDateChooser1.setDate(null);
        txt_diaChi.setText("");
        JDate_ngayVaoLam.setDate(null);

        rd_nu.setSelected(true);
        cmb_chucVu.setSelectedIndex(0);
        cmb_trangThai.setSelectedIndex(0);
        }
    private void lockTable() {
        table1.setRowSelectionAllowed(false);
        table1.setEnabled(false);
    }
    private void setViewMode() {

        unlockTable();

        txt_maNV.setEditable(false);
        txt_tenNV.setEditable(false);
        txt_sdt.setEditable(false);
        txt_diaChi.setEditable(false);
        txt_luongCoBan.setEditable(false);
        JDateChooser1.setEnabled(false);

        rd_nam.setEnabled(false);
        rd_nu.setEnabled(false);

        cmb_chucVu.setEnabled(false);
        cmb_trangThai.setEnabled(false);

        JDate_ngayVaoLam.setEnabled(false);

        btn_luu.setVisible(false);
        btn_huy.setVisible(false);

        btn_them.setEnabled(true);
        btn_capNhat.setEnabled(true);
    }
    private void setUpdateMode() {

        lockTable();

        txt_tenNV.setEditable(true);
        txt_sdt.setEditable(true);
        txt_diaChi.setEditable(true);
        txt_luongCoBan.setEditable(true);
        JDateChooser1.setEnabled(true);

        rd_nam.setEnabled(true);
        rd_nu.setEnabled(true);

        cmb_chucVu.setEnabled(true);
        taoCmbChucVu();
        cmb_trangThai.setEnabled(true);

        JDate_ngayVaoLam.setEnabled(true);

        btn_luu.setVisible(true);
        btn_huy.setVisible(true);

        btn_them.setEnabled(false);
        btn_capNhat.setEnabled(false);
    }
    private void unlockTable() {
        table1.setRowSelectionAllowed(true);
        table1.setEnabled(true);
    }
    private void resetState() {

        clearForm();

        btn_luu.setVisible(false);
        btn_huy.setVisible(false);
        txt_maNV.setEditable(true);
    }
    private void xuLySuKien() {
        initSearchSuggest();

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int row = table1.getSelectedRow();

                if(row < 0) return;

                hienChiTietNhanVien(row);
            }
        });
        cmb_locTrangThai.addActionListener(e -> {
            String loai = cmb_locTrangThai.getSelectedItem().toString();
            loadSuggestData(loai);
        });
        btnTimKiem.addActionListener(e -> {
            loadTable(timKiem());
        });
        btn_them.addActionListener(e -> xuLyThem());

        btn_capNhat.addActionListener(e -> xuLyCapNhat());

        btn_luu.addActionListener(e -> xuLyLuu());

        btn_huy.addActionListener(e -> xuLyHuy());
        btn_thoat.addActionListener(e -> {

            cmb_chonTrangThai.setSelectedIndex(0);
            cmb_chonChucVu.setSelectedIndex(0);

            ((JTextField)cmb_timKiem.getEditor()
                    .getEditorComponent()).setText("");

            loadTable(bus.getAll());
        });
        btnXuatExcel.addActionListener(e -> {
            btnXuat(e);
        });
    }

    private void xuLyThem(){
        isInsert = true;
        isUpdate = false;
        clearForm();
        txt_maNV.setText(bus.getNextId());
        JDate_ngayVaoLam.setDate(new java.util.Date());
        taoCmbChucVu();

        enableForm(true);
    }
    private void xuLyCapNhat(){
        int row = table1.getSelectedRow();
        if(row < 0){
            JOptionPane.showMessageDialog(null,"Chọn nhân viên cần cập nhật");
            return;
        }

        isInsert = false;
        isUpdate = true;

        String chucVuHienTai = model.getValueAt(row,6).toString();
        cmb_chucVu.removeAllItems();

        ArrayList<String> list = bus.getTenQuyenKhongCoCuaHangVaHoatDong();
        boolean tonTai = false;
        for(String ten : list){
            if(ten.equals(chucVuHienTai)){
                tonTai = true;
            }
            cmb_chucVu.addItem(ten);
        }
        if(!tonTai){
            cmb_chucVu.insertItemAt(chucVuHienTai,0);
        }
        cmb_chucVu.setSelectedItem(chucVuHienTai);

        enableForm(true);
    }
    private void xuLyLuu(){
        if(!kiemTraDuLieu()){
            return;
        }
        String sdt = txt_sdt.getText();
        String chucVuMoi = cmb_chucVu.getSelectedItem().toString();
        PhanQuyen_DTO QuyenMoi = PhanQuyen_BUS.getInstance().getByName(chucVuMoi);
        String maQuyenMoi = QuyenMoi.getMaQuyen();
        NhanVien_DTO nv = layDuLieuForm();
        if(nv == null){
            return;
        }
        boolean result;
        if(isInsert){
            result = bus.them(nv);
            if(result){
                bus.taoTaiKhoanNhanVien(sdt,maQuyenMoi);
            }
        }else{
            NhanVien_DTO nvCu = bus.getById(nv.getMa());
            String chucVuCu = nvCu.getChucVu();
            String maQuyenCu = TaiKhoan_BUS.getInstance().getMaQuyen(chucVuCu);
            result = bus.capNhat(nv);
            if(result){
                if(!chucVuCu.equals(chucVuMoi)){
                    bus.capNhatQuyenTaiKhoan(
                            sdt,
                            maQuyenCu,
                            maQuyenMoi
                    );
                }
            }
        }

        if(result){
            JOptionPane.showMessageDialog(null,"Lưu thành công");
            loadTable(bus.getAll());
            enableForm(false);
            isInsert = false;
            isUpdate = false;
        }
    }
    private void xuLyHuy(){
        isInsert = false;
        isUpdate = false;
        taoCmbChucVu();
        enableForm(false);
        clearForm();

    }
    private NhanVien_DTO layDuLieuForm(){

        NhanVien_DTO nv = new NhanVien_DTO();

        nv.setMa(txt_maNV.getText());
        nv.setTen(txt_tenNV.getText());
        nv.setSdt(txt_sdt.getText());

        String tenQuyen = cmb_chucVu.getSelectedItem().toString();
        nv.setChucVu(tenQuyen);

        PhanQuyen_DTO Quyen = PhanQuyen_BUS.getInstance().getByName(tenQuyen);
        nv.setMaQuyen(Quyen.getMaQuyen());

        nv.setLuongCoBan(parseLuong(txt_luongCoBan.getText()));

        nv.setTrangThai(
                cmb_trangThai.getSelectedItem().toString().equals("Đang làm") ? 1 : 0
        );

        LocalDate ngaySinh = JDateChooser1.getDate()
                .toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();

        nv.setNgaySinh(ngaySinh);

        LocalDate ngayVaoLam = JDate_ngayVaoLam.getDate()
                .toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();

        nv.setNgayVaoLam(ngayVaoLam);

        DIACHI_DTO dc;

        if(isInsert){
            dc = taoDiaChiTuText();
        }else{
            dc = capNhatDiaChiTuText();
        }

        if(dc == null) return null;

        nv.setMaDiaChi(dc.getMaDiaChi());

        return nv;
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
        DiaChi_BUS bus = DiaChi_BUS.getInstance();

        String maDC = bus.getNextId();

        DIACHI_DTO dc = new DIACHI_DTO(maDC, tinh, phuong, duong, soNha);
        boolean ok = bus.them(dc);
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

        String maNV = txt_maNV.getText();
        NhanVien_DTO nv = bus.getById(maNV);
        String maDC = nv.getMaDiaChi();
        DIACHI_DTO dc = new DIACHI_DTO(maDC,tinh,phuong,duong,soNha);
        boolean ok = DiaChi_BUS.getInstance().capNhat(dc);
        if(!ok){
            JOptionPane.showMessageDialog(null,"Cập nhật địa chỉ thất bại");
            return null;
        }

        return dc;
    }
    private void hienChiTietNhanVien(int row){
        txt_maNV.setText(model.getValueAt(row, 1).toString());
        txt_tenNV.setText(model.getValueAt(row, 2).toString());
        txt_sdt.setText(model.getValueAt(row, 3).toString());

        setNgayChoForm(row);

        if (model.getValueAt(row, 5).toString().equals("Nam"))
            rd_nam.setSelected(true);
        else
            rd_nu.setSelected(true);

        String chucVu = model.getValueAt(row,6).toString().trim();
        cmb_chucVu.removeAllItems();
        cmb_chucVu.addItem(chucVu);

        txt_luongCoBan.setText(model.getValueAt(row, 7).toString());

        cmb_trangThai.setSelectedItem(model.getValueAt(row, 9).toString());

        String maNV = model.getValueAt(row,1).toString();
        NhanVien_DTO nv = bus.getById(maNV);
        String maDC = nv.getMaDiaChi();

        String diaChi = bus.getDiaChiByMaDC(maDC);

        txt_diaChi.setText(diaChi);
    }
    private void setNgayChoForm(int row){
        try{
            java.sql.Date ns = java.sql.Date.valueOf(model.getValueAt(row,4).toString());
            JDateChooser1.setDate(ns);
            java.sql.Date nvl = java.sql.Date.valueOf(model.getValueAt(row,8).toString());
            JDate_ngayVaoLam.setDate(nvl);
        }
        catch(Exception ex){
            JDateChooser1.setDate(null);
            JDate_ngayVaoLam.setDate(null);
        }
    }
    private ArrayList<NhanVien_DTO> timKiem(){
        String keyword = getKeyword();

        String chucVu = getFilterChucVu();

        Integer trangThai = getFilterTrangThai();

        return bus.timKiem(keyword, chucVu, trangThai);
    }
    private String getKeyword(){
        return ((JTextField)cmb_timKiem.getEditor().getEditorComponent()).getText().trim();
    }
    private String getFilterChucVu(){
        if(cmb_chonChucVu.getSelectedIndex() > 0){
            return cmb_chonChucVu.getSelectedItem().toString();
        }
        return null;
    }
    private Integer getFilterTrangThai(){
        if(cmb_chonTrangThai.getSelectedIndex() == 1)
            return 1;
        if(cmb_chonTrangThai.getSelectedIndex() == 2)
            return 0;
        return null;
    }

    private void initSearchSuggest(){
        JTextField editor = (JTextField) cmb_timKiem.getEditor().getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = editor.getText().toLowerCase();
                String loai = cmb_locTrangThai.getSelectedItem().toString();
                updateSuggest(text, loai);
            }
        });
    }
    private void updateSuggest(String text, String loai){
        DefaultComboBoxModel<String> newModel = new DefaultComboBoxModel<>();
        for (NhanVien_DTO nv : bus.getAll()) {
            switch (loai) {
                case "Mã NV":
                    if (nv.getMa().toLowerCase().contains(text))
                        newModel.addElement(nv.getMa());
                    break;

                case "Tên NV":
                    if (nv.getTen().toLowerCase().contains(text))
                        newModel.addElement(nv.getTen());
                    break;

                case "SĐT":
                    if (nv.getSdt().contains(text))
                        newModel.addElement(nv.getSdt());
                    break;

                case "Tất cả":

                    if (nv.getMa().toLowerCase().contains(text))
                        newModel.addElement(nv.getMa());

                    if (nv.getTen().toLowerCase().contains(text))
                        newModel.addElement(nv.getTen());

                    if (nv.getSdt().contains(text))
                        newModel.addElement(nv.getSdt());

                    break;
            }
        }
        cmb_timKiem.setModel(newModel);
        cmb_timKiem.setSelectedItem(text);
        cmb_timKiem.showPopup();
    }
    private void createUIComponents() {
        JDateChooser1 = new JDateChooser();
        JDateChooser1.setDateFormatString("yyyy-MM-dd");

        JDate_ngayVaoLam = new JDateChooser();
        JDate_ngayVaoLam.setDateFormatString("yyyy-MM-dd");
    }
    private void loadSuggestData(String loai) {

        DefaultComboBoxModel<String> modelSuggest = new DefaultComboBoxModel<>();

        for (NhanVien_DTO nv : bus.getAll()) {

            switch (loai) {

                case "Mã NV":
                    modelSuggest.addElement(nv.getMa());
                    break;

                case "Tên NV":
                    modelSuggest.addElement(nv.getTen());
                    break;

                case "SĐT":
                    modelSuggest.addElement(nv.getSdt());
                    break;

                case "Tất cả":
                    modelSuggest.addElement(nv.getMa());
                    modelSuggest.addElement(nv.getTen());
                    modelSuggest.addElement(nv.getSdt());
                    break;
            }
            cmb_timKiem.setModel(modelSuggest);
            cmb_timKiem.setSelectedItem(null);
            ((JTextField) cmb_timKiem.getEditor().getEditorComponent()).setText("");
        }

        cmb_timKiem.setModel(modelSuggest);
    }

    private void enableForm(boolean enable){

        txt_tenNV.setEditable(enable);
        txt_sdt.setEditable(enable);
        txt_diaChi.setEditable(enable);
        txt_luongCoBan.setEditable(enable);

        cmb_chucVu.setEnabled(enable);
        cmb_trangThai.setEnabled(enable);

        rd_nam.setEnabled(enable);
        rd_nu.setEnabled(enable);

        JDateChooser1.setEnabled(enable);
        JDate_ngayVaoLam.setEnabled(false);

        btn_luu.setVisible(enable);
        btn_huy.setVisible(enable);

        btn_them.setEnabled(!enable);
        btn_capNhat.setEnabled(!enable);
    }
    private boolean kiemTraDuLieu(){

        if(txt_tenNV.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null,"Tên nhân viên không được trống");
            txt_tenNV.requestFocus();
            return false;
        }

        String sdt = txt_sdt.getText().trim();

        if(!sdt.matches("^0[0-9]{9}$")){
            JOptionPane.showMessageDialog(null,"SĐT không hợp lệ (10 số)");
            txt_sdt.requestFocus();
            return false;
        }

        try{
            Double.parseDouble(txt_luongCoBan.getText());
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Lương cơ bản phải là số");
            txt_luongCoBan.requestFocus();
            return false;
        }
        if(!rd_nam.isSelected() && !rd_nu.isSelected()){
            JOptionPane.showMessageDialog(null,"Vui lòng chọn giới tính");
            return false;
        }

        if(JDateChooser1.getDate() == null){
            JOptionPane.showMessageDialog(null,"Vui lòng chọn ngày sinh");
            return false;
        }

        String diaChi = txt_diaChi.getText().trim();

        if(diaChi.split(",").length != 4){
            JOptionPane.showMessageDialog(null,
                    "Địa chỉ phải đúng định dạng:\nSố nhà, Đường, Quận/Phường, Tỉnh/TP");
            txt_diaChi.requestFocus();
            return false;
        }

        return true;
    }
    private double parseLuong(String luongText){
        if(luongText == null || luongText.trim().isEmpty()){
            return 0;
        }
        luongText = luongText.replace(",", "").trim();

        return Double.parseDouble(luongText);
    }

    private void btnXuat(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showSaveDialog(this);
        if(result == JFileChooser.APPROVE_OPTION){
            String path = chooser.getSelectedFile().getAbsolutePath() + ".xlsx";
            boolean ok = NhanVien_BUS.getInstance().exportExcel(path);
            if(ok)
                JOptionPane.showMessageDialog(this,"Xuất Excel thành công");
            else
                JOptionPane.showMessageDialog(this,"Xuất Excel thất bại");
        }
    }
}