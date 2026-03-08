package gui.trangChuDatHang;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import bus.*;
import dto.*;

public class SanPhamChiTiet_GUI extends JDialog {

    private JLabel lblImage;
    private JPanel panelThuocTinh;

    public SanPhamChiTiet_GUI(JFrame parent, SanPham_DTO sp) {

        super(parent, "Chi tiết sản phẩm", true);

        setSize(500,350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10,10));

        lblImage = new JLabel();
        lblImage.setPreferredSize(new Dimension(220,280));
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);

        loadImage(sp.getHinhAnh());

        add(lblImage,BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info,BoxLayout.Y_AXIS));
        info.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        info.add(new JLabel("<html><b>Mã SP:</b> "+sp.getMaSP()+"</html>"));

        info.add(new JLabel(
                "<html><body style='width:230px'><b>Tên SP:</b> "
                        + sp.getTenSP() + "</body></html>"
        ));

        info.add(new JLabel("<html><b>Đơn vị tính:</b> "+sp.getDonViTinh()+"</html>"));

        info.add(new JLabel("<html><b>Giá:</b> " + String.format("%,.0f VND", getGiaBan(sp.getMaSP())) + "</html>"));

        info.add(new JLabel("<html><b>Số lượng tồn:</b> " + getSoLuongTon(sp.getMaSP())+ "</html>"));

        info.add(Box.createVerticalStrut(10));
        info.add(new JSeparator());
        info.add(Box.createVerticalStrut(10));

        panelThuocTinh = new JPanel();
        panelThuocTinh.setLayout(new BoxLayout(panelThuocTinh,BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(panelThuocTinh);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(240,150));

        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        info.add(scroll);

        add(info,BorderLayout.CENTER);

        loadThuocTinh(sp.getMaSP());
    }

    private void loadImage(String path){

        String realPath = getRealImagePath(path);

        if(realPath != null){

            ImageIcon icon = new ImageIcon(realPath);

            Image img = icon.getImage().getScaledInstance(
                    200,260,Image.SCALE_SMOOTH
            );

            lblImage.setIcon(new ImageIcon(img));

        }else{

            lblImage.setText("No Image");

        }
    }

    private void loadThuocTinh(String maSP){

        SanPham_BUS bus = SanPham_BUS.getInstance();

        var list = bus.getThuocTinhSP(maSP);

        for(String[] row : list){

            String ten = row[0];
            String value = row[1];

            JLabel lb = new JLabel(
                    "<html><body style='width:210px'><b>"
                            + ten + ":</b> " + value +
                            "</body></html>"
            );

            panelThuocTinh.add(lb);
        }

        panelThuocTinh.revalidate();
        panelThuocTinh.repaint();
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
    private int getSoLuongTon(String maSP) {
        return LoHang_BUS.getInstance().getTongSPTonByMaSP(maSP);
    }
}