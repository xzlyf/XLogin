package com.xz.xlogin.pojo.vo;

import com.xz.xlogin.constant.StatusEnum;
import org.springframework.data.domain.Page;

import java.io.Serializable;

/**
 * @Author: xz
 * @Date: 2020/11/22
 * <p>
 * 分页结果实体数据返回
 */
public class PagingResult<T> extends ApiResult implements Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * 每页大小
     */
    private int page;

    /**
     * 当前页为第几页
     */
    private int size;

    /**
     * 总共有多少页
     */
    private int totalPages;

    /**
     * 总共有多少条数据
     */
    private long totalElements;

    public PagingResult(StatusEnum status, Page<T> page) {
        super(status, page.getContent());
        this.page = page.getSize();
        this.size = page.getNumber() + 1;
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalPages = totalElements % page == 0 ? totalElements / page : (totalElements / page) + 1;
        this.totalElements = totalElements;
    }
}
