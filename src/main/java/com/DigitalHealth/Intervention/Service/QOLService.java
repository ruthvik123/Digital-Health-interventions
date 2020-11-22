package com.DigitalHealth.Intervention.Service;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.DigitalHealth.Intervention.model.QOLQuestionnaire;
import com.DigitalHealth.Intervention.model.Questions;

@Component
public class QOLService {
	public QOLQuestionnaire getQuestionnaire(String userID) {
		boolean calling = false;
		boolean messaging = false;
		boolean socialMedia = false;
		boolean physicalActivity = false;
		boolean ambientNoise = false;
		boolean alarmMap = false;
		
		List<Questions> questions = new ArrayList<>();
		
		if(calling) {
			questions.add(new Questions("Rate in terms of (1-5) how occupied were you on phone for professional reasons (assuming the remainder is spent for personal calls)", "type1"));
		} else {
			questions.add(new Questions("How connected do you think you are with your friends and family?","type1"));
		}
		
		if(messaging) {
			questions.add(new Questions("aa","aa"));
		} else {
			questions.add(new Questions("aa","aa"));
		}
		
		if(socialMedia) {
			questions.add(new Questions("aa","aa"));
		} else {
			questions.add(new Questions("aa","aa"));
		}
		
		if(physicalActivity) {
			questions.add(new Questions("aa","aa"));
		} else {
			questions.add(new Questions("aa","aa"));
		}
		
		if(ambientNoise) {
			questions.add(new Questions("aa","aa"));
		} else {
			questions.add(new Questions("aa","aa"));
		}
		
		if(alarmMap) {
			questions.add(new Questions("aa","aa"));
		} else {
			questions.add(new Questions("aa","aa"));
		}
		
		return new QOLQuestionnaire(questions);
	}
}
