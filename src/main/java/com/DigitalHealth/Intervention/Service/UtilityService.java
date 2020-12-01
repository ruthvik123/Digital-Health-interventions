package com.DigitalHealth.Intervention.Service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class UtilityService {
	
	public long getTimestamp(int windowInHours) {
		Date currentDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.HOUR, -windowInHours);
		return cal.getTimeInMillis();
	}

	@Autowired
	NamedParameterJdbcTemplate namedjdbcTemplate;

	public int getCallDuration(String userID, int windowInHours) {

		
		long windowMilli = getTimestamp(windowInHours);
		
		String sql = "SELECT SUM(JSON_EXTRACT(data, '$.call_duration')) \r\n"
				+ "AS 'call_duration' FROM mhdp.calls where device_id IN (SELECT device_id FROM mhdp.device_id where user_id = :userID) \r\n"
				+ "AND (JSON_EXTRACT(data, '$.timestamp')) > :windowMilli  group by device_id;";

		SqlParameterSource namedParams = new MapSqlParameterSource("userID", userID).addValue("windowMilli",
				windowMilli);
		int callDuration;
		try {
			callDuration = namedjdbcTemplate.queryForObject(sql, namedParams, Integer.class);
			return callDuration;

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return -1;
		}
	}
	
	public double getDeviceUsage(String userID, int windowInHours) {

		
		long windowMilli = getTimestamp(windowInHours);
		
		String sql = "SELECT SUM(JSON_EXTRACT(data, '$.elapsed_device_on')) \r\n"
				+ "AS 'device_usage' FROM mhdp.plugin_device_usage where device_id IN (SELECT device_id FROM mhdp.device_id where user_id = :userID) \r\n"
				+ "AND (JSON_EXTRACT(data, '$.timestamp')) > :windowMilli  group by device_id;";

		SqlParameterSource namedParams = new MapSqlParameterSource("userID", userID).addValue("windowMilli",
				windowMilli);
		double deviceUsage;
		try {
			deviceUsage = namedjdbcTemplate.queryForObject(sql, namedParams, Integer.class);
			return deviceUsage;

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return -1;
		}
	}
	
	public int getToneAnalysis(String userID, int windowInHours) {

		
		long windowMilli = getTimestamp(windowInHours);
		
		String sql = "SELECT SUM(JSON_EXTRACT(data, '$.call_duration')) \r\n"
				+ "AS 'call_duration' FROM mhdp.calls where device_id=(SELECT device_id FROM mhdp.device_id where user_id = :userID) \r\n"
				+ "AND (JSON_EXTRACT(data, '$.timestamp')) > :windowMilli  group by device_id;";

		SqlParameterSource namedParams = new MapSqlParameterSource("userID", userID).addValue("windowMilli",
				windowMilli);
		int callDuration;
		try {
			callDuration = namedjdbcTemplate.queryForObject(sql, namedParams, Integer.class);
			return callDuration;

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return -1;
		}
	}
	
	public int getAmbientNoise(String userID, int windowInHours) {

		
		long windowMilli = getTimestamp(windowInHours);
		
		String sql = "SELECT COUNT(JSON_EXTRACT(data, '$.double_decibels')) \r\n"
				+ "AS 'high_decibel' FROM mhdp.plugin_ambient_noise where device_id=(SELECT device_id FROM mhdp.device_id where user_id = :userID) \r\n"
				+ "AND (JSON_EXTRACT(data, '$.timestamp')) > :windowMilli AND JSON_EXTRACT(data, '$.double_decibels') > 80 \r\n"
				+ " group by device_id;";

		SqlParameterSource namedParams = new MapSqlParameterSource("userID", userID).addValue("windowMilli",
				windowMilli);
		int callDuration;
		try {
			callDuration = namedjdbcTemplate.queryForObject(sql, namedParams, Integer.class);
			return callDuration;

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return -1;
		}
	}
	
	public int getPhysicalActivity(String userID, int windowInHours) {

		
		long windowMilli = getTimestamp(windowInHours);
		
		String sql = "SELECT COUNT(JSON_EXTRACT(data, '$.double_decibels')) \r\n"
				+ "AS 'high_decibel' FROM mhdp.plugin_ambient_noise where device_id=(SELECT device_id FROM mhdp.device_id where user_id = :userID) \r\n"
				+ "AND (JSON_EXTRACT(data, '$.timestamp')) > :windowMilli AND JSON_EXTRACT(data, '$.double_decibels') > 80 \r\n"
				+ " group by device_id;";

		SqlParameterSource namedParams = new MapSqlParameterSource("userID", userID).addValue("windowMilli",
				windowMilli);
		int callDuration;
		try {
			callDuration = namedjdbcTemplate.queryForObject(sql, namedParams, Integer.class);
			return callDuration;

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return -1;
		}
	}

}
