package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DESX {
    public DESX() {
    }

    final int[] initPermTable= {
            58,	50,	42,	34,	26,	18,	10,	2,
            60,	52,	44,	36,	28,	20,	12,	4,
            62,	54,	46,	38,	30,	22,	14,	6,
            64,	56,	48,	40,	32,	24,	16,	8,
            57,	49,	41,	33,	25,	17,	9,	1,
            59,	51,	43,	35,	27,	19,	11,	3,
            61,	53,	45,	37,	29,	21,	13,	5,
            63,	55,	47,	39,	31,	23,	15,	7};



    public List<byte []> formatInput(String input){
        List<byte []> output = new ArrayList<byte[]>();
        byte[] format = input.getBytes();
         for(int i = 0; i < input.length(); i+= 8){
            byte [] temp = Arrays.copyOfRange(format, i, Math.min(format.length, i+8));
            output.add(temp);
        }
        if(output.get(output.size()-1).length != 8){
            addPadding(output.get(output.size()-1));
        }
        return output;
    }

    public byte[] initialPermutation(byte[] input){
        byte[] output = new byte[8];
        for(int i = 0; i < 64; i++){
            int pos = initPermTable[i] - 1;
            int bitIndex = 7- (pos % 8);
            int bit = (input[pos/8] >> bitIndex) & 1;
            if(bit == 1){
                int out = i / 8;
                int outBit = 7 - (i % 8);
                output[out] |= (1 << outBit);
            }
        }
        return output;
    }
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }




    private byte[] addPadding(byte[] input){
        int overflow = 8-(input.length % 8);
        byte[] output = Arrays.copyOf(input, input.length + overflow);
        return output;
    }
    byte[] XORInputs(byte[] a, byte[] b){
        if(a.length!=b.length){
            throw new IllegalArgumentException();
        }
        byte[] result = new byte[a.length];
        for(int i=0;i<a.length;i++){
            result[i]= (byte) (a[i] ^ b[i]);
        }
        return result;

    }





}

