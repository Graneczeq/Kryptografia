package org.example;


import javax.swing.*;

public class App {

    static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setContentPane(new GUIWindow().mainWindow);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.setVisible(true);
    }
}
