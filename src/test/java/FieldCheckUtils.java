import cn.crabapples.annotations.Crabapples;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * TODO 属性验证工具
 *
 * @author Mr.He
 * @date 2018/9/27 4:31:27
 * e-mail wishforyou.xia@gmail.com
 * qq 294046317
 * pc-name 29404
 */
public class FieldCheckUtils {
    //需要进行匹配的值(不区分大小写)
    private static final String[] CHECK_PARAMS = {" ", "", "[]", "{}", "undefined", "null", "[object Object]"};

    /**
     * 实体类执行匹配操作
     *
     * @param value 需要进行匹配的值
     * @return 返回验证结果，true为匹配成功，false为匹配失败
     */
    private static boolean entityCheckMethod(Object value) {
        //首先判断需要验证的value是否为null
        if (null != value) {
            for (String checkParam : CHECK_PARAMS) {
                //验证value中是否有CHECK_PARAMS中对应的值
                if ((checkParam.equals(value.toString().toLowerCase()))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Map执行匹配操作
     *
     * @param entity 当前验证参数值
     * @return 返回验证结果，true为匹配成功，false为匹配失败
     */
    private static boolean mapCheckMethod(Object entity) {
        //首先判断需要验证的value是否为null
        if (null != entity) {
            for (String checkParam : CHECK_PARAMS) {
                //验证value中是否有CHECK_PARAMS中对应的值
                if ((checkParam.equals(entity.toString().toLowerCase()))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 对传入Map中传入的key进行匹配
     *
     * @param map    需要验证的Map
     * @param params params 需要验证的Key
     * @return 返回验证结果，true为通过，false为未通过
     */
    public static boolean mapCheckByArray(Map<String, Object> map, String[] params) {
        //获取map的KeySet
        Set<String> keySet = map.keySet();
        //遍历map的key
        for (String key : keySet) {
            //遍历需要验证的key名字
            for (String param : params) {
                //判断map中的key是否有需要验证的值
                if (key.equals(param)) {
                    //获取对应key的value值
                    Object entry = map.get(key);
                    //进入MapCheckMethod方法判断进行匹配验证
                    if (mapCheckMethod(entry)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 对传入实体类进行对应属性匹配(泛型方法)(只能传入实体类)
     *
     * @param <T>    泛型方法
     * @param entity  需要验证的实体类
     * @param params 需要验证的属性
     * @return 返回验证结果，true为匹配成功，false为匹配失败
     */
    public static <T> boolean entityCheckByArray(T entity, String[] params) throws Exception {
        //获取传入参数的类型中包含的方法
        Method[] methods = entity.getClass().getMethods();
        for (String str : params) {
            //将需要验证的参数拼接为get方法
            String checkName = ("get" + str).toLowerCase();
            for (Method method : methods) {
                //判断传入参数中的所有以get开头的方法
                if (method.getName().startsWith("get")) {
                    //判断当前get方法是否为需要验证的方法
                    if (checkName.equals(method.getName().toLowerCase())) {
                        //执行当前get方法并获取返回值
                        Object result = method.invoke(entity);
                        //进入MapCheckMethod方法判断进行匹配验证
                        if (entityCheckMethod(result)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 对传入实体类进行所有属性匹配(泛型方法)(只能传入实体类)
     *
     * @param <T>   泛型方法
     * @param entity 需要验证的实体类
     * @return 返回验证结果，true为匹配成功，false为匹配失败
     */
    public static <T> boolean entityCheckAll(T entity) throws Exception {
        //获取传入参数的类型中包含的方法
        Method[] methods = entity.getClass().getMethods();
        for (Method method : methods) {
            //判断传入参数中的所有以get开头的方法
            if (method.getName().startsWith("get")) {
                //执行当前get方法并获取返回值
                Object result = method.invoke(entity);
                //进入MapCheckMethod方法判断进行匹配验证
                if (entityCheckMethod(result)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 对传入实体类进行被@r标记的属性进行匹配(泛型方法)(只能传入实体类)
     * 注:若未添加注解则始终验证通过
     *
     * @param entity 需要验证的实体类
     * @return 返回验证结果，true为匹配成功，false为匹配失败
     * @param<T> 泛型方法
     */
    public static <T, R extends Annotation> boolean entityCheckByAnnotation(T entity, R r) throws Exception {

        //获取所有自定义属性
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            //获取每个属性的名字,用于下面验证时拼接
            String name = field.getName();
            Annotation annotation = field.getDeclaredAnnotation(r.annotationType());
            //判断属性上是否拥有注解@r
            if (null != annotation) {
                //获取所方法
                Method[] methods = entity.getClass().getMethods();
                //遍历所有方法
                for (Method method : methods) {
                    //判断当前方法是否为get方法
                    if (method.getName().toLowerCase().equals(("get" + name).toLowerCase())) {
                        //执行当前get方法并获取返回值
                        Object result = method.invoke(entity);
                        //进入ModelCheckMethod方法进行匹配
                        if (entityCheckMethod(result)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param param 需要验证的字符串
     * @return 返回验证结果，true为匹配成功，false为匹配失败
     */
    public static boolean stringCheck(String param) {
        if (null != param) {
            for (String checkParam : CHECK_PARAMS) {
                //判断value中是否有CHECK_PARAMS中对应的值
                if ((checkParam.equals(param.toLowerCase()))) {
                    return true;
                }
            }
        }
        return false;
    }
}
