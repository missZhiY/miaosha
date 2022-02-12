package org.example.miaosha.service;

import org.example.miaosha.service.Model.PromoModel;

public interface PromoService {

    //根据itemId获取即将进行的或正在进行的秒杀活动
    PromoModel getPromoByItemId(Integer itemId);

    //判断当前时间

}
