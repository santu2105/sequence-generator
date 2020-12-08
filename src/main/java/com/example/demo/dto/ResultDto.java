package com.example.demo.dto;

public class ResultDto {
	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public ResultDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResultDto(String result) {
		super();
		this.result = result;
	}

	@Override
	public String toString() {
		return "ResultDto [result=" + result + "]";
	}

}
