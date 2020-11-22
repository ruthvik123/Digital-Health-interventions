package com.DigitalHealth.Intervention.model;

public class Questions{
	String Question;
	String Type;
	
	public Questions(String questionBody, String questionType) {
		super();
		this.Question = questionBody;
		this.Type = questionType;
	}
//
	public String getQuestion() {
		return Question;
	}

	public void setQuestion(String question) {
		Question = question;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}
	
	
	
	
}
