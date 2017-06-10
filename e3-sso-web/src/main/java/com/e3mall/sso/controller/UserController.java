package com.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.utils.CookieUtils;
import com.e3mall.pojo.TbUser;
import com.e3mall.sso.service.UserService;

/*
 * 用户管理controller
 */
@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;
	/*
	 * 核查用户数据
	 * url /user/check/{param}/{type}
	 * 参数  {param} 要核查的用户数据   username  phone  emaill
	 *     {type}               1          2     3
	 * 返回值 E3Result    
	 */
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result checkUserData(@PathVariable String param,@PathVariable Integer type){
		E3Result result = userService.checkUserData(param, type);
		return result;
	}
	
	/*
	 * 页面跳转
	 */
	@RequestMapping("/page/{page}")
	public String showPage(@PathVariable String page){
		return page;
	}
	/*
	 * 用户注册
	 * url /user/register
	 * 参数 	tbUser
	 * 返回值 E3Result
	 */
	@RequestMapping("/user/register")
	@ResponseBody
	public E3Result addUser(TbUser user){
		E3Result result = userService.addUser(user);
		return result;
	}
	/*
	 * 用户登录
	 * url /user/login
	 * 参数 username password
	 * 返回值 E3Result
	 */
	@RequestMapping("/user/login")
	@ResponseBody
	public E3Result login(String username,String password,
			HttpServletRequest request,
			HttpServletResponse response){
		//查询用户
		E3Result result = userService.login(username, password);
		//登录成功
		//获得用户口令token,存入session
		if(result.getData()!=null){
			String token = result.getData().toString();
			CookieUtils.setCookie(request, response, COOKIE_TOKEN_KEY, token);
		}
		//设置返回值,登录成功时包含token
		return result;
	}
	/*
	 * 根据token获取用户信息
	 * url /user/token/{token}
	 * 参数   token
	 * 返回值 E3Result  包装TbItem
	 */
	@RequestMapping("/user/token/{token}")
	@ResponseBody
	public E3Result getUserByToken(@PathVariable String token){
		E3Result result = userService.getUserByToken(token);
		return result;
	}
	
	/*
	 * 安全退出
	 * url /user/logout/{token}
	 * 参数 token
	 * 返回值 E3Result
	 */
	@RequestMapping("/user/logout/{token}")
	@ResponseBody
	public E3Result logout(@PathVariable String token){
		E3Result result = userService.logout(token);
		return result;
	}
}
