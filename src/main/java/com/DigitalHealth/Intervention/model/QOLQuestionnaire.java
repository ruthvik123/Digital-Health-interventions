package com.DigitalHealth.Intervention.model;

import java.util.List;

public class QOLQuestionnaire {
	
	
	private List<Questions> Questions;

	public QOLQuestionnaire(List<Questions> questions) {
		this.Questions = questions;
	}
	
	public List<Questions> getQuestions(){
		return Questions;
	}//
	
	public void setQuestions(List<Questions> questions){
		this.Questions = questions;
	}
}
