package org.example.miaosha.error;

import java.io.BufferedReader;
import java.util.concurrent.CompletableFuture;

//包装器业务异常类实现
public class BusinessException extends Exception implements CommonError {

    private CommonError commonError;

    //构造方法, 直接接受EmBusinessError的传参, 用于构造业务异常
    public BusinessException(CommonError commonError) {
        super();
        this.commonError = commonError;
    }

    //接受自定义异常
    public BusinessException(CommonError commonError, String errMsg) {
        super();
        this.commonError = commonError;
        this.commonError.setErrorMsg(errMsg);
    }



    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrorMsg(String errMsg) {
        this.commonError.setErrorMsg(errMsg);
        return this;
    }
}
