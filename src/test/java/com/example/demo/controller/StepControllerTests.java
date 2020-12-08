package com.example.demo.controller;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import com.example.demo.dto.ResultDto;
import com.example.demo.dto.TaskDto;
import com.example.demo.dto.ResultsDto;
import com.example.demo.dto.GenerateDto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StepControllerTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void getInProgressOnGenerare() throws Exception {
		GenerateDto generateDto = new GenerateDto(10, 2);

		String uuid = this.restTemplate
				.postForEntity("http://localhost:" + port + "/api/generate", generateDto, TaskDto.class).getBody().getTask();

		assertThat(!uuid.isEmpty());

		assertThat(this.restTemplate
				.getForEntity("http://localhost:" + port + "/api/task/" + uuid + "/status", ResultDto.class).getBody()
				.getResult().contains("IN_PROGRESS"));

		TimeUnit.SECONDS.sleep(40);

		assertThat(this.restTemplate
				.getForEntity("http://localhost:" + port + "/api/task/" + uuid + "/status", ResultDto.class).getBody()
				.getResult().contains("SUCCESS"));

		assertThat(this.restTemplate.getForEntity("http://localhost:" + port + "/api/task/" + uuid, ResultDto.class)
				.getBody().getResult().contains("10, 8, 6, 4, 2, 0"));
	}

	@Test
	public void getInProgressOnBulkGenerare() throws Exception {
		ArrayList<GenerateDto> generateDtoList = new ArrayList<GenerateDto>();
		generateDtoList.add(new GenerateDto(10, 2));
		generateDtoList.add(new GenerateDto(20, 2));

		String uuid = this.restTemplate
				.postForEntity("http://localhost:" + port + "/api/bulkGenerate", generateDtoList, TaskDto.class)
				.getBody().getTask();

		assertThat(!uuid.isEmpty());

		assertThat(this.restTemplate
				.getForEntity("http://localhost:" + port + "/api/task/" + uuid + "/status", ResultDto.class).getBody()
				.getResult().contains("IN_PROGRESS"));

		TimeUnit.SECONDS.sleep(40);

		assertThat(this.restTemplate
				.getForEntity("http://localhost:" + port + "/api/task/" + uuid + "/status", ResultsDto.class).getBody()
				.getResults().get(0).contains("SUCCESS"));

		assertThat(this.restTemplate.getForEntity("http://localhost:" + port + "/api/task/" + uuid, ResultsDto.class)
				.getBody().getResults().get(0).contains("10, 8, 6, 4, 2, 0"));
	}
}