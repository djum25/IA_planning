package com.project.optaplanner.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.optaplanner.domain.Holiday;
import com.project.optaplanner.persistence.HolidayRepository;

@Service
public class HolidayService {

	@Autowired
	private HolidayRepository repository;
	
	public Map<String, Object> AddMultiple(MultipartFile file) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (file.isEmpty()) {
			map.put("success", false);
			map.put("message", "Oups vous avez pas de fichier");
		} else {
			try {
				XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
				XSSFSheet sheet = workbook.getSheetAt(1);
				Iterator<Row> rowIterator = sheet.iterator();
				List<Holiday> holidays = new ArrayList<Holiday>();
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					if((row.getCell(0).getCellType().toString().equals("NUMERIC")
							||row.getCell(0).getCellType().toString().equals("FORMULA")) && 
							row.getCell(1).getCellType().toString().equals("STRING")){
						Holiday holiday = new Holiday();
						holiday.setDay(row.getCell(0).getLocalDateTimeCellValue().toLocalDate());
						holiday.setDescription(row.getCell(1).getStringCellValue());
						holidays.add(holiday);
					}
				}
				repository.saveAll(holidays);
				map.put("success", true);
				map.put("message", "Ajouter avec success");
				map.put("nombre", holidays.size());
				workbook.close();
			}catch (Exception e) {
				map.put("success", true);
				map.put("message", "Oups y'a une erreurs lors de la lecture");
				map.put("detail", e.getCause());
				map.put("nombre", 0);
				e.printStackTrace();
			}
		}
		return map;
	}
	
	public List<Holiday> getHoliday() {
		return repository.findAll();
	}

	public List<LocalDate> getHolidaysDate() {
		return repository.findAll().stream().map(h->h.getDay()).collect(Collectors.toList());
	}

	public int dayToSkip(LocalDate startDate,LocalDate endDate) {
		 Long days = startDate.datesUntil(endDate).count();
		 return days.intValue();
	}
}
