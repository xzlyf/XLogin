package com.xz.xlogin.utils;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具
 */
public class PageUtil {

    /**
     * 每页大小
     */
    private int pageSize;

    /**
     * 当前页为第几页
     */
    private int number;

    /**
     * 总共有多少页
     */
    private int totalPages;

    /**
     * 总共有多少条数据
     */
    private int totalElements;

    /**
     * 数据
     */
    private List content = new ArrayList();

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalPages = totalElements % pageSize == 0 ? totalElements/pageSize : (totalElements/pageSize) + 1;
        this.totalElements = totalElements;
    }

    public List getContent() {
        return content;
    }

    public void setContent(List content) {
        this.content = content;
    }
}
