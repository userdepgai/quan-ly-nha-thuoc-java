package gui.NCCKVLT;

import bus.NhaCungCap_BUS;
import dao.DiaChi_DAO;
import dto.DIACHI_DTO;
import dto.NhaCungCap_DTO;
import bus.SanPhamNCC_BUS;
import bus.SanPham_BUS;
import dto.SanPhamNCC_DTO;
import dto.SanPham_DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class NCC extends JPanel {
    private JButton btnXuat;
    private JButton btnNhap;
    private JTextField textLoc;
    private JComboBox comboBoxTrangThai;
    private JButton btnTimKiem;
    private JButton btnThoat;
    private JTable tableDanhSach;
    private JTextField textMa;
    private JTextField textTen;
    private JTextField textMST;
    private JTextField textNLH;
    private JTextField textSDT;
    private JTextField textDCHI;
    private JComboBox comboBoxTthai;
    private JTable tableChiTiet;
    private JTextField textTenSP;
    private JTextField textGiaBan;
    private JComboBox comboBoxTThai;
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnHuy;
    private JButton btnCapNhat;
    private JButton btnThemNCC;
    private JTextField textMaSP;
    private JPanel panelMain;
    private JButton btn_Huy;
    private JButton btn_Luu;

    private DefaultTableModel modelNCC;
    private DefaultTableModel modelSP;

    private NhaCungCap_BUS bus = NhaCungCap_BUS.getInstance();
    private boolean isAdding = false;
    private boolean isUpdating = false;
    private boolean isAddingSP = false;
    private boolean isUpdatingSP = false;
    private JPopupMenu popupGoiY = new JPopupMenu();

    public NCC() {
        this.setLayout(new BorderLayout());
        if (panelMain != null) {
            this.add(panelMain, BorderLayout.CENTER);
        } else {
            System.out.println("Lỗi: panelMain chưa được khởi tạo!");
        }

        setupTableData();
        loadDataToTable();
        setKhoaForm(true);
        addEvents();
        this.revalidate();
        this.repaint();
        setViewMode();
    }

    private void setupTableData() {
        String[] columns = {"STT", "Mã NCC", "Tên NCC", "Mã số thuế", "SĐT", "Người liên hệ", "Địa chỉ", "Trạng thái"};
        modelNCC = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableDanhSach.setModel(modelNCC);
        setupTableProperties(tableDanhSach);

        String[] colChiTiet = {"STT", "Mã Sản Phẩm", "Tên Sản Phẩm", "Giá Bán", "Trạng Thái"};
        modelSP = new DefaultTableModel(colChiTiet, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableChiTiet.setModel(modelSP);
        setupTableChiTietProperties(tableChiTiet);
    }

    private void setupTableChiTietProperties(JTable table) {
        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true);

        if (table.getColumnCount() > 0) {
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

            fixColumnWidth(table, 0, 40);
            fixColumnWidth(table, 1, 100);
            fixColumnWidth(table, 3, 100);
            fixColumnWidth(table, 4, 120);
        }
    }

    public void loadDataToTable() {
        if (modelNCC == null) return;

        modelNCC.setRowCount(0);
        bus.refreshData();

        ArrayList<NhaCungCap_DTO> listNCC = bus.getAll();

        if (listNCC == null || listNCC.isEmpty()) {
            System.out.println("Dữ liệu list trả về bị rỗng!");
            return;
        }
        DiaChi_DAO diaChiDAO = new DiaChi_DAO();
        ArrayList<DIACHI_DTO> listTatCaDiaChi = diaChiDAO.getAll();

        int stt = 1;

        for (NhaCungCap_DTO ncc : listNCC) {
            String trangThaiText = (ncc.getTrangThai() == 1) ? "ĐANG_GIAO_DỊCH" : "NGỪNG_HỢP_TÁC";
            String diaChiHienThi = "";

            if (ncc.getDiaChi() != null && ncc.getDiaChi().getMaDiaChi() != null) {
                String maDCCanTim = ncc.getDiaChi().getMaDiaChi();

                for (DIACHI_DTO dcFull : listTatCaDiaChi) {
                    if (dcFull.getMaDiaChi().equals(maDCCanTim)) {
                        ncc.setDiaChi(dcFull);

                        StringBuilder sb = new StringBuilder();
                        if (dcFull.getSoNha() != null && !dcFull.getSoNha().isEmpty())
                            sb.append(dcFull.getSoNha()).append(", ");
                        if (dcFull.getDuong() != null && !dcFull.getDuong().isEmpty())
                            sb.append(dcFull.getDuong()).append(", ");
                        if (dcFull.getPhuong() != null && !dcFull.getPhuong().isEmpty())
                            sb.append(dcFull.getPhuong()).append(", ");
                        if (dcFull.getTinh() != null && !dcFull.getTinh().isEmpty()) sb.append(dcFull.getTinh());

                        diaChiHienThi = sb.toString();

                        if (diaChiHienThi.endsWith(", ")) {
                            diaChiHienThi = diaChiHienThi.substring(0, diaChiHienThi.length() - 2);
                        }
                        break;
                    }
                }
            }

            modelNCC.addRow(new Object[]{
                    stt++,
                    ncc.getMaNCC(),
                    ncc.getTenNCC(),
                    ncc.getMaSoThue(),
                    ncc.getSdt(),
                    ncc.getNguoiLienHe(),
                    diaChiHienThi,
                    trangThaiText
            });
        }
    }

    private void loadSanPhamTheoNCC(String maNCC) {
        modelSP.setRowCount(0);

        SanPhamNCC_BUS spNccBus = SanPhamNCC_BUS.getInstance();
        spNccBus.refreshData();
        ArrayList<SanPhamNCC_DTO> list = spNccBus.getByMaNCC(maNCC);

        SanPham_BUS spBus = SanPham_BUS.getInstance();
        spBus.refreshData();

        int stt = 1;

        for (SanPhamNCC_DTO spNcc : list) {
            SanPham_DTO sp = spBus.getById(spNcc.getMaSanPham());
            String tenSP = (sp != null) ? sp.getTenSP() : "KHÔNG_TÌM_THẤY";

            modelSP.addRow(new Object[]{
                    stt++,
                    spNcc.getMaSanPham(),
                    tenSP,
                    spNcc.getGiaNhap(),
                    spNcc.getTrangThai() == 1 ? "CÒN_CUNG_CẤP" : "NGỪNG_CUNG_CẤP"
            });
        }
    }

    private void fixColumnWidth(JTable table, int columnIndex, int width) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        column.setPreferredWidth(width);
        column.setMinWidth(width);
        column.setMaxWidth(width);
    }

    private void setupTableProperties(JTable table) {
        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true);

        if (table.getColumnCount() > 0) {
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

            fixColumnWidth(table, 0, 40);
            fixColumnWidth(table, 1, 80);
            fixColumnWidth(table, 2, 150);
            fixColumnWidth(table, 3, 100);
            fixColumnWidth(table, 4, 100);
            fixColumnWidth(table, 5, 120);
            fixColumnWidth(table, 7, 130);
        }
    }
    private void setViewMode() {

        setKhoaForm(true);

        btn_Luu.setVisible(false);
        btn_Huy.setVisible(false);

        btnThemNCC.setEnabled(true);
        btnCapNhat.setEnabled(true);

        isAdding = false;
        isUpdating = false;
    }
    private void setAddMode() {

        isAdding = true;
        isUpdating = false;

        lamMoiForm();
        setKhoaForm(false);

        textMa.setText(bus.getNextId());
        textMa.setEditable(false);

        comboBoxTthai.setSelectedIndex(0);
        comboBoxTthai.setEnabled(false);

        btnThemNCC.setEnabled(false);
        btnCapNhat.setEnabled(false);

        btn_Luu.setVisible(true);
        btn_Huy.setVisible(true);
    }
    private void setUpdateMode() {

        isAdding = false;
        isUpdating = true;

        setKhoaForm(false);
        textMa.setEditable(false);
        comboBoxTthai.setEnabled(true);

        btnThemNCC.setEnabled(false);
        btnCapNhat.setEnabled(false);

        btn_Luu.setVisible(true);
        btn_Huy.setVisible(true);
    }
    // nút thêm
    private void xuLyThem() {

        try {
            NhaCungCap_DTO ncc = new NhaCungCap_DTO();
            ncc.setMaNCC(textMa.getText().trim());
            ncc.setTenNCC(textTen.getText().trim());
            ncc.setMaSoThue(textMST.getText().trim());
            ncc.setSdt(textSDT.getText().trim());
            ncc.setNguoiLienHe(textNLH.getText().trim());

            String diaChiNhap = textDCHI.getText().trim();

            if (!diaChiNhap.isEmpty()) {
                DiaChi_DAO dcDao = new DiaChi_DAO();
                DIACHI_DTO dc = new DIACHI_DTO();
                dc.setMaDiaChi(dcDao.getNextId());
                dc.setSoNha(diaChiNhap);
                dc.setDuong("");
                dc.setPhuong("");
                dc.setTinh("");

                if (!dcDao.them(dc)) {
                    JOptionPane.showMessageDialog(this, "Lỗi lưu địa chỉ!");
                    return;
                }

                ncc.setDiaChi(dc);
            }

            ncc.setTrangThai(comboBoxTthai.getSelectedIndex() == 0 ? 1 : 0);

            if (bus.insert(ncc)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadDataToTable();
                setViewMode();
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    private void xuLyCapNhat() {

        try {
            String ma = textMa.getText().trim();
            NhaCungCap_DTO ncc = bus.getById(ma);

            if (ncc == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy NCC!");
                return;
            }

            ncc.setTenNCC(textTen.getText().trim());
            ncc.setMaSoThue(textMST.getText().trim());
            ncc.setSdt(textSDT.getText().trim());
            ncc.setNguoiLienHe(textNLH.getText().trim());

            String diaChiNhap = textDCHI.getText().trim();
            DiaChi_DAO dcDao = new DiaChi_DAO();

            if (ncc.getDiaChi() != null) {

                ncc.getDiaChi().setSoNha(diaChiNhap);
                ncc.getDiaChi().setDuong("");
                ncc.getDiaChi().setPhuong("");
                ncc.getDiaChi().setTinh("");

                dcDao.capNhat(ncc.getDiaChi());

            } else if (!diaChiNhap.isEmpty()) {

                DIACHI_DTO dc = new DIACHI_DTO();
                dc.setMaDiaChi(dcDao.getNextId());
                dc.setSoNha(diaChiNhap);
                dc.setDuong("");
                dc.setPhuong("");
                dc.setTinh("");

                if (dcDao.them(dc)) {
                    ncc.setDiaChi(dc);
                }
            }

            ncc.setTrangThai(comboBoxTthai.getSelectedIndex() == 0 ? 1 : 0);

            if (bus.update(ncc)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadDataToTable();
                setViewMode();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    // hàm gọi sự kiện
    private void addEvents() {
        tableDanhSach.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                hienThiChiTiet();
            }
        });

        btnThemNCC.addActionListener(e -> setAddMode());

        btnCapNhat.addActionListener(e -> {
            if (tableDanhSach.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp!");
                return;
            }
            setUpdateMode();
        });

        btn_Luu.addActionListener(e -> {
            if (isAdding) {
                xuLyThem();
            } else if (isUpdating) {
                xuLyCapNhat();
            }
        });

        btn_Huy.addActionListener(e -> {
            setViewMode();
            lamMoiForm();
        });
        btnTimKiem.addActionListener(e -> timKiemNCC());
        btnThoat.addActionListener(e -> thoatForm());

        btnThem.addActionListener(e -> themSanPhamChoNCC());
        btnSua.addActionListener(e -> suaSanPhamChoNCC());
        btnHuy.addActionListener(e -> xoaSanPhamChoNCC());

        tableChiTiet.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tableChiTiet.getSelectedRow();
                if (row >= 0) {
                    textMaSP.setText(tableChiTiet.getValueAt(row, 1).toString());
                    textTenSP.setText(tableChiTiet.getValueAt(row, 2).toString());
                    textGiaBan.setText(tableChiTiet.getValueAt(row, 3).toString());
                    String tt = tableChiTiet.getValueAt(row, 4).toString();
                    comboBoxTThai.setSelectedIndex(tt.equals("CÒN_CUNG_CẤP") ? 0 : 1);

                    setKhoaFormSP(true);
                    isAddingSP = false;
                    isUpdatingSP = false;
                    btnThem.setText("THÊM");
                    btnSua.setText("SỬA");
                    btnThem.setEnabled(true);
                    btnSua.setEnabled(true);
                    btnHuy.setEnabled(true);
                }
            }
        });

        textLoc.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String keyword = textLoc.getText().trim().toLowerCase();
                if (!keyword.isEmpty()) {
                    ArrayList<NhaCungCap_DTO> listGoiY = new ArrayList<>();
                    for (NhaCungCap_DTO ncc : bus.getAll()) {
                        if (ncc.getTenNCC().toLowerCase().contains(keyword) ||
                                ncc.getMaNCC().toLowerCase().contains(keyword)) {
                            listGoiY.add(ncc);
                        }
                    }
                    hienThiGoiYNCC(listGoiY);
                } else {
                    popupGoiY.setVisible(false);
                }
            }
        });
    }

    private void setKhoaForm(boolean isLocked) {

        textMa.setEditable(!isLocked);
        textTen.setEditable(!isLocked);
        textMST.setEditable(!isLocked);
        textNLH.setEditable(!isLocked);
        textSDT.setEditable(!isLocked);
        textDCHI.setEditable(!isLocked);

        comboBoxTthai.setEnabled(!isLocked);

    }
    private void lamMoiForm() {
        textMa.setText("");
        textMa.setEditable(true);
        textTen.setText("");
        textMST.setText("");
        textNLH.setText("");
        textSDT.setText("");
        textDCHI.setText("");
        comboBoxTthai.setSelectedIndex(0);
        tableDanhSach.clearSelection();
    }

    private void setKhoaFormSP(boolean isLocked) {

        textMaSP.setEditable(!isLocked);
        textTenSP.setEditable(!isLocked);
        textGiaBan.setEditable(!isLocked);

        comboBoxTThai.setEnabled(!isLocked);

    }

    private void lamMoiFormSanPham() {
        textMaSP.setText("");
        textTenSP.setText("");
        textGiaBan.setText("");
        comboBoxTThai.setSelectedIndex(0);
        tableChiTiet.clearSelection();
    }

    private void hienThiChiTiet() {

        int selectedRow = tableDanhSach.getSelectedRow();
        if (selectedRow < 0) return;
        if (isAddingSP || isUpdatingSP) {
            isAddingSP = false;
            isUpdatingSP = false;

            btnThem.setText("THÊM");
            btnSua.setText("SỬA");
            btnThem.setEnabled(true);
            btnSua.setEnabled(true);
            btnHuy.setEnabled(true);

            lamMoiFormSanPham();
            setKhoaFormSP(true);
        }

        int modelRow = tableDanhSach.convertRowIndexToModel(selectedRow);

        textMa.setText(modelNCC.getValueAt(modelRow, 1).toString());
        textTen.setText(modelNCC.getValueAt(modelRow, 2).toString());
        textMST.setText(modelNCC.getValueAt(modelRow, 3).toString());
        textSDT.setText(modelNCC.getValueAt(modelRow, 4).toString());
        textNLH.setText(modelNCC.getValueAt(modelRow, 5).toString());

        Object objDC = modelNCC.getValueAt(modelRow, 6);
        textDCHI.setText(objDC != null ? objDC.toString() : "");

        String trangThai = modelNCC.getValueAt(modelRow, 7).toString();
        comboBoxTthai.setSelectedIndex(trangThai.equals("ĐANG_GIAO_DỊCH") ? 0 : 1);

        modelSP.setRowCount(0);
        lamMoiFormSanPham();

        loadSanPhamTheoNCC(textMa.getText().trim());

        setKhoaForm(true);

        btnThemNCC.setEnabled(true);
        btnCapNhat.setEnabled(true);

        isAdding = false;
        isUpdating = false;
    }



    private void timKiemNCC() {
        String tuKhoa = textLoc.getText().trim().toLowerCase();
        String trangThaiLoc = "";

        if (comboBoxTrangThai.getSelectedItem() != null) {
            trangThaiLoc = comboBoxTrangThai.getSelectedItem().toString();
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelNCC);
        tableDanhSach.setRowSorter(sorter);

        java.util.List<RowFilter<Object, Object>> filters = new ArrayList<>();

        if (!tuKhoa.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + tuKhoa));
        }
        if (!trangThaiLoc.trim().isEmpty()) {
            filters.add(RowFilter.regexFilter("^" + trangThaiLoc + "$", 7));
        }

        sorter.setRowFilter(RowFilter.andFilter(filters));
    }

    private void themSanPhamChoNCC() {
        if (!isAddingSP) {
            isAddingSP = true;
            isUpdatingSP = false;

            lamMoiFormSanPham();
            setKhoaFormSP(false);
            textMaSP.setEnabled(false);
            btnThem.setText("Lưu");
            btnSua.setEnabled(false);
            btnHuy.setEnabled(false);
            return;
        }

        String maNCC = textMa.getText().trim();
        String maSP = textMaSP.getText().trim();
        String giaNhapStr = textGiaBan.getText().trim();

        if (maNCC.isEmpty() || maSP.isEmpty() || giaNhapStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn NCC và nhập đầy đủ thông tin sản phẩm!");
            return;
        }

        try {
            double giaNhap = Double.parseDouble(giaNhapStr);
            SanPhamNCC_DTO spNcc = new SanPhamNCC_DTO();
            spNcc.setMaNCC(maNCC);
            spNcc.setMaSanPham(maSP);
            spNcc.setGiaNhap(giaNhap);
            spNcc.setTrangThai(comboBoxTThai.getSelectedIndex() == 0 ? 1 : 0);

            SanPhamNCC_BUS spNccBus = SanPhamNCC_BUS.getInstance();
            if (spNccBus.insert(spNcc)) {
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
                loadSanPhamTheoNCC(maNCC);
                lamMoiFormSanPham();

                isAddingSP = false;
                setKhoaFormSP(true);
                btnThem.setText("THÊM");
                btnSua.setEnabled(true);
                btnHuy.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Sản phẩm này đã tồn tại trong danh sách của NCC!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá nhập không hợp lệ!");
        }
    }

    private void suaSanPhamChoNCC() {
        int row = tableChiTiet.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa trong bảng!");
            return;
        }

        if (!isUpdatingSP) {
            isUpdatingSP = true;
            isAddingSP = false;

            setKhoaFormSP(false);
            textMaSP.setEnabled(false);
            textTenSP.setEnabled(false);
            btnSua.setText("Lưu");
            btnThem.setEnabled(false);
            btnHuy.setEnabled(false);
            return;
        }

        try {
            String maNCC = textMa.getText().trim();
            String maSP = textMaSP.getText().trim();
            double giaMoi = Double.parseDouble(textGiaBan.getText().trim());
            int trangThai = comboBoxTThai.getSelectedIndex() == 0 ? 1 : 0;

            SanPhamNCC_DTO spNcc = new SanPhamNCC_DTO();
            spNcc.setMaNCC(maNCC);
            spNcc.setMaSanPham(maSP);
            spNcc.setGiaNhap(giaMoi);
            spNcc.setTrangThai(trangThai);

            SanPhamNCC_BUS spNccBus = SanPhamNCC_BUS.getInstance();

            if (spNccBus.update(spNcc)) {
                JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!");
                loadSanPhamTheoNCC(maNCC);

                isUpdatingSP = false;
                setKhoaFormSP(true);
                btnSua.setText("SỬA");
                btnThem.setEnabled(true);
                btnHuy.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại! Không tìm thấy sản phẩm này của NCC trong Database.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá bán không hợp lệ! Vui lòng chỉ nhập số.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage());
        }
    }

    private void xoaSanPhamChoNCC() {
        int row = tableChiTiet.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sản phẩm này khỏi NCC?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String maNCC = textMa.getText().trim();
            String maSP = tableChiTiet.getValueAt(row, 1).toString();

            SanPhamNCC_BUS spNccBus = SanPhamNCC_BUS.getInstance();
            if (spNccBus.delete(maNCC, maSP)) {
                JOptionPane.showMessageDialog(this, "Đã xóa sản phẩm thành công!");
                loadSanPhamTheoNCC(maNCC);
                lamMoiFormSanPham();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi: Không thể xóa sản phẩm!");
            }
        }
    }

    private void thoatForm() {
        if (JOptionPane.showConfirmDialog(this, "Bạn có muốn hủy bỏ các thao tác hiện tại không?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            lamMoiForm();
            setKhoaForm(true);
            btnThemNCC.setText("Thêm");
            btnThemNCC.setEnabled(true);
            btnCapNhat.setText("Cập Nhật");
            btnCapNhat.setEnabled(true);
            isAdding = false;
            isUpdating = false;

            lamMoiFormSanPham();
            setKhoaFormSP(true);
            btnThem.setText("THÊM");
            btnThem.setEnabled(true);
            btnSua.setText("SỬA");
            btnSua.setEnabled(true);
            btnHuy.setEnabled(true);
            isAddingSP = false;
            isUpdatingSP = false;
        }
    }

    private void hienThiGoiYNCC(ArrayList<NhaCungCap_DTO> list) {
        popupGoiY.setVisible(false);
        popupGoiY.removeAll();

        if (list == null || list.isEmpty()) return;

        JPanel panelNoiDung = new JPanel();
        panelNoiDung.setLayout(new BoxLayout(panelNoiDung, BoxLayout.Y_AXIS));
        panelNoiDung.setBackground(Color.WHITE);

        for (NhaCungCap_DTO ncc : list) {
            JButton btnItem = new JButton(ncc.getMaNCC() + " - " + ncc.getTenNCC());
            btnItem.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnItem.setHorizontalAlignment(SwingConstants.LEFT);
            btnItem.setMargin(new Insets(2, 10, 2, 10));
            btnItem.setBorderPainted(false);
            btnItem.setContentAreaFilled(false);
            btnItem.setFocusPainted(false);
            btnItem.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btnItem.setPreferredSize(new Dimension(textLoc.getWidth(), 30));
            btnItem.setMaximumSize(new Dimension(textLoc.getWidth(), 30));

            btnItem.addActionListener(e -> {
                textLoc.setText(ncc.getMaNCC());
                popupGoiY.setVisible(false);
                timKiemNCC();
            });
            btnItem.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    btnItem.setContentAreaFilled(true);
                    btnItem.setBackground(new Color(240, 240, 240));
                }

                public void mouseExited(MouseEvent evt) {
                    btnItem.setContentAreaFilled(false);
                }
            });

            panelNoiDung.add(btnItem);
        }

        JScrollPane scrollPane = new JScrollPane(panelNoiDung);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

        int height = Math.min(list.size() * 30, 150);
        scrollPane.setPreferredSize(new Dimension(textLoc.getWidth(), height));
        popupGoiY.setFocusable(false);
        popupGoiY.add(scrollPane);
        popupGoiY.show(textLoc, 0, textLoc.getHeight());

        scrollPane.setFocusable(false);
    }

    public JPanel getPanelMain() {
        return panelMain;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}