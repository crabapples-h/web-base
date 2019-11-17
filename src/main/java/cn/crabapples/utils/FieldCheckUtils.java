package cn.crabapples.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * TODO 属性验证工具(对属性进行匹配，不区分大小写‘)
 *
 * @author Mr.He
 * @date 2018/9/27 4:31:27
 * e-mail wishforyou.xia@gmail.com
 * qq 294046317
 * pc-name 29404
 */
public class FieldCheckUtils {
    private static final Logger logger = LoggerFactory.getLogger(FieldCheckUtils.class);
    //需要进行匹配的值(不区分大小写)
//    private static String[] checkParams = {" ", "", "[]", "{}", "undefined", "null", "[object Object]"};
    private String[] checkParams = {};

    public FieldCheckUtils(String[] checkParams) {
        logger.info("传入匹配组为:[Array]");
        FieldCheckUtils(checkParams);
    }

    public FieldCheckUtils(List<String> checkParams) {
        logger.info("传入匹配组为:[List]");
        FieldCheckUtils(checkParams);
    }

    private void FieldCheckUtils(Object object) {
        logger.debug("准备开始成匹配组");
        List<String> params = new ArrayList<>();
        if (object instanceof String[]) {
            params = Arrays.asList((String[]) object);
        } else if (object instanceof List) {
            params = (List<String>) object;
        }
        logger.debug("开始成匹配组");
        this.checkParams = params.toArray(this.checkParams);
        logger.debug("匹配组生成完毕");
    }

    /**
     * 实体类执行匹配操作
     *
     * @param key   当前执行匹配的属性名
     * @param value 需要进行匹配的值
     * @return 返回验证结果，true为匹配成功，false为匹配失败
     */
    private boolean entityCheckMethod(String key, Object value) {
        logger.debug("当前进行匹配的属性:[{}],值:[{}]", key, value);
        //首先判断需要验证的value是否为null
        if (null != value) {
            for (String checkParam : checkParams) {
                //验证value中是否有checkParams中对应的值
                if ((checkParam.equals(value.toString().toLowerCase()))) {
                    logger.debug("属性匹配成功:[{}],值:[{}]", key, value);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Map执行匹配操作
     *
     * @param key   当前执行匹配的属性名
     * @param value 当前验证参数值
     * @return 返回验证结果，true为匹配成功，false为匹配失败
     */
    private boolean mapCheckMethod(String key, Object value) {
        logger.debug("当前进行匹配的属性:[{}],值:[{}]", key, value);
        //首先判断需要验证的value是否为null
        if (null != value) {
            for (String checkParam : checkParams) {
                //验证value中是否有checkParams中对应的值
                if ((checkParam.equals(value.toString().toLowerCase()))) {
                    logger.debug("属性匹配成功:[{}],值:[{}]", key, value);
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
    public boolean mapCheckByArray(Map<String, Object> map, String[] params) {
        logger.info("开始对map执行匹配操作,需要执行匹配的Map:[{}],需要匹配的key:[{}]", map, params);
        //获取map的KeySet
        Set<String> keySet = map.keySet();
        logger.debug("获取keySet成功:[{}]", keySet);
        //遍历map的key
        for (String key : keySet) {
            //遍历需要验证的key名字
            for (String param : params) {
                //判断map中的key是否有需要验证的值
                if (key.equals(param)) {
                    logger.debug("开始对:[{}]执行匹配操作", key);
                    //获取对应key的value值
                    Object value = map.get(key);
                    //进入mapCheckMethod方法判断进行匹配验证
                    if (mapCheckMethod(key, value)) {
                        logger.debug("属性:[{}]匹配成功", key);
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
     * @param entity 需要验证的实体类
     * @param params 需要验证的属性
     * @return 返回验证结果，true为匹配成功，false为匹配失败
     */
    public <T> boolean entityCheckByArray(T entity, String[] params) throws Exception {
        logger.info("开始对entity执行匹配操作,需要执行匹配的类:[{}],需要匹配的key:[{}]", entity, params);
        //获取传入参数的类型中包含的方法
        Method[] methods = entity.getClass().getMethods();
        logger.debug("获取实体类属性成功:[{}]", methods);
        for (String str : params) {
            //将需要验证的参数拼接为get方法
            String checkName = ("get" + str).toLowerCase();
            for (Method method : methods) {
                //判断传入参数中的所有以get开头的方法
                if (method.getName().startsWith("get")) {
                    //判断当前get方法是否为需要验证的方法
                    if (checkName.equals(method.getName().toLowerCase())) {
                        logger.debug("开始对:[{}]执行匹配操作", method.getName());
                        //执行当前get方法并获取返回值
                        Object result = method.invoke(entity);
                        //进入entityCheckMethod方法判断进行匹配验证
                        if (entityCheckMethod(method.getName(), result)) {
                            logger.debug("属性:[{}]匹配成功", method.getName());
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
     * @param <T>    泛型方法
     * @param entity 需要验证的实体类
     * @return 返回验证结果，true为匹配成功，false为匹配失败
     */
    public <T> boolean entityCheckAll(T entity) throws Exception {
        logger.info("开始对entity的所有属性执行匹配操作,需要执行匹配的类:[{}]", entity);
        //获取传入参数的类型中包含的方法
        Method[] methods = entity.getClass().getMethods();
        logger.debug("获取实体类属性成功:[{}]", methods);
        for (Method method : methods) {
            //判断传入参数中的所有以get开头的方法
            if (method.getName().startsWith("get")) {
                logger.debug("开始对:[{}]执行匹配操作", method.getName());
                //执行当前get方法并获取返回值
                Object result = method.invoke(entity);
                //进入entityCheckMethod方法判断进行匹配验证
                if (entityCheckMethod(method.getName(), result)) {
                    logger.debug("属性:[{}]匹配成功", method.getName());
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
    public <T, R extends Annotation> boolean entityCheckByAnnotation(T entity, Class<R> r) throws Exception {
        logger.info("开始对entity的所有属性执行匹配操作,需要执行匹配的类:[{}],需要匹配的注解:[{}]", entity, r);
        //获取所有自定义属性
        Field[] fields = entity.getClass().getDeclaredFields();
        logger.debug("获取实体类自定义属性成功:[{}]", fields);
        for (Field field : fields) {
            //获取每个属性的名字,用于下面验证时拼接
            String name = field.getName();
            Annotation annotation = field.getDeclaredAnnotation(r);
            logger.debug("获取属性:[{}]上的注解成功", field);
            //判断属性上是否拥有注解@r
            if (null != annotation) {
                //获取所方法
                Method[] methods = entity.getClass().getMethods();
                //遍历所有方法
                logger.debug("属性:[{}]上存在:[{}]注解", field, r);
                for (Method method : methods) {
                    //判断当前方法是否为get方法
                    if (method.getName().toLowerCase().equals(("get" + name).toLowerCase())) {
                        logger.debug("开始对:[{}]执行匹配操作", method.getName());
                        //执行当前get方法并获取返回值
                        Object result = method.invoke(entity);
                        //进入entityCheckMethod方法判断进行匹配验证
                        if (entityCheckMethod(method.getName(), result)) {
                            logger.debug("属性:[{}]匹配成功", method.getName());
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
    public boolean stringCheck(String param) {
        logger.info("开始对[{}]执行匹配操作", param);
        if (null != param) {
            for (String checkParam : checkParams) {
                //判断value中是否有checkParams中对应的值
                if ((checkParam.equals(param.toLowerCase()))) {
                    logger.debug("字符串:[{}]匹配成功", param);
                    return true;
                }
            }
        }
        return false;
    }
}
