package com.project.optaplanner.service;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.optaplanner.domain.Resource;
import com.project.optaplanner.domain.Type;
import com.project.optaplanner.persistence.ResourceRepository;

@Service
public class ResourceService {

	@Autowired
	private ResourceRepository resourceRepository;

	public Resource add() {
		Resource r = new Resource();
		r.setCapacity(3);
		r.setType(Type.MOTOR);
		r.setWorkDay(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY)));

		Resource resource = resourceRepository.save(r);

		return resource;
	}

	public Map<String, Object> addMultiple(MultipartFile file) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (file.isEmpty()) {
			map.put("success", false);
			map.put("message", "Oups vous avez pas de fichier");
		} else {
			try {
				XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
				XSSFSheet sheet = workbook.getSheetAt(0);
				Iterator<Row> rowIterator = sheet.iterator();
				List<Resource> resources = new ArrayList<Resource>();
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
						if (row.getCell(0).getCellType().toString().equals("NUMERIC") 
								&& row.getCell(1).getCellType().toString().equals("STRING")
								&& (row.getCell(2).getCellType().toString().equals("NUMERIC")
								||row.getCell(2).getCellType().toString().equals("FORMULA"))) {
							Resource resource = new Resource(ToInt(row.getCell(0).getNumericCellValue()),
									toType(row.getCell(1).getStringCellValue()),
									row.getCell(2).getLocalDateTimeCellValue().toLocalDate());
							resources.add(resource);
					}
				}
				resourceRepository.saveAll(resources);
				map.put("success", true);
				map.put("message", "Ajouter avec success");
				map.put("nombre", resources.size());
				workbook.close();
			} catch (IOException e) {
				map.put("success", true);
				map.put("message", "Oups y'a une erreurs lors de la lecture");
				map.put("detail", e.getCause());
				map.put("nombre", 0);
				e.printStackTrace();
			}
		}

		return map;
	}

	public List<Resource> getResource() {
		return resourceRepository.findAll();
	}

	private int ToInt(Number val) {
		return val.intValue();
	}

	private Type toType(String str) {
		if (str.equals("MOTOR")) {
			return Type.MOTOR;
		} else if (str.equals("manuel")) {
			return Type.MANUAL_BOX;
		} else {
			return Type.AUTOMATIC_BOX;
		}
	}
}
