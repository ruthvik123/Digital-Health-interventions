package com.DigitalHealth.Intervention.Service;

import java.io.StringWriter;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.DigitalHealth.Intervention.model.PeerResponse;


@Component
public class ScheduledTasks {

	@Autowired
	NamedParameterJdbcTemplate namedjdbcTemplate;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	UtilityService us;
	
	@Scheduled(fixedRate = 1000*60*60*24)
	public void pollAllUsers() {
		List<String> userList;
		String sql = "SELECT DISTINCT user_id FROM mhdp.device_id;";
		try {
			userList =  namedjdbcTemplate.queryForList(sql,new HashMap<String,String>(),String.class);
			System.out.println(userList);
			getIntervention(userList);
			clustering(userList);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void getIntervention(List<String> userList) {
		
		
		for(String user : userList) {
			
			String interventionValue = "";
			
			if(us.getCallScore(user,24*7) < 25 ) { // threshold value selected after research
				interventionValue = interventionValue + "High Calls,";
			}
			if(us.getAmbientNoiseScore(user,24*7) < 25) { 
				interventionValue = interventionValue + "Loud Noise,";
			}
			if(us.getPhysicalActivityScore(user,24*7) < 25) { 
				interventionValue = interventionValue + "Low Physical Activity,";
			}
			if(us.getAlarmScore(user, 24*7) < 25) { 
				interventionValue = interventionValue + "Alarm Map Alert,";
			}
			if(us.getToneScore(user, 24*7) < 25) { 
				interventionValue = interventionValue + "Alarm Map Alert,";
			}
			if(us.getDeviceUsageScore(user, 24*7) < 25) { 
				interventionValue = interventionValue + "High Device Usage,";
			}
			if(!interventionValue.equals("")) {
				interventionValue = interventionValue.substring(0, interventionValue.length() - 1);
								
//				Date date = (Date) new java.util.Date();
//				Calendar cal = Calendar.getInstance();
//				cal.setTime(date);
//				cal.add(Calendar.DATE, -3);
//				Object timeStamp = new java.sql.Timestamp(cal.getTimeInMillis());
				
				String sql = "INSERT INTO mhdp.worklist_Table (userId, message, status, timestamp)\r\n" + 
						"SELECT :userID, :message, 'Pending', now() \r\n" + 
						"WHERE NOT EXISTS (\r\n" + 
						"    SELECT userId FROM mhdp.worklist_Table WHERE message = :message and userId = :userID and timestamp  >= NOW() - INTERVAL 3 DAY  \r\n" + 
						") LIMIT 1;";

				SqlParameterSource namedParams = new MapSqlParameterSource("userID", user)
						.addValue("message", interventionValue);
						//.addValue("status", "Pending");
//						.addValue("timestp", timeStamp);
		       
				try {
				namedjdbcTemplate.update(sql,namedParams);
				}
				catch(Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}
		
	}
	
	public void clustering(List<String> userList) {
		
		int k = (int) Math.ceil(userList.size()/10); // 10 patients in 1 grpoup
	
		Clusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<DoublePoint>(k);
		Map<DoublePoint, Queue<String>> map = new HashMap();
		List<DoublePoint> Data = new ArrayList<>();
		
		for(String user : userList){
			
			double dimensions[] = new double[5];
			
			dimensions[0] = (double)us.getCallScore(user, 7*24);
			dimensions[1] = (double)us.getAmbientNoiseScore(user, 7*24);
			dimensions[2] = (double)us.getPhysicalActivityScore(user, 7*24);
			dimensions[3] = (double)us.getDeviceUsageScore(user, 7*24);
			dimensions[4] = (double)us.getToneScore(user, 7*24);
			
			DoublePoint vector = new DoublePoint(dimensions);
			Data.add(vector);
			
			Queue<String> temp;
			if(map.containsKey(vector)) {
				temp = map.get(vector);
			}
			else {
				temp = new LinkedList<String>();
			}
			temp.add(user);
			map.put(vector,temp);
		}
		
		List<? extends Cluster<DoublePoint>> res = clusterer.cluster(Data);
		for (Cluster<DoublePoint> re : res) {
	        System.out.println(re.getPoints());
	    }
		try {
			String sql0 = "DELETE FROM mhdp.peer_table;";
			jdbcTemplate.update(sql0);
			
			int idCounter = 1;
			
			for (Cluster<DoublePoint> re : res) {
		         List<DoublePoint> points = re.getPoints();
		         for(DoublePoint DP : points) {
		        	 Queue<String> q = map.get(DP);
		        	 String PatientName = (String) q.poll();
		        	 String sql = "INSERT INTO peer_table (peergroup_id, userId)\r\n" + 
		 					"VALUES (:peergroupID, :userID);";
		        	 SqlParameterSource namedParams = new MapSqlParameterSource("peergroupID", idCounter)
		 					.addValue("userID", PatientName);
		        	 namedjdbcTemplate.update(sql,namedParams);
		         }
		        idCounter++;
		    }
		}	
		catch(Exception e) {
			System.out.println(e);
		}

	}
}
