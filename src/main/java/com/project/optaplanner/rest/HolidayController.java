package com.project.optaplanner.rest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.optaplanner.domain.Holiday;
import com.project.optaplanner.service.HolidayService;
import com.project.optaplanner.service.TeamService;
import com.project.optaplanner.service.WorkCalendarService;

@RestController
public class HolidayController {

	@Autowired
	private HolidayService service;
	@Autowired
	private TeamService teamService;
	@Autowired
	private WorkCalendarService workCalendarService;
	
	@RequestMapping(value = "/holiday", method = RequestMethod.POST)
	public Map<String, Object> postHoliday(@RequestParam("file") MultipartFile file) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(file.isEmpty()) {
			map.put("success", false);
	        map.put("message","Oups vous avez pas de fichier");
		}else {
			map = service.AddMultiple(file);
		}
		return map;
	}
	
	@RequestMapping(value = "/holiday", method = RequestMethod.GET)
	public List<Holiday> add() {
		return service.getHoliday();
	}
	
	@RequestMapping(value = "/team", method = RequestMethod.POST)
	public Map<String, Object> saveTeam(@RequestBody Map<String, Object> map){
		return teamService.addTeam(map);
	}

	@RequestMapping(value = "/workingDay", method = RequestMethod.GET)
	public Map<String, Object>  workingDay(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("size", workCalendarService.generateWorkingDay().size());
		map.put("list", workCalendarService.generateWorkingDay());
		return map;
	}
	
	
}
