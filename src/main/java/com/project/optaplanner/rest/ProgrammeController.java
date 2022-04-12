package com.project.optaplanner.rest;

import org.optaplanner.core.api.score.ScoreManager;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.api.solver.SolverStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.optaplanner.domain.Programme;
import com.project.optaplanner.persistence.ProgrammeRepository;

@RestController
@RequestMapping("/programme")
public class ProgrammeController {

	@Autowired
	private ProgrammeRepository programmeRepository;
	@Autowired
	private SolverManager<Programme, Long> solverManager;
	@Autowired
	private ScoreManager<Programme, HardSoftScore> scoreManager;
	
	@GetMapping("/solve")
	public Programme getProgramme() {
		SolverStatus solverStatus = getSolverStatus();
    	Programme solution = programmeRepository.findById(ProgrammeRepository.SINGLETON_TIME_TABLE_ID);
        scoreManager.updateScore(solution); // Sets the score
        solution.setSolverStatus(solverStatus);
        return solution;
	}
	
    @PostMapping("/solve")
    public void solve() {
        solverManager.solveAndListen(ProgrammeRepository.SINGLETON_TIME_TABLE_ID,
        		programmeRepository::findById,
        		programmeRepository::save);
    }
	
    public SolverStatus getSolverStatus() {
        return solverManager.getSolverStatus(ProgrammeRepository.SINGLETON_TIME_TABLE_ID);
    }
    
    @PostMapping("/stopSolving")
    public void stopSolving() {
        solverManager.terminateEarly(ProgrammeRepository.SINGLETON_TIME_TABLE_ID);
    }
}
