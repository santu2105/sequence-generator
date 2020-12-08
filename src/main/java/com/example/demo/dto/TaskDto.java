package com.example.demo.dto;

public class TaskDto {
	private String task;

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public TaskDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TaskDto(String task) {
		super();
		this.task = task;
	}

	@Override
	public String toString() {
		return "TaskDto [task=" + task + "]";
	}
}
