package org.example;

// Autorzy:
// Krzysztof Dunajski - 254744
// Kornel Komorowski - 254783
import javax.swing.*;

public class App {
    // Funkcja inicjalizująca aplikację - logika poszczególnych elementów została zawarta w GUIWindow.
    static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setContentPane(new GUIWindow().mainWindow);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.setVisible(true);
    }
}
