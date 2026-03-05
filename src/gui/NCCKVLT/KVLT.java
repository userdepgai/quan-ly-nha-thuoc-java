package gui.NCCKVLT;

import dao.DiaChi_DAO;
import dto.DIACHI_DTO;
import dto.KhuVucLuuTru_DTO;
import bus.KhuVucLuuTru_BUS;

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
import java.sql.Date;
import java.util.ArrayList;

public class KVLT extends JPanel {

    private JButton btnXuat;
    private JButton btnNhap;
    private JTextField textLoc;
    private JComboBox comboBoxTrangThai;
    private JButton btnTimKiem;
    private JButton btnThoat;
    private JTable tableDanhSach;
    private JTextField textMa;
    private JTextField textTen;
    private JTextField textSucChua;
    private JTextField textHienCo;
    private JTextField textDCHI;
    private JComboBox comboBoxTthai;
    private JTable tableChiTiet;
    private JButton btnCapNhat;
    private JButton btnThemKV;
    private JPanel panelMain;


    private JButton btn_Luu;
    private JButton btn_Huy;

    private DefaultTableModel modelKVLT;
    private KhuVucLuuTru_BUS bus = KhuVucLuuTru_BUS.getInstance();
    private JPopupMenu popupGoiY = new JPopupMenu();


    private boolean isAdding = false;
    private boolean isUpdating = false;

    public KVLT() {
        this.setLayout(new BorderLayout());
        if (panelMain != null) {
            this.add(panelMain, BorderLayout.CENTER);
        }

        setupTableData();
        loadDataToTableKVLT();
        bus.refreshData();

        addEvents();
        setViewMode();

        this.revalidate();
        this.repaint();
    }

    private void setupTableData() {
        String[] columns = {
                "STT", "Mã KV", "Tên Khu Vực", "Sức Chứa", "Hiện Có", "Ngày Lập", "Địa Chỉ", "Trạng Thái"
        };

        modelKVLT = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (tableDanhSach != null) {
            tableDanhSach.setModel(modelKVLT);
            setupTableProperties(tableDanhSach);
        }

        String[] colChiTiet = {"STT", "Mã Sản Phẩm", "Tên Sản Phẩm", "Số Lượng", "Lô Hàng"};
        DefaultTableModel model2 = new DefaultTableModel(colChiTiet, 0);
        if (tableChiTiet != null) {
            tableChiTiet.setModel(model2);
            setupTableChiTietProperties(tableChiTiet);
        }
    }

