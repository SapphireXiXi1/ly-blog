package com.example.utils;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {
    // 全部字符
    public static final String allCharacter = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    // 字母字符
    public static final String letterCharacter = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    // 数字字符
    public static final String numberCharacter = "0123456789";

    /**
     * 随机生成36位字符
     *
     * @return
     */
    public static String UUID36() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    /**
     * 随机生成32位字符
     *
     * @return
     */
    public static String UUID32() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    /**
     * 生成纯数字字符串
     *
     * @param length 自己根据需求传参
     * @return 如：774464
     */
    public static String generateNumberStr(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(numberCharacter.charAt(random.nextInt(numberCharacter.length())));
        }
        return stringBuffer.toString();
    }

    /**
     * 生成纯零的字符串
     *
     * @param length
     * @return 如：00000
     */
    public static String generateZeroStr(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append('0');
        }
        return stringBuffer.toString();
    }

    /**
     * 生成包含大小写字母的字符串
     *
     * @param length
     * @return 如: XrUhokjP
     */
    public static String generateLetterStr(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(letterCharacter.charAt(random.nextInt(letterCharacter.length())));
        }
        return stringBuffer.toString();
    }

    /**
     * 生成纯小写字母字符串
     *
     * @param length
     * @return 如：bdiasbd
     */
    public static String generateLowercaseLetterStr(int length) {
        return generateLetterStr(length).toLowerCase();
    }

    /**
     * 生成纯大写字母字符串
     *
     * @param length
     * @return 如：BAUFDGFI
     */
    public static String generateUppercaseLetterStr(int length) {
        return generateLetterStr(length).toUpperCase();
    }

    /**
     * 生成包含大、小写字母、数字的字符串
     *
     * @param length
     * @return 如: sdOJOJ015
     */
    public static String generateAllLetterStr(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(allCharacter.charAt(random.nextInt(allCharacter.length())));
        }
        return stringBuffer.toString();
    }

}
