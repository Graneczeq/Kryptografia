package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

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

                long key1 = desx.bytesToLong(Arrays.copyOfRange(keyBytes, 0, 8));
                long key2 = desx.bytesToLong(Arrays.copyOfRange(keyBytes, 9, 16));
                long key3 = desx.bytesToLong(Arrays.copyOfRange(keyBytes, 17, 24));



                keyOutputPanel.setText(Long.toHexString(key1).toUpperCase()+Long.toHexString(key2).toUpperCase()+Long.toHexString(key3).toUpperCase());

                if (keyOutputPanel.getText().length()==48){
                    logsLabel.setText("Key generated.");
                }
                else {logsLabel.setText("Keygen failed.");}
            }
        });

        cipherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                long key1 = desx.stringToLong(keyOutputPanel.getText().substring(0,16));
                long key2 =desx.stringToLong(keyOutputPanel.getText().substring(16,32));
                long key3 = desx.stringToLong(keyOutputPanel.getText().substring(32,48));

                long[] message = desx.stringToLongArray(inputTextField.getText());
                long[] output = new long[message.length];

                for(int i = 0; i < output.length; i++) {
                    long decrypted = desx.DESXencrypt(message[i],desx.generateSubkeys(key2),key1,key3);
                    output[i]=decrypted;
                }
                String hexout= "";
                for(int i = 0; i < output.length; i++) {
                    hexout = hexout + Long.toHexString(output[i]);
                }
                outputTextField.setText(hexout);
                logsLabel.setText("Text message has been encrypted.");

            }
        });
        decipherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long key1 = desx.stringToLong(keyOutputPanel.getText().substring(0,16));
                long key2 = desx.stringToLong(keyOutputPanel.getText().substring(16,32));
                long key3 = desx.stringToLong(keyOutputPanel.getText().substring(32,48));
                long[] message = desx.HexStringToLongArray(inputTextField.getText());
                long[] output = new long[message.length];
                for(int i = 0; i < message.length; i++) {
                    long decrypted = desx.DESXdecrypt(message[i],desx.generateSubkeys(key2),key1,key3);
                    output[i]=decrypted;
                }

                outputTextField.setText(desx.longArrayToString(output));
            }
        });
        fileCipherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    byte[] inputBytes = Files.readAllBytes(Path.of(inputFileTextField.getText()));
                    long key1 = desx.stringToLong(keyOutputPanel.getText().substring(0,16));
                    long key2 = desx.stringToLong(keyOutputPanel.getText().substring(16,32));
                    long key3 = desx.stringToLong(keyOutputPanel.getText().substring(32,48));
                    long[] message = desx.bytesToLongArray(inputBytes);
                    long[] output = new long[message.length];
                    for(int i = 0; i < message.length; i++) {
                        long encrypted = desx.DESXencrypt(message[i],desx.generateSubkeys(key2),key1,key3);
                        output[i]=encrypted;
                    }
                    byte[] encryptedBytes = new byte[output.length*8];
                    for(int i = 0; i < output.length; i++) {
                        byte[] byteRow = desx.longToBytes(output[i]);
                        for(int j = 0; j < 8; j++) {
                            encryptedBytes[j] = byteRow[j];
                        }
                    }
                    JFileChooser fileSearch = new JFileChooser();
                    int selected = fileSearch.showSaveDialog(null);
                    if (selected == JFileChooser.APPROVE_OPTION) {
                        outputTextField.setText(fileSearch.getSelectedFile().getAbsolutePath());
                        Files.write(Path.of(outputFileTextField.getText()), encryptedBytes);
                        logsLabel.setText("Saved in" + fileSearch.getSelectedFile().getAbsolutePath());
                    }
                    //TODO: ZAPIS JESZCZE NIE DZIAŁA




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
                int selected = fileSearch.showOpenDialog(null);
                if (selected == JFileChooser.APPROVE_OPTION) {
                    inputFileTextField.setText(fileSearch.getSelectedFile().getAbsolutePath());
                }

            }
        });
    }
}
