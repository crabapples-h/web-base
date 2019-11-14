package cn.crabapples.commons;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.List;

/**
 * TODO 验证Bean数据完整性工具
 *
 * @author Mr.He
 * @date 2019/7/19 0:28
 * e-mail wishforyou.xia@gmail.com
 * pc-name 29404
 */
@Component
public class BeanValidatorUtils {

    /**
     * 验证Bean实例对象
     */
    @Autowired
    private Validator validator;

    /**
     * 服务端参数有效性验证
     *
     * @param object 验证的实体对象
     * @param groups 验证组
     */
    public void beanValidator(Object object, Class<?>... groups) throws ValidationException {
        try {
            BeanValidators.validateWithException(validator, object, groups);
        } catch (ConstraintViolationException ex) {
            List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
            String msg = list == null || list.size() == 0 ? "数据校验失败" : list.get(0);
            if (msg.contains(":")) {
                String[] arr = msg.split(":");
                if (arr.length > 1) {
                    msg = arr[1];
                }
            }
            throw new ValidationException(msg);
        }
    }

}