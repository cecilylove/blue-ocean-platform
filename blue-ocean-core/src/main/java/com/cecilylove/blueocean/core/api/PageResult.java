package com.cecilylove.blueocean.core.api;

import com.cecilylove.blueocean.core.enums.CommonRespCode;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果集
 *
 * @author Wang Li Hong
 * @since 1.0.0
 */
public class PageResult<T> extends Result<PageResult.PageInfo<T>> {


    /**
     * 手动构建分页返回 (通用版)
     * 适用于 MyBatis, JPA, ES 等所有场景
     * @param current 当前页码
     * @param size 每页显示条数
     * @param total 总记录数
     * @param list 结果集
     * @return PageResult
     * @param <T> 泛型
     */
    public static <T> PageResult<T> of(long current, long size, long total, List<T> list) {
        PageResult<T> result = new PageResult<>();
        result.setCode(CommonRespCode.SUCCESS.getCode());
        result.setMessage(CommonRespCode.SUCCESS.getMessage());

        PageInfo<T> pageInfo = new PageInfo<>();
        pageInfo.setCurrent(current);
        pageInfo.setSize(size);
        pageInfo.setTotal(total);
        pageInfo.setList(list);

        // 计算总页数
        if (size > 0) {
            pageInfo.setPages((total + size - 1) / size);
        } else {
            pageInfo.setPages(0L);
        }

        result.setData(pageInfo);
        return result;
    }


    @Data
    public static class PageInfo<T> implements Serializable {
        /**
         * 当前页码
         */
        private Long current;
        /**
         * 每页显示条数
         */
        private Long size;
        /**
         * 总记录数
         */
        private Long total;
        /**
         * 总页数
         */
        private Long pages;
        /**
         * 结果集
         */
        private List<T> list;

        public String toString() {
            return "PageInfo{current=" + this.current + ", size=" + this.size + ", total=" + this.total + ", pages=" + this.pages + ", list=" + this.list + '}';
        }
    }
}

