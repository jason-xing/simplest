package org.projectsample.simplest.s1.security.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5
 */
public final class MD5 {

    /**
     * The jce MD5 message digest generator.
     */
    private static MessageDigest md5;

    /**
     * Retrieves a hexidecimal character sequence representing the MD5 digest of the specified 
     * character sequence, using the specified encoding to first convert the character sequence 
     * into a byte sequence. If the specified encoding is null, then ISO-8859-1 is assumed.
     * 
     * @param string the string to encode
     * @param encoding the encoding used to convert the string into the byte sequence to 
     * submit for MD5 digest
     * 
     * @return a hexidecimal character sequence representing the MD5 digest of the specified string
     * 
     * @throws RuntimeException if an MD5 digest algorithm is not available through 
     * the java.security.MessageDigest spi or the requested encoding is not available
     */
    public static final String encodeString(String string, String encoding) {
        try {
            return byteToHex(digestString(string, encoding));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.toString());
        }
    }

    /**
     * Retrieves a byte sequence representing the MD5 digest of the specified character sequence, 
     * using the specified encoding to first convert the character sequence into a byte sequence.
     * If the specified encoding is null, then ISO-8859-1 is assumed.
     *
     * @param string the string to digest
     * @param encoding the character encoding
     * 
     * @return the digest as an array of 16 bytes
     * 
     * @throws UnsupportedEncodingException if the requested encoding is not available
     * @throws NoSuchAlgorithmException if an MD5 digest algorithm is not available through 
     * the java.security.MessageDigest spi 
     */
    private static byte[] digestString(String string, String encoding) 
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (encoding == null) {
            encoding = "ISO-8859-1";
        }
        byte[] data = string.getBytes(encoding);
        return digestBytes(data);
    }

    /**
     * Retrieves a byte sequence representing the MD5 digest of the specified byte sequence.
     *
     * @param data the data to digest
     * 
     * @return the MD5 digest as an array of 16 bytes
     * 
     * @throws NoSuchAlgorithmException if an MD5 digest algorithm is not available through 
     * the java.security.MessageDigest spi
     */
    private static final byte[] digestBytes(byte[] data) 
            throws NoSuchAlgorithmException {
        synchronized (MD5.class) {
            if (md5 == null) {
                md5 = MessageDigest.getInstance("MD5");
            }
            return md5.digest(data);
        }
    }
    
    /**
     * Converts a byte array into a hexadecimal string.ßßß
     *
     * @param b byte array
     *
     * @return the hex string
     */
    private static String byteToHex(byte b[]) {
        char HEXCHAR[] = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
            'a', 'b', 'c', 'd', 'e', 'f'
        };
        int len = b.length;
        char[] s   = new char[len * 2];
        for (int i = 0, j = 0; i < len; i++) {
            int c = ((int) b[i]) & 0xff;
            s[j++] = HEXCHAR[c >> 4 & 0xf];
            s[j++] = HEXCHAR[c & 0xf];
        }
        return new String(s);
    }

}

