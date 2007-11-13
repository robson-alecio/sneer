/*
 * [ JIGA ]
 * 
 * Copyright (c) 2004 Shiraz Kanga <skanga at findant.com>
 * 
 * This code is distributed under the GNU Library General Public License
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Library General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Library General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 675 Mass Ave, Cambridge, MA 02139, USA.
 * 
 * 
 * [http://glenn.sanson.free.fr/jiga/]
 */

package frozenbubble.game.net.library.jiga;

/**
 * A simple BASE64 codec. 
 * It implements both normal and safe BASE64 alphabets.
 * The only difference between these two versions is the use of symbols '-' and '_'
 * in replacement of '+' and '/' (often used as special characters)
 * 
 * @author Glenn Sanson
 */
public class Base64 {
    /** Characters of the normal BASE64 alphabet */
    public final static char[] BASE64_ALPHABET = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
    /** Characters of the safe BASE64 alphabet */
    public final static char[] BASE64_SAFE_ALPHABET = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_' };

    private Base64() {}
    
    /**
     * Converts an ISO-8859-1 <code>String</code> into its BASE64 (normal set) representation 
     * @param data The data to convert
     * @return Its BASE64 interpretation
     */
    public static String toBase64(String data) {
        return toBase64(data, BASE64_ALPHABET);
    }

    /**
     * Converts an ISO-8859-1 <code>String</code> into its BASE64 representation 
     * @param data The data to convert
     * @param base64 The set to use
     * @return Its BASE64 interpretation
     */
    public static String toBase64(String data, char[] base64) {
        String output = new String();

        int[] input = null;
        try {
            byte[] temp = data.getBytes("ISO-8859-1");

            input = new int[temp.length];
            for (int i = 0; i < input.length; i++) {
                if (temp[i] < 0) {
                    input[i] = 128 - temp[i];
                } else {
                    input[i] = temp[i];
                }
            }
        } catch (Exception e) {
            System.err.println("Character set not recognized");
        }

        int current = 0;

        for (int i = 0; i < input.length; i++) {

            output += base64[current + (input[i] >> ((i % 3 + 1) * 2))];
            current = ((input[i] << ((3 - i % 3) * 2)) & 255) >> 2;

            if (i % 3 == 2) {
                output += base64[current];
                current = 0;
            }
        }

        if (input.length % 3 != 0) {

            output += base64[current];

            for (int i = 0; i < (3 - (input.length % 3)); i++) {
                output += "=";
            }
        }

        return output;
    }

    public static String fromBase64(String data) {
        return fromBase64(data, BASE64_ALPHABET);
    }

    public static String fromBase64(String data, char[] base64) {

        String texte = new String();

        int newInt = 0;
        int complet = 0;

        for (int i = 0; i < data.length(); i++) {
            int value = getBase64Value(data.charAt(i), base64);

            if (value != -1) {
                if (complet == 0) {
                    newInt += value << 2;
                }
                if (complet == 2) {
                    newInt += value;
                    texte += (char) newInt;
                    newInt = 0;
                }
                if (complet > 2) {
                    newInt += (value >> (complet - 2)) & 255;
                    texte += (char) newInt;
                    newInt = (value << (10 - complet)) & 255;
                }

                complet += 6;
                complet %= 8;
            }
        }

        if (newInt != 0) {
            texte += (char) newInt;
        }

        return texte;
    }

    private static int getBase64Value(char c, char[] base64) {
        for (int i = 0; i < 64; i++) {
            if (c == base64[i]) {
                return i;
            }
        }

        return -1;
    }
}