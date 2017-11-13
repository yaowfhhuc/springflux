package me.spring.act.reactor.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import me.spring.act.reactor.dao.UserDao;
import me.spring.act.reactor.pojo.UserInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {

	@Autowired
	private UserDao userDao;
	
	/**GET method
	 * http://localhost:80/route
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> getUser(ServerRequest request) {
		return ServerResponse.ok().body(userDao.getUsers(),UserInfo.class);
	}
	
	/**GET method
	 * http://localhost:80/route/12345
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> getByUserId(ServerRequest request){
		
		return userDao.findOne(request.pathVariable("id"))
				.flatMap(user -> ServerResponse.ok().body(Mono.just(user),UserInfo.class))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	/**
	 * GET method
	 * http://localhost:80/route/delete/123
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> deleteUser(ServerRequest request){
		return userDao.delete(request.pathVariable("id"))
				.flatMap(bool->ServerResponse.ok().body(Mono.just(bool),Integer.class))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
}
