package cool.doudou.mybatis.assistant.expansion.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * BeanUtil
 *
 * @author jiangcs
 * @since 2022/5/11
 */
public class BeanUtil {
    /**
     * 获取属性值
     *
     * @param t    实体对象
     * @param name 属性名
     * @param <T>  类
     * @return 属性值
     */
    public static <T> Object getValue(T t, String name) {
        try {
            Field field = t.getClass().getDeclaredField(name);
            return field.get(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 复制
     *
     * @param source 源类
     * @param target 目标类
     * @param <T>    类
     */
    public static <T> void copy(T source, T target) {
        Map<String, Object> properties = new HashMap<>();
        Arrays.stream(source.getClass().getDeclaredFields()).forEach((field -> {
            try {
                field.setAccessible(true);
                properties.put(field.getName(), field.get(source));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }));
        populateBean(target, target.getClass(), properties);
        populateBean(target, target.getClass().getSuperclass(), properties);
    }

    private static <T> void populateBean(T bean, Class clazz, Map<String, Object> properties) {
        Arrays.stream(clazz.getDeclaredFields()).forEach((field -> {
            try {
                field.setAccessible(true);

                // 如果非空，覆盖原有值；如果为空，则不覆盖原有值
                if (properties.get(field.getName()) != null) {
                    field.set(bean, properties.get(field.getName()));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }));
    }
}
