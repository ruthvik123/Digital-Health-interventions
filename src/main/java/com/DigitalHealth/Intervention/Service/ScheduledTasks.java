package com.DigitalHealth.Intervention.Service;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	JdbcTemplate jdbcTemplate;
	UtilityService us;
	
	@Scheduled(fixedRate = 1000*60*60*24)
	public void pollAllUsers() {
		List<String> userList;
		String sql = "SELECT DISTINCT user_id FROM mhdp.device_id;";
		try {
			userList =  namedjdbcTemplate.queryForList(sql,new HashMap<String,String>(),String.class);
			System.out.println(userList);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		
	}
	
	public void getIntervention(List<String> userList) {
		
		
		for(String user : userList) {
			
			String interventionValue = "";
			
			if(us.getCallDuration(user,24*30) > 119 ) { // threshold value selected after research
				interventionValue = interventionValue + "High calls | ";
			}
			if(us.getAmbientNoise(user,24) > 55) { 
				interventionValue = interventionValue + "High noise | ";
			}
			if(us.getPhysicalActivity(user,24) < 45) { 
				interventionValue = interventionValue + "Low Physical Activity | ";
			}
			
			// similarly for all 6 biomarkers
			
			if(!interventionValue.equals("")) {
				String sql = "INSERT INTO worklist_Table (userId, message, status, timestamp)\r\n" + 
						"VALUES (:userID, :message, :status, :timestp);";
				
				java.util.Date date = new java.util.Date();
				Object timeStamp = new java.sql.Timestamp(date.getTime());
				
				
				SqlParameterSource namedParams = new MapSqlParameterSource("userID", user)
						.addValue("message", interventionValue)
						.addValue("status", "Pending")
						.addValue("timestp", timeStamp);
		       
				namedjdbcTemplate.update(sql,namedParams);
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
			if(map.containsValue(vector)) {
				temp = map.get(vector);
			}
			else {
				temp = new LinkedList<String>();
			}
			temp.add(user);
			map.put(vector,temp);
		}
		
		List<? extends Cluster<DoublePoint>> res = clusterer.cluster(Data);
		
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
