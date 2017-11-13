package me.spring.act.reactor.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.spring.act.reactor.dao.UserDao;
import me.spring.act.reactor.pojo.UserInfo;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

	@Autowired
	private UserDao userDao;
	
	/**
	 * http://localhost:80/get?id=1234565
	 * @param id
	 * @return
	 */
	@GetMapping("/get")
	public Mono<UserInfo> getById(@RequestParam("id") String id){
		return this.userDao.findOne(id);
	}
	
	/**
	 * http://localhost:80/get/1234565
	 * @param id
	 * @return
	 */
	@GetMapping("/get/{id}")
	public Mono<UserInfo> getId(@PathVariable("id")String id){
		return this.userDao.findOne(id);
	}
	
}
