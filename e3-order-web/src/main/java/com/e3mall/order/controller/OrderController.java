package com.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.e3mall.cart.service.CartService;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbUser;

/*
 * 订单controller
 */
@Controller
public class OrderController {
	
	@Autowired
	private CartService cartService;
	/*
	 * 把购物车提交到订单
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
	
}
