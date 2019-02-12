package utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Json返回格式约定类
 *
 * @author tanxianwen 2016年9月5日
 */
public class JsonResponse implements Serializable {

    private static final long serialVersionUID = -6267859767710893271L;

    // 由于返回给前端的data字段不能为null,默认给个空对象
    private static final Object EMPTY = new Object();

    private Integer code = 1; // 大于0表示成功，小于0表示失败。-1表示普通失败，其他负数表示特定失败
    private String message = "";
    private Object data = EMPTY;

    public static JsonResponse success() {
        return new JsonResponse(StatCode.SUCCESS);
    }

    public static JsonResponse success(Object bean) {
        return success().add(bean);
    }

    public static JsonResponse success(String key, Object value) {
        return success().put(key, value);
    }

    public static JsonResponse failed() {
        return new JsonResponse(StatCode.FAILED);
    }

    public static JsonResponse failed(String msg) {
        return failed().setMsg(msg);
    }

    /**
     * 解析spring mvc中BindingResult的错误结果
     *
     * @param result result
     * @return JsonResponse
     */
    public static JsonResponse failed(BindingResult result) {
        JsonResponse resp = failed();
        if (result.hasFieldErrors()) {
            // for (FieldError err : result.getFieldErrors()) {
            // resp.put(err.getField(), err.getDefaultMessage());
            // }
            FieldError err = result.getFieldError();
            String msg = err.getDefaultMessage();
            if (StringUtils.contains(err.getCode(), TypeMismatchException.ERROR_CODE)) {
                msg = "参数" + err.getField() + "的值[" + err.getRejectedValue() + "]格式不正确，请检查参数格式!";
            }
            resp.setMsg(msg);
        }
        if (result.hasGlobalErrors()) {
            // for (ObjectError err : result.getGlobalErrors()) {
            // resp.put(err.getObjectName(), err.getDefaultMessage());
            // }
            resp.setMsg(result.getGlobalError().getDefaultMessage());
        }
        return resp;
    }

    public static JsonResponse failed(Integer code, String msg) {
        return failed().setCode(code).setMsg(msg);

    }

    public JsonResponse setCode(Integer code) {
        this.code = code;
        return this;
    }

    public JsonResponse setMsg(String msg) {
        this.message = msg;
        return this;
    }

    /**
     * 从json规范的角度来说，如果add到data的值是非json object(如数字、数组、字符串、布尔)类型， 则后续不能在添加不符合json规范的类型。如：
     * add(list).add(map), add(list).add(number)都会抛出异常。 但 add(list).add(list),
     * add(map).add(map)不会抛出异常。
     * 
     * @param bean bean
     * @return JsonResponse
     */
    public JsonResponse add(Object bean) {
        if (null == bean) {
            return this;
        }

        if (bean instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) bean;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                put(entry.getKey().toString(), entry.getValue());
            }
        } else if (bean instanceof Collection) {
            Collection<?> coll = (Collection<?>) bean;
            getDataAsList().addAll(coll);
        } else if (bean.getClass().isArray()) {
            List<Object> list = getDataAsList();
            int len = Array.getLength(bean);
            for (int i = 0; i < len; i++) {
                list.add(Array.get(bean, i));
            }
        } else if (bean instanceof Number || bean instanceof String || bean instanceof Boolean) {
            if (EMPTY == data) {
                data = bean;
            } else {
                throw new IllegalStateException("不能将" + bean.getClass().getSimpleName() + "类型追加到" + data.getClass().getSimpleName() + "类型");
            }
        } else { // 为了利用JSON注解，采用JSON接口来处理
            JSONObject o = (JSONObject) JSON.toJSON(bean);
            getDataAsMap().putAll(o);
        }
        return this;
    }

    @SuppressWarnings({"unchecked"})
    private Map<String, Object> getDataAsMap() {
        if (EMPTY == data) {
            data = new HashMap<String, Object>();
            return (Map<String, Object>) data;
        }
        if (data instanceof Map) {
            return (Map<String, Object>) data;
        }
        throw new IllegalStateException("数据类型不兼容，不能将" + data.getClass().getSimpleName() + "类型转成Map类型");
    }

    @SuppressWarnings("unchecked")
    private List<Object> getDataAsList() {
        if (EMPTY == data) {
            data = new ArrayList<Object>();
            return (List<Object>) data;
        }
        if (data instanceof List) {
            return (List<Object>) data;
        }
        throw new IllegalStateException("数据类型不兼容，不能将" + data.getClass().getSimpleName() + "类型转成List类型");
    }

    public JsonResponse put(String key, Object value) {
        this.getDataAsMap().put(key, value);
        return this;
    }

    public JsonResponse putAll(Map<String, Object> map) {
        this.getDataAsMap().putAll(map);
        return this;
    }

    public Integer getCode() {
        return code;
    }

    // public String getMsg() {
    // return message;
    // }

    public Object getData() {
        return data;
    }

    public JsonResponse(StatCode statCode) {
        this.code = statCode.getCode();
        this.message = statCode.getMsg();
    }

    enum StatCode {
        SUCCESS(1, "ok"),
        FAILED(-1, "请求失败！");
        private int code;
        private String msg;

        StatCode(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }

    public String getMessage() {
        return message;
    }
}
