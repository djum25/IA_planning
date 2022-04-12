package com.project.optaplanner.service;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.optaplanner.domain.Team;
import com.project.optaplanner.persistence.TeamRepository;

@Service
public class TeamService {

	@Autowired
	private TeamRepository teamRepository;
	
	public Map<String, Object> addTeam(Map<String, Object> map) {
		Map<String, Object> returnMap = new HashedMap<String, Object>();
		
		int x = toInt(map.get("x"));
		int y = toInt(map.get("y"));
		int z = toInt(map.get("z"));
		
		for(int i= 0; i<x; i++)
		teamRepository.save(new Team("M10","X",10));
		for(int i= 0; i<y; i++)
		teamRepository.save(new Team("M10","Y",10));
		for(int i= 0; i<z; i++)
		teamRepository.save(new Team("M10","Z",10));
		
		returnMap.put("x", x+" Ressource ajouté pour Tremery");
		returnMap.put("y", y+" Ressource ajouté pour Atelier Carrieres sous Poissy");
		returnMap.put("z", z+" Ressource ajouté pour Valencienne");
		
		return returnMap;
	}
	
	public void deleteALL() {
		teamRepository.deleteAll();
	}
	
	public void createTeams() {
		teamRepository.save(new Team("M1","M1",1));
        teamRepository.save(new Team("M2","M2",2));
        teamRepository.save(new Team("M3","M3",3));
        teamRepository.save(new Team("M4","M4",4));
        teamRepository.save(new Team("M5","M5",5));
        teamRepository.save(new Team("M6","M6",6));
        teamRepository.save(new Team("M7","M7",7));
        teamRepository.save(new Team("M8","M8",8));
        teamRepository.save(new Team("M9","M9",9));
	}
	
	private int toInt(Object object) {
		return Integer.parseInt(object.toString());
	}
}
