package gui;

import dto.SanPham_DTO;

import javax.swing.*;
import java.awt.*;

public class ChiTietSanPhamCard_GUI extends JFrame {

    private JPanel panel_main;
    private JLabel lblHinhAnh;
    private JTextArea txt_moTa;
    private JTextField txt_gia;
    private JTextField txt_danhMuc;
    private JTextField txt_quyCach;
    private JTextField txt_dangBaoChe;
    private JTextField txt_thanhPhan;
    private JTextField txt_maSP;
    private JTextField txt_tenSP;
    private JTextField txt_thuocCanKeToa;
    private JButton btn_dong;

    public ChiTietSanPhamCard_GUI(SanPham_DTO sp, double giaBan) {
        if (panel_main == null) {
            panel_main = new JPanel();
        }
        setTitle("Chi tiết sản phẩm");
        setContentPane(panel_main);
        setSize(600,500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // ===== HIỂN THỊ DỮ LIỆU =====
        txt_maSP.setText(sp.getMaSP());
        txt_tenSP.setText(sp.getTenSP());
        if(giaBan <= 0){
            txt_gia.setText("Đang cập nhật");
        }else{
            txt_gia.setText(String.format("%,.0f VNĐ", giaBan));
        }

        //txt_danhMuc.setText(sp.getDanhMuc());
        //txt_quyCach.setText(sp.getQuyCach());
        //txt_dangBaoChe.setText(sp.getDangBaoChe());
        //txt_thanhPhan.setText(sp.getThanhPhan());
        //txt_thuocKeToa.setText(sp.getThuocKeToa());

        //txt_moTa.setText(sp.getMoTa());

        // ===== KHÔNG CHO SỬA =====
        txt_maSP.setEditable(false);
        txt_tenSP.setEditable(false);
        txt_gia.setEditable(false);
        txt_danhMuc.setEditable(false);
        txt_quyCach.setEditable(false);
        txt_dangBaoChe.setEditable(false);
        txt_thanhPhan.setEditable(false);
        txt_thuocCanKeToa.setEditable(false);
        txt_moTa.setEditable(false);

        // ===== HIỂN THỊ HÌNH =====
        setHinhAnh(sp.getHinhAnh());
    }

    private void setHinhAnh(String path){

        if(path == null || path.isEmpty()){
            lblHinhAnh.setText("No Image");
            lblHinhAnh.setHorizontalAlignment(SwingConstants.CENTER);
            return;
        }

        ImageIcon icon = new ImageIcon(path);

        Image img = icon.getImage().getScaledInstance(
                200,
                200,
                Image.SCALE_SMOOTH
        );

        lblHinhAnh.setIcon(new ImageIcon(img));
        btn_dong.addActionListener(e -> dispose());
    }

}