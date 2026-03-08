package gui;

import bus.ThanhToan_BUS;
import bus.Voucher_BUS;
import dto.ThanhToan_DTO;
import dto.Voucher_DTO;
import dto.ChiTietGioHang_DTO; // Đã đổi import chuẩn

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ThanhToan_GUI extends JPanel {

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
    private JButton đặtHàngButton;
    private JPanel pnlDanhSachMua;
    private JButton btnSua;
    private JButton btnThemDC;

    private Runnable onDatHangThanhCongCallback;
    private List<ChiTietGioHang_DTO> danhSachMua; // Đã đổi thành DTO chuẩn
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
        if (pnlDanhSachMua != null) {
            pnlDanhSachMua.setLayout(new BoxLayout(pnlDanhSachMua, BoxLayout.Y_AXIS));
            pnlDanhSachMua.setBackground(Color.WHITE);
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

        // Lấy tên và giá từ Database (thông qua BUS)
        String tenSP = getTenSanPham(item.getMaSP());
        double donGia = getGiaSanPham(item.getMaSP());

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

        // 1. Tính tổng tiền nguyên giá chưa áp dụng bất kỳ mã nào
        if (danhSachMua != null) {
            for (ChiTietGioHang_DTO item : danhSachMua) {
                tongTienHangGocToanBo += (getGiaSanPham(item.getMaSP()) * item.getSoLuong());
            }
        }

        // 2. Tính số tiền được giảm nhờ Khuyến Mãi Sản Phẩm bên Giỏ Hàng truyền sang
        double tongGiamGiaSanPham = tongTienHangGocToanBo - this.tienHangTruocVoucher;
        if (tongGiamGiaSanPham < 0) tongGiamGiaSanPham = 0;

        // 3. Tính tiền giảm nhờ Voucher của toàn đơn hàng
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

        // 4. Tổng hợp tất cả tiền được giảm
        double tongTatCaGiamGia = tongGiamGiaSanPham + tienGiamVoucher;
        textTongGiamGia.setText(String.format("- %,.0f VNĐ", tongTatCaGiamGia));

        // 5. Chốt sổ cuối cùng
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
        JDialog dialog = new JDialog(parentWindow instanceof Frame ? (Frame) parentWindow : null, "Thanh toán bằng " + phuongThuc, JDialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String soTien = textTongThanhToan.getText();
        JLabel lblTitle = new JLabel("<html><center><h2>" + phuongThuc + "</h2>Vui lòng quét mã QR bên dưới để thanh toán<br>Số tiền: <font color='red'><b>" + soTien + "</b></font></center></html>");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblQR = new JLabel("<html><center><font color='gray'>[ĐẶT ẢNH MÃ QR Ở ĐÂY]</font></center></html>", SwingConstants.CENTER);
        lblQR.setPreferredSize(new Dimension(250, 250));
        lblQR.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));


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
        if (đặtHàngButton != null) {
            đặtHàngButton.addActionListener(e -> {
                String ten = textTen.getText().trim();
                String sdt = textSDT.getText().trim();
                String diaChi = textDiaChi.getText().trim();

                if (ten.isEmpty() || sdt.isEmpty() || diaChi.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Tên, Số điện thoại và Địa chỉ!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!sdt.matches("\\d{10,11}")) {
                    JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int phuongThuc = comboBoxPthucTt.getSelectedIndex();
                if (phuongThuc == 1 || phuongThuc == 2) {
                    String tenPhuongThuc = (String) comboBoxPthucTt.getSelectedItem();
                    hienThiDialogQR(tenPhuongThuc, ten);
                } else {
                    xuLyDatHangThanhCong(ten);
                }
            });
        }
    }

    public void setOnDatHangThanhCong(Runnable callback) {
        this.onDatHangThanhCongCallback = callback;
    }

    private String getTenSanPham(String maSP) {
        dto.SanPham_DTO sp = bus.SanPham_BUS.getInstance().getById(maSP);
        if (sp != null) {
            return sp.getTenSP();
        }
        return "Sản phẩm không tồn tại";
    }

    private double getGiaSanPham(String maSP) {
        dto.SanPham_DTO sp = bus.SanPham_BUS.getInstance().getById(maSP);
        if (sp != null) {
            return sp.getLoiNhuan();
        }
        return 0;
    }
}