package com.eshore.common.core.response;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author jianlin.liu
 * @since 2022/07/01
 *
 *  * 分页信息数据体
 */
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseBody<T> implements Serializable {
    private static final long serialVersionUID = 2958399849099655507L;
    /**
     * 数据总条数
     */
    private int total;

    /**
     * 数据记录
     */
    private List<T> records;

    /**
     * 当前页
     */
    private int pageNum;

    /**
     * 每页的数量
     */
    private int pageSize;

    /**
     * 当前页的数量
     */
    private int size;

    /**
     * 总页数
     */
    private int pages;


    public PageResponseBody<T> setPageInfo(PageInfo<T> pageInfo) {
        this.total = (int) pageInfo.getTotal();
        this.records = pageInfo.getList();
        this.pageNum = pageInfo.getPageNum();
        this.pageSize = pageInfo.getPageSize();
        this.size = pageInfo.getSize();
        this.pages = pageInfo.getPages();
        return this;
    }

    public PageResponseBody setTotal(int total) {
        this.total = total;
        return this;
    }

    public PageResponseBody setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    public PageResponseBody setPageNum(int pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public PageResponseBody setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PageResponseBody setSize(int size) {
        this.size = size;
        return this;
    }

    public PageResponseBody setPages(int pages) {
        this.pages = pages;
        return this;
    }
}
