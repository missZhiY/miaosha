package org.example.miaosha.service.Model;

import java.math.BigDecimal;

//用户下单的交易模型
public class OrderModel {
    //单号
    private String id;

    //若非空，则表示以秒杀商品价格下单
    private Integer promoId;

    //购买时商品单价，若promoId非空，则表示活动价格
    private BigDecimal itemPrice;

    //用户id
    private Integer userId;

    //商品id
    private Integer itemId;

    //购买数量
    private Integer amount;

    //购买金额，若promoId非空，则表示以活动价格购买的订单金额
    private BigDecimal orderPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }
}
