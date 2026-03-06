package gui;

import bus.TrangChu_BUS;
import dto.SanPham_DTO;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class TrangChu_GUI extends JPanel {

    private JPanel panel_trangChu = new JPanel(new BorderLayout());

    private JComboBox<String> cmb_locTrangThai = new JComboBox<>();
    private JComboBox<String> cmb_timKiem = new JComboBox<>();
    private JComboBox<String> cmb_khoangGia = new JComboBox<>();
    private JComboBox<String> cmb_loai = new JComboBox<>();
    private JComboBox<String> cmb_congDung = new JComboBox<>();
    private JComboBox<String> cmb_doiTuong = new JComboBox<>();

    private JButton btn_timKiem = new JButton("Tìm kiếm");
    private JButton btn_thoat = new JButton("Thoát");

    private JButton btnPrev = new JButton("<<");
    private JButton btnNext = new JButton(">>");

    private JPanel sanPham = new JPanel();
    private JScrollPane scroll_sanPham = new JScrollPane(sanPham);
    private JLabel lblTrangChu;
    private JTextField txt_timKiem;
    private JPopupMenu popupGoiY = new JPopupMenu();
    private JPanel panelNangCao = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JPanel panelSoTrang = new JPanel();
    private TrangChu_BUS bus = TrangChu_BUS.getInstance();

    private int page = 1;
    private int limit = 12;

    private ArrayList<SanPham_DTO> currentList;

    public TrangChu_GUI() {

        initComponents();
        initData();
        cmb_locTrangThai.addActionListener(e -> {

            String selected = cmb_locTrangThai.getSelectedItem().toString();

            if(selected.equals("Lọc theo tên")){
                txt_timKiem.setToolTipText("Nhập tên sản phẩm");
            }
            else if(selected.equals("Lọc theo mã")){
                txt_timKiem.setToolTipText("Nhập mã sản phẩm");
            }

        });
        txt_timKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {

                if(!txt_timKiem.getText().trim().isEmpty()){
                    hienThiGoiY(bus.timKiem(txt_timKiem.getText().trim(), "Tất cả", "Tất cả", "Tất cả"));
                }
                else{
                    popupGoiY.setVisible(false);
                }

            }
        });
        panelNangCao.setVisible(false);

        currentList = bus.getAll();
        loadDanhSach(currentList);
        loadSoTrang();

        btnPrev.addActionListener(e -> {

            if(page > 1){
                page--;
                loadDanhSach(currentList);
                loadSoTrang();
            }

        });

        btnNext.addActionListener(e -> {

            int maxPage = (int)Math.ceil((double) currentList.size() / limit);

            if(page < maxPage){
                page++;
                loadDanhSach(currentList);
                loadSoTrang();
            }

        });

        cmb_loai.addActionListener(e -> {

            String loai = cmb_loai.getSelectedItem().toString();

            cmb_congDung.removeAllItems();
            cmb_doiTuong.removeAllItems();

            if(loai.equals("Tất cả")){
                panelNangCao.setVisible(false);
            }

            else if(loai.equals("Thuốc")){

                panelNangCao.setVisible(true);

                cmb_congDung.addItem("Giảm đau");
                cmb_congDung.addItem("Kháng sinh");
                cmb_congDung.addItem("Hạ sốt");

                cmb_doiTuong.addItem("Người lớn");
                cmb_doiTuong.addItem("Trẻ em");
                cmb_doiTuong.addItem("Phụ nữ mang thai");
            }

            else if(loai.equals("Thực phẩm chức năng")){

                panelNangCao.setVisible(true);

                cmb_congDung.addItem("Vitamin");
                cmb_congDung.addItem("Tăng đề kháng");
                cmb_congDung.addItem("Bổ sung dinh dưỡng");

                cmb_doiTuong.addItem("Người lớn");
                cmb_doiTuong.addItem("Người cao tuổi");
                cmb_doiTuong.addItem("Trẻ em");
            }

            else if(loai.equals("Vật tư y tế")){

                panelNangCao.setVisible(true);

                cmb_congDung.addItem("Sát khuẩn");
                cmb_congDung.addItem("Bảo vệ");

                cmb_doiTuong.addItem("Nam");
                cmb_doiTuong.addItem("Nữ");
            }

            revalidate();
            repaint();
        });
        // ===== TÌM KIẾM =====
        btn_timKiem.addActionListener(e -> {

            currentList = timKiem();

            page = 1;

            loadDanhSach(currentList);
            loadSoTrang();

        });
        // ===== RESET =====
        btn_thoat.addActionListener(e -> {

            cmb_locTrangThai.setSelectedIndex(0);
            cmb_khoangGia.setSelectedIndex(0);
            cmb_loai.setSelectedIndex(0);

            txt_timKiem.setText("");

            currentList = bus.getAll();

            page = 1;

            loadDanhSach(currentList);
            loadSoTrang();

        });
    }

    // ================= UI =================
    private void initComponents() {

        panel_trangChu = new JPanel(new BorderLayout());

        // ===== TITLE =====
        JLabel lblTrangChu = new JLabel("Trang chủ");
        lblTrangChu.setHorizontalAlignment(SwingConstants.LEFT);
        lblTrangChu.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblTrangChu.setFont(new Font("Arial", Font.BOLD, 24));
        lblTrangChu.setBorder(BorderFactory.createEmptyBorder(10,15,10,10));
        lblTrangChu.setAlignmentX(Component.LEFT_ALIGNMENT);
        // ===== COMPONENT =====
        cmb_locTrangThai = new JComboBox<>();
        cmb_khoangGia = new JComboBox<>();
        cmb_loai = new JComboBox<>();
        cmb_congDung = new JComboBox<>();
        cmb_doiTuong = new JComboBox<>();
        txt_timKiem = new JTextField(15);
        btn_timKiem = new JButton("Tìm kiếm");
        btn_thoat = new JButton("Thoát");

        // ===== FILTER =====
        JPanel panelFilter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFilter.setBorder(BorderFactory.createTitledBorder("Bộ lọc"));

        panelFilter.add(new JLabel("Lọc theo"));
        panelFilter.add(cmb_locTrangThai);

        panelFilter.add(new JLabel("Tìm kiếm"));
        panelFilter.add(txt_timKiem);

        panelFilter.add(new JLabel("Khoảng giá"));
        panelFilter.add(cmb_khoangGia);

        panelFilter.add(new JLabel("Loại"));
        panelFilter.add(cmb_loai);

// ===== LỌC NÂNG CAO =====
        panelNangCao.add(new JLabel("Công dụng"));
        panelNangCao.add(cmb_congDung);

        panelNangCao.add(new JLabel("Đối tượng"));
        panelNangCao.add(cmb_doiTuong);

        panelFilter.add(panelNangCao);

        panelFilter.add(btn_timKiem);
        panelFilter.add(btn_thoat);

        // ===== HEADER (TITLE + FILTER) =====
        JPanel panelHeader = new JPanel(new BorderLayout());

        panelHeader.add(lblTrangChu, BorderLayout.NORTH);
        panelHeader.add(panelFilter, BorderLayout.CENTER);

        // ===== SẢN PHẨM =====
        sanPham = new JPanel();
        sanPham.setLayout(new GridLayout(0,4,20,20));
        sanPham.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        scroll_sanPham = new JScrollPane(sanPham);

        // ===== PAGE =====
        btnPrev = new JButton("<<");
        btnNext = new JButton(">>");

        JPanel panelPage = new JPanel(new FlowLayout());

        panelPage.add(btnPrev);
        panelPage.add(panelSoTrang);
        panelPage.add(btnNext);

        // ===== ADD =====
        panel_trangChu.add(panelHeader, BorderLayout.NORTH);
        panel_trangChu.add(scroll_sanPham, BorderLayout.CENTER);
        panel_trangChu.add(panelPage, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(panel_trangChu, BorderLayout.CENTER);
    }

    // ================= DATA =================
    private void initData() {

        cmb_locTrangThai.setModel(new DefaultComboBoxModel<>(
                new String[]{"Tất cả", "Lọc theo tên", "Lọc theo mã"}
        ));

        cmb_khoangGia.setModel(new DefaultComboBoxModel<>(
                new String[]{
                        "Tất cả",
                        "Dưới 100.000",
                        "100.000 - 500.000",
                        "Trên 500.000"
                }
        ));

        cmb_loai.setModel(new DefaultComboBoxModel<>(
                new String[]{
                        "Tất cả",
                        "Thuốc",
                        "Thực phẩm chức năng",
                        "Vật tư y tế"
                }
        ));

        cmb_congDung.setModel(new DefaultComboBoxModel<>(
                new String[]{
                        "Giảm đau",
                        "Kháng sinh",
                        "Vitamin",
                        "Tăng đề kháng"
                }
        ));

        cmb_doiTuong.setModel(new DefaultComboBoxModel<>(
                new String[]{
                        "Người lớn",
                        "Trẻ em",
                        "Phụ nữ mang thai",
                        "Người cao tuổi"
                }
        ));

    }

    // ================= LOAD CARD =================
    private void loadDanhSach(ArrayList<SanPham_DTO> list) {

        sanPham.removeAll();

        int start = (page - 1) * limit;
        int end = Math.min(start + limit, list.size());

        for (int i = start; i < end; i++) {

            SanPham_DTO sp = list.get(i);

            double giaBan = bus.getGiaBan(sp.getMaSP());

            SanPhamCard_GUI card = new SanPhamCard_GUI(sp, giaBan);

            sanPham.add(card);
        }

        sanPham.revalidate();
        sanPham.repaint();
    }
    private void loadSoTrang() {

        panelSoTrang.removeAll();

        int maxPage = (int)Math.ceil((double) currentList.size() / limit);

        for(int i = 1; i <= maxPage; i++){

            int pageNumber = i;

            JButton btnPage = new JButton(String.valueOf(i));

            btnPage.addActionListener(e -> {

                page = pageNumber;
                loadDanhSach(currentList);
                loadSoTrang();

            });

            panelSoTrang.add(btnPage);
        }

        panelSoTrang.revalidate();
        panelSoTrang.repaint();
    }
    private ArrayList<SanPham_DTO> timKiem(){

        String keyword = txt_timKiem.getText().trim();
        String locTheo = cmb_locTrangThai.getSelectedItem().toString();
        String khoangGia = cmb_khoangGia.getSelectedItem().toString();
        String loai = cmb_loai.getSelectedItem().toString();

        return bus.timKiem(keyword, locTheo, khoangGia, loai);

    }
    private void hienThiGoiY(ArrayList<SanPham_DTO> list){

        popupGoiY.setVisible(false);
        popupGoiY.removeAll();

        if(list.isEmpty()) return;

        JPanel panelNoiDung = new JPanel();
        panelNoiDung.setLayout(new BoxLayout(panelNoiDung, BoxLayout.Y_AXIS));
        panelNoiDung.setBackground(Color.WHITE);

        for(SanPham_DTO sp : list){

            JButton btnItem = new JButton(sp.getMaSP() + " - " + sp.getTenSP());

            btnItem.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnItem.setHorizontalAlignment(SwingConstants.LEFT);
            btnItem.setMargin(new Insets(2,10,2,10));

            btnItem.setBorderPainted(false);
            btnItem.setContentAreaFilled(false);
            btnItem.setFocusPainted(false);

            btnItem.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btnItem.setPreferredSize(new Dimension(txt_timKiem.getWidth(),30));
            btnItem.setMaximumSize(new Dimension(txt_timKiem.getWidth(),30));

            btnItem.addActionListener(e -> {

                txt_timKiem.setText(sp.getMaSP());
                popupGoiY.setVisible(false);

            });

            btnItem.addMouseListener(new MouseAdapter(){

                public void mouseEntered(MouseEvent evt){
                    btnItem.setContentAreaFilled(true);
                    btnItem.setBackground(new Color(240,240,240));
                }

                public void mouseExited(MouseEvent evt){
                    btnItem.setContentAreaFilled(false);
                }

            });

            panelNoiDung.add(btnItem);
        }

        JScrollPane scrollPane = new JScrollPane(panelNoiDung);

        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0,0));

        int height = Math.min(list.size() * 30,150);

        scrollPane.setPreferredSize(new Dimension(txt_timKiem.getWidth(),height));

        popupGoiY.setFocusable(false);

        popupGoiY.add(scrollPane);

        popupGoiY.show(txt_timKiem,0,txt_timKiem.getHeight());

        scrollPane.setFocusable(false);
    }
}