package com.project.optaplanner.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.optaplanner.domain.Prototype;
import com.project.optaplanner.service.PrototypeService;

@RestController
public class PrototypeController {

	@Autowired
	private PrototypeService service;
	
	@RequestMapping(value = "/prototype", method = RequestMethod.POST)
	public Map<String, Object> post(@RequestBody Prototype prototype) {
		return service.add(prototype);
	}
	
	@RequestMapping(value = "/prototype", method = RequestMethod.GET)
	public Map<String, Object> get(){
		return service.findAll();
	}
	
	@RequestMapping(value = "/prototype/{id}", method = RequestMethod.DELETE)
	public Map<String, Object> delete(@PathVariable("id") Long id){
		return service.delete(id);
	}
	
	@RequestMapping(value = "/prototype/delete", method = RequestMethod.GET)
	public boolean deleteAll(){
		service.deleteAll();
		return true;
	}
}
