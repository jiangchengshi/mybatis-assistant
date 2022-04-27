package cool.doudou.mybatis.assistant.core.helper;

import cool.doudou.mybatis.assistant.core.page.Page;

import java.util.Map;

/**
 * PageHelper
 *
 * @author jiangcs
 * @since 2022/4/3
 */
public class PageHelper {
    protected static final ThreadLocal<Page> LOCAL_PAGE = new ThreadLocal<>();

    public static Page getPage(Object parameterObject) {
        Page page = LOCAL_PAGE.get();
        if (page == null) {
            if (parameterObject instanceof Page) {
                page = (Page) parameterObject;
            } else if (parameterObject instanceof Map) {
                Map<String, Object> parameterObjectMap = (Map<String, Object>) parameterObject;
                for (Object value : parameterObjectMap.values()) {
                    if (value instanceof Page) {
                        page = (Page) value;
                    }
                }
            }

            setLocalPage(page);
        }
        return page;
    }

    public static Page getLocalPage() {
        return LOCAL_PAGE.get();
    }

    protected static void setLocalPage(Page page) {
        LOCAL_PAGE.set(page);
    }

    public static void clearLocalPage() {
        LOCAL_PAGE.remove();
    }
}
