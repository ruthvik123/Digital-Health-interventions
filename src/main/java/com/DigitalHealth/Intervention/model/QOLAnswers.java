package com.DigitalHealth.Intervention.model;

public class QOLAnswers {
	
	private String question;
	private String answer;
	public QOLAnswers() {
		
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	@Override
	public String toString() {
		return String.format("question: "+question +" answer: "+ answer);
	}
}
