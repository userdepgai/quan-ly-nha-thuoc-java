package gui.trangChuDatHang;

import bus.*;
import dto.SanPham_DTO;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import utils.Session;
import utils.GuestCart;
import dto.*;

public class ProductCard extends JPanel {
    private SanPham_DTO sp;

    public ProductCard(SanPham_DTO sp) {
        this.sp = sp;

        setLayout(new BorderLayout(2,2));
        setPreferredSize(new Dimension(250,280));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel img = new JLabel();
        img.setHorizontalAlignment(SwingConstants.CENTER);
        img.setPreferredSize(new Dimension(250,140));

        String realPath = getRealImagePath(sp.getHinhAnh());

        if(realPath != null){
            ImageIcon icon = new ImageIcon(realPath);
            Image image = icon.getImage().getScaledInstance(180,130,Image.SCALE_SMOOTH);
            img.setIcon(new ImageIcon(image));
        }else{
            img.setText("No Image");
        }

        JPanel info = new JPanel(new GridBagLayout());
        info.setBorder(BorderFactory.createEmptyBorder(3,10,3,10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2,2,2,2);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField ma = new JTextField(sp.getMaSP());
        JTextField ten = new JTextField(sp.getTenSP());
        JTextField gia = new JTextField(String.format("%,.0f VND", getGiaBan(sp.getMaSP())));

        ma.setEditable(false);
        ten.setEditable(false);
        gia.setEditable(false);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        info.add(new JLabel("Mã SP:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        info.add(ma, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        info.add(new JLabel("Tên:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        info.add(ten, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        info.add(new JLabel("Giá:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        info.add(gia, gbc);

        JButton btnThem = new JButton("Thêm giỏ");
        JButton btnChiTiet = new JButton("Chi tiết");

        JPanel btnPanel = new JPanel(new GridLayout(1,2,6,0));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(3,10,6,10)); // giảm khoảng cách

        btnPanel.add(btnThem);
        btnPanel.add(btnChiTiet);

        add(img,BorderLayout.NORTH);
        add(info,BorderLayout.CENTER);
        add(btnPanel,BorderLayout.SOUTH);
        addMouseListener(new java.awt.event.MouseAdapter(){

            public void mouseEntered(java.awt.event.MouseEvent evt){
                setBorder(BorderFactory.createLineBorder(Color.BLUE));
            }

            public void mouseExited(java.awt.event.MouseEvent evt){
                setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }

        });
        btnThem.addActionListener(e -> xuLyThemGiaHang());
        btnChiTiet.addActionListener(e -> {

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            new SanPhamChiTiet_GUI(parent, sp).setVisible(true);

        });
    }
    private void xuLyThemGiaHang() {
        String maSP = sp.getMaSP();

        if(!Session.isLoggedIn()) {
            GuestCart.addProduct(maSP,1);
            JOptionPane.showMessageDialog(this,
                    "Đã thêm vào giỏ (tạm)");
            return;
        }

        if(!Session.isCustomer()){
            JOptionPane.showMessageDialog(this,
                    "Chỉ khách hàng mới có giỏ hàng");
            return;
        }

        String sdt = Session.getCurrentUser().getSdt();

        KhachHang_DTO kh = KhachHang_BUS.getInstance().getBysdt(sdt);
        if(kh == null){
            JOptionPane.showMessageDialog(this,
                    "Không tìm thấy khách hàng");
            return;
        }

        String maKH = kh.getMa();
        GioHang_DTO gh = GioHang_BUS.getInstance().getByMaKH(maKH);
        if(gh == null){
            JOptionPane.showMessageDialog(this,"Không tìm thấy giỏ hàng");
            return;
        }
        ChiTietGioHang_BUS ctBUS = new ChiTietGioHang_BUS();

        ChiTietGioHang_DTO existing = ctBUS.getById(gh.getMaGH(),maSP);

        if(existing != null){
            existing.setSoLuong(existing.getSoLuong()+1);
            ctBUS.capNhatSoLuong(existing);
        }else{
            ctBUS.them(new ChiTietGioHang_DTO(gh.getMaGH(), maSP, 1));
        }
        JOptionPane.showMessageDialog(this,
                "Đã thêm vào giỏ");

    }
    private String getRealImagePath(String path){

        if(path == null || path.trim().isEmpty())
            return null;

        String realPath = path;
        if(path.startsWith("img/")){
            realPath = path.replace("img/","images/");
        }
        File f = new File("src/" + realPath);

        if(f.exists()){
            return "src/" + realPath;
        }
        f = new File(realPath);

        if(f.exists()){
            return realPath;
        }
        return null;
    }
    private double getGiaBan(String maSP) {
        double giaNhap = LoHang_BUS.getInstance().getGiaNhapThapNhatByMaSP(maSP);
        return SanPham_BUS.getInstance().tinhGiaBan(maSP,giaNhap);
    }

}