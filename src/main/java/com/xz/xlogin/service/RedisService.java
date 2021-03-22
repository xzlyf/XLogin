package com.xz.xlogin.service;

public interface RedisService {

    /**
     * 存储数据
     */
    void set(String key, String value);

    /**
     * 获取数据
     */
    String get(String key);

    /**
     * 设置超期时间
     *
     * @param expire 超时时间  单位 秒
     */
    boolean expire(String key, long expire);

    /**
     * 获取剩余过期时间
     */
    long getExpire(String key);

    /**
     * 删除数据
     */
    void remove(String key);

    /**
     * 自增操作
     *
     * @param delta 自增步长
     */
    Long increment(String key, long delta);

}