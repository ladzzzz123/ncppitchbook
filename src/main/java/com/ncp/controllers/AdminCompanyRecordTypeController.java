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
import com.ncp.entity.CompanyRecordType;
import com.ncp.forms.DatatablesForm;
import com.ncp.repositories.CompanyRecordTypeRepository;


@Controller
@RequestMapping("/admin/companyRecordType/*")
public class AdminCompanyRecordTypeController {
	
	@Autowired
	private CompanyRecordTypeRepository companyRecordTypeRepository;

	@RequestMapping(value="index", method=RequestMethod.GET)
	public ModelAndView loginScreen(HttpServletRequest request) throws Exception{
		Map<String, Object> model = new HashMap<String, Object>();
		
		return new ModelAndView("companyRecordType.index", model);
	}
	
	@RequestMapping(value="loadCompanyRecordTypeTable")
	public @ResponseBody DatatablesForm loadRecordsTable(HttpServletRequest request) throws Exception{
		Map<String, Object> model = new HashMap<String, Object>();
		
		List<String[]> rows = new ArrayList<String[]>();
		
		List<CompanyRecordType> companyRecordTypes = companyRecordTypeRepository.findAll();
		
		for(CompanyRecordType type : companyRecordTypes){
			List<String> curRow = new ArrayList<String>(); 
			curRow.add(type.getId().toString());
			curRow.add(type.getCompanyRecordType());
			rows.add(curRow.toArray(new String[]{}));
		}
		
		return new DatatablesForm(null, null, 0, rows.toArray(new Object[]{}));
	}
	
	@RequestMapping(value="saveNewCompanyRecordType")
	public ResponseEntity<String> saveNewCompanyRecordType(@RequestParam String companyRecordType) throws Exception{
		try{
			CompanyRecordType crt = new CompanyRecordType();
			crt.setCompanyRecordType(companyRecordType);
			crt.setCreatedBy("NCP");
			crt.setCreatedDate(DateTime.now());
			crt.setLastModifiedBy("NCP");
			crt.setLastModifiedDate(DateTime.now());
			crt.setVersion(0L);
			
			companyRecordTypeRepository.save(crt);
			
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
}
