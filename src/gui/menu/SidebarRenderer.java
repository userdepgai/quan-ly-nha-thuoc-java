package gui.menu;

import javax.swing.*;
import java.awt.*;
import dto.MenuItem;
class SidebarRenderer extends JLabel implements ListCellRenderer<MenuItem> {

    public SidebarRenderer() {
        setOpaque(true);
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends MenuItem> list,
            MenuItem value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        setText(value.title);
        setIcon(value.icon);
        setIconTextGap(12);

        if (value.isGroup) {
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setForeground(Color.GRAY);
            setBackground(new Color(245, 245, 245));
            setIcon(null);
        } else {
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(Color.BLACK);
            setBackground(isSelected ? new Color(220, 235, 250) : Color.WHITE);
        }

        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));
        return this;
    }
}
