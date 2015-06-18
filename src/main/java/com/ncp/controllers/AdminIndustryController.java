package com.ncp.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ncp.entity.Category;
import com.ncp.entity.Industry;
import com.ncp.forms.DatatablesForm;
import com.ncp.repositories.IndustryRepository;


@Controller
@RequestMapping("/admin/industry/*")
public class AdminIndustryController {
	
	@Autowired
	private IndustryRepository industryRepository;

	@RequestMapping(value="index", method=RequestMethod.GET)
	public ModelAndView loginScreen(HttpServletRequest request) throws Exception{
		Map<String, Object> model = new HashMap<String, Object>();
		
		return new ModelAndView("industry.index", model);
	}
	
	@RequestMapping(value="loadIndustryTable")
	public @ResponseBody DatatablesForm loadIndustryTable(HttpServletRequest request) throws Exception{
		Map<String, Object> model = new HashMap<String, Object>();
		
		List<String[]> rows = new ArrayList<String[]>();
		
		List<Industry> industries = industryRepository.findAll();
		
		for(Industry industry : industries){
			List<String> curRow = new ArrayList<String>(); 
			curRow.add(industry.getId().toString());
			curRow.add(industry.getIndustry());
			rows.add(curRow.toArray(new String[]{}));
		}
		
		return new DatatablesForm(null, null, 0, rows.toArray(new Object[]{}));
	}

	
	@RequestMapping(value="saveNewIndustry")
	public ResponseEntity<String> saveNewIndustry(@RequestParam String industry) throws Exception{
		try{
			Industry ind = new Industry();
			ind.setIndustry(industry);
			ind.setCreatedBy("NCP");
			ind.setCreatedDate(DateTime.now());
			ind.setLastModifiedBy("NCP");
			ind.setLastModifiedDate(DateTime.now());
			ind.setVersion(0L);
			
			industryRepository.save(ind);
			
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
}
