package com.example;

import java.awt.*;
import java.awt.TrayIcon.MessageType;


public class NotificationSender {

    TrayIcon trayIcon;
    public NotificationSender(String tooltip, String iconTooltip, String imagePath){
        if(!SystemTray.isSupported()) return;

        SystemTray tray = SystemTray.getSystemTray();

        Image image = Toolkit.getDefaultToolkit().createImage(imagePath);

        this.trayIcon = new TrayIcon(image, tooltip);

        this.trayIcon.setImageAutoSize(true);

        this.trayIcon.setToolTip(iconTooltip);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void displayTray(String caption, String text){
        this.trayIcon.displayMessage(caption, text, MessageType.INFO);
    }
}
