package org.example.miaosha.service;

import org.example.miaosha.error.BusinessException;
import org.example.miaosha.service.Model.ItemModel;

import java.util.List;

public interface ItemService {

    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    //商品列表浏览
    List<ItemModel> listItem();

    //商品详情浏览
    ItemModel getItemById(Integer id);

    //库存扣减，开给OrderService使用
    //需要加锁
    boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException;

    //商品销量增加，暂不考虑支付情况，假设落单成功即销量增加
    //不用加锁
    void increaseSales(Integer itemId, Integer amount) throws BusinessException;
}
