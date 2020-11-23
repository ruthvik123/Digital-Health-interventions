package com.DigitalHealth.Intervention.Service;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.DigitalHealth.Intervention.model.QOLQuestionnaire;
import com.DigitalHealth.Intervention.model.QOLQuestionnaireDescription;
import com.DigitalHealth.Intervention.model.QOLQuestionnaireList;
import com.DigitalHealth.Intervention.model.Questions;

@Component
public class QOLService {
	
	public QOLQuestionnaireList getQuestionnaireList(String userID) {
		List<QOLQuestionnaireDescription> questionnaireList = new ArrayList<>();
		questionnaireList.add(new QOLQuestionnaireDescription( 1, "Pending"));
		questionnaireList.add(new QOLQuestionnaireDescription( 2, "Completed"));
		return new QOLQuestionnaireList(questionnaireList);
	}
	
	public QOLQuestionnaire getQuestionnaire(String userID, int questionnaireID) {
		if(questionnaireID == 1) {
			return getQuestionnairePending(userID,questionnaireID);
		}else {
			return getQuestionnaireCompleted(userID, questionnaireID);
		}
	}
	
	
	
	public QOLQuestionnaire getQuestionnairePending(String userID, int questionnaireID) {
		boolean calling = false;
		boolean messaging = false;
		boolean socialMedia = false;
		boolean physicalActivity = false;
		boolean ambientNoise = false;
		boolean alarmMap = false;
		
		int count = 1;
		
		List<Questions> questions = new ArrayList<>();
		
		
		if(calling) {
			questions.add(new Questions(count, "Explain on what topics do you spend the most time on the phone", "Text"));
			count++;
		} else {
			questions.add(new Questions(count, "How connected do you think you are with your friends and family?","Rating"));
			count++;
		}
		
		if(messaging) {
			questions.add(new Questions(count, "While messaging, how well aware are you of your surroundings","Rating"));
			count++;
		} else {
			questions.add(new Questions(count, "Do you feel lonely","Yes/No"));
			count++;
		}
		
		if(socialMedia) {
			questions.add(new Questions(count, "Is your social media interactions affecting your daily tasks?","Rating"));
			count++;
		} else {
			questions.add(new Questions(count, "Are you in touch with your friends?","Yes/No"));
			count++;
		}//
		
		if(physicalActivity) {
			questions.add(new Questions(count, "Does your body shape affect you mentally and hinders with your productivity?","Yes/No"));
			count++;
		} else {
			questions.add(new Questions(count, "Do you think you are physically healthy?","Yes/No"));
			count++;
		}
		
		if(ambientNoise) {
			questions.add(new Questions(count, "How stressful do you feel in your daily life?","Rating"));
			count++;
		} else {
			questions.add(new Questions(count, "Would you say you live a sedentary lifestyle?","Yes/No"));
			count++;
		}
		
		if(alarmMap) {
			questions.add(new Questions(count, "Placeholder1","Text"));
			count++;
		} else {
			questions.add(new Questions(count, "Placeholder2","Rating"));
			count++;
		}
		
		return new QOLQuestionnaire(questions);
	}
	
	public QOLQuestionnaire getQuestionnaireCompleted(String userID, int questionnaireID) {
		boolean calling = false;
		boolean messaging = false;
		boolean socialMedia = false;
		boolean physicalActivity = false;
		boolean ambientNoise = false;
		boolean alarmMap = false;
		
		int count = 1;
		
		List<Questions> questions = new ArrayList<>();
		
		
		if(calling) {
			questions.add(new Questions(count, "Explain on what topics do you spend the most time on the phone", "Text", "Sample Answer"));
			count++;
		} else {
			questions.add(new Questions(count, "How connected do you think you are with your friends and family?","Rating", "2"));
			count++;
		}
		
		if(messaging) {
			questions.add(new Questions(count, "While messaging, how well aware are you of your surroundings","Rating", "3"));
			count++;
		} else {
			questions.add(new Questions(count, "Do you feel lonely","Yes/No", "No"));
			count++;
		}
		
		if(socialMedia) {
			questions.add(new Questions(count, "Is your social media interactions affecting your daily tasks?","Rating", "4"));
			count++;
		} else {
			questions.add(new Questions(count, "Are you in touch with your friends?","Yes/No", "Yes"));
			count++;
		}//
		
		if(physicalActivity) {
			questions.add(new Questions(count, "Does your body shape affect you mentally and hinders with your productivity?","Yes/No", "No"));
			count++;
		} else {
			questions.add(new Questions(count, "Do you think you are physically healthy?","Yes/No", "Yes"));
			count++;
		}
		
		if(ambientNoise) {
			questions.add(new Questions(count, "How stressful do you feel in your daily life?","Rating", "4"));
			count++;
		} else {
			questions.add(new Questions(count, "Would you say you live a sedentary lifestyle?","Yes/No", "No"));
			count++;
		}
		
		if(alarmMap) {
			questions.add(new Questions(count, "Placeholder1","Text", "Sample Text"));
			count++;
		} else {
			questions.add(new Questions(count, "Placeholder2","Rating", "4"));
			count++;
		}
		
		return new QOLQuestionnaire(questions);
	}
}
