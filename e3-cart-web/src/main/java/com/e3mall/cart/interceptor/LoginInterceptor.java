package com.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.utils.CookieUtils;
import com.e3mall.pojo.TbUser;
import com.e3mall.sso.service.UserService;

/*
 * 用户登录的拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;
	@Autowired
	private UserService userService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		//1.从cookie中取用户token
		String token = CookieUtils.getCookieValue(request, COOKIE_TOKEN_KEY);
		//2.判断token是否存在
		if(StringUtils.isBlank(token)){
			//3.不存在，没登录，放行
			return true;
		}
		//4.存在，调用sso服务，通过token查询用户信息
		E3Result result = userService.getUserByToken(token);
		//5.判断用户信息，是否失效
		if(result.getStatus()!=200){
			//6.没有返回200，登录失效,放行
			return true;
		}
		//7.返回200，取出用户信息，存放到request中
		TbUser user = (TbUser) result.getData();
		request.setAttribute("user", user);
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
