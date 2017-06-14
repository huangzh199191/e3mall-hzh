package com.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.e3mall.cart.service.CartService;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.order.pojo.OrderInfo;
import com.e3mall.order.service.OrderService;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbUser;

/*
 * 订单controller
 */
@Controller
public class OrderController {
	
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	/*
	 * 把购物车提交到订单,展示订单详情
	 * url /order/order-cart
	 * 参数 无
	 * 返回值 逻辑视图
	 */
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request){
		//取用户信息,经过拦截器,用户信息保存到request中
		TbUser user = (TbUser) request.getAttribute("user");
		//获得购物车列表
		List<TbItem> cartList = cartService.getRedisCart(user.getId());
		//返回jsp
		request.setAttribute("cartList",cartList );
		return "order-cart";
	}
	/*
	 * 生成订单
	 * url  /order/create
	 * 参数 无
	 * 返回值  E3Result 包装订单号 逻辑视图
	 */
	@RequestMapping("/order/create")
	public String createOrder(OrderInfo orderInfo,HttpServletRequest request){
		//获得用户信息
		TbUser user = (TbUser) request.getAttribute("user");
		
		//调用服务，生成订单
		E3Result result = orderService.createOrder(orderInfo,user);
		//获得订单id
		String orderId  = result.getData().toString();
		//设置返回值
		request.setAttribute("orderId", orderId);
		request.setAttribute("payment", orderInfo.getPayment());
		//返回逻辑视图
		return "success";
	}
	
}
