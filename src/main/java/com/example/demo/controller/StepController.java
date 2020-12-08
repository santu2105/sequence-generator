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

@RestController
public class StepController {
	@Autowired
	private CompletableFutureConfig completableFutureConfig;

	@PostMapping("/api/generate")
	public ResponseEntity<TaskDto> generateSequence(@RequestBody GenerateDto generateDto) {
		UUID uuid = UUID.randomUUID();

		SequenceDto sequenceDtoInProgress = new SequenceDto("IN_PROGRESS", null);

		ArrayList<SequenceDto> sequenceDtoListInProgress = new ArrayList<SequenceDto>();
		sequenceDtoListInProgress.add(sequenceDtoInProgress);

		completableFutureConfig.getSequenceMap().put(uuid.toString(), sequenceDtoListInProgress);

		try {
			CompletableFuture.runAsync(() -> {
				ArrayList<Integer> sequence = new ArrayList<Integer>();

				try {
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

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			SequenceDto sequenceDtoFailed = new SequenceDto("FAILED", null);

			ArrayList<SequenceDto> sequenceDtoListFailed = new ArrayList<SequenceDto>();
			sequenceDtoListFailed.add(sequenceDtoFailed);

			completableFutureConfig.getSequenceMap().replace(uuid.toString(), sequenceDtoListFailed);
		}

		return ResponseEntity.accepted().body(new TaskDto(uuid.toString()));
	}

	@PostMapping("/api/bulkGenerate")
	public ResponseEntity<TaskDto> bulkGenerateSequence(@RequestBody ArrayList<GenerateDto> generateDtoList) {

		UUID uuid = UUID.randomUUID();

		SequenceDto sequenceDtoInProgress = new SequenceDto("IN_PROGRESS", null);

		ArrayList<SequenceDto> sequenceDtoListInProgress = new ArrayList<SequenceDto>();
		sequenceDtoListInProgress.add(sequenceDtoInProgress);

		completableFutureConfig.getSequenceMap().put(uuid.toString(), sequenceDtoListInProgress);

		try {
			CompletableFuture.runAsync(() -> {
			
				ArrayList<SequenceDto> sequenceDtoListSuccess = new ArrayList<SequenceDto>();

				try {
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

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			SequenceDto sequenceDtoFailed = new SequenceDto("FAILED", null);

			ArrayList<SequenceDto> sequenceDtoListFailed = new ArrayList<SequenceDto>();
			sequenceDtoListFailed.add(sequenceDtoFailed);

			completableFutureConfig.getSequenceMap().replace(uuid.toString(), sequenceDtoListFailed);
		}

		return ResponseEntity.accepted().body(new TaskDto(uuid.toString()));

	}

	@GetMapping("/api/task/{uuid}")
	public ResponseEntity<?> getSequence(@PathVariable("uuid") String uuid) {
		ArrayList<SequenceDto> sequenceDtoList = completableFutureConfig.getSequenceMap().get(uuid);

		ArrayList<String> result = new ArrayList<String>();
		for (SequenceDto sequenceDto : sequenceDtoList) {
			result.add(String.join(", ",
					sequenceDto.getSeqeunce().stream().map(Object::toString).collect(Collectors.toList())));
		}

		return ResponseEntity.ok().body(result.size() > 1 ? new ResultsDto(result) : new ResultDto(result.get(0)));
	}

	@GetMapping("/api/task/{uuid}/status")
	public ResponseEntity<?> getStatus(@PathVariable("uuid") String uuid) {

		ArrayList<SequenceDto> sequenceDtoList = completableFutureConfig.getSequenceMap().get(uuid);

		ArrayList<String> result = new ArrayList<String>();
		for (SequenceDto sequenceDto : sequenceDtoList) {
			result.add(sequenceDto.getStatus());
		}

		return ResponseEntity.ok().body(result.size() > 1 ? new ResultsDto(result) : new ResultDto(result.get(0)));
	}
}