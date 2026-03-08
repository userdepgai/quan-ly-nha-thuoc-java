package gui;

import javax.swing.*;
import java.awt.*;

public class DashBoard_GUI extends JPanel {
    public DashBoard_GUI(){
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));

        // ===== TIÊU ĐỀ =====
        JLabel title = new JLabel("Dashboard", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // ===== PANEL TRUNG TÂM =====
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(15, 15));
        centerPanel.setOpaque(false);
        add(centerPanel, BorderLayout.CENTER);

        // ===== CARD THỐNG KÊ =====
        JPanel cardPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        cardPanel.setOpaque(false);

        cardPanel.add(createStatCard("👥", "Khách hàng", "1,245"));
        cardPanel.add(createStatCard("📦", "Sản phẩm", "356"));
        cardPanel.add(createStatCard("🧾", "Hóa đơn", "892"));
        cardPanel.add(createStatCard("💰", "Doanh thu", "1.2 tỷ"));

        centerPanel.add(cardPanel, BorderLayout.NORTH);

        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createTitledBorder("Doanh thu theo tháng"));


        centerPanel.add(chartPanel, BorderLayout.CENTER);

        chartPanel.add(new ModernBarChart(), BorderLayout.CENTER);
        centerPanel.add(chartPanel, BorderLayout.CENTER);
    }
    private JPanel createStatCard(String icon, String title, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(Color.GRAY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        top.setOpaque(false);
        top.add(iconLabel);
        top.add(titleLabel);

        panel.add(top, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
    }
}
