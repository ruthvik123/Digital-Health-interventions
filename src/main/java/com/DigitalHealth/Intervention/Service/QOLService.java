package com.DigitalHealth.Intervention.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.DigitalHealth.Intervention.model.QOLAnswers;
import com.DigitalHealth.Intervention.model.QOLQuestionnaire;
import com.DigitalHealth.Intervention.model.QOLQuestionnaireDescription;
import com.DigitalHealth.Intervention.model.QOLQuestionnaireList;
import com.DigitalHealth.Intervention.model.QOLResponseFromApp;
import com.DigitalHealth.Intervention.model.Questions;

@Component
public class QOLService {
	
	public class getQuestionnaireRowMapper implements RowMapper {
		@Override
		public QOLQuestionnaireDescription  mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			QOLQuestionnaireDescription obj = new QOLQuestionnaireDescription();
			obj.setId(rs.getInt("questionnaire_id"));
			obj.setStatus(rs.getString("status"));
			return obj;
		}
	}
	
	public class getQuestionsRowMapper implements RowMapper {
		@Override
		public Questions  mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			Questions obj = new Questions();
			obj.setId(rs.getInt("question_id"));
			obj.setQuestion(rs.getString("question"));
			obj.setAnswer(rs.getString("answer"));
			obj.setType(rs.getString("type"));
			return obj;
		}
	}
	
	@Autowired
	NamedParameterJdbcTemplate namedjdbcTemplate;
	
	@Autowired
	UtilityService us;
	
	
	public QOLQuestionnaireList getQuestionnaireList(String userID) {
		String sql = "select questionnaire_id, status from Questionnaire_table where patient_id = :patientID";
		SqlParameterSource namedParams = new MapSqlParameterSource("patientID",userID);
		List<QOLQuestionnaireDescription> questionnaireList = namedjdbcTemplate.query(sql, namedParams, new getQuestionnaireRowMapper());		
	    return new QOLQuestionnaireList(questionnaireList);
	}
	
	public QOLQuestionnaire getQuestionnaire(String userID, int questionnaireID) {
		
		String sql = "select COUNT(questionnaire_id) from Questions_table where questionnaire_id = :questionnaireID";
		SqlParameterSource namedParams = new MapSqlParameterSource("questionnaireID",questionnaireID);
		Integer count = namedjdbcTemplate.queryForObject(sql, namedParams, Integer.class);
		QOLQuestionnaire response;
		if(count == 0) {
			
			response = getQuestionnairePending(userID,questionnaireID);
		}
		else {
		    response = getQuestionnaireCompleted(userID,questionnaireID);
		}
		return response;
	}
	
	public void insertPendingQuestions(List<Questions> questions, int questionnaireID) {
		int count = 1;
		for(Questions q: questions) {
			String sql = "INSERT INTO Questions_table (questionnaire_id, question_id, question, type, answer)\r\n" + 
					"VALUES (:questionnaireID, :questionID, :Q, :T, :A);";
			SqlParameterSource namedParams = new MapSqlParameterSource("questionnaireID", questionnaireID)
					.addValue("questionID", count)
					.addValue("Q", q.getQuestion())
					.addValue("T", q.getType())
					.addValue("A", q.getAnswer());
			namedjdbcTemplate.update(sql,namedParams);
			count++;
		}
		
	}
	
	public QOLQuestionnaire getQuestionnairePending(String userID, int questionnaireID) {
		boolean calling = false;
		boolean messaging = false;
		boolean socialMedia = false;
		boolean physicalActivity = false;
		boolean ambientNoise = false;
		boolean alarmMap = false;
		
		System.out.println("Call duration is " + us.getCallDuration(userID, 24));
		
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
			questions.add(new Questions(count, "Placeholder2","Text"));
			count++;
		}
		insertPendingQuestions(questions,questionnaireID);
		return new QOLQuestionnaire(questions);
	}
	
	public QOLQuestionnaire getQuestionnaireCompleted(String userID, int questionnaireID) {
		
        String sql = "select question_id, question, type, answer from Questions_table where questionnaire_id = :id";
        SqlParameterSource namedParams = new MapSqlParameterSource("id",questionnaireID);
        List<Questions> questions = namedjdbcTemplate.query(sql, namedParams, new getQuestionsRowMapper());
		return new QOLQuestionnaire(questions);
	}
	
	public void updateAnswersToTable(QOLResponseFromApp response) {
		SqlParameterSource namedParams;
		for(QOLAnswers ans : response.getUserResponse()) {
			String sql = "UPDATE Questions_table\r\n" + 
					"SET answer = :ANS " + 
					"WHERE questionnaire_id  = :questionnaireID AND question_id = :questionID ;";
			namedParams = new MapSqlParameterSource("ANS",ans.getAnswer())
					.addValue("questionnaireID", response.getQuestionnaireID())
					.addValue("questionID", ans.getId());
			namedjdbcTemplate.update(sql,namedParams);
					
		}
		
		String sql_updateQuestionnaire = "UPDATE Questionnaire_table \r\n" + 
									"SET status = 'Completed' " + 
									"WHERE questionnaire_id  = :questionnaireID ;";
		namedParams = new MapSqlParameterSource("questionnaireID",response.getQuestionnaireID());
		namedjdbcTemplate.update(sql_updateQuestionnaire,namedParams);
		
	}
	
}
