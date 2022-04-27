package cool.doudou.mybatis.assistant.core.functions;

import cool.doudou.mybatis.assistant.expansion.util.ComUtil;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FunctionGetter
 *
 * @author jiangcs
 * @since 2022/4/21
 */
public class FunctionGetter {
    public static Map<Class, SerializedLambda> LAMBDA_CACHE = new ConcurrentHashMap<>();

    public static <T> String name(SFunction<T> sFunction) {
        SerializedLambda serializedLambda = getSerializedLambda(sFunction);
        if (serializedLambda != null) {
            String implMethodName = serializedLambda.getImplMethodName();
            return ComUtil.lowerFirst(implMethodName.replace("get", ""));
        }
        return null;
    }

    private static <T> SerializedLambda getSerializedLambda(SFunction<T> sFunction) {
        SerializedLambda serializedLambda = LAMBDA_CACHE.get(sFunction.getClass());
        if (serializedLambda == null) {
            try {
                Method method = sFunction.getClass().getDeclaredMethod("writeReplace");
                method.setAccessible(Boolean.TRUE);
                serializedLambda = (SerializedLambda) method.invoke(sFunction);
                LAMBDA_CACHE.put(sFunction.getClass(), serializedLambda);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return serializedLambda;
    }
}
