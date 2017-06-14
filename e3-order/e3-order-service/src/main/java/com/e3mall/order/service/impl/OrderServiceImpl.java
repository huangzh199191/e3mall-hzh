package com.e3mall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e3mall.common.jedis.JedisClient;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.dao.TbOrderItemMapper;
import com.e3mall.dao.TbOrderMapper;
import com.e3mall.dao.TbOrderShippingMapper;
import com.e3mall.order.pojo.OrderInfo;
import com.e3mall.order.service.OrderService;
import com.e3mall.pojo.TbOrderItem;
import com.e3mall.pojo.TbOrderShipping;
import com.e3mall.pojo.TbUser;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private JedisClient jedisClient;
	@Value("${ORDER_GEN_ID}")
	private String ORDER_GEN_ID;
	@Value("${ORDER_GEN_BEGIN}")
	private String ORDER_GEN_BEGIN;
	@Value("${ORDER_ITEM_ID}")
	private String ORDER_ITEM_ID;
	@Value("${CART_REDIS_KEY}")
	private String CART_REDIS_KEY;
	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	//生成订单
	public E3Result createOrder(OrderInfo orderInfo,TbUser user) {
		//1.提交到订单表
		//1）生成订单id(用redis的incr)
		if(!jedisClient.exists(ORDER_GEN_ID)){
			//赋初始值
			jedisClient.set(ORDER_GEN_ID, ORDER_GEN_BEGIN);
		}
		String orderId = jedisClient.incr(ORDER_GEN_ID).toString();
		//2)补全属性
		orderInfo.setOrderId(orderId);
		//补全属性
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		//1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		//3)提交到tb_order表
		orderMapper.insert(orderInfo);
		//2.提交到订单商品表(订单明细)
		//获得订单商品集合
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		//遍历，补全属性
		for (TbOrderItem tbOrderItem : orderItems) {
			tbOrderItem.setId(jedisClient.incr(ORDER_ITEM_ID).toString());
			tbOrderItem.setOrderId(orderId);
			//插入数据
			orderItemMapper.insert(tbOrderItem);
		}
		//3.提交到订单物流表
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		//插入数据
		orderShippingMapper.insert(orderShipping);
		//4.返回值
		return E3Result.ok(orderId);
	}

}
