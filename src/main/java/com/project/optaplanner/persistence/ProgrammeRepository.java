package com.project.optaplanner.persistence;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.optaplanner.domain.Programme;
import com.project.optaplanner.domain.Task;
import com.project.optaplanner.service.HolidayService;

@Service
@Transactional
public class ProgrammeRepository {

	public static final Long SINGLETON_TIME_TABLE_ID = 1L;
	
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private PrototypeRepository prototypeRepository;
	@Autowired
	private WorkCalendarRepository workCalendarRepository;
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private HolidayService holidayService;
	
	public Programme findById(Long id) {
		if (!SINGLETON_TIME_TABLE_ID.equals(id)) {
            throw new IllegalStateException("There is no timeTable with id (" + id + ").");
        }
		return new Programme(workCalendarRepository.findAll().get(0),prototypeRepository.findAll(),
		taskRepository.findAll(),teamRepository.findAll(),holidayService.getHolidaysDate());
	}
	
	public void save(Programme programme) {
		for(Task task:programme.getTaskList()) {
			taskRepository.save(task);
		}
	}
}
