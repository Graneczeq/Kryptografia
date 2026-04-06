
package org.example;

import java.util.ArrayList;
import java.util.List;

public class Window {
    static void main(String[] args) {

        String input = "tekstsmie";

        DESX desx = new DESX();

        List<byte[]> data = new ArrayList<byte[]>();

        // Przykładowy blok 8-bajtowy
        byte[] test = new byte[]{0x01, 0x23, 0x45, 0x67, (byte)0x89, (byte)0xAB, (byte)0xCD, (byte)0xEF};

        byte[] res = desx.initialPermutation(test);

        System.out.println("Oryginał: " + desx.bytesToHex(test));
        System.out.println("Po IP:    " + desx.bytesToHex(res));




        }
    }

