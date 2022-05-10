package cool.doudou.mybatis.assistant.core.page;

/**
 * 分页参数
 *
 * @author jiangcs
 * @since 2022/4/1
 */
public class PageDTO<T> {
    private int pageNum;
    private int pageSize;
    private T data;

    public PageDTO() {
    }

    public PageDTO(int pageNum, int pageSize, T data) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.data = data;
    }

    public Page page() {
        if (this.pageNum == 0) {
            this.pageNum = 1;
        }
        if (this.pageSize == 0) {
            this.pageSize = 20;
        }
        return new Page(this.pageNum, this.pageSize);
    }

    public T data() {
        return data;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "PageDTO{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", data=" + data +
                '}';
    }
}
