package gui;

import bus.*;
import dto.ChiTietGioHang_DTO;
import dto.GioHang_DTO;
import dto.KhuyenMai_DTO;
import dto.Voucher_DTO;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GIOHANG extends JPanel {

    private JPanel MainPanel;
    private JCheckBox tấtCảCheckBox;
    private JPanel pnlDanhSachSP;
    private JScrollPane scrollGioHang;
    private JButton btnMuaHang;
    private JTextField textTtien;
    private JPanel ItemPanel;
    private JButton btnChonVoucher;
    private Voucher_DTO voucherDangApDung = null;
    private List<JCheckBox> listCbxSanPham = new ArrayList<>();

    private Map<ChiTietGioHang_DTO, Double> mapGiaSale = new HashMap<>();

    private ChiTietGioHang_BUS ctBus = new ChiTietGioHang_BUS();
    private GioHang_BUS ghBus = GioHang_BUS.getInstance();
    private List<ChiTietGioHang_DTO> danhSachGioHang = new ArrayList<>();

    private String maGH_HienTai = null;

    public GIOHANG() {
        loadData();
        this.setLayout(new BorderLayout());
        if (MainPanel != null) {
            this.add(MainPanel, BorderLayout.CENTER);
            MainPanel.setBackground(Color.decode("#F5F5F5"));
        }

        if (pnlDanhSachSP != null) {
            pnlDanhSachSP.setLayout(new BoxLayout(pnlDanhSachSP, BoxLayout.Y_AXIS));
            pnlDanhSachSP.setBackground(Color.decode("#F5F5F5"));
        }

        if (scrollGioHang != null) {
            scrollGioHang.getVerticalScrollBar().setUnitIncrement(16);
            scrollGioHang.setBorder(BorderFactory.createEmptyBorder());
            scrollGioHang.getViewport().setBackground(Color.decode("#F5F5F5"));
        }

        if (textTtien != null) {
            textTtien.setEditable(false);
            textTtien.setFont(new Font("Segoe UI", Font.BOLD, 18));
            textTtien.setForeground(Color.decode("#EE4D2D"));
            textTtien.setHorizontalAlignment(JTextField.RIGHT);
            textTtien.setBorder(null);
            textTtien.setOpaque(false);
        }

        if (tấtCảCheckBox != null) {
            tấtCảCheckBox.addActionListener(e -> {
                boolean isSelected = tấtCảCheckBox.isSelected();
                for (JCheckBox cb : listCbxSanPham) {
                    cb.setSelected(isSelected);
                }
                tinhTongTien();
            });
        }

        if (btnChonVoucher != null) {
            btnChonVoucher.setText("🏷️ Chọn Voucher");
            btnChonVoucher.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnChonVoucher.setForeground(Color.decode("#EE4D2D"));
            btnChonVoucher.setContentAreaFilled(false);
            btnChonVoucher.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnChonVoucher.addActionListener(e -> hienThiDialogVoucher());
        }

        if (btnMuaHang != null) {
            btnMuaHang.setText("Mua Hàng");
            btnMuaHang.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btnMuaHang.setBackground(Color.decode("#EE4D2D"));
            btnMuaHang.setForeground(Color.WHITE);
            btnMuaHang.setFocusPainted(false);
            btnMuaHang.setBorderPainted(false);
            btnMuaHang.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btnMuaHang.addActionListener(e -> {
                if (danhSachGioHang == null || danhSachGioHang.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống!");
                    return;
                }
                List<ChiTietGioHang_DTO> danhSachMua = new ArrayList<>();
                for (int i = 0; i < listCbxSanPham.size(); i++) {
                    if (listCbxSanPham.get(i).isSelected()) {
                        danhSachMua.add(danhSachGioHang.get(i));
                    }
                }

                if (danhSachMua.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng tick chọn ít nhất 1 sản phẩm để thanh toán!");
                    return;
                }

                Window parentWindow = SwingUtilities.getWindowAncestor(this);
                JDialog popupThanhToan = new JDialog(parentWindow instanceof Frame ? (Frame) parentWindow : null, "Xác nhận Thanh Toán", true);
                ThanhToan_GUI pnlThanhToan = new ThanhToan_GUI();

                double tongTienHang = layTongTienHang();
                pnlThanhToan.setDuLieuThanhToan(danhSachMua, tongTienHang, voucherDangApDung);
                pnlThanhToan.setOnDatHangThanhCong(() -> {
                    for (ChiTietGioHang_DTO muaItem : danhSachMua) {
                        ctBus.xoa(muaItem.getMaGH(), muaItem.getMaSP());
                    }
                    loadData();
                    kiemTraChonTatCa();
                });

                popupThanhToan.getContentPane().add(pnlThanhToan);
                popupThanhToan.pack();
                popupThanhToan.setSize(650, 700);
                popupThanhToan.setLocationRelativeTo(parentWindow);
                popupThanhToan.setResizable(false);
                popupThanhToan.setVisible(true);
            });
        }

        loadData();
    }

    private double layTongTienHang() {
        double tongTien = 0;
        for (int i = 0; i < danhSachGioHang.size(); i++) {
            if (i < listCbxSanPham.size() && listCbxSanPham.get(i).isSelected()) {
                ChiTietGioHang_DTO item = danhSachGioHang.get(i);
                double donGiaGoc = getGiaSanPham(item.getMaSP());
                double giaThucTe = mapGiaSale.getOrDefault(item, donGiaGoc);
                tongTien += (giaThucTe * item.getSoLuong());
            }
        }
        return tongTien;
    }

    private void tinhTongTien() {
        if (textTtien == null) return;

        double tongTienHang = layTongTienHang();
        if (tongTienHang == 0) {
            textTtien.setText("0 VNĐ");
            return;
        }

        double tienGiam = 0;

        if (voucherDangApDung != null) {
            if (tongTienHang >= voucherDangApDung.getDonToiThieu()) {
                if (voucherDangApDung.getLoaiVoucher() == 0) {
                    tienGiam = tongTienHang * voucherDangApDung.getGiaTriVoucher();
                } else {
                    tienGiam = voucherDangApDung.getGiaTriVoucher();
                }
            } else {
                voucherDangApDung = null;
                if (btnChonVoucher != null) btnChonVoucher.setText("🏷️ Chọn Voucher");
                JOptionPane.showMessageDialog(this, "Tổng tiền không đủ điều kiện dùng voucher này nữa. Đã gỡ voucher!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        }

        double tongThanhToan = tongTienHang - tienGiam;
        if (tongThanhToan < 0) tongThanhToan = 0;

        textTtien.setText(String.format("%,.0f VNĐ", tongThanhToan));
    }


    public void loadData() {
        voucherDangApDung = null;
        danhSachGioHang = new ArrayList<>();

        if (!utils.Session.isLoggedIn() || !utils.Session.isCustomer()) {
            renderGioHang();
            return;
        }
        String sdt = utils.Session.getCurrentUser().getSdt();
        dto.KhachHang_DTO kh = bus.KhachHang_BUS.getInstance().getBysdt(sdt);

        if (kh != null) {
            String maKH = kh.getMa();
            GioHang_DTO gh = ghBus.getByMaKH(maKH);

            if (gh != null) {
                maGH_HienTai = gh.getMaGH();
                List<ChiTietGioHang_DTO> listTuDB = ctBus.getByMaGH(maGH_HienTai);
                if (listTuDB != null) {
                    danhSachGioHang = listTuDB;
                }
            }
        }

        renderGioHang();
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                loadData();
            }
        });
    }

    private void renderGioHang() {
        if (pnlDanhSachSP == null) return;
        pnlDanhSachSP.removeAll();
        listCbxSanPham.clear();

        mapGiaSale.keySet().retainAll(danhSachGioHang);

        for (ChiTietGioHang_DTO item : danhSachGioHang) {
            if(!mapGiaSale.containsKey(item)) {
                mapGiaSale.put(item, getGiaSanPham(item.getMaSP()));
            }
            JPanel cardSP = taoTheSanPham(item);
            pnlDanhSachSP.add(cardSP);
            pnlDanhSachSP.add(Box.createVerticalStrut(10));
        }

        pnlDanhSachSP.revalidate();
        pnlDanhSachSP.repaint();

        tinhTongTien();
    }

    private void kiemTraChonTatCa() {
        if (tấtCảCheckBox == null) return;
        boolean allSelected = true;

        if (listCbxSanPham.isEmpty()) allSelected = false;

        for (JCheckBox cb : listCbxSanPham) {
            if (!cb.isSelected()) {
                allSelected = false;
                break;
            }
        }
        tấtCảCheckBox.setSelected(allSelected);
    }

    private void hienThiDialogVoucher() {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow instanceof Frame ? (Frame) parentWindow : null, "Chọn Voucher", JDialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);

        JPanel pnlList = new JPanel();
        pnlList.setLayout(new BoxLayout(pnlList, BoxLayout.Y_AXIS));
        pnlList.setBackground(Color.decode("#F8F9FA"));
        pnlList.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton btnHuy = new JButton("✖ Không dùng Voucher / Bỏ áp dụng");
        btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnHuy.setForeground(Color.decode("#AD6862"));
        btnHuy.setBackground(Color.decode("#F5E6E5"));
        btnHuy.setFocusPainted(false);
        btnHuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHuy.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHuy.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        btnHuy.addActionListener(e -> {
            voucherDangApDung = null;
            tinhTongTien();
            if (btnChonVoucher != null) btnChonVoucher.setText("🏷️ Chọn Voucher");
            dialog.dispose();
        });

        pnlList.add(btnHuy);
        pnlList.add(Box.createVerticalStrut(15));
        ArrayList<Voucher_DTO> listVoucher = Voucher_BUS.getInstance().getAll();

        if (listVoucher != null && !listVoucher.isEmpty()) {
            long now = System.currentTimeMillis();
            for (Voucher_DTO v : listVoucher) {
                if (v.getTrangThai() == 1 && v.getNgayBatDau().getTime() <= now && v.getNgayKetThuc().getTime() >= now) {
                    pnlList.add(taoTheVoucherDialog(v, dialog));
                    pnlList.add(Box.createVerticalStrut(10));
                }
            }
        } else {
            JLabel lblEmpty = new JLabel("Hiện chưa có mã giảm giá nào.");
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            pnlList.add(lblEmpty);
        }

        JScrollPane scroll = new JScrollPane(pnlList);
        scroll.setBorder(null);
        dialog.add(scroll);
        dialog.setVisible(true);
    }

    private JPanel taoTheVoucherDialog(Voucher_DTO v, JDialog dialog) {
        JPanel marginWrapper = new JPanel(new BorderLayout());
        marginWrapper.setOpaque(false);
        marginWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.decode("#E8B4B0"), 1));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        card.setPreferredSize(new Dimension(0, 90));
        JPanel pnlLeft = new JPanel(new GridBagLayout());
        pnlLeft.setBackground(Color.decode("#D4847D"));
        pnlLeft.setPreferredSize(new Dimension(110, 90));

        JLabel lblMa = new JLabel("<html><center><b>" + v.getMa() + "</b></center></html>");
        lblMa.setForeground(Color.WHITE);
        lblMa.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlLeft.add(lblMa);
        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setOpaque(false);
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        StringBuilder moTaBuilder = new StringBuilder("<html><b>" + v.getTen() + "</b><br>");
        if (v.getLoaiVoucher() == 0) {
            moTaBuilder.append("<font color='#EE4D2D'>Giảm ").append(v.getGiaTriVoucher()).append("%</font><br>");
        } else {
            moTaBuilder.append("<font color='#EE4D2D'>Giảm thẳng ").append(String.format("%,.0fđ", v.getGiaTriVoucher())).append("</font><br>");
        }

        moTaBuilder.append("<i><font color='#888888'>Đơn tối thiểu: ").append(String.format("%,.0fđ", v.getDonToiThieu())).append("</font></i></html>");

        JLabel lblMoTa = new JLabel(moTaBuilder.toString());
        lblMoTa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMoTa.setForeground(Color.decode("#4A4A4A"));
        pnlCenter.add(lblMoTa, BorderLayout.CENTER);
        JPanel pnlRight = new JPanel(new GridBagLayout());
        pnlRight.setOpaque(false);
        pnlRight.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));

        JButton btnApDung = new JButton("Dùng");
        btnApDung.setFont(new Font("Segoe UI", Font.BOLD, 12));

        double tongTienHienTai = layTongTienHang();
        if (tongTienHienTai < v.getDonToiThieu() /* || v.getSoLuong() <= 0 */) {
            btnApDung.setBackground(Color.decode("#E0E0E0"));
            btnApDung.setForeground(Color.GRAY);
            btnApDung.setText("Chưa đạt");
            btnApDung.setEnabled(false);
        } else {
            btnApDung.setBackground(Color.decode("#D4847D"));
            btnApDung.setForeground(Color.WHITE);
            btnApDung.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnApDung.addActionListener(e -> {
                voucherDangApDung = v;
                tinhTongTien();
                if (btnChonVoucher != null) {
                    btnChonVoucher.setText("🏷️ Đã áp dụng: " + v.getMa());
                }
                dialog.dispose();
                JOptionPane.showMessageDialog(pnlCenter, "Đã áp dụng mã " + v.getMa() + " thành công!");
            });
        }

        btnApDung.setPreferredSize(new Dimension(85, 35));
        pnlRight.add(btnApDung);

        card.add(pnlLeft, BorderLayout.WEST);
        card.add(pnlCenter, BorderLayout.CENTER);
        card.add(pnlRight, BorderLayout.EAST);
        marginWrapper.add(card, BorderLayout.CENTER);

        return marginWrapper;
    }

    private JPanel taoTheSanPham(ChiTietGioHang_DTO item) {
        String tenSP = getTenSanPham(item.getMaSP());
        double donGia = getGiaSanPham(item.getMaSP());
        double giaSale = mapGiaSale.get(item);

        JPanel cardWrapper = new JPanel(new BorderLayout());
        cardWrapper.setBackground(Color.WHITE);
        cardWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#E0E0E0"), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        cardWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        JPanel mainContent = new JPanel(new BorderLayout(15, 0));
        mainContent.setOpaque(false);

        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlLeft.setOpaque(false);

        JCheckBox cbxItem = new JCheckBox();
        listCbxSanPham.add(cbxItem);

        cbxItem.addActionListener(e -> {
            kiemTraChonTatCa();
            tinhTongTien();
        });

        pnlLeft.add(cbxItem);

        JLabel lblAnh = new JLabel("ẢNH");
        lblAnh.setPreferredSize(new Dimension(80, 80));
        lblAnh.setOpaque(true);
        lblAnh.setBackground(Color.decode("#F5F5F5"));
        lblAnh.setHorizontalAlignment(SwingConstants.CENTER);
        lblAnh.setBorder(BorderFactory.createLineBorder(Color.decode("#EEEEEE")));
        pnlLeft.add(lblAnh);

        JPanel pnlInfo = new JPanel();
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
        pnlInfo.setOpaque(false);
        pnlInfo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel lblMa = new JLabel("Mã SP: " + item.getMaSP());
        lblMa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMa.setForeground(Color.decode("#888888"));

        JLabel lblTen = new JLabel(tenSP);
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel lblGiaGoc = new JLabel();
        lblGiaGoc.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblGiaSale = new JLabel();
        lblGiaSale.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblGiaSale.setForeground(Color.decode("#EE4D2D"));

        if (giaSale < donGia) {
            lblGiaGoc.setText("<html><font color='#999999'>Giá gốc: <strike>" + String.format("%,.0fđ", donGia) + "</strike></font></html>");
            lblGiaSale.setText("Sale: " + String.format("%,.0fđ", giaSale));
        } else {
            lblGiaGoc.setText("<html><font color='#555555'>Đơn giá: " + String.format("%,.0fđ", donGia) + "</font></html>");
            lblGiaSale.setText(" ");
        }

        pnlInfo.add(lblMa);
        pnlInfo.add(Box.createVerticalStrut(5));
        pnlInfo.add(lblTen);
        pnlInfo.add(Box.createVerticalStrut(5));
        pnlInfo.add(lblGiaGoc);
        pnlInfo.add(lblGiaSale);

        JPanel pnlRight = new JPanel(new BorderLayout());
        pnlRight.setOpaque(false);

        JPanel pnlRightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlRightTop.setOpaque(false);

        JSpinner spinSL = new JSpinner(new SpinnerNumberModel(item.getSoLuong(), 1, 100, 1));
        spinSL.setPreferredSize(new Dimension(50, 25));

        JButton btnXoa = new JButton("Xóa");
        btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnXoa.setForeground(Color.decode("#D32F2F"));
        btnXoa.setContentAreaFilled(false);
        btnXoa.setBorderPainted(false);
        btnXoa.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnXoa.addActionListener(e -> {
            boolean success = ctBus.xoa(item.getMaGH(), item.getMaSP());
            if (success) {

                danhSachGioHang.remove(item);
                mapGiaSale.remove(item);
                kiemTraChonTatCa();
                renderGioHang();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi: Không thể xóa sản phẩm khỏi cơ sở dữ liệu!");
            }
        });

        pnlRightTop.add(spinSL);
        pnlRightTop.add(btnXoa);

        JPanel pnlRightBottom = new JPanel();
        pnlRightBottom.setLayout(new BoxLayout(pnlRightBottom, BoxLayout.Y_AXIS));
        pnlRightBottom.setOpaque(false);

        JLabel lblThanhTienMoiMon = new JLabel("Thành Tiền: " + String.format("%,.0fđ", giaSale * item.getSoLuong()));
        lblThanhTienMoiMon.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblThanhTienMoiMon.setAlignmentX(Component.RIGHT_ALIGNMENT);

        spinSL.addChangeListener(e -> {
            int newSL = (int) spinSL.getValue();
            item.setSoLuong(newSL);
            ctBus.capNhatSoLuong(item);

            double currentSalePrice = mapGiaSale.get(item);
            lblThanhTienMoiMon.setText("Thành Tiền: " + String.format("%,.0fđ", currentSalePrice * item.getSoLuong()));
            tinhTongTien();
        });

        JButton btnKhuyenMaiSP = new JButton("Khuyến mãi SP ▼");
        btnKhuyenMaiSP.setForeground(Color.decode("#28A745"));
        btnKhuyenMaiSP.setContentAreaFilled(false);
        btnKhuyenMaiSP.setBorderPainted(false);
        btnKhuyenMaiSP.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnKhuyenMaiSP.setAlignmentX(Component.RIGHT_ALIGNMENT);

        pnlRightBottom.add(lblThanhTienMoiMon);
        pnlRightBottom.add(btnKhuyenMaiSP);

        pnlRight.add(pnlRightTop, BorderLayout.NORTH);
        pnlRight.add(pnlRightBottom, BorderLayout.SOUTH);

        mainContent.add(pnlLeft, BorderLayout.WEST);
        mainContent.add(pnlInfo, BorderLayout.CENTER);
        mainContent.add(pnlRight, BorderLayout.EAST);

        JPanel pnlKhuyenMaiCollapse = new JPanel();
        pnlKhuyenMaiCollapse.setLayout(new BoxLayout(pnlKhuyenMaiCollapse, BoxLayout.Y_AXIS));
        pnlKhuyenMaiCollapse.setVisible(false);
        pnlKhuyenMaiCollapse.setBackground(Color.decode("#F9F9F9"));
        pnlKhuyenMaiCollapse.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        ArrayList<KhuyenMai_DTO> listKM = KhuyenMai_BUS.getInstance().getAll();

        if (listKM != null) {
            for (KhuyenMai_DTO km : listKM) {
                if (km.getTrangThai() == 1 && km.getDoiTuongApDung() == 1) {
                    if (km.getMaSanPham() != null && item.getMaSP() != null &&
                            km.getMaSanPham().trim().equalsIgnoreCase(item.getMaSP().trim())) {
                        pnlKhuyenMaiCollapse.add(taoTheKhuyenMaiSP(
                                km, item, lblGiaGoc, lblGiaSale, lblThanhTienMoiMon, pnlKhuyenMaiCollapse, btnKhuyenMaiSP, donGia, tenSP
                        ));
                        pnlKhuyenMaiCollapse.add(Box.createVerticalStrut(5));
                    }
                }
            }
        }

        btnKhuyenMaiSP.addActionListener(e -> {
            boolean isVis = pnlKhuyenMaiCollapse.isVisible();
            pnlKhuyenMaiCollapse.setVisible(!isVis);
            btnKhuyenMaiSP.setText(isVis ? "Khuyến mãi SP ▼" : "Khuyến mãi SP ▲");
            pnlDanhSachSP.revalidate();
            pnlDanhSachSP.repaint();
        });

        cardWrapper.add(mainContent, BorderLayout.CENTER);
        cardWrapper.add(pnlKhuyenMaiCollapse, BorderLayout.SOUTH);

        return cardWrapper;
    }

    private JPanel taoTheKhuyenMaiSP(KhuyenMai_DTO km, ChiTietGioHang_DTO item, JLabel lblGiaGoc, JLabel lblGiaSale,
                                     JLabel lblThanhTienMoiMon, JPanel pnlCollapse, JButton btnToggle, double donGia, String tenSP) {

        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#E0E0E0"), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        card.setBackground(Color.WHITE);

        JLabel lblInfo = new JLabel("<html><font color='#28A745'><b>" + km.getMaKM() + "</b></font> - " + km.getTenKM() + "</html>");

        JButton btnApDung = new JButton("Áp dụng");
        btnApDung.setBackground(Color.decode("#28A745"));
        btnApDung.setForeground(Color.WHITE);
        btnApDung.setFocusPainted(false);
        btnApDung.setBorderPainted(false);
        btnApDung.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnApDung.addActionListener(e -> {
            double tienGiam = 0;
            if (km.getLoaiKhuyenMai() == 0) {
                double phanTram = km.getGiaTriKhuyenMai();
                if(phanTram <= 1.0) phanTram = phanTram * 100;
                tienGiam = donGia * phanTram / 100;
            } else {
                tienGiam = km.getGiaTriKhuyenMai();
            }

            double giaSaleMoi = donGia - tienGiam;
            if(giaSaleMoi < 0) giaSaleMoi = 0.0;

            mapGiaSale.put(item, giaSaleMoi);

            lblGiaSale.setText("Sale: " + String.format("%,.0fđ", giaSaleMoi));
            lblGiaGoc.setText("<html><font color='#999999'>Giá gốc: <strike>" + String.format("%,.0fđ", donGia) + "</strike></font></html>");
            lblThanhTienMoiMon.setText("Thành Tiền: " + String.format("%,.0fđ", giaSaleMoi * item.getSoLuong()));

            tinhTongTien();

            pnlCollapse.setVisible(false);
            btnToggle.setText("🏷️ Đã chọn: " + km.getMaKM());

            JOptionPane.showMessageDialog(this, "Đã áp dụng " + km.getMaKM() + " cho SP: " + tenSP);
        });

        card.add(lblInfo, BorderLayout.CENTER);
        card.add(btnApDung, BorderLayout.EAST);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        return card;
    }


    private String getTenSanPham(String maSP) {
        dto.SanPham_DTO sp = bus.SanPham_BUS.getInstance().getById(maSP);
        if (sp != null) {
            return sp.getTenSP();
        }
        return "Sản phẩm không tồn tại";
    }

    private double getGiaSanPham(String maSP) {
        return HoaDonBan_BUS.getInstance().getGiaBanSP(maSP, 1);
    }
}