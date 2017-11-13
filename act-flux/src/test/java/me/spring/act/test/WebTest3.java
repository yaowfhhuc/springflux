package me.spring.act.test;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import me.spring.act.reactor.pojo.UserInfo;

@AutoConfigureWebFlux
@SpringBootTest
public class WebTest3 {
	
	@Autowired
	private WebTestClient webTestClient;

	@Before
	public void init() {
		webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:80").build();
	}
	
	@Test
	public void test() throws IOException {
		FluxExchangeResult<UserInfo> result = webTestClient.get().uri("route").accept(MediaType.APPLICATION_JSON)
				.exchange().returnResult(UserInfo.class);
		assert result.getStatus().value() == 200;
		List<UserInfo> users = result.getResponseBody().collectList().block();
		assert users.size() == 2;
		assert users.iterator().next().getId().equals("123");
	}


	@Test
	public void test1() throws IOException {
		UserInfo user = webTestClient.get().uri("/route/12345").accept(MediaType.APPLICATION_JSON).exchange()
				.returnResult(UserInfo.class).getResponseBody().blockFirst();
		assert user.getId().equals("12345");
	}

	@Test
	public void test2() throws IOException {
		UserInfo user = webTestClient.get().uri("/get?id=1234565").accept(MediaType.APPLICATION_JSON).exchange()
				.returnResult(UserInfo.class).getResponseBody().blockFirst();
		assert user.getId().equals("1234565");
	}
	
	@Test
	public void test3() throws IOException {
		UserInfo user = webTestClient.get().uri("/get/1234565").accept(MediaType.APPLICATION_JSON).exchange()
				.returnResult(UserInfo.class).getResponseBody().blockFirst();
		assert user.getId().equals("1234565");
	}
}
