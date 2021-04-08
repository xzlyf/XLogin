package com.xz.xlogin.interceptor;

import com.alibaba.fastjson.JSON;
import com.xz.xlogin.bean.vo.ApiResult;
import com.xz.xlogin.constant.StatusEnum;
import com.xz.xlogin.service.impl.AppServiceImpl;
import com.xz.xlogin.utils.SecretUtil;
import com.xz.xlogin.utils.ServletUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * interceptor for api sign
 * 验签拦截器
 */
@Component
public class SignInterceptor implements HandlerInterceptor {
    @Autowired
    AppServiceImpl appServiceImpl;
    private static long REQUEST_TIMEOUT = 5 * 60 * 1000;//服务器时间不可与服务器相差超过n分钟

    /**
     * @param request：请求对象
     * @param response：响应对象
     * @param handler：处理对象：controller中的信息
     * @return true表示正常, false表示被拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //拦截请求时间不在范围的请求
        String timestampStr = request.getHeader("timestamp");
        long timestamp;
        try {
            if (StringUtils.isBlank(timestampStr)) {
                ServletUtil.renderString(response, JSON.toJSONString(new ApiResult(StatusEnum.STATUS_401, null)));
                return false;
            }
            timestamp = Long.parseLong(timestampStr);
            long now = System.currentTimeMillis();
            if (now - timestamp >= REQUEST_TIMEOUT || now - timestamp <= -REQUEST_TIMEOUT) {
                ServletUtil.renderString(response, JSON.toJSONString(new ApiResult(StatusEnum.STATUS_303, null)));
                return false;
            }
        } catch (Exception e) {
            ServletUtil.renderString(response, JSON.toJSONString(new ApiResult(StatusEnum.STATUS_401, null)));
            return false;
        }
        //拦截空appid的的请求
        String appId = request.getHeader("appid");
        if (StringUtils.isBlank(appId)) {
            ServletUtil.renderString(response, JSON.toJSONString(new ApiResult(StatusEnum.STATUS_401, null)));
            return false;
        }
        //拦截空签名的请求
        String sign = request.getHeader("sign");
        if (StringUtils.isBlank(sign)) {
            ServletUtil.renderString(response, JSON.toJSONString(new ApiResult(StatusEnum.STATUS_401, null)));
            return false;
        }

        //通过读取appSecret判断请求者appId是否合法
        String appSecret = appServiceImpl.getAppSecret(appId);
        if (appSecret == null) {
            //非法appId
            ServletUtil.renderString(response, JSON.toJSONString(new ApiResult(StatusEnum.STATUS_306, null)));
            return false;
        }
        /*
         *开始校验签名  参考验参规则
         * 2.0 sign加密
         * 规则：根据key的ANSI码从小到大排序得到
         * MD5(AppId+Key=Value+Key=Value...+Key=Value+AppSecret)
         * （+号 =号 省略）
         */
        String origin = SecretUtil.getSignByRequest(request, appId, appSecret);//计算签名
        System.out.println("===================");
        System.out.println("timestamp------->" + timestamp);
        System.out.println("appId    ------->" + appId);
        System.out.println("clientSign------>" + sign);
        System.out.println("originSign------>" + origin);
        System.out.println("===================");
        //todo 做好日志存储
        assert origin != null;

        if (!origin.equalsIgnoreCase(sign)) {
            ServletUtil.renderString(response, JSON.toJSONString(new ApiResult(StatusEnum.STATUS_301, null)));
            return false;
        }

        //sign校验无问题,放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}