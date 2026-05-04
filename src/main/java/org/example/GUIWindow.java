package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

// Autorzy:
//
// Krzysztof Dunajski - 254744
// Kornel Komorowski - 254783

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
        // Generowanie klucza w oparciu o podany tekst (lub jego brak)
        generateKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = uniqueKeyPanel.getText();

                // jeśli input jest pusty, wrzuć tam losową wartość double od 0 do 1
                if(inputText.equals("")) {
                    inputText= String.valueOf(Math.random());

                }
                byte[] byteText = inputText.getBytes();
                byte[] keyBytes = new byte[24];
                // kopiowanie danych do tablicy keyBytes
                for (int i = 0; i < byteText.length && i < 24; i++) {
                    keyBytes[i] = byteText[i];
                }

                for (int i = byteText.length; i < 24; i++) {
                    keyBytes[i] = (byte) (byteText[i % byteText.length] ^ (i * 31));
                }
                //podział na podklucze
                long key1 = desx.bytesToLong(Arrays.copyOfRange(keyBytes, 0, 8));
                long key2 = desx.bytesToLong(Arrays.copyOfRange(keyBytes, 8, 16));
                long key3 = desx.bytesToLong(Arrays.copyOfRange(keyBytes, 16, 24));



                keyOutputPanel.setText(Long.toHexString(key1).toUpperCase()+Long.toHexString(key2).toUpperCase()+Long.toHexString(key3).toUpperCase());

                if (keyOutputPanel.getText().length()==48){
                    logsLabel.setText("Key generated.");
                }
                else {logsLabel.setText("Keygen failed.");}
            }
        });
        // Szyfrowanie tekstu
        cipherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(keyOutputPanel.getText().isEmpty()){
                    logsLabel.setText("Błąd: Wygeneruj najpierw klucz!");
                    return;
                }

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
        // Deszyfrowywanie tekstu
        decipherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(keyOutputPanel.getText().isEmpty()){
                    logsLabel.setText("Błąd: Wygeneruj umieść klucz");
                    return;
                }
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
        // Szyfrowanie pliku binarnego
        fileCipherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String fullKey = keyOutputPanel.getText();
                    if (fullKey.length() < 48) {
                        logsLabel.setText("Błąd: Wygeneruj najpierw klucz!");
                        return;
                    }
                    long key1 = desx.stringToLong(fullKey.substring(0, 16));
                    long key2 = desx.stringToLong(fullKey.substring(16, 32));
                    long key3 = desx.stringToLong(fullKey.substring(32, 48));

                    byte[] inputBytes = Files.readAllBytes(Path.of(inputFileTextField.getText()));
                    long[] message = desx.bytesToLongArray(inputBytes);
                    long[] output = new long[message.length];

                    long[] subkeys = desx.generateSubkeys(key2);
                    for (int i = 0; i < message.length; i++) {
                        output[i] = desx.DESXencrypt(message[i], subkeys, key1, key3);
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
                        logsLabel.setText("Zaszyfrowano i zapisano: " + targetFile.getName());
                    }

                } catch (Exception ex) {
                    logsLabel.setText("Błąd: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        // Deszyfrowywanie pliku binarnego
        fileDecipherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String fullKey = keyOutputPanel.getText();
                    if (fullKey.length() < 48) {
                        logsLabel.setText("Błąd: Wygeneruj najpierw klucz!");
                        return;
                    }

                    long key1 = desx.stringToLong(fullKey.substring(0, 16));
                    long key2 = desx.stringToLong(fullKey.substring(16, 32));
                    long key3 = desx.stringToLong(fullKey.substring(32, 48));

                    Path inputPath = Path.of(inputFileTextField.getText());
                    byte[] inputBytes = Files.readAllBytes(inputPath);

                    long[] message = desx.bytesToLongArray(inputBytes);
                    long[] output = new long[message.length];
                    long[] subkeys = desx.generateSubkeys(key2);

                    for(int i = 0; i < message.length; i++) {
                        output[i] = desx.DESXdecrypt(message[i], subkeys, key1, key3);
                    }

                    byte[] decryptedBytes = new byte[output.length * 8];
                    for(int i = 0; i < output.length; i++) {
                        byte[] tempBytes = desx.longToBytes(output[i]);
                        System.arraycopy(tempBytes, 0, decryptedBytes, i * 8, 8);
                    }

                    int realLength = decryptedBytes.length;
                    while (realLength > 0 && decryptedBytes[realLength - 1] == 0) {
                        realLength--;
                    }

                    byte[] finalBytes = new byte[realLength];
                    System.arraycopy(decryptedBytes, 0, finalBytes, 0, realLength);

                    JFileChooser fileChooser = new JFileChooser();
                    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        Files.write(fileChooser.getSelectedFile().toPath(), finalBytes);
                        logsLabel.setText("Odszyfrowano PNG poprawnie.");
                    }

                } catch (Exception ex) {
                    logsLabel.setText("Błąd: " + ex.getMessage());
                }
            }
        });
        // Wyszukiwanie pliku
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
