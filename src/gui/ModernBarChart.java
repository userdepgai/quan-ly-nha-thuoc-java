package gui;

import javax.swing.*;
import java.awt.*;

public class ModernBarChart extends JPanel {

    private int[] values = {120, 200, 150, 300, 280, 350, 320, 410, 380, 420, 390, 450};
    private String[] labels = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    // Add a title variable
    private String title = "Doanh thu theo tháng";

    public ModernBarChart() {
        setPreferredSize(new Dimension(750, 220));
        setBackground(Color.WHITE);
    }

    // Setter to update the chart dynamically
    public void setChartData(String[] newLabels, int[] newValues) {
        this.labels = newLabels;
        this.values = newValues;
        repaint(); // Force the panel to redraw with new data
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        // Enable anti-aliasing for smooth lines and text
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Padding definitions
        int paddingBottom = 40;
        int paddingTop = 40;
        int paddingLeft = 45;
        int paddingRight = 20;

        int chartHeight = height - paddingTop - paddingBottom;
        int chartWidth = width - paddingLeft - paddingRight;

        // Calculate bar width based on available chart width, not total width
        int barWidth = chartWidth / values.length;

        // Find max value for scaling
        int maxValue = 0;
        for (int v : values) {
            maxValue = Math.max(maxValue, v);
        }

        // Prevent division by zero if all values are 0
        if (maxValue == 0) maxValue = 1;

        // 1. Draw horizontal grid lines
        g2.setColor(new Color(230, 230, 230));
        for (int i = 0; i <= 5; i++) { // Changed to <= 5 to draw the top line too
            int y = height - paddingBottom - (i * chartHeight / 5);
            g2.drawLine(paddingLeft, y, width - paddingRight, y);
        }

        // 2. Draw bars and labels
        for (int i = 0; i < values.length; i++) {
            int barHeight = (int) ((double) values[i] / maxValue * chartHeight);

            int x = paddingLeft + (i * barWidth);
            int y = height - paddingBottom - barHeight;

            // Gradient color for bars
            GradientPaint gp = new GradientPaint(
                    x, y, new Color(72, 149, 239),
                    x, y + barHeight, new Color(39, 102, 199)
            );

            g2.setPaint(gp);
            // Draw bar with a small gap (-15) and rounded top corners
            g2.fillRoundRect(x, y, barWidth - 15, barHeight, 10, 10);

            // To make only the top corners rounded, we can draw a sharp rectangle over the bottom
            g2.fillRect(x, y + 10, barWidth - 15, barHeight - 10);

            // Draw X-axis labels
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            // Center text slightly under the bar
            g2.drawString(labels[i], x + (barWidth - 15)/2 - 10, height - 15);
        }

        // 3. Draw Title (Fixed)
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.setColor(Color.BLACK);
        g2.drawString(title, paddingLeft, 25);
    }
}