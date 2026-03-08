package gui.trangChuDatHang;

import gui.trangChuDatHang.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
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
        cboLoai.addItem("Tất cả");
        cboLoai.addItem("Thuốc");
        cboLoai.addItem("Vitamin");
        cboLoai.addItem("Dụng cụ");

        JButton btnSearch = new JButton("Tìm kiếm");
        JButton btnExit = new JButton("Thoát");

        panel.add(new JLabel("Tìm kiếm"));
        panel.add(txtSearch);

        panel.add(new JLabel("Loại"));
        panel.add(cboLoai);

        panel.add(btnSearch);
        panel.add(btnExit);

        btnExit.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if(window != null){
                window.dispose();
            }
        });

        wrapper.add(panel, BorderLayout.CENTER);

        return wrapper;
    }

    private JScrollPane createProductArea() {

        productPanel = new JPanel();

        productPanel.setLayout(new GridLayout(0,2,20,20));
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
}