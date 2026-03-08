package gui.trangChuDatHang;

import gui.trangChuDatHang.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import dto.*;
import bus.*;

public class TrangChu_GUI extends JPanel {
    private JTextField txtSearch;
    private JComboBox<String> cboLoai;

    private JPanel productPanel;
    private JPanel paginationPanel;

    private ArrayList<SanPham_DTO> allProducts;
    private ArrayList<SanPham_DTO> filteredProducts;

    private int currentPage = 1;
    private int pageSize = 30;
    private JPopupMenu popupGoiY = new JPopupMenu();
    private boolean isSearching = false;

    public TrangChu_GUI() {
        setLayout(new BorderLayout());

        add(createFilterPanel(), BorderLayout.NORTH);
        add(createProductArea(), BorderLayout.CENTER);
        add(createPaginationPanel(), BorderLayout.SOUTH);

        loadData();
    }

    private JPanel createFilterPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createTitledBorder("Bộ lọc"));

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));

        txtSearch = new JTextField(18);

        cboLoai = new JComboBox<>();
        taoCMBLocDangMuc();

        JButton btnSearch = new JButton("Tìm kiếm");

        panel.add(new JLabel("Tìm kiếm"));
        panel.add(txtSearch);

        panel.add(new JLabel("Loại"));
        panel.add(cboLoai);

        panel.add(btnSearch);

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(!txtSearch.getText().trim().isEmpty())
                    goiYSanPham();
                else
                    popupGoiY.setVisible(false);
            }
        });
        btnSearch.addActionListener(e -> {
            if(!isSearching){
                locSanPham();
                btnSearch.setText("Làm mới");
                isSearching = true;
            }else{
                txtSearch.setText("");
                cboLoai.setSelectedIndex(0);

                filteredProducts = new ArrayList<>(allProducts);

                currentPage = 1;
                loadPage();

                btnSearch.setText("Tìm kiếm");
                isSearching = false;
            }

        });
        wrapper.add(panel, BorderLayout.CENTER);

        return wrapper;
    }

    private JScrollPane createProductArea() {

        productPanel = new JPanel();

        productPanel.setLayout(new WrapLayout(FlowLayout.LEFT,20,20));
        productPanel.setBorder(
                BorderFactory.createEmptyBorder(10,5,10,5)
        );

        JScrollPane scroll = new JScrollPane(productPanel);

        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        return scroll;
    }

    private JPanel createPaginationPanel() {
        paginationPanel = new JPanel();

        return paginationPanel;
    }

    private void loadData() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                SanPham_BUS bus = SanPham_BUS.getInstance();
                allProducts = bus.getAll();
                filteredProducts = new ArrayList<>(allProducts);
                return null;
            }
            @Override
            protected void done() {
                loadPage();
            }
        };
        worker.execute();
    }

    private void loadPage() {
        productPanel.removeAll();

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, filteredProducts.size());

        for (int i = start; i < end; i++) {
            ProductCard card = new ProductCard(filteredProducts.get(i));
//            card.setPreferredSize(new Dimension(230,260));
//            card.setMaximumSize(new Dimension(230,260));
//            card.setMinimumSize(new Dimension(230,260));

            productPanel.add(card);
        }
        productPanel.revalidate();
        productPanel.repaint();
        buildPagination();
    }

    private void buildPagination() {
        paginationPanel.removeAll();

        int totalPages = (int)Math.ceil((double)filteredProducts.size()/pageSize);
        JButton prev = new JButton("<<");
        prev.addActionListener(e -> {
            if(currentPage > 1){
                currentPage--;
                loadPage();
            }
        });

        paginationPanel.add(prev);

        int start = Math.max(1, currentPage - 2);
        int end = Math.min(totalPages, currentPage + 2);

        for(int i = start; i <= end; i++){
            int page = i;
            JButton btn = new JButton(String.valueOf(i));
            if(i == currentPage){
                btn.setBackground(Color.LIGHT_GRAY);
            }

            btn.addActionListener(e -> {
                currentPage = page;
                loadPage();
            });

            paginationPanel.add(btn);
        }

        JButton next = new JButton(">>");
        next.addActionListener(e -> {
            if(currentPage < totalPages){
                currentPage++;
                loadPage();
            }
        });

        paginationPanel.add(next);

        paginationPanel.revalidate();
        paginationPanel.repaint();
    }
    private void taoCMBLocDangMuc() {
        cboLoai.removeAllItems();
        cboLoai.addItem("-- Chọn danh muc --");
        ArrayList<DanhMuc_DTO> listDanhMuc = DanhMuc_BUS.getInstance().getAll();
        for(DanhMuc_DTO dm : listDanhMuc) {
            if(dm.getTrangThai() == 1)
            cboLoai.addItem(dm.getTenDM());
        }
    }
    private void locSanPham(){

        String key = txtSearch.getText().trim().toLowerCase();
        String loai = (String)cboLoai.getSelectedItem();

        filteredProducts.clear();

        for(SanPham_DTO sp : allProducts){

            boolean matchTen = key.isEmpty() ||
                    sp.getTenSP().toLowerCase().contains(key);

            boolean matchLoai = loai.equals("-- Chọn danh muc --") ||
                    DanhMuc_BUS.getInstance().getById(sp.getMaDM()).getTenDM().equals(loai);
            if(matchTen && matchLoai){
                filteredProducts.add(sp);
            }
        }

        currentPage = 1;
        loadPage();
    }
    private void hienThiGoiYSanPham(ArrayList<SanPham_DTO> list){

        popupGoiY.setVisible(false);
        popupGoiY.removeAll();

        if(list.isEmpty()) return;

        JPanel panelNoiDung = new JPanel();
        panelNoiDung.setLayout(new BoxLayout(panelNoiDung,BoxLayout.Y_AXIS));
        panelNoiDung.setBackground(Color.WHITE);

        for(SanPham_DTO sp : list){

            double giaNhap = LoHang_BUS.getInstance().getGiaNhapThapNhatByMaSP(sp.getMaSP());
            double giaBan = SanPham_BUS.getInstance().tinhGiaBan(sp.getMaSP(), giaNhap);

            String text = "<html><b>" + sp.getTenSP() + "</b> - "
                    + String.format("%,.0f đ", giaBan) + "</html>";

            JButton btnItem = new JButton(text);

            btnItem.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnItem.setHorizontalAlignment(SwingConstants.LEFT);
            btnItem.setMargin(new Insets(2,10,2,10));
            btnItem.setBorderPainted(false);
            btnItem.setContentAreaFilled(false);
            btnItem.setFocusPainted(false);
            btnItem.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btnItem.setPreferredSize(new Dimension(txtSearch.getWidth(),30));
            btnItem.setMaximumSize(new Dimension(txtSearch.getWidth(),30));

            btnItem.addActionListener(e -> {

                txtSearch.setText(sp.getTenSP());
                cboLoai.setSelectedIndex(0);
                popupGoiY.setVisible(false);
                isSearching = true;
                locSanPham();
            });

            btnItem.addMouseListener(new java.awt.event.MouseAdapter(){
                public void mouseEntered(java.awt.event.MouseEvent evt){
                    btnItem.setContentAreaFilled(true);
                    btnItem.setBackground(new Color(240,240,240));
                }

                public void mouseExited(java.awt.event.MouseEvent evt){
                    btnItem.setContentAreaFilled(false);
                }
            });

            panelNoiDung.add(btnItem);
        }

        JScrollPane scrollPane = new JScrollPane(panelNoiDung);
        scrollPane.setBorder(null);

        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0,0));

        int height = Math.min(list.size()*30,150);
        scrollPane.setPreferredSize(new Dimension(txtSearch.getWidth(),height));

        popupGoiY.setFocusable(false);
        popupGoiY.add(scrollPane);

        popupGoiY.show(txtSearch,0,txtSearch.getHeight());

        scrollPane.setFocusable(false);
    }
    private void goiYSanPham(){

        if(allProducts == null) return;

        String key = txtSearch.getText().trim().toLowerCase();

        if(key.isEmpty()){
            popupGoiY.setVisible(false);
            return;
        }

        ArrayList<SanPham_DTO> list = new ArrayList<>();

        for(SanPham_DTO sp : allProducts){
            if(sp.getTenSP().toLowerCase().contains(key)){
                list.add(sp);
            }
        }

        hienThiGoiYSanPham(list);
    }

}