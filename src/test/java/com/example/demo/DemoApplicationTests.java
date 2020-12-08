package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.controller.StepController;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private StepController stepController;

	@Test
	public void contextLoads() throws Exception {
		assertThat(stepController).isNotNull();
	}

}
