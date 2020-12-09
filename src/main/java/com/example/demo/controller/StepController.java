package com.example.demo.controller;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.CompletableFutureConfig;
import com.example.demo.dto.GenerateDto;
import com.example.demo.dto.ResultDto;
import com.example.demo.dto.ResultsDto;
import com.example.demo.dto.SequenceDto;
import com.example.demo.dto.TaskDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class StepController {
	private static final Logger logger = LoggerFactory.getLogger(StepController.class);

	@Autowired
	private CompletableFutureConfig completableFutureConfig;

	@PostMapping("/api/generate")
	public ResponseEntity<TaskDto> generateSequence(@RequestBody GenerateDto generateDto) {

		UUID uuid = UUID.randomUUID();
		logger.info("Generating UUI for task: " + uuid.toString());

		SequenceDto sequenceDtoInProgress = new SequenceDto("IN_PROGRESS", null);

		ArrayList<SequenceDto> sequenceDtoListInProgress = new ArrayList<SequenceDto>();
		sequenceDtoListInProgress.add(sequenceDtoInProgress);

		completableFutureConfig.getSequenceMap().put(uuid.toString(), sequenceDtoListInProgress);
		logger.info("Adding list of IN_PROGRESS task to completableFutureConfig");

		try {
			CompletableFuture.runAsync(() -> {
				ArrayList<Integer> sequence = new ArrayList<Integer>();

				try {
					logger.info("Waiting for 30 seconds");
					TimeUnit.SECONDS.sleep(30);
				} catch (InterruptedException e) {
					throw new IllegalStateException(e);
				}

				for (int i = generateDto.getGoal(); i >= 0; i -= generateDto.getStep()) {
					sequence.add(i);
				}

				SequenceDto sequenceDtoSuccess = new SequenceDto("SUCCESS", sequence);

				ArrayList<SequenceDto> sequenceDtoListSuccess = new ArrayList<SequenceDto>();
				sequenceDtoListSuccess.add(sequenceDtoSuccess);

				completableFutureConfig.getSequenceMap().replace(uuid.toString(), sequenceDtoListSuccess);
				logger.info("Adding list of SUCCESS task to completableFutureConfig");

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			SequenceDto sequenceDtoFailed = new SequenceDto("FAILED", null);

			ArrayList<SequenceDto> sequenceDtoListFailed = new ArrayList<SequenceDto>();
			sequenceDtoListFailed.add(sequenceDtoFailed);

			completableFutureConfig.getSequenceMap().replace(uuid.toString(), sequenceDtoListFailed);
			logger.info("Adding list of FAILED task to completableFutureConfig");
		}

		logger.info("Sending response");
		return ResponseEntity.accepted().body(new TaskDto(uuid.toString()));
	}

	@PostMapping("/api/bulkGenerate")
	public ResponseEntity<TaskDto> bulkGenerateSequence(@RequestBody ArrayList<GenerateDto> generateDtoList) {

		UUID uuid = UUID.randomUUID();
		logger.info("Generating UUI for task: " + uuid.toString());

		SequenceDto sequenceDtoInProgress = new SequenceDto("IN_PROGRESS", null);

		ArrayList<SequenceDto> sequenceDtoListInProgress = new ArrayList<SequenceDto>();
		sequenceDtoListInProgress.add(sequenceDtoInProgress);

		completableFutureConfig.getSequenceMap().put(uuid.toString(), sequenceDtoListInProgress);
		logger.info("Adding list of IN_PROGRESS task to completableFutureConfig");

		try {
			CompletableFuture.runAsync(() -> {

				ArrayList<SequenceDto> sequenceDtoListSuccess = new ArrayList<SequenceDto>();

				try {
					logger.info("Waiting for 30 seconds");
					TimeUnit.SECONDS.sleep(30);
				} catch (InterruptedException e) {
					throw new IllegalStateException(e);
				}

				for (GenerateDto generateDto : generateDtoList) {
					ArrayList<Integer> sequence = new ArrayList<Integer>();

					for (int i = generateDto.getGoal(); i >= 0; i -= generateDto.getStep()) {
						sequence.add(i);
					}

					SequenceDto sequenceDtoSuccess = new SequenceDto("SUCCESS", sequence);
					sequenceDtoListSuccess.add(sequenceDtoSuccess);
				}

				completableFutureConfig.getSequenceMap().replace(uuid.toString(), sequenceDtoListSuccess);
				logger.info("Adding list of SUCCESS task to completableFutureConfig");
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			SequenceDto sequenceDtoFailed = new SequenceDto("FAILED", null);

			ArrayList<SequenceDto> sequenceDtoListFailed = new ArrayList<SequenceDto>();
			sequenceDtoListFailed.add(sequenceDtoFailed);

			completableFutureConfig.getSequenceMap().replace(uuid.toString(), sequenceDtoListFailed);
			logger.info("Adding list of FAILED task to completableFutureConfig");
		}

		logger.info("Sending response");
		return ResponseEntity.accepted().body(new TaskDto(uuid.toString()));

	}

	@GetMapping("/api/task/{uuid}")
	public ResponseEntity<?> getSequence(@PathVariable("uuid") String uuid) {
		ArrayList<SequenceDto> sequenceDtoList = completableFutureConfig.getSequenceMap().get(uuid);

		ArrayList<String> result = new ArrayList<String>();
		if (sequenceDtoList != null) {
			for (SequenceDto sequenceDto : sequenceDtoList) {
				result.add(String.join(", ",
						sequenceDto.getSeqeunce().stream().map(Object::toString).collect(Collectors.toList())));
			}
		} else {
			return ResponseEntity.notFound().build();
		}
		if (result.isEmpty()) {
			logger.error("The Sequence Genertion for \"" + uuid + "\" still in Progress");
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().body(result.size() > 1 ? new ResultsDto(result) : new ResultDto(result.get(0)));
	}

	@GetMapping("/api/task/{uuid}/status")
	public ResponseEntity<?> getStatus(@PathVariable("uuid") String uuid) {

		ArrayList<SequenceDto> sequenceDtoList = completableFutureConfig.getSequenceMap().get(uuid);

		ArrayList<String> result = new ArrayList<String>();
		if (sequenceDtoList != null) {
			for (SequenceDto sequenceDto : sequenceDtoList) {
				result.add(sequenceDto.getStatus());
			}
		} else {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().body(result.size() > 1 ? new ResultsDto(result) : new ResultDto(result.get(0)));
	}
}