package com.xz.xlogin.utils;


import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/1/6
 */
public class SecretUtil {

    /**
     * 1.0 sign加密规则
     *
     * @param timestamp 时间戳
     */
    public static String getSign(long timestamp,String appId,String appSecret,String version) {
        return MD5Util.getMD5(appId + appSecret + timestamp + version);
    }

    /**
     * 2.0 sign加密
     * 规则：根据key的ANSI码从小到大排序得到
     * MD5(AppId+Key=Value+Key=Value...+Key=Value+AppSecret)
     * （+号 =号 省略）
     */
    public static String getSign(Map<String, Object> params,String appId,String appSecret) {
        Map<String, Object> newParams = sortMapByKey(params);
        StringBuilder sb = new StringBuilder();
        sb.append(appId);
        for (String key : newParams.keySet()) {
            sb.append(key);
            sb.append(newParams.get(key));
        }
        sb.append(appSecret);
        return MD5Util.getMD5(sb.toString());
    }

    /**
     * 服务端调用
     * 获取请求者的sign
     */
    public static String getSignByRequest(HttpServletRequest request,String appId,String appSecret) throws NullPointerException {

        Map<String, String[]> params = request.getParameterMap();
        Map<String, Object> newParams = new HashMap<>();
        for (String key : params.keySet()) {
            newParams.put(key, params.get(key)[0]);
        }
        return getSign(newParams,appId,appSecret);
    }


    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    private static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        sortMap.putAll(map);
        return sortMap;

    }
}

