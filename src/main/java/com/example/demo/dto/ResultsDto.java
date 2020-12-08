package com.example.demo.dto;

import java.util.ArrayList;

public class ResultsDto {
	private ArrayList<String> results;

	public ArrayList<String> getResults() {
		return results;
	}

	public void setResults(ArrayList<String> results) {
		this.results = results;
	}

	public ResultsDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResultsDto(ArrayList<String> results) {
		super();
		this.results = results;
	}

	@Override
	public String toString() {
		return "ResultDto [result=" + results + "]";
	}

}
