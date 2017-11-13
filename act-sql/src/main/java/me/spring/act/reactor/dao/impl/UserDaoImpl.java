package me.spring.act.reactor.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import me.spring.act.reactor.dao.UserDao;
import me.spring.act.reactor.pojo.UserInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Repository
public class UserDaoImpl implements UserDao{

	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
	@Autowired
	@Qualifier("ds1JdbcTemplate")
	private JdbcTemplate ds1JdbcTemplate;
	
	@Autowired
	@Qualifier("ds2JdbcTemplate")
	private JdbcTemplate ds2JdbcTemplate;
	
	
	@Autowired
	@Qualifier("ds3JdbcTemplate")
	private JdbcTemplate ds3JdbcTemplate;
	
	@Override
	public Mono<Integer> save(UserInfo userInfo) {
		return Mono.just(jdbcTemplate.update("insert into user_info(name,age) values (?,?)", new Object[]{userInfo.getName(),userInfo.getAge()}));
	}

	@Override
	public Mono<Integer> update(UserInfo userInfo) {
		return Mono.just(jdbcTemplate.update("update table user_info set name = ? ,age = ? where id= ?",
				new Object[]{userInfo.getName(),userInfo.getAge(),userInfo.getId()}));
	}

	@Override
	public Mono<UserInfo> findOne(String id) {
		List<UserInfo> list = jdbcTemplate.query("select * from user_info where id=?",
				new Object[]{id}, new RowMapper<UserInfo>() {
					UserInfo userInfo = null;
					@Override
					public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException  {
						userInfo = new UserInfo();
						userInfo.setAge(rs.getInt("AGE"));
						userInfo.setId(rs.getInt("ID")+"");
						userInfo.setName(rs.getString("NAME"));
						return userInfo;
					}
				});
		return Mono.just(list.get(0));
	}

	@Override
	public Mono<Integer> delete(String id) {
		return  Mono.just(ds1JdbcTemplate.update("delete from user_info where id=?", new Object[]{id})) ;
	}

	@Override
	public  Flux<UserInfo> getUsers() {
		return Flux.fromIterable(ds2JdbcTemplate.query("select * from user_info",new RowMapper<UserInfo>() {
			UserInfo userInfo = null;
			@Override
			public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException  {
				userInfo = new UserInfo();
				userInfo.setAge(rs.getInt("AGE"));
				userInfo.setId(rs.getInt("ID")+"");
				userInfo.setName(rs.getString("NAME"));
				return userInfo;
			}
		}));
	}

}
