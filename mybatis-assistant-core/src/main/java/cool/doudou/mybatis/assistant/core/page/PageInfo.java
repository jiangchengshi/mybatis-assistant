package cool.doudou.mybatis.assistant.core.page;

import cool.doudou.mybatis.assistant.core.helper.PageHelper;

import java.util.Collections;
import java.util.List;

/**
 * 封装分页信息
 *
 * @author jiangcs
 * @since 2022/4/2
 */
public class PageInfo<T> {
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
    /**
     * 总页数
     */
    private int pages = 0;
    /**
     * 数据集合
     */
    private List<T> list = Collections.emptyList();

    public PageInfo(int pageNum, int pageSize, long total) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
    }

    public static <T> PageInfo<T> of(List<T> list) {
        int pageNum = 1, pageSize = 20;
        long total = 0;
        Page page = PageHelper.getLocalPage();
        if (page != null) {
            total = page.getTotal();
            pageNum = page.getPageNum();
            pageSize = page.getPageSize();
        }

        PageInfo<T> pageInfo = new PageInfo<>(pageNum, pageSize, total);
        pageInfo.setPages(calcPages(total, pageSize));
        pageInfo.setList(list);

        return pageInfo;
    }

    private static int calcPages(long total, int pageSize) {
        int pages = 0;
        if (total > 0) {
            pages = (int) (total / pageSize);
            if (total % pageSize != 0) {
                pages++;
            }
        }
        return pages;
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

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", total=" + total +
                ", pages=" + pages +
                ", list=" + list +
                '}';
    }
}
