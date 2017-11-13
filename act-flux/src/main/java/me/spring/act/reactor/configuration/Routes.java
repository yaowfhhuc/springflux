package me.spring.act.reactor.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import me.spring.act.reactor.handler.UserHandler;

@Configuration
public class Routes {

	@Bean
	public RouterFunction<ServerResponse> monoRouterFunction(UserHandler userHandler) {
		return RouterFunctions.route(RequestPredicates.GET("/route").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userHandler::getUser)
				.andRoute( RequestPredicates.GET("/route/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userHandler::getByUserId)
				.andRoute(RequestPredicates.GET("/route/delete/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userHandler::deleteUser);
	}
}
