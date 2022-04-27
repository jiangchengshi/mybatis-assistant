package cool.doudou.mybatis.assistant.core.page;

/**
 * Page
 *
 * @author jiangcs
 * @since 2022/4/1
 */
public class Page {
    /**
     * 当前页码
     */
    private int pageNum;
    /**
     * 每页记录数
     */
    private int pageSize;
    /**
     * 总记录数
     */
    private long total = 0;

    public Page(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Page page() {
        return this;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
