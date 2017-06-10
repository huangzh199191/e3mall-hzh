package com.e3mall.sso.service;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.pojo.TbUser;

public interface UserService {
	E3Result checkUserData(String param,Integer type);

	E3Result addUser(TbUser user);
	
	E3Result login(String username,String password);
	
	E3Result getUserByToken(String token);
	
	E3Result logout(String token);
}
