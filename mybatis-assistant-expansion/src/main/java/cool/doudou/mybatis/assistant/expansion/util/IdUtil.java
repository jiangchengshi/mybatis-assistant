package cool.doudou.mybatis.assistant.expansion.util;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * IdUtil
 *
 * @author jiangcs
 * @since 2022/5/10
 */
public class IdUtil {
    private static final AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 由于前端16位限制
     */
    private static final int ID_LENGTH = 16;

    public static Long nextId() {
        // 4位随机数
        int random4 = 1000 + ThreadLocalRandom.current().nextInt(8999);
        // 时间戳：毫秒
        String millisStr = String.valueOf(System.currentTimeMillis());
        // 递增生成最大9999的递增ID
        if (atomicInteger.get() == 9999) {
            atomicInteger.set(0);
        }
        int atomicInt = atomicInteger.getAndIncrement();
        String id = millisStr.substring(5).concat(String.valueOf(random4)).concat(String.valueOf(atomicInt));
        // 防止长度超出
        if (id.length() > ID_LENGTH) {
            id = id.substring(id.length() - ID_LENGTH);
        }
        return Long.valueOf(id);
    }
}
