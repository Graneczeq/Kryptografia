package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.security.MessageDigest;

// Autorzy:
//
// Krzysztof Dunajski - 254744
// Kornel Komorowski - 254783

public class GUIWindow extends JFrame {
    private JPanel Logs;
    private JPanel MainPanel;
    private JTextField uniqueKeyPanel;
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

    private long[] prepareKeysFromPassphrase(String passphrase) {
        DESX desx = new DESX();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(passphrase.getBytes());

            long k1 = desx.bytesToLong(Arrays.copyOfRange(hash, 0, 8));
            long k2 = desx.bytesToLong(Arrays.copyOfRange(hash, 8, 16));
            long k3 = desx.bytesToLong(Arrays.copyOfRange(hash, 16, 24));

            return new long[]{k1, k2, k3};
        } catch (Exception e) {
            return new long[]{0, 0, 0};
        }
    }
    public GUIWindow() {
        DESX desx = new DESX();
        cipherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pass = uniqueKeyPanel.getText();
                if(pass.isEmpty()) {
                    logsLabel.setText("Błąd: Wpisz klucz!");
                    return;
                }

                long[] keys = prepareKeysFromPassphrase(pass);
                long[] message = desx.stringToLongArray(inputTextField.getText());
                long[] output = new long[message.length];
                long[] subkeys = desx.generateSubkeys(keys[1]);

                for(int i = 0; i < message.length; i++) {
                    output[i] = desx.DESXencrypt(message[i], subkeys, keys[0], keys[2]);
                }

                StringBuilder hexout = new StringBuilder();
                for(long l : output) {
                    hexout.append(String.format("%016x", l));
                }
                outputTextField.setText(hexout.toString());
                logsLabel.setText("Zaszyfrowano.");
            }
        });

        decipherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    String pass = uniqueKeyPanel.getText();
                    if(pass.isEmpty()) {
                        logsLabel.setText("Błąd: Wpisz klucz!");
                        return;
                    }

                    long[] keys = prepareKeysFromPassphrase(pass);
                    String hexInput = inputTextField.getText().trim();
                    long[] message = desx.HexStringToLongArray(hexInput);
                    long[] output = new long[message.length];
                    long[] subkeys = desx.generateSubkeys(keys[1]);

                    for(int i = 0; i < message.length; i++) {
                        output[i] = desx.DESXdecrypt(message[i], subkeys, keys[0], keys[2]);
                    }

                    outputTextField.setText(desx.longArrayToString(output));
                    logsLabel.setText("Odszyfrowywanie zakończone.");
            }
        });

        fileCipherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String pass = uniqueKeyPanel.getText();
                    if (pass.isEmpty()) {
                        logsLabel.setText("Błąd: Wpisz klucz!");
                        return;
                    }

                    long[] keys = prepareKeysFromPassphrase(pass);
                    long[] subkeys = desx.generateSubkeys(keys[1]);
                    byte[] inputBytes = Files.readAllBytes(Path.of(inputFileTextField.getText()));
                    long[] message = desx.bytesToLongArray(inputBytes);
                    long[] output = new long[message.length];

                    for (int i = 0; i < message.length; i++) {
                        output[i] = desx.DESXencrypt(message[i], subkeys, keys[0], keys[2]);
                    }

                    byte[] encryptedBytes = new byte[output.length * 8];
                    for (int i = 0; i < output.length; i++) {
                        byte[] tempBytes = desx.longToBytes(output[i]);
                        System.arraycopy(tempBytes, 0, encryptedBytes, i * 8, 8);
                    }

                    JFileChooser fileChooser = new JFileChooser();
                    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        File targetFile = fileChooser.getSelectedFile();
                        Files.write(targetFile.toPath(), encryptedBytes);
                        outputFileTextField.setText(targetFile.getAbsolutePath());
                        logsLabel.setText("Plik został zaszyfrowany.");
                    }

                } catch (Exception ex) {
                    logsLabel.setText("Błąd szyfrowania pliku: " + ex.getMessage());
                }
            }
        });

        fileDecipherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String pass = uniqueKeyPanel.getText();
                    if (pass.isEmpty()) {
                        logsLabel.setText("Błąd: Wpisz klucz!");
                        return;
                    }

                    long[] keys = prepareKeysFromPassphrase(pass);
                    long[] subkeys = desx.generateSubkeys(keys[1]);
                    Path inputPath = Path.of(inputFileTextField.getText());
                    byte[] inputBytes = Files.readAllBytes(inputPath);
                    long[] message = desx.bytesToLongArrayNoPadding(inputBytes);
                    long[] output = new long[message.length];

                    for(int i = 0; i < message.length; i++) {
                        output[i] = desx.DESXdecrypt(message[i], subkeys, keys[0], keys[2]);
                    }

                    byte[] decryptedBytes = new byte[output.length * 8];
                    for(int i = 0; i < output.length; i++) {
                        byte[] tempBytes = desx.longToBytes(output[i]);
                        System.arraycopy(tempBytes, 0, decryptedBytes, i * 8, 8);
                    }

                    int paddingLen = decryptedBytes[decryptedBytes.length - 1] & 0xFF;
                    byte[] finalData;

                    if (paddingLen >= 1 && paddingLen <= 8) {
                        int realLength = decryptedBytes.length - paddingLen;
                        finalData = new byte[realLength];
                        System.arraycopy(decryptedBytes, 0, finalData, 0, realLength);
                        logsLabel.setText("Plik odszyfrowany.");
                    } else {
                        finalData = decryptedBytes;
                    }

                    JFileChooser fileChooser = new JFileChooser();
                    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        Files.write(fileChooser.getSelectedFile().toPath(), finalData);
                    }

                } catch (Exception ex) {
                    logsLabel.setText("Błąd deszyfrowania: " + ex.getMessage());
                }
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
