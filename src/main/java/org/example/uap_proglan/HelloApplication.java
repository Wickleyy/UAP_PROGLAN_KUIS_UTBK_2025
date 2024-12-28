package org.example.uap_proglan;

import javax.swing.*;

public class HelloApplication {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UIManager uiManager = new UIManager();
            uiManager.showMainMenu();
        });
    }
}