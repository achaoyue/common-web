package com.mwy.base.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SequenceUtils {
    private static int index = 0;
    public static synchronized  String getSeqCode(){
        int random = (int)(Math.random() * 10000000);
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hashedBytes = digest.digest((System.currentTimeMillis()+"_"+(index++)+"_"+ random).getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
