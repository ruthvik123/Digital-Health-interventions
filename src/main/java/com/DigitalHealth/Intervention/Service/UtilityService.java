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

	@Autowired
	NamedParameterJdbcTemplate namedjdbcTemplate;

	public int getCallDuration(int userID, int windowInHours) {

		Date currentDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.HOUR, -windowInHours);
		long windowMilli = cal.getTimeInMillis();
		
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

}
