package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class GUIWindow extends JFrame {
    private JPanel Logs;
    private JPanel MainPanel;
    private JPasswordField uniqueKeyPanel;
    private JTextField keyOutputPanel;
    private JButton generateKeyButton;
    private JTabbedPane inputTabPane;
    private JTextField inputTextField;
    private JTextField outputTextField;
    private JButton cipherButton;
    private JButton decipherButton;
    private JTextField inputFileTextField;
    private JTextField outputFileTextField;
    private JButton fileSearchButton;
    private JButton fileCipherButton;
    private JButton fileDecipherButton;
    JPanel mainWindow;
    private JLabel DESXLabel;
    private JPanel keyPanel;
    private JPanel inputPanel;
    private JPanel textInputPanel;
    private JPanel fileInputPanel;
    private JLabel logsLabel;


    public GUIWindow() {
        DESX desx = new DESX();

        generateKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = uniqueKeyPanel.getText();
                byte[] byteText = inputText.getBytes();
                byte[] keyBytes = new byte[24];

                for (int i = 0; i < byteText.length && i < 24; i++) {
                    keyBytes[i] = byteText[i];
                }
                for (int i = byteText.length; i < 24; i++) {
                    keyBytes[i] = (byte) (byteText[i % byteText.length] ^ (i * 31));
                }

                long key1 = 0,key2 =0,key3 = 0;
                for (int i = 0; i < 8; i++) {
                    key1 = (key1 << 8) | (keyBytes[i] & 0xFF);
                }
                for (int i = 0; i < 8; i++) {
                    key2 = (key2 << 8) | (keyBytes[i+8] & 0xFF);
                }
                for (int i = 0; i < 8; i++) {
                    key3 = (key3 << 8) | (keyBytes[i+16] & 0xFF);
                }


                keyOutputPanel.setText(Long.toHexString(key1).toUpperCase()+Long.toHexString(key2).toUpperCase()+Long.toHexString(key3).toUpperCase());
                logsLabel.setText("Key generated.");
            }
        });

        cipherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                byte[] inputBytes = inputTextField.getText().getBytes();
                byte[] keyBytes = keyOutputPanel.getText().getBytes();
                long inputlong=0;
                long key1 = 0,key2 =0,key3 = 0;
                for (int i = 0; i < 8; i++) {
                    key1 = (key1 << 8) | (keyBytes[i] & 0xFF);
                }
                for (int i = 0; i < 8; i++) {
                    key2 = (key2 << 8) | (keyBytes[i+8] & 0xFF);
                }
                for (int i = 0; i < 8; i++) {
                    key3 = (key3 << 8) | (keyBytes[i+16] & 0xFF);
                }
                for (int i = 0; i < inputBytes.length; i++) {
                    inputlong = (inputlong << 8) | (inputBytes[i] & 0xFF);
                }


                long encrypted = desx.DESXencrypt(inputlong,desx.generateSubkeys(key2),key1,key3);
                outputTextField.setText(Long.toHexString(encrypted));
                logsLabel.setText("Text message has been encrypted.");

            }
        });
        decipherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                byte[] inputBytes = inputTextField.getText().getBytes();
                byte[] keyBytes = keyOutputPanel.getText().getBytes();
                long inputlong=0;
                long key1 = 0,key2 =0,key3 = 0;
                for (int i = 0; i < 8; i++) {
                    key1 = (key1 << 8) | (keyBytes[i] & 0xFF);
                }
                for (int i = 0; i < 8; i++) {
                    key2 = (key2 << 8) | (keyBytes[i+8] & 0xFF);
                }
                for (int i = 0; i < 8; i++) {
                    key3 = (key3 << 8) | (keyBytes[i+16] & 0xFF);
                }
                for (int i = 0; i < inputBytes.length; i++) {
                    inputlong = (inputlong << 8) | (inputBytes[i] & 0xFF);
                }


                long encrypted = desx.DESXdecrypt(inputlong,desx.generateSubkeys(key2),key1,key3);
                outputTextField.setText(Long.toString(encrypted));
            }
        });
        fileCipherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    byte[] inputBytes = Files.readAllBytes(Path.of(inputFileTextField.getText()));
                    byte[] keyBytes = keyOutputPanel.getText().getBytes();
                    long inputlong=0;
                    long key1 = 0,key2 =0,key3 = 0;
                    for (int i = 0; i < 8; i++) {
                        key1 = (key1 << 8) | (keyBytes[i] & 0xFF);
                    }
                    for (int i = 0; i < 8; i++) {
                        key2 = (key2 << 8) | (keyBytes[i+8] & 0xFF);
                    }
                    for (int i = 0; i < 8; i++) {
                        key3 = (key3 << 8) | (keyBytes[i+16] & 0xFF);
                    }
                    for (int i = 0; i < inputBytes.length; i++) {
                        inputlong = (inputlong << 8) | (inputBytes[i] & 0xFF);
                    }


                    long encrypted = desx.DESXencrypt(inputlong,desx.generateSubkeys(key2),key1,key3);

                    // TODO: SZYFROWANIE I ODSZYFROWYWANIE PLIKÓW BINARNYCH

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        fileDecipherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        fileSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileSearch = new JFileChooser();
                int result = fileSearch.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    inputFileTextField.setText(fileSearch.getSelectedFile().getAbsolutePath());
                }

            }
        });
    }
}
