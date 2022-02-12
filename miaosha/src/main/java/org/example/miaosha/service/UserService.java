package org.example.miaosha.service;

import org.example.miaosha.error.BusinessException;
import org.example.miaosha.service.Model.UserModel;

public interface UserService {

    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;

    /*
    number:用户注册手机
    password:用户加密后的密码
     */
    UserModel validateLogin(String number, String encrptPassword) throws BusinessException;
}
