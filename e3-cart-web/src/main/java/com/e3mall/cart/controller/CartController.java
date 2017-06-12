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

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.utils.CookieUtils;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.pojo.TbItem;
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
	/*
	 * 将商品添加到购物车
	 * url:/cart/add/{itemId}
	 * 参数: 商品id itemId  商品数量 num
	 * 返回值 逻辑视图 
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addCartItem(@PathVariable Long itemId,Integer num,
			HttpServletRequest request,HttpServletResponse response){
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
	public String showCart(HttpServletRequest request,Model model){
		List<TbItem> cartList = getCartList(request);
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
