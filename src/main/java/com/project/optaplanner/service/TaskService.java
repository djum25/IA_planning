package com.project.optaplanner.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.optaplanner.domain.Task;
import com.project.optaplanner.persistence.TaskRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository repository;
	
	public Map<String, Object> add(Task task) {
		Map<String, Object> map = new HashedMap<String, Object>();
		
		repository.save(task);
		if(task == null) {
			map.put("success", false);
			map.put("message", "Enregistrement echouer");
		}else {
			map.put("success", true);
			map.put("message", "Enregistrement reussit");
			map.put("task", task);
		}
		
		return map;
	}
	
	public Map<String, Object> addMultiple(String phase, String type,LocalDate delivery,String factory) {
		Map<String, Object> map = new HashedMap<String, Object>();
		
		if(phase.equals("EL")) {
			List<Task> tasks  =  createELTasks(type, delivery,factory);
			repository.saveAll(tasks);
			map.put("success", true);
			map.put("nombre", tasks.size());
			map.put("tasks", tasks);
		}
		else if(phase.equals("HL")) {
			List<Task> tasks  =  createHLTasks(type, delivery,factory);
			repository.saveAll(tasks);
			map.put("success", true);
			map.put("nombre", tasks.size());
			map.put("tasks", tasks);
		}
		else {
			List<Task> tasks  =  createPROTOTasks(type, delivery,factory);
			repository.saveAll(tasks);
			map.put("success", true);
			map.put("nombre", tasks.size());
			map.put("tasks", tasks);
		}
		
		
		
		return map;
	}
	
	private List<Task> createELTasks(String type,LocalDate delivery,String factory){
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(new Task("Définition archi",delivery.plusWeeks(-46) ,delivery.plusWeeks(-44) ,10,1,0,"EL",type,"M1"));
		tasks.add(new Task("Initialisation SI",delivery.plusWeeks(-44),delivery.plusWeeks(-42),10,2,0,"EL",type,"M2"));
		tasks.add(new Task("Cahier des charges",delivery.plusWeeks(-42),delivery.plusWeeks(-40),10,3,0,"EL",type,"M3"));
		tasks.add(new Task("Offi système",delivery.plusWeeks(-40),delivery.plusWeeks(-33),35,4,0,"EL",type,"M4"));
		tasks.add(new Task("Chiffrage",delivery.plusWeeks(-33),delivery.plusWeeks(-27),30,5,0,"EL",type,"M5"));
		tasks.add(new Task("Définition",delivery.plusWeeks(-27),delivery.plusWeeks(-17),50,6,0,"EL",type,"M6"));
		tasks.add(new Task("Commande des pièces",delivery.plusWeeks(-17),delivery.plusWeeks(-7),50,7,0,"EL",type,"M7"));
		tasks.add(new Task("Réception/logistique",delivery.plusWeeks(-7),delivery.plusWeeks(-5),10,8,0,"EL",type,"M8"));
		tasks.add(new Task("strumentation",delivery.plusWeeks(-5),delivery.plusWeeks(-3),10,9,0,"EL",type,"M9"));
		tasks.add(new Task("Montage",delivery.plusWeeks(-3),delivery.plusWeeks(-1),10,10,0,"EL",type,factory));
		tasks.add(new Task("Rodage/courbage",delivery.plusWeeks(-1),delivery,5,11,0,"EL",type,"M11"));
		
		return tasks;
	}
	
	private List<Task> createHLTasks(String type,LocalDate delivery, String factory){
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(new Task("Définition archi",delivery.plusWeeks(-36),delivery.plusWeeks(-34),10,1,0,"EL",type,"M1"));
		tasks.add(new Task("Initialisation SI",delivery.plusWeeks(-34),delivery.plusWeeks(-31),15,2,0,"EL",type,"M2"));
		tasks.add(new Task("Cahier des charges",delivery.plusWeeks(-31),delivery.plusWeeks(-29),10,3,0,"EL",type,"M3"));//162 134
		tasks.add(new Task("Offi système",delivery.plusWeeks(-29),delivery.plusWeeks(-20),45,4,0,"EL",type,"M4"));//134 106
		tasks.add(new Task("Chiffrage",delivery.plusWeeks(-20),delivery.plusWeeks(-17),15,5,0,"EL",type,"M5"));//106 79
		tasks.add(new Task("Définition",delivery.plusWeeks(-17),delivery.plusWeeks(-9),40,6,0,"EL",type,"M6"));//79 53
		tasks.add(new Task("Commande des pièces",delivery.plusWeeks(-9),delivery.plusWeeks(-5),20,7,0,"EL",type,"M7"));//53 32 
		tasks.add(new Task("Réception/logistique",delivery.plusWeeks(-5),delivery.plusWeeks(-3),10,8,0,"EL",type,"M8"));//32 13
		tasks.add(new Task("strumentation",delivery.plusWeeks(-3),delivery.plusWeeks(-2),5,9,0,"EL",type,"M9"));
		tasks.add(new Task("Montage",delivery.plusWeeks(-2),delivery.plusWeeks(-1),5,10,0,"EL",type,factory));
		tasks.add(new Task("Rodage/courbage",delivery.plusWeeks(-1),delivery,5,11,0,"EL",type,"M11"));
		
		return tasks;
	}
	
	private List<Task> createPROTOTasks(String type,LocalDate delivery,String factory){
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(new Task("Définition archi",delivery.plusWeeks(-25),delivery.plusWeeks(-23),10,1,0,"EL",type,"M1"));//221 191
		tasks.add(new Task("Initialisation SI",delivery.plusWeeks(-23),delivery.plusWeeks(-20),15,2,0,"EL",type,"M2"));//191 162
		tasks.add(new Task("Cahier des charges",delivery.plusWeeks(-20),delivery.plusWeeks(19),5,3,0,"EL",type,"M3"));//162 134
		tasks.add(new Task("Offi système",delivery.plusWeeks(-19),delivery.plusWeeks(-17),10,4,0,"EL",type,"M4"));//134 106
		tasks.add(new Task("Chiffrage",delivery.plusWeeks(-17),delivery.plusWeeks(-16),5,5,0,"EL",type,"M5"));//106 79
		tasks.add(new Task("Définition",delivery.plusWeeks(-16),delivery.plusWeeks(-14),10,6,0,"EL",type,"M6"));// 79 53
		tasks.add(new Task("Commande des pièces",delivery.plusWeeks(-14),delivery.plusWeeks(-13),5,7,0,"EL",type,"M7"));//53 32
		tasks.add(new Task("Réception/logistique",delivery.plusWeeks(13),delivery.plusWeeks(-4),45,8,0,"EL",type,"M8"));//32 13
		tasks.add(new Task("strumentation",delivery.plusWeeks(-4),delivery.plusWeeks(-2),10,9,0,"EL",type,"M9"));// 13 9
		tasks.add(new Task("Montage",delivery.plusWeeks(-2),delivery.plusWeeks(-1),5,10,0,"EL",type,factory));// 9 2
		tasks.add(new Task("Rodage/courbage",delivery.plusWeeks(-1),delivery,5,11,0,"EL",type,"M11"));// 2 0
		
		return tasks;
	}
	
	/*
	 * private List<Task> createNDEV_HAB_Task(){ List<Task> tasks = new
	 * ArrayList<Task>(); return tasks; }
	 */
	

	
	public void deleteAll() {
		repository.deleteAll();
	}
}
