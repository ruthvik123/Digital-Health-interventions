package com.DigitalHealth.Intervention.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class ScheduledTasks {

	@Autowired
	NamedParameterJdbcTemplate namedjdbcTemplate;
	
	@Scheduled(fixedRate = 1000*60*60*24)
	public void pollAllUsers() {
		List<Integer> userList;
		String sql = "SELECT user_id FROM mhdp.device_id;";
		try {
			userList =  namedjdbcTemplate.queryForList(sql,new HashMap<String,String>(),Integer.class);
			// function which takes in this list and checks for interventions
			System.out.println(userList);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		
	}
	
}