    public void loadDataToTableKVLT() {
        if (modelKVLT == null) return;
        modelKVLT.setRowCount(0);

        bus.refreshData();
        ArrayList<KhuVucLuuTru_DTO> list = bus.getAll();
        if (list == null || list.isEmpty()) return;

        DiaChi_DAO diaChiDAO = new DiaChi_DAO();
        ArrayList<DIACHI_DTO> listTatCaDiaChi = diaChiDAO.getAll();

        int stt = 1;
        for (KhuVucLuuTru_DTO kv : list) {
            String trangThaiText = switch (kv.getTrangThai()) {
                case 0 -> "Bảo trì";
                case 1 -> "Còn trống";
                case 2 -> "Đã đầy";
                default -> "Lỗi TT: " + kv.getTrangThai();
            };

            String diaChiHienThi = "";
            if (kv.getDiaChi() != null && kv.getDiaChi().getMaDiaChi() != null) {
                String maDCCanTim = kv.getDiaChi().getMaDiaChi();

                for (DIACHI_DTO dcFull : listTatCaDiaChi) {
                    if (dcFull.getMaDiaChi().equals(maDCCanTim)) {
                        kv.setDiaChi(dcFull);
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

            modelKVLT.addRow(new Object[]{
                    stt++,
                    kv.getMaKVLT(),
                    kv.getTenKVLT(),
                    kv.getSucChua(),
                    kv.getHienCo(),
                    kv.getNgayLapKho(),
                    diaChiHienThi,
                    trangThaiText
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
            fixColumnWidth(table, 3, 80);
            fixColumnWidth(table, 4, 80);
            fixColumnWidth(table, 5, 100);
            fixColumnWidth(table, 7, 120);
        }

        comboBoxTthai.setModel(new DefaultComboBoxModel<>(new String[]{
                "Còn trống", "Đã đầy", "Bảo trì"
        }));
    }

    private void setupTableChiTietProperties(JTable table) {
        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true);

        if (table.getColumnCount() > 0) {
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

            fixColumnWidth(table, 0, 40);
            fixColumnWidth(table, 1, 100);


            fixColumnWidth(table, 3, 100);
            fixColumnWidth(table, 4, 120);
        }
    }

    private void setViewMode() {
        isAdding = false;
        isUpdating = false;

        setKhoaForm(true);

        if (btn_Luu != null) btn_Luu.setVisible(false);
        if (btn_Huy != null) btn_Huy.setVisible(false);

        btnThemKV.setEnabled(true);
        btnCapNhat.setEnabled(true);
    }

    private void setAddMode() {
        isAdding = true;
        isUpdating = false;

        lamMoiForm();
        setKhoaForm(false);

        textMa.setText(bus.getNextId());
        textMa.setEditable(false);
        textHienCo.setText("0");
        textHienCo.setEditable(false);

        comboBoxTthai.setSelectedIndex(0);

        btnThemKV.setEnabled(false);
        btnCapNhat.setEnabled(false);

        if (btn_Luu != null) btn_Luu.setVisible(true);
        if (btn_Huy != null) btn_Huy.setVisible(true);
    }

    private void setUpdateMode() {
        isAdding = false;
        isUpdating = true;

        setKhoaForm(false);
        textMa.setEditable(false);
        textHienCo.setEditable(false);

        btnThemKV.setEnabled(false);
        btnCapNhat.setEnabled(false);

        if (btn_Luu != null) btn_Luu.setVisible(true);
        if (btn_Huy != null) btn_Huy.setVisible(true);
    }

    private void setKhoaForm(boolean isLocked) {
        textMa.setEditable(!isLocked);
        textTen.setEditable(!isLocked);
        textSucChua.setEditable(!isLocked);
        textHienCo.setEditable(!isLocked);
        textDCHI.setEditable(!isLocked);
        comboBoxTthai.setEnabled(!isLocked);
    }

    private void lamMoiForm() {
        textMa.setText("");
        textTen.setText("");
        textSucChua.setText("");
        textHienCo.setText("0");
        textDCHI.setText("");
        comboBoxTthai.setSelectedIndex(0);
        tableDanhSach.clearSelection();
    }

    private void hienThiChiTiet() {
        int selectedRow = tableDanhSach.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = tableDanhSach.convertRowIndexToModel(selectedRow);
            String maKV = modelKVLT.getValueAt(modelRow, 1).toString();
            textMa.setText(modelKVLT.getValueAt(modelRow, 1).toString());
            textTen.setText(modelKVLT.getValueAt(modelRow, 2).toString());
            textSucChua.setText(modelKVLT.getValueAt(modelRow, 3).toString());
            textHienCo.setText(modelKVLT.getValueAt(modelRow, 4).toString());

            Object obj = modelKVLT.getValueAt(modelRow, 6);
            if (obj != null) {
                textDCHI.setText(obj.toString());
            } else {
                textDCHI.setText("");
            }
            String trangThai = modelKVLT.getValueAt(modelRow, 7).toString();
            if (trangThai.equals("Còn trống")) comboBoxTthai.setSelectedIndex(0);
            else if (trangThai.equals("Đã đầy")) comboBoxTthai.setSelectedIndex(1);
            else comboBoxTthai.setSelectedIndex(2);
            loadChiTietSanPham(maKV);
            setViewMode();

        }
    }


    private void xuLyThem() {
        try {
            KhuVucLuuTru_DTO kv = new KhuVucLuuTru_DTO();
            kv.setMaKVLT(textMa.getText().trim());
            kv.setTenKVLT(textTen.getText().trim());
            kv.setSucChua(Integer.parseInt(textSucChua.getText().trim()));
            kv.setHienCo(0);
            kv.setNgayLapKho(new Date(System.currentTimeMillis()));
            int trangThaiValue = 0;
            if (comboBoxTthai.getSelectedIndex() == 0) {
                trangThaiValue = 1;
            } else if (comboBoxTthai.getSelectedIndex() == 1) {
                trangThaiValue = 2;
            } else if (comboBoxTthai.getSelectedIndex() == 2) {
                trangThaiValue = 0;
            }
            kv.setTrangThai(trangThaiValue);

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
                    kv.setDiaChi(dcMoi);
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi: Không thể tạo mới Địa Chỉ trong CSDL!");
                    return;
                }
            }

            if (bus.insert(kv)) {
                JOptionPane.showMessageDialog(this, "Thêm khu vực thành công!");
                loadDataToTableKVLT();
                setViewMode();
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm khu vực thất bại!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ vào ô Sức Chứa!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + e.getMessage());
        }
    }

    private void xuLyCapNhat() {
        try {
            KhuVucLuuTru_DTO kv = bus.getById(textMa.getText().trim());

            if (kv == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khu vực lưu trữ này trong CSDL!");
                return;
            }
            kv.setTenKVLT(textTen.getText().trim());
            kv.setSucChua(Integer.parseInt(textSucChua.getText().trim()));
            int trangThaiValue = 0;
            if (comboBoxTthai.getSelectedIndex() == 0) {
                trangThaiValue = 1;
            } else if (comboBoxTthai.getSelectedIndex() == 1) {
                trangThaiValue = 2;
            } else if (comboBoxTthai.getSelectedIndex() == 2) {
                trangThaiValue = 0;
            }
            kv.setTrangThai(trangThaiValue);

            String diaChiNhapVao = textDCHI.getText().trim();
            DiaChi_DAO dcDao = new DiaChi_DAO();

            if (kv.getDiaChi() != null && kv.getDiaChi().getMaDiaChi() != null) {
                kv.getDiaChi().setSoNha(diaChiNhapVao);
                kv.getDiaChi().setDuong("");
                kv.getDiaChi().setPhuong("");
                kv.getDiaChi().setTinh("");

                dcDao.capNhat(kv.getDiaChi());
            } else {
                if (!diaChiNhapVao.isEmpty()) {
                    DIACHI_DTO dcMoi = new DIACHI_DTO();
                    dcMoi.setMaDiaChi(dcDao.getNextId());
                    dcMoi.setSoNha(diaChiNhapVao);
                    dcMoi.setDuong("");
                    dcMoi.setPhuong("");
                    dcMoi.setTinh("");

                    if (dcDao.them(dcMoi)) {
                        kv.setDiaChi(dcMoi);
                    }
                }
            }

            if (bus.update(kv)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadDataToTableKVLT();
                setViewMode();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Sức chứa phải là một số nguyên hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + e.getMessage());
        }
    }


    private void addEvents() {
        tableDanhSach.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                hienThiChiTiet();
            }
        });

        btnThemKV.addActionListener(e -> setAddMode());

        btnCapNhat.addActionListener(e -> {
            if (tableDanhSach.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khu vực cần cập nhật!");
                return;
            }
            setUpdateMode();
        });

        if (btn_Luu != null) {
            btn_Luu.addActionListener(e -> {
                if (isAdding) {
                    xuLyThem();
                } else if (isUpdating) {
                    xuLyCapNhat();
                }
            });
        }

        if (btn_Huy != null) {
            btn_Huy.addActionListener(e -> {
                setViewMode();
                lamMoiForm();
            });
        }

        btnTimKiem.addActionListener(e -> timKiemKhuVuc());
        btnThoat.addActionListener(e -> thoatForm());

        textLoc.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String keyword = textLoc.getText().trim().toLowerCase();

                if (!keyword.isEmpty()) {
                    ArrayList<KhuVucLuuTru_DTO> listGoiY = new ArrayList<>();

                    for (KhuVucLuuTru_DTO kv : bus.getAll()) {
                        String trangThaiStr = "";
                        if (kv.getTrangThai() == 1) trangThaiStr = "còn trống";
                        else if (kv.getTrangThai() == 2) trangThaiStr = "đã đầy";
                        else trangThaiStr = "Bảo trì";

                        String thongTinTongHop = String.format("%s %s %d %d %s %s",
                                kv.getMaKVLT() != null ? kv.getMaKVLT() : "",
                                kv.getTenKVLT() != null ? kv.getTenKVLT() : "",
                                kv.getSucChua(),
                                kv.getHienCo(),
                                kv.getNgayLapKho() != null ? kv.getNgayLapKho().toString() : "",
                                trangThaiStr
                        ).toLowerCase();
                        if (thongTinTongHop.contains(keyword)) {
                            listGoiY.add(kv);
                        }
                    }

                    hienThiGoiYKVLT(listGoiY);
                } else {
                    popupGoiY.setVisible(false);
                }
            }
        });
    }

    private void timKiemKhuVuc() {
        String tuKhoa = textLoc.getText().trim().toLowerCase();

        String trangThaiLoc = "";
        if (comboBoxTrangThai.getSelectedItem() != null) {
            trangThaiLoc = comboBoxTrangThai.getSelectedItem().toString();
        }

        if (trangThaiLoc.equals("Còn trống")) trangThaiLoc = "Còn trống";
        else if (trangThaiLoc.equals("Đã đầy")) trangThaiLoc = "Đã đầy";
        else if (trangThaiLoc.equals("Bảo trì")) trangThaiLoc = "Bảo trì";

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelKVLT);
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
    private void loadChiTietSanPham(String maKV) {
        DefaultTableModel modelChiTiet = (DefaultTableModel) tableChiTiet.getModel();
        modelChiTiet.setRowCount(0);

        dao.LoHang_DAO loHangDAO = new dao.LoHang_DAO();
        dao.SanPham_DAO spDAO = new dao.SanPham_DAO();

        ArrayList<dto.LoHang_DTO> tatCaLoHang = loHangDAO.getAll();
        ArrayList<dto.SanPham_DTO> tatCaSanPham = spDAO.getAll();

        if (tatCaLoHang != null) {
            int stt = 1;
            for (dto.LoHang_DTO lo : tatCaLoHang) {

                if (lo.getMaKvlt() != null && lo.getMaKvlt().trim().equals(maKV.trim())) {

                    String tenSP = "Không tìm thấy tên";
                    if (tatCaSanPham != null) {
                        for (dto.SanPham_DTO sp : tatCaSanPham) {
                            if (sp.getMaSP() != null && lo.getMaSp() != null &&
                                    sp.getMaSP().trim().equals(lo.getMaSp().trim())) {
                                tenSP = sp.getTenSP();
                                break;
                            }
                        }
                    }

                    modelChiTiet.addRow(new Object[]{
                            stt++,
                            lo.getMaSp(),
                            tenSP,
                            lo.getSoLuongConLai(),
                            lo.getMaLo()
                    });
                }
            }
        }
    }
    private void hienThiGoiYKVLT(ArrayList<KhuVucLuuTru_DTO> list) {
        popupGoiY.setVisible(false);
        popupGoiY.removeAll();

        if (list == null || list.isEmpty()) return;

        JPanel panelNoiDung = new JPanel();
        panelNoiDung.setLayout(new BoxLayout(panelNoiDung, BoxLayout.Y_AXIS));
        panelNoiDung.setBackground(Color.WHITE);

        for (KhuVucLuuTru_DTO kv : list) {
            JButton btnItem = new JButton(kv.getMaKVLT() + " - " + kv.getTenKVLT());
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
                textLoc.setText(kv.getMaKVLT());
                popupGoiY.setVisible(false);
                timKiemKhuVuc();
            });

            btnItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent evt) {
                    btnItem.setContentAreaFilled(true);
                    btnItem.setBackground(new Color(240, 240, 240));
                }

                @Override
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

    private void thoatForm() {
        textLoc.setText("");
        if (comboBoxTrangThai != null) {
            comboBoxTrangThai.setSelectedIndex(0);
        }

        if (tableDanhSach.getRowSorter() != null) {
            tableDanhSach.setRowSorter(null);
        }
        setViewMode();
        lamMoiForm();

        loadDataToTableKVLT();
        if (popupGoiY != null) {
            popupGoiY.setVisible(false);
        }
    }


}