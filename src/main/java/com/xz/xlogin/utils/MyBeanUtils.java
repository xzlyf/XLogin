package com.xz.xlogin.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 少丶客
 * @ClassName MyBeanUtils
 * @Description 当执行updateBlog方法时，
 *               blog赋值给b会导致createTime为空，
 *               所以用此类找出不为空的数据传给b
 * @Date 2020/6/28 19:10
 * @Vesion 1.0
 */
public class MyBeanUtils {

    /**
     * 获取所有的属性值为空属性名数组
     * @param source
     * @return
     */
    public static String[] getNullPropertyNames(Object source) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds =  beanWrapper.getPropertyDescriptors();
        List<String> nullPropertyNames = new ArrayList<>();
        for (PropertyDescriptor pd : pds) {
            String propertyName = pd.getName();
            if (beanWrapper.getPropertyValue(propertyName) == null) {
                nullPropertyNames.add(propertyName);
            }
        }
        return nullPropertyNames.toArray(new String[nullPropertyNames.size()]);
    }

}