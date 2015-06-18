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

import com.ncp.entity.Company;
import com.ncp.forms.DatatablesForm;
import com.ncp.repositories.CompanyRepository;


@Controller
@RequestMapping("/admin/company/*")
public class AdminCompanyController {
	
	@Autowired
	private CompanyRepository companyRepository;

	@RequestMapping(value="index", method=RequestMethod.GET)
	public ModelAndView loginScreen(HttpServletRequest request) throws Exception{
		Map<String, Object> model = new HashMap<String, Object>();
		
		return new ModelAndView("company.index", model);
	}
	
	@RequestMapping(value="loadCompanyTable")
	public @ResponseBody DatatablesForm loadRecordsTable(HttpServletRequest request) throws Exception{
		Map<String, Object> model = new HashMap<String, Object>();
		
		List<String[]> rows = new ArrayList<String[]>();
		
		List<Company> companies = companyRepository.findAll();
		
		for(Company company : companies){
			List<String> curRow = new ArrayList<String>(); 
			curRow.add(company.getId().toString());
			curRow.add(company.getName());
			curRow.add(company.getPhone());
			curRow.add(company.getEmail());
			rows.add(curRow.toArray(new String[]{}));
		}
		
		return new DatatablesForm(null, null, 0, rows.toArray(new Object[]{}));
	}
	
	@RequestMapping(value="saveNewCompany")
	public ResponseEntity<String> saveNewCompany(@RequestParam String companyName, @RequestParam String companyPhone,@RequestParam String companyEmail) throws Exception{
		try{
			Company company = new Company();
			company.setName(companyName);
			company.setPhone(companyPhone);
			company.setEmail(companyEmail);
			company.setCreatedBy("NCP");
			company.setCreatedDate(DateTime.now());
			company.setLastModifiedBy("NCP");
			company.setLastModifiedDate(DateTime.now());
			company.setVersion(0L);
			
			companyRepository.save(company);
			
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
	
}
