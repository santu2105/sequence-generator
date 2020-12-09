package com.example.demo.config;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.example.demo.dto.SequenceDto;

@Component
@ConfigurationProperties("static")
public class CompletableFutureConfig {
	private HashMap<String, ArrayList<SequenceDto>> sequenceMap;

	public HashMap<String, ArrayList<SequenceDto>> getSequenceMap() {
		return sequenceMap;
	}

	public void setSequenceMap(HashMap<String, ArrayList<SequenceDto>> sequenceMap) {
		this.sequenceMap = sequenceMap;
	}
	
	public CompletableFutureConfig() {
		super();
		// TODO Auto-generated constructor stub
		
		sequenceMap = new HashMap<String, ArrayList<SequenceDto>>();
	}

	public CompletableFutureConfig(HashMap<String, ArrayList<SequenceDto>> sequenceMap) {
		super();
		this.sequenceMap = sequenceMap;
	}

	@Override
	public String toString() {
		return "CompletableFutureRepository [sequenceList=" + sequenceMap + "]";
	}
}
