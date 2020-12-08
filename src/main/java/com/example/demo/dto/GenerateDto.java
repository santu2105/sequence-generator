package com.example.demo.dto;

public class GenerateDto {
	private Integer goal;
	private Integer step;

	public Integer getGoal() {
		return goal;
	}

	public void setGoal(Integer goal) {
		this.goal = goal;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public GenerateDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GenerateDto(Integer goal, Integer step) {
		super();
		this.goal = goal;
		this.step = step;
	}

	@Override
	public String toString() {
		return "GenerateDto [goal=" + goal + ", step=" + step + "]";
	}

}
