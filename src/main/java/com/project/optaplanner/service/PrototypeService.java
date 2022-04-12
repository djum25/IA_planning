package com.project.optaplanner.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.optaplanner.domain.Prototype;
import com.project.optaplanner.persistence.PrototypeRepository;

@Service
public class PrototypeService {

	
	@Autowired
	private PrototypeRepository repository;
	@Autowired
	private TaskService service;
	@Autowired
	private TeamService teamService;
	
	public Map<String, Object> add(Prototype prototype){
		Map<String,  Object> map = new HashMap<String, Object>();
		
		if (control(prototype).equals("OK")) {
			Prototype resultPrototype = repository.save(prototype);
			System.out.println("usine : "+prototype.getUsine());
			if (resultPrototype == null) {
				map.put("success", false);
				map.put("message", "L'enregistrement a échoué, éssayez encore");
			}else {
				Map<String,  Object> protypeTask = service.addMultiple(prototype.getPhase(), prototype.getType(), prototype.getDeliveryDate(),prototype.getUsine());
				teamService.createTeams();
				map.put("success", true);
				map.put("prototype", resultPrototype);
				map.put("taskResult",protypeTask);
				map.put("message", "Enregistrement réussit");
			}
		}else {
			map.put("success", false);
			map.put("message", control(prototype));
		}
		return map;
	}
	
	public Map<String, Object> findAll() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<Prototype> prototypes = repository.findAll();
		map.put("success", true);
		map.put("nombre", prototypes.size());
		map.put("prototypes", prototypes);
		
		return map;
	}
	
	public Map<String, Object> delete(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		 
		repository.deleteById(id);
		map.put("success", true);
		map.put("message", "Supprimer avec succé");
		
		return map;
	}
	
	private String control(Prototype prototype) {
		if(prototype.getPhase().equals(null))
			return " La phase ne peut etre null";
		else if(prototype.getType().equals(null))
			return " Le type ne peut etre null";
		 else if(prototype.getDeliveryDate().equals(null)) 
			  return " La date de livraison ne peut etre null";
		else if (prototype.getUsine().equals(null))  
			return "L'usine n'est pas renseigné";
		else
			return "OK";
	}
	
	public void deleteAll() {
		service.deleteAll();
		repository.deleteAll();
		teamService.deleteALL();
	}
}
