import java.lang.annotation.Annotation;
import java.util.*;

import cn.crabapples.annotations.Crabapples;
import cn.crabapples.utils.FieldCheckUtils;
import entity.User;
import org.junit.Before;
import org.junit.Test;


/**
 * TODO 注解使用测试类
 *
 * @author Mr.He
 * @date 2019/11/17 3:37
 * e-mail wishforyou.xia@gmail.com
 * qq 294046317
 * pc-name 29404
 */
public class AnnotationTest {
    private String[] checkParams = {" ", "", "[]", "{}", "undefined", "null", "[object Object]"};
    private Map<String, Object> map = new LinkedHashMap<String, Object>();
    private User user = new User();
    private FieldCheckUtils fieldCheckUtils = new FieldCheckUtils(checkParams);

    @Before
    public void init() {
        List<String> list = new ArrayList<String>();
        list.add("1");
        Map<String, String> map1 = new HashMap<String, String>();
        map.put("user_id", "这个是用户ID");
        map.put("status", "undefined");
        map.put("list", list);
        map.put("map", map1);

        List<String> interest = new ArrayList<String>();
        interest.add("12");

        user.setId("undefined");
        user.setName("uNdefined");
        user.setSex("sex_123456789");
        user.setInterest(interest);

    }

    /**
     * 根据注解进行相应验证测试
     */
    @Test
    public void entityCheckByAnnotation() throws Exception {
        boolean status = fieldCheckUtils.entityCheckByAnnotation(user, Crabapples.class);
        System.err.println("验证结果：" + status);
    }

    /**
     * 对验证数组中包含的参数名进行相应验证测试
     */
    @Test
    public void entityCheckByArray() throws Exception {
        String[] params = {"id"};
        boolean status = fieldCheckUtils.entityCheckByArray(user, params);
        System.err.println("验证结果：" + status);
    }


    @Test
    public void stringCheck() {
        boolean status = fieldCheckUtils.stringCheck("uNdefined");
        System.err.println("验证结果：" + status);
    }
}