package com.grandra.smartfarm;

public class PolicyPaging {

    private long currentPage;
    private long beginRow;
    private long pagePerRow;
    private long startPage;
    private long pageSize;
    private long endPage;
    private long lastPage;
    private long totalCount;

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public long getBeginRow() {
        return beginRow;
    }

    public void setBeginRow(long beginRow) {
        this.beginRow = beginRow;
    }

    public long getPagePerRow() {
        return pagePerRow;
    }

    public void setPagePerRow(long pagePerRow) {
        this.pagePerRow = pagePerRow;
    }

    public long getStartPage() {
        return startPage;
    }

    public void setStartPage(long startPage) {
        this.startPage = startPage;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getEndPage() {
        return endPage;
    }

    public void setEndPage(long endPage) {
        this.endPage = endPage;
    }

    public long getLastPage() {
        return lastPage;
    }

    public void setLastPage(long lastPage) {
        this.lastPage = lastPage;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

}