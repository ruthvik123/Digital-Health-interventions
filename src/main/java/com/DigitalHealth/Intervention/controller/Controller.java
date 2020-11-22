package com.DigitalHealth.Intervention.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.DigitalHealth.Intervention.Service.QOLService;
import com.DigitalHealth.Intervention.model.QOLQuestionnaire;


@RestController
public class Controller {
	
	@Autowired
	QOLService	qolService;

    @GetMapping("/")
    public String test(){
        return "app started...";
    }///
    
    @GetMapping("/qol/{userID}")
    public QOLQuestionnaire t1(@PathVariable String userID) { 
    	return qolService.getQuestionnaire(userID);
    }
}
