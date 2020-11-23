package com.DigitalHealth.Intervention.controller;

import java.lang.annotation.Target;
import java.lang.reflect.Field;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.DigitalHealth.Intervention.Service.QOLService;
import com.DigitalHealth.Intervention.model.QOLQuestionnaire;
import com.DigitalHealth.Intervention.model.QOLQuestionnaireList;
import com.DigitalHealth.Intervention.model.QOLResponseFromApp;


@RestController
public class Controller {
	
	@Autowired
	QOLService	qolService;

    @GetMapping("/")
    public String test(){
        return "app started...";
    }///
    
    @GetMapping("/qolList/{userID}")
    public QOLQuestionnaireList t1(@PathVariable String userID) { 
    	if(userID.equals("admin")) {
    		return qolService.getQuestionnaireList(userID);
    	} 
    	return new QOLQuestionnaireList();
    }
    
    @GetMapping("/qol/{userID}/{questionnaireID}")
    public QOLQuestionnaire t1(@PathVariable String userID,@PathVariable int questionnaireID) { 
    	return qolService.getQuestionnaire(userID,questionnaireID);
    }
    
    @PostMapping("/qol")
	public String postQOL(@RequestBody QOLResponseFromApp response) throws IllegalArgumentException, IllegalAccessException {
    	try {
    		System.out.println(response.toString());	
    	}
    	catch(Exception e) {
    		System.out.println(e);
    		return "failure";
    	}
    	return "success";
    }
}
