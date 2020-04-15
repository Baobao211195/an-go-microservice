package com.an.common.utils;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

public class RandomUtils {

    private static final Random RANDOM = new SecureRandom();
    private static final String DIGITAL = "0123456789";
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String generateOtp(int length) {
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(DIGITAL.charAt(RANDOM.nextInt(DIGITAL.length())));
        }
        return new String(returnValue);
    }

    public static String generatePassword(int length) {
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

    public static void main(String[] args) throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(new SimpleDateFormat("ddMMyyyy").parse("07102019"));
        c.add(Calendar.DATE, 1);
        System.out.println("date: " + c.getTime());
    }
}
