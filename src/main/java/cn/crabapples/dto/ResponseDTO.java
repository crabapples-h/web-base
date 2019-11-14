package cn.crabapples.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.text.SimpleDateFormat;

/**
 * TODO 通用返回DTO
 *
 * @author Mr.He
 * @date 2019/7/19 0:31
 * e-mail wishforyou.xia@gmail.com
 * pc-name 29404
 */
public class ResponseDTO<T> {

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
    public static final Integer SUCCESS = 200;
    public static final Integer BAD_REQUEST = 400;
    public static final Integer ERROR = 500;
    public static final Integer NOT_FOUND = 404;
    public static final Integer NO_AUTH = 403;

    //服务器状态码
    private Integer status;
    //服务器返回的错误信息
    private String msg;
    //服务器返回的数据
    private T data;
    //服务器系统时间
    private String systemTime;

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    public String getSystemTime() {
        return systemTime;
    }
    public void setSystemTime(String systemTime) {
        this.systemTime = systemTime;
    }

    public ResponseDTO(Integer status, String msg, T data) {
        this();
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ResponseDTO() {
        this.systemTime = SIMPLE_DATE_FORMAT.format(System.currentTimeMillis());
    }

    /**
     * 出现错误时返回的数据
     * @param errorMsg 错误信息
     * @return 返回操作失败的DTO
     */
    public static ResponseDTO returnErrorResponse(String errorMsg) {
        return new ResponseDTO(ERROR, errorMsg, null);
    }

    public static ResponseDTO returnUnauthorizedExceptionResponse(String errorMsg) {
        return new ResponseDTO(NO_AUTH, errorMsg, null);
    }

    public static <T> ResponseDTO returnErrorResponse(String errorMsg, T data) {
        return new ResponseDTO(ERROR, errorMsg, data);
    }

    /**
     * 成功，不返回数据
     * @param msg 错误信息
     * @return 返回操作失败的DTO
     */
    public static ResponseDTO returnSuccessResponse(String msg) {
        return new ResponseDTO(SUCCESS, msg, null);
    }

    /**
     * 成功并返回数据
     * @param msg 返回信息
     * @param data 返回的数据
     * @param <T> 数据的类型(会被转换为json)
     * @return 返回操作成功的DTO
     */
    public static <T> ResponseDTO returnSuccessResponse(String msg, T data) {
        return new ResponseDTO(SUCCESS, msg, data);
    }

    /**
     * 错误的请求
     * @param msg 错误信息
     * @return 返回操作失败的DTO
     */
    public static ResponseDTO returnBadRequestResponse(String msg) {
        return new ResponseDTO(BAD_REQUEST, msg, null);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }

}
