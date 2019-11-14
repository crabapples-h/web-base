package cn.crabapples.dto;

import com.alibaba.fastjson.JSONObject;

import javax.validation.constraints.NotBlank;

/**
 * TODO 基础请求参数
 *
 * @author Mr.He
 * @date 2019/7/19 0:31
 * e-mail wishforyou.xia@gmail.com
 * pc-name 29404
 */
public class BaseDTO {
    @NotBlank(message = "服务id不能为空")
    private String code;
    @NotBlank(message = "时间戳不能为空")
    private String timeStamp;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
