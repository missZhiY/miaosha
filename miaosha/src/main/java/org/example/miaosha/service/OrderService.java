package org.example.miaosha.service;

import org.example.miaosha.error.BusinessException;
import org.example.miaosha.service.Model.OrderModel;

public interface OrderService {

    //1.通过前端url传来的秒杀活动id，然后下单接口内校验对应id是否属于对应商品且活动已开始
    //2.直接在下单接口内判断对应商品是否存在秒杀活动，若存在且活动在进行中则以活动价格下单
    //两种方式各有优点，但第一种更合理。从业务逻辑的模型可扩展性角度考虑，
    //若一个商品存在多个秒杀活动，必须通过前端用户的使用路径访问
    OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException;

}
