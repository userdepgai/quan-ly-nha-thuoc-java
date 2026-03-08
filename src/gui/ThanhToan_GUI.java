package gui;
import bus.ThanhToan_BUS;
import bus.Voucher_BUS;
import dto.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ThanhToan_GUI extends JPanel {
    private String maDiaChiGiaoHang;
    private JPanel mainPanel;
    private JTextField textTen;
    private JTextField textDiaChi;
    private JTextField textSDT;
    private JTextField textGhiChu;
    private JButton voucherButton;
    private JComboBox<String> comboBoxPthucTt;
    private JTextField textTienHang;
    private JTextField textPhiVanChuyen;
    private JTextField textTongGiamGia;
    private JTextField textTongThanhToan;
    private JButton btnDatHang;

    private JScrollPane scrollDanhSachMua;
    private JPanel pnlDanhSachMua;
    private String maHoaDonVuaTao;
    private JButton btnSua;
    private JButton btnThemDC;

    private Runnable onDatHangThanhCongCallback;
    private List<ChiTietGioHang_DTO> danhSachMua;
    private double tienHangTruocVoucher;
    private Voucher_DTO voucherApDung;
    private final double PHI_VAN_CHUYEN = 15000;

    private ThanhToan_BUS thanhToanBUS;

    public ThanhToan_GUI() {
        if (mainPanel != null) {
            this.setLayout(new BorderLayout());
            this.add(mainPanel, BorderLayout.CENTER);
        }

        thanhToanBUS = new ThanhToan_BUS();

        caiDatGiaoDienBanDau();
        ganSuKien();
    }

    private void tuDongDienThongTin() {
        dto.TaiKhoan_DTO tk = utils.Session.getCurrentUser();

        if (tk == null) return;

        String idTaiKhoan = tk.getMaTK();

        ThanhToan_DTO thongTin = thanhToanBUS.layThongTinKhachHang(idTaiKhoan);

        if (thongTin != null) {
            textTen.setText(thongTin.getTenKH());
            textSDT.setText(thongTin.getSdt());
            textDiaChi.setText(thongTin.getDiaChiChiTiet());
        } else {
            System.out.println("Không tìm thấy thông tin cho tài khoản: " + idTaiKhoan);
        }
    }

    private void caiDatGiaoDienBanDau() {
            if (scrollDanhSachMua != null) {
                pnlDanhSachMua = new JPanel();
                pnlDanhSachMua.setLayout(new BoxLayout(pnlDanhSachMua, BoxLayout.Y_AXIS));
                pnlDanhSachMua.setBackground(Color.WHITE);
                scrollDanhSachMua.setViewportView(pnlDanhSachMua);

                scrollDanhSachMua.getVerticalScrollBar().setUnitIncrement(16);
                scrollDanhSachMua.setBorder(BorderFactory.createEmptyBorder());
                scrollDanhSachMua.getViewport().setBackground(Color.WHITE);
            }

        textTen.setEditable(false);
        textSDT.setEditable(false);
        textDiaChi.setEditable(false);
        textTienHang.setEditable(false);
        textPhiVanChuyen.setEditable(false);
        textTongGiamGia.setEditable(false);
        textTongThanhToan.setEditable(false);
        textTongThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        textTongThanhToan.setForeground(Color.RED);

        if (comboBoxPthucTt != null) {
            comboBoxPthucTt.removeAllItems();
            comboBoxPthucTt.addItem("Thanh toán tiền mặt (COD)");
            comboBoxPthucTt.addItem("Chuyển khoản ngân hàng");
            comboBoxPthucTt.addItem("Ví MoMo");
        }
        tuDongDienThongTin();
    }

    private JPanel taoDongSanPham(ChiTietGioHang_DTO item) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        row.setPreferredSize(new Dimension(0, 60));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        String tenSP = thanhToanBUS.getTenSanPham(item.getMaSP());
        double donGia = thanhToanBUS.getGiaSanPham(item.getMaSP());

        JLabel lblInfo = new JLabel("<html><b>" + tenSP + "</b><br><font color='gray'>Mã: " + item.getMaSP() + "</font></html>");
        lblInfo.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        double thanhTienGoc = donGia * item.getSoLuong();

        JLabel lblPrice = new JLabel("<html>x" + item.getSoLuong() + "<br>Thành tiền: <font color='red'><b>" + String.format("%,.0fđ", thanhTienGoc) + "</b></font></html>");
        lblPrice.setHorizontalAlignment(SwingConstants.RIGHT);
        lblPrice.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        row.add(lblInfo, BorderLayout.CENTER);
        row.add(lblPrice, BorderLayout.EAST);

        return row;
    }

    public void setDuLieuThanhToan(List<ChiTietGioHang_DTO> dsMua, double tongTienSauKhuyenMaiSP, Voucher_DTO voucher) {
        this.danhSachMua = dsMua;
        this.tienHangTruocVoucher = tongTienSauKhuyenMaiSP;
        this.voucherApDung = voucher;

        if (pnlDanhSachMua != null) {
            pnlDanhSachMua.removeAll();
            for (ChiTietGioHang_DTO item : dsMua) {
                pnlDanhSachMua.add(taoDongSanPham(item));
                pnlDanhSachMua.add(Box.createVerticalStrut(5));
            }
            pnlDanhSachMua.revalidate();
            pnlDanhSachMua.repaint();
        }

        tinhToanVaHienThi();
    }

    private void tinhToanVaHienThi() {
        double tongTienHangGocToanBo = 0;

        if (danhSachMua != null) {
            for (ChiTietGioHang_DTO item : danhSachMua) {
                tongTienHangGocToanBo +=
                        (thanhToanBUS.getGiaSanPham(item.getMaSP()) * item.getSoLuong());
            }
        }

        double tongGiamGiaSanPham = tongTienHangGocToanBo - this.tienHangTruocVoucher;
        if (tongGiamGiaSanPham < 0) tongGiamGiaSanPham = 0;

        double tienGiamVoucher = 0;
        if (voucherApDung != null) {
            if (voucherApDung.getLoaiVoucher() == 0) {
                double phanTram = voucherApDung.getGiaTriVoucher();
                if (phanTram > 1.0) phanTram = phanTram / 100.0;
                tienGiamVoucher = this.tienHangTruocVoucher * phanTram;
            } else {
                tienGiamVoucher = voucherApDung.getGiaTriVoucher();
            }
            voucherButton.setText("🏷️ Đã áp dụng: " + voucherApDung.getMa());
            voucherButton.setForeground(Color.decode("#28A745"));
        } else {
            voucherButton.setText("Chọn Voucher");
            voucherButton.setForeground(Color.BLACK);
        }

        textTienHang.setText(String.format("%,.0f VNĐ", tongTienHangGocToanBo));
        textPhiVanChuyen.setText(String.format("%,.0f VNĐ", PHI_VAN_CHUYEN));

        double tongTatCaGiamGia = tongGiamGiaSanPham + tienGiamVoucher;
        textTongGiamGia.setText(String.format("- %,.0f VNĐ", tongTatCaGiamGia));

        double tongThanhToan = tongTienHangGocToanBo + PHI_VAN_CHUYEN - tongTatCaGiamGia;
        if (tongThanhToan < 0) tongThanhToan = 0;

        textTongThanhToan.setText(String.format("%,.0f VNĐ", tongThanhToan));
    }

    private void hienThiDialogVoucher() {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow instanceof Frame ? (Frame) parentWindow : null, "Chọn Voucher", JDialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(450, 450);
        dialog.setLocationRelativeTo(this);

        JPanel pnlList = new JPanel();
        pnlList.setLayout(new BoxLayout(pnlList, BoxLayout.Y_AXIS));
        pnlList.setBackground(Color.decode("#F8F9FA"));
        pnlList.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton btnHuy = new JButton("✖ Không dùng Voucher / Bỏ áp dụng");
        btnHuy.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHuy.addActionListener(e -> {
            voucherApDung = null;
            tinhToanVaHienThi();
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
            pnlList.add(new JLabel("Không có voucher khả dụng."));
        }

        JScrollPane scroll = new JScrollPane(pnlList);
        scroll.setBorder(null);
        dialog.add(scroll);
        dialog.setVisible(true);
    }

    private JPanel taoTheVoucherDialog(Voucher_DTO v, JDialog dialog) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.decode("#E8B4B0"), 1));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setPreferredSize(new Dimension(0, 80));
        String desc = "<html><b>" + v.getMa() + "</b> - " + v.getTen() + "<br>"
                + "<font color='#888888'>Đơn tối thiểu: " + String.format("%,.0fđ", v.getDonToiThieu()) + "</font></html>";
        JLabel lblDesc = new JLabel(desc);
        lblDesc.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

        JButton btnApDung = new JButton("Dùng");
        btnApDung.addActionListener(e -> {
            if (this.tienHangTruocVoucher < v.getDonToiThieu()) {
                JOptionPane.showMessageDialog(dialog, "Đơn hàng chưa đủ giá trị tối thiểu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            this.voucherApDung = v;
            tinhToanVaHienThi();
            dialog.dispose();
        });

        card.add(lblDesc, BorderLayout.CENTER);
        card.add(btnApDung, BorderLayout.EAST);

        return card;
    }

    private void xuLyDatHangThanhCong(String tenKhachHang) {
        JOptionPane.showMessageDialog(this, "🎉 Đặt hàng thành công!\nCảm ơn " + tenKhachHang + " đã mua sắm.");

        Window win = SwingUtilities.getWindowAncestor(this);
        if (win instanceof JDialog) {
            win.dispose();
        }
        if (onDatHangThanhCongCallback != null) {
            onDatHangThanhCongCallback.run();
        }
    }
    private void hienThiDialogQR(String phuongThuc, String tenKhachHang) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow instanceof Frame ? (Frame) parentWindow : null,
                "Thanh toán bằng " + phuongThuc, JDialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String soTien = textTongThanhToan.getText();
        JLabel lblTitle = new JLabel("<html><center><h2>" + phuongThuc + "</h2>"
                + "Vui lòng quét mã QR bên dưới để thanh toán<br>"
                + "Số tiền: <font color='red'><b>" + soTien + "</b></font></center></html>");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblQR = new JLabel();
        int qrSize = 250;
        lblQR.setPreferredSize(new Dimension(qrSize, qrSize));
        lblQR.setHorizontalAlignment(SwingConstants.CENTER);
        lblQR.setVerticalAlignment(SwingConstants.CENTER);
        lblQR.setBackground(Color.WHITE);
        lblQR.setOpaque(true);
        lblQR.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        try {
            java.net.URL imgURL = getClass().getResource("/images/qr.jpg");

            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(qrSize, qrSize, Image.SCALE_SMOOTH);

                lblQR.setIcon(new ImageIcon(scaledImg));
                lblQR.setText("");
            } else {
                lblQR.setText("Lỗi: Không tìm thấy file qr.jpg");
                System.err.println("Đường dẫn /images/qr.jpg không tồn tại trong resources!");
            }
        } catch (Exception e) {
            lblQR.setText("Lỗi tải ảnh!");
            e.printStackTrace();
        }

        pnlCenter.add(lblTitle, BorderLayout.NORTH);
        pnlCenter.add(lblQR, BorderLayout.CENTER);
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlBottom.setBackground(Color.WHITE);

        JButton btnHuy = new JButton("Hủy giao dịch");
        btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnHuy.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnXacNhan = new JButton("Tôi đã thanh toán");
        btnXacNhan.setBackground(Color.decode("#28A745"));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXacNhan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHuy.addActionListener(e -> dialog.dispose());

        btnXacNhan.addActionListener(e -> {
            dialog.dispose();
            xuLyDatHangThanhCong(tenKhachHang);
        });

        pnlBottom.add(btnHuy);
        pnlBottom.add(btnXacNhan);

        dialog.add(pnlCenter, BorderLayout.CENTER);
        dialog.add(pnlBottom, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
    private void ganSuKien() {

        if (voucherButton != null) {
            voucherButton.addActionListener(e -> hienThiDialogVoucher());
        }

        if (btnSua != null) {
            btnSua.addActionListener(e -> {

                boolean dangKhoa = !textTen.isEditable();

                if (dangKhoa) {
                    textTen.setEditable(true);
                    textSDT.setEditable(true);
                    textDiaChi.setEditable(true);
                    btnSua.setText("Lưu");
                    textTen.requestFocus();
                } else {
                    textTen.setEditable(false);
                    textSDT.setEditable(false);
                    textDiaChi.setEditable(false);
                    btnSua.setText("Sửa");
                }
            });
        }

        btnDatHang.addActionListener(e -> xuLyDatHang());
    }

    public void setOnDatHangThanhCong(Runnable callback) {
        this.onDatHangThanhCongCallback = callback;
    }
    public ThanhToan_DTO layDuLieuDonHang() {

        ThanhToan_DTO dto = new ThanhToan_DTO();

        dto.setTenKH(textTen.getText().trim());
        dto.setSdt(textSDT.getText().trim());
        dto.setDiaChiChiTiet(textDiaChi.getText().trim());
        dto.setGhiChu(textGhiChu.getText().trim());

        if (voucherApDung != null) {
            dto.setMaVoucher(voucherApDung.getMa());
        }

        String tong = textTongThanhToan.getText().replaceAll("[^0-9]", "");
        if (!tong.isEmpty()) {
            dto.setTongThanhToan(Double.parseDouble(tong));
        }

        List<ChiTietHoaDonBan_DTO> list = new ArrayList<>();

        if (danhSachMua != null && !danhSachMua.isEmpty()) {

            for (ChiTietGioHang_DTO item : danhSachMua) {

                ChiTietHoaDonBan_DTO ct = new ChiTietHoaDonBan_DTO();

                ct.setMaSP(item.getMaSP());
                ct.setMaLo(item.getMaLo());
                ct.setSoLuong(item.getSoLuong());
                double gia = thanhToanBUS.getGiaSanPham(item.getMaSP());
                ct.setGiaBan(gia);
                ct.setGiaBanSauApKM(gia);
                ct.setThanhTien(gia * item.getSoLuong());

                list.add(ct);
            }
        }

        dto.setDanhSachSanPham(list);

        return dto;
    }

    private void xuLyDatHang() {

        try {
            ThanhToan_DTO donHang = layDuLieuDonHang();

            if (donHang.getDanhSachSanPham() == null ||
                    donHang.getDanhSachSanPham().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Giỏ hàng trống!");
                return;
            }

            if (utils.Session.getCurrentUser() == null) {

                JOptionPane.showMessageDialog(this,
                        "Phiên đăng nhập hết hạn!");
                return;
            }

            String sdtTK = utils.Session.getCurrentUser().getSdt();

            ArrayList<ChiTietHoaDonBan_DTO> dsCT =
                    new ArrayList<>(donHang.getDanhSachSanPham());

            maHoaDonVuaTao =
                    thanhToanBUS.taoDonOnline(
                            sdtTK,
                            this.maDiaChiGiaoHang,
                            dsCT
                    );

            if (maHoaDonVuaTao == null) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy khách hàng!");
                return;
            }

            System.out.println("Đã tạo hóa đơn: " + maHoaDonVuaTao);

            int phuongThuc = comboBoxPthucTt.getSelectedIndex();

            // COD
            if (phuongThuc == 0) {

                xuLyDatHangThanhCong(donHang.getTenKH());

            }
            else {

                String tenPT =
                        (String) comboBoxPthucTt.getSelectedItem();

                hienThiDialogQR(tenPT, donHang.getTenKH());
            }

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Đặt hàng thất bại\n" + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}