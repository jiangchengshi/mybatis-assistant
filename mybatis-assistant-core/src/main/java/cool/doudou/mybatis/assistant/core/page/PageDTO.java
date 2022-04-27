package cool.doudou.mybatis.assistant.core.page;

/**
 * 分页参数
 *
 * @author jiangcs
 * @since 2022/4/1
 */
public class PageDTO<T> extends Page {
    private T data;

    public PageDTO(int pageNum, int pageSize) {
        super(pageNum, pageSize);
    }

    public Page page() {
        return super.page();
    }

    public T data() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PageDTO{" +
                "data=" + data +
                '}';
    }
}
