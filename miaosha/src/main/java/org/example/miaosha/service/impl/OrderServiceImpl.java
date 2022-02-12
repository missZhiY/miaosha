package org.example.miaosha.service.impl;

import org.example.miaosha.dao.OrderDOMapper;
import org.example.miaosha.dao.SequenceDOMapper;
import org.example.miaosha.dataObject.OrderDO;
import org.example.miaosha.dataObject.SequenceDO;
import org.example.miaosha.error.BusinessException;
import org.example.miaosha.error.EmBusinessError;
import org.example.miaosha.service.ItemService;
import org.example.miaosha.service.Model.ItemModel;
import org.example.miaosha.service.Model.OrderModel;
import org.example.miaosha.service.Model.UserModel;
import org.example.miaosha.service.OrderService;
import org.example.miaosha.service.UserService;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDOMapper orderDOMapper;

    @Autowired
    private SequenceDOMapper sequenceDOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException {
        //入参校验，校验下单状态，下单商品是否存在，用户是否合法，购买数量是否正确
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品信息不存在");
        }

        UserModel userModel = userService.getUserById(userId);
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户信息不存在");
        }

        if (amount < 1 || amount > 99) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "购买数量需大于0且小于100");
        }
        //校验活动信息
        if (promoId != null) {
            //校验对应活动是否包含商品
            if (promoId.intValue() != itemModel.getPromoModel().getId()) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "该商品不参与活动");
                //进一步校验活动是否正在进行
            } else if (itemModel.getPromoModel().getStatus().intValue() != 2) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动未开始");
            }
        }

        //落单减库存（本项目采用） || 支付减库存
        boolean result = itemService.decreaseStock(itemId, amount);
        if (!result) {
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        //订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setAmount(amount);
        orderModel.setItemId(itemId);
        orderModel.setUserId(userId);
        orderModel.setPromoId(promoId);
        if (promoId != null) {
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        } else {
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));
        //按一定规则生成交易流水号(主键id)
        orderModel.setId(generateOderNumber());
        //model -> dataObject
        OrderDO orderDO = convertOrderDOFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);
        //算上商品销量
        itemService.increaseSales(itemId, amount);
        //返回前端
        return orderModel;
    }

//    public static void main(String[] args) {
//        LocalDateTime now = LocalDateTime.now();
//        System.out.println(now.format(DateTimeFormatter.ISO_DATE).replace("-",""));
//    }

    //规定订单号16位
    //因创建订单打了Transactional标签，sequence的生成也被包装在该方法中，
    //若当其某一步出现错误，在回滚事务的过程中，sequence生成操作也会被回滚，
    //则可能导致下一次生成的sequence和回滚前的相同，而实际中为保证全局唯一性，sequence生成操作不应该回滚
    //使用@Transactional(propagation = Propagation.REQUIRES_NEW)解决
    /*
     *     REQUIRED(0),
     *     SUPPORTS(1),
     *     MANDATORY(2),
     *     REQUIRES_NEW(3),
     *     NOT_SUPPORTED(4),
     *     NEVER(5),
     *     NESTED(6);
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    String generateOderNumber() {
        StringBuilder stringBuilder = new StringBuilder();
        //前8位为时间信息：年月日
        //当总数据量超出数据库存储范围时，可根据前八位对一定时间之前的数据进行归档
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);
        //中间6位为自增序列
        //获取当前sequence
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        sequence = sequenceDO.getCurrentValue();
        //本项目默认sequence最大为6位，但sequence应该考虑最大值问题，当CurrentValue + Step大于最大值时需要按一定规则处理
        //eg:在sequence表中添加max，default两列，分别存放sequence的最大值和默认值，当超出最大值则重置为默认值，即循环sequence
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue() + sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        //拼接sequence
        String sequenceStr = String.valueOf(sequence);
        for (int i = 0; i < 6 - sequenceStr.length(); i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);
        //最后2位为分库分表位，本项目暂时写死
        //根据用户id或其他信息按一定规则对订单进行分流，落入不同库的不同表中，
        //分散数据库的查询和追加的落单压力
        //eg:按userId分流，定义规则userId % 99，保证用户产生的订单落入对应的分库分表中
        stringBuilder.append("01");
        return stringBuilder.toString();
    }

    private OrderDO convertOrderDOFromOrderModel(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel, orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDO;
    }

}
