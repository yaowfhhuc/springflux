package me.spring.act.reactor.dao;

import me.spring.act.reactor.pojo.UserInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface UserDao {
    Mono<Integer> save(UserInfo userInfo);
    Mono<Integer> update(UserInfo userInfo);
    Mono<UserInfo> findOne(String id);
    Flux<UserInfo> getUsers();
    Mono<Integer> delete(String id);
}
