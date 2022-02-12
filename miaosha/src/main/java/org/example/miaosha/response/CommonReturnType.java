package org.example.miaosha.response;


//使用对应status和data返回所有json序列化方式的对象供前端解析使用
//摒弃了使用HttpStatusCode+内嵌Tomcat自带error页的处理方式
//同时定义了EmBusinessError统一管理返回的错误码，使用errCode + errMsg吃掉未定义的异常
//定义了BaseController基类，并实现通用的exceptionHandler解决未被controller层吸收的exception
public class CommonReturnType {

    //表示对应前端请求的返回处理结果：success | fail
    private String status;

    //若status = success, 则data内返回前端需要的json数据
    //若status = fail, 则返回通用的错误码
    private Object data;

    //定义一个通用的创建方法
    public static CommonReturnType create(Object result) {
        return CommonReturnType.create(result, "success");
    }

    public static CommonReturnType create(Object result, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
