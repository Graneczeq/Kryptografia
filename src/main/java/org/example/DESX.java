package org.example;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DESX {

    static final int[] initPermTable = {
            58,	50,	42,	34,	26,	18,	10,	2,
            60,	52,	44,	36,	28,	20,	12,	4,
            62,	54,	46,	38,	30,	22,	14,	6,
            64,	56,	48,	40,	32,	24,	16,	8,
            57,	49,	41,	33,	25,	17,	9,	1,
            59,	51,	43,	35,	27,	19,	11,	3,
            61,	53,	45,	37,	29,	21,	13,	5,
            63,	55,	47,	39,	31,	23,	15,	7};
    static final int[] inverseInitPermTable = {
            40,	8, 48, 16, 56, 24, 64, 32,
            39,	7, 47, 15, 55, 23, 63, 31,
            38,	6, 46, 14, 54, 22, 62, 30,
            37,	5, 45, 13, 53, 21, 61, 29,
            36,	4, 44, 12, 52, 20, 60, 28,
            35,	3, 43, 11, 51, 19, 59, 27,
            34,	2, 42, 10, 50, 18, 58, 26,
            33,	1, 41, 9, 49, 17, 57, 25
    };
    static final int[] expansionTable = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };
    static final int[] permutationTable = {
        16, 7, 20, 21, 29, 12, 28, 17,
        1, 15, 23, 26, 5, 18, 31, 10,
        2, 8, 24, 14, 32, 27, 3, 9,
        19, 13, 30, 6, 22, 11, 4, 25
    };
    static final int[][][] substitutionBoxes = {
            {
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0,	15,	7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11,3, 14, 10, 0, 6, 13}
            },
            {
                    {15, 1,	8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3,	13,	4, 7, 15, 2, 8,	14,	12,	0, 1, 10, 6, 9,	11,	5},
                    {0,	14,	7, 11, 10, 4, 13, 1, 5,	8, 12, 6, 9, 3,	2, 15},
                    {13, 8,	10,	1, 3, 15, 4, 2,	11,	6, 7, 12, 0, 5,	14,	9}
            },
            {
                    {10, 0,	9, 14, 6, 3, 15, 5,	1, 13, 12, 7, 11, 4, 2,	8},
                    {13, 7,	0, 9, 3, 4,	6, 10, 2, 8, 5,	14,	12,	11,	15,	1},
                    {13, 6,	4, 9, 8, 15, 3,	0, 11, 1, 2, 12, 5,	10,	14,	7},
                    {1,	10,	13,	0, 6, 9, 8,	7, 4, 15, 14, 3, 11, 5,	2, 12}
            },
            {
                    {7,	13,	14,	3, 0, 6, 9,	10,	1, 2, 8, 5,	11,	12,	4, 15},
                    {13, 8,	11,	5, 6, 15, 0, 3,	4, 7, 2, 12, 1,	10,	14,	9},
                    {10, 6,	9, 0, 12, 11, 7, 13, 15, 1,	3, 14, 5, 2, 8,	4},
                    {3,	15,	0, 6, 10, 1, 13, 8,	9, 4, 5, 11, 12, 7,	2, 14}
            },
            {
                    {2,	12,	4, 1, 7, 10, 11, 6,	8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2,	12,	4, 7, 13, 1, 5,	0, 15, 10, 3, 9, 8,	6},
                    {4,	2, 1, 11, 10, 13, 7, 8, 15,	9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12,	7, 1, 14, 2, 13, 6, 15,	0, 9, 10, 4, 5, 3}
            },
            {
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15,	4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9,	14,	15,	5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13,	11,	6},
                    {4,	3, 2, 12, 9, 5, 15,	10,	11,	14,	1, 7, 6, 0, 8, 13}
            },
            {
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1,	4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6,	11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            },
            {
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
            }
    };
    private static final int[] permutedChoice1 = {
            57, 49, 41, 33, 25, 17, 9,  1, 58, 50, 42, 34, 26, 18,
            10, 2,  59, 51, 43, 35, 27, 19, 11, 3,  60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15, 7,  62, 54, 46, 38, 30, 22,
            14, 6,  61, 53, 45, 37, 29, 21, 13, 5,  28, 20, 12, 4
    };

    private static final int[] permutedChoice2 = {
            14, 17, 11, 24, 1,  5,  3,  28, 15, 6,  21, 10,
            23, 19, 12, 4,  26, 8,  16, 7,  27, 20, 13, 2,
            41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32
    };
    private static final int[] iterations = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };

    public long[] generateSubkeys(long key){
        long[] subkeys = new long[16];

        long key56 = permute(key,permutedChoice1,64);

        int C = (int) (key56 >>> 28) & 0x0FFFFFFF;
        int D = (int) (key56 & 0x0FFFFFFF);

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < iterations[i]; j++) {
                C = ((C << 1) | (C >>> 27)) & 0x0FFFFFFF;
                D = ((D << 1) | (D >>> 27)) & 0x0FFFFFFF;
            }
            subkeys[i] = permute(((long) C << 28) | (D & 0x0FFFFFFF),permutedChoice2,56);
        }
        return subkeys;
    }

    public static long permute(long input,int table[], int inputLen){
        long output = 0;
        for (int i = 0; i < table.length; i++){
            int bitPosition = table[i] - 1;

            long bit = (input >>> (inputLen - 1 - bitPosition)) & 1;

            output = (output << 1 )| bit;
        }
        return output;
    };
    public int functionF(int rightHalf, long subkey){
        long expanded = permute(rightHalf & 0xFFFFFFFFL, expansionTable, 32);

        long xor = expanded ^ subkey;

        int substituted = 0;
        for (int i = 0; i < 8; i++) {
            int block = (int) ((xor >>> (42 - i * 6)) & 0x3F);
            int row = ((block >>> 4) & 2) | (block & 1);
            int col = (block >>> 1) & 0x0F;
            substituted = (substituted << 4) | substitutionBoxes[i][row][col];
        }

        return (int) permute(substituted & 0xFFFFFFFFL, permutationTable, 32);
    }

    public long DESencrypt(long message, long[] subkeys){
        long block = permute(message, initPermTable,64);

        int left = (int) (block >>> 32);
        int right = (int) (block & 0xFFFFFFFFL);

        for (int i = 0; i < 16; i++){
            int temp = right;
            right = left ^ functionF(right,subkeys[i]);
            left=temp;
        }
        long combined = ((long) right << 32) | (left & 0xFFFFFFFFL);
        return permute(combined, inverseInitPermTable,64);
    };

    public long DESXencrypt(long message, long[] subkeys, long key1, long key3){

        long keyed1 = message ^ key1;
        long keyed2 = DESencrypt(keyed1,subkeys);
        long keyed3 = keyed2 ^ key3;

        return keyed3;
    };
    public long DESXdecrypt(long message, long[] subkeys, long key1, long key3){

        long keyed3 = message ^ key3;
        long keyed2 = DESdecrypt(keyed3,subkeys);
        long keyed1 = keyed2 ^ key1;

        return keyed1;
    };

    public long DESdecrypt(long message, long[] subkeys){
        long block = permute(message, initPermTable,64);

        int left = (int) (block >>> 32);
        int right = (int) (block & 0xFFFFFFFFL);

        for (int i = 15; i >= 0; i--){
            int temp = right;
            right = left ^ functionF(right,subkeys[i]);
            left=temp;
        }
        long combined = ((long) right << 32) | (left & 0xFFFFFFFFL);
        return permute(combined, inverseInitPermTable,64);
    };

    long[] stringToLongArray(String input){
        byte[] inputBytes = input.getBytes();
        long[] output = new long[(input.length()+7)/8];

        for(int i = 0; i < output.length; i++){
            long elem = 0;
            for(int j = 0; j < 8; j++){
                elem <<= 8;
                int index = i * 8 + j;
                if (index < inputBytes.length) {
                    elem |= (inputBytes[index] & 0xFF);
                }
            }
            output[i] = elem;
        }
        return output;
    }

    long[] bytesToLongArray(byte[] input){
        long[] output = new long[(input.length+7)/8];

        for(int i = 0; i < output.length; i++){
            long elem = 0;
            for(int j = 0; j < 8; j++){
                elem <<= 8;
                int index = i * 8 + j;
                if (index < input.length) {
                    elem |= (input[index] & 0xFF);
                }
            }
            output[i] = elem;
        }
        return output;
    }

    long stringToLong(String input){
        byte[] inputBytes = input.getBytes();
        long output = 0;
            for(int i = 0; i < 8; i++){
                output <<= 8;
                if (i < inputBytes.length) {
                    output |= (inputBytes[i] & 0xFF);
                }
        }
        return output;
    }

    long bytesToLong(byte[] inputBytes){
        long output = 0;
        for(int i = 0; i < 8; i++){
            output <<= 8;
            if (i < inputBytes.length) {
                output |= (inputBytes[i] & 0xFF);
            }
        }
        return output;
    }
    byte[] longToBytes(long input){
        byte[] output = new byte[8];
        for (int i = 7; i >= 0; i--) {
            output[i] = (byte)(input & 0xFF);
            input >>= 8;
        }
        return output;
    }

    String longArrayToString(long[] input){
        byte[] bytes = new byte[input.length * 8];
        for (int i = 0; i < input.length; i++) {
            long arrayElement = input[i];
            for (int j = 7; j >= 0; j--) {
                bytes[i * 8 + j] = (byte) (arrayElement & 0xFF);
                arrayElement >>= 8;
            }
        }

        String result = new String(bytes);
        return result.replace("\0", "").trim();
    }

    long[] HexStringToLongArray(String input){
        long[] output = new long[input.length() / 16];
        for (int i = 0; i < output.length; i++) {
            output[i] = Long.parseUnsignedLong(input.substring(i * 16, (i + 1) * 16), 16);
        }
        return output;
    }

}

