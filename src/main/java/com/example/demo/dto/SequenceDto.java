package com.example.demo.dto;

import java.util.ArrayList;

public class SequenceDto {
	private String status;
	private ArrayList<Integer> seqeunce;
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public ArrayList<Integer> getSeqeunce() {
		return seqeunce;
	}
	
	public void setSeqeunce(ArrayList<Integer> seqeunce) {
		this.seqeunce = seqeunce;
	}
	
	public SequenceDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public SequenceDto(String status, ArrayList<Integer> seqeunce) {
		super();
		this.status = status;
		this.seqeunce = seqeunce;
	}

	@Override
	public String toString() {
		return "SequenceDto [status=" + status + ", seqeunce=" + seqeunce + "]";
	}
}
