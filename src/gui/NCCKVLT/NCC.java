package gui.NCCKVLT;

import bus.NhaCungCap_BUS;
import dao.DiaChi_DAO;
import dao.NhaCungCap_DAO;
import dto.DIACHI_DTO;
import dto.NhaCungCap_DTO;
import bus.SanPhamNCC_BUS;
import bus.SanPham_BUS;
import dto.SanPhamNCC_DTO;
import dto.SanPham_DTO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Locale;

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
    private JComboBox comboBox1;
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnHuy;
    private JButton btnCapNhat;
    private JButton btnThemNCC;
    private JTextField textMaSP;
    private JPanel panelMain;
    private DefaultTableModel modelNCC;
    private DefaultTableModel modelSP;
    private NhaCungCap_DAO dao = new NhaCungCap_DAO();
    private NhaCungCap_BUS bus = NhaCungCap_BUS.getInstance();
    private boolean isAdding = false;
    private boolean isUpdating = false;
    private boolean isAddingSP = false;
    private boolean isUpdatingSP = false;
    private JPopupMenu popupGoiY = new JPopupMenu();

    public NCC() {
        $$$setupUI$$$();

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
        ArrayList<NhaCungCap_DTO> listNCC = dao.getAll();

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


    private void addEvents() {
        tableDanhSach.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                hienThiChiTiet();
            }
        });

        btnThemNCC.addActionListener(e -> themNCC());
        btnCapNhat.addActionListener(e -> capNhatNCC());
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
                    comboBox1.setSelectedIndex(tt.equals("CÒN_CUNG_CẤP") ? 0 : 1);

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
        comboBox1.setEnabled(!isLocked);
    }

    private void lamMoiFormSanPham() {
        textMaSP.setText("");
        textTenSP.setText("");
        textGiaBan.setText("");
        comboBox1.setSelectedIndex(0);
        tableChiTiet.clearSelection();
    }

    private void hienThiChiTiet() {
        int selectedRow = tableDanhSach.getSelectedRow();
        if (selectedRow >= 0) {
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


            loadSanPhamTheoNCC(textMa.getText().trim());

            setKhoaForm(true);
            btnThemNCC.setText("Thêm ");
            btnCapNhat.setText("Cập Nhật ");
            btnThemNCC.setEnabled(true);
            btnCapNhat.setEnabled(true);

            isAdding = false;
            isUpdating = false;
        }
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

    private void themNCC() {

        if (!isAdding) {
            isAdding = true;
            isUpdating = false;

            lamMoiForm();
            setKhoaForm(false);


            String maMoi = dao.getNextId();
            textMa.setText(maMoi);
            textMa.setEditable(false);

            btnThemNCC.setText("Lưu");
            btnCapNhat.setEnabled(false);
            return;
        }


        try {
            NhaCungCap_DTO ncc = new NhaCungCap_DTO();
            ncc.setMaNCC(textMa.getText().trim());
            ncc.setTenNCC(textTen.getText().trim());
            ncc.setMaSoThue(textMST.getText().trim());
            ncc.setSdt(textSDT.getText().trim());
            ncc.setNguoiLienHe(textNLH.getText().trim());
            String diaChiNhapVao = textDCHI.getText().trim();
            if (!diaChiNhapVao.isEmpty()) {
                DiaChi_DAO dcDao = new DiaChi_DAO();
                DIACHI_DTO dcMoi = new DIACHI_DTO();
                dcMoi.setMaDiaChi(dcDao.getNextId());
                dcMoi.setSoNha(diaChiNhapVao);
                dcMoi.setDuong("");
                dcMoi.setPhuong("");
                dcMoi.setTinh("");
                if (dcDao.them(dcMoi)) {
                    ncc.setDiaChi(dcMoi);
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi: Không thể lưu thông tin Địa Chỉ vào CSDL!");
                    return;
                }
            }
            int trangThaiSo = (comboBoxTthai.getSelectedIndex() == 0) ? 1 : 0;
            ncc.setTrangThai(trangThaiSo);

            if (dao.insert(ncc)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadDataToTable();
                lamMoiForm();
                setKhoaForm(true);

                isAdding = false;
                btnThemNCC.setText("Thêm ");
                btnCapNhat.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void capNhatNCC() {
        int selectedRow = tableDanhSach.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp!");
            return;
        }

        if (!isUpdating) {
            isUpdating = true;
            isAdding = false;
            setKhoaForm(false);
            textMa.setEditable(false);
            btnCapNhat.setText("Lưu");
            btnThemNCC.setEnabled(false);
            return;
        }

        try {
            String maNCC = textMa.getText().trim();
            NhaCungCap_DTO ncc = bus.getById(maNCC);

            if (ncc == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy nhà cung cấp này trong CSDL!");
                return;
            }
            ncc.setTenNCC(textTen.getText().trim());
            ncc.setMaSoThue(textMST.getText().trim());
            ncc.setSdt(textSDT.getText().trim());
            ncc.setNguoiLienHe(textNLH.getText().trim());
            String diaChiNhapVao = textDCHI.getText().trim();
            DiaChi_DAO dcDao = new DiaChi_DAO();

            if (ncc.getDiaChi() != null && ncc.getDiaChi().getMaDiaChi() != null) {
                ncc.getDiaChi().setSoNha(diaChiNhapVao);
                ncc.getDiaChi().setDuong("");
                ncc.getDiaChi().setPhuong("");
                ncc.getDiaChi().setTinh("");
                dcDao.capNhat(ncc.getDiaChi());
            } else {
                if (!diaChiNhapVao.isEmpty()) {
                    DIACHI_DTO dcMoi = new DIACHI_DTO();
                    dcMoi.setMaDiaChi(dcDao.getNextId());
                    dcMoi.setSoNha(diaChiNhapVao);
                    dcMoi.setDuong("");
                    dcMoi.setPhuong("");
                    dcMoi.setTinh("");

                    if (dcDao.them(dcMoi)) {
                        ncc.setDiaChi(dcMoi);
                    }
                }
            }
            int trangThaiSo = (comboBoxTthai.getSelectedIndex() == 0) ? 1 : 0;
            ncc.setTrangThai(trangThaiSo);

            if (dao.update(ncc)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadDataToTable();
                setKhoaForm(true);

                isUpdating = false;
                btnCapNhat.setText("Cập Nhật ");
                btnThemNCC.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
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
            spNcc.setTrangThai(comboBox1.getSelectedIndex() == 0 ? 1 : 0);

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
            textMaSP.setEditable(false);
            textTenSP.setEditable(false);

            btnSua.setText("Lưu");
            btnThem.setEnabled(false);
            btnHuy.setEnabled(false);
            return;
        }


        try {
            String maNCC = textMa.getText().trim();
            String maSP = textMaSP.getText().trim();
            double giaMoi = Double.parseDouble(textGiaBan.getText().trim());
            int trangThai = comboBox1.getSelectedIndex() == 0 ? 1 : 0;

            // Đã đổi sang dùng hàm set... để không bao giờ bị ngược dữ liệu
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
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thoát?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
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

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new BorderLayout(0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 4, new Insets(5, 5, 5, 0), -1, -1));
        panelMain.add(panel1, BorderLayout.CENTER);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        btnXuat = new JButton();
        btnXuat.setText("Xuất Excel");
        panel2.add(btnXuat, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnNhap = new JButton();
        btnNhap.setText("Nhập Excel");
        panel2.add(btnNhap, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Segoe UI", Font.BOLD, 28, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("QUẢN LÝ NHÀ CUNG CẤP ");
        panel2.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 7, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(null, "Bộ Lọc", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label2 = new JLabel();
        label2.setText("Lọc Theo:");
        panel3.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(0, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        textLoc = new JTextField();
        textLoc.setText("");
        panel3.add(textLoc, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Trạng Thái:");
        panel3.add(label3, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxTrangThai = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("ĐANG_GIAO_DỊCH");
        defaultComboBoxModel1.addElement("NGỪNG_HỢP_TÁC");
        comboBoxTrangThai.setModel(defaultComboBoxModel1);
        panel3.add(comboBoxTrangThai, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        btnTimKiem = new JButton();
        btnTimKiem.setText("Tìm Kiếm");
        panel3.add(btnTimKiem, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnThoat = new JButton();
        btnThoat.setText("Thoát");
        panel3.add(btnThoat, new com.intellij.uiDesigner.core.GridConstraints(0, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 300), null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "DANH SÁCH NHÀ CUNG CẤP ", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        tableDanhSach = new JTable();
        tableDanhSach.setPreferredScrollableViewportSize(new Dimension(450, 600));
        scrollPane1.setViewportView(tableDanhSach);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder(null, "Thông Tin ", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label4 = new JLabel();
        label4.setText("Mã NCC:");
        panel5.add(label4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textMa = new JTextField();
        panel5.add(textMa, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Tên NCC:");
        panel5.add(label5, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textTen = new JTextField();
        panel5.add(textTen, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Mã Số Thuế:");
        panel5.add(label6, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textMST = new JTextField();
        panel5.add(textMST, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Người Liên Hệ:");
        panel5.add(label7, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textNLH = new JTextField();
        panel5.add(textNLH, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Số Điện Thoại:");
        panel5.add(label8, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textSDT = new JTextField();
        panel5.add(textSDT, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Địa Chỉ: ");
        panel5.add(label9, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textDCHI = new JTextField();
        panel5.add(textDCHI, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Trạng Thái");
        panel5.add(label10, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxTthai = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("ĐANG_GIAO_DỊCH");
        defaultComboBoxModel2.addElement("NGỪNG_HỢP_TÁC");
        comboBoxTthai.setModel(defaultComboBoxModel2);
        panel5.add(comboBoxTthai, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel6, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel6.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane2.setBorder(BorderFactory.createTitledBorder(null, "Danh Sách Sản Phẩm", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        tableChiTiet = new JTable();
        scrollPane2.setViewportView(tableChiTiet);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel7, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Mã Sản Phẩm:");
        panel7.add(label11, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textMaSP = new JTextField();
        textMaSP.setText("");
        panel7.add(textMaSP, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Tên Sản Phẩm ");
        panel7.add(label12, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textTenSP = new JTextField();
        textTenSP.setText("");
        panel7.add(textTenSP, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Giá Bán:");
        panel7.add(label13, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textGiaBan = new JTextField();
        panel7.add(textGiaBan, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("Trạng Thái");
        panel7.add(label14, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBox1 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        defaultComboBoxModel3.addElement("CÒN_CUNG_CẤP");
        defaultComboBoxModel3.addElement("NGỪNG_CUNG_CẤP");
        comboBox1.setModel(defaultComboBoxModel3);
        panel7.add(comboBox1, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnThem = new JButton();
        btnThem.setText("THÊM");
        panel7.add(btnThem, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnSua = new JButton();
        btnSua.setText("SỬA");
        panel7.add(btnSua, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnHuy = new JButton();
        btnHuy.setText("XÓA");
        panel7.add(btnHuy, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnCapNhat = new JButton();
        btnCapNhat.setText("Cập Nhật ");
        panel1.add(btnCapNhat, new com.intellij.uiDesigner.core.GridConstraints(4, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnThemNCC = new JButton();
        btnThemNCC.setText("Thêm ");
        panel1.add(btnThemNCC, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

    public JPanel getPanelMain() {
        return panelMain;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}