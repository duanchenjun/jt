package com.jt.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.service.UserService;
import com.jt.util.IPUtil;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 根据用户信息实现数据的校验
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param,@PathVariable Integer type,String callback) {
		boolean data=userService.checkUser(param,type);
		
		return new JSONPObject(callback,SysResult.success(data));
	}
	
	/**
	 * url:http://sso.jt.com/user/query/7ab89fedefa2de22541363c7d1068494?callback=jsonp1571282597276&_=1571282597440
	 */
	@RequestMapping("/query/{ticket}")
	public JSONPObject findUserByTicket(@PathVariable String ticket,String callback) {
		String userJSON=jedisCluster.get(ticket);
		if(StringUtils.isEmpty(userJSON)) {
			//用户使用的ticket有问题
			return new JSONPObject(callback, SysResult.fail());
		}
		return new JSONPObject(callback, SysResult.success(userJSON));
	}
	
	@RequestMapping("/query/{ticket}/{username}")
	public JSONPObject findUserByTicket(@PathVariable String ticket,
										@PathVariable String username,
										String callback,
										HttpServletRequest request) {
		//1.校验用户的IP地址
		String ip = IPUtil.getIpAddr(request);
		String localIP = jedisCluster.hget(username, "JT_IP");
		if(!ip.equalsIgnoreCase(localIP)) {
			return new JSONPObject(callback, SysResult.fail());
		}
		
		//2.校验ticket信息
		String localTicket = jedisCluster.hget(username, "JT_TICKET");
		if(!ticket.equalsIgnoreCase(localTicket)) {
			return new JSONPObject(callback, SysResult.fail());
		}
		
		//3.
		String userJSON = jedisCluster.hget(username, "JT_USERJSON");
		return new JSONPObject(callback, SysResult.success(userJSON));
	}
}
