package com.mr.test;

import com.mr.entity.User;
import com.mr.service.impl.SolrUserServiceImpl;

public class Test {

	public static void main(String[] args) {
		User user = new User();
		user.setCustNo("1");
		user.setEmail("275722000@qq.com");
		user.setUsername("袁玉印");
		user.setAge(28);
		SolrUserServiceImpl userService = new SolrUserServiceImpl();
		userService.createUserIndex(user);
		
	}
}
