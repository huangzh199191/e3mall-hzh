package com.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.e3mall.common.jedis.JedisClient;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.dao.TbUserMapper;
import com.e3mall.pojo.TbUser;
import com.e3mall.pojo.TbUserExample;
import com.e3mall.pojo.TbUserExample.Criteria;
import com.e3mall.sso.service.UserService;

@Service
/*
 * 用户管理service
 */
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${USER_SESSION}")
	private String USER_SESSION;
	@Value("${SESSION_EXPIRE}")
	private int SESSION_EXPIRE;
	
	//检查用户数据
	public E3Result checkUserData(String param, Integer type) {
		
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		//1.用户名  2.电话   3.邮箱
		if(type==1){
			criteria.andUsernameEqualTo(param);
		}else if(type==2){
			criteria.andPhoneEqualTo(param);
		}else if(type==3){
			criteria.andEmailEqualTo(param);
		}else{
			return E3Result.build(400, "参数不正确");
		}
		//执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		//如果查询到数据，返回false,没有查询到，返回true
		if(list==null || list.size()==0){
			//没有查询到数据,数据可用
			return E3Result.ok(true);
		}
		//数据不可用
		return E3Result.ok(false);
	}
	//用户注册
	public E3Result addUser(TbUser user) {
		//判断必填项
		if(StringUtils.isBlank(user.getUsername())){
			return E3Result.build(400, "用户名不能为空");
		}
		if(StringUtils.isBlank(user.getPassword())){
			return E3Result.build(400, "用户密码不能为空");
		}
		//判断用户名、电话、邮箱是否已经存在
		E3Result result = checkUserData(user.getUsername(), 1);
		if(!(boolean) result.getData()){
			return E3Result.build(400, "用户名已存在");
		}
		if(StringUtils.isNotBlank(user.getPhone())){
			E3Result result2 = checkUserData(user.getPhone(), 2);
			if(!(boolean) result2.getData()){
				return E3Result.build(400, "电话已经存在");
			}
		}
		if(StringUtils.isNotBlank(user.getEmail())){
			E3Result result3 = checkUserData(user.getEmail(), 3);
			if(!(boolean) result3.getData()){
				return E3Result.build(400, "邮箱已存在");
			}
		}
		
		//补充属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//密码进行MD5加密
		String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(password);
		//提交到数据库
		userMapper.insert(user);
		//返回
		return E3Result.ok();
	}
	//用户登录
	public E3Result login(String username, String password) {
		//1.判断用户名和密码是否正确
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//查询用户名
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		if(list==null||list.size()==0){
			//用户不存在，登录失败
			return E3Result.build(400, "用户名或者密码错误！！！");
		}
		//用户存在，获得用户
		TbUser tbUser = list.get(0);
		//校验密码
		//输入密码加密后和真正密码比较
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbUser.getPassword())){
			//密码错误
			return E3Result.build(400, "用户名或者密码错误！！！");
		}
		//2.用户名密码正确，登录成功,使用uuid生成token
		String token = UUID.randomUUID().toString();
		//3.保存用户信息到redis,key就是token,value就是用户对象的json串
		//为了保密，将密码设置为null
		tbUser.setPassword(null);
		jedisClient.set(USER_SESSION+":"+token, JsonUtils.ObjectToJson(tbUser));
		//4.设置redis失效时间
		jedisClient.expire(USER_SESSION+":"+token, SESSION_EXPIRE);
		//5.设置返回值
		//返回token,存放到session
		return E3Result.ok(token);
	}
	//通过token获得redis中（session共享）用户信息
	public E3Result getUserByToken(String token) {
		//获取用户信息json串
		String json = jedisClient.get(USER_SESSION+":"+token);
		if(StringUtils.isBlank(json)){
			return E3Result.build(400, "口令已失效");
		}
		//已经登录
		//获取用户信息
		TbUser tbUser = JsonUtils.jsonToObject(json, TbUser.class);
		//重新设置时效
		jedisClient.expire(USER_SESSION+":"+token, SESSION_EXPIRE);
		//返回值
		return E3Result.ok(tbUser);
	}
	//安全退出
	public E3Result logout(String token) {
		//通过token删除对象redis中对象信息
		jedisClient.expire(USER_SESSION+":"+token, 0);
		return E3Result.ok();
	}

}
