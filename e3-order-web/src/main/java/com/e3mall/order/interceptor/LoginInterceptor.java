package com.e3mall.order.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.e3mall.cart.service.CartService;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.utils.CookieUtils;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbUser;
import com.e3mall.sso.service.UserService;

public class LoginInterceptor implements HandlerInterceptor {
	
	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;
	@Value("${SSO_URL}")
	private String SSO_URL;
	@Value("${E3_CART}")
	private String E3_CART;
	@Autowired
	private UserService userService;
	@Autowired
	private CartService cartService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		//1.取cookie中的用户token
		String token = CookieUtils.getCookieValue(request, COOKIE_TOKEN_KEY);
		//2.如果没有取到，cookie失效，跳转到sso系统的登录页面，拦截
		if(StringUtils.isBlank(token)){
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
			return false;
		}
		//3.如果取到token,调用sso服务，通过token取用户信息
		E3Result result = userService.getUserByToken(token);
		//4.没有取到用户信息，登录失败，跳转到登录页面，拦截
		if(result.getStatus()!=200){
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
			return false;
		}
		//5.取到用户信息，登录成功，将用户信息存到request中，放行
		TbUser user = (TbUser) result.getData();
		request.setAttribute("user", user);
		//6.判断cookie中是否有购物车信息
		String cookieValue = CookieUtils.getCookieValue(request, E3_CART, true);
		//7.如果有，合并购物车，清除cookie中的购物车
		if(StringUtils.isNotBlank(cookieValue)){
			List<TbItem> cartList = JsonUtils.jsonToObjectList(cookieValue, TbItem.class);
			cartService.megerCart(user.getId(), cartList);
			CookieUtils.deleteCookie(request, response, E3_CART);
		}
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
