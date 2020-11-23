package com.DigitalHealth.Intervention.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
@Component
public class QOLResponse {

	private List<QOLAnswers> answerList = new ArrayList();
	private String userID;

	public List<QOLAnswers> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<QOLAnswers> answerList) {
		this.answerList = answerList;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	@Override
	public String toString() {
		return String.format("userID: "+userID.toString() +" answers: "+ answerList.toString());
	}

}
