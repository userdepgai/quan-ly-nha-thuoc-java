package gui;

import bus.LoHang_BUS;
import dto.LoHang_DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class LoHang_GUI extends JPanel{
    private JTable table_dsLo;
    private JTextField txtMaLo;
    private JTextField txtNhaCungCap;
    private JTextField txtKVLT;
    private JButton btnCapNhat;
    private JButton btnThem;
    private JButton btnXuatExcel;
    private JButton btnNhapExcel;
    private JTextField txtLocMaSP;
    private JTextField txtHienCo;
    private JComboBox cmbTinhTrangSP;
    private JComboBox cmbLocNCC;
    private JComboBox cmbLocKVLT;
    private JPanel panel_loHang;
    private JComboBox cmbPNK;
    private JComboBox cmbSanPham;
    private JTextField txtMaSP_SL;
    private JTextField txtGiaNhap;
    private JTextField txtThanhTien;
    private JButton btnThoat;
    private JButton btnTimKiem;
    private JTextField txtLocMaLo;
    private JScrollPane src_dsLo;
    private JLabel label_tieuDe;
    private JLabel labelLocMaLo;
    private JLabel labelHienCo;
    private JLabel labelLocMaSanPham;
    private JLabel labelLocNhaCungCap;
    private JLabel labelLocKVLT;
    private JLabel lableTinhTrangSP;
    private JComboBox cmbLocTrangThai;
    private JLabel labelLocTrangThai;
    private JLabel labelMaLo;
    private JLabel labelNhaCungCap;
    private JLabel label;
    private JTextField txtLocMaPNK;
    private JLabel labelLocMaPNK;
    private JLabel labelPNK;
    private JLabel labelSanPham;
    private JComboBox cmbTrangThai;
    private JTextField txtHSD;
    private JButton btnHuy;
    private JButton btnLuu;
    private JPanel labelKVLT;
    private JLabel labeltrangThai;
    private JLabel labelMaSP_SL;
    private JLabel labelGiaNhap;
    private JLabel labelThanhTien;
    private JPanel panelSL_HSD;
    private JLabel labelHSD;
    private JLabel labelTTTK;
    private JComboBox cmbTrangThaiTon;

    private boolean isAddingLo = false;
    private boolean isUpdatingLo = false;

    private DefaultTableModel model;
    private LoHang_BUS bus = LoHang_BUS.getInstance();

    public LoHang_GUI(){
        this.setLayout(new BorderLayout());
        this.add(panel_loHang, BorderLayout.CENTER);

        formEdit();
        initTable();
        loadTableFromList(bus.getAll());

        xuLySuKien();
    }



    private void formEdit() {
        thietKeCmbTrangThai();
        thietKeCmbTrangThaiTon();
        thietKeCmbLocNCC();
        thietKeCmbLocKVLT();
        thietKeCmbLocTrangThai();
        thietKeCmbLocTrangThaiTon();

        setViewMode();
    }
    private void thietKeCmbTrangThai() {
        cmbTrangThai.removeAllItems();
        cmbTrangThai.addItem("-- Chọn trạng thái --");

        cmbTrangThai.addItem(LoHang_DTO.CHO);
        cmbTrangThai.addItem(LoHang_DTO.HOAN_THANH);
        cmbTrangThai.addItem(LoHang_DTO.HUY);

        cmbTrangThai.setSelectedIndex(0);
    }
    private void thietKeCmbLocTrangThai() {
        cmbLocTrangThai.removeAllItems();
        cmbLocTrangThai.addItem("-- Chọn trạng thái --");

        cmbLocTrangThai.addItem(LoHang_DTO.CHO);
        cmbLocTrangThai.addItem(LoHang_DTO.HOAN_THANH);
        cmbLocTrangThai.addItem(LoHang_DTO.HUY);

        cmbLocTrangThai.setSelectedIndex(0);
    }
    private void thietKeCmbTrangThaiTon() {
        cmbTrangThaiTon.removeAllItems();
        cmbTrangThaiTon.addItem("-- Chọn trạng thái tồn --");

        cmbTrangThaiTon.addItem(LoHang_DTO.BINH_THUONG);
        cmbTrangThaiTon.addItem(LoHang_DTO.SAP_HET_HAN);
        cmbTrangThaiTon.addItem(LoHang_DTO.HET_HAN);
        cmbTrangThaiTon.addItem(LoHang_DTO.HET_HANG);

        cmbTrangThaiTon.setSelectedIndex(0);
    }
    private void thietKeCmbLocTrangThaiTon() {
        cmbTinhTrangSP.removeAllItems();
        cmbTinhTrangSP.addItem("-- Chọn trạng thái tồn --");

        cmbTinhTrangSP.addItem(LoHang_DTO.SAP_HET_HAN);
        cmbTinhTrangSP.addItem(LoHang_DTO.HET_HAN);
        cmbTinhTrangSP.addItem(LoHang_DTO.HET_HANG);

        cmbTinhTrangSP.setSelectedIndex(0);
    }
    private void thietKeCmbLocNCC() {
        cmbLocNCC.removeAllItems();
        cmbLocNCC.addItem("-- Chọn NCC --");

        ArrayList<String> dsTenNCC = bus.getDSTenNCC();
        for (String ten : dsTenNCC) {
            cmbLocNCC.addItem(ten);
        }
        cmbLocNCC.setSelectedIndex(0);
    }
    private void thietKeCmbLocKVLT() {
        cmbLocKVLT.removeAllItems();
        cmbLocKVLT.addItem("-- Chọn KVLT --");

        ArrayList<String> dsTenKVLT = bus.getDSTenKVLT();
        for (String ten : dsTenKVLT) {
            cmbLocKVLT.addItem(ten);
        }
        cmbLocKVLT.setSelectedIndex(0);
    }
    private void initTable() {

        String[] columns = {
                "STT",
                "Mã lô",
                "Mã PNK",
                "Tên sản phẩm",
                "Giá nhập",
                "Số lượng",
                "Còn lại",
                "Hạn sử dụng",
                "Thành tiền",
                "Tên NCC",
                "Tên KVLT",
                "Trạng thái"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table_dsLo.setModel(model);

        table_dsLo.getTableHeader().setResizingAllowed(false);
        table_dsLo.getTableHeader().setReorderingAllowed(false);

        table_dsLo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table_dsLo.getColumnModel().getColumn(0).setPreferredWidth(50);
        table_dsLo.getColumnModel().getColumn(1).setPreferredWidth(75);
        table_dsLo.getColumnModel().getColumn(2).setPreferredWidth(75);
        table_dsLo.getColumnModel().getColumn(3).setPreferredWidth(150);
        table_dsLo.getColumnModel().getColumn(4).setPreferredWidth(100);
        table_dsLo.getColumnModel().getColumn(5).setPreferredWidth(70);
        table_dsLo.getColumnModel().getColumn(6).setPreferredWidth(70);
        table_dsLo.getColumnModel().getColumn(7).setPreferredWidth(90);
        table_dsLo.getColumnModel().getColumn(8).setPreferredWidth(100);
        table_dsLo.getColumnModel().getColumn(9).setPreferredWidth(150);
        table_dsLo.getColumnModel().getColumn(10).setPreferredWidth(150);
        table_dsLo.getColumnModel().getColumn(11).setPreferredWidth(80);

        src_dsLo.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        table_dsLo.setRowHeight(25);
        table_dsLo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        src_dsLo.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
    }

    private void loadTableFromList(ArrayList<LoHang_DTO> list) {
        model.setRowCount(0);
        int stt = 1;
        for (LoHang_DTO lo : list) {
            model.addRow(new Object[]{
                    stt++,
                    lo.getMaLo(),
                    lo.getMaPnk(),
                    bus.getNameSP(lo.getMaSp()),
                    lo.getGiaNhap(),
                    lo.getSoLuongNhap(),
                    lo.getSoLuongConLai(),
                    lo.getHsd(),
                    lo.getThanhTien(),
                    bus.getNameNCC(lo.getMaNcc()),
                    bus.getNameKVLT(lo.getMaKvlt()),
                    lo.getTrangThaiText()
            });
        }
    }

    private void xuLySuKien() {
        table_dsLo.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienChiTiet();
            }
        });
        btnTimKiem.addActionListener(e -> xuLyTimKiem());
        btnThoat.addActionListener(e -> {
            clearFormTimKiem();
        });
        cmbPNK.addActionListener(e -> xuLyChonPNK());
        cmbSanPham.addActionListener(e -> xuLyChonSanPham());

        btnThem.addActionListener(e -> {
            xuLyThem();
        });
        btnCapNhat.addActionListener(e -> {
            xuLyCapNhat();
        });
        btnLuu.addActionListener(e -> {
            if(isAddingLo)
                xuLyLuu();
            else if(isUpdatingLo)
                xuLyLuuCapNhat();
        });
        btnHuy.addActionListener(e -> {
            xuLyHuy();
        });
    }

    private void xuLyThem() {
        lockTable();
        isAddingLo = true;
        isUpdatingLo = false;

        txtMaLo.setText(bus.getNextID());

        txtGiaNhap.setText("");
        txtThanhTien.setText("");
        txtHSD.setText("");
        txtMaSP_SL.setText("");
        txtNhaCungCap.setText("");
        txtKVLT.setText("");

        cmbPNK.removeAllItems();
        ArrayList<String> dsPNK = bus.getPNKTrangThaiChuanBi();
        cmbPNK.addItem("-- Chọn PNK --");
        for(String ma : dsPNK){
            cmbPNK.addItem(ma);
        }

        cmbPNK.setEnabled(true);
        cmbSanPham.removeAllItems();
        cmbSanPham.setEnabled(false);

        cmbTrangThai.setSelectedItem(LoHang_DTO.CHO);
        cmbTrangThaiTon.setSelectedItem(LoHang_DTO.BINH_THUONG);

        btnLuu.setVisible(true);
        btnHuy.setVisible(true);

        btnThem.setEnabled(false);
        btnCapNhat.setEnabled(false);
    }
    private void xuLyChonPNK(){
        if(cmbPNK.getSelectedIndex() < 0) return;

        String maPNK = cmbPNK.getSelectedItem().toString();

        txtNhaCungCap.setText(bus.getTenNCCByPNK(maPNK));
        txtKVLT.setText(bus.getTenKVLTByPNK(maPNK));

        cmbSanPham.removeAllItems();
        cmbSanPham.addItem("-- Chọn sản phẩm --");
        ArrayList<String> dsSP = bus.getTenSPChuaCoLoByPNK(maPNK);
        for(String sp : dsSP){
            cmbSanPham.addItem(sp);
        }

        cmbSanPham.setEnabled(true);
    }
    private void xuLyChonSanPham(){

        if(cmbSanPham.getSelectedIndex() < 0) return;

        String tenSP = cmbSanPham.getSelectedItem().toString();
        String maSP = bus.getMaSPByName(tenSP);
        String maPNK = cmbPNK.getSelectedItem().toString();
        String maNCC = bus.getMaNCCByPNK(maPNK);
        int soLuong = bus.getSoLuongChiTiet(maPNK,maSP);

        double gia = bus.getGiaNhap(maNCC,maSP);
        if(cmbSanPham.getSelectedIndex() != 0){
            txtHSD.setEditable(true);
            txtMaSP_SL.setText(maSP + " - " + soLuong);

            txtGiaNhap.setText(String.valueOf(gia));

            txtThanhTien.setText(String.valueOf(gia * soLuong));
        }
    }

    private void xuLyLuu(){
        if(!kiemTraFormLoHang()) return;
        String maLo = txtMaLo.getText();
        String maPNK = cmbPNK.getSelectedItem().toString();

        String tenSP = cmbSanPham.getSelectedItem().toString();
        String maSP = bus.getMaSPByName(tenSP);
        String maNCC = bus.getMaNCCByPNK(maPNK);
        String maKVLT = bus.getMaKVLTByName(txtKVLT.getText());
        int soLuong = bus.getSoLuongChiTiet(maPNK,maSP);

        double giaNhap = bus.getGiaNhap(maNCC,maSP);

        String txthsd = txtHSD.getText();
        LocalDate hsd = LocalDate.parse(txthsd);

        LoHang_DTO lo = new LoHang_DTO();

        lo.setMaLo(maLo);
        lo.setMaPnk(maPNK);
        lo.setMaKvlt(maKVLT);
        lo.setMaNcc(maNCC);
        lo.setMaSp(maSP);
        lo.setSoLuongNhap(soLuong);
        lo.setSoLuongConLai(soLuong);
        lo.setGiaNhap(giaNhap);
        lo.setThanhTien(giaNhap*soLuong);
        lo.setHsd(hsd);
        lo.setTrangThai(LoHang_DTO.TT_CHO);
        lo.setTrangThaiTonKho(LoHang_DTO.TK_BINH_THUONG);
        boolean result = bus.them(lo);
        if(result){
            JOptionPane.showMessageDialog(this,"Thêm lô thành công");
            loadTableFromList(bus.getAll());
            clearForm();
            setViewMode();
        }else{
            JOptionPane.showMessageDialog(this,"Thêm thất bại");
        }
    }
    private void xuLyHuy(){
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có muốn hủy thao tác?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );
        if(confirm != JOptionPane.YES_OPTION) return;
        txtMaLo.setText("");
        txtGiaNhap.setText("");
        txtThanhTien.setText("");
        txtHSD.setText("");
        txtMaSP_SL.setText("");
        txtNhaCungCap.setText("");
        txtKVLT.setText("");

        cmbPNK.removeAllItems();
        cmbSanPham.removeAllItems();

        setViewMode();
    }
    private void xuLyCapNhat() {
        int row = table_dsLo.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lô cần cập nhật");
            return;
        }

        lockTable();
        isUpdatingLo = true;
        isAddingLo = false;

        btnLuu.setVisible(true);
        btnHuy.setVisible(true);

        btnThem.setEnabled(false);
        btnCapNhat.setEnabled(false);

        cmbTrangThai.setEnabled(true);

        String maLo = model.getValueAt(row, 1).toString();
        LoHang_DTO lo = bus.getById(maLo);

        if (lo == null) return;

        cmbTrangThai.removeAllItems();

        if(lo.getTrangThai() == LoHang_DTO.TT_CHO){
            cmbTrangThai.addItem(LoHang_DTO.CHO);
            txtHSD.setEditable(true);
        } else if (lo.getTrangThai() == LoHang_DTO.TT_HOAN_THANH) {
            cmbTrangThai.addItem(LoHang_DTO.HOAN_THANH);
            cmbTrangThai.addItem(LoHang_DTO.HUY);
        } else if (lo.getTrangThai() == LoHang_DTO.TT_HUY){
            cmbTrangThai.addItem(LoHang_DTO.HUY);
            cmbTrangThai.addItem(LoHang_DTO.HOAN_THANH);
        }
        String maPNK = lo.getMaPnk();
        String maSP = lo.getMaSp();

        taoCmbPNKTheoMa(maPNK);
        taoCmbSanPhamCapNhat(maPNK, maSP);

        cmbSanPham.setEnabled(true);

        cmbTrangThai.setSelectedItem(lo.getTrangThaiText());
        cmbPNK.setEnabled(false);

        txtMaSP_SL.setEditable(false);
        txtGiaNhap.setEditable(false);
        txtThanhTien.setEditable(false);
    }
    private void xuLyLuuCapNhat() {
        int row = table_dsLo.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lô cần cập nhật");
            return;
        }

        if (!kiemTraFormLoHang()) return;
        String maLo = txtMaLo.getText().trim();
        String maPNK = cmbPNK.getSelectedItem().toString();

        String tenSP = cmbSanPham.getSelectedItem().toString();
        String maSP = bus.getMaSPByName(tenSP);

        String maNCC = bus.getMaNCCByPNK(maPNK);

        int soLuong = bus.getSoLuongChiTiet(maPNK, maSP);
        double giaNhap = bus.getGiaNhap(maNCC, maSP);

        LocalDate hsd = LocalDate.parse(txtHSD.getText().trim());

        String ttText = cmbTrangThai.getSelectedItem().toString();
        int trangThaiMoi;

        if (ttText.equals(LoHang_DTO.CHO)) {
            trangThaiMoi = LoHang_DTO.TT_CHO;
        } else if (ttText.equals(LoHang_DTO.HUY)) {
            trangThaiMoi = LoHang_DTO.TT_HUY;
        } else {
            trangThaiMoi = LoHang_DTO.TT_HOAN_THANH;
        }
        LoHang_DTO loCu = bus.getById(maLo);
        int trangThaiCu = loCu.getTrangThai();

        LoHang_DTO lo = new LoHang_DTO();
        lo.setMaLo(maLo);
        lo.setMaPnk(maPNK);
        lo.setMaSp(maSP);

        lo.setSoLuongNhap(soLuong);
        lo.setSoLuongConLai(soLuong);

        lo.setGiaNhap(giaNhap);
        lo.setThanhTien(giaNhap * soLuong);

        lo.setHsd(hsd);

        lo.setTrangThai(trangThaiMoi);

        String maKVLT = loCu.getMaKvlt();

        if (trangThaiCu == LoHang_DTO.TT_HOAN_THANH
                && trangThaiMoi == LoHang_DTO.TT_HUY) {

            bus.truHienCoKho(maKVLT, soLuong);
        }

        if (trangThaiCu == LoHang_DTO.TT_HUY
                && trangThaiMoi == LoHang_DTO.TT_HOAN_THANH) {

            bus.congHienCoKho(maKVLT, soLuong);
        }

        if (trangThaiCu == LoHang_DTO.TT_CHO
                && trangThaiMoi == LoHang_DTO.TT_HOAN_THANH) {

            bus.congHienCoKho(maKVLT, soLuong);
        }

        boolean result = bus.capNhat(lo);

        if (result) {

            JOptionPane.showMessageDialog(this, "Cập nhật lô thành công");

            loadTableFromList(bus.getAll());
            clearForm();
            setViewMode();

        } else {

            JOptionPane.showMessageDialog(this, "Cập nhật lô thất bại");
        }
    }
    private void taoCmbSanPhamCapNhat(String maPNK, String maSPHienTai) {
        cmbSanPham.removeAllItems();
        ArrayList<String> dsSP = bus.getTenSPChuaCoLoByPNK(maPNK);
        cmbSanPham.addItem(bus.getNameSP(maSPHienTai));
        for (String maSP : dsSP) {
            if (!maSP.equals(maSPHienTai)) {
                cmbSanPham.addItem(bus.getNameSP(maSP));
            }
        }
        cmbSanPham.setSelectedItem(bus.getNameSP(maSPHienTai));
    }
    private void hienChiTiet() {
        int row = table_dsLo.getSelectedRow();
        if (row < 0) return;
        String maLo = model.getValueAt(row, 1).toString();
        LoHang_DTO lo = bus.getById(maLo);

        if (lo == null) return;
        txtNhaCungCap.setText(table_dsLo.getValueAt(row, 9).toString());
        txtKVLT.setText(table_dsLo.getValueAt(row,10).toString());

        txtMaLo.setText(lo.getMaLo());
        txtGiaNhap.setText(String.valueOf(lo.getGiaNhap()));
        txtThanhTien.setText(String.valueOf(lo.getThanhTien()));
        txtHSD.setText(lo.getHsd().toString());

        txtMaSP_SL.setText(lo.getMaSp() + " -  " + lo.getSoLuongNhap());

        cmbTrangThai.setSelectedItem(lo.getTrangThaiText());
        cmbTrangThaiTon.setSelectedItem(lo.getTrangThaiTonKhoText());

        taoCmbPNKTheoMa(lo.getMaPnk());
        taoCmbSPTheoMa(lo.getMaSp());
    }
    private void taoCmbPNKTheoMa(String maPNK) {
        cmbPNK.removeAllItems();
        cmbPNK.addItem(maPNK);
    }
    private void taoCmbSPTheoMa(String maSP) {
        cmbSanPham.removeAllItems();
        cmbSanPham.addItem(bus.getNameSP(maSP));
    }
    private void xuLyTimKiem() {
        String maLo = txtLocMaLo.getText().trim();
        String maPNK = txtLocMaPNK.getText().trim();
        String maSP = txtLocMaSP.getText().trim();

        String tenNCC = cmbLocNCC.getSelectedItem().toString();
        String maNCC = null;
        if (!tenNCC.equals("-- Chọn NCC --")) {
            maNCC = bus.getMaNCCByName(tenNCC);
        }

        String tenKVLT = cmbLocKVLT.getSelectedItem().toString();
        String maKVLT = null;
        if (!tenKVLT.equals("-- Chọn KVLT --")) {
            maKVLT = bus.getMaKVLTByName(tenKVLT);
        }

        Integer tinhTrangSP = null;
        String ttTon = cmbTinhTrangSP.getSelectedItem().toString();

        if (!ttTon.equals("-- Chọn trạng thái tồn --")) {
            if (ttTon.equals(LoHang_DTO.SAP_HET_HAN))
                tinhTrangSP = LoHang_DTO.TK_SAP_HET_HAN;
            else if (ttTon.equals(LoHang_DTO.HET_HAN))
                tinhTrangSP = LoHang_DTO.TK_HET_HAN;
            else if (ttTon.equals(LoHang_DTO.HET_HANG))
                tinhTrangSP = LoHang_DTO.TK_HET_HANG;
        }
        Integer trangThai = null;
        String tt = cmbLocTrangThai.getSelectedItem().toString();
        if (!tt.equals("-- Chọn trạng thái --")) {
            if (tt.equals(LoHang_DTO.CHO))
                trangThai = LoHang_DTO.TT_CHO;
            else if (tt.equals(LoHang_DTO.HOAN_THANH))
                trangThai = LoHang_DTO.TT_HOAN_THANH;
            else if (tt.equals(LoHang_DTO.HUY))
                trangThai = LoHang_DTO.TT_HUY;
        }
        ArrayList<LoHang_DTO> ketQua = bus.timKiem(
                maLo,
                maPNK,
                maSP,
                maNCC,
                maKVLT,
                tinhTrangSP,
                trangThai
        );

        loadTableFromList(ketQua);

        txtHienCo.setText(String.valueOf(ketQua.size()));
    }
    private void clearFormTimKiem() {
        cmbLocTrangThai.setSelectedIndex(0);
        cmbLocNCC.setSelectedIndex(0);
        cmbLocKVLT.setSelectedIndex(0);
        cmbTinhTrangSP.setSelectedIndex(0);
        txtLocMaLo.setText("");
        txtLocMaPNK.setText("");
        txtLocMaSP.setText("");
        txtHienCo.setText("");

        loadTableFromList(bus.getAll());
    }

    private boolean kiemTraFormLoHang(){
        if(cmbPNK.getSelectedIndex() < 0){
            JOptionPane.showMessageDialog(this,"Vui lòng chọn phiếu nhập");
            return false;
        }
        if(cmbSanPham.getSelectedIndex() < 0){
            JOptionPane.showMessageDialog(this,"Vui lòng chọn sản phẩm");
            return false;
        }
        if(txtHSD.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this,"Vui lòng nhập hạn sử dụng");
            return false;
        }
        if(!kiemTraHSD()){
            return false;
        }

        return true;
    }
    private boolean kiemTraHSD() {
        String hsdStr = txtHSD.getText().trim();

        if (hsdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập hạn sử dụng");
            txtHSD.requestFocus();
            return false;
        }
        try {
            LocalDate hsd = LocalDate.parse(hsdStr);
            LocalDate minDate = LocalDate.now().plusDays(30);
            if (!hsd.isAfter(minDate)) {
                JOptionPane.showMessageDialog(this,
                        "Hạn sử dụng phải sau ngày hiện tại ít nhất 30 ngày");
                txtHSD.requestFocus();
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Hạn sử dụng phải đúng định dạng yyyy-MM-dd");
            txtHSD.requestFocus();
            return false;
        }

        return true;
    }
    private void clearForm(){
        txtMaLo.setText("");
        txtNhaCungCap.setText("");
        txtKVLT.setText("");
        txtHSD.setText("");
        txtGiaNhap.setText("");
        txtThanhTien.setText("");
        txtMaSP_SL.setText("");

        cmbTrangThai.setSelectedIndex(0);
        cmbTrangThaiTon.setSelectedIndex(0);
    }
    private void setViewMode() {
        unlockTable();
        isAddingLo = false;
        isUpdatingLo = false;

        txtMaLo.setEditable(false);
        txtGiaNhap.setEditable(false);
        txtThanhTien.setEditable(false);
        txtHSD.setEditable(false);
        txtMaSP_SL.setEditable(false);
        txtNhaCungCap.setEditable(false);
        txtKVLT.setEditable(false);

        cmbTrangThai.setEnabled(false);
        cmbTrangThaiTon.setEnabled(false);
        cmbPNK.removeAllItems();
        cmbPNK.setEnabled(false);
        cmbSanPham.removeAllItems();
        cmbSanPham.setEnabled(false);

        btnLuu.setVisible(false);
        btnHuy.setVisible(false);

        btnCapNhat.setEnabled(true);
        btnThem.setEnabled(true);
    }

    private void lockTable() {
        table_dsLo.setEnabled(false);
    }
    private void unlockTable() {
        table_dsLo.setEnabled(true);
    }

}
