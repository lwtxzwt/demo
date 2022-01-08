package com.lwtxzwt.demo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
class DemoApplicationTests {

	@Autowired
	private RestTemplate restTemplate;

	@Test
	public void test1(){
		System.out.println(restTemplate.getForObject("http://localhost:8080/api/demo/getTemperature?province=江苏&city=苏州&county=吴中", String.class));
	}

	@Test
	public void test2(){
		System.out.println(restTemplate.getForObject("http://localhost:8080/api/demo/getTemperature?province=江苏&city=苏州&county=111", String.class));
	}

	@Test
	public void test3(){
		System.out.println(restTemplate.getForObject("http://localhost:8080/api/demo/getTemperature?province=江苏&city=111&county=111", String.class));
	}

	@Test
	public void test4(){
		System.out.println(restTemplate.getForObject("http://localhost:8080/api/demo/getTemperature?province=111&city=111&county=111", String.class));
	}

	// 并发测试通过jmeter实现

}
