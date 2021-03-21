package com.xz.xlogin.utils;

import java.util.Random;

/**
 * @Author: xz
 * @Date: 2021/3/21
 */
public class RandomUtil {
    private static String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";//随机产生数字与字母组合的字符串


    public static String getRandom(int len) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(randString.charAt(random.nextInt(randString.length())));
        }
        return sb.toString();
    }
}
