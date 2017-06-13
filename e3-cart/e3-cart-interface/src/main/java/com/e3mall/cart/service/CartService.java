package com.e3mall.cart.service;

import java.util.List;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.pojo.TbItem;

public interface CartService {
	
	E3Result addItemCart(Long userId,Long itemId,Integer num);
	E3Result megerCart(Long userId,List<TbItem> cookieCartList);
	List<TbItem> getRedisCart(Long userId);
	E3Result updateCartItemNum(Long userId,Long itemId,Integer num);
	E3Result deleteItemFromCart(Long userId,Long itemId);
}
