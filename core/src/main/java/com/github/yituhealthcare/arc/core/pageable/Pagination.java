package com.github.yituhealthcare.arc.core.pageable;

import java.io.Serializable;

/**
 * @author junhao.chen
 */
public class Pagination implements Serializable {

    private Integer pageNum;

    private Integer pageSize;

    public Pagination(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Pagination() {
    }

    public static PaginationBuilder builder() {
        return new PaginationBuilder();
    }

    public Integer getPageNum() {
        return this.pageNum;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String toString() {
        return "Pagination(pageNum=" + this.getPageNum() + ", pageSize=" + this.getPageSize() + ")";
    }

    public static class PaginationBuilder {
        private Integer pageNum;
        private Integer pageSize;

        PaginationBuilder() {
        }

        public Pagination.PaginationBuilder pageNum(Integer pageNum) {
            this.pageNum = pageNum;
            return this;
        }

        public Pagination.PaginationBuilder pageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Pagination build() {
            return new Pagination(pageNum, pageSize);
        }

        public String toString() {
            return "Pagination.PaginationBuilder(pageNum=" + this.pageNum + ", pageSize=" + this.pageSize + ")";
        }
    }
}
