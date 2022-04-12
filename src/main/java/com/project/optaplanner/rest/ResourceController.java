package com.project.optaplanner.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.optaplanner.domain.Resource;
import com.project.optaplanner.service.ResourceService;

@RestController
public class ResourceController {

	@Autowired
	private ResourceService resourceService;
	
	@RequestMapping(value = "/resource", method = RequestMethod.GET)
	public List<Resource> add() {
		return resourceService.getResource();
	}
	
	@RequestMapping(value = "/resource", method = RequestMethod.POST)
	public Map<String, Object> importData(@RequestParam("file") MultipartFile file){
		Map<String, Object> map = new HashMap<String, Object>();
		if(file.isEmpty()) {
			map.put("success", false);
	        map.put("message","Oups vous avez pas de fichier");
		}else {
			map = resourceService.addMultiple(file);
		}
		return map;
	}
	
	
 }
