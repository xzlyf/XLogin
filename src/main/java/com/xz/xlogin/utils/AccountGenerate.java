package com.xz.xlogin.utils;

import java.util.Date;
import java.util.Random;

/**
 * @Author: xz
 * @Date: 2020/11/19
 * 账户生成器
 * 随机生成指定规则账号(纯数字)
 */
public class AccountGenerate {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println("得到账号：" + makeAccount(8));
        }

    }


    /**
     * 简单版账号生成器
     *
     * @param len 指定位数
     * @return
     */
    public static String makeAccount(int len) {
        Random random = new Random();
        StringBuilder buffNumber = new StringBuilder();
        int temp;
        for (int i = 0; i < len; i++) {
            temp = random.nextInt(10);
            if (i == 0) {
                //保证账号第一位不为0；
                while (temp == 0) {
                    temp = random.nextInt(10);
                }
            }
            buffNumber.append(temp);
        }
        return buffNumber.toString();
    }


    /**
     * 是否靓号
     *
     * @param account
     * @return
     */
    //private boolean isLuckyNumber(String account) {
    //Pattern pattern;
    //Matcher matcher;
    //for (String regex : luckyPatterns) {
    //    pattern = Pattern.compile(regex);
    //    matcher = pattern.matcher(account);
    //    if (matcher.matches()) {
    //        return true;
    //    }
    //}
    //return false;
    //}
}
