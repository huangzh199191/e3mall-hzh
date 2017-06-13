package com.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.cart.service.CartService;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.utils.CookieUtils;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbUser;
import com.e3mall.service.ItemService;

/*
 * 购物车controller
 */
@Controller
public class CartController {
	
	@Value("${E3_CART}")
	private String E3_CART;
	@Value("${CART_EXPIRE}")
	private Integer CART_EXPIRE;
	@Autowired
	private ItemService itemService;
	@Autowired
	private CartService cartService;
	/*
	 * 将商品添加到购物车
	 * url:/cart/add/{itemId}
	 * 参数: 商品id itemId  商品数量 num
	 * 返回值 逻辑视图 
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addCartItem(@PathVariable Long itemId,Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//判断用户是否登录，如果登录，调用cartService服务，如果未登录，执行web中的程序
		//根据拦截器，登录成功后用户信息保存到request
		TbUser user = (TbUser) request.getAttribute("user");
		//登录成功，购物车保存到redis
		if(user!=null){
			E3Result result = cartService.addItemCart(user.getId(), itemId, num);
			return "cartSuccess";
		}
		//未登录,购物车信息保存到cookie
		//1.从cookie中取出购物车商品列表
		List<TbItem> cartList = getCartList(request);
		//2.判断该商品是否在商品列表中存在
		boolean hasItem = false;
		for (TbItem tbItem : cartList) {
			if(tbItem.getId()==itemId.longValue()){
				//3.如果存在，增加商品数量
				tbItem.setNum(tbItem.getNum()+num);
				hasItem = true;
				break;
			}
		}
		//4.如果不存在，根据商品id查询商品信息
		if(!hasItem){
			TbItem item = itemService.getItemById(itemId);
			//设置购买数量
			item.setNum(num);
			//取一张照片
			String images = item.getImage();
			if(StringUtils.isNotBlank(images)){
				String[] imageArr = images.split(",");
				item.setImage(imageArr[0]);
			}
			//5.把商品添加到购物车列表
			cartList.add(item);
		}
		//6.将购物车写入cookie
		CookieUtils.setCookie(request, response, E3_CART, JsonUtils.ObjectToJson(cartList),
				CART_EXPIRE, true);
		return "cartSuccess";
	}
	/*
	 * 从cookie中取出购物车列表
	 */
	private List<TbItem> getCartList(HttpServletRequest request) {
		String cookieValue = CookieUtils.getCookieValue(request, E3_CART, true);
		//如果cookie存在
		if(StringUtils.isNotBlank(cookieValue)){
			List<TbItem> list = JsonUtils.jsonToObjectList(cookieValue, TbItem.class);
			return list;
		}
		//cookie存在,返回空的集合
		return new ArrayList<>();
	}
	/*
	 * 展示我的购物车
	 * url /cart/cart
	 * 参数 无
	 * 返回值 逻辑视图
	 */
	@RequestMapping("/cart/cart")
	public String showCart(HttpServletRequest request,
			HttpServletResponse response ,Model model){
		//从cookie中取出商品列表
		List<TbItem> cartList = getCartList(request);
		//判断登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		//登录成功
		if(user!=null){
			//判断cookie中是否有购物车信息
			if(cartList!=null && cartList.size()>0){
				//合并购物车
				cartService.megerCart(user.getId(), cartList);
				//删除cookie中的购物车
				CookieUtils.deleteCookie(request, response, E3_CART);
			}
			//取出redis中的购物车
			List<TbItem> list = cartService.getRedisCart(user.getId());
			//返回给页面
			request.setAttribute("cartList", list);
			return "cart";
		}
		//未登录状态下
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	/*
	 * 在购物车列表上修改商品数量，重新计算总价
	 * url /cart/update/num/{itemId}/{num}
	 * 参数 商品id  修改后的数量num
	 * 返回值 E3Result
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartItemNum(@PathVariable Long itemId,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//判断是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		//登录状态
		if(user!=null){
			cartService.updateCartItemNum(user.getId(), itemId, num);
			//返回值
			return E3Result.ok();
		}
		//未登录状态
		//1.接受两个参数
		//2.从cookie中取出购物车列表
		List<TbItem> cartList = getCartList(request);
		//3.根据商品id找到购物车中的商品
		for (TbItem tbItem : cartList) {
			if(tbItem.getId()==itemId.longValue()){
				//4.修改商品数量
				tbItem.setNum(num);
				break;
			}
		}
		//5.重新写入cookie
		CookieUtils.setCookie(request, response, E3_CART, JsonUtils.ObjectToJson(cartList), CART_EXPIRE, true);
		//6.返回结果
		return E3Result.ok();
	}
	/*
	 * 从购物车中删除商品
	 * url /cart/delete/{itemId}
	 * 参数 商品id
	 * 返回值 逻辑视图，重定向到购物车页面
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteItemFromCart(@PathVariable Long itemId,
			HttpServletRequest request,HttpServletResponse response){
		//判断登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		//登录状态
		if(user!=null){
			//调用服务
			cartService.deleteItemFromCart(user.getId(), itemId);
			//返回
			return "redirect:/cart/cart.html";
		}
		//未登录状态
		//1.接受参数
		//2.从取出cookie中的购物车列表
		List<TbItem> cartList = getCartList(request);
		//3.遍历，找到对应商品
		for (TbItem tbItem : cartList) {
			if(tbItem.getId()==itemId.longValue()){
				//4.删除商品
				cartList.remove(tbItem);
				break;
			}
		}
		//5.重写写入cookie
		CookieUtils.setCookie(request, response, E3_CART, 
				JsonUtils.ObjectToJson(cartList),
				CART_EXPIRE, true);
		//6.返回值
		return "redirect:/cart/cart.html";
	}
}
