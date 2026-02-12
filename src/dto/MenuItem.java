package dto;

import javax.swing.*;

public class MenuItem {
    public String title;
    public String cardName;
    public Icon icon;
    public boolean isGroup;

    public MenuItem(String title, String cardName, Icon icon, boolean isGroup) {
        this.title = title;
        this.cardName = cardName;
        this.icon = icon;
        this.isGroup = isGroup;
    }

    @Override
    public String toString() {
        return title;
    }
}
