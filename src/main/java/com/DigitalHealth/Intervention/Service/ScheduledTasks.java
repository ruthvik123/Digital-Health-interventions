package com.DigitalHealth.Intervention.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class ScheduledTasks {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	public void reportCurrentTime() {
		System.out.println("Time is " + printTime());
	}
	
	public String printTime() {
		 return dateFormat.format(new Date(System.currentTimeMillis())).toString();
	}
	
}
