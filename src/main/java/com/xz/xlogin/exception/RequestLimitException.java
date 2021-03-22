package com.xz.xlogin.exception;

/**
 * @Author:高键城
 * @time：
 * @Discription：
 */
public class RequestLimitException extends Exception {
    private static final long serialVersionUID = 1364225358754654702L;

    public RequestLimitException(){
        super("HTTP请求超出设定的限制");
    }

    public RequestLimitException(String message){
        super(message);
    }
}